package org.qtitools.qti.exception;

import org.qtitools.qti.node.XmlNode;

/**
 * This exception is used for propagating errors during item flow.
 * 
 * @author Jiri Kajaba
 */
public class QTIItemFlowException extends QTIRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructs A new exception with the specified detailed message.
	 *
	 * @param source source of this exception
	 * @param message the detail message
	 */
    public QTIItemFlowException(XmlNode source, String message) {
        super(message + " (" + source.getFullName() + ")");
    }
}
