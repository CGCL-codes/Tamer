package com.liferay.portal.language;

import com.liferay.portal.PortalException;

/**
 * <a href="LanguageException.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.4 $
 *
 */
public class LanguageException extends PortalException {

    public LanguageException() {
        super();
    }

    public LanguageException(String msg) {
        super(msg);
    }

    public LanguageException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public LanguageException(Throwable cause) {
        super(cause);
    }
}
