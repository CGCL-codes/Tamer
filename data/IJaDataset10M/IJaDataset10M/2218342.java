package org.gdbi.util.list;

import java.security.AccessControlException;
import java.util.prefs.Preferences;

/**
 * This class only has a static methods for getting the
 * top-level GDBI Preferences (under org\gdbi).
 *
 * There is a bug on Windows where the top-level node does not exist, so all
 * Preferences fail.  The fix it to run regedit and manually create:
 * HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft\Prefs
 */
public class UListPreferences {

    private static final String NAME_TOP = "gdbi";

    private static final String NAME_OPEN = "open";

    public static Preferences getGdbi() {
        Preferences prefPackage = null;
        try {
            prefPackage = Preferences.userNodeForPackage(UListPreferences.class);
        } catch (AccessControlException e) {
        }
        Preferences prefTop = prefPackage;
        while ((prefTop != null) && (!NAME_TOP.equals(prefTop.name()))) {
            prefTop = prefTop.parent();
        }
        final Preferences out = (prefTop == null) ? prefPackage : prefTop;
        return out;
    }

    public static Preferences getGdbiOpen() {
        final Preferences top = getGdbi();
        if (top == null) return null;
        final Preferences out = top.node(NAME_OPEN);
        return out;
    }
}
