package fw4ex.authoride.plugin.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import fw4ex.authoride.plugin.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_TEXT_AREA_DOUBLE_CLICK, true);
        store.setDefault(PreferenceConstants.P_MOUSE_AUX, "3");
        store.setDefault(PreferenceConstants.P_TEXT_AREA_MOUSE_GESTURES, "None");
        store.setDefault(PreferenceConstants.P_TEXT_WINDOW_LOSE_FOCUS, true);
        PreferenceConverter.setDefault(store, PreferenceConstants.P_TEXT_WINDOW_FONT, new FontData());
        store.setDefault(PreferenceConstants.P_TEXT_WINDOW_WIDTH, 600);
        store.setDefault(PreferenceConstants.P_TEXT_WINDOW_HEIGHT, 500);
        store.setDefault(PreferenceConstants.P_AUTH_ADDRESS, "http://x.paracamplus.com");
        store.setDefault(PreferenceConstants.P_LOGIN, "");
        store.setDefault(PreferenceConstants.P_PASSWORD, "");
        store.setDefault(PreferenceConstants.P_SUB_ADDRESS, "http://e.paracamplus.com");
        store.setDefault(PreferenceConstants.P_CHECK_ADDRESS, "http://s.paracamplus.com");
        store.setDefault(PreferenceConstants.P_ALWAYS_ADDRESS, false);
        store.setDefault(PreferenceConstants.P_CHECK_INTERVAL, 5);
        store.setDefault(PreferenceConstants.P_SUB_DELAY, 10);
        store.setDefault(PreferenceConstants.P_CHECK_ATTEMPTS, 10);
        store.setDefault(PreferenceConstants.P_KEEP_TARGZ, true);
        store.setDefault(PreferenceConstants.P_REPORTS_FOLDER, "/tmp");
        store.setDefault(PreferenceConstants.P_SAVE_REPORTS, true);
        store.setDefault(PreferenceConstants.P_LOCAL_CSS, false);
        store.setDefault(PreferenceConstants.P_CSS_FILE, "");
        store.setDefault(PreferenceConstants.P_FIRST_NAME, "");
        store.setDefault(PreferenceConstants.P_MIDDLE_NAME, "");
        store.setDefault(PreferenceConstants.P_LAST_NAME, "");
        store.setDefault(PreferenceConstants.P_POST_LAST_NAME, "");
        store.setDefault(PreferenceConstants.P_EMAIL, "");
        store.setDefault(PreferenceConstants.P_ALWAYS_NAME, false);
        store.setDefault(PreferenceConstants.P_EXTERN_GRAMMAR, false);
        store.setDefault(PreferenceConstants.P_GRAMMAR_FILE, "");
        PreferenceConverter.setDefault(store, PreferenceConstants.XML_COMMENT, new RGB(128, 0, 0));
        PreferenceConverter.setDefault(store, PreferenceConstants.PROC_INSTR, new RGB(128, 128, 128));
        PreferenceConverter.setDefault(store, PreferenceConstants.STRING, new RGB(0, 128, 0));
        PreferenceConverter.setDefault(store, PreferenceConstants.DEFAULT, new RGB(0, 0, 0));
        PreferenceConverter.setDefault(store, PreferenceConstants.TAG, new RGB(0, 0, 128));
    }
}
