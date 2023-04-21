package com.siasal.prestamos.commons;

import com.common.to.BaseTO;
import com.siasal.usuarios.business.UsuarioConsulta;
import com.siasal.usuarios.commons.UsuarioConsultaTO;

public class AtrasoItemTO extends BaseTO {

    /**
	 * La informacion de esta clase perteneces a la de un ejemplar
	 * con atraso en devolucion
	 */
    private static final long serialVersionUID = 184156923757033413L;

    private String codigo;

    private String nombre;

    private String autores;

    private int numeroDias;

    private UsuarioConsulta usuario;

    private Integer idEjemplar;

    private Integer idPrestamo;

    /** Creates a new instance of DevolucionItemTO */
    public AtrasoItemTO() {
    }

    public AtrasoItemTO(String codigo, String nombre, String autores, int numeroDias, UsuarioConsulta usuario) {
        this.setCodigo(codigo);
        this.setNombre(nombre);
        this.setAutores(autores);
        this.setNumeroDias(numeroDias);
        this.setUsuario(usuario);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    public int getNumeroDias() {
        return numeroDias;
    }

    public void setNumeroDias(int numeroDias) {
        this.numeroDias = numeroDias;
    }

    public Integer getIdEjemplar() {
        return idEjemplar;
    }

    public void setIdEjemplar(Integer idEjemplar) {
        this.idEjemplar = idEjemplar;
    }

    public Integer getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(Integer idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        AtrasoItemTO atrasoItem = (AtrasoItemTO) obj;
        if (atrasoItem.getIdEjemplar().equals(getIdEjemplar())) {
            return true;
        }
        return false;
    }

    public UsuarioConsulta getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioConsulta usuario) {
        this.usuario = usuario;
    }
}
