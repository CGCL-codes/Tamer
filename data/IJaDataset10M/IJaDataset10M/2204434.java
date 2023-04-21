package com.aptana.ide.core.ui.views.fileexplorer;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.preference.IPreferenceStore;
import com.aptana.ide.core.ui.CoreUIPlugin;
import com.aptana.ide.core.ui.preferences.FileExtensionPreferencePage;
import com.aptana.ide.core.ui.preferences.IPreferenceConstants;

/**
 * The file editors page presents the collection of file names and extensions for which the user has registered editors.
 * It also lets the user add new internal or external (program) editors for a given file name and extension. The user
 * can add an editor for either a specific file name and extension (e.g. report.doc), or for all file names of a given
 * extension (e.g. *.doc) The set of registered editors is tracked by the EditorRegistery available from the workbench
 * plugin.
 */
public class FileExplorerPreferencePage extends FileExtensionPreferencePage {

    /**
	 * @see com.aptana.ide.core.ui.preferences.FileExtensionPreferencePage#getTableDescription()
	 */
    protected String getTableDescription() {
        return Messages.FileExplorerPreferencePage_AddExtensions;
    }

    /**
	 * @see com.aptana.ide.core.ui.preferences.FileExtensionPreferencePage#doGetPreferenceID()
	 */
    protected String doGetPreferenceID() {
        return IPreferenceConstants.PREF_FILE_EXPLORER_WEB_FILES;
    }

    /**
	 * @see org.eclipse.jface.preference.PreferencePage#doGetPreferenceStore()
	 */
    protected IPreferenceStore doGetPreferenceStore() {
        return CoreUIPlugin.getDefault().getPreferenceStore();
    }

    /**
	 * @see com.aptana.ide.core.ui.preferences.FileExtensionPreferencePage#doGetPlugin()
	 */
    protected Plugin doGetPlugin() {
        return CoreUIPlugin.getDefault();
    }
}
