package ingenias.plugin.actions;

import ingenias.codeproc.IAFGenerator;
import ingenias.editor.IDEState;
import ingenias.editor.ProjectProperty;
import ingenias.generator.browser.BrowserImp;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Properties;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.*;
import ingenias.plugin.editor.*;

public abstract class INGENIASModuleAction implements IObjectActionDelegate, IWorkbenchWindowActionDelegate {

    protected String CONSOLE_NAME = "IAF Output";

    private IWorkbenchWindow window;

    private Shell shell;

    private IWorkbenchPart currentPart;

    private DiagramEditor currentEditor;

    private IPartListener partListener;

    /**
	 * Constructor for Action1.
	 */
    public INGENIASModuleAction(String consoleName) {
        super();
        this.CONSOLE_NAME = consoleName;
    }

    /**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        if (!(targetPart instanceof DiagramEditor)) return;
        shell = targetPart.getSite().getShell();
        currentEditor = (DiagramEditor) targetPart;
    }

    protected static IProject getProject(ISelection selection) {
        if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
            IStructuredSelection ssel = (IStructuredSelection) selection;
            if (ssel.size() > 1) return null;
            Object obj = ssel.getFirstElement();
            if (obj.getClass().getName().equals("IJavaElement")) obj = ((IJavaElement) obj).getResource();
            if (obj instanceof IResource) {
                return ((IResource) obj).getProject();
            }
            if (obj instanceof JavaProject) {
                return ((JavaProject) obj).getProject();
            }
        }
        if (selection instanceof ITextSelection || selection == null) {
            IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            IEditorInput editorInput = editorPart.getEditorInput();
            if (editorInput instanceof IFileEditorInput) {
                IFileEditorInput fileInput = (IFileEditorInput) editorInput;
                return fileInput.getFile().getProject();
            }
        }
        return null;
    }

    /** Return the selected project or null if no project is selected **/
    public static IProject getSelectedProject(Shell shell) {
        ISelectionService service = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
        ISelection selection = service.getSelection();
        IProject project = getProject(selection);
        if (project == null) {
            MessageDialog.openInformation(shell, "Warning", "A project must be selected");
        }
        return project;
    }

    public void run(IAction action) {
        if (currentEditor != null && currentEditor.getModel() != null && currentEditor.getModel().getIDEState() != null) {
            final IDEState ids = currentEditor.getModel().getIDEState();
            try {
                new Thread() {

                    public void run() {
                        try {
                            IProject project = getSelectedProject(shell);
                            if (project == null) {
                                MessageDialog.openInformation(shell, "Generating code", "There is no INGENIAS project open or selected");
                                return;
                            }
                            String pathProject = project.getLocation().toString();
                            MessageConsole myConsole = findConsole(CONSOLE_NAME);
                            MessageConsoleStream out = myConsole.newMessageStream();
                            MessageConsoleStream err = myConsole.newMessageStream();
                            Color purple = new Color(Display.getCurrent(), 160, 32, 140);
                            err.setColor(purple);
                            IWorkbenchPage page = window.getActivePage();
                            String id = IConsoleConstants.ID_CONSOLE_VIEW;
                            IConsoleView view = (IConsoleView) page.showView(id);
                            view.display(myConsole);
                            ingenias.editor.Log.initInstance(new PrintWriter(out), new PrintWriter(err));
                            BrowserImp.initialise(ids);
                            launchModuleAction(ids, pathProject);
                            project.refreshLocal(IResource.DEPTH_INFINITE, new IProgressMonitor() {

                                @Override
                                public void beginTask(String name, int totalWork) {
                                }

                                @Override
                                public void done() {
                                }

                                @Override
                                public void internalWorked(double work) {
                                }

                                @Override
                                public boolean isCanceled() {
                                    return false;
                                }

                                @Override
                                public void setCanceled(boolean value) {
                                }

                                @Override
                                public void setTaskName(String name) {
                                }

                                @Override
                                public void subTask(String name) {
                                }

                                @Override
                                public void worked(int work) {
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.run();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            MessageDialog.openError(shell, "Error", "There has to be an open specification");
        }
    }

    private MessageConsole findConsole(String name) {
        ConsolePlugin plugin = ConsolePlugin.getDefault();
        IConsoleManager conMan = plugin.getConsoleManager();
        IConsole[] existing = conMan.getConsoles();
        for (int i = 0; i < existing.length; i++) if (name.equals(existing[i].getName())) return (MessageConsole) existing[i];
        MessageConsole myConsole = new MessageConsole(name, null);
        conMan.addConsoles(new IConsole[] { myConsole });
        return myConsole;
    }

    /**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
    public void dispose() {
    }

    /**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
    public void init(IWorkbenchWindow window) {
        this.window = window;
        shell = window.getShell();
        partListener = new IPartListener() {

            private void trackOpenChatEditors(IWorkbenchPart part) {
                if (!(part instanceof DiagramEditor)) return;
                DiagramEditor editor = (DiagramEditor) part;
                currentEditor = editor;
            }

            @Override
            public void partActivated(IWorkbenchPart part) {
                trackOpenChatEditors(part);
            }

            @Override
            public void partBroughtToTop(IWorkbenchPart part) {
                trackOpenChatEditors(part);
            }

            @Override
            public void partClosed(IWorkbenchPart part) {
                if (part == currentEditor) currentEditor = null;
            }

            @Override
            public void partDeactivated(IWorkbenchPart arg0) {
            }

            @Override
            public void partOpened(IWorkbenchPart part) {
                trackOpenChatEditors(part);
            }
        };
        window.getPartService().addPartListener(partListener);
    }

    public abstract void launchModuleAction(final IDEState ids, String pathProject) throws FileNotFoundException;
}
