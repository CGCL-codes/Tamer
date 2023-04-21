package hu.sztaki.lpds.wfi.inf;

import hu.sztaki.lpds.information.inf.BaseCommunicationFace;
import hu.sztaki.lpds.wfi.com.WorkflowInformationBean;
import hu.sztaki.lpds.wfi.com.WorkflowRuntimeBean;
import java.util.Vector;

public interface PortalWfiClient extends BaseCommunicationFace {

    /**
 * Submitting the workflow
 * @param value The descriptor of the workflow submission
 */
    public String submitWorkflow(WorkflowRuntimeBean value);

    /**
 * Aborts the running of a workflow
 * @param value The runtime descriptor ID of the workflow
 */
    public Boolean abortWorkflow(String value);

    /**
 * Continues the run of a workflow after failure
 * @param value The descriptor of the workflow submission
 * @deprected
 */
    public Boolean rescueWorkflow(WorkflowRuntimeBean value);

    /**
 * Reloads the workflow
 * @param value The descriptor of the workflow submission
 * @deprected
 */
    public Boolean reloadWorkflow(WorkflowRuntimeBean value);

    /**
 * Query of the status of the running workflows
 * @return Workflow descriptions
 * @see WorkflowInformationBean
 */
    public Vector<WorkflowInformationBean> getInformation();
}
