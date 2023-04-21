package com.baratongoweb.modelos;

import java.util.LinkedList;

/**
 *
 * @author Usuario
 */
public class Seccion {

    private LinkedList<Producto> lista;

    private String nombre;

    private String codigo;

    private String descripcion;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LinkedList<Producto> getLista() {
        return lista;
    }

    public void setLista(LinkedList<Producto> lista) {
        this.lista = lista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
