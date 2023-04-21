package com.intel.gpe.client2.portalclient.portlets.actions;

import java.io.IOException;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import com.intel.gpe.client2.common.clientwrapper.ClientWrapper;
import com.intel.gpe.client2.common.tables.jobs.JobEntry;
import com.intel.gpe.client2.portalclient.portlets.ActionHandler;
import com.intel.gpe.client2.portalclient.portlets.PortalClientPortlet;
import com.intel.gpe.client2.portalclient.portlets.StringConstants;
import com.intel.gpe.client2.portalclient.portlets.stages.TargetSystemsStage;
import com.intel.gpe.clients.api.JobClient;
import com.intel.gpe.clients.api.exceptions.GPEException;

/**
 * Main page: Start selected job 
 * 
 * @author Alexander Lukichev
 * @version $Id: StartJobAction.java,v 1.1 2006/10/23 13:54:49 lukichev Exp $
 *
 */
public class StartJobAction extends ActionHandler {

    public static final String NAME = "startJob";

    public StartJobAction(PortalClientPortlet portlet) {
        super(portlet);
    }

    @Override
    public void handle(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        response.setRenderParameter(StringConstants.STAGE, TargetSystemsStage.NAME);
        response.setRenderParameter(StringConstants.RENDER_METHOD, "selectTargetSystem");
        response.setRenderParameter("targetSystem", (String) getPortlet().getSessionObject(request, StringConstants.SELECTED_TARGET_SYSTEM_ID));
        String selectedJob = request.getParameter("jobId");
        Map<String, ClientWrapper<JobClient, JobEntry>> jobs = (Map<String, ClientWrapper<JobClient, JobEntry>>) getPortlet().getSessionObject(request, StringConstants.JOB_LIST);
        if (selectedJob != null && jobs != null) {
            ClientWrapper<JobClient, JobEntry> job = jobs.get(selectedJob);
            try {
                job.getClient().start();
            } catch (GPEException e) {
                getPortlet().showException(request, "Cannot start job", e);
            }
        }
    }
}
