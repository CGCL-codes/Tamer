package edu.harvard.fas.rbrady.tpteam.tpbuddy.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNode;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.ITreeNodeChangeListener;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;

/*******************************************************************************
 * File 		: 	TestContentProvider.java
 * 
 * Description 	: 	Provides the content for the Test View
 * 
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$ Copyright (c) 2007 Bob Brady
 ******************************************************************************/
public class TestContentProvider extends ArrayContentProvider implements ITreeContentProvider, ITreeNodeChangeListener {

    /** The Test View TreeViewer */
    private TreeViewer mTreeViewer;

    /**
	 * Refreshes the tree when input has changed
	 * 
	 * @param viewer
	 *            the TestView's TreeViewer
	 * @param oldInput
	 *            the old data input
	 * @param newIput
	 *            the new data input
	 */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        mTreeViewer = (TreeViewer) viewer;
        if (oldInput != null) {
            for (ITreeNode node : (TPEntity[]) oldInput) removeListenerFrom(node);
        }
        if (newInput != null) {
            for (ITreeNode node : (TPEntity[]) newInput) addListenerTo(node);
        }
    }

    /**
	 * Because the domain model does not have a richer listener model,
	 * recursively remove this listener from each child node of the given
	 * ITreeNode.
	 * 
	 * @param node
	 *            the ITreeNode
	 */
    protected void removeListenerFrom(ITreeNode node) {
        node.removeChangeListener(this);
        for (ITreeNode child : node.getChildren()) {
            removeListenerFrom(child);
        }
    }

    /**
	 * Because the domain model does not have a richer listener model,
	 * recursively add this listener to each child node of the given 
	 * ITreeNode.
	 * 
	 * @param node the ITreeNode
	 */
    protected void addListenerTo(ITreeNode node) {
        node.addChangeListener(this);
        for (ITreeNode child : node.getChildren()) {
            addListenerTo(child);
        }
    }

    public void updateNode(ITreeNode node) {
        mTreeViewer.refresh(node.getParent(), true);
    }

    public void addNode(ITreeNode node) {
        addListenerTo(node);
        mTreeViewer.refresh(node.getParent(), false);
    }

    public void deleteNode(ITreeNode node) {
        removeListenerFrom(node);
        mTreeViewer.refresh(node.getParent(), true);
    }

    public Object getParent(Object child) {
        return ((ITreeNode) child).getParent();
    }

    public Object[] getChildren(Object parent) {
        return ((ITreeNode) parent).getChildren().toArray(new ITreeNode[0]);
    }

    public boolean hasChildren(Object parent) {
        return ((ITreeNode) parent).getChildren().size() > 0;
    }
}
