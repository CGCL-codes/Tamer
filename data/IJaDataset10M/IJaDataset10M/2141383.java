package org.xulbooster.eclipse.xb.ui.snippets.item.scrollbar;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.wst.common.snippets.internal.SnippetsPlugin;
import org.xulbooster.eclipse.xb.ui.snippets.utils.XulItem;

public class ScrollBar extends XulItem {

    public ScrollBar() {
        super();
    }

    public String run(ScrollBarWizard aWizard) {
        WizardDialog dialog = new WizardDialog(SnippetsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), aWizard);
        if (dialog.open() == WizardDialog.OK) {
            return aWizard.getResult();
        } else {
            return "";
        }
    }

    public String getInsertString() {
        return run(new ScrollBarWizard());
    }
}
