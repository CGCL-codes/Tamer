package org.nakedobjects.ide.core.launches;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

class MakeDirtyModifyListener implements ModifyListener {

    private NakedObjectsArgumentsTab tab;

    public NakedObjectsArgumentsTab getTab() {
        return tab;
    }

    MakeDirtyModifyListener(NakedObjectsArgumentsTab tab) {
        this.tab = tab;
    }

    public void modifyText(ModifyEvent e) {
        NakedObjectsArgumentsTab.getLOGGER().debug("Modify text; source = " + e.widget.toString());
        getTab().setDirty(true);
        getTab().updateLaunchConfigurationDialog();
    }
}
