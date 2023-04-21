package uk.ac.ed.csbe.plasmo;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class PlasmoContentProvider implements ITreeContentProvider {

    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof IFile) {
            IFile file = (IFile) parentElement;
            String[] idAndVer = PlasmoUtil.extractPlasmoIDAndVersion(file);
            SimpleTextAdapter text = new SimpleTextAdapter(file, idAndVer[0], idAndVer[1]);
            return new Object[] { text };
        }
        return null;
    }

    public Object getParent(Object element) {
        if (element instanceof IFile) {
            return null;
        } else if (element instanceof SimpleTextAdapter) {
            return ((SimpleTextAdapter) element).getParent();
        }
        return null;
    }

    public boolean hasChildren(Object element) {
        if (element instanceof IFile) return true;
        return false;
    }

    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
