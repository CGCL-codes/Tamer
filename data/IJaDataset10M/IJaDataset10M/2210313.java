package org.designerator.explorer.model;

import java.io.*;
import org.eclipse.jface.viewers.*;

public class FileTreeContentProvider implements ITreeContentProvider {

    private final class DirFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            return file.isDirectory();
        }
    }

    private static DirFilter dirFilter;

    public FileTreeContentProvider() {
        dirFilter = new DirFilter();
    }

    public Object[] getChildren(Object element) {
        Object[] children = null;
        if (element instanceof File) {
            children = ((File) element).listFiles(dirFilter);
            return children == null ? new Object[0] : children;
        } else if (element instanceof FileRoot) {
            children = ((FileRoot) element).listFiles(dirFilter);
            return children == null ? new Object[0] : children;
        } else if (element instanceof FileRootProvider) {
            children = ((FileRootProvider) element).listFiles();
            return children == null ? new Object[0] : children;
        }
        return new Object[0];
    }

    public Object[] getElements(Object element) {
        return getChildren(element);
    }

    public boolean hasChildren(Object element) {
        return true;
    }

    public Object getParent(Object element) {
        if (element instanceof File) {
            return ((File) element).getParent();
        }
        if (element instanceof FileRootProvider) {
            return null;
        }
        if (element instanceof FileRoot) {
            return null;
        }
        return null;
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object old_input, Object new_input) {
    }
}
