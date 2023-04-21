package org.eclipse.gef.examples.logicdesigner.figures;

import org.eclipse.draw2d.RectangleFigure;

public class CircuitFeedbackFigure extends RectangleFigure {

    public CircuitFeedbackFigure() {
        this.setFill(false);
        this.setXOR(true);
        setBorder(new CircuitFeedbackBorder());
    }

    protected boolean useLocalCoordinates() {
        return true;
    }
}
