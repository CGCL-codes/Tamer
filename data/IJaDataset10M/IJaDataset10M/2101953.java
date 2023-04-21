package com.mci.escuelas.business;

import java.io.Serializable;
import java.util.Date;

/**
 * @hibernate.class table="esc_materia"
 * @author fervincent
 *
 */
public class Materia implements Serializable {

    protected java.lang.Integer id;

    private String nombre;

    private int estado;

    /**
	 * 
	 * @hibernate.id column="materia_id" type="int"
	 *               generator-class="com.common.persistence.secuenciales.MaxIdentifierGenerator"
	 * @hibernate.generator-param name="table" value="esc_materia"
	 * @hibernate.generator-param name="column" value="materia_id"
	 * 
	 */
    public java.lang.Integer getId() {
        return id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    /**
	 * @hibernate.property 
	 */
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
	 * @hibernate.property type="int"
	 */
    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public boolean equals(Object obj) {
        Materia materiaTmp = (Materia) obj;
        if (materiaTmp != null && getId() != null && materiaTmp.getId() != null && getId().equals(materiaTmp.getId())) {
            return true;
        }
        return false;
    }

    public String toString() {
        return getNombre();
    }
}
