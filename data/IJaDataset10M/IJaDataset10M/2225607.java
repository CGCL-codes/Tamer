package net.sf.elbe.ui.views.browser;

import net.sf.elbe.ui.ELBEUIConstants;
import net.sf.elbe.ui.ELBEUIPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

public class ShowDirectoryMetadataEntriesAction extends Action {

    public ShowDirectoryMetadataEntriesAction() {
        super("Show Directory Metadata", IAction.AS_CHECK_BOX);
        super.setEnabled(true);
        super.setChecked(ELBEUIPlugin.getDefault().getPreferenceStore().getBoolean(ELBEUIConstants.PREFERENCE_BROWSER_SHOW_DIRECTORY_META_ENTRIES));
    }

    public void run() {
        ELBEUIPlugin.getDefault().getPreferenceStore().setValue(ELBEUIConstants.PREFERENCE_BROWSER_SHOW_DIRECTORY_META_ENTRIES, super.isChecked());
    }

    public void dispose() {
    }
}
