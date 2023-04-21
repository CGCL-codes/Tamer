package com.wgo.precise.client.ui.view.util.dialogs;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Shell;
import com.wgo.precise.client.ui.view.util.validators.UrlInputValidator;

/**
 * @author petterei
 *
 * @version $Id: LinkedTextInputDialog.java,v 1.1 2006-03-07 14:56:23 petterei Exp $
 */
public class LinkedTextInputDialog extends InputDialog {

    public LinkedTextInputDialog(Shell parentShell, String dialogTitle, String dialogMessage, String initialValue) {
        super(parentShell, dialogTitle, dialogMessage, initialValue, new UrlInputValidator());
    }

    public LinkedTextInputDialog(Shell parentShell, String dialogTitle, String dialogMessage, String initialValue, IInputValidator inputValidator) {
        super(parentShell, dialogTitle, dialogMessage, initialValue, inputValidator);
    }
}
