package hu.sztaki.lpds.pgportal.portlets.informations;

import hu.sztaki.lpds.information.inf.ResourceConfigurationClient;
import hu.sztaki.lpds.information.local.InformationBase;
import hu.sztaki.lpds.information.local.PropertyLoader;
import hu.sztaki.lpds.pgportal.portlet.GenericWSPgradePortlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import java.io.IOException;
import java.util.HashMap;
import javax.portlet.PortletSession;

/**
 * GliteInformotainsPortlet Portlet Class
 */
public class GliteInformotainsPortlet extends GenericWSPgradePortlet {

    @Override
    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        response.setContentType("text/html");
        if (!isInited()) {
            getPortletContext().getRequestDispatcher("/WEB-INF/jsp/error/init.jsp").include(request, response);
            return;
        }
        try {
            String portalID = PropertyLoader.getInstance().getProperty("service.url");
            ResourceConfigurationClient cl = (ResourceConfigurationClient) InformationBase.getI().getServiceClient("resourceconfigure", "portal");
            request.setAttribute("glitevos", cl.getAllGrids(portalID, request.getRemoteUser(), "glite"));
            if (request.getParameter("vo") != null) {
                HashMap props = cl.getGridProperies(portalID, request.getRemoteUser(), "glite", request.getParameter("vo"));
                request.setAttribute("props", props);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        getPortletContext().getRequestDispatcher("/WEB-INF/jsp/informations/glite/view.jsp").include(request, response);
    }
}
