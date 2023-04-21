package com.alquilacosas.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author wilson
 */
public class CalificacionDTO implements Serializable {

    private Integer idCalificacion;

    private Date fechaCalificacion;

    private Date fechaReplica;

    private Integer idUsuarioCalificador;

    private Integer idUsuarioReplicador;

    private String comentarioCalificacion;

    private String comentarioReplica;

    private Integer idPuntuacion;

    private String nombreUsuarioCalificador;

    private String nombreUsuarioReplica;

    private String nombrePuntuacion;

    private Boolean yaReplico;

    public CalificacionDTO() {
    }

    public CalificacionDTO(int idCalificacion, Date fechaCalificacion, Date fechaReplica, int idUsuarioCalificador, int idUsuarioReplicador, String comentarioCalificacion, String comentarioReplica, int idPuntuacion, String nombreUsuarioCalificador, String nombreUsuarioReplica, String nombrePuntuacion, Boolean yaReplico) {
        this.idCalificacion = idCalificacion;
        this.fechaCalificacion = fechaCalificacion;
        this.fechaReplica = fechaReplica;
        this.idUsuarioCalificador = idUsuarioCalificador;
        this.idUsuarioReplicador = idUsuarioReplicador;
        this.comentarioCalificacion = comentarioCalificacion;
        this.comentarioReplica = comentarioReplica;
        this.idPuntuacion = idPuntuacion;
        this.nombreUsuarioCalificador = nombreUsuarioCalificador;
        this.nombreUsuarioReplica = nombreUsuarioReplica;
        this.nombrePuntuacion = nombrePuntuacion;
        this.yaReplico = yaReplico;
    }

    public String getComentarioCalificacion() {
        return comentarioCalificacion;
    }

    public void setComentarioCalificacion(String comentarioCalificacion) {
        this.comentarioCalificacion = comentarioCalificacion;
    }

    public String getComentarioReplica() {
        return comentarioReplica;
    }

    public void setComentarioReplica(String comentarioReplica) {
        this.comentarioReplica = comentarioReplica;
    }

    public Date getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(Date fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }

    public Date getFechaReplica() {
        return fechaReplica;
    }

    public void setFechaReplica(Date fechaReplica) {
        this.fechaReplica = fechaReplica;
    }

    public Integer getIdCalificacion() {
        return idCalificacion;
    }

    public void setIdCalificacion(Integer idCalificacion) {
        this.idCalificacion = idCalificacion;
    }

    public Integer getIdPuntuacion() {
        return idPuntuacion;
    }

    public void setIdPuntuacion(Integer idPuntuacion) {
        this.idPuntuacion = idPuntuacion;
    }

    public Integer getIdUsuarioCalificador() {
        return idUsuarioCalificador;
    }

    public void setIdUsuarioCalificador(Integer idUsuarioCalidicador) {
        this.idUsuarioCalificador = idUsuarioCalidicador;
    }

    public Integer getIdUsuarioReplicador() {
        return idUsuarioReplicador;
    }

    public void setIdUsuarioReplicador(Integer idUsuarioReplicador) {
        this.idUsuarioReplicador = idUsuarioReplicador;
    }

    public String getNombreUsuarioCalificador() {
        return nombreUsuarioCalificador;
    }

    public void setNombreUsuarioCalificador(String nombreUsuarioCalificador) {
        this.nombreUsuarioCalificador = nombreUsuarioCalificador;
    }

    public String getNombreUsuarioReplica() {
        return nombreUsuarioReplica;
    }

    public void setNombreUsuarioReplica(String nombreUsuarioReplica) {
        this.nombreUsuarioReplica = nombreUsuarioReplica;
    }

    public String getNombrePuntuacion() {
        return nombrePuntuacion;
    }

    public void setNombrePuntuacion(String nombrePuntuacion) {
        this.nombrePuntuacion = nombrePuntuacion;
    }

    public Boolean getYaReplico() {
        return yaReplico;
    }

    public void setYaReplico(Boolean yaReplico) {
        this.yaReplico = yaReplico;
    }
}
