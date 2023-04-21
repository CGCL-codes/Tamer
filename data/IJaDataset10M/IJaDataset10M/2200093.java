package com.aptana.ide.editor.php.preferences;

import org.eclipse.ui.IWorkbench;
import com.aptana.ide.editor.php.PHPPlugin;
import com.aptana.ide.editors.preferences.EditorPreferencePage;

/**
 * @author Kevin Lindsey
 *
 */
public class GeneralPreferencePage extends EditorPreferencePage {

    /**
	 * GeneralPreferencePage
	 */
    public GeneralPreferencePage() {
        super(GRID);
        setPreferenceStore(PHPPlugin.getDefault().getPreferenceStore());
        setDescription(Messages.GeneralPreferencePage_GeneralPreferencesDescription + "\n\n");
    }

    /**
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
    public void init(IWorkbench workbench) {
    }
}
