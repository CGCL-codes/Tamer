package uk.org.ogsadai.authorization;

/**
 * Null security context. Provide no information regarding the caller.
 *
 * @author The OGSA-DAI Project Team
 */
public class NullSecurityContext implements SecurityContext {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2010.";

    /**
	 * Constructor.
	 */
    public NullSecurityContext() {
    }
}
