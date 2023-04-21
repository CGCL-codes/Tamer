package net.sourceforge.copernicus.client.controller.windows;

import net.sourceforge.copernicus.client.controller.Configuration;
import net.sourceforge.copernicus.client.model.ModelConfiguration;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public class LoginDialog extends Dialog {

    private static I18n i18n = I18nFactory.getI18n(LoginDialog.class);

    private static final int SETTINGS_BUTTON_ID = IDialogConstants.CLIENT_ID + 1;

    private Configuration configuration;

    private Text loginTextfield;

    private Text passwordTextfield;

    private Button savePasswordCheckbox;

    public LoginDialog(Shell parentShell, Configuration configuration) {
        super(parentShell);
        this.configuration = configuration;
    }

    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
        Control ret = super.createContents(parent);
        getButton(IDialogConstants.OK_ID).setText(i18n.tr("Login"));
        getButton(IDialogConstants.CANCEL_ID).setText(i18n.tr("Exit"));
        Composite row1 = new Composite(composite, SWT.NULL);
        row1.setLayout(new RowLayout());
        Label loginLabel = new Label(row1, SWT.NULL);
        loginLabel.setText(i18n.tr("User name") + ":");
        loginTextfield = new Text(row1, SWT.NULL);
        loginTextfield.setText(configuration.getModelConfiguration().getCimUsername());
        Composite row2 = new Composite(composite, SWT.NULL);
        row2.setLayout(new RowLayout());
        Label passwordLabel = new Label(row2, SWT.NULL);
        passwordLabel.setText(i18n.tr("Password") + ":");
        passwordTextfield = new Text(row2, SWT.PASSWORD);
        passwordTextfield.setText(configuration.getModelConfiguration().getCimPassword());
        Composite row3 = new Composite(composite, SWT.NULL);
        row3.setLayout(new RowLayout());
        savePasswordCheckbox = new Button(row3, SWT.CHECK);
        savePasswordCheckbox.setText(i18n.tr("Save password"));
        savePasswordCheckbox.setSelection(configuration.isSavePassword());
        return ret;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        createButton(parent, SETTINGS_BUTTON_ID, i18n.tr("Settings") + "...", false);
    }

    @Override
    protected void okPressed() {
        ModelConfiguration modelConfiguration = configuration.getModelConfiguration();
        modelConfiguration.setCimUsername(loginTextfield.getText());
        modelConfiguration.setCimPassword(passwordTextfield.getText());
        configuration.setSavePassword(savePasswordCheckbox.getSelection());
        try {
            configuration.save();
        } catch (Exception e) {
        }
        super.okPressed();
    }

    @Override
    protected void buttonPressed(int buttonId) {
        super.buttonPressed(buttonId);
        if (buttonId == SETTINGS_BUTTON_ID) {
            SettingsDialog settingsDialog = new SettingsDialog(getShell(), configuration);
            settingsDialog.setBlockOnOpen(true);
            settingsDialog.open();
        }
    }
}
