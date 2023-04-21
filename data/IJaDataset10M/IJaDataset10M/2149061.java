package lif.core.service.impl;

/**
 * Created by IntelliJ IDEA.
 * User: malaka
 * Date: Jan 28, 2008
 * Time: 12:57:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfileNotFoundException extends Exception {

    public ProfileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileNotFoundException(String message) {
        super(message);
    }

    public ProfileNotFoundException(Throwable cause) {
        super(cause);
    }
}
