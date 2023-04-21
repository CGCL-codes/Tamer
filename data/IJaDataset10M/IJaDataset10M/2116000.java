package org.springframework.richclient.settings.j2seprefs;

import java.io.IOException;
import java.util.Arrays;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.springframework.richclient.settings.AbstractSettings;
import org.springframework.richclient.settings.Settings;

/**
 * Settings implementation using the J2SE Preferences API. <br>
 * Not using the PreferenceChangeListener to implement PropertyChangeListener
 * support, because we also need the old value.
 * 
 * @author Peter De Bruycker
 */
public class PreferencesSettings extends AbstractSettings {

    private Preferences prefs;

    /**
     * Create the root.
     */
    public PreferencesSettings(String name) {
        this(null, name);
    }

    /**
     * Create a child with the given name.
     */
    public PreferencesSettings(PreferencesSettings parent, String name) {
        super(parent, name);
        if (parent != null) {
            prefs = parent.getPreferences().node(name);
        } else {
            prefs = Preferences.userRoot().node(name);
        }
    }

    /**
     * @param preferences
     */
    public PreferencesSettings(Preferences preferences) {
        super(null, preferences.name());
        this.prefs = preferences;
    }

    public Preferences getPreferences() {
        return prefs;
    }

    public void save() throws IOException {
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            IOException ioe = new IOException("Unable to save settings");
            ioe.initCause(e);
            throw ioe;
        }
    }

    public void load() throws IOException {
        try {
            prefs.sync();
        } catch (BackingStoreException e) {
            IOException ioe = new IOException("Unable to save settings");
            ioe.initCause(e);
            throw ioe;
        }
    }

    protected String internalGet(String key) {
        if (prefs.get(key, "").equals("")) {
            return null;
        }
        return prefs.get(key, "");
    }

    protected Settings internalCreateChild(String key) {
        return new PreferencesSettings(this, key);
    }

    protected void internalSet(String key, String value) {
        prefs.put(key, value);
    }

    public String[] getKeys() {
        try {
            return prefs.keys();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

    protected void internalRemove(String key) {
        prefs.remove(key);
    }

    public boolean internalContains(String key) {
        try {
            return Arrays.asList(prefs.keys()).contains(key);
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

    protected String[] internalGetChildSettings() {
        try {
            return prefs.childrenNames();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

    protected void internalRemoveSettings() {
        try {
            prefs.removeNode();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }
}
