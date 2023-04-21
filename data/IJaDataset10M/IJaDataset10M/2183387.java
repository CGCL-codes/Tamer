package q_impress.pmi.plugin.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.*;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.ide.IDE;
import q_impress.pmi.plugin.handlers.AddUmlResourceHandler;

/**
 * This is the Performance Model from UML model wizard. Its role is to
 * create a new file resource in the provided container.
 * If the container resource (a folder or a project) is selected 
 * in the workspace when the wizard is opened, it will accept it 
 * as the target container.
 * The wizard creates one file with the extension "pmi". The wizard
 * also prompts the user to select an "uml" file to import for the 
 * "pmi" resource.
 */
public class PMFromUmlWizard extends Wizard implements INewWizard {

    private CreatePmiFileWizardPage pmiFilePage;

    private SelectUmlWizardPage umlPage;

    private ISelection selection;

    /**
	 * Default constructor.
	 */
    public PMFromUmlWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    /**
	 * Adding the page to the wizard.
	 */
    public void addPages() {
        pmiFilePage = new CreatePmiFileWizardPage(selection);
        umlPage = new SelectUmlWizardPage();
        addPage(pmiFilePage);
        addPage(umlPage);
    }

    /**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
    public boolean performFinish() {
        final String containerName = pmiFilePage.getContainerName();
        final String fileName = pmiFilePage.getFileName();
        final String modelName = pmiFilePage.getModelName();
        final String umlResourcePath = umlPage.getXmiResourcePath();
        final boolean doBundling = umlPage.getDoBundling();
        final boolean doManageSync = umlPage.getManageSynchronization();
        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    doFinish(containerName, modelName, fileName, umlResourcePath, doBundling, doManageSync, monitor);
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

    private void doFinish(String containerName, String modelName, String fileName, String umlResourcePath, boolean doBundling, boolean doManageSync, IProgressMonitor monitor) throws CoreException {
        monitor.beginTask("Creating model skeleton" + fileName, 4);
        createSkeleton(containerName, modelName);
        monitor.worked(1);
        monitor.setTaskName("Creating empty model");
        String modelRoot = containerName + "/" + modelName;
        createEmptyModel(modelRoot, fileName);
        monitor.worked(1);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IResource resource = root.findMember(new Path(modelRoot));
        IContainer container = (IContainer) resource;
        final IFile file = container.getFile(new Path(fileName));
        monitor.setTaskName("Importing UML resource");
        addUmlResource(umlResourcePath, doBundling, doManageSync, file.getFullPath().toPortableString());
        monitor.worked(1);
        monitor.setTaskName("Opening model for editing...");
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

    private void createSkeleton(String containerName, String projectName) throws CoreException {
        ICommandService cmdService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
        IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
        Command cmd = cmdService.getCommand("q_impress.pmi.plugin.commands.createProjectSkeleton");
        IParameter rootParm = null;
        IParameter projectNameParm = null;
        try {
            rootParm = cmd.getParameter("q_impress.pmi.plugin.commands.createProjectSkeleton.root");
            projectNameParm = cmd.getParameter("q_impress.pmi.plugin.commands.createProjectSkeleton.projectName");
        } catch (NotDefinedException e) {
            this.throwCoreException("Unable to execute command, invalid parameter :" + e.getMessage(), e);
        }
        Parameterization parmRoot = new Parameterization(rootParm, containerName);
        Parameterization parmProjectName = new Parameterization(projectNameParm, projectName);
        ParameterizedCommand parmCommand = new ParameterizedCommand(cmd, new Parameterization[] { parmRoot, parmProjectName });
        try {
            handlerService.executeCommand(parmCommand, null);
        } catch (Exception e) {
            this.throwCoreException(e.getMessage(), e);
        }
    }

    private void createEmptyModel(String containerName, String fileName) throws CoreException {
        ICommandService cmdService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
        IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
        Command createPmiFileCmd = cmdService.getCommand("q_impress.pmi.plugin.commands.createEmptyPerformanceModel");
        IParameter dirParm = null;
        IParameter fileParm = null;
        try {
            dirParm = createPmiFileCmd.getParameter("q_impress.pmi.plugin.commands.createEmptyPerformanceModel.directory");
            fileParm = createPmiFileCmd.getParameter("q_impress.pmi.plugin.commands.createEmptyPerformanceModel.filename");
        } catch (NotDefinedException e) {
            this.throwCoreException("Unable to execute command, invalid parameter :" + e.getMessage(), e);
        }
        Parameterization parmDir = new Parameterization(dirParm, containerName);
        Parameterization parmFile = new Parameterization(fileParm, fileName);
        ParameterizedCommand parmCommand = new ParameterizedCommand(createPmiFileCmd, new Parameterization[] { parmDir, parmFile });
        try {
            handlerService.executeCommand(parmCommand, null);
        } catch (Exception e) {
            this.throwCoreException(e.getMessage(), e);
        }
    }

    private void addUmlResource(String umlResourcePath, boolean doBundling, boolean doSync, String modelPath) throws CoreException {
        ICommandService cmdService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
        IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
        Command command = cmdService.getCommand(AddUmlResourceHandler.COMMAND_ID);
        IParameter umlPathParm = null;
        IParameter doBundlingParm = null;
        IParameter targetModelFileParm = null;
        try {
            umlPathParm = command.getParameter(AddUmlResourceHandler.PARAM_UML_RESOURCE_PATH);
            doBundlingParm = command.getParameter(AddUmlResourceHandler.PARAM_DO_BUNDLING);
            targetModelFileParm = command.getParameter(AddUmlResourceHandler.PARAM_TARGET_MODEL_PATH);
        } catch (NotDefinedException e) {
            this.throwCoreException("Unable to execute command, invalid parameter :" + e.getMessage(), e);
        }
        Parameterization parmUmlPath = new Parameterization(umlPathParm, umlResourcePath);
        Parameterization parmDoBundling = new Parameterization(doBundlingParm, Boolean.toString(doBundling));
        Parameterization parmTargetModelFile = new Parameterization(targetModelFileParm, modelPath);
        ParameterizedCommand parmCommand = new ParameterizedCommand(command, new Parameterization[] { parmUmlPath, parmDoBundling, parmTargetModelFile });
        try {
            handlerService.executeCommand(parmCommand, null);
        } catch (Exception e) {
            this.throwCoreException(e.getMessage(), e);
        }
    }

    private void throwCoreException(String message, Exception e) throws CoreException {
        IStatus status = new Status(IStatus.ERROR, "q_impress", IStatus.ERROR, message, e);
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
