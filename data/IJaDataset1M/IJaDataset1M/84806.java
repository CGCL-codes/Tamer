package org.sbbuilder.build.osb;

/**
 * Unchecked error thrown while attempting to build an OSB configuration file.
 * 
 * @author Steve Leach
 */
public class SBBuildError extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructor for SBBuildError.
	 */
    public SBBuildError() {
        super();
    }

    /**
	 * Constructor for SBBuildError.
	 * 
	 * @param msg the error message
	 * @param cause the root cause
	 */
    public SBBuildError(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
	 * Constructor for SBBuildError.
	 *
	 * @param msg the error message
	 */
    public SBBuildError(String msg) {
        super(msg);
    }

    /**
	 * Constructor for SBBuildError.
	 *
	 * @param cause the root cause
	 */
    public SBBuildError(Throwable cause) {
        super(cause);
    }
}
