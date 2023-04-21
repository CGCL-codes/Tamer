package com.abiquo.framework.exception;

import com.abiquo.framework.config.FrameworkTypes.ExceptionType;

/**
 * Class exception to throw when there is a provisioning problem.
 * 
 * @author abiquo
 * 
 */
public class ProvisionException extends FrameworkException {

    private static final long serialVersionUID = 1L;

    /**
	 * Class constructor adding a message.
	 * 
	 * @param string
	 *            the exception message
	 */
    public ProvisionException(final String string) {
        super(ExceptionType.PROVISIONING, string);
    }

    /**
	 * Class constructor adding a message.
	 * 
	 * @param string
	 *            the exception message
	 * @param th
	 *            the exception
	 */
    public ProvisionException(final String string, Throwable th) {
        super(ExceptionType.PROVISIONING, string, th);
    }
}
