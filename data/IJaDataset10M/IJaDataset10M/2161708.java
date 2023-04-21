package org.wijiscommons.ssaf.process.solr;

import org.wijiscommons.ssaf.util.dom.SSAFErrorTypes;

public class SSAFSolrException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5140313841123074342L;

    private SSAFErrorTypes errorType;

    public SSAFSolrException() {
        super();
    }

    public SSAFSolrException(String message) {
        super(message);
    }

    public SSAFSolrException(String message, SSAFErrorTypes errorType) {
        super(message);
        this.errorType = errorType;
    }

    public void setErrortype(SSAFErrorTypes errorType) {
        this.errorType = errorType;
    }

    public SSAFErrorTypes getErrortype() {
        return errorType;
    }
}
