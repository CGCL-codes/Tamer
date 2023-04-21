package org.gecko.jee.community.mobidick.beanmapper.error;

import org.gecko.jee.community.mobidick.error.TechnicalException;

/**
 * <b> Description: Exception for mapping errors.</b>
 * <p>
 * BeanMappingException is an exception due to an error that may occur during a
 * object <=> object conversion.
 * </p>
 * <hr>
 * 
 * @author GECKO SOFTWARE
 * 
 */
@Deprecated
public class BeanMappingException extends TechnicalException {

    /**
	 * Serial UID.
	 */
    private static final long serialVersionUID = -7587097562760040147L;

    /**
	 * Constructor.
	 * 
	 * @param code
	 *            exception code
	 */
    @Deprecated
    public BeanMappingException(final String code) {
        super(code);
    }

    /**
	 * Constructor.
	 * 
	 * @param code
	 *            exception code
	 * @param information
	 *            detailed information
	 */
    @Deprecated
    public BeanMappingException(final String code, final String information) {
        super(code, information);
    }

    /**
	 * Constructor.
	 * 
	 * @param code
	 *            exception code
	 * @param information
	 *            detailed information
	 * @param cause
	 *            underlying throwable
	 */
    @Deprecated
    public BeanMappingException(final String code, final String information, final Throwable cause) {
        super(code, information, cause);
    }

    /**
	 * Constructor.
	 * 
	 * @param code
	 *            exception code
	 * @param cause
	 *            underlying throwable
	 */
    @Deprecated
    public BeanMappingException(final String code, final Throwable cause) {
        super(code, cause);
    }
}
