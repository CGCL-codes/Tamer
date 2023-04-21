package com.mci.escuelas.business;

import java.util.Date;

/**
 * @hibernate.class table="esc_periodo"
 * @author fervincent
 *
 */
public class Periodo {

    protected java.lang.Integer id;

    private Date fechaInicio;

    private Date fechaFin;

    private int estado;

    /**
	 * @hibernate.property type="date"
	 */
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
	 * @hibernate.property type="date"
	 */
    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
	 * 
	 * @hibernate.id column="periodo_id" type="int"
	 *               generator-class="com.common.persistence.secuenciales.MaxIdentifierGenerator"
	 * @hibernate.generator-param name="table" value="esc_periodo"
	 * @hibernate.generator-param name="column" value="periodo_id"
	 * 
	 */
    public java.lang.Integer getId() {
        return id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
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
}
