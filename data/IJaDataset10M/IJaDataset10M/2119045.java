package junit.log4j;

import java.util.Iterator;
import java.util.LinkedList;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * <p>Singleton that distributes 
 * {@link org.apache.log4j.Layout#format(org.apache.log4j.spi.LoggingEvent)
 *        logging events} to all registered
 * {@link junit.log4j.AppenderListener logging listeners}.</p>
 *
 *
 * Created: Mon Nov 12 10:21:18 2001
 *
 * @author <a href="mailto:wolfgang@openfuture.de">Wolfgang Reissenberger</a>
 * @version $Revision: 1.2 $
 */
public class JUnitAppenderSingleton {

    private static JUnitAppenderSingleton instance;

    private static Layout layout;

    private static LinkedList listeners;

    /**
     * Creates a new <code>JUnitAppenderSingleton</code> instance.
     *
     */
    protected JUnitAppenderSingleton() {
        listeners = new LinkedList();
    }

    /**
     * Return the instance of this class. If no instance is present,
     * a new one will be instantiated first.
     *
     * @return a <code>JUnitAppenderSingleton</code> value
     */
    protected static synchronized JUnitAppenderSingleton getInstance() {
        if (instance == null) {
            instance = new JUnitAppenderSingleton();
        }
        return instance;
    }

    /**
     * Sends the event as
     * {@link org.apache.log4j.Layout#format(org.apache.log4j.spi.LoggingEvent)
     *        formatted message} to all 
     * {@link junit.log4j.AppenderListener logging listeners}.
     * @param event a <code>LoggingEvent</code> value
     */
    protected static synchronized void addLoggingEvent(LoggingEvent event) {
        if (event == null) return;
        String message;
        if (getLayout() != null) {
            message = getLayout().format(event);
        } else message = event.getMessage().toString();
        if (message == null) return;
        Iterator it = cloneListeners().iterator();
        while (it.hasNext()) {
            Object listener = it.next();
            if (listener instanceof AppenderListener) {
                ((AppenderListener) listener).addMessage(message);
            }
        }
    }

    /**
     * Get the value of layout.
     * @return value of layout.
     */
    public static synchronized Layout getLayout() {
        return getInstance().layout;
    }

    /**
     * Set the value of layout.
     * @param v  Value to assign to layout.
     */
    public static synchronized void setLayout(Layout v) {
        getInstance().layout = v;
    }

    /**
     * Registers a TestListener.
     * @param listener an <code>AppenderListener</code> value
     */
    public static synchronized void addListener(AppenderListener listener) {
        getInstance().listeners.add(listener);
    }

    /**
     * Unregisters a TestListener.
     * @param listener an <code>AppenderListener</code> value
     */
    public static synchronized void removeListener(AppenderListener listener) {
        getInstance().listeners.remove(listener);
    }

    /**
     * Clone the current list of listeners.
     *
     * @return a <code>LinkedList</code> value
     */
    public static synchronized LinkedList cloneListeners() {
        return (LinkedList) getInstance().listeners.clone();
    }
}
