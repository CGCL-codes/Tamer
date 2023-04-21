package hu.sztaki.lpds.wfi.inf;

import hu.sztaki.lpds.wfi.com.WorkflowInformationBean;
import hu.sztaki.lpds.wfi.com.WorkflowRuntimeBean;
import java.util.Vector;

/**
 * @author krisztian
 */
public interface WfiPortalService {

    /**
 * Workflow Submit
 * @param pWorkflowData workflow descriptor
 * @return workflow runtime ID
 */
    public String submitWorkflow(WorkflowRuntimeBean pWorkflowData);

    /**
 * Workflow abort
 * @param pRuntimeID workflow runtime ID
 */
    public void abortWorkflow(String pRuntimeID);

    /**
 * Workflow restart
 * @param pWorkflowData workflow descriptor
 */
    public void rescueWorkflow(WorkflowRuntimeBean pWorkflowData);

    /**
 * Listing the running workflows to the server console
 * @param pURL not used
 */
    public void getWaitingJob(String pURL);

    /**
 * Getting information about the statuses of the running workflows
 * @return Workflow descriptions
 * @see WorkflowInformationBean
 */
    public Vector<WorkflowInformationBean> getInformation();
}
