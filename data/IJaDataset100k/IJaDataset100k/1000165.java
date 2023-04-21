package com.hack23.cia.web.common;

import thinwire.ui.event.ActionListener;
import thinwire.ui.event.KeyPressListener;

/**
 * The listener interface for receiving controllerAction events.
 * The class that is interested in processing a controllerAction
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addControllerActionListener<code> method. When
 * the controllerAction event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ControllerActionEvent
 */
public interface ControllerActionListener extends ActionListener, KeyPressListener {

    /**
	 * Handle user action.
	 *
	 * @param userObject the user object
	 */
    void handleUserAction(final Object userObject);
}
