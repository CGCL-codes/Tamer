package de.cabanis.unific.ui.runnables;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import de.cabanis.unific.ui.wizards.ClipboardToPlaylistWizard;

/**
 * TODO javadoc
 * @author Nicolas Cabanis
 */
public class CreateClipboardToPlaylistWizardRunnable extends CreateWizardRunnable {

    public CreateClipboardToPlaylistWizardRunnable(Shell shell) {
        super(shell);
    }

    public void run(IProgressMonitor monitor) {
        Wizard wizard = new ClipboardToPlaylistWizard(shell.getDisplay());
        dialog = new WizardDialog(shell, wizard);
    }
}
