package javax.comm;

import java.util.EventListener;

/**
 * This interface is used to receive notifications of serial port
 * events.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 2.0.3
 */
public interface SerialPortEventListener extends EventListener {

    /**
   * Notifies this listener of a serial port event.
   * @param ev the event
   */
    void serialEvent(SerialPortEvent ev);
}
