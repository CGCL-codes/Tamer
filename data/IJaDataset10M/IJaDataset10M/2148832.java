package edu.vt.eng.swat.workflow.view.workflow.customer;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import edu.vt.eng.swat.workflow.utils.tree.TreeNode;

public class CustomerContentProvider implements ITreeContentProvider {

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof TreeNode) {
            if (((TreeNode) parentElement).getChildren() != null) {
                return ((TreeNode) parentElement).getChildren();
            } else {
                return new Object[0];
            }
        } else {
            throw new IllegalArgumentException("parentElement should be of TreeNode type ");
        }
    }

    @Override
    public Object getParent(Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        return getChildren(element).length > 0;
    }

    @Override
    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
