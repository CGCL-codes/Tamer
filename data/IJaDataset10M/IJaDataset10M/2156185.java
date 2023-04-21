package com.ecomponentes.hibernate.teste;

import java.io.Serializable;

/**
 * A class that represents a row in the 'tb_teste' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class TbTeste extends AbstractTbTeste implements Serializable {

    /**
     * Simple constructor of TbTeste instances.
     */
    public TbTeste() {
    }

    /**
     * Constructor of TbTeste instances given a composite primary key.
     * @param id
     */
    public TbTeste(TbTesteKey id) {
        super(id);
    }
}
