package com.pegaprecos.model;

import java.io.Serializable;

/**
 * Esta classe representa um usu�rio do sistema.
 * 
 * @author PegaPre�os
 * 
 */
public class Usuario implements Serializable {

    private String email;

    private String nome;

    private Boolean administrador;

    public Boolean getAdministrador() {
        return administrador;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
