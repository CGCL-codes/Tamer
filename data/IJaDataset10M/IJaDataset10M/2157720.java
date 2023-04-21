package at.rc.tacos.core.net.event;

import at.rc.tacos.core.net.socket.MyClient;

/**
 * This interface defines the methods that the network layer must provide to
 * communicate with the service
 * 
 * @author Michael
 */
public interface INetListener {

    /**
	 * Invoked when new data received.
	 * 
	 * @param ne
	 *            the net event fired
	 */
    public void dataReceived(NetEvent ne);

    /**
	 * Invoked when the data could not be send
	 * 
	 * @param ne
	 *            the net event fired
	 */
    public void dataTransferFailed(NetEvent ne);

    /**
	 * Invoked when a socket changed the status
	 * 
	 * @param client
	 *            the client connection that changed the status
	 * @param status
	 *            the new status
	 */
    public void socketStatusChanged(MyClient client, int status);
}
