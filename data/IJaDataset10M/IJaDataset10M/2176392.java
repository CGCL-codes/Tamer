package net.sourceforge.trafalgar.androidtrafalgar;

import java.io.Serializable;

/**
 * Información del mensaje recibido
 */
public class Mensaje implements Serializable {

    private static final long serialVersionUID = 4236060813418330231L;

    private String tipo;

    private String remitente;

    private String destinatario;

    private String dato;

    private String fila;

    private String columna;

    /**
     * Constructor
     * 
     * @param tipo el tipo del mensaje
     */
    public Mensaje(String tipo) {
        super();
        this.tipo = tipo;
    }

    /**
     * @return el tipo del mensaje
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Devuelve el remitente del mensaje
     * @return el remitente
     */
    public String getRemitente() {
        return remitente;
    }

    /**
     * Establece el remitente del mensaje
     * 
     * @param remitente el remitente a establecer
     */
    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    /**
     * Devuelve el destinatario del mensaje
     * 
     * @return el destinatario
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * Establece el destinatario del mensaje
     * 
     * @param destinatario el destinatario a establecer
     */
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * Devuelve el dato 
     * 
     * @return el dato
     */
    public String getDato() {
        return dato;
    }

    /**
     * Establece el dato del mensaje (para los mensajes que lo emplean)
     * 
     * @param dato el dato a establecer
     */
    public void setDato(String dato) {
        this.dato = dato;
    }

    /**
     * Devuelve la fila (para los mensajes de tipo disparo o resultado)
     * 
     * @return la fila
     */
    public String getFila() {
        return fila;
    }

    /**
     * Establece la fila
     * 
     * @param fila la fila a establecer
     */
    public void setFila(String fila) {
        this.fila = fila;
    }

    /**
     * Devuelve la columna (para los mensajes de tipo disparo o resultado)
     * 
     * @return la columna
     */
    public String getColumna() {
        return columna;
    }

    /**
     * Establece la columna
     * 
     * @param columna la columna a establecer
     */
    public void setColumna(String columna) {
        this.columna = columna;
    }
}
