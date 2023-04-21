package net.sf.parser4j.kernelgenerator.service.grammarnode;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class GrammarNodeVisitException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7719145581906962749L;

    public GrammarNodeVisitException() {
        super();
    }

    public GrammarNodeVisitException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public GrammarNodeVisitException(final String message) {
        super(message);
    }

    public GrammarNodeVisitException(final Throwable cause) {
        super(cause);
    }
}
