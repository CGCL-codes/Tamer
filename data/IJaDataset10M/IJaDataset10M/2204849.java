package hla.rti1516e;

import java.io.Serializable;

/**
 * Array of these records returned by (4.25) federationRestoreStatusResponse
 */
public final class FederateRestoreStatus implements Serializable {

    public FederateRestoreStatus(FederateHandle preRestoreHandle, FederateHandle postRestoreHandle, RestoreStatus rs) {
        this.preRestoreHandle = preRestoreHandle;
        this.postRestoreHandle = postRestoreHandle;
        status = rs;
    }

    public final FederateHandle preRestoreHandle;

    public final FederateHandle postRestoreHandle;

    public final RestoreStatus status;
}
