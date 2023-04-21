package com.ecomponentes.hibernate.tipo;

import java.io.Serializable;

/**
 * A class that represents a row in the 'tb_tipo' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class TbTipo extends AbstractTbTipo implements Serializable {

    /**
     * Simple constructor of TbTipo instances.
     */
    public TbTipo() {
    }

    /**
     * Constructor of TbTipo instances given a simple primary key.
     * @param idTipo
     */
    public TbTipo(java.lang.Integer idTipo) {
        super(idTipo);
    }
}
