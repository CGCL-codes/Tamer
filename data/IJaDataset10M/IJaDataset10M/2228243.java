package org.ewm;

import gnu.x11.Display;
import gnu.x11.Window;
import java.util.HashMap;
import java.util.Map;
import org.ewm.clientManagement.ClientWindow;
import org.ewm.clientManagement.FloatingDecorator;
import org.ewm.clientManagement.EWindow;

/**
 * Represents a managed X display. A display event listener, message producer,
 * event manager and a default decoration are registered here. Together they
 * manage the X display.
 * 
 * @author Erik De Rijcke
 * 
 */
public class ManagedXDisplay {

    private Display display;

    private XDisplayEventListener eventListener;

    private XEventMessageProducer messageProducer;

    private XEventManager eventManager;

    private Map<Integer, EWindow> windowContainerMap;

    /**
	 * Initializes a new managed display. If the defaultDecorationManager is not
	 * specified, the default floating decorator is used.
	 * 
	 * @param display
	 *            The display that needs managing.
	 * @param defaultDecorationManager
	 *            The decoration manager that manages newly created windows.
	 */
    public ManagedXDisplay(Display display) {
        this.windowContainerMap = new HashMap<Integer, EWindow>(20);
        setDisplay(display);
        setEventListener(new XDisplayEventListener());
        setEventManager(new XEventManager());
        setMessageProducer(new XEventMessageProducer());
        getEventListener().addObserver(getMessageProducer());
    }

    /**
	 * Initialize this managed display for event manipulation and managing.
	 */
    public void init() {
        scanForUnmanagedWindows();
        getEventListener().run();
    }

    /**
	 * Scan for previous unmanaged client windows living on this display and
	 * manage them.
	 */
    public void scanForUnmanagedWindows() {
        for (Window window : getDisplay().getRootWindow().tree().children()) {
            if (!window.attributes().override_redirect() && window.attributes().map_state() == Window.AttributesReply.VIEWABLE) {
                ClientWindow clientWindow = new ClientWindow(window);
                clientWindow.init();
            }
        }
    }

    /**
	 * Get the window with the specified id from this display's default root
	 * window.
	 * 
	 * @param windowID
	 *            the id of the window you want to call.
	 * @return The window with the corresponding window id.
	 */
    public Window getWindowFromDisplay(int windowID) {
        Window tmpwindow = null;
        for (Window window : display.getRootWindow().tree().children()) {
            if (window.id == windowID) {
                tmpwindow = window;
            }
        }
        return tmpwindow;
    }

    /**
	 * Gets the default display of this ManagedXDisplay.
	 * 
	 * @return: The default display registered with this ManagedXDisplay.
	 */
    public Display getDisplay() {
        return display;
    }

    /**
	 * Set the default display of this ManagedXDisplay.
	 * 
	 * @param display
	 *            : The display you want to register with this ManagedXDisplay.
	 */
    public void setDisplay(Display managedDisplay) {
        this.display = managedDisplay;
    }

    /**
	 * Returns the event listener registered for this managed display.
	 * 
	 * @return The event listener active for this display.
	 */
    public XDisplayEventListener getEventListener() {
        return eventListener;
    }

    /**
	 * Set's an event listener that listens for events on this managed display.
	 * 
	 * @param eventListener
	 *            The event listener that listens for events
	 */
    private void setEventListener(XDisplayEventListener eventListener) {
        this.eventListener = eventListener;
        this.eventListener.setDisplay(getDisplay());
    }

    /**
	 * Gets the message producer for this managed display.
	 * 
	 * @return The message producer that produces messengers.
	 */
    public XEventMessageProducer getMessageProducer() {
        return messageProducer;
    }

    /**
	 * Sets the message producer for this managed display.
	 * 
	 * @param messageProducer
	 *            The message producer that produces messengers for this managed
	 *            display.
	 */
    private void setMessageProducer(XEventMessageProducer messageProducer) {
        this.messageProducer = messageProducer;
        this.messageProducer.setXEventManager(getEventManager());
    }

    /**
	 * Gets the event manager for this managed display.
	 * 
	 * @return The event manager that manages events that it receives from
	 *         messengers produces by the message producer.
	 */
    public XEventManager getEventManager() {
        return eventManager;
    }

    /**
	 * Sets the event manager for this managed display.
	 * 
	 * @param eventManager
	 *            The event manager that manages events for this managed
	 *            display.
	 */
    private void setEventManager(XEventManager eventManager) {
        this.eventManager = eventManager;
        this.eventManager.setManagedXDisplay(this);
    }

    public Map<Integer, EWindow> getWindowContainerMap() {
        return windowContainerMap;
    }
}
