package org.mobicents.slee.connector.local;

import java.util.UUID;
import javax.slee.connection.ExternalActivityHandle;

/**
 * @author Tim
 * @author eduardomartins
 * 
 * Implementaion of a serializable handle to a null activity that lives on the SLEE
 * 
 */
public class ExternalActivityHandleImpl implements ExternalActivityHandle {

    private String activityContextId;

    ExternalActivityHandleImpl(String activityContextId) {
        this.activityContextId = activityContextId;
    }

    public boolean equals(Object other) {
        if (other != null && this.getClass() == other.getClass()) {
            return ((ExternalActivityHandleImpl) other).activityContextId.equals(this.activityContextId);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return activityContextId.hashCode();
    }

    String getActivityContextId() {
        return activityContextId;
    }
}
