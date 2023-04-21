package passreminder.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import passreminder.Messages;

/**
 * @author Eyecanseeyou
 * 
 */
public class MasterDialog extends Dialog {

    public static boolean lock = false;

    protected Text passwordField;

    protected Text confirmPasswordField;

    protected String confirmPassword = null;

    protected Image keyLockImage;

    protected String password = null;

    protected String message = null;

    protected int passwordStyle;

    protected Label warningText = null;

    protected Label warningLabel = null;

    protected boolean confirm;

    public MasterDialog(Shell parentShell, String message, boolean confirm, boolean hidePassword) {
        super(parentShell);
        this.confirm = confirm;
        this.passwordStyle = hidePassword ? SWT.BORDER | SWT.PASSWORD : SWT.BORDER;
        this.message = message;
        lock = true;
    }

    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("dialog.master.title"));
    }

    public void create() {
        super.create();
        passwordField.setFocus();
        passwordField.selectAll();
        dialogChanged();
    }

    protected Control createDialogArea(Composite parent) {
        Composite top = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        top.setLayout(layout);
        top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Composite imageComposite = new Composite(top, SWT.NONE);
        layout = new GridLayout();
        imageComposite.setLayout(layout);
        imageComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        Composite main = new Composite(top, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 3;
        main.setLayout(layout);
        main.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Label imageLabel = new Label(imageComposite, SWT.NONE);
        keyLockImage = ImageDescriptor.createFromURL(getClass().getResource("keylock.gif")).createImage();
        imageLabel.setImage(keyLockImage);
        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        imageLabel.setLayoutData(data);
        if (message != null) {
            Label messageLabel = new Label(main, SWT.WRAP);
            messageLabel.setText(message + "\n ");
            data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
            data.horizontalSpan = 3;
            data.widthHint = 300;
            messageLabel.setLayoutData(data);
        }
        createUsernameFields(main);
        if (confirm) createPasswordFields(main);
        Composite warningComposite = new Composite(main, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 0;
        layout.marginHeight = 0;
        warningComposite.setLayout(layout);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 3;
        warningComposite.setLayoutData(data);
        warningLabel = new Label(warningComposite, SWT.NONE);
        warningLabel.setImage(getImage(DLG_IMG_MESSAGE_WARNING));
        warningLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING));
        warningText = new Label(warningComposite, SWT.WRAP);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 300;
        warningText.setLayoutData(data);
        Dialog.applyDialogFont(parent);
        return main;
    }

    /**
	 * Create a spacer.
	 */
    protected void createSpacer(Composite top, int columnSpan, int vertSpan) {
        Label l = new Label(top, SWT.NONE);
        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        data.horizontalSpan = columnSpan;
        data.verticalSpan = vertSpan;
        l.setLayoutData(data);
    }

    /**
	 * Creates the three widgets that represent the password entry area.
	 * 
	 * @param parent
	 *            the parent of the widgets
	 */
    protected void createPasswordFields(Composite parent) {
        new Label(parent, SWT.NONE).setText("Confirm");
        confirmPasswordField = new Text(parent, passwordStyle);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.ENTRY_FIELD_WIDTH);
        confirmPasswordField.setLayoutData(data);
        confirmPasswordField.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event event) {
                dialogChanged();
            }
        });
    }

    /**
	 * Creates the three widgets that represent the user name entry area.
	 * 
	 * @param parent
	 *            the parent of the widgets
	 */
    protected void createUsernameFields(Composite parent) {
        new Label(parent, SWT.NONE).setText(Messages.getString("password"));
        passwordField = new Text(parent, passwordStyle);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.ENTRY_FIELD_WIDTH);
        passwordField.setLayoutData(data);
        passwordField.addListener(SWT.Modify, new Listener() {

            public void handleEvent(Event event) {
                dialogChanged();
            }
        });
    }

    private void dialogChanged() {
        if (passwordField.getText().length() == 0) {
            warningText.setVisible(true);
            warningLabel.setVisible(true);
            warningText.setText(Messages.getString("dialog.master.password.required"));
            getButton(IDialogConstants.OK_ID).setEnabled(false);
            return;
        }
        if (confirm) {
            if (confirmPasswordField.getText().length() == 0) {
                warningText.setVisible(true);
                warningLabel.setVisible(true);
                warningText.setText(Messages.getString("dialog.master.confirm.required"));
                getButton(IDialogConstants.OK_ID).setEnabled(false);
                return;
            } else if (!confirmPasswordField.getText().equals(passwordField.getText())) {
                warningText.setVisible(true);
                warningLabel.setVisible(true);
                warningText.setText(Messages.getString("dialog.master.confirm_do_no_match"));
                getButton(IDialogConstants.OK_ID).setEnabled(false);
                return;
            }
        }
        getButton(IDialogConstants.OK_ID).setEnabled(true);
        warningText.setVisible(false);
        warningLabel.setVisible(false);
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getPassword() {
        return password;
    }

    protected void okPressed() {
        confirmPassword = passwordField.getText();
        super.okPressed();
    }

    public boolean close() {
        if (keyLockImage != null) {
            keyLockImage.dispose();
        }
        lock = false;
        return super.close();
    }
}
