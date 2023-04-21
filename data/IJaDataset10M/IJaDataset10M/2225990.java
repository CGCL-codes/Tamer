package com.antares.sirius.view.form;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.util.Collection;
import org.apache.struts.action.ActionForm;
import com.antares.commons.enums.FormatoReporteEnum;
import com.antares.sirius.model.Persona;
import com.antares.sirius.model.RelacionContractual;

/**
 * Formulario que contendra los filtros a partir de los cuales se generara el reporte de Personas.
 *
 * @version 1.0.0 Created 23/04/2011 by Pablo Delfino
 * @author <a href:mailto:pnicdelfino@gmail.com> Pablo Delfino </a>
 *
 */
@SuppressWarnings("serial")
public class ReportePersonaForm extends ActionForm {

    private Collection<FormatoReporteEnum> formatosReporte;

    private String formatoReporte;

    private String apellido;

    private String nombre;

    private String idRelacionContractual;

    private Collection<RelacionContractual> relacionesContractuales;

    private Collection<Persona> result;

    private Boolean verNumeroDocumento;

    private Boolean verCuit;

    private Boolean verCBU;

    private Boolean verFechaNacimiento;

    private Boolean verProfesion;

    private Boolean verDireccion;

    private Boolean verTelefono = TRUE;

    private Boolean verEmail;

    private Boolean verFuncion;

    private Boolean verRelacionContractual = TRUE;

    public String getFormatoReporte() {
        return formatoReporte;
    }

    public void setFormatoReporte(String formatoReporte) {
        this.formatoReporte = formatoReporte;
    }

    public Collection<FormatoReporteEnum> getFormatosReporte() {
        return formatosReporte;
    }

    public void setFormatosReporte(Collection<FormatoReporteEnum> formatosReporte) {
        this.formatosReporte = formatosReporte;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getVerNumeroDocumento() {
        return verNumeroDocumento;
    }

    public void setVerNumeroDocumento(Boolean verNumeroDocumento) {
        this.verNumeroDocumento = verNumeroDocumento;
    }

    public Boolean getVerCuit() {
        return verCuit;
    }

    public void setVerCuit(Boolean verCuit) {
        this.verCuit = verCuit;
    }

    public Boolean getVerCBU() {
        return verCBU;
    }

    public void setVerCBU(Boolean verCBU) {
        this.verCBU = verCBU;
    }

    public Boolean getVerFechaNacimiento() {
        return verFechaNacimiento;
    }

    public void setVerFechaNacimiento(Boolean verFechaNacimiento) {
        this.verFechaNacimiento = verFechaNacimiento;
    }

    public Boolean getVerProfesion() {
        return verProfesion;
    }

    public void setVerProfesion(Boolean verProfesion) {
        this.verProfesion = verProfesion;
    }

    public Boolean getVerDireccion() {
        return verDireccion;
    }

    public void setVerDireccion(Boolean verDireccion) {
        this.verDireccion = verDireccion;
    }

    public Boolean getVerTelefono() {
        return verTelefono;
    }

    public void setVerTelefono(Boolean verTelefono) {
        this.verTelefono = verTelefono;
    }

    public Boolean getVerEmail() {
        return verEmail;
    }

    public void setVerEmail(Boolean verEmail) {
        this.verEmail = verEmail;
    }

    public Boolean getVerFuncion() {
        return verFuncion;
    }

    public void setVerFuncion(Boolean verFuncion) {
        this.verFuncion = verFuncion;
    }

    public Boolean getVerRelacionContractual() {
        return verRelacionContractual;
    }

    public void setVerRelacionContractual(Boolean verRelacionContractual) {
        this.verRelacionContractual = verRelacionContractual;
    }

    public Collection<Persona> getResult() {
        return result;
    }

    public void setResult(Collection<Persona> result) {
        this.result = result;
    }

    public String getIdRelacionContractual() {
        return idRelacionContractual;
    }

    public void setIdRelacionContractual(String idRelacionContractual) {
        this.idRelacionContractual = idRelacionContractual;
    }

    public Collection<RelacionContractual> getRelacionesContractuales() {
        return relacionesContractuales;
    }

    public void setRelacionesContractuales(Collection<RelacionContractual> relacionesContractuales) {
        this.relacionesContractuales = relacionesContractuales;
    }

    public void initialize() {
        this.result = null;
        this.apellido = "";
        this.nombre = "";
        this.idRelacionContractual = "";
        this.verNumeroDocumento = TRUE;
        this.verCuit = FALSE;
        this.verCBU = FALSE;
        this.verFechaNacimiento = FALSE;
        this.verProfesion = FALSE;
        this.verDireccion = FALSE;
        this.verTelefono = TRUE;
        this.verEmail = FALSE;
        this.verFuncion = FALSE;
        this.verRelacionContractual = TRUE;
    }

    public void initializeForm() {
        this.apellido = "";
        this.nombre = "";
        this.idRelacionContractual = "";
        this.verNumeroDocumento = TRUE;
        this.verCuit = FALSE;
        this.verCBU = FALSE;
        this.verFechaNacimiento = FALSE;
        this.verProfesion = FALSE;
        this.verDireccion = FALSE;
        this.verTelefono = TRUE;
        this.verEmail = FALSE;
        this.verFuncion = FALSE;
        this.verRelacionContractual = TRUE;
    }

    public void initializeForm(Persona entity) {
        this.apellido = entity.getApellido();
        this.nombre = entity.getNombre();
        this.idRelacionContractual = entity.getRelacionContractual().getId().toString();
    }
}
