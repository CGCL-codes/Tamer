package org.ws4d.osgi.eventConverter.util;

import java.util.Hashtable;
import java.util.Vector;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;
import org.ws4d.osgi.eventConverter.Activator;

public class EventConverterProperties {

    private static EventConverterProperties instance;

    private Hashtable properties = new Hashtable();

    private Vector propertyListener = new Vector();

    ServiceTracker configAdminTracker;

    public static synchronized EventConverterProperties getInstance() {
        if (instance == null) {
            instance = new EventConverterProperties();
        }
        return instance;
    }

    private EventConverterProperties() {
        configAdminTracker = new ServiceTracker(Activator.getBC(), ConfigurationAdmin.class.getName(), null);
        configAdminTracker.open();
        initProperties();
    }

    private void initProperties() {
    }

    public void setProperty(String property, Object value) {
        if (properties == null) {
            properties = new Hashtable();
        }
        if (!value.equals(properties.get(property))) {
            properties.put(property, value);
            notifyListeners(property);
        }
    }

    /**
	 * Add <code>String</code> property to properties table.
	 * 
	 * @param name
	 *            name of the property.
	 * @param value
	 *            value of the property.
	 */
    public void setProperty(String name, String value) {
        setProperty(name, (Object) value);
    }

    /**
	 * Add <code>int</code> property to properties table.
	 * 
	 * @param name
	 *            name of the property.
	 * @param value
	 *            value of the property.
	 */
    public void setProperty(String name, int value) {
        setProperty(name, (Object) new Integer(value));
    }

    /**
	 * Add <code>long</code> property toproperties table.
	 * 
	 * @param name
	 *            name of the property.
	 * @param value
	 *            value of the property.
	 */
    public void setProperty(String name, long value) {
        setProperty(name, (Object) new Long(value));
    }

    /**
	 * Add <code>boolean</code> property to properties table.
	 * 
	 * @param name
	 *            name of the property.
	 * @param value
	 *            value of the property.
	 */
    public void setProperty(String name, boolean value) {
        setProperty(name, (Object) new Boolean(value));
    }

    public Object getProperty(String name) {
        if (properties == null) {
            return null;
        }
        return properties.get(name);
    }

    public boolean getBooleanProperty(String name) {
        return ((Boolean) getProperty(name)).booleanValue();
    }

    public int getIntProperty(String name) {
        return ((Integer) getProperty(name)).intValue();
    }

    public String getStringProperty(String name) {
        return (String) getProperty(name);
    }

    public void registerPropertyListener(IPropertyCallback listener) {
        propertyListener.add(listener);
    }

    public void removePropertyListener(IPropertyCallback listener, String property) {
        propertyListener.remove(listener);
    }

    private void notifyListeners(String property) {
        for (int i = 0; i < propertyListener.size(); i++) {
            IPropertyCallback listener = (IPropertyCallback) propertyListener.elementAt(i);
            listener.propertyChanged(property);
        }
    }
}
