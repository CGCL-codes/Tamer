package org.viewaframework.swing.binding.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public interface Observable {

    /**
	 * This method adds listeners registering all 
	 * modifications made to registered properties.
	 * 
	 * @param listener
	 */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
	 * Fires an event of a specific property
	 * 
	 * @param evt Triggered event
	 */
    public void firePropertyChange(PropertyChangeEvent evt);

    /**
	 * Returns a list of PropertyChangeListener objects
	 * 
	 * @return PropertyChangeListener list
	 */
    public List<PropertyChangeListener> getPropertyChangeListeners();

    /**
	 * This method removes listeners registering all 
	 * modifications made to registered properties.
	 * 
	 * @param listener
	 * @return
	 */
    public void removePropertyChangeListener(PropertyChangeListener listener);
}
