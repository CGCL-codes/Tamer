package org.iqual.chaplin.example.dci.transfer;

/**
 * User: zslajchrt
* Date: Mar 30, 2009
* Time: 4:42:14 PM
*/
public class CannotPlayRoleException extends Exception {

    private final Class role;

    public CannotPlayRoleException(Class role, String message, Throwable t) {
        super(message, t);
        this.role = role;
    }
}
