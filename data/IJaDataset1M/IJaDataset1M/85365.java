package com.utn.gestmusica.models;

import java.util.HashSet;
import java.util.Set;

public class Genero {

    private String nombre;

    private Set<CdMusica> cdsMusica = new HashSet<CdMusica>();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<CdMusica> getCdsMusica() {
        return cdsMusica;
    }

    public void setCdsMusica(Set<CdMusica> cdsMusica) {
        this.cdsMusica = cdsMusica;
    }
}
