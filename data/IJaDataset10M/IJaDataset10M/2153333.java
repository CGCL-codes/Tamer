package org.yawlfoundation.yawl.scheduling.timer;

/**
 * @author Michael Adams
 * @date 1/12/11
 */
public class JobTimerException extends Exception {

    public JobTimerException() {
        super();
    }

    public JobTimerException(String msg) {
        super(msg);
    }

    public JobTimerException(String msg, Throwable t) {
        super(msg, t);
    }
}
