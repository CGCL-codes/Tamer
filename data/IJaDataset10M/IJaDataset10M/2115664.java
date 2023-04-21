package org.tripcom.integration.entry;

import java.net.URI;

public class CommitTransaction extends EndTransaction {

    /**
	 * Serial Version uid.
	 */
    private static final long serialVersionUID = -2192810143027570634L;

    /**
	 * Default constructor.
	 */
    public CommitTransaction() {
        super();
    }

    /**
	 * Creates a new CreateTransaction object.
	 * 
	 * @param operationID
	 *            the operation id.
	 * @param transactionID
	 *            the transaction which shall be commited.
	 */
    public CommitTransaction(Long operationID, URI transactionID) {
        super(operationID, transactionID);
    }
}
