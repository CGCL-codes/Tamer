package admin.view.utils.dialogs;

import java.awt.Frame;
import java.awt.Toolkit;

public class EditBankrollDialog extends EditUserDialog {

    private static final long serialVersionUID = 1763427984675376200L;

    public EditBankrollDialog(Frame parent, String title) {
        super(parent, title);
    }

    protected void action(String command) {
        if ("Ok".equals(command)) {
            optionChosen = OK_OPTION;
            newAttribute = textField.getText();
            if (!isInteger(newAttribute)) {
                Toolkit.getDefaultToolkit().beep();
                textField.selectAll();
                return;
            }
        } else {
            optionChosen = CANCEL_OPTION;
        }
        hideDialog();
    }

    private boolean isInteger(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
