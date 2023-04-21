package org.exist.eclipse.util.internal.backup;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;

/**
 * This is the Wizard that handles the backup process.
 * 
 * @author Markus Tanner
 * 
 */
public class BackupWizard extends Wizard implements IWorkbenchWizard {

    private BackupTargetWizardPage _backupTargetPage;

    private ISelection _selection;

    private final IBrowseItem _item;

    private IBrowseService _itemService;

    private BackupJob _job;

    public BackupWizard(IBrowseItem item) {
        super();
        _item = item;
        _itemService = (IBrowseService) _item.getAdapter(IBrowseService.class);
        this.setWindowTitle("Create a backup");
        setNeedsProgressMonitor(true);
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        _selection = selection;
    }

    /**
	 * Adding the page to the wizard.
	 */
    public void addPages() {
        _backupTargetPage = new BackupTargetWizardPage(_selection, _item);
        addPage(_backupTargetPage);
    }

    /**
	 * This method figures out whether the 'Finish' button should be enabled.
	 * The button should only be enabled on the {@link BackupTargetWizardPage}.
	 */
    public boolean canFinish() {
        return _backupTargetPage.isAbleToFinish();
    }

    /**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
    public boolean performFinish() {
        boolean isFinished = true;
        if (IManagementService.class.cast(_item.getConnection().getAdapter(IManagementService.class)).check()) {
            if (IBrowseService.class.cast(_item.getAdapter(IBrowseService.class)).check()) {
                _job = new BackupJob(_item, _backupTargetPage.getBackupLocation());
                _job.setUser(true);
                _job.schedule();
            }
        }
        return isFinished;
    }

    @Override
    public boolean performCancel() {
        return _itemService.check();
    }
}
