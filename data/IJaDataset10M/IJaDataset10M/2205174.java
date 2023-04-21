package org.jowidgets.api.controller;

import org.jowidgets.api.widgets.IComponent;

public interface IListenerFactory<LISTENER_TYPE> {

    /**
	 * Creates a listener for the component or null, if no listener should observe
	 * the component.
	 * 
	 * @param component The component to create the listener for
	 * 
	 * @return The created listener or null
	 */
    LISTENER_TYPE create(IComponent component);
}
