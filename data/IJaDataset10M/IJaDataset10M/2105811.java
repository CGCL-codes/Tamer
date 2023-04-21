package org.tripcom.integration.entry;

import java.net.URI;

public abstract class MgmtBase extends TripComEntry {

    public ManagementOperation operation;

    public ClientInfo clientInfo;

    public URI transactionID;

    public SpaceURI space;

    public Long timestamp;

    /**
     * Default constructor.
     */
    public MgmtBase() {
        super();
    }

    /**
     * Create a new MgmtBase entry.
     */
    public MgmtBase(SpaceURI space, Long timestamp, ManagementOperation operation, ClientInfo clientInfo, Long operationID, URI transactionID) {
        super(operationID);
        this.space = space;
        this.timestamp = timestamp;
        this.operation = operation;
        this.clientInfo = clientInfo;
        this.transactionID = transactionID;
    }

    public final ManagementOperation getOperation() {
        return this.operation;
    }

    public final void setOperation(ManagementOperation operation) {
        this.operation = operation;
    }

    public final ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public final void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public final URI getTransactionID() {
        return this.transactionID;
    }

    public final void setTransactionID(URI transactionID) {
        this.transactionID = transactionID;
    }

    public final void setSpace(SpaceURI space) {
        this.space = space;
    }

    public final SpaceURI getSpace() {
        return this.space;
    }

    public final Long getTimestamp() {
        return this.timestamp;
    }

    public final void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
