package ieditor;

import ieditor.model.PageDiagram;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

/**
 * Create new new .shape-file. 
 * Those files can be used with the ShapesEditor (see plugin.xml).
 * @author Elias Volanakis
 */
public class ElementCreationWizard extends Wizard implements INewWizard {

    private static int fileCount = 1;

    private CreationPage page1;

    public void addPages() {
        addPage(page1);
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        page1 = new CreationPage(workbench, selection);
    }

    public boolean performFinish() {
        return page1.finish();
    }

    /**
 * This WizardPage can create an empty .shapes file for the ShapesEditor.
 */
    private class CreationPage extends WizardNewFileCreationPage {

        private static final String DEFAULT_EXTENSION = ".shapes";

        private final IWorkbench workbench;

        /**
	 * Create a new wizard page instance.
	 * @param workbench the current workbench
	 * @param selection the current object selection
	 * @see ElementCreationWizard#init(IWorkbench, IStructuredSelection)
	 */
        CreationPage(IWorkbench workbench, IStructuredSelection selection) {
            super("shapeCreationPage1", selection);
            this.workbench = workbench;
            setTitle("Create a new " + DEFAULT_EXTENSION + " file");
            setDescription("Create a new " + DEFAULT_EXTENSION + " file");
        }

        public void createControl(Composite parent) {
            super.createControl(parent);
            setFileName("shapesExample" + fileCount + DEFAULT_EXTENSION);
            setPageComplete(validatePage());
        }

        /** Return a new ShapesDiagram instance. */
        private Object createDefaultContent() {
            return new PageDiagram();
        }

        /**
	 * This method will be invoked, when the "Finish" button is pressed.
	 * @see ElementCreationWizard#performFinish()
	 */
        boolean finish() {
            IFile newFile = createNewFile();
            fileCount++;
            IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
            if (newFile != null && page != null) {
                try {
                    IDE.openEditor(page, newFile, true);
                } catch (PartInitException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }

        protected InputStream getInitialContents() {
            ByteArrayInputStream bais = null;
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(createDefaultContent());
                oos.flush();
                oos.close();
                bais = new ByteArrayInputStream(baos.toByteArray());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return bais;
        }

        /**
	 * Return true, if the file name entered in this page is valid.
	 */
        private boolean validateFilename() {
            if (getFileName() != null && getFileName().endsWith(DEFAULT_EXTENSION)) {
                return true;
            }
            setErrorMessage("The 'file' name must end with " + DEFAULT_EXTENSION);
            return false;
        }

        protected boolean validatePage() {
            return super.validatePage() && validateFilename();
        }
    }
}
