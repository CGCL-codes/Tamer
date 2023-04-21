package com.tuzoftware.opencronos.persistencia.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>Pojo mapping table DiasFestivos</p>
 *
 * <p>Generated at Tue Aug 28 11:40:33 CDT 2007</p>
 * @author Salto-db Eclipse v1.0.15 / Hibernate pojos and xml mapping files.
 *
 */
public class Diasfestivos implements Serializable {

    /**utilizado por hibernate**/
    private static final long serialVersionUID = 1L;

    /**
     * Attribute idDia.
     */
    private Integer idDia;

    /**
     * Attribute dtFecha.
     */
    private Timestamp dtFecha;

    /**
     * Attribute descripcion.
     */
    private String descripcion;

    /**
     * @return idDia
     */
    public Integer getIdDia() {
        return idDia;
    }

    /**
     * @param idDia new value for idDia
     */
    public void setIdDia(Integer idDia) {
        this.idDia = idDia;
    }

    /**
     * @return dtFecha
     */
    public Timestamp getDtFecha() {
        return dtFecha;
    }

    /**
     * @param dtFecha new value for dtFecha
     */
    public void setDtFecha(Timestamp dtFecha) {
        this.dtFecha = dtFecha;
    }

    /**
     * @return descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion new value for descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
