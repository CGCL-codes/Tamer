package net.sourceforge.keytool.views;

import net.sourceforge.keytool.helpers.ImageKeys;
import net.sourceforge.keytool.wizards.newcertificate.NewCertificateWizard;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class NewCertificateAction extends Action {

    private Shell shell;

    public NewCertificateAction(Shell shell) {
        super();
        setText("New certificate");
        setToolTipText("Create a new certificate");
        setImageDescriptor(ImageKeys.getImageDescriptor(ImageKeys.NEW_CERTIFICATE));
        this.shell = shell;
    }

    public final void run() {
        super.run();
        NewCertificateWizard wizard = new NewCertificateWizard();
        WizardDialog dialog = new WizardDialog(shell, wizard);
        dialog.open();
    }
}
