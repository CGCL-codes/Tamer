package org.jscsi.parser.datasegment;

/**
 * <h1>SettingsMap</h1>
 * <p>
 * This class encapsulates all informations of one setting of the configuration
 * file.
 * 
 * @author Volker Wildi
 */
public final class SettingEntry {

    /** Scope identifier. */
    private String scope;

    /** Result function. */
    private IResultFunction result;

    /** Value of the parameter. */
    private String value;

    /**
	 * Default constructor to create a new, empty <code>SettingEntry</code>.
	 */
    public SettingEntry() {
    }

    /**
	 * Sets the result function of this object to the given one.
	 * 
	 * @param newResult
	 *            The new result function.
	 */
    public final void setResult(final IResultFunction newResult) {
        result = newResult;
    }

    /**
	 * Sets the scope of this object to the given one.
	 * 
	 * @param newScope
	 *            The new scope.
	 */
    public void setScope(final String newScope) {
        scope = newScope;
    }

    /**
	 * Sets the value of this object to the given one.
	 * 
	 * @param newValue
	 *            The new value.
	 */
    public void setValue(final String newValue) {
        value = newValue;
    }

    /**
	 * Returns the scope of this object.
	 * 
	 * @return The scope of this object.
	 */
    public String getScope() {
        return scope;
    }

    /**
	 * Returns the result function of this object.
	 * 
	 * @return The result function of this object.
	 */
    public IResultFunction getResult() {
        return result;
    }

    /**
	 * Returns the value of this object.
	 * 
	 * @return The value of this object.
	 */
    public String getValue() {
        return value;
    }
}
