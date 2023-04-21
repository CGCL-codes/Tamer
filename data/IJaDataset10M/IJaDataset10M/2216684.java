package com.safi.asterisk.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;

/**
 * @generated
 */
public class OutputTargetFigure extends PolylineConnectionEx {

    /**
   * @generated NOT
   */
    public OutputTargetFigure() {
        setTargetDecoration(createTargetDecoration());
        setForegroundColor(ColorConstants.blue);
    }

    /**
   * @generated
   */
    private RotatableDecoration createTargetDecoration() {
        PolylineDecoration df = new PolylineDecoration();
        df.setLineWidth(2);
        return df;
    }
}
