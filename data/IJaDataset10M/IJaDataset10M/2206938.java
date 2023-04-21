package hub.sam.mof.simulator.behaviour.diagram.part;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * @generated
 */
public class M3ActionsCreationWizardPage extends WizardNewFileCreationPage {

    /**
	 * @generated
	 */
    private final String fileExtension;

    /**
	 * @generated
	 */
    public M3ActionsCreationWizardPage(String pageName, IStructuredSelection selection, String fileExtension) {
        super(pageName, selection);
        this.fileExtension = fileExtension;
    }

    /**
	 * Override to create files with this extension.
	 * 
	 * @generated
	 */
    protected String getExtension() {
        return fileExtension;
    }

    /**
	 * @generated
	 */
    public URI getURI() {
        return URI.createPlatformResourceURI(getFilePath().toString(), false);
    }

    /**
	 * @generated
	 */
    protected IPath getFilePath() {
        IPath path = getContainerFullPath();
        if (path == null) {
            path = new Path("");
        }
        String fileName = getFileName();
        if (fileName != null) {
            path = path.append(fileName);
        }
        return path;
    }

    /**
	 * @generated
	 */
    public void createControl(Composite parent) {
        super.createControl(parent);
        setFileName(M3ActionsDiagramEditorUtil.getUniqueFileName(getContainerFullPath(), getFileName(), getExtension()));
        setPageComplete(validatePage());
    }

    /**
	 * @generated
	 */
    protected boolean validatePage() {
        if (!super.validatePage()) {
            return false;
        }
        String extension = getExtension();
        if (extension != null && !getFilePath().toString().endsWith("." + extension)) {
            setErrorMessage(NLS.bind(hub.sam.mof.simulator.behaviour.diagram.part.Messages.M3ActionsCreationWizardPageExtensionError, extension));
            return false;
        }
        return true;
    }
}
