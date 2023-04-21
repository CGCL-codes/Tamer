package uk.ac.ncl.cs.instantsoap;

/**
 * Date: 30-May-2006
 * Time: 17:19:08
 *
 * @author Cheng-Yang(Louis) Tang
 */
public class JobExecutionException extends Exception {

    public JobExecutionException() {
    }

    public JobExecutionException(String message) {
        super(message);
    }

    public JobExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobExecutionException(Throwable cause) {
        super(cause);
    }
}
