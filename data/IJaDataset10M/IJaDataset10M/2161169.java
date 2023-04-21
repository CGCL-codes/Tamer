package com.museum4j.actions.director;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.*;

public class ContenidoForm extends ActionForm {

    private int idContenido;

    private String[] codigo;

    private String[] titulo;

    private String[] descripcion;

    private String estiloCSS;

    private int[] idContenidoBorrar = null;

    private int idContenidoDePortada;

    private int idRecorridoVistaMovil;

    private String codigoIdiomaVistaMovil = null;

    public int getIdContenido() {
        return this.idContenido;
    }

    public void setIdContenido(int idContenido) {
        this.idContenido = idContenido;
    }

    public String[] getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String[] codigo) {
        this.codigo = codigo;
    }

    public String[] getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String[] titulo) {
        this.titulo = titulo;
    }

    public String[] getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String[] descripcion) {
        this.descripcion = descripcion;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

    public String getEstiloCSS() {
        return this.estiloCSS;
    }

    public void setEstiloCSS(String css) {
        this.estiloCSS = css;
    }

    public int[] getIdContenidoBorrar() {
        return this.idContenidoBorrar;
    }

    public void setIdContenidoBorrar(int[] idContenidoBorrar) {
        this.idContenidoBorrar = idContenidoBorrar;
    }

    public int getIdContenidoDePortada() {
        return this.idContenidoDePortada;
    }

    public void setIdContenidoDePortada(int id) {
        this.idContenidoDePortada = id;
    }

    public int getIdRecorridoVistaMovil() {
        return this.idRecorridoVistaMovil;
    }

    public void setIdRecorridoVistaMovil(int id) {
        this.idRecorridoVistaMovil = id;
    }

    public String getCodigoIdiomaVistaMovil() {
        return this.codigoIdiomaVistaMovil;
    }

    public void setCodigoIdiomaVistaMovil(String codigoIdioma) {
        this.codigoIdiomaVistaMovil = codigoIdioma;
    }
}
