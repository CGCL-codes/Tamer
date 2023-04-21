package Beans.Requests.DataRetrieval;

import Utilities.Constants.VirtualResourceType;
import Utilities.Constants.ResourceRequestType;
import Utilities.PermissionsConstants;

/**
 * Instances of this class are used by the user interface when a request to
 * retrieve snapshot data for a specific virtual machine is sent to the data
 * retrieval service.
 *
 * @author Angel Sanadinov
 */
public class SnapshotsDataRetrievalRequest extends VBoxResourceRequest {

    /**
     * No-argument constructor. <br><br>
     * All fields are set to their invalid values.
     */
    public SnapshotsDataRetrievalRequest() {
        super();
        setRequestedAction(PermissionsConstants.ACTION_RETRIEVE_SNAPSHOTS);
    }

    /**
     * Constructs a data request for a machine's snapshots.
     *
     * @param requestorId the id of the user that made the request
     * @param serverId the id of the server on which the machine resides
     * @param machineId the id of the required machine
     */
    public SnapshotsDataRetrievalRequest(int requestorId, int serverId, String machineId) {
        super(requestorId, serverId, machineId);
        setRequestedAction(PermissionsConstants.ACTION_RETRIEVE_SNAPSHOTS);
    }

    @Override
    public VirtualResourceType getResourceType() {
        return VirtualResourceType.MACHINE;
    }

    @Override
    public boolean isValid() {
        if (super.isValid() && getResourceRequestType() == ResourceRequestType.SINGLE_RESOURCE) return true; else return false;
    }

    /**
     * Returns a textual representation of the object. <br>
     *
     * It is in the form: "ClassName: (machineId,limit,ownerId)".
     *
     * @return the object represented as a String
     */
    @Override
    public String toString() {
        return SnapshotsDataRetrievalRequest.class.getSimpleName() + ": (" + getResourceId() + "," + getLimit() + "," + getOwnerId() + ")";
    }
}
