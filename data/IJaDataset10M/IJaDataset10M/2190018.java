package net.sourceforge.acelogger.interpolator;

/**
 * Abstracts interpolation of parameters in a message.
 * 
 * @author Zardi (https://sourceforge.net/users/daniel_zardi)
 * @version 1.0.0
 * @since 1.0.0
 */
public interface TextInterpolator {

    /**
	 * Interpolates parameters within a string, this should include formatting, padding and some
	 * basic conditionals. The provided features depend directly of the underlying interpolator
	 * used.
	 * 
	 * @param pattern
	 *            The string which should be interpolated.
	 * @param params
	 *            The parameters used to interpolate the message.
	 * @return A string containing the pattern interpolated with the supplied parameters.
	 * @since 1.0.0
	 */
    public String interpolate(String pattern, Object... params);
}
