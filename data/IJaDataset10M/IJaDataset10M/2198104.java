package org.fspmboard.server.exception;

import org.springframework.mail.MailException;

/**
 * 
 * 
 * @author Holz, Roberto
 * 15.01.2009 | 11:52:48
 *
 */
public class MailActionException extends MailException {

    private static final long serialVersionUID = 1359182010205454802L;

    public MailActionException(String message) {
        super(message);
    }

    public MailActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
