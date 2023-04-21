package org.akrogen.tkui.gui.jface.wizards;

import org.akrogen.tkui.core.gui.wizards.ITkuiWizardContainer;
import org.akrogen.tkui.gui.jface.wizards.dynamic.DynamicWizardDialog;
import org.akrogen.tkui.gui.jface.wizards.dynamic.IDynamicWizard;
import org.eclipse.swt.widgets.Shell;

public abstract class TkuiWizardDialog extends DynamicWizardDialog implements ITkuiWizardContainer {

    public TkuiWizardDialog(Shell shell, IDynamicWizard wizard) {
        super(shell, wizard);
    }
}
