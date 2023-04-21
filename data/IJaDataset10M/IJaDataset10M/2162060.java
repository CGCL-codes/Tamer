package javax.faces.view.facelets;

import javax.faces.view.facelets.FaceletException;

/**
 * An Exception caused by a Tag
 * 
 * @author Jacob Hookom
 * @version $Id: TagException.java,v 1.4 2008/07/13 19:01:35 rlubke Exp $
 */
public final class TagException extends FaceletException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public TagException(Tag tag) {
        super(tag.toString());
    }

    /**
     * @param message
     */
    public TagException(Tag tag, String message) {
        super(tag + " " + message);
    }

    /**
     * @param message
     * @param cause
     */
    public TagException(Tag tag, String message, Throwable cause) {
        super(tag + " " + message, cause);
    }

    /**
     * @param cause
     */
    public TagException(Tag tag, Throwable cause) {
        super(tag.toString(), cause);
    }
}
