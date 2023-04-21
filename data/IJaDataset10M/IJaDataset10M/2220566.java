package org.nexopenframework.core;

import org.nexopenframework.core.CoreException;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Generic exception for service component module</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class ServiceComponentException extends CoreException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public ServiceComponentException() {
    }

    /**
	 * @param message
	 */
    public ServiceComponentException(String message) {
        super(message);
    }

    /**
	 * @param message
	 * @param module
	 */
    public ServiceComponentException(String message, String module) {
        super(message, module);
    }

    /**
	 * @param thr
	 */
    public ServiceComponentException(Throwable thr) {
        super(thr);
    }

    /**
	 * @param thr
	 * @param module
	 */
    public ServiceComponentException(Throwable thr, String module) {
        super(thr, module);
    }

    /**
	 * @param message
	 * @param thr
	 */
    public ServiceComponentException(String message, Throwable thr) {
        super(message, thr);
    }

    /**
	 * @param message
	 * @param thr
	 * @param module
	 */
    public ServiceComponentException(String message, Throwable thr, String module) {
        super(message, thr, module);
    }
}
