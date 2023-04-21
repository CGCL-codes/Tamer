package studivz.mailtool.guiprototype.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import studivz.mailtool.model.core.Repository;
import studivz.mailtool.util.Utilities;

public class CreateAccountDialog extends Dialog {

    private Label lId, lPassword;

    private Text tId, tPassword;

    private Button okButton, cbSavePassword, cbDefault;

    private String title, id, password;

    private boolean storePassword, asDefault;

    public CreateAccountDialog(IShellProvider parentShell) {
        super(parentShell);
        title = "New Account ...";
    }

    public CreateAccountDialog(Shell parentShell) {
        super(parentShell);
        title = "New Account ...";
    }

    public void setTitle(String title) {
        this.title = title;
        if (getShell() != null) {
            getShell().setText(title);
        }
    }

    public String getTitle() {
        return title;
    }

    protected boolean isInputComplete() {
        return !Utilities.isEmpty(tId.getText()) && !Utilities.isEmpty(tPassword.getText());
    }

    protected Text createIdText(Composite parent) {
        Text id = new Text(parent, SWT.BORDER);
        id.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                okButton.setEnabled(isInputComplete());
            }

            public void keyReleased(KeyEvent e) {
                okButton.setEnabled(isInputComplete());
            }
        });
        return id;
    }

    protected Text createPasswordText(Composite parent) {
        Text pw = new Text(parent, SWT.BORDER | SWT.PASSWORD);
        pw.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                okButton.setEnabled(isInputComplete());
            }

            public void keyReleased(KeyEvent e) {
                okButton.setEnabled(isInputComplete());
            }
        });
        return pw;
    }

    protected Button createStorePasswordCheckbox(Composite parent) {
        Button cb = new Button(parent, SWT.CHECK);
        cb.setText("store password");
        return cb;
    }

    protected Button createLoginCheckbox(Composite parent) {
        Button cb = new Button(parent, SWT.CHECK);
        cb.setText("login when application starts");
        cb.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                if (cbDefault.getSelection()) {
                    cbSavePassword.setSelection(true);
                    cbSavePassword.setEnabled(false);
                } else {
                    cbSavePassword.setEnabled(true);
                }
            }
        });
        return cb;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        Composite grid = new Composite(composite, SWT.NONE);
        {
            GridData gridData = new GridData();
            gridData.horizontalAlignment = GridData.FILL;
            gridData.verticalAlignment = GridData.FILL;
            gridData.grabExcessHorizontalSpace = true;
            gridData.grabExcessVerticalSpace = true;
            grid.setLayoutData(gridData);
            GridLayout layout = new GridLayout();
            layout.numColumns = 2;
            grid.setLayout(layout);
        }
        lId = new Label(grid, SWT.NONE);
        lId.setText("e-mail:");
        {
            GridData gridData = new GridData();
            gridData.horizontalAlignment = GridData.FILL;
            gridData.verticalAlignment = GridData.CENTER;
            lId.setLayoutData(gridData);
        }
        tId = createIdText(grid);
        {
            GridData gridData = new GridData();
            gridData.horizontalAlignment = GridData.FILL;
            gridData.verticalAlignment = GridData.FILL;
            gridData.grabExcessHorizontalSpace = true;
            tId.setLayoutData(gridData);
        }
        lPassword = new Label(grid, SWT.NONE);
        lPassword.setText("password:");
        {
            GridData gridData = new GridData();
            gridData.horizontalAlignment = GridData.FILL;
            gridData.verticalAlignment = GridData.CENTER;
            lPassword.setLayoutData(gridData);
        }
        tPassword = createPasswordText(grid);
        {
            GridData gridData = new GridData();
            gridData.horizontalAlignment = GridData.FILL;
            gridData.verticalAlignment = GridData.FILL;
            gridData.grabExcessHorizontalSpace = true;
            tPassword.setLayoutData(gridData);
        }
        cbSavePassword = createStorePasswordCheckbox(grid);
        {
            GridData gridData = new GridData();
            gridData.horizontalAlignment = GridData.FILL;
            gridData.grabExcessHorizontalSpace = true;
            gridData.horizontalSpan = 2;
            cbSavePassword.setLayoutData(gridData);
        }
        cbDefault = createLoginCheckbox(grid);
        {
            GridData gridData = new GridData();
            gridData.horizontalAlignment = GridData.FILL;
            gridData.grabExcessHorizontalSpace = true;
            gridData.horizontalSpan = 2;
            cbDefault.setLayoutData(gridData);
        }
        return composite;
    }

    @Override
    protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
        Button button = super.createButton(parent, id, label, defaultButton);
        if (id == Dialog.OK) {
            button.setEnabled(false);
            button.setText("create");
            this.okButton = button;
        }
        return button;
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == Dialog.OK) {
            if (Repository.instance().getAccount(tId.getText()) != null) {
                showMessage(getShell().getText(), "Es gibt bereits einen Account mit dieser Id.");
                return;
            }
        }
        super.buttonPressed(buttonId);
    }

    protected void showMessage(String title, String msg) {
        MessageDialog.openWarning(getShell(), title, msg);
    }

    @Override
    public void create() {
        super.create();
        getShell().setText(getTitle());
    }

    @Override
    protected void okPressed() {
        password = tPassword.getText();
        id = tId.getText();
        storePassword = cbSavePassword.getSelection();
        asDefault = cbDefault.getSelection();
        super.okPressed();
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public boolean storePassword() {
        return storePassword;
    }

    public boolean loginOnStart() {
        return asDefault;
    }
}
