package org.deft.operation.gui;

import java.util.Collection;
import org.deft.requirementsontology.IndividualVertex;
import org.deft.requirementsontology.ObjectPropertyEdge;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;

public class RequirementsGraphTableContentProvider implements ITreeContentProvider {

    private DelegateForest<IndividualVertex, ObjectPropertyEdge> forest;

    @Override
    @SuppressWarnings("unchecked")
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        this.forest = (DelegateForest<IndividualVertex, ObjectPropertyEdge>) newInput;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof DelegateForest) {
            DelegateForest<IndividualVertex, ObjectPropertyEdge> forest = (DelegateForest<IndividualVertex, ObjectPropertyEdge>) inputElement;
            Collection<IndividualVertex> trees = forest.getRoots();
            return trees.toArray();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof DelegateForest) {
            return getElements(parentElement);
        } else if (parentElement instanceof DelegateTree) {
            DelegateTree<IndividualVertex, ObjectPropertyEdge> tree = (DelegateTree<IndividualVertex, ObjectPropertyEdge>) parentElement;
            IndividualVertex root = (IndividualVertex) tree.getRoot();
            Object[] children = tree.getChildren(root).toArray();
            return children;
        } else if (parentElement instanceof IndividualVertex) {
            Collection<IndividualVertex> c = forest.getChildren((IndividualVertex) parentElement);
            return c.toArray();
        }
        return null;
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof DelegateTree) {
            return forest;
        } else if (element instanceof IndividualVertex) {
            Object parent = forest.getParent((IndividualVertex) element);
            return parent;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hasChildren(Object element) {
        if (element instanceof DelegateTree) {
            DelegateTree<IndividualVertex, ObjectPropertyEdge> tree = (DelegateTree<IndividualVertex, ObjectPropertyEdge>) element;
            IndividualVertex root = (IndividualVertex) tree.getRoot();
            int childCount = tree.getChildCount(root);
            return childCount > 0;
        } else if (element instanceof IndividualVertex) {
            int childCount = forest.getChildCount((IndividualVertex) element);
            return childCount > 0;
        }
        return false;
    }

    @Override
    public void dispose() {
    }
}
