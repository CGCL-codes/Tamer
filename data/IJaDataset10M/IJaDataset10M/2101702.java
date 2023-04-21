package de.walware.statet.ext.ui.wizards;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * 
 */
public abstract class AbstractWizard extends Wizard {

    protected IWorkbench fWorkbench;

    protected IStructuredSelection fWorkbenchSelection;

    public void init(final IWorkbench workbench, final IStructuredSelection currentSelection) {
        fWorkbench = workbench;
        fWorkbenchSelection = currentSelection;
    }

    protected void setDialogSettings(final AbstractUIPlugin plugin, final String sectionName) {
        final IDialogSettings master = plugin.getDialogSettings();
        IDialogSettings settings = master.getSection(sectionName);
        if (settings == null) {
            settings = master.addNewSection(sectionName);
        }
        setDialogSettings(settings);
    }
}
