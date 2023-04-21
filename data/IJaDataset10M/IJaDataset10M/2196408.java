package edu.vt.eng.swat.workflow.dialogs.account;

import net.miginfocom.swt.MigLayout;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import edu.vt.eng.swat.workflow.db.base.entity.User;

public class AccountSettingsDialog extends TitleAreaDialog {

    private User user;

    private Text username;

    private Text firstName;

    private Text lastName;

    private Text email;

    private Text newPassword;

    private Text repeatNewPassword;

    private String newPswd;

    private String repeatNewPswd;

    public AccountSettingsDialog(Shell parentShell, User user) {
        super(parentShell);
        this.user = user;
    }

    @Override
    public void create() {
        super.create();
        setTitle("Account Settings");
        setMessage("");
    }

    /**
     * Creates dialog area.
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setLayout(new MigLayout("wrap 4, fillx"));
        composite.getChildren()[0].setLayoutData("growx, spanx 4, wrap");
        Label label = new Label(composite, SWT.NONE);
        label.setText("Username:");
        label.setLayoutData("width 57!, gapx 0 0");
        username = new Text(composite, SWT.BORDER);
        username.setLayoutData("growx, width 150!, gapx 0 0");
        username.setEnabled(false);
        label = new Label(composite, SWT.NONE);
        label.setText("Email:");
        label.setLayoutData("width 57!, gapx 0 0");
        email = new Text(composite, SWT.BORDER);
        email.setLayoutData("growx, width 150!, gapx 0 0");
        email.setEnabled(false);
        label = new Label(composite, SWT.NONE);
        label.setText("First Name:");
        label.setLayoutData("width 57!, gapx 0 0");
        firstName = new Text(composite, SWT.BORDER);
        firstName.setLayoutData("growx, width 150!, gapx 0 0");
        firstName.setEnabled(false);
        label = new Label(composite, SWT.NONE);
        label.setText("Last Name: ");
        lastName = new Text(composite, SWT.BORDER);
        lastName.setLayoutData("growx, width 150!, gapx 0 0");
        lastName.setEnabled(false);
        label = new Label(composite, SWT.NONE);
        label.setText("New Password: ");
        newPassword = new Text(composite, SWT.BORDER | SWT.PASSWORD);
        newPassword.setLayoutData("growx, width 150!, gapx 0 0");
        label = new Label(composite, SWT.NONE);
        label.setText("Repeat New Password: ");
        repeatNewPassword = new Text(composite, SWT.BORDER | SWT.PASSWORD);
        repeatNewPassword.setLayoutData("growx, width 150!, gapx 0 0");
        if (user != null) {
            username.setText(getStrValue(user.getName()));
            firstName.setText(getStrValue(user.getFirstName()));
            lastName.setText(getStrValue(user.getLastName()));
            email.setText(getStrValue(user.getEmail()));
        }
        return composite;
    }

    private String getStrValue(String str) {
        return str == null ? "" : str;
    }

    /**
     * Executes when OK button is pressed.
     */
    @Override
    protected void okPressed() {
        if (newPswd.length() == 0) {
            setErrorMessage("Please enter new password");
            newPassword.setFocus();
            return;
        }
        if (newPswd.length() < 6) {
            setErrorMessage("Password should be at least 6 characters long");
            newPassword.setFocus();
            return;
        }
        if (repeatNewPswd.length() == 0) {
            setErrorMessage("Please repeat new password");
            repeatNewPassword.setFocus();
            return;
        }
        if (!repeatNewPswd.equals(newPswd)) {
            setErrorMessage("Passwords should be the same in both fields");
            repeatNewPassword.setFocus();
            return;
        }
        super.okPressed();
    }

    /**
     * Executes when any button is pressed
     */
    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
            newPswd = newPassword.getText();
            repeatNewPswd = repeatNewPassword.getText();
        } else {
            newPswd = "";
            repeatNewPswd = "";
        }
        super.buttonPressed(buttonId);
    }

    public String getNewPassword() {
        return newPswd;
    }
}
