package es.caib.zonaper.front.action;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import es.caib.zonaper.front.form.ActualizarAlertasForm;
import es.caib.zonaper.modelInterfaz.PersonaPAD;
import es.caib.zonaper.persistence.delegate.DelegateUtil;
import es.caib.zonaper.persistence.delegate.PadAplicacionDelegate;

/**
 * @struts.action
 *  name="actualizarAlertasForm"
 *  path="/protected/actualizarAlertas"
 *  scope="request"
 *  validate="true"
 *  input=".alertas"
 *  
 * @struts.action-forward
 *  name="success" path="/protected/menuAutenticado"
 *
 */
public class ActualizarAlertasAction extends BaseAction {

    private static Log _log = LogFactory.getLog(ActualizarAlertasAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActualizarAlertasForm f = (ActualizarAlertasForm) form;
        Principal seyconPrincipal = request.getUserPrincipal();
        if (_log.isDebugEnabled()) _log.debug("Actualizando datos alertas para el usuario " + seyconPrincipal.getName());
        PadAplicacionDelegate delegate = DelegateUtil.getPadAplicacionDelegate();
        PersonaPAD personaPAD = delegate.obtenerDatosPersonaPADporUsuario(this.getDatosSesion(request).getCodigoUsuario());
        personaPAD.setHabilitarAvisosExpediente(f.getHabilitarAvisosExpediente());
        delegate.modificarPersona(personaPAD);
        return mapping.findForward("success");
    }
}
