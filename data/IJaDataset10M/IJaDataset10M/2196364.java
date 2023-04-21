package net.sf.iauthor.ui.commands;

import net.sf.iauthor.ui.wizards.NewProjectWizard;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

/**
 * @author Andreas Beckers
 */
public class NewProjectWizardAction extends AbstractHandler implements IHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        final NewProjectWizard w = new NewProjectWizard();
        final WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), w);
        dialog.create();
        dialog.open();
        return null;
    }
}
