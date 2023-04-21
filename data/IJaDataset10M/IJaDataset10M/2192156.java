package net.sf.ulmac.ui.preferences;

import net.sf.ulmac.core.managers.PrefManager;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public abstract class AbstractPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    private void flush() {
        Preferences preferences = new DefaultScope().getNode(PlatformUI.PLUGIN_ID);
        try {
            preferences.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    protected abstract String getPageID();

    @Override
    public String getTitle() {
        PrefManager.setLastPreferencePageID(getPageID());
        return super.getTitle();
    }

    void savePrefence(String prefId, boolean prefValue) {
        getPreferenceStore().setValue(prefId, prefValue);
        flush();
    }

    void savePrefence(String prefId, double prefValue) {
        getPreferenceStore().setValue(prefId, prefValue);
        flush();
    }

    void savePrefence(String prefId, int prefValue) {
        getPreferenceStore().setValue(prefId, prefValue);
        flush();
    }

    void savePrefence(String prefId, String prefValue) {
        getPreferenceStore().setValue(prefId, prefValue);
        flush();
    }
}
