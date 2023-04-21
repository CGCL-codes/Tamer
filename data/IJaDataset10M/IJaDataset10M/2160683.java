package net.sf.myway.gps.ui.handlers;

import net.sf.myway.gps.ui.wizards.DownloadWizard;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class DownloadHandler extends AbstractHandler {

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final DownloadWizard w = new DownloadWizard();
        final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        final WizardDialog dialog = new WizardDialog(shell, w);
        dialog.create();
        dialog.open();
        return null;
    }
}
