package org.pwsafe.passwordsafeswt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.dialog.EditDialog;
import org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants;

/**
 * Adds a new record to the password safe.
 *
 * @author Glen Smith
 */
public class AddRecordAction extends Action {

    public AddRecordAction() {
        super(Messages.getString("AddRecordAction.Label"));
        setAccelerator(SWT.MOD1 | 'A');
        setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader().getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_add.gif")));
        setToolTipText(Messages.getString("AddRecordAction.Tooltip"));
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        PasswordSafeJFace app = PasswordSafeJFace.getApp();
        PwsEntryBean newEntry = PwsEntryBean.fromPwsRecord(app.getPwsFile().newRecord());
        IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
        if (thePrefs.getBoolean(JpwPreferenceConstants.USE_DEFAULT_USERNAME)) {
            newEntry.setUsername(thePrefs.getString(JpwPreferenceConstants.DEFAULT_USERNAME));
        }
        if (app.isTreeViewShowing()) {
            String selectedGroup = app.getSelectedTreeGroupPath();
            if (selectedGroup != null && selectedGroup.length() > 0) {
                newEntry.setGroup(selectedGroup);
            }
        }
        EditDialog ed = new EditDialog(app.getShell(), newEntry);
        newEntry = (PwsEntryBean) ed.open();
        if (newEntry != null) {
            newEntry.setSparse(false);
            app.addRecord(newEntry);
        }
    }
}
