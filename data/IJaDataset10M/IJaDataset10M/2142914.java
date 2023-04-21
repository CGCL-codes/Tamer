package org.nodevision.portal.struts.portlets;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nodevision.portal.om.portletregistry.RegisteredPortlet;
import org.nodevision.portal.om.portletregistry.Webapplication;
import org.nodevision.portal.repositories.RepositoryBasic;

public class EditPortlet extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RepositoryBasic.getInstance(getServlet().getServletContext());
        Webapplication webapp = RepositoryBasic.getWebapplications().getWebapplication(request.getParameter("webapp"));
        ArrayList portlets = webapp.getPortlets();
        ArrayList list = new ArrayList();
        for (int i = 0; i < portlets.size(); i++) {
            RegisteredPortlet portlet = (RegisteredPortlet) portlets.get(i);
            HashMap temp = new HashMap();
            temp.put("portletname", portlet.getNamePortlet());
            temp.put("webappname", request.getParameter("webapp"));
            list.add(temp);
        }
        request.setAttribute("webappportlets", list);
        return mapping.getInputForward();
    }
}
