package org.eclipse.mylyn.internal.bugzilla.ui.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.mylyn.internal.bugzilla.core.NewBugzillaReport;
import org.eclipse.mylyn.internal.bugzilla.ui.BugzillaUiPlugin;
import org.eclipse.mylyn.provisional.tasklist.MylarTaskListPlugin;
import org.eclipse.mylyn.provisional.tasklist.TaskRepository;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Class that contains shared functions for the wizards that submit bug reports.
 * 
 * @author Eric Booth
 * @author Mik Kersten (some hardening of prototype)
 * @author Rob Elves
 */
public abstract class AbstractBugzillaReportWizard extends Wizard implements INewWizard {

    protected boolean fromDialog = false;

    /** The model used to store all of the data for the wizard */
    protected NewBugzillaReport model;

    /**
	 * Flag to indicate if the wizard can be completed based on the attributes
	 * page
	 */
    protected boolean completed = false;

    /** The workbench instance */
    protected IWorkbench workbenchInstance;

    private final TaskRepository repository;

    public AbstractBugzillaReportWizard(TaskRepository repository) {
        super();
        this.repository = repository;
        model = new NewBugzillaReport(repository.getUrl(), MylarTaskListPlugin.getDefault().getOfflineReportsFile().getNextOfflineBugId());
        super.setDefaultPageImageDescriptor(BugzillaUiPlugin.imageDescriptorFromPlugin("org.eclipse.mylyn.internal.bugzilla.ui", "icons/wizban/bug-wizard.gif"));
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbenchInstance = workbench;
    }

    @Override
    public void addPages() {
        super.addPages();
    }

    /**
	 * Saves the bug report offline on the user's hard-drive. All offline bug
	 * reports are saved together in a single file in the plug-in's directory.
	 */
    protected abstract void saveBugOffline();

    /**
	 * @return the last page of this wizard
	 */
    protected abstract AbstractBugzillaWizardPage getWizardDataPage();

    public TaskRepository getRepository() {
        return repository;
    }
}
