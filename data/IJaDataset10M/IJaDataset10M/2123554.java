package org.posterita.exceptions;

import org.posterita.exceptions.OperationException;

public class EMailMessageParsingException extends OperationException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public EMailMessageParsingException(String msg) {
        super(msg);
    }

    public EMailMessageParsingException(String msg, Exception ex) {
        super(msg, ex);
    }
}
