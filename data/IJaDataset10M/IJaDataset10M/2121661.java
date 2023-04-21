package org.lcelb.accounts.manager.ui.workbench.actions;

import org.eclipse.ui.IWorkbenchWindow;
import org.lcelb.accounts.manager.ui.workbench.message.Messages;

public class NewWizardAction extends AbstractGlobalAction {

    /**
   * Creates a new <code>NewWizardAction</code>.
   * 
   * @param window the window
   */
    public NewWizardAction(IWorkbenchWindow workbenchWindow_p) {
        super(workbenchWindow_p);
        setId("org.lcelb.accounts.manager.ui.workbench.actions.newAction");
        setText(Messages.NewAction_Title);
        setToolTipText(Messages.NewAction_Tooltip);
        setEnabled(false);
    }
}
