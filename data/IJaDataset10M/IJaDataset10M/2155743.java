package com.softaspects.jsf.support.internationalization;

/**
 * Interface to manage local resources
 * There are can be two kind of values used in application.
 * First kind is a single string value.
 * If manager evaluates the value of this type it returns this value.
 * Second kind is localizable value. It is string in a special format.
 * Format has next presentation "#{expr1.expr2}"
 * If manager evaluates the value of this type it takes inner
 * bundle by expr1. Then takes value from this bundle by expr2.
 */
public interface LocalizationManager {

    /**
	 * Get value of specified expression.
	 * If expression is a single string then the value of this expression will be returned
	 * If expression is a localizable string then the value from the inner bundle
	 * will be returned
	 *
	 * @param expression expression
	 * @return value
	 */
    public String getValue(String expression);

    /**
	 * Return value of expression as boolean
	 *
     * @param expression expression
     * @param property property
     * @return value
	 */
    public boolean getBooleanValue(String expression, String property);

    /**
	 * Return value of expression as integer with the range check
	 *
     * @param expression expression
     * @param property property
	 * @param min        min value of expression
	 * @param max        max value of expression
     * @return value
	 */
    public int getIntegerValueRange(String expression, String property, int min, int max);

    /**
	 * Return value of expression as integer
	 *
     * @param expression expression
     * @param property property
     * @return value
	 */
    public int getIntegerValue(String expression, String property);

    /**
	 * Return value of expression as short
	 *
     * @param expression expression
	 * @param property property
     * @return value
	 */
    public short getShortValue(String expression, String property);

    /**
	 * Add secified Bundle to the list of Bundles
	 *
	 * @param key    key of bundle
	 * @param bundle to be added
	 */
    public void addBundle(String key, Bundle bundle);

    /**
	 * Remove bundle with the specified key
	 *
	 * @param key key of bundle
	 */
    public void removeBundle(String key);
}
