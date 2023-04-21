package ch.iserver.ace;

/**
 * An exception thrown by <code>DocumentModel</code>s apply method.
 */
public class DocumentModelException extends RuntimeException {

    /**
	 * Constructor.
	 * 
	 * @param msg
	 */
    public DocumentModelException(String msg) {
        super(msg);
    }

    /**
	 * Constructor.
	 * 
	 * @param t
	 */
    public DocumentModelException(Throwable t) {
        super(t);
    }
}
