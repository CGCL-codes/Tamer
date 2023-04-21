package view.views;

import org.eclipse.jface.wizard.Wizard;

public class DbImportWizardMain extends Wizard {

    private DbImportWizardPageOne one;

    private DbImportWizardPageTwo two;

    private boolean isUpdate;

    public DbImportWizardMain(boolean isUpdate) {
        super();
        this.isUpdate = isUpdate;
        setNeedsProgressMonitor(true);
    }

    @Override
    public void addPages() {
        one = new DbImportWizardPageOne(isUpdate);
        two = new DbImportWizardPageTwo(isUpdate);
        addPage(one);
        addPage(two);
    }

    @Override
    public boolean performFinish() {
        return true;
    }
}
