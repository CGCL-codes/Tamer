package com.liferay.portlet.mail;

import com.liferay.portal.PortalException;

/**
 * <a href="AccountNotFoundException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Jorge Ferrer
 *
 */
public class AccountNotFoundException extends PortalException {

    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String msg) {
        super(msg);
    }

    public AccountNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AccountNotFoundException(Throwable cause) {
        super(cause);
    }
}
