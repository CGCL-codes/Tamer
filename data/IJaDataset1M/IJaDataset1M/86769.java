package view.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class LoginDialog extends MessageDialog {

    String user = "";

    String password = "";

    public LoginDialog(String title, String dialogMessage) {
        super(null, title, null, dialogMessage, QUESTION, new String[] { "Sign In", "Exit" }, 0);
    }

    protected Control createCustomArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setLayout(new GridLayout(2, false));
        Label label = new Label(composite, SWT.NULL);
        label.setText("User:");
        final Text userText = new Text(composite, SWT.BORDER);
        userText.setTextLimit(20);
        userText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        userText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                user = userText.getText();
            }
        });
        label = new Label(composite, SWT.NULL);
        label.setText("Password:");
        final Text passwordText = new Text(composite, SWT.BORDER | SWT.PASSWORD);
        passwordText.setTextLimit(30);
        passwordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        passwordText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                password = passwordText.getText();
            }
        });
        return null;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
