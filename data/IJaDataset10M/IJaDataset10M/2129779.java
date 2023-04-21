package net.sf.nutj.prefs;

/**
 * A preference based on a String value.
 * 
 * @author Nathan C Jones
 */
public abstract class StringPreference extends AbstractPreference<String> {

    /**
	 * Create a preference with the given name and description.
	 * @param name the name of the preference.
	 * @param description the description of the preference.
	 * @param preferenceStore the preference store the preference belongs to.
	 */
    public StringPreference(String name, String description, PreferenceStore preferenceStore) {
        super(name, description, preferenceStore);
    }

    public Class<String> getType() {
        return String.class;
    }

    /**
	 * Returns the parameter unchanged.
	 */
    public String fromString(String value) {
        return value;
    }
}
