package org.domain.siplacad5.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * ViewMateriasRolCoordinadorAreaId generated by hbm2java
 */
@Embeddable
public class ViewMateriasRolCoordinadorAreaId implements java.io.Serializable {

    private int idRol;

    private String nombreRol;

    private int idArea;

    private String nombreArea;

    private int idMateria;

    private String codigoMateria;

    private String nombreMateria;

    public ViewMateriasRolCoordinadorAreaId() {
    }

    public ViewMateriasRolCoordinadorAreaId(int idRol, String nombreRol, int idArea, String nombreArea, int idMateria, String codigoMateria, String nombreMateria) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.idArea = idArea;
        this.nombreArea = nombreArea;
        this.idMateria = idMateria;
        this.codigoMateria = codigoMateria;
        this.nombreMateria = nombreMateria;
    }

    @Column(name = "id_rol", nullable = false)
    @NotNull
    public int getIdRol() {
        return this.idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    @Column(name = "nombre_rol", nullable = false, length = 50)
    @NotNull
    @Length(max = 50)
    public String getNombreRol() {
        return this.nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    @Column(name = "id_area", nullable = false)
    @NotNull
    public int getIdArea() {
        return this.idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    @Column(name = "nombre_area", nullable = false, length = 100)
    @NotNull
    @Length(max = 100)
    public String getNombreArea() {
        return this.nombreArea;
    }

    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }

    @Column(name = "id_materia", nullable = false)
    @NotNull
    public int getIdMateria() {
        return this.idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    @Column(name = "codigo_materia", nullable = false, length = 10)
    @NotNull
    @Length(max = 10)
    public String getCodigoMateria() {
        return this.codigoMateria;
    }

    public void setCodigoMateria(String codigoMateria) {
        this.codigoMateria = codigoMateria;
    }

    @Column(name = "nombre_materia", nullable = false, length = 100)
    @NotNull
    @Length(max = 100)
    public String getNombreMateria() {
        return this.nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof ViewMateriasRolCoordinadorAreaId)) return false;
        ViewMateriasRolCoordinadorAreaId castOther = (ViewMateriasRolCoordinadorAreaId) other;
        return (this.getIdRol() == castOther.getIdRol()) && ((this.getNombreRol() == castOther.getNombreRol()) || (this.getNombreRol() != null && castOther.getNombreRol() != null && this.getNombreRol().equals(castOther.getNombreRol()))) && (this.getIdArea() == castOther.getIdArea()) && ((this.getNombreArea() == castOther.getNombreArea()) || (this.getNombreArea() != null && castOther.getNombreArea() != null && this.getNombreArea().equals(castOther.getNombreArea()))) && (this.getIdMateria() == castOther.getIdMateria()) && ((this.getCodigoMateria() == castOther.getCodigoMateria()) || (this.getCodigoMateria() != null && castOther.getCodigoMateria() != null && this.getCodigoMateria().equals(castOther.getCodigoMateria()))) && ((this.getNombreMateria() == castOther.getNombreMateria()) || (this.getNombreMateria() != null && castOther.getNombreMateria() != null && this.getNombreMateria().equals(castOther.getNombreMateria())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.getIdRol();
        result = 37 * result + (getNombreRol() == null ? 0 : this.getNombreRol().hashCode());
        result = 37 * result + this.getIdArea();
        result = 37 * result + (getNombreArea() == null ? 0 : this.getNombreArea().hashCode());
        result = 37 * result + this.getIdMateria();
        result = 37 * result + (getCodigoMateria() == null ? 0 : this.getCodigoMateria().hashCode());
        result = 37 * result + (getNombreMateria() == null ? 0 : this.getNombreMateria().hashCode());
        return result;
    }
}