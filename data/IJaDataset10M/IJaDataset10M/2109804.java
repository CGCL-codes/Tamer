package org.eclipse.misc.pluginsexport;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

public class PluginsExportWizard extends Wizard implements IExportWizard {

    ExportWizardPage mainPage;

    public PluginsExportWizard() {
    }

    public boolean performFinish() {
        new ExportJob(mainPage.getSelectedFeatures()).schedule();
        return true;
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        setWindowTitle("Export Features Wizard");
        setNeedsProgressMonitor(true);
        mainPage = new ExportWizardPage("Export features");
    }

    public void addPages() {
        super.addPages();
        addPage(mainPage);
    }
}
