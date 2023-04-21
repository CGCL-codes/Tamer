package org.ws4d.java.communication;

import org.ws4d.java.constants.CommunicationConstants;

/**
 * The protocol ID of the DPWS technology.
 */
public final class DPWSCommunicationID implements CommunicationID {

    public static final CommunicationID INSTANCE = new DPWSCommunicationID();

    private DPWSCommunicationID() {
        super();
    }

    public String getId() {
        return CommunicationConstants.COMMUNICATION_MANAGER_DPWS;
    }

    public String toString() {
        return getId();
    }
}
