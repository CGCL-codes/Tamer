package de.walware.eclipsecommons.ui.preferences;

import java.io.IOException;
import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.NodeChangeEvent;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.osgi.service.prefs.BackingStoreException;

/**
 * The ScopedPreferenceStore is an IPreferenceStore that uses the scopes
 * provided in org.eclipse.core.runtime.preferences.
 * <p>
 * A ScopedPreferenceStore does the lookup of a preference based on it's search
 * scopes and sets the value of the preference based on its store scope.
 * </p>
 * <p>
 * The default scope is always included in the search scopes when searching for
 * preference values.
 * </p>
 * 
 * @see org.eclipse.core.runtime.preferences
 * @since 3.1
 */
public class ScopedPreferenceStore extends EventManager implements IPreferenceStore, IPersistentPreferenceStore {

    /**
	 * The storeContext is the context where values will stored with the
	 * setValue methods. If there are no searchContexts this will be the search
	 * context. (along with the "default" context)
	 */
    private final IScopeContext storeContext;

    /**
	 * The searchContext is the array of contexts that will be used by the get
	 * methods for searching for values.
	 */
    private IScopeContext[] searchContexts;

    /**
	 * A boolean to indicate the property changes should not be propagated.
	 */
    protected boolean silentRunning = false;

    /**
	 * The listener on the IEclipsePreferences. This is used to forward updates
	 * to the property change listeners on the preference store.
	 */
    IEclipsePreferences.IPreferenceChangeListener preferencesListener;

    private IEclipsePreferences.IPreferenceChangeListener[] searchPreferencesListeners;

    /**
	 * The default context is the context where getDefault and setDefault
	 * methods will search. This context is also used in the search.
	 */
    private final IScopeContext defaultContext = new DefaultScope();

    /**
	 * The nodeQualifer is the string used to look up the node in the contexts.
	 */
    String nodeQualifier;

    /**
	 * The defaultQualifier is the string used to look up the default node.
	 */
    String defaultQualifier;

    /**
	 * Boolean value indicating whether or not this store has changes to be
	 * saved.
	 */
    private boolean dirty;

    /**
	 * Create a new instance of the receiver. Store the values in context in the
	 * node looked up by qualifier. <strong>NOTE:</strong> Any instance of
	 * ScopedPreferenceStore should call
	 * 
	 * @param context
	 *            the scope to store to
	 * @param qualifier
	 *            the qualifier used to look up the preference node
	 * @param defaultQualifierPath
	 *            the qualifier used when looking up the defaults
	 */
    public ScopedPreferenceStore(final IScopeContext context, final String qualifier, final String defaultQualifierPath) {
        this(context, qualifier);
        this.defaultQualifier = defaultQualifierPath;
    }

    /**
	 * Create a new instance of the receiver. Store the values in context in the
	 * node looked up by qualifier.
	 * 
	 * @param context
	 *            the scope to store to
	 * @param qualifier
	 *            the qualifer used to look up the preference node
	 */
    public ScopedPreferenceStore(final IScopeContext context, final String qualifier) {
        storeContext = context;
        this.nodeQualifier = qualifier;
        this.defaultQualifier = qualifier;
        ((IEclipsePreferences) getStorePreferences().parent()).addNodeChangeListener(getNodeChangeListener());
    }

    /**
	 * Return a node change listener that adds a removes the receiver when nodes
	 * change.
	 * 
	 * @return INodeChangeListener
	 */
    private INodeChangeListener getNodeChangeListener() {
        return new IEclipsePreferences.INodeChangeListener() {

            public void added(final NodeChangeEvent event) {
                if (nodeQualifier.equals(event.getChild().name()) && isListenerAttached()) {
                    getStorePreferences().addPreferenceChangeListener(preferencesListener);
                }
            }

            public void removed(final NodeChangeEvent event) {
            }
        };
    }

    /**
	 * Initialize the preferences listener.
	 */
    private void initializePreferencesListener() {
        if (searchContexts == null && preferencesListener == null) {
            preferencesListener = new IEclipsePreferences.IPreferenceChangeListener() {

                public void preferenceChange(final PreferenceChangeEvent event) {
                    if (silentRunning) {
                        return;
                    }
                    Object oldValue = event.getOldValue();
                    Object newValue = event.getNewValue();
                    final String key = event.getKey();
                    if (newValue == null) {
                        newValue = getDefault(key, oldValue);
                    } else if (oldValue == null) {
                        oldValue = getDefault(key, newValue);
                    }
                    firePropertyChangeEvent(event.getKey(), oldValue, newValue);
                }
            };
            getStorePreferences().addPreferenceChangeListener(preferencesListener);
        } else if (searchContexts != null && searchPreferencesListeners == null) {
            searchPreferencesListeners = new IEclipsePreferences.IPreferenceChangeListener[searchContexts.length];
            for (int i = 0; i < searchContexts.length; i++) {
                final int idxSearchContext = i;
                searchPreferencesListeners[idxSearchContext] = new IEclipsePreferences.IPreferenceChangeListener() {

                    public void preferenceChange(final PreferenceChangeEvent event) {
                        if (silentRunning) {
                            return;
                        }
                        final IEclipsePreferences[] preferences = getPreferenceNodes(true);
                        final String key = event.getKey();
                        Object oldValue = event.getOldValue();
                        Object newValue = null;
                        int idxNewContext = preferences.length;
                        for (int j = 0; j < preferences.length; j++) {
                            final String value = preferences[j].get(key, null);
                            if (value != null) {
                                if (newValue == null) {
                                    newValue = value;
                                    idxNewContext = j;
                                } else if (oldValue == null) {
                                    oldValue = value;
                                }
                            }
                            if (newValue != null && oldValue != null) {
                                break;
                            }
                        }
                        if (idxNewContext < idxSearchContext) {
                            return;
                        }
                        if (newValue == null) {
                            newValue = getDefault(key, oldValue);
                        }
                        if (oldValue == null) {
                            oldValue = getDefault(key, newValue);
                        }
                        if (oldValue != null && oldValue.equals(newValue)) {
                            return;
                        }
                        firePropertyChangeEvent(event.getKey(), oldValue, newValue);
                    }
                };
                searchContexts[i].getNode(nodeQualifier).addPreferenceChangeListener(searchPreferencesListeners[i]);
            }
        }
    }

    /**
	 * Does its best at determining the default value for the given key. Checks
	 * the given object's type and then looks in the list of defaults to see if
	 * a value exists. If not or if there is a problem converting the value, the
	 * default default value for that type is returned.
	 * 
	 * @param key
	 *            the key to search
	 * @param obj
	 *            the object who default we are looking for
	 * @return Object or <code>null</code>
	 */
    Object getDefault(final String key, final Object obj) {
        final IEclipsePreferences defaults = getDefaultPreferences();
        if (obj instanceof String) {
            return defaults.get(key, STRING_DEFAULT_DEFAULT);
        } else if (obj instanceof Integer) {
            return new Integer(defaults.getInt(key, INT_DEFAULT_DEFAULT));
        } else if (obj instanceof Double) {
            return new Double(defaults.getDouble(key, DOUBLE_DEFAULT_DEFAULT));
        } else if (obj instanceof Float) {
            return new Float(defaults.getFloat(key, FLOAT_DEFAULT_DEFAULT));
        } else if (obj instanceof Long) {
            return new Long(defaults.getLong(key, LONG_DEFAULT_DEFAULT));
        } else if (obj instanceof Boolean) {
            return new Boolean(defaults.getBoolean(key, BOOLEAN_DEFAULT_DEFAULT));
        } else {
            return null;
        }
    }

    /**
	 * Return the IEclipsePreferences node associated with this store.
	 * 
	 * @return the preference node for this store
	 */
    IEclipsePreferences getStorePreferences() {
        return storeContext.getNode(nodeQualifier);
    }

    /**
	 * Return the default IEclipsePreferences for this store.
	 * 
	 * @return this store's default preference node
	 */
    private IEclipsePreferences getDefaultPreferences() {
        return defaultContext.getNode(defaultQualifier);
    }

    public void addPropertyChangeListener(final IPropertyChangeListener listener) {
        initializePreferencesListener();
        addListenerObject(listener);
    }

    /**
	 * Return the preference path to search preferences on. This is the list of
	 * preference nodes based on the scope contexts for this store. If there are
	 * no search contexts set, then return this store's context.
	 * <p>
	 * Whether or not the default context should be included in the resulting
	 * list is specified by the <code>includeDefault</code> parameter.
	 * </p>
	 * 
	 * @param includeDefault
	 *            <code>true</code> if the default context should be included
	 *            and <code>false</code> otherwise
	 * @return IEclipsePreferences[]
	 */
    private IEclipsePreferences[] getPreferenceNodes(final boolean includeDefault) {
        if (searchContexts == null) {
            if (includeDefault) {
                return new IEclipsePreferences[] { getStorePreferences(), getDefaultPreferences() };
            }
            return new IEclipsePreferences[] { getStorePreferences() };
        }
        int length = searchContexts.length;
        if (includeDefault) {
            length++;
        }
        final IEclipsePreferences[] preferences = new IEclipsePreferences[length];
        for (int i = 0; i < searchContexts.length; i++) {
            preferences[i] = searchContexts[i].getNode(nodeQualifier);
        }
        if (includeDefault) {
            preferences[length - 1] = getDefaultPreferences();
        }
        return preferences;
    }

    /**
	 * Set the search contexts to scopes. When searching for a value the seach
	 * will be done in the order of scope contexts and will not search the
	 * storeContext unless it is in this list.
	 * <p>
	 * If the given list is <code>null</code>, then clear this store's search
	 * contexts. This means that only this store's scope context and default
	 * scope will be used during preference value searching.
	 * </p>
	 * <p>
	 * The defaultContext will be added to the end of this list automatically
	 * and <em>MUST NOT</em> be included by the user.
	 * </p>
	 * 
	 * @param scopes
	 *            a list of scope contexts to use when searching, or
	 *            <code>null</code>
	 */
    public void setSearchContexts(final IScopeContext[] scopes) {
        if (scopes != null) {
            for (final IScopeContext element : scopes) {
                if (element.equals(defaultContext)) {
                    Assert.isTrue(false, WorkbenchMessages.ScopedPreferenceStore_DefaultAddedError);
                }
            }
        }
        if (isListenerAttached()) {
            disposePreferenceStoreListener();
        }
        this.searchContexts = scopes;
        if (isListenerAttached()) {
            initializePreferencesListener();
        }
    }

    public boolean contains(final String name) {
        if (name == null) {
            return false;
        }
        return (Platform.getPreferencesService().get(name, null, getPreferenceNodes(true))) != null;
    }

    public void firePropertyChangeEvent(final String name, final Object oldValue, final Object newValue) {
        final Object[] list = getListeners();
        if (list.length == 0) {
            return;
        }
        final PropertyChangeEvent event = new PropertyChangeEvent(this, name, oldValue, newValue);
        for (final Object element : list) {
            final IPropertyChangeListener listener = (IPropertyChangeListener) element;
            SafeRunner.run(new SafeRunnable(JFaceResources.getString("PreferenceStore.changeError")) {

                public void run() {
                    listener.propertyChange(event);
                }
            });
        }
    }

    public boolean getBoolean(final String name) {
        final String value = internalGet(name);
        return value == null ? BOOLEAN_DEFAULT_DEFAULT : Boolean.valueOf(value).booleanValue();
    }

    public boolean getDefaultBoolean(final String name) {
        return getDefaultPreferences().getBoolean(name, BOOLEAN_DEFAULT_DEFAULT);
    }

    public double getDefaultDouble(final String name) {
        return getDefaultPreferences().getDouble(name, DOUBLE_DEFAULT_DEFAULT);
    }

    public float getDefaultFloat(final String name) {
        return getDefaultPreferences().getFloat(name, FLOAT_DEFAULT_DEFAULT);
    }

    public int getDefaultInt(final String name) {
        return getDefaultPreferences().getInt(name, INT_DEFAULT_DEFAULT);
    }

    public long getDefaultLong(final String name) {
        return getDefaultPreferences().getLong(name, LONG_DEFAULT_DEFAULT);
    }

    public String getDefaultString(final String name) {
        return getDefaultPreferences().get(name, STRING_DEFAULT_DEFAULT);
    }

    public double getDouble(final String name) {
        final String value = internalGet(name);
        if (value == null) {
            return DOUBLE_DEFAULT_DEFAULT;
        }
        try {
            return Double.parseDouble(value);
        } catch (final NumberFormatException e) {
            return DOUBLE_DEFAULT_DEFAULT;
        }
    }

    /**
	 * Return the string value for the specified key. Look in the nodes which
	 * are specified by this object's list of search scopes. If the value does
	 * not exist then return <code>null</code>.
	 * 
	 * @param key
	 *            the key to search with
	 * @return String or <code>null</code> if the value does not exist.
	 */
    private String internalGet(final String key) {
        return Platform.getPreferencesService().get(key, null, getPreferenceNodes(true));
    }

    public float getFloat(final String name) {
        final String value = internalGet(name);
        if (value == null) {
            return FLOAT_DEFAULT_DEFAULT;
        }
        try {
            return Float.parseFloat(value);
        } catch (final NumberFormatException e) {
            return FLOAT_DEFAULT_DEFAULT;
        }
    }

    public int getInt(final String name) {
        final String value = internalGet(name);
        if (value == null) {
            return INT_DEFAULT_DEFAULT;
        }
        try {
            return Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            return INT_DEFAULT_DEFAULT;
        }
    }

    public long getLong(final String name) {
        final String value = internalGet(name);
        if (value == null) {
            return LONG_DEFAULT_DEFAULT;
        }
        try {
            return Long.parseLong(value);
        } catch (final NumberFormatException e) {
            return LONG_DEFAULT_DEFAULT;
        }
    }

    public String getString(final String name) {
        final String value = internalGet(name);
        return value == null ? STRING_DEFAULT_DEFAULT : value;
    }

    public boolean isDefault(final String name) {
        if (name == null) {
            return false;
        }
        return (Platform.getPreferencesService().get(name, null, getPreferenceNodes(false))) == null;
    }

    public boolean needsSaving() {
        return dirty;
    }

    public void putValue(final String name, final String value) {
        try {
            silentRunning = true;
            getStorePreferences().put(name, value);
        } finally {
            silentRunning = false;
            dirty = true;
        }
    }

    public void removePropertyChangeListener(final IPropertyChangeListener listener) {
        removeListenerObject(listener);
        if (!isListenerAttached()) {
            disposePreferenceStoreListener();
        }
    }

    public void setDefault(final String name, final double value) {
        getDefaultPreferences().putDouble(name, value);
    }

    public void setDefault(final String name, final float value) {
        getDefaultPreferences().putFloat(name, value);
    }

    public void setDefault(final String name, final int value) {
        getDefaultPreferences().putInt(name, value);
    }

    public void setDefault(final String name, final long value) {
        getDefaultPreferences().putLong(name, value);
    }

    public void setDefault(final String name, final String defaultObject) {
        getDefaultPreferences().put(name, defaultObject);
    }

    public void setDefault(final String name, final boolean value) {
        getDefaultPreferences().putBoolean(name, value);
    }

    public void setToDefault(final String name) {
        final String oldValue = getString(name);
        final String defaultValue = getDefaultString(name);
        try {
            silentRunning = true;
            getStorePreferences().remove(name);
            dirty = true;
            firePropertyChangeEvent(name, oldValue, defaultValue);
        } finally {
            silentRunning = false;
        }
    }

    public void setValue(final String name, final double value) {
        final double oldValue = getDouble(name);
        if (oldValue == value) {
            return;
        }
        try {
            silentRunning = true;
            if (getDefaultDouble(name) == value) {
                getStorePreferences().remove(name);
            } else {
                getStorePreferences().putDouble(name, value);
            }
            dirty = true;
            firePropertyChangeEvent(name, new Double(oldValue), new Double(value));
        } finally {
            silentRunning = false;
        }
    }

    public void setValue(final String name, final float value) {
        final float oldValue = getFloat(name);
        if (oldValue == value) {
            return;
        }
        try {
            silentRunning = true;
            if (getDefaultFloat(name) == value) {
                getStorePreferences().remove(name);
            } else {
                getStorePreferences().putFloat(name, value);
            }
            dirty = true;
            firePropertyChangeEvent(name, new Float(oldValue), new Float(value));
        } finally {
            silentRunning = false;
        }
    }

    public void setValue(final String name, final int value) {
        final int oldValue = getInt(name);
        if (oldValue == value) {
            return;
        }
        try {
            silentRunning = true;
            if (getDefaultInt(name) == value) {
                getStorePreferences().remove(name);
            } else {
                getStorePreferences().putInt(name, value);
            }
            dirty = true;
            firePropertyChangeEvent(name, new Integer(oldValue), new Integer(value));
        } finally {
            silentRunning = false;
        }
    }

    public void setValue(final String name, final long value) {
        final long oldValue = getLong(name);
        if (oldValue == value) {
            return;
        }
        try {
            silentRunning = true;
            if (getDefaultLong(name) == value) {
                getStorePreferences().remove(name);
            } else {
                getStorePreferences().putLong(name, value);
            }
            dirty = true;
            firePropertyChangeEvent(name, new Long(oldValue), new Long(value));
        } finally {
            silentRunning = false;
        }
    }

    public void setValue(final String name, final String value) {
        if (getDefaultString(name).equals(value)) {
            getStorePreferences().remove(name);
        } else {
            getStorePreferences().put(name, value);
        }
        dirty = true;
    }

    public void setValue(final String name, final boolean value) {
        final boolean oldValue = getBoolean(name);
        if (oldValue == value) {
            return;
        }
        try {
            silentRunning = true;
            if (getDefaultBoolean(name) == value) {
                getStorePreferences().remove(name);
            } else {
                getStorePreferences().putBoolean(name, value);
            }
            dirty = true;
            firePropertyChangeEvent(name, new Boolean(oldValue), new Boolean(value));
        } finally {
            silentRunning = false;
        }
    }

    public void save() throws IOException {
        try {
            getStorePreferences().flush();
            dirty = false;
        } catch (final BackingStoreException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
	 * Dispose the receiver.
	 */
    private void disposePreferenceStoreListener() {
        final IEclipsePreferences root = (IEclipsePreferences) Platform.getPreferencesService().getRootNode().node(Plugin.PLUGIN_PREFERENCE_SCOPE);
        try {
            if (!(root.nodeExists(nodeQualifier))) {
                return;
            }
        } catch (final BackingStoreException e) {
            return;
        }
        IEclipsePreferences preferences = getStorePreferences();
        if (preferences != null && preferencesListener != null) {
            preferences.removePreferenceChangeListener(preferencesListener);
            preferencesListener = null;
        }
        if (searchContexts != null && searchPreferencesListeners != null) {
            for (int i = 0; i < searchContexts.length; i++) {
                preferences = searchContexts[i].getNode(nodeQualifier);
                if (preferences != null) {
                    preferences.removePreferenceChangeListener(searchPreferencesListeners[i]);
                }
            }
        }
    }
}
