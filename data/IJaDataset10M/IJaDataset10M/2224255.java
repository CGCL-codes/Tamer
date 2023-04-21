package org.wtc.eclipse.platform.helpers.adapters;

import junit.framework.TestCase;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import org.wtc.eclipse.core.util.Eclipse;
import org.wtc.eclipse.platform.helpers.EclipseHelperFactory;
import org.wtc.eclipse.platform.helpers.IProjectHelper;
import org.wtc.eclipse.platform.helpers.IWorkbenchHelper;
import org.wtc.eclipse.platform.util.ExceptionHandler;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.swt.condition.eclipse.ProjectExistsCondition;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.FilteredTreeItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TabItemLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

/**
 * Helper for creating and manipulating projects.
 *
 * @since  3.8.0
 */
public abstract class ProjectHelperImplAdapter extends HelperImplAdapter {

    private static final String CONFIRM_PROJECT_DELETE_SHELL_TITLE = Eclipse.VERSION.is(3, 3) ? "Confirm Project Delete" : "Delete Resources";

    /**
     * addProjectBuildDependency - Open the project properties for a given project and add
     * a project dependency to another given project.
     *
     * @since  3.8.0
     * @param  ui  - Driver for UI generated input
     */
    public void addProjectBuildDependency(IUIContext ui, String sourceProject, String targetProject) {
        TestCase.assertNotNull(ui);
        TestCase.assertNotNull(sourceProject);
        TestCase.assertNotNull(targetProject);
        logEntry2(sourceProject, targetProject);
        try {
            invokeProjectPropertiesDialog(ui, sourceProject);
            ui.click(new FilteredTreeItemLocator("Java Build Path"));
            ui.click(new TabItemLocator("&Projects"));
            ui.click(new ButtonLocator("&Add..."));
            ui.wait(new ShellShowingCondition("Required Project Selection"));
            ui.click(1, new TableItemLocator(targetProject), SWT.CHECK | SWT.BUTTON1);
            clickOK(ui);
            clickOK(ui);
            ui.wait(new ShellDisposedCondition("Properties for " + sourceProject));
        } catch (WidgetSearchException wse) {
            ExceptionHandler.handle(wse);
        }
        logExit2();
    }

    /**
     * Delete the given project from the workspace.
     *
     * @since  3.8.0
     * @param  ui           - Driver for UI generated input
     * @param  projectName  - Should adhere to project name validation rules (not null,
     *                      not the empty string, legal characters, etc)
     */
    public void deleteProject(IUIContext ui, String projectName) {
        deleteProject(ui, projectName, false);
    }

    /**
     * Delete the given project from the workspace.
     *
     * @since  3.8.0
     * @param  ui           - Driver for UI generated input
     * @param  projectName  - Should adhere to project name validation rules (not null,
     *                      not the empty string, legal characters, etc)
     */
    public void deleteProject(IUIContext ui, String projectName, boolean deleteContents) {
        TestCase.assertNotNull(ui);
        TestCase.assertNotNull(projectName);
        logEntry2(projectName, Boolean.toString(deleteContents));
        IWorkbenchHelper workbench = EclipseHelperFactory.getWorkbenchHelper();
        workbench.openView(ui, IWorkbenchHelper.View.JAVA_PACKAGEEXPLORER);
        try {
            ui.contextClick(new TreeItemLocator(projectName, new SWTWidgetLocator(Tree.class, new ViewLocator(IWorkbenchHelper.View.JAVA_PACKAGEEXPLORER.getViewID()))), "&Delete\tDelete");
            ui.wait(new ShellShowingCondition(CONFIRM_PROJECT_DELETE_SHELL_TITLE));
            if (Eclipse.VERSION.is(3, 3)) {
                if (deleteContents) {
                    ui.click(new ButtonLocator("&Also delete.*"));
                } else {
                    ui.click(new ButtonLocator("&Do not delete.*"));
                }
                clickYes(ui);
            } else {
                ui.click(new ButtonLocator("&Delete project contents on disk.*"));
                clickOK(ui);
            }
            ui.wait(new ShellDisposedCondition(CONFIRM_PROJECT_DELETE_SHELL_TITLE));
            waitForProjectExists(ui, projectName, false);
        } catch (WidgetSearchException wse) {
            ExceptionHandler.handle(wse);
        }
        logExit2();
    }

    /**
     * TEMPORARY WORKAROUND: Get the project properties dialog to show
     *
     * <p>invokeProjecPropertiesDialog - Show the properties dialog for the project with
     * the given name</p>
     *
     * @since  3.8.0
     * @param  ui           - Driver for UI generated input
     * @param  projectName  - The project whose properties dialog is to be shown
     */
    public void invokeProjectPropertiesDialog(IUIContext ui, final String projectName) {
        TestCase.assertNotNull(projectName);
        waitForProjectExists(ui, projectName, true);
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                IWorkbenchSite wbSite = getActiveWorkbenchSite();
                PropertyDialogAction propertyAction = new PropertyDialogAction(wbSite, new ISelectionProvider() {

                    public void addSelectionChangedListener(ISelectionChangedListener listener) {
                    }

                    public ISelection getSelection() {
                        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
                        return new StructuredSelection(project);
                    }

                    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
                    }

                    public void setSelection(ISelection selection) {
                    }
                });
                propertyAction.run();
            }

            private IWorkbenchSite getActiveWorkbenchSite() {
                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                if (window == null) return null;
                IWorkbenchPage curPage = window.getActivePage();
                if (curPage == null) return null;
                IWorkbenchPart curPart = curPage.getActivePart();
                if (curPart == null) return null;
                return curPart.getSite();
            }
        });
        ui.wait(new ShellShowingCondition("Properties for " + projectName));
    }

    /**
     * @see  IProjectHelper#waitForProjectExists(IUIContext, String, boolean)
     */
    public void waitForProjectExists(IUIContext ui, String projectName, boolean exists) {
        waitForProjectExists(ui, projectName, exists, 45000, 2500);
    }

    /**
     * @see  IProjectHelper#waitForProjectExists(IUIContext, String, boolean, long, int)
     */
    public void waitForProjectExists(IUIContext ui, String projectName, boolean exists, long timeout, int interval) {
        logEntry2(projectName, Boolean.toString(exists), Long.toString(timeout), Integer.toString(interval));
        ui.wait(new ProjectExistsCondition(projectName, exists), timeout, interval);
        logExit2();
    }
}
