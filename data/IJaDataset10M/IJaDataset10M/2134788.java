package mytest2.wizards;

import hello.StringConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "java". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */
public class SampleNewWizard extends Wizard implements INewWizard {

    private SampleNewWizardPage page;

    private ISelection selection;

    /**
	 * Constructor for SampleNewWizard.
	 */
    public SampleNewWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    /**
	 * Adding the page to the wizard.
	 */
    public void addPages() {
        page = new SampleNewWizardPage(selection);
        addPage(page);
    }

    /**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
    public boolean performFinish() {
        final IPath containerPath = page.getContainerPackagePath();
        final String fileName = page.getFileName();
        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    doFinish(containerPath, fileName, monitor);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error", realException.getMessage());
            return false;
        }
        return true;
    }

    /**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */
    private void doFinish(IPath containerPath, String fileName, IProgressMonitor monitor) throws CoreException {
        monitor.beginTask("Creating " + fileName, 2);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IResource resource = root.findMember(containerPath);
        IContainer container = (IContainer) resource;
        final IFile file = container.getFile(new Path(fileName));
        try {
            InputStream stream = page.openContentStream();
            if (file.exists()) {
                file.setContents(stream, true, true, monitor);
            } else {
                file.create(stream, true, monitor);
            }
            stream.close();
        } catch (IOException e) {
        }
        monitor.worked(1);
        monitor.setTaskName("Opening file for editing...");
        getShell().getDisplay().asyncExec(new Runnable() {

            public void run() {
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try {
                    IDE.openEditor(page, file, true);
                } catch (PartInitException e) {
                }
            }
        });
        monitor.worked(1);
    }

    /**
	 * We will initialize file contents with a sample text.
	 */
    private InputStream openContentStream() {
        String contents = StringConstants.DRL_TEMPLATE_ONE + "public class MyDRLTest { \n" + StringConstants.DRL_TEMPLATE_TWO;
        return new ByteArrayInputStream(contents.getBytes());
    }

    private void throwCoreException(String message) throws CoreException {
        IStatus status = new Status(IStatus.ERROR, "mytest2", IStatus.OK, message, null);
        throw new CoreException(status);
    }

    /**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }
}
