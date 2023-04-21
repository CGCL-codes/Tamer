package org.light.portlets.search;

import static org.light.portal.util.Constants._PORTLET_JSP_PATH;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.light.portal.portlet.core.impl.LightGenericPortlet;
import org.light.portal.portlet.core.impl.PortletPreference;

/**
 * 
 * @author Jianmin Liu
 **/
public class ExternalSearchPortlet extends LightGenericPortlet {

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, java.io.IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String name = request.getParameter("urlName");
            String url = request.getParameter("url");
            PortletPreferences portletPreferences = request.getPreferences();
            portletPreferences.setValue(name, url);
            portletPreferences.store();
        }
        if ("delete".equals(action)) {
            String name = request.getParameter("engineName");
            PortletPreferences portletPreferences = request.getPreferences();
            portletPreferences.reset(name);
            portletPreferences.store();
        }
    }

    protected void doView(RenderRequest request, RenderResponse response) throws PortletException, java.io.IOException {
        List<PortletPreference> searchEngine = new ArrayList<PortletPreference>();
        PortletPreferences portletPreferences = request.getPreferences();
        Enumeration enumerator = portletPreferences.getNames();
        while (enumerator.hasMoreElements()) {
            String name = (String) enumerator.nextElement();
            searchEngine.add(new PortletPreference(name, portletPreferences.getValue(name, "")));
        }
        request.setAttribute("searchEngine", searchEngine);
        this.getPortletContext().getRequestDispatcher(_PORTLET_JSP_PATH + "/search/searchPortletView.jsp").include(request, response);
    }

    protected void doEdit(RenderRequest request, RenderResponse response) throws PortletException, java.io.IOException {
        List<PortletPreference> searchEngine = new ArrayList<PortletPreference>();
        PortletPreferences portletPreferences = request.getPreferences();
        Enumeration enumerator = portletPreferences.getNames();
        while (enumerator.hasMoreElements()) {
            String name = (String) enumerator.nextElement();
            if (!portletPreferences.isReadOnly(name)) searchEngine.add(new PortletPreference(name, portletPreferences.getValue(name, "")));
        }
        request.setAttribute("searchEngine", searchEngine);
        this.getPortletContext().getRequestDispatcher(_PORTLET_JSP_PATH + "/search/searchPortletEdit.jsp").include(request, response);
    }
}
