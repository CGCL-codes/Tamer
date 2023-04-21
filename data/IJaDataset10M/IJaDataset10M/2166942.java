package org.tripcom.integration.entry.transaction;

import java.net.URI;

public class GetTransaction extends TransactionEntry {

    /**
     * Auto generated serial version UID.
     */
    private static final long serialVersionUID = -8446588806931304012L;

    public URI transactionID;

    public GetTransaction() {
        super();
    }

    /**
     * Create a new GetTransaction.
     *
     * @param operationID
     *        the operation id.
     * @param transactionID
     *        the transaction id.
     */
    public GetTransaction(Long operationID, URI transactionID) {
        super(operationID);
        this.transactionID = transactionID;
    }
}
