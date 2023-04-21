package galerias.utilidades;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class GeneradorMensajes {

    public static void generarMensaje(FacesContext contexto, String mensajeResumen, String mensajeDetalle, String idClienteId, Severity severidad) {
        FacesMessage mensaje = new FacesMessage(severidad, mensajeResumen, mensajeDetalle);
        contexto.addMessage(idClienteId, mensaje);
    }

    public static void generarMensaje(FacesContext contexto, String mensajeResumen, String idClienteId, Severity severidad) {
        FacesMessage mensaje = new FacesMessage();
        mensaje.setSeverity(severidad);
        mensaje.setSummary(mensajeResumen);
        contexto.addMessage(idClienteId, mensaje);
    }
}
