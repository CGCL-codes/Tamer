package attacktree.diagram.part;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import attacktree.diagram.application.WizardNewFileCreationPage;

/**
 * @generated
 */
public class SeaMonsterCreationWizardPage extends WizardNewFileCreationPage {

    /**
	 * @generated
	 */
    private final String fileExtension;

    /**
	 * @generated
	 */
    public SeaMonsterCreationWizardPage(String pageName, IStructuredSelection selection, String fileExtension) {
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
        return URI.createFileURI(getFilePath().toString());
    }

    /**
	 * @generated
	 */
    public void createControl(Composite parent) {
        super.createControl(parent);
        setFileName(SeaMonsterDiagramEditorUtil.getUniqueFileName(getContainerFullPath(), getFileName(), getExtension()));
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
            setErrorMessage(NLS.bind(Messages.SeaMonsterCreationWizardPageExtensionError, extension));
            return false;
        }
        return true;
    }
}
