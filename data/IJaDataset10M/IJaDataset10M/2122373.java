package com.intel.gpe.services.vtsf.notification;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.List;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.workspace.WorkspaceConstants;
import org.globus.workspace.generated.negotiable.AggregateWorkspaceDeployment_Type;
import org.globus.wsrf.NotifyCallback;
import org.globus.wsrf.core.notification.ResourcePropertyValueChangeNotificationElementType;
import com.intel.gpe.services.vtss.common.Status;
import com.intel.gpe.services.vtss.gtk.GT4VirtualTargetSystemResource;

/**
 * @author Sai Srinivas Dharanikota
 * @version $Id$
 */
public class DeploymentListener implements NotifyCallback {

    private GT4VirtualTargetSystemResource resource;

    private String newState;

    public DeploymentListener(GT4VirtualTargetSystemResource resource, PrintStream _out) {
        this.resource = resource;
    }

    public void deliver(List list, EndpointReferenceType endpointReferenceType, Object o) {
        System.out.print(Calendar.getInstance().getTime() + " -- ");
        System.out.println("Received notification: ");
        AggregateWorkspaceDeployment_Type depl = null;
        ResourcePropertyValueChangeNotificationElementType valueChange = (ResourcePropertyValueChangeNotificationElementType) o;
        try {
            depl = (AggregateWorkspaceDeployment_Type) valueChange.getResourcePropertyValueChangeNotification().getNewValue().get_any()[0].getValueAsType(WorkspaceConstants.DEPLOYMENT_SET, AggregateWorkspaceDeployment_Type.class);
            newState = depl.getDeploymentSet(0).getWorkspaceDeployment().getWorkspaceState().getValue();
            if ((resource.getStatus().compareTo(Status.DEPLOYED) == 0) && (newState.compareTo(Status.RUNNING) == 0)) {
                resource.setStatus(Status.BOOTING);
            } else {
                resource.setStatus(newState);
            }
            resource.store();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print("The new state of the workspace is:" + newState);
        try {
            if (newState == Status.RUNNING) {
                resource.refresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
