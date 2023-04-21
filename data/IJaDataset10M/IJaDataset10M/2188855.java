package org.iascf.itmm.client.selection.ui.tree;

import com.extjs.gxt.ui.client.widget.tree.Tree;
import java.util.LinkedList;
import java.util.List;
import org.iascf.itmm.client.application.externalLink.ViewerBinder;

/**
 * Abstract ITMM tree.
 * 
 * @author Lukas Pruschke (lpruschke@iasb.org)
 */
public abstract class ATree extends Tree implements ISourcesViewerClickedEvent {

    protected List<ITreeListener> treeEventListener;

    public ATree() {
        this.treeEventListener = new LinkedList<ITreeListener>();
    }

    /**
     * Resets the tree
     */
    public abstract void reset();

    public void addTreeListener(ITreeListener listener) {
        this.treeEventListener.add(listener);
    }

    public void removeTreeListener(ITreeListener listener) {
        this.treeEventListener.remove(listener);
    }

    /**
     * Forwards viewer clicked command to all listeners
     * @param item
     */
    public void viewerClicked(SubModuleTreeItem item) {
        ViewerBinder.getInstance().viewerClicked(item);
    }
}
