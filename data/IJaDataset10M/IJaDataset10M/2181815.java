package edu.lcmi.grouppac.util;

/**
 * This exception is throwed when a Debug.assertCondition() call is not true.
 * Created: Fri Mar  3 16:58:41 2000
 * 
 * @version $Revision: 1.12 $
 * @author Herbert Kiefer
 * @author <a href="mailto:padilha@das.ufsc.br">Ricardo Sangoi Padilha</a>, <a
 *         href="http://www.das.ufsc.br/">UFSC, Florian�polis, SC, Brazil</a>
 * @see Debug#assertCondition(boolean, String)
 */
public class AssertionViolation extends RuntimeException {

    /**
	 * Creates a new AssertionViolation object.
	 */
    public AssertionViolation() {
        this("");
    }

    /**
	 * Creates a new AssertionViolation object.
	 * 
	 * @param msg
	 */
    public AssertionViolation(String msg) {
        super(msg);
    }
}
