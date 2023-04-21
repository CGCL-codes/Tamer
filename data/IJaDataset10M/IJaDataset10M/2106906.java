package tuwien.auto.calimero;

import java.util.EventListener;

/**
 * The base listener interface to receive events of the communication with a KNX
 * network.
 * <p>
 */
public interface KNXListener extends EventListener {

    /**
	 * Arrival of a new KNX message frame.
	 * <p>
	 * 
	 * @param e frame event object
	 */
    void frameReceived(FrameEvent e);

    /**
	 * The connection has been closed.
	 * <p>
	 * 
	 * @param e connection close event object
	 */
    void connectionClosed(CloseEvent e);
}
