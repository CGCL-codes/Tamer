package org.jcryptool.crypto.flexiprovider.keystore.actions;

import java.security.cert.Certificate;
import javax.crypto.SecretKey;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.flexiprovider.keystore.FlexiProviderKeystorePlugin;
import org.jcryptool.crypto.flexiprovider.keystore.ImportManager;
import org.jcryptool.crypto.flexiprovider.keystore.wizards.ImportWizard;
import org.jcryptool.crypto.keys.KeyType;
import org.jcryptool.crypto.keystore.descriptors.ImportDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.IImportDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.IImportWizard;
import org.jcryptool.crypto.keystore.ui.actions.AbstractImportKeyStoreEntryAction;
import codec.pkcs12.PFX;

/**
 * @author tkern
 *
 */
public class ImportKeyAction extends AbstractImportKeyStoreEntryAction {

    private Shell shell;

    private WizardDialog dialog;

    public ImportKeyAction() {
        this.setText("i18n Import");
        this.setToolTipText("i18n Import");
        this.setImageDescriptor(FlexiProviderKeystorePlugin.getImageDescriptor("/icons/16x16/kgpg_import.png"));
    }

    public void run() {
        shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        Wizard wizard = new ImportWizard();
        dialog = new WizardDialog(shell, wizard);
        dialog.setMinimumPageSize(300, 350);
        int result = dialog.open();
        if (result == Window.OK) {
            if (wizard instanceof IImportWizard) {
                IImportDescriptor desc = ((IImportWizard) wizard).getImportDescriptor();
                IPath path = new Path(desc.getFileName());
                if (desc.getKeyStoreEntryType().equals(KeyType.SECRETKEY)) {
                    SecretKey key = ImportManager.getInstance().importSecretKey(path);
                    performImportAction(new ImportDescriptor(desc.getContactName(), key.getAlgorithm(), KeyType.SECRETKEY, desc.getFileName(), desc.getPassword(), "FlexiCore", -1), key);
                } else if (desc.getKeyStoreEntryType().equals(KeyType.KEYPAIR)) {
                    PFX pfx = ImportManager.getInstance().importPFX(path);
                    performImportAction(desc, pfx);
                } else if (desc.getKeyStoreEntryType().equals(KeyType.PUBLICKEY)) {
                    Certificate cert = ImportManager.getInstance().importCertificate(path);
                    performImportAction(desc, cert);
                }
            }
        }
    }
}
