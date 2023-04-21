package org.lcelb.accounts.manager.ui.workbench.actions.navigator.edit;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.lcelb.accounts.manager.data.AbstractOwner;
import org.lcelb.accounts.manager.data.extensions.transaction.payment.DefaultPayment;
import org.lcelb.accounts.manager.ui.UIHelper;
import org.lcelb.accounts.manager.ui.workbench.message.Messages;

/**
 * Action to delete a payment.<br>
 * 
 * @author fournier <br>
 * 
 * 4 dec. 06
 */
public class DeletePaymentAction extends DeleteElementAction {

    private DefaultPayment _payment;

    public DeletePaymentAction(Shell shell_p, ISelectionProvider selectionProvider_p) {
        super(shell_p, selectionProvider_p);
        setText(Messages.DeletePaymentAction_Title);
        setToolTipText(Messages.DeletePaymentAction_Tooltip);
    }

    /**
   * @see org.lcelb.accounts.manager.ui.workbench.actions.navigator.AbstractNavigatorAction#getModelElementClass()
   */
    @Override
    protected Class<?> getModelElementClass() {
        return DefaultPayment.class;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.workbench.actions.navigator.AbstractNavigatorAction#setSelectedElement(java.lang.Object)
   */
    @Override
    protected void setSelectedElement(Object object_p) {
        _payment = (DefaultPayment) object_p;
    }

    /**
   * @see org.eclipse.jface.action.Action#run()
   */
    @Override
    public void run() {
        String[] arguments = { Messages.SelectedPayment_Message, _payment.getName() };
        if (UIHelper.openDeleteConfirmationDialog(getShell(), arguments)) {
            AbstractOwner owner = (AbstractOwner) _payment.eContainer();
            owner.getPayments().remove(_payment);
            markDirty();
        }
    }
}
