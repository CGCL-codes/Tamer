package org.lcelb.accounts.manager.ui.workbench.actions.navigator.create;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.lcelb.accounts.manager.data.Account;
import org.lcelb.accounts.manager.ui.extensions.AccountsManagerUIExtensionsActivator;
import org.lcelb.accounts.manager.ui.extensions.IImageKeys;
import org.lcelb.accounts.manager.ui.extensions.wizards.NewYearTransactionsWizard;
import org.lcelb.accounts.manager.ui.workbench.actions.navigator.AbstractNavigatorAction;
import org.lcelb.accounts.manager.ui.workbench.message.Messages;

/**
 * Created on 1 dec. 06
 * 
 * @author fournier
 * 
 */
public class NewYearTransactionsAction extends AbstractNavigatorAction {

    private Account _account;

    /**
   * @param selectionProvider_p
   */
    public NewYearTransactionsAction(Shell shell_p, ISelectionProvider selectionProvider_p) {
        super(shell_p, selectionProvider_p);
        setText(Messages.NewYearTransactionsAction_Title);
        setToolTipText(Messages.NewYearTransactionsAction_Tooltip);
        setImageDescriptor(AccountsManagerUIExtensionsActivator.getDefault().getImageDescriptor(IImageKeys.IMG_NEW));
        setId("org.lcelb.accounts.manager.ui.workbench.actions.navigator.create.NewYearTransactionsAction");
    }

    /**
   * @see org.lcelb.accounts.manager.ui.workbench.actions.navigator.AbstractNavigatorAction#getModelElementClass()
   */
    @Override
    protected Class<?> getModelElementClass() {
        return Account.class;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.workbench.actions.navigator.AbstractNavigatorAction#setSelectedElement(java.lang.Object)
   */
    @Override
    protected void setSelectedElement(Object object_p) {
        _account = (Account) object_p;
    }

    /**
   * @see org.eclipse.jface.action.Action#run()
   */
    @Override
    public void run() {
        NewYearTransactionsWizard wizard = new NewYearTransactionsWizard(_account);
        openWizardDialog(wizard);
    }
}
