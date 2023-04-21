package org.ucl.xpath;

/**
 * Error caused by static name.
 */
public class StaticNameError extends StaticError {

    public static final String NAME_NOT_FOUND = "XP0008";

    /**
	 * Constructor for static name error
	 * @param code is the code.
	 * @param reason is the reason for the error.
 	 */
    public StaticNameError(String code, String reason) {
        super(code, reason);
    }

    /**
	 * Constructor for static name error
	 * @param reason is the reason for the error.
 	 */
    public StaticNameError(String reason) {
        this(NAME_NOT_FOUND, reason);
    }
}
