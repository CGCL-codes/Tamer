package org.dengues.commons.ui.swt.colorstyledtext.jedit;

import java.util.Map;
import java.util.TreeMap;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 1 2006-09-29 17:06:40Z qiang.zhang $
 * 
 */
public class Modes {

    protected static Modes soleInstace = new Modes();

    protected boolean hasBeenLoaded = false;

    protected Map<String, Mode> modes;

    protected Mode[] modeList;

    public static Modes getSoleInstance() {
        return soleInstace;
    }

    protected Modes() {
        super();
        modes = new TreeMap<String, Mode>();
    }

    public static Mode getMode(String name) {
        return getSoleInstance().getModeNamed(name);
    }

    public static Mode getModeFor(String filename) {
        return getSoleInstance().getModeForFilename(filename);
    }

    private Mode getModeForFilename(String filename) {
        if (filename == null) {
            return getModeNamed("text.xml");
        }
        String modeName = filenameToModeName(filename);
        if (modeName == null) {
            return getModeNamed("text.xml");
        }
        return getModeNamed(modeName);
    }

    private String filenameToModeName(String filename) {
        Mode[] localModes = getModeList();
        if (localModes == null) {
            return null;
        }
        for (int i = 0; i < localModes.length; i++) {
            Mode mode = localModes[i];
            if (mode.matches(filename)) {
                return mode.getFilename();
            }
        }
        return null;
    }

    /**
     * Answer a sorted array containing all of the modes defined by the catalog.
     * 
     * @return Mode[]
     */
    public Mode[] getModeList() {
        if (modeList == null) {
            loadCatalog();
        }
        return modeList;
    }

    public static void load() {
        getSoleInstance().loadCatalog();
    }

    protected void loadCatalog() {
        CatalogReader reader = new CatalogReader();
        modeList = reader.read("modes/catalog");
        for (int i = 0; i < modeList.length; i++) {
            Mode mode = modeList[i];
            modes.put(mode.getFilename(), mode);
        }
    }

    protected Mode getModeNamed(String name) {
        loadIfNecessary(name);
        return modes.get(name);
    }

    private void loadIfNecessary(String name) {
        Mode hull = modes.get(name);
        if (hull == null) {
            loadCatalog();
            if (modes.size() == 0) {
                return;
            }
            hull = modes.get(name);
        }
        if (hull.notLoaded()) {
            hull.load();
        }
    }

    public static Rule resolveDelegate(Mode mode, String delegateName) {
        int index = delegateName.indexOf("::");
        if (index == -1) {
            return mode.getRule(delegateName);
        }
        Mode loadedMode = getMode(delegateName.substring(0, index) + ".xml");
        return loadedMode.getRule(delegateName.substring(index + 2));
    }
}
