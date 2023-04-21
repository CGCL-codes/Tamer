package org.timothyb89.jtelirc.config;

/**
 * A Listener for property change events.
 * @author timothyb89
 */
public interface PropertyChangeListener {

    /**
	 * Called when the key of a Property is changed.
	 * @param prop The property modified .
	 * @param oldKey The old key.
	 * @param newKey The new key.
	 */
    public void onKeyChanged(Property prop, String oldKey, String newKey);

    /**
	 * Called when the value of a Property is changed.
	 * @param prop The property changed.
	 * @param oldValue The old value of the property.
	 * @param newValue The new value of the property.
	 */
    public void onValueChanged(Property prop, Object oldValue, Object newValue);
}
