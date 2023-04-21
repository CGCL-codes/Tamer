package net.sourceforge.dalutils4j.eclipse;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.JavaElementComparator;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

public class ProjectFolderSelector {

    public static IPath selectProjectFolder(Shell parent, IJavaProject javaProject, String title) {
        IProject project = (IProject) javaProject.getResource();
        ITreeContentProvider provider = new FolderTreeContentProvider(project);
        ILabelProvider labelProvider = new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(parent, labelProvider, provider);
        dialog.setComparator(new JavaElementComparator());
        if (title == null) {
            title = "Select a folder";
        }
        dialog.setTitle(title);
        dialog.setInput(project.getWorkspace().getRoot());
        dialog.setInitialSelection(project);
        dialog.setHelpAvailable(false);
        if (dialog.open() == Window.OK) {
            Object element = dialog.getFirstResult();
            if (element instanceof IContainer) {
                return ((IContainer) element).getFullPath();
            }
            return null;
        }
        return null;
    }
}
