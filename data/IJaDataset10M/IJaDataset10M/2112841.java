package org.gvt.editpolicy;

import org.eclipse.draw2d.*;
import org.eclipse.gef.*;
import org.eclipse.swt.graphics.Color;
import org.gvt.ChisioMain;
import org.gvt.figure.CompoundFigure;

/**
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class CompoundHighlightEditPolicy extends org.eclipse.gef.editpolicies.GraphicalEditPolicy {

    private Figure selectionFigure;

    public void eraseTargetFeedback(Request request) {
        if (selectionFigure != null) {
            getContainerFigure().remove(selectionFigure);
            selectionFigure = null;
        }
    }

    private IFigure getContainerFigure() {
        return ((GraphicalEditPart) getHost()).getFigure();
    }

    public EditPart getTargetEditPart(Request request) {
        return request.getType().equals(RequestConstants.REQ_SELECTION_HOVER) ? getHost() : null;
    }

    protected void showHighlight() {
        if (selectionFigure == null && ChisioMain.transferNode) {
            selectionFigure = new RectangleFigure() {

                protected void fillShape(Graphics graphics) {
                    if (ChisioMain.runningOnWindows) {
                        graphics.setAlpha(100);
                    }
                    graphics.setBackgroundColor(ColorConstants.cyan);
                    graphics.fillRectangle(getBounds());
                }
            };
            selectionFigure.setBounds(getContainerFigure().getBounds());
            getContainerFigure().add(selectionFigure, 0);
        }
    }

    public void showTargetFeedback(Request request) {
        if (request.getType().equals(RequestConstants.REQ_MOVE) || request.getType().equals(RequestConstants.REQ_ADD) || request.getType().equals(RequestConstants.REQ_CLONE) || request.getType().equals(RequestConstants.REQ_CONNECTION_START) || request.getType().equals(RequestConstants.REQ_CONNECTION_END) || request.getType().equals(RequestConstants.REQ_CREATE)) {
            showHighlight();
        }
    }
}
