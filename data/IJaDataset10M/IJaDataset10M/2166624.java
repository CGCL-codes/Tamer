package de.sciss.eisenkraut.io;

import java.io.File;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import de.sciss.app.AbstractApplication;
import de.sciss.io.CacheManager;
import de.sciss.util.Param;
import de.sciss.util.ParamSpace;

/**
 *  @author		Hanns Holger Rutz
 *  @version	0.70, 28-Jul-07
 */
public class PrefCacheManager extends CacheManager implements PreferenceChangeListener {

    /**
	 *	Convenient name for preferences node
	 */
    public static final String DEFAULT_NODE = "cache";

    public static final String KEY_ACTIVE = "active";

    public static final String KEY_FOLDER = "folder";

    public static final String KEY_CAPACITY = "capacity";

    private final Preferences prefs;

    private static final Param DEFAULT_CAPACITY = new Param(100, ParamSpace.NONE | ParamSpace.ABS);

    private static PrefCacheManager instance;

    public PrefCacheManager(Preferences prefs) {
        super();
        if (instance != null) throw new IllegalStateException("Only one instance allowed");
        instance = this;
        this.prefs = prefs;
        final int capacity;
        final File folder;
        final boolean active;
        if (prefs.get(KEY_CAPACITY, null) == null) {
            capacity = (int) DEFAULT_CAPACITY.val;
            folder = new File(new File(System.getProperty("user.home"), AbstractApplication.getApplication().getName()), "cache");
            active = false;
        } else {
            capacity = (int) Param.fromPrefs(prefs, KEY_CAPACITY, DEFAULT_CAPACITY).val;
            folder = new File(prefs.get(KEY_FOLDER, ""));
            active = prefs.getBoolean(KEY_ACTIVE, false);
        }
        setFolderAndCapacity(folder, capacity);
        setActive(active);
        prefs.addPreferenceChangeListener(this);
    }

    public static PrefCacheManager getInstance() {
        return instance;
    }

    public Preferences getPreferences() {
        return prefs;
    }

    public void setActive(boolean onOff) {
        super.setActive(onOff);
        prefs.putBoolean(KEY_ACTIVE, onOff);
    }

    public void setFolderAndCapacity(File folder, int capacity) {
        super.setFolderAndCapacity(folder, capacity);
        prefs.put(KEY_FOLDER, folder.getPath());
        prefs.put(KEY_CAPACITY, new Param(capacity, ParamSpace.NONE | ParamSpace.ABS).toString());
    }

    public void preferenceChange(PreferenceChangeEvent e) {
        final String key = e.getKey();
        if (key.equals(KEY_FOLDER)) {
            final File f = new File(e.getNewValue());
            if ((getFolder() == null) || !(getFolder().equals(f))) {
                setFolder(f);
            }
        } else if (key.equals(KEY_CAPACITY)) {
            final int c = (int) Param.fromPrefs(prefs, key, DEFAULT_CAPACITY).val;
            if (getCapacity() != c) {
                setCapacity(c);
            }
        } else if (key.equals(KEY_ACTIVE)) {
            final boolean b = Boolean.valueOf(e.getNewValue()).booleanValue();
            if (isActive() != b) {
                setActive(b);
            }
        }
    }
}
