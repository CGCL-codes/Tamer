package com.jacum.cms.rcp.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import com.jacum.cms.rcp.ui.Messages;

/**
 * Instances of this class allow the user to navigate
 * the file system and select a directory.
 * Extends SWT DirectoryDialog class for storing last selection int the dialog settings
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 * 
 * @author rich
 */
public class ChooseFolderDialog extends DirectoryDialog {

    private static final String TITLE = Messages.getString("ChooseFolderDialog.0");

    private static final String MESSAGE = Messages.getString("ChooseFolderDialog.1");

    private static final String LAST_PATH_KEY = "last_path";

    private static DialogSettingsProvider dialogSettingProvider = DialogSettingsProvider.getInstance();

    private static IDialogSettings dialogSettings = dialogSettingProvider.getSettings(ChooseFolderDialog.class.getCanonicalName());

    /**
	 * @param parent
	 */
    public ChooseFolderDialog(Shell parent) {
        super(parent);
        setText(TITLE);
        setMessage(MESSAGE);
        setFilterPath(getLastPath());
    }

    @Override
    public String open() {
        String path = super.open();
        if (path != null) {
            saveLastPath(path);
        }
        return path;
    }

    /**
	 * @return
	 */
    private String getLastPath() {
        return dialogSettings.get(LAST_PATH_KEY);
    }

    /**
	 * @param path
	 */
    private void saveLastPath(String path) {
        dialogSettings.put(LAST_PATH_KEY, path);
        dialogSettingProvider.saveSettings();
    }

    @Override
    protected void checkSubclass() {
    }
}
