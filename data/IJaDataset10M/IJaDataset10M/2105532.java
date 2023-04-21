package protopeer.network;

import java.util.concurrent.*;
import org.apache.log4j.*;
import protopeer.*;
import protopeer.measurement.*;

/**
 * Represents the network interface through which messages can be sent and
 * received to/from other network interfaces. Maintains a set of
 * <code>NetworkListener</code>s that receive a callback on network interface
 * events. There is one <code>NetworkAddress</code> associated with each
 * <code>NetworkInterface</code>.
 * 
 */
public abstract class NetworkInterface {

    private static final Logger logger = Logger.getLogger(NetworkInterface.class);

    private CopyOnWriteArrayList<NetworkListener> listeners = new CopyOnWriteArrayList<NetworkListener>();

    private MeasurementLogger measurementLogger;

    protected boolean interfaceUp;

    protected NetworkAddress networkAddress;

    private Peer hostPeer;

    public NetworkInterface(NetworkAddress networkAddress, MeasurementLogger measurementLogger) {
        this.measurementLogger = measurementLogger;
        this.networkAddress = networkAddress;
    }

    /**
	 * Sends the message to a destination.
	 * 
	 * @param destination
	 *            the destination to which the <code>message</code> is sent
	 * @param message
	 *            the message to be sent
	 */
    public abstract void sendMessage(NetworkAddress destination, Message message);

    /**
	 * Sends the message to all destinations that are reachable from this
	 * interface. The <code>NetworkInteface</code> is not required to
	 * implement this method.
	 * 
	 * @param message
	 *            the message to be broadcast
	 */
    public abstract void broadcastMessage(Message message);

    public void addNetworkListener(NetworkListener listener) {
        listeners.add(listener);
    }

    public void removeNetworkListener(NetworkListener listener) {
        listeners.remove(listener);
    }

    /**
	 * Sets the currently executing peer in the Experiment. This is called
	 * before executing the networking event handlers.
	 * 
	 */
    private void setTLSPeer() {
        if (Experiment.getSingleton() != null) {
            Experiment.getSingleton().setCurrentlyExecutingPeer(getHostPeer());
        }
    }

    /**
	 * Resets the currently executing peer back to null when the handlers
	 * finish execution.
	 * 
	 */
    private void unsetTLSPeer() {
        if (Experiment.getSingleton() != null) {
            Experiment.getSingleton().setCurrentlyExecutingPeer(null);
        }
    }

    protected void fireMessageReceived(NetworkAddress sourceAddress, Message message) {
        try {
            setTLSPeer();
            for (NetworkListener listener : listeners) {
                listener.messageReceived(this, sourceAddress, message);
            }
            unsetTLSPeer();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    protected void fireExceptionHappened(NetworkAddress remoteAddress, Message message, Throwable cause) {
        try {
            setTLSPeer();
            for (NetworkListener listener : listeners) {
                listener.exceptionHappened(this, remoteAddress, message, cause);
            }
            unsetTLSPeer();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    protected void fireMessageSent(NetworkAddress destinationAddress, Message message) {
        try {
            setTLSPeer();
            for (NetworkListener listener : listeners) {
                listener.messageSent(this, destinationAddress, message);
            }
            unsetTLSPeer();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    protected void fireInterfaceDown() {
        try {
            setTLSPeer();
            for (NetworkListener listener : listeners) {
                listener.interfaceDown(this);
            }
            unsetTLSPeer();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    protected void fireInterfaceUp() {
        try {
            setTLSPeer();
            for (NetworkListener listener : listeners) {
                listener.interfaceUp(this);
            }
            unsetTLSPeer();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /**
	 * Gets the measurement logger associated with this network interface.
	 * 
	 * @return
	 */
    public MeasurementLogger getMeasurementLogger() {
        return measurementLogger;
    }

    /**
	 * Brings the network interface down, freeing all the underlying resources.
	 * The interface cannot be used to send messages any more. To do that, a new
	 * interface needs to be created.
	 */
    public void bringUp() {
        interfaceUp = true;
        fireInterfaceUp();
    }

    /**
	 * Brings the network interface up. The interface is ready to send messages
	 * after this call.
	 * 
	 */
    public void bringDown() {
        interfaceUp = false;
        fireInterfaceDown();
    }

    public boolean isUp() {
        return interfaceUp;
    }

    /**
	 * Returns the network address associated with this interface.
	 * 
	 * @return
	 */
    public NetworkAddress getNetworkAddress() {
        return networkAddress;
    }

    /**
	 * Returns the peer that is using this network interface.
	 * 
	 * @return
	 */
    public Peer getHostPeer() {
        return hostPeer;
    }

    /**
	 * Lets the network interface know which peer is using it.
	 * 
	 * @param hostPeer
	 */
    public void setHostPeer(Peer hostPeer) {
        this.hostPeer = hostPeer;
    }
}
