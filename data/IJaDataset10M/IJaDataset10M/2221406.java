package es.caib.zonaper.front.action;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import es.caib.audita.modelInterfaz.ConstantesAuditoria;
import es.caib.audita.modelInterfaz.Evento;
import es.caib.audita.persistence.delegate.DelegateAUDUtil;
import es.caib.sistra.plugins.PluginFactory;
import es.caib.sistra.plugins.login.PluginLoginIntf;
import es.caib.util.CredentialUtil;
import es.caib.zonaper.front.Constants;
import es.caib.zonaper.model.DatosSesion;
import es.caib.zonaper.model.ElementoExpediente;
import es.caib.zonaper.model.Entrada;
import es.caib.zonaper.model.EntradaTelematica;
import es.caib.zonaper.modelInterfaz.PersonaPAD;
import es.caib.zonaper.persistence.delegate.DelegatePADUtil;
import es.caib.zonaper.persistence.delegate.DelegateUtil;
import es.caib.zonaper.persistence.delegate.PadDelegate;

/**
 * @struts.action
 *  name="initForm"
 *  path="/protected/init"
 *  scope="request"
 *  validate="false"
 *  
 * @struts.action-forward
 *  name="success" path="/protected/menuAutenticado.do"
 *  
 * @struts.action-forward
 *  name="inicioAnonimo" path="/protected/menuAnonimo.do" 
 *
 */
public class InitAction extends BaseAction {

    Log logger = LogFactory.getLog(InitAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Principal seyconPrincipal = this.getPrincipal(request);
        DatosSesion datosSesion = this.getDatosSesion(request);
        String result = "S";
        String descripcion = null;
        if (datosSesion == null) {
            try {
                PluginLoginIntf plgLogin = PluginFactory.getInstance().getPluginLogin();
                datosSesion = new DatosSesion();
                datosSesion.setNivelAutenticacion(plgLogin.getMetodoAutenticacion(seyconPrincipal));
                datosSesion.setLocale(this.getLocale(request));
                if (datosSesion.getNivelAutenticacion() != CredentialUtil.NIVEL_AUTENTICACION_ANONIMO) {
                    PadDelegate delegate = DelegatePADUtil.getPadDelegate();
                    PersonaPAD personaPAD = delegate.obtenerDatosPersonaPADporUsuario(seyconPrincipal.getName());
                    datosSesion.setPersonaPAD(personaPAD);
                }
                this.setDatosSesion(request, datosSesion);
            } catch (Exception exc) {
                result = "N";
                descripcion = exc.getMessage();
                logger.error("Error accediendo a la zona personal", exc);
                throw (exc);
            }
            logEventoAccesoZonaPersonal(datosSesion.getNivelAutenticacion(), datosSesion.getCodigoUsuario(), datosSesion.getNifUsuario(), datosSesion.getNombreUsuario(), datosSesion.getLocale().getLanguage(), result, descripcion);
        }
        if (request.getParameter("notificacion") != null) {
            if (this.getDatosSesion(request).getNivelAutenticacion() == 'A') {
                response.sendRedirect(request.getContextPath() + "/protected/accesoDirectoAnonimo.do?codigo=" + request.getParameter("notificacion") + "&tipo=" + ElementoExpediente.TIPO_NOTIFICACION);
            } else {
                response.sendRedirect(request.getContextPath() + "/protected/mostrarDetalleElemento.do?codigo=" + request.getParameter("notificacion") + "&tipo=" + ElementoExpediente.TIPO_NOTIFICACION + "&expediente=true");
            }
            return null;
        }
        if (request.getParameter("aviso") != null) {
            if (this.getDatosSesion(request).getNivelAutenticacion() == 'A') {
                response.sendRedirect(request.getContextPath() + "/protected/accesoDirectoAnonimo.do?codigo=" + request.getParameter("aviso") + "&tipo=" + ElementoExpediente.TIPO_AVISO_EXPEDIENTE);
            } else {
                response.sendRedirect(request.getContextPath() + "/protected/mostrarDetalleElemento.do?codigo=" + request.getParameter("aviso") + "&tipo=" + ElementoExpediente.TIPO_AVISO_EXPEDIENTE + "&expediente=true");
            }
            return null;
        }
        if (request.getParameter("tramite") != null) {
            String idPersistencia = request.getParameter("tramite");
            Entrada entrada;
            entrada = DelegateUtil.getEntradaTelematicaDelegate().obtenerEntradaTelematica(idPersistencia);
            if (entrada == null) {
                entrada = DelegateUtil.getEntradaPreregistroDelegate().obtenerEntradaPreregistro(idPersistencia);
            }
            if (entrada == null) return null;
            if (this.getDatosSesion(request).getNivelAutenticacion() == 'A') {
                response.sendRedirect(request.getContextPath() + "/protected/infoTramiteAnonimo.do?idPersistencia=" + idPersistencia);
            } else {
                response.sendRedirect(request.getContextPath() + "/protected/mostrarDetalleExpediente.do?id=" + (entrada instanceof EntradaTelematica ? "T" : "P") + entrada.getCodigo());
            }
            return null;
        }
        if (datosSesion.getNivelAutenticacion() == Constants.NIVEL_AUTENTICACION_ANONIMO) {
            return mapping.findForward("inicioAnonimo");
        }
        return mapping.findForward("success");
    }

    public void logEventoAccesoZonaPersonal(char nivelAutenticacion, String seyconUser, String idDocumentoIdPersonal, String nombre, String lang, String result, String descripcion) throws Exception {
        try {
            Evento eventoAuditado = new Evento();
            eventoAuditado.setTipo(ConstantesAuditoria.EVENTO_ACCESO_ZONA_PERSONAL);
            eventoAuditado.setNivelAutenticacion(String.valueOf(nivelAutenticacion));
            if (nivelAutenticacion != 'A') {
                eventoAuditado.setUsuarioSeycon(seyconUser);
                eventoAuditado.setNumeroDocumentoIdentificacion(idDocumentoIdPersonal);
                eventoAuditado.setNombre(nombre);
            }
            eventoAuditado.setDescripcion(descripcion);
            eventoAuditado.setIdioma(lang);
            eventoAuditado.setResultado(result);
            DelegateAUDUtil.getAuditaDelegate().logEvento(eventoAuditado);
        } catch (Exception ex) {
            logger.error("Excepci�n auditoria en InitAction Zona Personal: " + ex.getMessage(), ex);
        }
    }
}
