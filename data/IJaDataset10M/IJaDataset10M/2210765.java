package org.arch4j.webpresentation;

/**
 * This class is a simple bean which helps the {@link UnknownFrontCommand}.
 * It contains a message explaining why the command was unknown.
 *
 * @author $Author: rgreinke $
 * @version $Revision: 1.1 $
 */
public class UnknownHelper {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
