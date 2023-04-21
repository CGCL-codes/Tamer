package de.iritgo.openmetix.core.network;

import de.iritgo.openmetix.core.iobject.NoSuchIObjectException;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * NetworkSystemAdapter.
 *
 * @version $Id: NetworkSystemAdapter.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class NetworkSystemAdapter implements NetworkSystemListener {

    /**
	 * @see de.iritgo.openmetix.core.network.NetworkSystemListener#connectionEstablished(de.iritgo.openmetix.core.network.NetworkService, de.iritgo.openmetix.core.network.Channel)
	 */
    public void connectionEstablished(NetworkService networkBase, Channel connectedChannel) {
    }

    /**
	 * @see de.iritgo.openmetix.core.network.NetworkSystemListener#connectionTerminated(de.iritgo.openmetix.core.network.NetworkService, de.iritgo.openmetix.core.network.Channel)
	 */
    public void connectionTerminated(NetworkService networkBase, Channel connectedChannel) {
    }

    /**
	 * @see de.iritgo.openmetix.core.network.NetworkSystemListener#error(de.iritgo.openmetix.core.network.NetworkService, de.iritgo.openmetix.core.network.Channel, de.iritgo.openmetix.core.iobject.NoSuchIObjectException)
	 */
    public void error(NetworkService networkBase, Channel connectedChannel, NoSuchIObjectException x) {
    }

    /**
	 * @see de.iritgo.openmetix.core.network.NetworkSystemListener#error(de.iritgo.openmetix.core.network.NetworkService, de.iritgo.openmetix.core.network.Channel, java.net.SocketTimeoutException)
	 */
    public void error(NetworkService networkBase, Channel connectedChannel, SocketTimeoutException x) {
    }

    /**
	 * @see de.iritgo.openmetix.core.network.NetworkSystemListener#error(de.iritgo.openmetix.core.network.NetworkService, de.iritgo.openmetix.core.network.Channel, java.lang.ClassNotFoundException)
	 */
    public void error(NetworkService networkBase, Channel connectedChannel, ClassNotFoundException x) {
    }

    /**
	 * @see de.iritgo.openmetix.core.network.NetworkSystemListener#error(de.iritgo.openmetix.core.network.NetworkService, de.iritgo.openmetix.core.network.Channel, java.io.EOFException)
	 */
    public void error(NetworkService networkBase, Channel connectedChannel, EOFException x) {
    }

    /**
	 * @see de.iritgo.openmetix.core.network.NetworkSystemListener#error(de.iritgo.openmetix.core.network.NetworkService, de.iritgo.openmetix.core.network.Channel, java.net.SocketException)
	 */
    public void error(NetworkService networkBase, Channel connectedChannel, SocketException x) {
    }

    /**
	 * @see de.iritgo.openmetix.core.network.NetworkSystemListener#error(de.iritgo.openmetix.core.network.NetworkService, de.iritgo.openmetix.core.network.Channel, java.io.IOException)
	 */
    public void error(NetworkService networkBase, Channel connectedChannel, IOException x) {
    }
}
