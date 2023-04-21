package org.mxrapclipse.dialogs;

import java.lang.reflect.InvocationTargetException;
import org.mxrapclipse.preferences.IPreferenceStore;
import org.mxrapclipse.preferences.PreferenceConstants;
import org.mxrapclipse.utils.MxEclipseConstants;
import org.mxrapclipse.utils.MxEclipseUtils;
import matrix.db.Context;
import matrix.util.MatrixException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.mxrapclipse.MxEclipsePlugin;
import org.mxrapclipse.matrix.MatrixOperations;

/**
 * @author Administrator
 */
public class MatrixLoginDialog extends Dialog {

    private Label hostLabel;

    private Text hostText;

    private Label userLabel;

    private Label passLabel;

    private Text userText;

    private Text passText;

    private Button chkBox;

    private static IPreferenceStore store;

    static {
        MatrixLoginDialog.store = MxEclipsePlugin.getDefault().getPreferenceStore();
    }

    /**
	 * @param parentShell
	 */
    public MatrixLoginDialog(Shell parentShell) {
        super(parentShell);
    }

    public MatrixLoginDialog(IShellProvider parentShell) {
        super(parentShell);
    }

    protected Control createDialogArea(Composite parent) {
        Composite comp = (Composite) super.createDialogArea(parent);
        GridLayout layout = (GridLayout) comp.getLayout();
        layout.numColumns = 2;
        layout.marginLeft = 5;
        layout.marginRight = 5;
        layout.marginTop = 5;
        layout.marginBottom = 5;
        GridData labelData = new GridData(SWT.FILL, SWT.FILL, true, true);
        labelData.widthHint = 75;
        GridData fieldData = new GridData(SWT.FILL, SWT.FILL, true, true);
        fieldData.horizontalIndent = 5;
        fieldData.widthHint = 150;
        GridData chkBoxData = new GridData(SWT.FILL, SWT.FILL, true, true);
        chkBoxData.horizontalSpan = 2;
        this.hostLabel = new Label(comp, SWT.LEFT);
        this.hostLabel.setText(MxEclipseUtils.getString(MxEclipseConstants.LABEL_MATRIX_HOST));
        this.hostLabel.setLayoutData(labelData);
        this.hostText = new Text(comp, SWT.BORDER);
        this.hostText.setLayoutData(fieldData);
        this.hostText.setText(MatrixLoginDialog.store.getString(PreferenceConstants.MATRIX_HOST));
        this.hostText.setFocus();
        this.userLabel = new Label(comp, SWT.LEFT);
        this.userLabel.setText(MxEclipseUtils.getString(MxEclipseConstants.LABEL_MATRIX_USER));
        this.userLabel.setLayoutData(labelData);
        this.userText = new Text(comp, SWT.BORDER);
        this.userText.setLayoutData(fieldData);
        this.userText.setText(MatrixLoginDialog.store.getString(PreferenceConstants.MATRIX_USER));
        this.passLabel = new Label(comp, SWT.LEFT);
        this.passLabel.setText(MxEclipseUtils.getString(MxEclipseConstants.LABEL_MATRIX_PASSWORD));
        this.passLabel.setLayoutData(labelData);
        this.passText = new Text(comp, SWT.BORDER | SWT.PASSWORD);
        this.passText.setLayoutData(fieldData);
        this.passText.setText(MatrixLoginDialog.store.getString(PreferenceConstants.MATRIX_PWD));
        this.chkBox = new Button(comp, SWT.CHECK);
        this.chkBox.setText(MxEclipseUtils.getString(MxEclipseConstants.CHECKBOX_SAVE_HOST_DETAILS));
        this.chkBox.setLayoutData(chkBoxData);
        return comp;
    }

    protected void okPressed() {
        MatrixOperations mxops = new MatrixOperations();
        mxops.setHost(this.hostText.getText());
        mxops.setUser(this.userText.getText());
        mxops.setPassword(this.passText.getText());
        boolean saveData = false;
        try {
            MxEclipsePlugin.getDefault().loginDirect(MxEclipsePlugin.getDefault().getPreferenceStore());
        } catch (MatrixException e) {
            MessageDialog.openInformation(getShell(), "Error when trying to login", "Error when trying to login: " + e.getMessage(), null);
        }
        Context context = MxEclipsePlugin.getDefault().getContext();
        if (context.isConnected()) {
            MessageDialog.openInformation(getShell(), MxEclipseUtils.getString(MxEclipseConstants.MESSAGE_CONNECTION_SUCCESSFUL), MxEclipseUtils.getString(MxEclipseConstants.MESSAGE_CONNECTED), null);
            if (saveData) {
                MatrixLoginDialog.store.setValue(PreferenceConstants.MATRIX_HOST, this.hostText.getText());
                MatrixLoginDialog.store.setValue(PreferenceConstants.MATRIX_USER, this.userText.getText());
                MatrixLoginDialog.store.setValue(PreferenceConstants.MATRIX_PWD, this.passText.getText());
                MatrixLoginDialog.store.setValue(PreferenceConstants.MATRIX_DEFAULT_LOGIN, saveData);
            }
            super.okPressed();
        }
    }

    protected void cancelPressed() {
        super.cancelPressed();
    }

    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
    }

    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, MxEclipseUtils.getString(MxEclipseConstants.BUTTON_CONNECT), true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }
}
