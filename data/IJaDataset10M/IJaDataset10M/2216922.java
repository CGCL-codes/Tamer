package com.myapp.struts;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

/**
 *
 * @author Alfonso
 */
public class IdiomaAction extends org.apache.struts.action.Action {

    private static final String SUCCESS = "idioma";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession sesion = request.getSession(true);
        String l = request.getParameter("lang");
        if (l.equals("fr")) {
            sesion.setAttribute("org.apache.struts.action.LOCALE", Locale.FRENCH);
        } else if (l.equals("gb")) {
            sesion.setAttribute("org.apache.struts.action.LOCALE", Locale.ENGLISH);
        } else {
            sesion.setAttribute("org.apache.struts.action.LOCALE", Locale.getDefault());
        }
        return mapping.findForward(SUCCESS);
    }
}
