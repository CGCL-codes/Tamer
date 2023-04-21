package org.rascalli.mbe.simplemind;

/**
 * <p>
 * 
 * </p>
 * 
 * <p>
 * <b>Company:&nbsp;</b> SAT, Research Studios Austria
 * </p>
 * 
 * <p>
 * <b>Copyright:&nbsp;</b> (c) 2007
 * </p>
 * 
 * <p>
 * <b>last modified:</b><br/> $Author: $<br/> $Date: $<br/> $Revision: $
 * </p>
 * 
 * @author Christian Schollum
 */
public class RssXmlException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public RssXmlException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public RssXmlException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public RssXmlException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public RssXmlException(Throwable cause) {
        super(cause);
    }
}
