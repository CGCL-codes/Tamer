package com.aimo.sked.modelo;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Evento {

    private int evento_id;

    private GregorianCalendar fecha;

    private String asunto;

    private String lugar;

    private String detalles;

    private ArrayList<String> asistentes;

    public Evento(String asunto, GregorianCalendar fecha, String lugar, String detalles, ArrayList<String> asistentes) {
        super();
        this.asunto = asunto;
        this.fecha = fecha;
        this.lugar = lugar;
        this.detalles = detalles;
        this.asistentes = asistentes;
    }

    public Evento(String asunto, long fecha, String lugar, String detalles, ArrayList<String> asistentes) {
        super();
        this.asunto = asunto;
        setFecha(fecha);
        this.lugar = lugar;
        this.detalles = detalles;
        this.asistentes = asistentes;
    }

    public int getEventoId() {
        return evento_id;
    }

    public void setEventoId(int evento_id) {
        this.evento_id = evento_id;
    }

    public long getFecha() {
        return fecha.getTimeInMillis();
    }

    public void setFecha(GregorianCalendar fecha) {
        this.fecha = fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = new GregorianCalendar();
        this.fecha.setTimeInMillis(fecha);
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public ArrayList<String> getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(ArrayList<String> asistentes) {
        this.asistentes = asistentes;
    }

    public String toString() {
        String cadena = "Evento: " + fecha + " - " + asunto + "\n";
        cadena += "\tLugar: " + lugar + "\n";
        cadena += "\tDetalles: " + detalles + "\n";
        cadena += "\tAsistentes: {";
        for (int i = 0; i < asistentes.size(); i++) {
            cadena += asistentes.get(1) + ", ";
        }
        cadena += "}";
        return cadena;
    }
}
