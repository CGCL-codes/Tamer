package com.jiexplorer.rcp.model;

import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.TreePath;
import com.jiexplorer.rcp.ui.INodeChangedListener;

public interface ITreeNode extends INamedObject, IUidObject, IAdaptable {

    public static final ITreeNode[] NONE = new ITreeNode[] {};

    public static final String NODE_OBJECT = "NODE_OBJECT";

    public List<ITreeNode> getChildren();

    public boolean hasChildren();

    public boolean isLeaf();

    public ITreeNode getParent();

    public void setParent(ITreeNode parent);

    public Object getNodeObject();

    public void removeChild(ITreeNode node);

    public void removeChildren(ITreeNode[] nodes);

    public void addChild(ITreeNode node);

    public void addNodeChangeListener(INodeChangedListener listener);

    public void removeNodeChangeListener(INodeChangedListener listener);

    public void fireNodesUpdated(ITreeNode[] children);

    public void fireNodesAdded(ITreeNode[] children);

    public void fireNodesRemoved(ITreeNode[] children);

    public void fireNodeExpand();

    public void fireNodeCollapse();

    public TreePath getTreePath();

    public List getAncestors();

    public void refresh();
}
