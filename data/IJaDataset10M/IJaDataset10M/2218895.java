package com.miniTwitter.domain;

public class SolicitudRelacion {

    private Usuario usuarioSoliciante;

    public SolicitudRelacion(Usuario usuarioSolicitante) {
        this.usuarioSoliciante = usuarioSolicitante;
    }

    public boolean esDeUsuario(String userName) {
        return (this.usuarioSoliciante.getUserName().equals(userName));
    }

    public Usuario getUsuarioSoliciante() {
        return usuarioSoliciante;
    }

    public void setUsuarioSoliciante(Usuario usuarioSoliciante) {
        this.usuarioSoliciante = usuarioSoliciante;
    }
}
