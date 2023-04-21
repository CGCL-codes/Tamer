package com.inout.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author pablo
 */
public class tarjetaDTO implements Serializable {

    private String id;

    private String descripcion;

    private Short tipo;

    private Date FechaEntrega;

    private Date FechaDevolucion;

    private Boolean activa;

    public tarjetaDTO(String id, String descripcion, Short tipo, Date FechaEntrega, Date FechaDevolucion, Boolean activa) {
        this.id = id;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.FechaEntrega = FechaEntrega;
        this.FechaDevolucion = FechaDevolucion;
        this.activa = activa;
    }

    public tarjetaDTO(String id, String Descripcion, Short tipo, Date FechaEntrega, Boolean activa) {
        this.id = id;
        this.tipo = tipo;
        this.FechaEntrega = FechaEntrega;
        this.activa = activa;
        this.descripcion = Descripcion;
    }

    public tarjetaDTO(String id) {
        this.id = id;
    }

    public Date getFechaDevolucion() {
        return FechaDevolucion;
    }

    public void setFechaDevolucion(Date FechaDevolucion) {
        this.FechaDevolucion = FechaDevolucion;
    }

    public Date getFechaEntrega() {
        return FechaEntrega;
    }

    public void setFechaEntrega(Date FechaEntrega) {
        this.FechaEntrega = FechaEntrega;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
    }
}
