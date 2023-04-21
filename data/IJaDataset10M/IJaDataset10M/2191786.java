package com.ext.portlet.solicitud.forms;

import org.apache.struts.action.ActionForm;

public class CrearActividadForm extends ActionForm {

    private String codigoActividad;

    private String nombreUniversidad;

    private String nombreActividad;

    private String lstCiclo;

    private String objetivosActividad;

    private String lstEspecialidadDocente;

    private String fechainicioActividad;

    private String fechafinActividad;

    private String mtlDiasHorasDictado;

    private String mliRecursosActividad;

    /**
	    *
	    */
    public CrearActividadForm() {
        super();
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public String getLstEspecialidadDocente() {
        return lstEspecialidadDocente;
    }

    public void setLstEspecialidadDocente(String lstEspecialidadDocente) {
        this.lstEspecialidadDocente = lstEspecialidadDocente;
    }

    public String getFechainicioActividad() {
        return fechainicioActividad;
    }

    public void setFechainicioActividad(String fechainicioActividad) {
        this.fechainicioActividad = fechainicioActividad;
    }

    public String getFechafinActividad() {
        return fechafinActividad;
    }

    public void setFechafinActividad(String fechafinActividad) {
        this.fechafinActividad = fechafinActividad;
    }

    public String getObjetivosActividad() {
        return objetivosActividad;
    }

    public void setObjetivosActividad(String objetivosActividad) {
        this.objetivosActividad = objetivosActividad;
    }

    public String getMtlDiasHorasDictado() {
        return mtlDiasHorasDictado;
    }

    public void setMtlDiasHorasDictado(String mtlDiasHorasDictado) {
        this.mtlDiasHorasDictado = mtlDiasHorasDictado;
    }

    public String getMliRecursosActividad() {
        return mliRecursosActividad;
    }

    public void setMliRecursosActividad(String mliRecursosActividad) {
        this.mliRecursosActividad = mliRecursosActividad;
    }

    public String getLstCiclo() {
        return lstCiclo;
    }

    public void setLstCiclo(String lstCiclo) {
        this.lstCiclo = lstCiclo;
    }

    public String getCodigoActividad() {
        return codigoActividad;
    }

    public void setCodigoActividad(String codigoActividad) {
        this.codigoActividad = codigoActividad;
    }

    public void setNombreUniversidad(String nombreUniversidad) {
        this.nombreUniversidad = nombreUniversidad;
    }

    public String getNombreUniversidad() {
        return nombreUniversidad;
    }
}
