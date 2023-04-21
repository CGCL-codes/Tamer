package com.opendicom.miniRIS.entidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Procedimiento {

    private String id;

    private String nombre;

    private String modalidad;

    private int tiempo;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }
}
