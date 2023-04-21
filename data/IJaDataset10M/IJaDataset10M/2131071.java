package com.liferay.struts;

import org.apache.commons.lang.exception.NestableException;

/**
 * <a href="SampleException.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class SampleException extends NestableException {

    public SampleException() {
        super();
    }

    public SampleException(String msg) {
        super(msg);
    }

    public SampleException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SampleException(Throwable cause) {
        super(cause);
    }
}
