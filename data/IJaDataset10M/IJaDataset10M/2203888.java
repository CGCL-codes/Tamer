package lslplus.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import lslplus.LslPlusPlugin;
import lslplus.util.Util;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewScriptWizard extends Wizard implements INewWizard {

    private LslScriptWizardPage mainPage;

    private IStructuredSelection selection;

    private class LslScriptWizardPage extends LslFileCreationWizardPage {

        private static final String DEFAULT_SCRIPT_CONTENTS = "\n\ndefault {\n    state_entry() {\n        llOwnerSay(\"Hello " + "Scripter\");\n    }\n}\n";

        public LslScriptWizardPage(IStructuredSelection selection) {
            super("createModule", selection);
            setTitle(Messages.getString("NewScriptWizard.NEW_SCRIPT"));
            setPageComplete(false);
            setFileExtension("lslp");
            setDefaultPageImageDescriptor(image());
        }

        protected InputStream getInitialContents() {
            return new ByteArrayInputStream(DEFAULT_SCRIPT_CONTENTS.getBytes());
        }

        protected IStatus validateFileName(String fileName) {
            return new Status(IStatus.OK, "lslplus", "");
        }
    }

    public NewScriptWizard() {
        this.setDefaultPageImageDescriptor(image());
    }

    private static ImageDescriptor image() {
        return Util.findDescriptor("$nl$/icons/new_test.png");
    }

    public boolean performFinish() {
        IFile f = mainPage.createNewFile();
        LslPlusPlugin.openResource(getShell(), f);
        return true;
    }

    public void addPages() {
        super.addPages();
        mainPage = new LslScriptWizardPage(selection);
        addPage(mainPage);
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }
}
