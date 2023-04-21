package org.jbfilter.core.fcomps.single;

/**
 * A filter component which offers common string filtering options, like case sensitivity
 * and regular expression filtering.
 * @author Marcus Adrian
 * @param <E> the type of the beans to filter
 */
public interface StringFilterComponent<E> extends RegexFilterComponent<E>, ContainsStringFilterComponent<E> {

    /**
	 * The clean value for this member. Set it to {@code null} to ignore while clearing.
	 * @param regexCleanValue
	 */
    void setRegexCleanValue(Boolean regexCleanValue);

    /**
	 * Defaults to {@code false}.
	 * @see #setRegexCleanValue(Boolean)
	 */
    Boolean getRegexCleanValue();

    /**
	 * Switches on/off the regular expression mode.
	 * @param regex
	 */
    void setRegex(boolean regex);

    /**
	 * Indicates if the regular expression mode is on/off.
	 * @return {@code true} if on, {@code false} if off,  
	 */
    boolean isRegex();
}
