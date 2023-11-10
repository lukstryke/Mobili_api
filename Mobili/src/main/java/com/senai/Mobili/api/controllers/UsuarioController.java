package com.example.Mobili.api.controllers;


import com.example.Mobili.api.dtos.UsuarioDto;
import com.example.Mobili.api.models.UsuarioModel;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Mobili.api.repositories.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/usuario", produces = {"application/json"})
public class UsuarioController {
    UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<UsuarioModel>> listarUsuarios(){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.findAll());
    }
    @GetMapping("/{idUsuario}")
    public ResponseEntity<Object> buscarUsuario(@PathVariable(value = "idUsuario") UUID id){
        Optional<UsuarioModel> UsuarioBuscado = usuarioRepository.findById(id);
        if (UsuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao encontrado");

        }
        return ResponseEntity.status(HttpStatus.OK).body(UsuarioBuscado.get());
    }
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> criarUsuario(@ModelAttribute @Valid UsuarioDto usuarioDto){
        if (usuarioRepository.findByEmail(usuarioDto.email()) != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email ja cadastrado");

        }
        UsuarioModel novousuario = new UsuarioModel();
        BeanUtils.copyProperties(usuarioDto, novousuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(novousuario));

    }
        @PutMapping(value = "/{idUsuario}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
        public ResponseEntity<Object> editarUsuario(@PathVariable(value = "idUsuario")UUID id, @ModelAttribute @Valid UsuarioDto usuarioDto) {
            Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);
            if (usuarioBuscado.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao encontrado");
            }

            UsuarioModel usuarioBd = usuarioBuscado.get();
            BeanUtils.copyProperties(usuarioDto, usuarioBd);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.save(usuarioBd));

        }

            @DeleteMapping ("/{idUsuario}")
            public ResponseEntity<Object> deletarUsuario(@PathVariable(value = "idUsuario") UUID id){
                Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

                if(usuarioBuscado.isEmpty()){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao encontrado");
                }
                usuarioRepository.delete(usuarioBuscado.get());
                return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado com sucesso");
            }


}
