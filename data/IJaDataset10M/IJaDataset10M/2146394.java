package com.bizsensors.gourangi.struts.actions;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LogoutAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String target = new String("success");
        request.setAttribute("LOGGED_IN_USER", null);
        request.getSession().setAttribute("LOGGED_IN_USER", null);
        request.getSession().invalidate();
        return (mapping.findForward(target));
    }
}
