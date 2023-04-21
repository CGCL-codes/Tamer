package com.bonkey.wizards.registration;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.bonkey.config.local.LocalConfigManager;
import com.bonkey.dialogs.ActivationProgressDialog;

/**
 * Wizard page for activating the product
 * 
 * @author marcel
 */
public class RegPageActivation extends WizardPage {

    /**
	 * Label for activation text box
	 */
    private Label activationLabel;

    /**
	 * Entry for activation key
	 */
    private Text activation;

    public RegPageActivation(String pageName) {
        super(pageName);
        setPageComplete(true);
    }

    public void createControl(Composite parent) {
        setTitle(Messages.getString("RegPageActivation.ActivationTitle"));
        setMessage(Messages.getString("RegPageActivation.EnterActivationKey"));
        Composite base = new Composite(parent, SWT.NULL);
        setControl(base);
        GridLayout layout = new GridLayout();
        layout.horizontalSpacing = 20;
        layout.verticalSpacing = 10;
        activationLabel = new Label(base, SWT.LEFT);
        activationLabel.setText(Messages.getString("RegPageActivation.ActivationKey"));
        activation = new Text(base, SWT.SINGLE | SWT.BORDER);
        activation.setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, false));
        base.setLayout(layout);
    }

    public IWizardPage getNextPage() {
        RegistrationWizard wizard = (RegistrationWizard) getWizard();
        boolean success = true;
        String failureMessage = null;
        ActivationProgressDialog runnable = new ActivationProgressDialog(activation.getText());
        try {
            new ProgressMonitorDialog(wizard.getContainer().getShell()).run(true, true, runnable);
            success = runnable.getOutcome();
        } catch (Exception ex) {
            LocalConfigManager.getConfigManager().logError(Messages.getString("RegPageActivation.ErrorActivatingProduct") + ex.getMessage());
            success = false;
            failureMessage = ex.getMessage();
        }
        wizard.setOutcome(success);
        if (success) {
            wizard.setKeys(runnable.getAccessKey(), runnable.getSecretKey(), runnable.getUserToken());
        } else {
            if (failureMessage == null) {
                failureMessage = runnable.getFailureMessage();
            }
            wizard.setFailureMessage(failureMessage);
        }
        return wizard.prepNextPage(this);
    }

    public boolean canFlipToNextPage() {
        return isPageComplete() && (super.getNextPage() != null);
    }
}
