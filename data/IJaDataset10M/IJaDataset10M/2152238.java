package com.juanfrivaldes.cio2005.gestion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author root
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class MarcarPagadoAction extends GestionAction {

    private static Log log = LogFactory.getLog(MarcarPagadoAction.class);

    protected ActionForward protectedExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EnviarMailsForm mailsForm = (EnviarMailsForm) form;
        String usr = (String) request.getParameter("usr");
        if (usr != null) {
            log.trace("Se solicita marcar como pagado al usuario: ");
            this.getCio2005().setPagado(usr);
        }
        return mapping.findForward("success");
    }
}
