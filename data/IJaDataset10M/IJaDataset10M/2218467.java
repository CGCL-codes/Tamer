package org.gecko.jee.community.mobidick.batchprocess;

/**
 * <b> Description: Constants for exit codes.</b>
 * <p>
 * </p>
 * <hr>
 * 
 * @author GECKO SOFTWARE
 * 
 */
public class ExitStatus extends org.springframework.batch.repeat.ExitStatus {

    /**
	 * Serial UID.
	 */
    private static final long serialVersionUID = -3017530476897649402L;

    /**
	 * Exit status UNKNOWN: the cause of the exit can not be identified.
	 */
    public static final ExitStatus UNKNOWN = new ExitStatus(false, "UNKNOWN");

    /**
	 * Exit status CONTINUABLE: the batch is completed, but may be continued.
	 */
    public static final ExitStatus CONTINUABLE = new ExitStatus(true, "CONTINUABLE");

    /**
	 * Exit status FINISHED: the batch is completed.
	 */
    public static final ExitStatus FINISHED = new ExitStatus(false, "COMPLETED");

    /**
	 * Exit status NOOP: the batch is completed, but no operation has been
	 * achieved.
	 */
    public static final ExitStatus NOOP = new ExitStatus(false, "NOOP");

    /**
	 * Exit status FAILED: the batch failed, an exception was thrown.
	 */
    public static final ExitStatus FAILED = new ExitStatus(false, "FAILED");

    /**
	 * @param continuable
	 */
    public ExitStatus(final boolean continuable) {
        super(continuable);
    }

    /**
	 * Constructor.
	 * 
	 * @param continuable
	 *            boolean to report if the batch can continue
	 * @param exitCode
	 *            the exit code
	 */
    public ExitStatus(final boolean continuable, final String exitCode) {
        this(continuable, exitCode, "");
    }

    /**
	 * Constructor.
	 * 
	 * @param continuable
	 *            boolean to report if the batch can continue
	 * @param exitCode
	 *            the exit code
	 * @param exitDescription
	 *            optional description for the exit
	 */
    public ExitStatus(final boolean continuable, final String exitCode, final String exitDescription) {
        super(continuable, exitCode, exitDescription);
    }
}
