package net.sf.elbe.ui.dialogs;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class CredentialsDialog extends InputDialog {

    public CredentialsDialog(Shell parentShell, String dialogTitle, String dialogMessage, String initialValue, IInputValidator validator) {
        super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
    }

    protected void setShellStyle(int newShellStyle) {
        super.setShellStyle(newShellStyle);
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        super.getText().setEchoChar('*');
        return composite;
    }
}
