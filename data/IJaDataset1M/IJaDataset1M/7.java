package com.pureperfect.purview.i18n;

/**
 * Interface for interpolating expressions on strings.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public interface ExpressionEngine {

    /**
	 * Interpolate any expressions in the given string using values from the
	 * problem object.
	 * 
	 * @param messageTemplate
	 *            the string to interpolate expressions.
	 * @param problem
	 *            the object to evaluate expressions on
	 * @return the message key with all expressions substituted with their
	 *         appropriate values.
	 * @throws InterpolationException
	 *             if an error occurs.
	 */
    public abstract String eval(String messageTemplate, Object problem) throws InterpolationException;
}
