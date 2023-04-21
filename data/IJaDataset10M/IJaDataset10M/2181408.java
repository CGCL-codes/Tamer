package org.nakedobjects.plugins.dnd.view.composite;

import org.nakedobjects.plugins.dnd.view.Axes;
import org.nakedobjects.plugins.dnd.view.UserActionSet;
import org.nakedobjects.plugins.dnd.view.View;

public abstract class AbstractBuilderDecorator implements ViewBuilder {

    protected final ViewBuilder wrappedBuilder;

    public AbstractBuilderDecorator(final ViewBuilder design) {
        this.wrappedBuilder = design;
    }

    public void build(final View view, Axes axes) {
        wrappedBuilder.build(view, axes);
    }

    public boolean isOpen() {
        return wrappedBuilder.isOpen();
    }

    public boolean isReplaceable() {
        return wrappedBuilder.isReplaceable();
    }

    public boolean isSubView() {
        return wrappedBuilder.isSubView();
    }

    @Override
    public String toString() {
        final String name = getClass().getName();
        return wrappedBuilder + "/" + name.substring(name.lastIndexOf('.') + 1);
    }

    public void viewMenuOptions(UserActionSet options, View view) {
        wrappedBuilder.viewMenuOptions(options, view);
    }
}
