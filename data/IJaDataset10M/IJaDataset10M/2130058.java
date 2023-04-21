package ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchyTreeView;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

public class TestHierarchyTreeViewContentProvider implements ITreeContentProvider, IDeltaListener {

    private static Object[] EMPTY_ARRAY = new Object[0];

    protected TreeViewer viewer;

    public Object[] getChildren(Object parentElement) {
        return null;
    }

    public Object getParent(Object element) {
        return null;
    }

    public boolean hasChildren(Object element) {
        return false;
    }

    public Object[] getElements(Object inputElement) {
        return null;
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    public void add(DeltaEvent event) {
    }

    public void remove(DeltaEvent event) {
    }
}
