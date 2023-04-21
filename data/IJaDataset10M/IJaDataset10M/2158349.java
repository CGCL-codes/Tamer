package org.bellard.qemoon.wizards;

import org.apache.log4j.Logger;
import org.bellard.qemoon.Activator;
import org.bellard.qemoon.constants.Configuration2Constants;
import org.bellard.qemoon.model.VM;
import org.bellard.qemoon.utils.ErrorUtils;
import org.bellard.qemoon.views.VMNavigationView;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

/**
 * @author France Telecom
 * @Copyright France Telecom S.A. 2006
 */
public class NewExampleProjectWizard extends Wizard implements INewWizard {

    public static final Logger logger = Logger.getLogger(NewExampleProjectWizard.class);

    public static final String ID = NewExampleProjectWizard.class.getName();

    private IStructuredSelection selection;

    /**
	 * 
	 */
    public NewExampleProjectWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    private NewProjectWizardPage newPage;

    @Override
    public void addPages() {
        newPage = new NewProjectWizardPage(selection);
        addPage(newPage);
    }

    @Override
    public boolean performFinish() {
        String name = newPage.getNameText();
        try {
            VM v = new VM(name);
            IPath imagePath = Activator.getDefault().getPlatformPath().append("linux.img");
            v.getPreferenceStore().setValue(Configuration2Constants.IMAGE_CUSTOM, true);
            v.getPreferenceStore().setValue(Configuration2Constants.IMAGE_VALUE, imagePath.toString());
            VMNavigationView view = Activator.getDefault().getVMNavigationView();
            view.addVM(v);
            return true;
        } catch (Exception e) {
            String message = "Problem during the project creation operation";
            logger.error(message, e);
        }
        ErrorUtils.displayErrorDialog("error.project.create");
        return false;
    }

    /**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }
}
