package org.eclipse.core.internal.preferences;

import java.io.*;
import java.util.Iterator;
import java.util.Properties;
import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

/**
 * This class represents a convenience layer between the Eclipse 3.0
 * preferences and pre-3.0 preferences. It acts as a bridge between the 
 * org.eclipse.core.runtime.Preferences object associated with a particular plug-in
 * object, and its corresponding preference node in the 3.0 preference node
 * hierarchy.
 * 
 * @since 3.0
 */
public class PreferenceForwarder extends Preferences implements IEclipsePreferences.IPreferenceChangeListener, IEclipsePreferences.INodeChangeListener {

    private static final byte[] BYTE_ARRAY_DEFAULT_DEFAULT = new byte[0];

    private IEclipsePreferences pluginRoot = (IEclipsePreferences) Platform.getPreferencesService().getRootNode().node(Plugin.PLUGIN_PREFERENCE_SCOPE);

    private DefaultPreferences defaultsRoot = (DefaultPreferences) Platform.getPreferencesService().getRootNode().node(DefaultScope.SCOPE);

    private String pluginID;

    private Plugin plugin;

    private boolean notify = true;

    public PreferenceForwarder(String pluginID) {
        this(null, pluginID);
    }

    public PreferenceForwarder(Plugin plugin, String pluginID) {
        super();
        this.plugin = plugin;
        this.pluginID = pluginID;
        pluginRoot.addNodeChangeListener(this);
    }

    public void added(IEclipsePreferences.NodeChangeEvent event) {
        if (listeners.size() > 0 && pluginID.equals(event.getChild().name())) getPluginPreferences(true).addPreferenceChangeListener(this);
    }

    public void removed(IEclipsePreferences.NodeChangeEvent event) {
    }

    /**
	 * Adds a property change listener to this preference object.
	 * Has no affect if the identical listener is already registered.
	 *
	 * @param listener a property change listener
	 */
    public void addPropertyChangeListener(IPropertyChangeListener listener) {
        getPluginPreferences(true).addPreferenceChangeListener(this);
        listeners.add(listener);
    }

    public void preferenceChange(IEclipsePreferences.PreferenceChangeEvent event) {
        if (!notify) return;
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        String key = event.getKey();
        if (newValue == null) newValue = getDefault(key, oldValue); else if (oldValue == null) oldValue = getDefault(key, newValue);
        firePropertyChangeEvent(key, oldValue, newValue);
    }

    private EclipsePreferences getPluginPreferences(boolean create) {
        try {
            if (!create && !pluginRoot.nodeExists(pluginID)) return null;
        } catch (BackingStoreException e) {
            return null;
        }
        try {
            return (EclipsePreferences) pluginRoot.node(pluginID);
        } catch (ClassCastException e) {
            throw new RuntimeException("Plug-in preferences must be instances of EclipsePreferences: " + e.getMessage());
        }
    }

    private IEclipsePreferences getDefaultPreferences() {
        return defaultsRoot.node(pluginID, plugin);
    }

    /**
	 * Removes the given listener from this preference object.
	 * Has no affect if the listener is not registered.
	 *
	 * @param listener a property change listener
	 */
    public void removePropertyChangeListener(IPropertyChangeListener listener) {
        listeners.remove(listener);
    }

    /**
	 * Does its best at determining the default value for the given key. Checks the
	 * given object's type and then looks in the list of defaults to see if a value
	 * exists. If not or if there is a problem converting the value, the default default 
	 * value for that type is returned.
	 */
    private Object getDefault(String key, Object obj) {
        IEclipsePreferences defaults = getDefaultPreferences();
        if (obj instanceof String) return defaults.get(key, STRING_DEFAULT_DEFAULT); else if (obj instanceof Integer) return new Integer(defaults.getInt(key, INT_DEFAULT_DEFAULT)); else if (obj instanceof Double) return new Double(defaults.getDouble(key, DOUBLE_DEFAULT_DEFAULT)); else if (obj instanceof Float) return new Float(defaults.getFloat(key, FLOAT_DEFAULT_DEFAULT)); else if (obj instanceof Long) return new Long(defaults.getLong(key, LONG_DEFAULT_DEFAULT)); else if (obj instanceof byte[]) return defaults.getByteArray(key, BYTE_ARRAY_DEFAULT_DEFAULT); else if (obj instanceof Boolean) return new Boolean(defaults.getBoolean(key, BOOLEAN_DEFAULT_DEFAULT)); else return null;
    }

    /**
	 * Returns whether the given property is known to this preference object,
	 * either by having an explicit setting or by having a default
	 * setting.
	 *
	 * @param name the name of the property
	 * @return <code>true</code> if either a current value or a default
	 *  value is known for the named property, and <code>false</code>otherwise
	 */
    public boolean contains(String name) {
        if (name == null) return false;
        String value = getPluginPreferences(true).get(name, null);
        if (value != null) return true;
        return getDefaultPreferences().get(name, null) != null;
    }

    /**
	 * Returns the current value of the boolean-valued property with the
	 * given name.
	 * Returns the default-default value (<code>false</code>) if there
	 * is no property with the given name, or if the current value 
	 * cannot be treated as a boolean.
	 *
	 * @param name the name of the property
	 * @return the boolean-valued property
	 */
    public boolean getBoolean(String name) {
        return getPluginPreferences(true).getBoolean(name, getDefaultPreferences().getBoolean(name, BOOLEAN_DEFAULT_DEFAULT));
    }

    /**
	 * Sets the current value of the boolean-valued property with the
	 * given name.
	 * <p>
	 * A property change event is reported if the current value of the 
	 * property actually changes from its previous value. In the event
	 * object, the property name is the name of the property, and the
	 * old and new values are wrapped as objects.
	 * </p>
	 * <p>
	 * If the given value is the same as the corresponding default value
	 * for the given property, the explicit setting is deleted.
	 * Note that the recommended way of re-initializing a property to its
	 * default value is to call <code>setToDefault</code>.
	 * </p>
	 *
	 * @param name the name of the property
	 * @param value the new current value of the property
	 */
    public void setValue(String name, boolean value) {
        Boolean oldValue = getBoolean(name) ? Boolean.TRUE : Boolean.FALSE;
        Boolean newValue = value ? Boolean.TRUE : Boolean.FALSE;
        if (newValue == oldValue) return;
        try {
            notify = false;
            if (getDefaultBoolean(name) == value) getPluginPreferences(true).remove(name); else getPluginPreferences(true).putBoolean(name, value);
            firePropertyChangeEvent(name, oldValue, newValue);
        } finally {
            notify = true;
        }
    }

    /**
	 * Returns the default value for the boolean-valued property
	 * with the given name.
	 * Returns the default-default value (<code>false</code>) if there
	 * is no default property with the given name, or if the default 
	 * value cannot be treated as a boolean.
	 *
	 * @param name the name of the property
	 * @return the default value of the named property
	 */
    public boolean getDefaultBoolean(String name) {
        return getDefaultPreferences().getBoolean(name, BOOLEAN_DEFAULT_DEFAULT);
    }

    /**
	 * Sets the default value for the boolean-valued property with the
	 * given name. 
	 * <p>
	 * Note that the current value of the property is affected if
	 * the property's current value was its old default value, in which
	 * case it changes to the new default value. If the property's current
	 * is different from its old default value, its current value is
	 * unaffected. No property change events are reported by changing default
	 * values.
	 * </p>
	 *
	 * @param name the name of the property
	 * @param value the new default value for the property
	 */
    public void setDefault(String name, boolean value) {
        getDefaultPreferences().putBoolean(name, value);
    }

    /**
	 * Returns the current value of the double-valued property with the
	 * given name.
	 * Returns the default-default value (<code>0.0</code>) if there
	 * is no property with the given name, or if the current value 
	 * cannot be treated as a double.
	 *
	 * @param name the name of the property
	 * @return the double-valued property
	 */
    public double getDouble(String name) {
        return getPluginPreferences(true).getDouble(name, getDefaultPreferences().getDouble(name, DOUBLE_DEFAULT_DEFAULT));
    }

    /**
	 * Sets the current value of the double-valued property with the
	 * given name.
	 * <p>
	 * A property change event is reported if the current value of the 
	 * property actually changes from its previous value. In the event
	 * object, the property name is the name of the property, and the
	 * old and new values are wrapped as objects.
	 * </p>
	 * <p>
	 * If the given value is the same as the corresponding default value
	 * for the given property, the explicit setting is deleted.
	 * Note that the recommended way of re-initializing a property to its
	 * default value is to call <code>setToDefault</code>.
	 * </p>
	 *
	 * @param name the name of the property
	 * @param value the new current value of the property; must be 
	 *   a number (not a NaN)
	 */
    public void setValue(String name, double value) {
        if (Double.isNaN(value)) throw new IllegalArgumentException();
        final double doubleValue = getDouble(name);
        if (value == doubleValue) return;
        Double oldValue = new Double(doubleValue);
        Double newValue = new Double(value);
        try {
            notify = false;
            if (getDefaultDouble(name) == value) getPluginPreferences(true).remove(name); else getPluginPreferences(true).putDouble(name, value);
            firePropertyChangeEvent(name, oldValue, newValue);
        } finally {
            notify = true;
        }
    }

    /**
	 * Returns the default value for the double-valued property
	 * with the given name.
	 * Returns the default-default value (<code>0.0</code>) if there
	 * is no default property with the given name, or if the default 
	 * value cannot be treated as a double.
	 *
	 * @param name the name of the property
	 * @return the default value of the named property
	 */
    public double getDefaultDouble(String name) {
        return getDefaultPreferences().getDouble(name, DOUBLE_DEFAULT_DEFAULT);
    }

    /**
	 * Sets the default value for the double-valued property with the
	 * given name. 
	 * <p>
	 * Note that the current value of the property is affected if
	 * the property's current value was its old default value, in which
	 * case it changes to the new default value. If the property's current
	 * is different from its old default value, its current value is
	 * unaffected. No property change events are reported by changing default
	 * values.
	 * </p>
	 *
	 * @param name the name of the property
	 * @param value the new default value for the property; must be 
	 *   a number (not a NaN)
	 */
    public void setDefault(String name, double value) {
        if (Double.isNaN(value)) throw new IllegalArgumentException();
        getDefaultPreferences().putDouble(name, value);
    }

    /**
	 * Returns the current value of the float-valued property with the
	 * given name.
	 * Returns the default-default value (<code>0.0f</code>) if there
	 * is no property with the given name, or if the current value 
	 * cannot be treated as a float.
	 *
	 * @param name the name of the property
	 * @return the float-valued property
	 */
    public float getFloat(String name) {
        return getPluginPreferences(true).getFloat(name, getDefaultPreferences().getFloat(name, FLOAT_DEFAULT_DEFAULT));
    }

    /**
	 * Sets the current value of the float-valued property with the
	 * given name.
	 * <p>
	 * A property change event is reported if the current value of the 
	 * property actually changes from its previous value. In the event
	 * object, the property name is the name of the property, and the
	 * old and new values are wrapped as objects.
	 * </p>
	 * <p>
	 * If the given value is the same as the corresponding default value
	 * for the given property, the explicit setting is deleted.
	 * Note that the recommended way of re-initializing a property to its
	 * default value is to call <code>setToDefault</code>.
	 * </p>
	 *
	 * @param name the name of the property
	 * @param value the new current value of the property; must be 
	 *   a number (not a NaN)
	 */
    public void setValue(String name, float value) {
        if (Float.isNaN(value)) throw new IllegalArgumentException();
        final float floatValue = getFloat(name);
        if (value == floatValue) return;
        Float oldValue = new Float(floatValue);
        Float newValue = new Float(value);
        try {
            notify = false;
            if (getDefaultFloat(name) == value) getPluginPreferences(true).remove(name); else getPluginPreferences(true).putFloat(name, value);
            firePropertyChangeEvent(name, oldValue, newValue);
        } finally {
            notify = true;
        }
    }

    /**
	 * Returns the default value for the float-valued property
	 * with the given name.
	 * Returns the default-default value (<code>0.0f</code>) if there
	 * is no default property with the given name, or if the default 
	 * value cannot be treated as a float.
	 *
	 * @param name the name of the property
	 * @return the default value of the named property
	 */
    public float getDefaultFloat(String name) {
        return getDefaultPreferences().getFloat(name, FLOAT_DEFAULT_DEFAULT);
    }

    /**
	 * Sets the default value for the float-valued property with the
	 * given name. 
	 * <p>
	 * Note that the current value of the property is affected if
	 * the property's current value was its old default value, in which
	 * case it changes to the new default value. If the property's current
	 * is different from its old default value, its current value is
	 * unaffected. No property change events are reported by changing default
	 * values.
	 * </p>
	 *
	 * @param name the name of the property
	 * @param value the new default value for the property; must be 
	 *   a number (not a NaN)
	 */
    public void setDefault(String name, float value) {
        if (Float.isNaN(value)) throw new IllegalArgumentException();
        getDefaultPreferences().putFloat(name, value);
    }

    /**
	 * Returns the current value of the integer-valued property with the
	 * given name.
	 * Returns the default-default value (<code>0</code>) if there
	 * is no property with the given name, or if the current value 
	 * cannot be treated as an integter.
	 *
	 * @param name the name of the property
	 * @return the int-valued property
	 */
    public int getInt(String name) {
        return getPluginPreferences(true).getInt(name, getDefaultPreferences().getInt(name, INT_DEFAULT_DEFAULT));
    }

    /**
	 * Sets the current value of the integer-valued property with the
	 * given name.
	 * <p>
	 * A property change event is reported if the current value of the 
	 * property actually changes from its previous value. In the event
	 * object, the property name is the name of the property, and the
	 * old and new values are wrapped as objects.
	 * </p>
	 * <p>
	 * If the given value is the same as the corresponding default value
	 * for the given property, the explicit setting is deleted.
	 * Note that the recommended way of re-initializing a property to its
	 * default value is to call <code>setToDefault</code>.
	 * </p>
	 *
	 * @param name the name of the property
	 * @param value the new current value of the property
	 */
    public void setValue(String name, int value) {
        final int intValue = getInt(name);
        if (value == intValue) return;
        Integer oldValue = new Integer(intValue);
        Integer newValue = new Integer(value);
        try {
            notify = false;
            if (getDefaultInt(name) == value) getPluginPreferences(true).remove(name); else getPluginPreferences(true).putInt(name, value);
            firePropertyChangeEvent(name, oldValue, newValue);
        } finally {
            notify = true;
        }
    }

    /**
	 * Returns the default value for the integer-valued property
	 * with the given name.
	 * Returns the default-default value (<code>0</code>) if there
	 * is no default property with the given name, or if the default 
	 * value cannot be treated as an integer.
	 *
	 * @param name the name of the property
	 * @return the default value of the named property
	 */
    public int getDefaultInt(String name) {
        return getDefaultPreferences().getInt(name, INT_DEFAULT_DEFAULT);
    }

    /**
	 * Sets the default value for the integer-valued property with the
	 * given name. 
	 * <p>
	 * Note that the current value of the property is affected if
	 * the property's current value was its old default value, in which
	 * case it changes to the new default value. If the property's current
	 * is different from its old default value, its current value is
	 * unaffected. No property change events are reported by changing default
	 * values.
	 * </p>
	 *
	 * @param name the name of the property
	 * @param value the new default value for the property
	 */
    public void setDefault(String name, int value) {
        getDefaultPreferences().putInt(name, value);
    }

    /**
	 * Returns the current value of the long-valued property with the
	 * given name.
	 * Returns the default-default value (<code>0L</code>) if there
	 * is no property with the given name, or if the current value 
	 * cannot be treated as a long.
	 *
	 * @param name the name of the property
	 * @return the long-valued property
	 */
    public long getLong(String name) {
        return getPluginPreferences(true).getLong(name, getDefaultPreferences().getLong(name, LONG_DEFAULT_DEFAULT));
    }

    /**
	 * Sets the current value of the long-valued property with the
	 * given name.
	 * <p>
	 * A property change event is reported if the current value of the 
	 * property actually changes from its previous value. In the event
	 * object, the property name is the name of the property, and the
	 * old and new values are wrapped as objects.
	 * </p>
	 * <p>
	 * If the given value is the same as the corresponding default value
	 * for the given property, the explicit setting is deleted.
	 * Note that the recommended way of re-initializing a property to its
	 * default value is to call <code>setToDefault</code>.
	 * </p>
	 *
	 * @param name the name of the property
	 * @param value the new current value of the property
	 */
    public void setValue(String name, long value) {
        final long longValue = getLong(name);
        if (value == longValue) return;
        Long oldValue = new Long(longValue);
        Long newValue = new Long(value);
        try {
            notify = false;
            if (getDefaultLong(name) == value) getPluginPreferences(true).remove(name); else getPluginPreferences(true).putLong(name, value);
            firePropertyChangeEvent(name, oldValue, newValue);
        } finally {
            notify = true;
        }
    }

    /**
	 * Returns the default value for the long-valued property
	 * with the given name.
	 * Returns the default-default value (<code>0L</code>) if there
	 * is no default property with the given name, or if the default 
	 * value cannot be treated as a long.
	 *
	 * @param name the name of the property
	 * @return the default value of the named property
	 */
    public long getDefaultLong(String name) {
        return getDefaultPreferences().getLong(name, LONG_DEFAULT_DEFAULT);
    }

    /**
	 * Sets the default value for the long-valued property with the
	 * given name. 
	 * <p>
	 * Note that the current value of the property is affected if
	 * the property's current value was its old default value, in which
	 * case it changes to the new default value. If the property's current
	 * is different from its old default value, its current value is
	 * unaffected. No property change events are reported by changing default
	 * values.
	 * </p>
	 *
	 * @param name the name of the property
	 * @param value the new default value for the property
	 */
    public void setDefault(String name, long value) {
        getDefaultPreferences().putLong(name, value);
    }

    /**
	 * Returns the current value of the string-valued property with the
	 * given name.
	 * Returns the default-default value (the empty string <code>""</code>)
	 * if there is no property with the given name.
	 *
	 * @param name the name of the property
	 * @return the string-valued property
	 */
    public String getString(String name) {
        return getPluginPreferences(true).get(name, getDefaultPreferences().get(name, STRING_DEFAULT_DEFAULT));
    }

    /**
	 * Sets the current value of the string-valued property with the
	 * given name.
	 * <p>
	 * A property change event is reported if the current value of the 
	 * property actually changes from its previous value. In the event
	 * object, the property name is the name of the property, and the
	 * old and new values are wrapped as objects.
	 * </p>
	 * <p>
	 * If the given value is the same as the corresponding default value
	 * for the given property, the explicit setting is deleted.
	 * Note that the recommended way of re-initializing a property to its
	 * default value is to call <code>setToDefault</code>.
	 * </p>
	 *
	 * @param name the name of the property
	 * @param value the new current value of the property
	 */
    public void setValue(String name, String value) {
        if (value == null) throw new IllegalArgumentException();
        String oldValue = getString(name);
        if (value.equals(oldValue)) return;
        try {
            notify = false;
            if (getDefaultString(name).equals(value)) getPluginPreferences(true).remove(name); else getPluginPreferences(true).put(name, value);
            firePropertyChangeEvent(name, oldValue, value);
        } finally {
            notify = true;
        }
    }

    /**
	 * Returns the default value for the string-valued property
	 * with the given name.
	 * Returns the default-default value (the empty string <code>""</code>) 
	 * is no default property with the given name, or if the default 
	 * value cannot be treated as a string.
	 *
	 * @param name the name of the property
	 * @return the default value of the named property
	 */
    public String getDefaultString(String name) {
        return getDefaultPreferences().get(name, STRING_DEFAULT_DEFAULT);
    }

    /**
	 * Sets the default value for the string-valued property with the
	 * given name. 
	 * <p>
	 * Note that the current value of the property is affected if
	 * the property's current value was its old default value, in which
	 * case it changes to the new default value. If the property's current
	 * is different from its old default value, its current value is
	 * unaffected. No property change events are reported by changing default
	 * values.
	 * </p>
	 *
	 * @param name the name of the property
	 * @param value the new default value for the property
	 */
    public void setDefault(String name, String value) {
        if (value == null) throw new IllegalArgumentException();
        getDefaultPreferences().put(name, value);
    }

    /**
	 * Returns whether the property with the given name has the default value in
	 * virtue of having no explicitly set value.
	 *
	 * @param name the name of the property
	 * @return <code>true</code> if the property has no explicitly set value,
	 * and <code>false</code> otherwise (including the case where the property
	 * is unknown to this object)
	 */
    public boolean isDefault(String name) {
        if (name == null) return false;
        return getPluginPreferences(true).get(name, null) == null;
    }

    /**
	 * Sets the current value of the property with the given name back
	 * to its default value. Has no effect if the property does not have
	 * its own current value.
	 * <p>
	 * Note that the recommended way of re-initializing a property to the
	 * appropriate default value is to call <code>setToDefault</code>.
	 * This is implemented by removing the named value from the object, 
	 * thereby exposing the default value.
	 * </p>
	 * <p>
	 * A property change event is always reported. In the event
	 * object, the property name is the name of the property, and the
	 * old and new values are either strings, or <code>null</code> 
	 * indicating the default-default value.
	 * </p>
	 *
	 * @param name the name of the property
	 */
    public void setToDefault(String name) {
        IEclipsePreferences preferences = getPluginPreferences(true);
        Object oldValue = preferences.get(name, null);
        if (oldValue != null) preferences.remove(name);
    }

    /**
	 * Returns a list of all properties known to this preference object which
	 * have current values other than their default value.
	 *
	 * @return an array of property names 
	 */
    public String[] propertyNames() {
        return getPluginPreferences(true).keys();
    }

    /**
	 * Returns a list of all properties known to this preference object which
	 * have default values other than their default-default value.
	 *
	 * @return an array of property names 
	 */
    public String[] defaultPropertyNames() {
        try {
            return getDefaultPreferences().keys();
        } catch (BackingStoreException e) {
            logError(e.getMessage(), e);
            return new String[0];
        }
    }

    /**
	 * Returns whether the current values in this preference object
	 * require saving.
	 *
	 * @return <code>true</code> if at least one of the properties
	 *  known to this preference object has a current value different from its
	 *  default value, and <code>false</code> otherwise
	 */
    public boolean needsSaving() {
        return getPluginPreferences(true).dirty;
    }

    /**
	 * Flush the values of these plug-in preferences to disk.
	 * 
	 * @throws BackingStoreException
	 */
    public void flush() throws BackingStoreException {
        IEclipsePreferences node = getPluginPreferences(false);
        if (node != null) node.flush();
    }

    private void logError(String message, Exception e) {
        IStatus status = new Status(IStatus.ERROR, Platform.PI_RUNTIME, IStatus.ERROR, message, e);
        InternalPlatform.getDefault().log(status);
    }

    public void load(InputStream in) throws IOException {
        Properties result = new Properties();
        result.load(in);
        convertFromProperties(result);
        try {
            flush();
        } catch (BackingStoreException e) {
            throw new IOException(e.getMessage());
        }
    }

    public void store(OutputStream out, String header) throws IOException {
        Properties result = convertToProperties();
        result.store(out, header);
        try {
            flush();
        } catch (BackingStoreException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void convertFromProperties(Properties props) {
        IEclipsePreferences preferences = getPluginPreferences(true);
        for (Iterator i = props.keySet().iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            String value = props.getProperty(key);
            if (value != null) preferences.put(key, value);
        }
    }

    public String toString() {
        return "PreferenceForwarder(" + pluginID + ")";
    }

    private Properties convertToProperties() {
        Properties result = new Properties();
        String[] keys = propertyNames();
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            String value = getString(key);
            if (!Preferences.STRING_DEFAULT_DEFAULT.equals(value)) result.put(key, value);
        }
        return result;
    }
}
