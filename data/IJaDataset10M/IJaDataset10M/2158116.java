package edu.upc.condominio;

import java.util.ArrayList;
import java.util.List;

public class Comunicaciones {

    private List<Comunicacion> Comunicacion = new ArrayList<Comunicacion>();

    public List<Comunicacion> getComunicaciones() {
        return Comunicacion;
    }

    public void setComunicaciones(List<Comunicacion> comunicaciones) {
        this.Comunicacion = comunicaciones;
    }

    public void registrarComunicaciones(String Usuario, String Titulo, String Noticia, String Post) {
        Comunicacion comunica = new Comunicacion(Usuario, Titulo, Noticia, Post);
        Comunicacion.add(comunica);
    }

    public Comunicacion buscarcomunicaciones(String Titulo) throws RuntimeException {
        for (Comunicacion comunica : Comunicacion) {
            if (comunica.getTitulo().equals(Titulo)) {
                return comunica;
            }
        }
        return null;
    }

    public void eliminarComunicaciones(String Titulo) {
        Comunicacion.remove(buscarcomunicaciones(Titulo));
    }
}
