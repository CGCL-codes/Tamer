package eu.more.diaball.interfaces;

/**
 * This interface will be implemented by the Enabling services which can deliver
 * message to the application layer
 * 
 * @author Akos
 * 
 */
public interface MessageProvider {

    /**
	 * Sets the message listener. Message listener is a part of the application
	 * layer, which absorbs the messages.
	 * 
	 * @param user
	 *            the object representing the user of the application
	 * @param listener
	 *            The listener which can absorb the messages
	 */
    public void setMessageListener(MessageListener listener);

    public void setUser(User user);

    public void removeListener(MessageListener listener);

    public void removeUser(User user);
}
