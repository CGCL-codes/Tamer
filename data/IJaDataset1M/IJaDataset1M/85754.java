package net.sf.pathfinder.util.properties;

public interface PropertyChangeListener {

    /**
	 * Is called after a property has changed
	 * @param source Source of the event
	 * @param propertyName Name of the changed property
	 * @param propertyValue New value of the property
	 */
    void propertyChanged(Object source, String propertyName, Object propertyValue);
}
