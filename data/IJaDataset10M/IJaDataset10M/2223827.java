package au.edu.jcu.v4l4j.exceptions;

/**
 * This class of exception is thrown when the initialisation a Video4Linux device fails.
 * The initialisation is made of three steps: allocating memory, opening the device file
 * and checking the version of V4L. Opening the device fail could fail if the device is
 * already being used, or if the permissions forbid it. Checking the V4L version will fail
 * only if the device file belongs to a non-V4L device. If either of the three steps
 * fails, an InitialistationException is thrown.
 * @author gilles
 */
public class InitialisationException extends V4L4JException {

    private static final long serialVersionUID = -3338859321078232443L;

    public InitialisationException(String message) {
        super(message);
    }

    public InitialisationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public InitialisationException(Throwable throwable) {
        super(throwable);
    }
}
