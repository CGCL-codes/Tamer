package org.nakedobjects.plugins.dnd.tree;

import org.nakedobjects.plugins.dnd.view.Axes;
import org.nakedobjects.plugins.dnd.view.Content;
import org.nakedobjects.plugins.dnd.view.View;
import org.nakedobjects.plugins.dnd.view.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.basic.NullFocusManager;

abstract class NodeSpecification implements ViewSpecification {

    public static final int CAN_OPEN = 1;

    public static final int CANT_OPEN = 2;

    public static final int UNKNOWN = 0;

    private ViewSpecification replacementNodeSpecification;

    public abstract int canOpen(final Content content);

    protected abstract View createNodeView(final Content content, Axes axes);

    public final View createView(final Content content, Axes axes, int sequence) {
        final View view = createNodeView(content, axes);
        final TreeNodeBorder newView = new TreeNodeBorder(view, replacementNodeSpecification);
        newView.setFocusManager(new NullFocusManager());
        return newView;
    }

    public boolean isAligned() {
        return false;
    }

    public boolean isOpen() {
        return false;
    }

    public boolean isReplaceable() {
        return false;
    }

    public boolean isResizeable() {
        return false;
    }

    public boolean isSubView() {
        return true;
    }

    final void setReplacementNodeSpecification(final ViewSpecification replacementNodeSpecification) {
        this.replacementNodeSpecification = replacementNodeSpecification;
    }
}
