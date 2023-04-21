package net.jforum.exceptions;

/**
 * @author Rafael Steil
 * @version $Id: BadExtensionException.java,v 1.4 2006/08/22 02:05:23 rafaelsteil Exp $
 */
public class BadExtensionException extends AttachmentException {

    public BadExtensionException(String message) {
        super(message);
    }
}
