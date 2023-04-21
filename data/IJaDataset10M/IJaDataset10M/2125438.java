package edu.cebanc.spring.biblioteca.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditorialBean {

    private int id_editorial;

    @NotNull
    @Size(min = 1, max = 45)
    private String nombre;

    public int getId_editorial() {
        return id_editorial;
    }

    public void setId_editorial(int id_editorial) {
        this.id_editorial = id_editorial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
