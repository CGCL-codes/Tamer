package net.wimpi.modbus.procimg;

/**
 * Class implementing a simple <tt>DigitalOut</tt>.
 * <p>
 * The set method is synchronized, which ensures atomic
 * access, but no specific access order.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class SimpleDigitalOut implements DigitalOut {

    /**
   * Field for the digital out state.
   */
    protected boolean m_Set;

    /**
   * Constructs a new <tt>SimpleDigitalOut</tt> instance.
   * It's state will be invalid.
   */
    public SimpleDigitalOut() {
    }

    /**
   * Constructs a new <tt>SimpleDigitalOut</tt> instance
   * with the given state.
   *
   * @param b true if set, false otherwise.
   */
    public SimpleDigitalOut(boolean b) {
        set(b);
    }

    public boolean isSet() {
        return m_Set;
    }

    public synchronized void set(boolean b) {
        m_Set = b;
    }
}
