package modelo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Cuadricula implements java.io.Serializable {

    private Integer idCuadricula;

    private Usuario usuario;

    private Date fechaInicio;

    TipoCuadricula estado;

    private Set datosCuadricula;

    public Cuadricula() {
    }

    public Integer getIdCuadricula() {
        return this.idCuadricula;
    }

    public void setIdCuadricula(Integer idCuadricula) {
        this.idCuadricula = idCuadricula;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaInicio() {
        return this.fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public TipoCuadricula getEstado() {
        return this.estado;
    }

    public void setEstado(TipoCuadricula estado) {
        this.estado = estado;
    }

    public Set getDatosCuadricula() {
        return this.datosCuadricula;
    }

    public void setDatosCuadricula(Set datoCuadriculas) {
        this.datosCuadricula = datoCuadriculas;
    }

    public void addDatosCuadricula(DatoCuadricula c) {
        c.setCuadricula(this);
        this.datosCuadricula.add(c);
    }
}
