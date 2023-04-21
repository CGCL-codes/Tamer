package com.intel.gpe.client2.portalclient.portlets.stages;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import com.intel.gpe.client2.portalclient.PortalClient;
import com.intel.gpe.client2.portalclient.database.beans.ProfileBean;
import com.intel.gpe.client2.portalclient.portlets.PortalClientPortlet;
import com.intel.gpe.client2.portalclient.portlets.Stage;
import com.intel.gpe.client2.portalclient.portlets.StringConstants;
import com.intel.gpe.clients.api.GridBean;
import com.intel.gpe.clients.api.GridBeanClient;
import com.intel.gpe.clients.api.exceptions.GPEMiddlewareRemoteException;

/**
 * The page with GridBean list to select a GridBean for specifying a new job or fetching outcome.
 * 
 * Render method must be set to "submit" if you are selecting a GridBean for job submission, or
 * to "fetch" if you are selecting a GridBean for fetching outcome.
 * 
 * @author Alexander Lukichev
 * @version $Id: SelectGridBeanStage.java,v 1.2 2006/11/15 13:44:52 lukichev Exp $
 *
 */
public class SelectGridBeanStage extends Stage {

    public static final String NAME = "_selectgridbean";

    private String renderStage;

    private String renderMethod;

    private String jobid;

    public SelectGridBeanStage(PortalClientPortlet portlet) {
        super(portlet);
        portlet.putStage(OutcomeStage.NAME, new OutcomeStage(portlet));
        addStageHandler("submit", new SelectGridBeanForSubmit());
        addStageHandler("fetch", new SelectGridBeanForFetchOutcome());
        addStageHandler(MoreHelp.NAME, new MoreHelp(portlet));
        addStageHandler(CloseHelp.NAME, new CloseHelp(portlet));
    }

    @Override
    public void renderStage(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        PortalClient portalClient = getPortlet().getPortalClient(request);
        ProfileBean profile = portalClient.getProfile();
        String[] gridBeanServices = profile.getGridBeanServicesArray();
        Map<String, String[]> result = new HashMap<String, String[]>();
        if (gridBeanServices != null) {
            for (int i = 0; i < gridBeanServices.length; i++) {
                GridBeanClient gbclient = portalClient.getGridBeanClient(gridBeanServices[i]);
                GridBean[] gridBeans;
                try {
                    gridBeans = gbclient.listGridBeans();
                } catch (GPEMiddlewareRemoteException e) {
                    getPortlet().getPortletContext().log("Cannot list GridBeans at " + gridBeanServices[i], e);
                    continue;
                }
                for (GridBean gridBean : gridBeans) {
                    result.put(gridBean.getName() + "@" + gridBeanServices[i], new String[] { "v." + Integer.toString(gridBean.getPluginVersion()), Integer.toString(gridBean.getSize() / 1024) + "K" });
                }
            }
        }
        request.setAttribute("gridbeanlist", result);
        PortletURL helpURL = response.createRenderURL();
        helpURL.setParameter(StringConstants.STAGE, NAME);
        if (getPortlet().getPortalClient(request).getProfile().getShowHelp()) {
            request.setAttribute(StringConstants.SHOW_HELP, "true");
        } else {
            request.setAttribute(StringConstants.SHOW_HELP, null);
        }
        helpURL.setParameter(StringConstants.RENDER_METHOD, MoreHelp.NAME);
        request.setAttribute(StringConstants.MORE_HELP, helpURL.toString());
        helpURL.setParameter(StringConstants.RENDER_METHOD, CloseHelp.NAME);
        request.setAttribute(StringConstants.LESS_HELP, helpURL.toString());
        PortletURL baseURL = response.createRenderURL();
        baseURL.setParameter(StringConstants.STAGE, renderStage);
        if (jobid != null) {
            baseURL.setParameter("jobId", jobid);
        }
        baseURL.setParameter(StringConstants.RENDER_METHOD, renderMethod);
        request.setAttribute("baseURL", baseURL);
        PortletRequestDispatcher rd = getPortlet().getPortletContext().getRequestDispatcher(StringConstants.JSP_DIRECTORY + StringConstants.PAGE_SELECT_GRIDBEAN);
        rd.include(request, response);
    }

    private class SelectGridBeanForSubmit implements StageHandler {

        public void handle(RenderRequest rquest, RenderResponse response) throws PortletException, IOException {
            renderStage = GridBeanStage1.NAME;
            jobid = null;
            renderMethod = "loadGridBean";
        }
    }

    private class SelectGridBeanForFetchOutcome implements StageHandler {

        public void handle(RenderRequest request, RenderResponse response) throws PortletException, IOException {
            renderStage = OutcomeStage.NAME;
            jobid = request.getParameter("jobId");
            renderMethod = "fetchOutcome";
        }
    }
}
