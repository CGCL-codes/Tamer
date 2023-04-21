package net.kano.joustsim.app.forms.prefs;

import javax.swing.Icon;
import java.awt.Component;

public interface PrefsPane {

    String getPlainPrefsName();

    boolean isGlobalPrefs();

    Icon getSmallPrefsIcon();

    String getPrefsName();

    String getPrefsDescription();

    Component getPrefsComponent();

    void prefsWindowFocused();

    void prefsWindowFocusLost();

    void prefsPaneToBeShown();

    void prefsPaneShown();

    void prefsPaneHidden();
}
