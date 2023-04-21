package net.cevnx.gui.event;

/**
 * The <code>ButtonListener</code> interface allows for call backs to button events.
 * 
 * @author Christopher Field <cfield2@gmail.com>
 * @version
 * @since 0.0.1
 */
public interface ButtonListener {

    /**
	 * Called when the button is pressed.
	 * 
	 * @param event The information regarding the event.
	 */
    public void buttonPressed(final ButtonEvent event);

    /**
	 * Called when the button is released.
	 * 
	 * @param event The information regarding the event.
	 */
    public void buttonReleased(final ButtonEvent event);
}
