package net.sf.opengroove.client.com;

/**
 * This class has been replaced by {@link PacketListener}.
 * @deprecated
 * @author Alexander Boyd
 *
 */
public interface LowLevelMessageSink {

    /**
	 * this method is called when the LowLevelCommunicator receives a response
	 * to a command.
	 * 
	 * NOTE: it is generally OK for the implementation to declare this method
	 * synchronized if this listener is used in only one LowLevelCommunicator.
	 * if this listener is registered to multiple LowLevelCommunicators, then it
	 * is not generally a good idea to synchronize this method.
	 * 
	 * @param command
	 *            the command to which a response has been received
	 * @param arguments
	 *            the arguments that were sent along with the command
	 */
    public void process(String command, String arguments);
}
