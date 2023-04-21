package com.msli.rcp.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.widgets.Shell;
import com.msli.app.status.Continuance;

/**
 * Facade for a standard "confirm exit" dialog. Prompts the user to confirm
 * exit. Includes a toggle for dialog use preference.
 * @author jonb
 */
public class RcpConfirmExitDialog extends RcpDialogFacade {

    /**
	 * Creates an instance.
	 * @param toggleInit Initial state of preference toggle. Typically false
	 * (otherwise this dialog should not have appeared).
	 */
    public RcpConfirmExitDialog(boolean toggleInit) {
        _toggleInit = toggleInit;
    }

    /**
	 * Gets the closed dialog toggle state. The semantics are defined by the
	 * dialog toggle message.
	 * @return The value.
	 * @throws IllegalStateException if the dialog has not been opened.
	 */
    public boolean getToggleState() {
        MessageDialogWithToggle target = (MessageDialogWithToggle) getDialog();
        return target.getToggleState();
    }

    @Override
    protected Object newDialog(Shell shell) {
        String[] buttons = new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL };
        MessageDialogWithToggle target = new MessageDialogWithToggle(shell, PackageProps.CONFIRM_ON_EXIT_DIALOG_TITLE, null, PackageProps.CONFIRM_ON_EXIT_DIALOG_MESSAGE, MessageDialog.QUESTION, buttons, 0, PackageProps.CONFIRM_ON_EXIT_DIALOG_TOGGLE, _toggleInit);
        return target;
    }

    @Override
    protected Continuance openDialog(Object dialog) {
        MessageDialogWithToggle target = (MessageDialogWithToggle) dialog;
        target.open();
        switch(target.getReturnCode()) {
            case 0:
                return Continuance.PROCEED;
            default:
                return Continuance.DESIST;
        }
    }

    @Override
    protected void disposeDialog(Object dialog) {
        ((MessageDialogWithToggle) dialog).close();
    }

    @Override
    public String getRcpId() {
        return RCP_ID;
    }

    private boolean _toggleInit;

    public static final String RCP_ID = "com.msli.rcp.RCP_CONFIRM_EXIT_DIALOG";
}
