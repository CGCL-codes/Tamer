package de.fmf.multiclip.splashHandlers;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.splash.AbstractSplashHandler;

/**
 * @since 3.3
 * 
 */
public class InteractiveSplashHandler extends AbstractSplashHandler {

    private static final int F_LABEL_HORIZONTAL_INDENT = 175;

    private static final int F_BUTTON_WIDTH_HINT = 80;

    private static final int F_TEXT_WIDTH_HINT = 175;

    private static final int F_COLUMN_COUNT = 3;

    private Composite fCompositeLogin;

    private Text fTextUsername;

    private Text fTextPassword;

    private Button fButtonOK;

    private Button fButtonCancel;

    private boolean fAuthenticated;

    /**
	 * 
	 */
    public InteractiveSplashHandler() {
        fCompositeLogin = null;
        fTextUsername = null;
        fTextPassword = null;
        fButtonOK = null;
        fButtonCancel = null;
        fAuthenticated = false;
    }

    public void init(final Shell splash) {
        super.init(splash);
        configureUISplash();
        createUI();
        createUIListeners();
        splash.layout(true);
        doEventLoop();
    }

    /**
	 * 
	 */
    private void doEventLoop() {
        Shell splash = getSplash();
        while (fAuthenticated == false) {
            if (splash.getDisplay().readAndDispatch() == false) {
                splash.getDisplay().sleep();
            }
        }
    }

    /**
	 * 
	 */
    private void createUIListeners() {
        createUIListenersButtonOK();
        createUIListenersButtonCancel();
    }

    /**
	 * 
	 */
    private void createUIListenersButtonCancel() {
        fButtonCancel.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleButtonCancelWidgetSelected();
            }
        });
    }

    /**
	 * 
	 */
    private void handleButtonCancelWidgetSelected() {
        getSplash().getDisplay().close();
        System.exit(0);
    }

    /**
	 * 
	 */
    private void createUIListenersButtonOK() {
        fButtonOK.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleButtonOKWidgetSelected();
            }
        });
    }

    /**
	 * 
	 */
    private void handleButtonOKWidgetSelected() {
        String username = fTextUsername.getText();
        String password = fTextPassword.getText();
        if ((username.length() > 0) && (password.length() > 0)) {
            fAuthenticated = true;
        } else {
            MessageDialog.openError(getSplash(), "Authentication Failed", "A username and password must be specified to login.");
        }
    }

    /**
	 * 
	 */
    private void createUI() {
        createUICompositeLogin();
        createUICompositeBlank();
        createUILabelUserName();
        createUITextUserName();
        createUILabelPassword();
        createUITextPassword();
        createUILabelBlank();
        createUIButtonOK();
        createUIButtonCancel();
    }

    /**
	 * 
	 */
    private void createUIButtonCancel() {
        fButtonCancel = new Button(fCompositeLogin, SWT.PUSH);
        fButtonCancel.setText("Cancel");
        GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
        data.widthHint = F_BUTTON_WIDTH_HINT;
        data.verticalIndent = 10;
        fButtonCancel.setLayoutData(data);
    }

    /**
	 * 
	 */
    private void createUIButtonOK() {
        fButtonOK = new Button(fCompositeLogin, SWT.PUSH);
        fButtonOK.setText("OK");
        GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
        data.widthHint = F_BUTTON_WIDTH_HINT;
        data.verticalIndent = 10;
        fButtonOK.setLayoutData(data);
    }

    /**
	 * 
	 */
    private void createUILabelBlank() {
        Label label = new Label(fCompositeLogin, SWT.NONE);
        label.setVisible(false);
    }

    /**
	 * 
	 */
    private void createUITextPassword() {
        int style = SWT.PASSWORD | SWT.BORDER;
        fTextPassword = new Text(fCompositeLogin, style);
        GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
        data.widthHint = F_TEXT_WIDTH_HINT;
        data.horizontalSpan = 2;
        fTextPassword.setLayoutData(data);
    }

    /**
	 * 
	 */
    private void createUILabelPassword() {
        Label label = new Label(fCompositeLogin, SWT.NONE);
        label.setText("&Password:");
        GridData data = new GridData();
        data.horizontalIndent = F_LABEL_HORIZONTAL_INDENT;
        label.setLayoutData(data);
    }

    /**
	 * 
	 */
    private void createUITextUserName() {
        fTextUsername = new Text(fCompositeLogin, SWT.BORDER);
        GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
        data.widthHint = F_TEXT_WIDTH_HINT;
        data.horizontalSpan = 2;
        fTextUsername.setLayoutData(data);
    }

    /**
	 * 
	 */
    private void createUILabelUserName() {
        Label label = new Label(fCompositeLogin, SWT.NONE);
        label.setText("&User Name:");
        GridData data = new GridData();
        data.horizontalIndent = F_LABEL_HORIZONTAL_INDENT;
        label.setLayoutData(data);
    }

    /**
	 * 
	 */
    private void createUICompositeBlank() {
        Composite spanner = new Composite(fCompositeLogin, SWT.NONE);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.horizontalSpan = F_COLUMN_COUNT;
        spanner.setLayoutData(data);
    }

    /**
	 * 
	 */
    private void createUICompositeLogin() {
        fCompositeLogin = new Composite(getSplash(), SWT.BORDER);
        GridLayout layout = new GridLayout(F_COLUMN_COUNT, false);
        fCompositeLogin.setLayout(layout);
    }

    /**
	 * 
	 */
    private void configureUISplash() {
        FillLayout layout = new FillLayout();
        getSplash().setLayout(layout);
        getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
    }
}
