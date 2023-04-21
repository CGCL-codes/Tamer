package net.sourceforge.dalutils4j.eclipse;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementComparator;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

public class WorkspaceSourceFolderSelector {

    public static IPackageFragmentRoot select(Shell parent, IPackageFragmentRoot initialSelection) {
        StandardJavaElementContentProvider provider = new StandardJavaElementContentProvider() {

            @Override
            public boolean hasChildren(Object element) {
                if (element instanceof IPackageFragmentRoot) {
                    return false;
                }
                return super.hasChildren(element);
            }

            @Override
            public Object[] getChildren(Object element) {
                if (element instanceof IWorkspaceRoot) {
                    IWorkspaceRoot ws = (IWorkspaceRoot) element;
                    IProject[] projects = ws.getProjects();
                    return projects;
                } else if (element instanceof IProject) {
                    IProject project = (IProject) element;
                    try {
                        if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
                            IJavaProject javaProject = JavaCore.create(project);
                            return getPackageFragmentRoots(javaProject);
                        }
                    } catch (Throwable e) {
                        return NO_CHILDREN;
                    }
                }
                return NO_CHILDREN;
            }
        };
        ILabelProvider labelProvider = new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent, labelProvider, provider);
        ISelectionStatusValidator validator = new ISelectionStatusValidator() {

            private Status WRONG = new Status(Status.ERROR, "unknown", 1, "", null);

            @Override
            public IStatus validate(Object[] selection) {
                if (selection != null && selection.length == 1) {
                    Object element = selection[0];
                    if (element instanceof IPackageFragmentRoot) {
                        try {
                            if (((IPackageFragmentRoot) element).getKind() == IPackageFragmentRoot.K_SOURCE) {
                                return Status.OK_STATUS;
                            }
                        } catch (JavaModelException e) {
                        }
                    }
                }
                return WRONG;
            }
        };
        ViewerFilter filter = new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parent, Object element) {
                System.out.println(element);
                if (element instanceof IProject) {
                    return true;
                } else if (element instanceof IPackageFragmentRoot) {
                    try {
                        return (((IPackageFragmentRoot) element).getKind() == IPackageFragmentRoot.K_SOURCE);
                    } catch (JavaModelException e) {
                        return false;
                    }
                }
                return false;
            }
        };
        dialog.addFilter(filter);
        dialog.setValidator(validator);
        dialog.setComparator(new JavaElementComparator());
        dialog.setTitle("Select source file");
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        dialog.setInput(workspaceRoot);
        dialog.setHelpAvailable(false);
        if (dialog.open() == Window.OK) {
            Object element = dialog.getFirstResult();
            if (element instanceof IPackageFragmentRoot) {
                return (IPackageFragmentRoot) element;
            }
            return null;
        }
        return null;
    }
}
