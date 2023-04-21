package org.jowidgets.impl.event;

import java.util.LinkedList;
import java.util.List;
import org.jowidgets.api.controller.ITreeSelectionEvent;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.util.Assert;

public final class TreeSelectionEvent implements ITreeSelectionEvent {

    private final List<ITreeNode> selected;

    private final List<ITreeNode> unselected;

    public TreeSelectionEvent(final List<ITreeNode> selected, final List<ITreeNode> unselected) {
        Assert.paramNotNull(selected, "selected");
        Assert.paramNotNull(unselected, "unselected");
        this.selected = selected;
        this.unselected = unselected;
    }

    @Override
    public List<ITreeNode> getSelected() {
        return new LinkedList<ITreeNode>(selected);
    }

    @Override
    public List<ITreeNode> getUnselected() {
        return new LinkedList<ITreeNode>(unselected);
    }

    @Override
    public ITreeNode getFirstSelected() {
        if (selected.size() > 0) {
            return selected.get(0);
        }
        return null;
    }

    @Override
    public ITreeNode getFirstUnselected() {
        if (unselected.size() > 0) {
            return unselected.get(0);
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(TreeSelectionEvent.class.getSimpleName() + " unselected: {");
        for (final ITreeNode unselectedNode : unselected) {
            result.append(unselectedNode + ", ");
        }
        if (unselected.size() > 0) {
            result.replace(result.length() - 2, result.length(), "} ");
        } else {
            result.append("} ");
        }
        result.append(TreeSelectionEvent.class.getSimpleName() + " selected: {");
        for (final ITreeNode selectedNode : selected) {
            result.append(selectedNode + ", ");
        }
        if (selected.size() > 0) {
            result.replace(result.length() - 2, result.length(), "} ");
        } else {
            result.append("} ");
        }
        return result.toString();
    }
}
