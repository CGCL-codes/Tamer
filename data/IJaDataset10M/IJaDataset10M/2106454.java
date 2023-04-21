package de.iritgo.openmetix.core.gui;

import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.base.BaseObject;
import de.iritgo.openmetix.core.iobject.IObject;
import de.iritgo.openmetix.core.iobject.IObjectProxy;
import de.iritgo.openmetix.core.iobject.IObjectProxyRegistry;
import de.iritgo.openmetix.core.sessioncontext.SessionContext;
import javax.swing.Icon;
import java.awt.Rectangle;
import java.util.Properties;

/**
 * IWindow
 *
 * @version $Id: IWindow.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IWindow extends BaseObject implements IDisplay {

    /** The window properties. */
    private Properties properties;

    /** The gui pane. */
    private GUIPane guiPane;

    /** The id of the desktop on which we are displayed. */
    private String desktopId;

    /** Our desktop manager. */
    private IDesktopManager desktopManager;

    /** Our window frame. */
    private IWindowFrame windowFrame;

    /**
	 * Create a new IWindow.
	 */
    public IWindow() {
        this("IWindow");
    }

    /**
	 * Create a new IWindow.
	 *
	 * @param windowId The window id.
	 */
    public IWindow(String windowId) {
        super(windowId);
        properties = new Properties();
    }

    /**
	 * Initialize the gui.
	 *
	 * @param guiPaneId The id of the gui pane that is to be displayed in this
	 *   window.
	 */
    public void initGUI(String guiPaneId) {
        initGUI(guiPaneId, null, null);
    }

    /**
	 * Initialize the gui.
	 *
	 * @param guiPaneId The id of the gui pane that is to be displayed in this
	 *   window.
	 * @param sessionContext The session contxt.
	 */
    public void initGUI(String guiPaneId, SessionContext sessionContext) {
        initGUI(guiPaneId, null, sessionContext);
    }

    /**
	 * Initialize the gui.
	 *
	 * @param guiPaneId The id of the gui pane that is to be displayed in this
	 *   window.
	 * @param object The IObject that is to be displayed in this window.
	 * @param sessionContext The session contxt.
	 */
    public void initGUI(String guiPaneId, IObject object, SessionContext sessionContext) {
        guiPane = (GUIPane) GUIPaneRegistry.instance().create(guiPaneId);
        guiPane.setObject(object);
        guiPane.setSessionContext(sessionContext);
        boolean resizable = true;
        boolean closable = true;
        boolean maximizable = true;
        boolean iconifiable = true;
        if (properties.get("resizable") != null) {
            resizable = ((Boolean) properties.get("resizable")).booleanValue();
        }
        if (properties.get("closable") != null) {
            closable = ((Boolean) properties.get("closable")).booleanValue();
        }
        if (properties.get("maximizable") != null) {
            maximizable = ((Boolean) properties.get("maximizable")).booleanValue();
        }
        if (properties.get("iconifiable") != null) {
            iconifiable = ((Boolean) properties.get("iconifiable")).booleanValue();
        }
        windowFrame = Engine.instance().getGUIFactory().createWindowFrame(this, getTypeId(), resizable, closable, maximizable, iconifiable);
        guiPane.setIDisplay(this);
        if (properties.get("bounds") != null) {
            windowFrame.setBounds((Rectangle) properties.get("bounds"));
        }
        if (properties.get("maximized") != null) {
            try {
                windowFrame.setMaximized(((Boolean) properties.get("maximized")).booleanValue());
            } catch (Exception x) {
            }
        }
        if (properties.get("title") != null) {
            windowFrame.setTitle((String) properties.get("title"));
        }
        guiPane.initGUI();
        if (properties.get("weightx") != null) {
            Rectangle bounds = windowFrame.getBounds();
            bounds.width *= ((Double) properties.get("weightx")).doubleValue();
            windowFrame.setBounds(bounds);
        }
        if (properties.get("weighty") != null) {
            Rectangle bounds = windowFrame.getBounds();
            bounds.height *= ((Double) properties.get("eightx")).doubleValue();
            windowFrame.setBounds(bounds);
        }
        if (object != null) {
            guiPane.registerProxyEventListener();
            IObjectProxyRegistry proxyRegistry = Engine.instance().getProxyRegistry();
            IObjectProxy prototypeProxy = (IObjectProxy) proxyRegistry.getProxy(object.getUniqueId());
            IObject prototypeable = prototypeProxy.getRealObject();
            guiPane.setObject(prototypeable);
            if (prototypeable != null) {
                loadFromObject();
            }
        }
    }

    /**
	 * Retrieve the gui pane of this window.
	 *
	 * @return The gui pane.
	 */
    public GUIPane getGUIPane() {
        return guiPane;
    }

    /**
	 * LoadFromObject, loads the GUI from object.
	 */
    public void loadFromObject() {
        guiPane.loadFromObject();
    }

    /**
	 * StoreToObject, save the GUI to object.
	 */
    public void storeToObject() {
        guiPane.storeToObject();
    }

    /**
	 * Get the id of the desktop on which this display is displayed.
	 *
	 * @return The desktop id (or null if this display is a dialog).
	 */
    public String getDesktopId() {
        return desktopId;
    }

    /**
	 * Set the id of the desktop on which this display is displayed.
	 *
	 * @param desktopId The desktop id.
	 */
    public void setDesktopId(String desktopId) {
        this.desktopId = desktopId;
    }

    /**
	 * Set the desktop manager.
	 *
	 * @param desktopManager The desktop manager.
	 */
    public void setDesktopManager(IDesktopManager desktopManager) {
        this.desktopManager = desktopManager;
    }

    /**
	 * Get the desktop manager.
	 *
	 * @return The desktop manager.
	 */
    public IDesktopManager getDesktopManager() {
        return desktopManager;
    }

    /**
	 * Close the window.
	 */
    public void close() {
        if (guiPane != null) {
            guiPane.close();
        }
        if (windowFrame != null) {
            windowFrame.close();
        }
        desktopManager.removeDisplay(this);
        Engine.instance().getEventRegistry().fire("iwindowframe.closed", new IDisplayClosedEvent(this));
    }

    /**
	 * Close the window.
	 */
    public void systemClose() {
        if (guiPane != null) {
            guiPane.systemClose();
        }
        if (windowFrame != null) {
            windowFrame.systemClose();
        }
        desktopManager.removeDisplay(this);
        Engine.instance().getEventRegistry().fire("iwindowframe.closed", new IDisplayClosedEvent(this));
    }

    /**
	 * Get the data object shown in this display.
	 *
	 * @return The data object.
	 */
    public IObject getDataObject() {
        return guiPane.getObject();
    }

    /**
	 * Set the window title. This title will be displayed on the window frame's
	 * title bar.
	 *
	 * @param title The new title.
	 */
    public void setTitle(String title) {
        windowFrame.setTitle(title);
    }

    /**
	 * Get the window title.
	 *
	 * @return The window title.
	 */
    public String getTitle() {
        return windowFrame.getTitle();
    }

    /**
	 * Set the window icon. This icon will be displayed on the window frame's
	 * title bar.
	 *
	 * @param icon The icon.
	 */
    public void setIcon(Icon icon) {
        windowFrame.setIcon(icon);
    }

    /**
	 * Get the display's icon.
	 *
	 * @return The display's icon.
	 */
    public Icon getIcon() {
        return windowFrame.getIcon();
    }

    /**
	 * Set a display property.
	 *
	 * @param key The key under which to store the property.
	 * @param value The property value.
	 */
    public void putProperty(String key, Object value) {
        properties.put(key, value);
    }

    /**
	 * Get a display property.
	 *
	 * @param key The key of the property to retrieve.
	 * @return The property value.
	 */
    public Object getProperty(String key) {
        return properties.get(key);
    }

    /**
	 * Set the display properties.
	 *
	 * @param properties The new properties.
	 */
    public void setProperties(Properties properties) {
        if (properties != null) {
            this.properties = properties;
        }
    }

    /**
	 * Get the display properties.
	 *
	 * @return The display properties.
	 */
    public Properties getProperties() {
        return properties;
    }

    /**
	 * Remove a display property.
	 *
	 * @param key The key of the property to remove.
	 */
    public void removeProperty(String key) {
        properties.remove(key);
    }

    /**
	 * Get the window frame of this IWindow
	 *
	 * @return The window frame.
	 */
    public IWindowFrame getWindowFrame() {
        return windowFrame;
    }

    /**
	 * Show the window frame.
	 */
    public void show() {
        windowFrame.showWindow();
        Engine.instance().getEventRegistry().fire("iwindowframe.opened", new IDisplayOpenedEvent(this));
    }

    /**
	 * Enable/disable the window.
	 *
	 * @param enabled If true the window is enabled.
	 */
    public void setEnabled(boolean enabled) {
        windowFrame.setEnabled(enabled);
    }
}
