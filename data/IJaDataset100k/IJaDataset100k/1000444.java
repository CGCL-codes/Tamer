package net.sourceforge.sfeclipse.importWizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public class ImportWizard extends Wizard implements IImportWizard {

    ImportWizardPage mainPage;

    public ImportWizard() {
        super();
    }

    public boolean performFinish() {
        IFile file = mainPage.createNewFile();
        if (file == null) return false;
        return true;
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        setWindowTitle("Symfony Import Wizard");
        setNeedsProgressMonitor(true);
        mainPage = new ImportWizardPage("SymfonyProject", selection);
    }

    public void addPages() {
        super.addPages();
        addPage(mainPage);
    }
}
