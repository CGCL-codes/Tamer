package jade.content.onto;

import jade.content.ContentException;

/**
 * Generic exception of the content support.
 *
 * @author Federico Bergenti - Universita` di Parma
 */
public class OntologyException extends ContentException {

    /**
     * Construct an <code>OntologyException</code> with a given message.
     *
     * @param message the message
     *
     */
    public OntologyException(String message) {
        super(message);
    }

    public OntologyException(String message, Throwable t) {
        super(message, t);
    }
}
