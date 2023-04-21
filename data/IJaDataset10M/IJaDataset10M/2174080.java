package edu.gsbme.msource.UI;

import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormDialog;

public class MathModelDialog extends FormDialog {

    public MathModelDialog(Shell shell) {
        super(shell);
    }

    public MathModelDialog(IShellProvider parentShellProvider) {
        super(parentShellProvider);
    }
}
