package ti.plato.components.ui.oscript;

import org.eclipse.jface.preference.IPreferenceStore;
import ti.plato.components.ui.oscript.internal.file.PlatoEditorUtil;

/**
 * Plato editor integration plugin save container.
 *
 * @author alex.k@ti.com
 */
public class WorkspaceSaveContainer {

    public static final String PRE = PlatoOScriptUIComponentActivator.PLUGIN_ID + ".";

    public static final String KEY_NEW_FILE_DIALOG__LAST_USED_DIRECTORY = PRE + "newFileDialog_last_used_directory";

    private IPreferenceStore _store;

    /**
	 * @param activator
	 *
	 * @author alex.k@ti.com
	 */
    public WorkspaceSaveContainer(PlatoOScriptUIComponentActivator activator) {
        _store = activator.getPreferenceStore();
        activator.getDialogSettings();
        setDefaults();
    }

    private static WorkspaceSaveContainer getContainer() {
        return PlatoOScriptUIComponentActivator.getWorkspaceSaveContainer();
    }

    private void setDefaults() {
        _store.setDefault(KEY_NEW_FILE_DIALOG__LAST_USED_DIRECTORY, PlatoEditorUtil.getScriptsHomeDir());
    }

    /**
	 * @param key
	 * @return
	 *
	 * @author alex.k@ti.com
	 */
    public static String getGlobalString(String key) {
        return getContainer()._store.getString(key);
    }

    /**
	 * @param key
	 * @param value
	 *
	 * @author alex.k@ti.com
	 */
    public static void setGlobalString(String key, String value) {
        getContainer()._store.setValue(key, value);
    }
}
