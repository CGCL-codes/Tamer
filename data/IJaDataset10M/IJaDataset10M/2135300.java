package com.siasal.documentos.commons;

import com.common.to.BaseTO;
import com.siasal.documentos.business.Ejemplar;

/**
 * Metodo que contiene el estado del ejemplar y un comentario en caso de estar
 * prestado
 * 
 * @author fervincent
 * 
 */
public class EstadoEjemplarTO extends BaseTO {

    private Ejemplar ejemplar;

    private String comentario;

    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    public void setEjemplar(Ejemplar ejemplar) {
        this.ejemplar = ejemplar;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public EstadoEjemplarTO(Ejemplar ejemplar, String comentario) {
        super();
        this.ejemplar = ejemplar;
        this.comentario = comentario;
    }
}
