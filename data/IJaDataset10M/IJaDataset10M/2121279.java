package com.museum4j.modelo;

import java.io.Serializable;
import java.util.*;

public class Recorrido extends Contenido implements Serializable {

    private List<Parada> paradas = new LinkedList();

    public List<Parada> getParadas() {
        return this.paradas;
    }

    public void setParadas(List<Parada> paradas) {
        this.paradas = paradas;
    }

    private Boolean enVistaMovil;

    public Boolean getEnVistaMovil() {
        return this.enVistaMovil;
    }

    public void setEnVistaMovil(Boolean e) {
        this.enVistaMovil = e;
    }

    private String rutaFicheroJar;

    public String getRutaFicheroJar() {
        return this.rutaFicheroJar;
    }

    public void setRutaFicheroJar(String rutaFicheroJar) {
        this.rutaFicheroJar = rutaFicheroJar;
    }

    private Date fechaGeneracionJar;

    public Date getFechaGeneracionJar() {
        return this.fechaGeneracionJar;
    }

    public void setFechaGeneracionJar(Date fechaGeneracionJar) {
        this.fechaGeneracionJar = fechaGeneracionJar;
    }
}
