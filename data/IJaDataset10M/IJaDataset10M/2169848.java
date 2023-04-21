package pcgen.core.prereq;

/**
 * @author wardc
 *
 */
public class PrerequisiteException extends Exception {

    /**
	 *
	 */
    public PrerequisiteException() {
        super();
    }

    /**
	 * @param arg0
	 */
    public PrerequisiteException(final String arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 * @param arg1
	 */
    public PrerequisiteException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    /**
	 * @param arg0
	 */
    public PrerequisiteException(final Throwable arg0) {
        super(arg0);
    }
}
