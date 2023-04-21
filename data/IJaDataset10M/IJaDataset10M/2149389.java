package org.apache.solr.client.solrj;

/** Exception to catch all types of communication / parsing issues associated with talking to SOLR
 * 
 * @version $Id: SolrServerException.java 555343 2007-07-11 17:46:25Z hossman $
 * @since solr 1.3
 */
public class SolrServerException extends Exception {

    private static final long serialVersionUID = -3371703521752000294L;

    public SolrServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SolrServerException(String message) {
        super(message);
    }

    public SolrServerException(Throwable cause) {
        super(cause);
    }

    public Throwable getRootCause() {
        Throwable t = this;
        while (true) {
            Throwable cause = t.getCause();
            if (cause != null) {
                t = cause;
            } else {
                break;
            }
        }
        return t;
    }
}
