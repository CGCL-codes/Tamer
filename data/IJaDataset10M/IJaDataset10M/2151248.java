package org.cubictest.ui.gef.controller;

import org.cubictest.model.ConnectionPoint;
import org.cubictest.model.ExtensionPoint;
import org.cubictest.model.TransitionNode;
import org.cubictest.ui.gef.policies.StartPointNodeEditPolicy;
import org.cubictest.ui.gef.policies.TestComponentEditPolicy;
import org.cubictest.ui.gef.view.AbstractTransitionNodeFigure;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;

/**
 * @author SK Skytteren
 * Contoller for the <code>ExtensionPoint</code> model.
 *
 */
public class ExtensionPointEditPart extends AbstractNodeEditPart {

    /**
	 * Constructor for <code>ExtensionPointEditPart</code>.
	 * @param point the model
	 */
    public ExtensionPointEditPart(ExtensionPoint point) {
        setModel(point);
    }

    @Override
    protected IFigure createFigure() {
        String name = ((ExtensionPoint) getModel()).getName();
        AbstractTransitionNodeFigure extensionPointFigure = new AbstractTransitionNodeFigure();
        extensionPointFigure.setBackgroundColor(ColorConstants.orange);
        Point p = ((TransitionNode) getModel()).getPosition();
        extensionPointFigure.setLocation(p);
        extensionPointFigure.setText(name);
        return extensionPointFigure;
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new TestComponentEditPolicy());
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new StartPointNodeEditPolicy());
    }

    @Override
    protected void refreshVisuals() {
        ConnectionPoint connectionPoint = (ConnectionPoint) getModel();
        AbstractTransitionNodeFigure figure = (AbstractTransitionNodeFigure) getFigure();
        Point position = connectionPoint.getPosition();
        Rectangle r = new Rectangle(position.x, position.y, -1, -1);
        ((TestEditPart) getParent()).setLayoutConstraint(this, figure, r);
    }

    public void updateParams() {
        refresh();
        refreshVisuals();
    }
}
