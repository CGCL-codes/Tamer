package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.service.PerspectiveContent;
import org.nakedobjects.plugins.dnd.view.Axes;
import org.nakedobjects.plugins.dnd.view.CompositeViewSpecification;
import org.nakedobjects.plugins.dnd.view.Content;
import org.nakedobjects.plugins.dnd.view.View;
import org.nakedobjects.plugins.dnd.view.ViewAxis;
import org.nakedobjects.plugins.dnd.view.ViewRequirement;
import org.nakedobjects.plugins.dnd.view.Workspace;
import org.nakedobjects.plugins.dnd.view.base.Layout;
import org.nakedobjects.plugins.dnd.view.composite.ViewBuilder;

public class WorkspaceSpecification implements CompositeViewSpecification {

    ApplicationWorkspaceBuilder builder = new ApplicationWorkspaceBuilder();

    public View createView(final Content content, Axes axes, int sequence) {
        Workspace workspace;
        workspace = new ApplicationWorkspace(content, axes, this, createLayout(content, axes), builder);
        return workspace;
    }

    public Layout createLayout(Content content, Axes axes) {
        return new ApplicationWorkspaceBuilder.ApplicationLayout();
    }

    public void createAxes(Content content, Axes axes) {
    }

    public ViewAxis axis(Content content) {
        return null;
    }

    public ViewBuilder getSubviewBuilder() {
        return builder;
    }

    public String getName() {
        return "Root Workspace";
    }

    public boolean isAligned() {
        return false;
    }

    public boolean isOpen() {
        return true;
    }

    public boolean isReplaceable() {
        return false;
    }

    public boolean isResizeable() {
        return false;
    }

    public boolean isSubView() {
        return false;
    }

    public boolean canDisplay(ViewRequirement requirement) {
        return requirement.isFor(PerspectiveContent.class);
    }
}
