package bbalc.gui.interfaces;

/**
 * Defines methods for the display of messages. 
 */
public interface IMessageDisplay {

    /**
	 * clear the message display
	 */
    public abstract void clear();

    /**
	 * adds a line to the message display
	 * @param message the message string to add
	 */
    public abstract void addLine(String message);

    /**
	 * adds a debug line to the message display
	 * @param message the message string to add
	 */
    public abstract void addLineDebug(String message);

    /**
	 * sets the text to display in the message display
	 * @param message the message to display
	 */
    public abstract void setText(String message);
}
