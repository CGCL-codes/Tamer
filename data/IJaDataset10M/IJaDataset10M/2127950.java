package org.skyfree.ghyll.tcard.wizard;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class MyWizardDialog extends WizardDialog {

    public MyWizardDialog(Shell parentShell, IWizard newWizard) {
        super(parentShell, newWizard);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        getButton(IDialogConstants.CANCEL_ID).setText("ȡ��");
        getButton(IDialogConstants.FINISH_ID).setText("����");
        getButton(IDialogConstants.BACK_ID).setText("����");
        getButton(IDialogConstants.NEXT_ID).setText("ǰ��");
    }
}
