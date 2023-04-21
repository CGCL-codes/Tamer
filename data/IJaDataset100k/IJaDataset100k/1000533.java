package com.controltier.ctl.authentication;

/**
 * UserInfoException is ...
 *
 * @author Greg Schueler <a href="mailto:greg@controltier.com">greg@controltier.com</a>
 * @version $Revision: 1079 $
 */
public class UserInfoException extends RuntimeException {

    public UserInfoException() {
        super();
    }

    public UserInfoException(String msg) {
        super(msg);
    }

    public UserInfoException(Exception cause) {
        super(cause);
    }

    public UserInfoException(String msg, Exception cause) {
        super(msg, cause);
    }
}
