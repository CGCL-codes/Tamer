package it.ancona.comune.ankonhippo.workflows.ankonhippo;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import it.ancona.comune.ankonhippo.workflows.ankonhippo.data.TaxonomyBean;
import it.ancona.comune.ankonhippo.workflows.ankonhippo.data.TaxonomyContainer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import nl.hippo.cms.workflows.shared.DateUtil;
import nl.hippo.cms.workflows.shared.FunctionProviderComponent;
import nl.hippo.componentcontainers.AvalonSpringBridge;
import nl.hippo.servermanager.Project;
import nl.hippo.servermanager.ServerManager;
import org.apache.avalon.framework.CascadingException;
import org.apache.commons.httpclient.HttpState;
import org.quartz.Scheduler;

public class StartUnpublicationTasksFunction extends FunctionProviderComponent {

    private static final int DEBUG_OFFSET_IN_MINUTES = 4;

    public StartUnpublicationTasksFunction() {
        super();
    }

    public void executeImpl(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        String location = (String) transientVars.get(AHConstants.LOCATION);
        String projectName = (String) transientVars.get(AHConstants.PROJECT_NAME);
        AvalonSpringBridge asb = null;
        TaxonomyContainer taxonomyContainer = (TaxonomyContainer) transientVars.get(AHConstants.TAXONOMY_CONTAINER);
        if (taxonomyContainer.isEmpty()) {
            getLogger().error("Unable to find any taxonomy: " + location);
            throw new WorkflowException("Unable to find any taxonomy: " + location);
        }
        Set<String> taskIds = new HashSet<String>();
        Map<String, String> taxonomies2TaskIds = new HashMap<String, String>();
        String taskId = null;
        for (TaxonomyBean taxonomyBean : taxonomyContainer) {
            if (!taxonomyBean.unpublishNever() && !DateUtil.isBeforeWithTime(taxonomyBean.getUnpublicationDate(), taxonomyBean.publishNow() ? AHConstants.PUBLICATION_DATE_MODE_NOW : AHConstants.PUBLICATION_DATE_MODE_OTHER, taxonomyBean.getPublicationDate())) {
                try {
                    asb = (AvalonSpringBridge) m_manager.lookup(AvalonSpringBridge.ROLE);
                    ServerManager sm = (ServerManager) asb.getBean(AHConstants.SERVER_MANAGER);
                    Project p = sm.getServer().getProject(projectName);
                    Scheduler scheduler = p.getJobScheduler();
                    WorkflowSchedulerUtil wsu = new WorkflowSchedulerUtil();
                    wsu.setScheduler(scheduler);
                    wsu.setProjectName(projectName);
                    taskId = wsu.schedule(location, taxonomyBean.getName(), UnpublicationTask.class.getName(), taxonomyBean.getUnpublicationDate(), DEBUG_OFFSET_IN_MINUTES);
                    taskIds.add(taskId);
                    taxonomies2TaskIds.put(taxonomyBean.getName(), taskId);
                } catch (Exception e) {
                    Throwable t = e;
                    while (t != null && t instanceof CascadingException) {
                        t = ((CascadingException) t).getCause();
                    }
                    throw new WorkflowException(t);
                } finally {
                    if (asb != null) {
                        m_manager.release(asb);
                    }
                }
            }
        }
        if (taskIds != null && !taskIds.isEmpty()) {
            ps.setObject(AHConstants.UNPUBLICATION_TASK_IDS, taskIds);
        }
        if (taxonomies2TaskIds != null && !taxonomies2TaskIds.isEmpty()) {
            ps.setObject(AHConstants.TAXONOMIES_2_UNPUBLICATION_TASK_IDS, taxonomies2TaskIds);
        }
        HttpState httpState = (HttpState) transientVars.get(AHConstants.HTTPSTATE);
        ps.setString(AHConstants.WORKFLOW_USER, AHUtils.getWorkflowUser(httpState));
    }
}
