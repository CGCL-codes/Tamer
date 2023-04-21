package mn.more.wits.server.exception;

/**
 * @author $Author: mikeliucc $
 * @version $Id: ExamSessionException.java 5 2008-09-01 12:08:42Z mikeliucc $
 */
public class ExamSessionException extends Exception {

    public ExamSessionException() {
    }

    public ExamSessionException(String message) {
        super(message);
    }

    public ExamSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExamSessionException(Throwable cause) {
        super(cause);
    }
}
