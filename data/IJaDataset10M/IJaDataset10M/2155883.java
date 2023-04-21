package org.humboldt.cassia.ws;

import java.io.Serializable;

/**
 * Clase usada en el ws para enviar la informaci�n de los atributos de un metadato
 * @author Benjamin A. Rodriguez R. benjamin.a.rodriguez@gmail.com
 *
 */
public class AtributoWS implements Serializable {

    private String nombre;

    private String valor;

    /**
	 * @return the nombre
	 */
    public String getNombre() {
        return nombre;
    }

    /**
	 * @param nombre the nombre to set
	 */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
	 * @return the valor
	 */
    public String getValor() {
        return valor;
    }

    /**
	 * @param valor the valor to set
	 */
    public void setValor(String valor) {
        this.valor = valor;
    }
}
