package org.xulbooster.eclipse.xb.ui.snippets.item.tabBox;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.wst.common.snippets.internal.SnippetsPlugin;
import org.xulbooster.eclipse.xb.ui.snippets.utils.XulItem;

public class TabBox extends XulItem {

    public TabBox() {
        super();
    }

    public String run(TabBoxWizard aWizard) {
        WizardDialog dialog = new WizardDialog(SnippetsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), aWizard);
        if (dialog.open() == WizardDialog.OK) {
            return aWizard.getResult();
        } else {
            return "";
        }
    }

    public String getInsertString() {
        return run(new TabBoxWizard());
    }
}
