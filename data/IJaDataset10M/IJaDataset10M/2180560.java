package org.upm;

/**
 * Clase que implemente un objeto de tipo llamada.
 * 
 * @author Rocío Sotomayor
 * @author Jonathan González
 * @version 24/04/2009
 */
public class Llamada {

    private int hora;

    private int duracion;

    private Usuario llamante;

    private Usuario llamado;

    private Tarifa tarifa;

    /**
     * Constructor de la clase
     * 
     * @param hora - hora a la que se realiza la llamada
     * @param llamante - Originador de la llamada
     * @param llamado - Destinatario de la llamada
     * @param tarifa - Tarifa de la llamada
     */
    public Llamada(int hora, Usuario llamante, Usuario llamado, Tarifa tarifa) {
        this.hora = hora;
        this.llamante = llamante;
        this.llamado = llamado;
        this.tarifa = tarifa;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getHora() {
        return hora;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setLlamante(Usuario llamante) {
        this.llamante = llamante;
    }

    public Usuario getLlamante() {
        return llamante;
    }

    public void setLlamado(Usuario llamado) {
        this.llamado = llamado;
    }

    public Usuario getLlamado() {
        return llamado;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

    public Tarifa getTarifa() {
        return tarifa;
    }
}
