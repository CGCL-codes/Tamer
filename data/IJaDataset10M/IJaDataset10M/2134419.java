package org.plazmaforge.bsolution.base.client;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.plazmaforge.bsolution.base.client.swt.dialogs.InputDialog;
import org.plazmaforge.bsolution.base.client.swt.dialogs.LoginDialog;
import org.plazmaforge.framework.app.AppEnvironment;
import org.plazmaforge.framework.client.swt.SWTUtils;
import org.plazmaforge.framework.core.exception.ApplicationException;

public class EclipseClientApplicationManager extends SWTClientApplicationManager {

    private Display display;

    private boolean isProcessing;

    /** Login dialog **/
    private LoginDialog loginDialog;

    /** Form to input start parameters **/
    private InputDialog inputDialog;

    public EclipseClientApplicationManager() {
    }

    protected void splashShow() throws ApplicationException {
    }

    protected void splashHide() throws ApplicationException {
    }

    protected void dialogShow() throws ApplicationException {
        display = Display.getDefault();
        Shell shell = new Shell(SWT.ON_TOP);
        loginDialog = new LoginDialog(shell);
        SWTUtils.centerWindow(shell);
        loginDialog.open();
        if (!isLogin()) {
            return;
        }
        if (!isSupportInputDialog()) {
            AppEnvironment.setUserInterface(AppEnvironment.DEFAULT_USER_INTERFACE);
            return;
        }
        if (!isAvailableCustomParameters()) {
            return;
        }
        inputDialog = new InputDialog(shell);
        SWTUtils.centerWindow(shell);
        inputDialog.open();
    }

    protected boolean isLogin() {
        if (loginDialog == null) {
            return false;
        }
        return loginDialog.isLogin();
    }

    public void updateStatusBar() {
    }

    public void loadSystemConfiguration() throws ApplicationException {
        isProcessing = true;
        processLoadSystemConfiguration();
    }
}
