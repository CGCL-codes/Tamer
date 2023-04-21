package com.tesisutn.restsoft.dominio.articulo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.tesisutn.restsoft.dominio.interfaces.ObjetoDeDominio;

@Entity
public class Marca implements ObjetoDeDominio {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
