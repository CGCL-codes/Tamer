package com.liferay.mail;

import com.liferay.portal.PortalException;

/**
 * <a href="NoSuchAttachmentException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class NoSuchAttachmentException extends PortalException {

    public NoSuchAttachmentException() {
        super();
    }

    public NoSuchAttachmentException(String msg) {
        super(msg);
    }

    public NoSuchAttachmentException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NoSuchAttachmentException(Throwable cause) {
        super(cause);
    }
}
