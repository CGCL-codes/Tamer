package org.wiigee.event;

import java.util.EventListener;

/**
 * This interface has to be implemented if the application should react
 * to button press/releases.
 *
 * @author Benjamin 'BePo' Poppinga
 */
public interface ButtonListener extends EventListener {

    /**
	 * This method would be called if a Device button has been pressed.
	 *
	 * @param event The button representation as an event.
	 */
    public abstract void buttonPressReceived(ButtonPressedEvent event);

    /**
	 * This method would be called if a Device button has been released.
	 *
	 * @param event This is actually a meta-event NOT containing which button
	 * has been released.
	 */
    public abstract void buttonReleaseReceived(ButtonReleasedEvent event);
}
