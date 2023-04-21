package net.sf.jregression.reglas.strategies.impl;

import net.sf.jregression.reglas.strategies.OutputSerializationStrategy;

/**
 * @author mblasi
 * mailto: matias.blasi@gmail.com
 *
 * Clase que implementa una logica de serializacion de la entrada 
 */
public class ToStringOutputSerializationStrategy implements OutputSerializationStrategy {

    /**
     * Implementacion de la logica de serializacion
     * @param salida salida obtenida
     * @return salida serializada
     */
    public String moldear(Object salida) {
        if (salida != null) return salida.toString();
        return (String) salida;
    }
}
