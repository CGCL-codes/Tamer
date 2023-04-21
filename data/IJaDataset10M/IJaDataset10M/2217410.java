package com.liferay.mail;

import com.liferay.portal.PortalException;

/**
 * <a href="MailboxException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class MailboxException extends PortalException {

    public static final int ACCOUNT_NOT_FOUND = 1;

    public static final int ATTACHMENT_NOT_FOUND = 2;

    public static final int FILE_EXTENSION_NOT_ALLOWED = 3;

    public static final int FOLDER_NOT_FOUND = 4;

    public static final int INCOMING_CONNECTION_FAILED = 5;

    public static final int INCOMING_AND_OUTGOING_CONNECTION_FAILED = 6;

    public static final int INVALID_EMAIL_ADDRESS = 7;

    public static final int INVALID_SCREENNAME = 8;

    public static final int MESSAGE_NOT_FOUND = 9;

    public static final int OUTGOING_CONNECTION_FAILED = 10;

    public static final int REQUIRED_FOLDER = 11;

    public static final int SERVER_ERROR = 12;

    public static final int UNKNOWN_ACCOUNT_TYPE = 13;

    public static final int UNKNOWN_FLAG = 14;

    public MailboxException() {
        super();
    }

    public MailboxException(String msg) {
        super(msg);
    }

    public MailboxException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MailboxException(Throwable cause) {
        super(cause);
    }

    public MailboxException(int type) {
        super();
        _type = type;
    }

    public MailboxException(int type, String msg) {
        super(msg);
        _type = type;
    }

    public int getType() {
        return _type;
    }

    private int _type;
}
