package org.tripcom.integration.entry;

import java.net.URI;

/**
 * @author Christian Schreiber
 * 
 */
public class EndTransaction extends TMRequestEntry {

    /**
	 * Serial version UID.
	 */
    private static final long serialVersionUID = 1170778455364494851L;

    /**
	 * the transaction which shall be ended.
	 */
    public URI transactionID;

    /**
	 * Default constructor.
	 */
    public EndTransaction() {
        super();
    }

    /**
	 * Creates a new EndTransaction object.
	 * 
	 * @param operationID
	 *            the operation id.
	 * @param transactionID
	 *            the transaction which shall be ended.
	 */
    public EndTransaction(Long operationID, URI transactionID) {
        this.operationID = operationID;
        this.transactionID = transactionID;
    }
}
