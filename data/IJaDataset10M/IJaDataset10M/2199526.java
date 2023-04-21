package com.luxoft.fitpro.plugin.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import com.luxoft.fitpro.adapters.io.FileSystem;
import com.luxoft.fitpro.plugin.config.HtmlEditorPlugin;

public class NewFitFileWizard extends Wizard implements INewWizard {

    private NewFitFileWizardPage page;

    private ISelection selection;

    private FileSystem fileSystem;

    /**
     * Constructor for NewFITSuiteWizard.
     */
    public NewFitFileWizard() {
        super();
        setNeedsProgressMonitor(true);
        fileSystem = new FileSystem();
    }

    /**
     * Adding the page to the wizard.
     */
    public void addPages() {
        page = new NewFitFileWizardPage(selection, fileSystem);
        addPage(page);
    }

    /**
     * This method is called when 'Finish' button is pressed in the wizard. We will create an operation and run it using
     * wizard as execution context.
     */
    public boolean performFinish() {
        final String containerName = page.getContainerName();
        final String fileName = page.getFileName().lastIndexOf('.') == -1 ? page.getFileName() + fileSystem.getDefaultFitExtension() : page.getFileName();
        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    doFinish(containerName, fileName, monitor);
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
     * The worker method. It will find the container, create the file if missing or just replace its contents, and open
     * the editor on the newly created file.
     */
    private void doFinish(String containerName, String fileName, IProgressMonitor monitor) throws CoreException {
        monitor.beginTask("Creating " + fileName, 2);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IResource resource = root.findMember(new Path(containerName));
        if (!resource.exists() || !(resource instanceof IContainer)) {
            throwCoreException("Container \"" + containerName + "\" does not exist.");
        }
        IContainer container = (IContainer) resource;
        final IFile file = container.getFile(new Path(fileName));
        try {
            InputStream stream = openContentStream();
            if (file.exists()) {
                throwCoreException(fileName + " already exists.");
            } else {
                file.create(stream, true, monitor);
            }
            stream.close();
        } catch (IOException e) {
            HtmlEditorPlugin.getDefault().getLog().log(new Status(Status.ERROR, HtmlEditorPlugin.getDefault().getPluginId(), Status.OK, "Unable close stream to file " + file.getName(), e));
        }
        monitor.worked(1);
        monitor.setTaskName("Opening file for editing...");
        getShell().getDisplay().asyncExec(new Runnable() {

            public void run() {
                IWorkbenchPage thisPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try {
                    IDE.openEditor(thisPage, file, true);
                } catch (PartInitException e) {
                    HtmlEditorPlugin.getDefault().getLog().log(new Status(Status.ERROR, HtmlEditorPlugin.getDefault().getPluginId(), Status.OK, "Unable open editor to file " + file.getName(), e));
                }
            }
        });
        monitor.worked(1);
    }

    /**
     * We will initialize file contents with a sample text.
     */
    private InputStream openContentStream() {
        String contents = "<html>\n</html>";
        return new ByteArrayInputStream(contents.getBytes());
    }

    private void throwCoreException(String message) throws CoreException {
        IStatus status = new Status(IStatus.ERROR, "com.luxoft.eclipse.fit.suiteeditor", IStatus.OK, message, null);
        throw new CoreException(status);
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }
}
