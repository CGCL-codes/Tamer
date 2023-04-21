package mn.more.wits.server.exception;

/**
 * signifies that a "test taker" is already participating in a test session. The
 * system <b>CANNOT</b> permit any user to participate in multiple tests at the
 * same time.  Optionally, a prompt may be presented at the UI to allow user to
 * "forfeit" an in-progress test in order to take another test.
 *
 * @author $Author: mikeliucc $
 * @version $Id: AlreadyInExamSessionException.java 5 2008-09-01 12:08:42Z mikeliucc $
 */
public class AlreadyInExamSessionException extends ExamSessionException {

    public AlreadyInExamSessionException() {
    }

    public AlreadyInExamSessionException(String message) {
        super(message);
    }

    public AlreadyInExamSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyInExamSessionException(Throwable cause) {
        super(cause);
    }
}
