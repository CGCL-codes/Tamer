package serl.equalschecker.jobs;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import serl.equalschecker.EqualsCheckerPlugin;
import serl.equalschecker.analysis.EqualsMethodDispatcher;
import serl.equalschecker.codegeneration.AlloyCodeAnalysisManager;
import serl.equalschecker.codegeneration.HierarchyCodeGenerator;
import serl.equalschecker.controlflow.InterProceduralPathGenerator;
import serl.equalschecker.dom.SootClassLoader;
import serl.equalschecker.dom.HierarchyBuilder;
import serl.equalschecker.dom.EqualsMethodFinder;
import serl.equalschecker.globals.Globals;
import serl.equalschecker.views.EqualsCheckerWindow;
import serl.equalschecker.views.HierarchyErrorView;

/**
 *
 *
 * @author Chandan Raj Rupakheti (rupakhcr@clarkson.edu)
 *
 */
public class EqualsCheckerMainJob extends EqualsCheckerJob {

    private final String projectName;

    private final EqualsCheckerWindow window;

    /**
	 * Creates a new instance of the class EqualsCheckerMainJob
	 * 
	 * @param name
	 */
    public EqualsCheckerMainJob(String projectName, EqualsCheckerWindow window) {
        super("Analyzing equals implementation of - " + projectName);
        this.projectName = projectName;
        this.window = window;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        monitor.beginTask("Checking problems with equals implementation ...", 6);
        try {
            monitor.setTaskName("Initializing ...");
            Globals.reset(projectName);
            monitor.worked(1);
            SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1);
            EqualsMethodFinder traverser = new EqualsMethodFinder(subMonitor);
            traverser.process();
            if (monitor.isCanceled()) {
                return Status.CANCEL_STATUS;
            }
            traverser = null;
            subMonitor.done();
            subMonitor = new SubProgressMonitor(monitor, 1);
            HierarchyBuilder hierarchyBuilder = new HierarchyBuilder(subMonitor);
            hierarchyBuilder.process();
            if (monitor.isCanceled()) {
                return Status.CANCEL_STATUS;
            }
            hierarchyBuilder = null;
            subMonitor.done();
            FileWriterJob.start();
            subMonitor = new SubProgressMonitor(monitor, 1);
            HierarchyCodeGenerator codeGenerator = new HierarchyCodeGenerator(subMonitor);
            codeGenerator.process();
            if (monitor.isCanceled()) {
                return Status.CANCEL_STATUS;
            }
            codeGenerator = null;
            subMonitor.done();
            monitor.setTaskName("Resetting soot ...");
            SootClassLoader.boot();
            monitor.worked(1);
            FileWriterJob.write(FileWriterJob.PATH, "0AccumulatedPathSize.txt", InterProceduralPathGenerator.getMethodToPathSizeData());
            FileWriterJob.write(FileWriterJob.PATH, "1RecusionFilter.txt", EqualsMethodDispatcher.getRecursionMapText());
            subMonitor = new SubProgressMonitor(monitor, 1);
            AlloyCodeAnalysisManager alloyAnalysis = new AlloyCodeAnalysisManager(subMonitor);
            alloyAnalysis.process();
            if (monitor.isCanceled()) {
                return Status.CANCEL_STATUS;
            }
            alloyAnalysis = null;
            subMonitor.done();
            FileWriterJob.stopAndJoin();
            try {
                monitor.subTask("Refresing output resources and updating error view ...");
                IProject project = SootClassLoader.getAnalysisProject().getProject();
                project.refreshLocal(IProject.DEPTH_INFINITE, null);
            } catch (Exception e) {
                EqualsCheckerPlugin.log(Status.WARNING, "Cannot refresh output folders of equals checker analysis", e);
            }
            window.getDisplay().syncExec(new Runnable() {

                public void run() {
                    window.showDefaultModel();
                    MessageDialog.openInformation(window.getShell(), "Analysis Complete", "Equals analysis completed successfully! Please check error log for intermediate problems.");
                    try {
                        IWorkbenchWindow workWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                        IWorkbenchPage workPage = workWindow.getActivePage();
                        IViewPart viewPart = workPage.findView(HierarchyErrorView.ID);
                        if (viewPart != null) {
                            HierarchyErrorView view = (HierarchyErrorView) viewPart;
                            view.activate();
                        }
                    } catch (Exception e) {
                        EqualsCheckerPlugin.log(Status.ERROR, "Problem activating save button.", e);
                    }
                }
            });
            monitor.done();
        } catch (Throwable t) {
            Globals.stopAnalysis();
            return new Status(Status.ERROR, EqualsCheckerPlugin.PLUGIN_ID, Status.CANCEL, "Error Processing Equals Analysis", t);
        }
        return Status.OK_STATUS;
    }
}
