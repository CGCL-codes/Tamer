package com.ttporg.pe.dto;

import java.util.Calendar;
import java.util.Date;
import com.ttporg.pe.util.UtilCalendario;

/**
 * @author Cesar Ricardo.
 * @clase: DetallePasajeDTO.java  
 * @descripci�n descripci�n de la clase.
 * @author_web: http://frameworksjava2008.blogspot.com
                http://viviendoconjavaynomoririntentandolo.blogspot.com
 * @author_email: nombre del email del autor.
 * @author_company: nombre de la compa��a del autor.
 * @fecha_de_creaci�n: dd-mm-yyyy.
 * @fecha_de_ultima_actualizaci�n: dd-mm-yyyy.
 * @versi�n 1.0
 **/
public class DetallePasajeDTO {

    private Integer idEmpresa;

    private Integer idAgencia;

    private Integer idServicio;

    private Integer idSalida;

    private Integer idSCalendario;

    private String razonSocial;

    private String nomAgencia;

    private String nomServicio;

    private String departamentoSalida;

    private String departamentoDestino;

    private Date fechaHoraSalida;

    private Date fechaHoraLlegada;

    private Integer duracion;

    public DetallePasajeDTO() {
    }

    public Integer getDuracion() {
        UtilCalendario utilCalendario = new UtilCalendario();
        Calendar fecInicio = Calendar.getInstance();
        Calendar fecFin = Calendar.getInstance();
        this.duracion = 0;
        if ((this.fechaHoraSalida != null) && (this.fechaHoraLlegada != null)) {
            fecInicio.setTime(this.fechaHoraSalida);
            fecFin.setTime(this.fechaHoraLlegada);
            int duracionNew = utilCalendario.getHorasEntreDosFechas(fecInicio, fecFin);
            this.duracion = duracionNew;
        }
        return duracion;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public Integer getIdAgencia() {
        return idAgencia;
    }

    public Integer getIdServicio() {
        return idServicio;
    }

    public Integer getIdSalida() {
        return idSalida;
    }

    public Integer getIdSCalendario() {
        return idSCalendario;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getNomAgencia() {
        return nomAgencia;
    }

    public String getNomServicio() {
        return nomServicio;
    }

    public String getDepartamentoSalida() {
        return departamentoSalida;
    }

    public String getDepartamentoDestino() {
        return departamentoDestino;
    }

    public Date getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public Date getFechaHoraLlegada() {
        return fechaHoraLlegada;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void setIdAgencia(Integer idAgencia) {
        this.idAgencia = idAgencia;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public void setIdSalida(Integer idSalida) {
        this.idSalida = idSalida;
    }

    public void setIdSCalendario(Integer idSCalendario) {
        this.idSCalendario = idSCalendario;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public void setNomAgencia(String nomAgencia) {
        this.nomAgencia = nomAgencia;
    }

    public void setNomServicio(String nomServicio) {
        this.nomServicio = nomServicio;
    }

    public void setDepartamentoSalida(String departamentoSalida) {
        this.departamentoSalida = departamentoSalida;
    }

    public void setDepartamentoDestino(String departamentoDestino) {
        this.departamentoDestino = departamentoDestino;
    }

    public void setFechaHoraSalida(Date fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public void setFechaHoraLlegada(Date fechaHoraLlegada) {
        this.fechaHoraLlegada = fechaHoraLlegada;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }
}
