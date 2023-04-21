package it.ancona.comune.ankonhippo.workflows.ankonhippo;

import com.opensymphony.workflow.WorkflowException;
import java.util.HashMap;
import java.util.Map;
import nl.hippo.cms.repositorylocation.CommonRepositoryLocationRoles;
import nl.hippo.cms.repositorylocation.RepositoryLocation;
import nl.hippo.cms.workflows.shared.WorkflowJob;
import nl.hippo.servermanager.ProjectWorkflowRepository;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.commons.httpclient.HttpState;

/**
 * NOTE: Moves/deletes of objects in the repository are fatal.
 */
public abstract class WorkflowTask implements WorkflowJob, Serviceable {

    static final long serialVersionUID = 1;

    private ServiceManager m_serviceManager;

    public WorkflowTask() {
        super();
    }

    public void service(ServiceManager serviceManager) {
        m_serviceManager = serviceManager;
    }

    public void execute(String url, long workflowId, Map jobData, HttpState httpState, ProjectWorkflowRepository pwr) throws WorkflowException {
        try {
            Map parameters = new HashMap();
            parameters.put(AHConstants.LOCATION, url);
            RepositoryLocation repositoryLocation = (RepositoryLocation) m_serviceManager.lookup(CommonRepositoryLocationRoles.EDITOR_REPOSITORY_LOCATION_ROLE);
            parameters.put(AHConstants.REPOSITORY_ROOT_URL, repositoryLocation.getRepositoryInformation().getAbsoluteBaseUri());
            parameters.put(AHConstants.HTTPSTATE, httpState);
            parameters.put("propertiesCache", new HashMap());
            parameters.put(AHConstants.TAXONOMY, jobData.get(AHConstants.TAXONOMY));
            pwr.doAction(workflowId, getActionId(), parameters);
        } catch (ServiceException e) {
            throw new WorkflowException("Unable to obtain repository location", e);
        }
    }

    protected abstract int getActionId();
}
