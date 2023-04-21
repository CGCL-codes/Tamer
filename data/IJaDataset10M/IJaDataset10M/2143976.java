package com.liferay.portal.kernel.job;

import com.liferay.portal.PortalException;

/**
 * <a href="JobExecutionException.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class JobExecutionException extends PortalException {

    public JobExecutionException() {
        super();
    }

    public JobExecutionException(String msg) {
        super(msg);
    }

    public JobExecutionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JobExecutionException(Throwable cause) {
        super(cause);
    }
}
