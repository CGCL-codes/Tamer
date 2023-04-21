package org.opensih.gdq.Controladores;

import javax.ejb.Stateless;
import org.opensih.gdq.Modelo.Intervencion;

@Stateless
public class RestriccionesGDQ implements IRestriccionesGDQ {

    public String controlar(Intervencion i) {
        if (i.getDatos().getDiagnostico() == null || i.getDatos().getDiagnostico().equals("<root></root>")) {
            return "Debe ingresar un Diagn�stico Pre Operatorio.";
        }
        if (i.getDatos().getProcedimiento() == null || i.getDatos().getProcedimiento().equals("<root></root>")) {
            return "Debe ingresar un Procedimiento Propuesto.";
        }
        if (i.getDatos().getCategoria() == null) {
            return "Debe indicar si el paciente es postergable o no.";
        }
        if (i.getDatos().getAnestesia() == null) {
            return "Debe seleccionar el tipo de Anestesia.";
        }
        if (i.getDatos().getModalidad() == null) {
            return "Debe seleccionar la Modalidad.";
        }
        if (i.getDatos().getTipo() == null && i.getDatos().getModalidad().equals("Cirug�a con Internaci�n")) {
            return "Debe seleccionar el Tipo.";
        }
        return "ok";
    }
}
