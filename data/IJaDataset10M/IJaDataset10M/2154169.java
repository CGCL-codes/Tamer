package org.sqlexp.preferences;

/**
 * Abstract repeated preference object.
 * Instances of this class should have multiple static instances.
 * @author Matthieu Réjou
 */
abstract class RepeatedPreference extends Preference {

    /**
	 * Gets the property index in preference collection.
	 * @return the index
	 */
    public abstract int getIndex();
}
