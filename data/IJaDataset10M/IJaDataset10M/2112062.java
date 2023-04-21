package fr.inria.papyrus.uml4tst.core;

import java.util.Iterator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.part.FileEditorInput;

public class ProjectGetter {

    public static IProject getIProjectFromEditor(FileEditorInput input) {
        return input.getFile().getProject();
    }

    @SuppressWarnings("unchecked")
    private static IResource selectionToDefaultPath(final ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            for (final Iterator<Object> iterator = structuredSelection.iterator(); iterator.hasNext(); ) {
                final Object element = iterator.next();
                if (element instanceof IProject || element instanceof IFolder || element instanceof IFile) {
                    return (IResource) element;
                }
            }
        }
        return null;
    }

    public static IProject getIProjectFromSelection(final ISelection selection) {
        final IResource folder = selectionToDefaultPath(selection);
        if (folder == null || !folder.isAccessible()) {
            return null;
        } else {
            return folder.getProject();
        }
    }
}
