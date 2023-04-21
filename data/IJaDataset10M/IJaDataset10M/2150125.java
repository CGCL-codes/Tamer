package org.spbu.pldoctoolkit.graph.diagram.infproduct.part;

import java.io.InputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards.EditorWizardPage;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.edit.parts.DocumentationCoreEditPart;

/**
 * @generated
 */
public class DrlModelCreationWizardPage extends WizardNewFileCreationPage {

    /**
	 * @generated
	 */
    private final String fileExtension;

    /**
	 * @generated
	 */
    public DrlModelCreationWizardPage(String pageName, IStructuredSelection selection, String fileExtension) {
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
        setFileName(DrlModelDiagramEditorUtil.getUniqueFileName(getContainerFullPath(), getFileName(), getExtension()));
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
            setErrorMessage(NLS.bind("File name should have ''{0}'' extension.", extension));
            return false;
        }
        return true;
    }
}
