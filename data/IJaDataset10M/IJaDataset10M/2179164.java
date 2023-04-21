package org.genie.cmof.gef.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;

public class OperationFigure extends Figure {

    public OperationFigure() {
        FlowLayout layout = new FlowLayout();
        layout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
        layout.setStretchMinorAxis(false);
        layout.setHorizontal(false);
        setLayoutManager(layout);
        setBorder(new OperationFigureBorder());
        setBackgroundColor(ColorConstants.tooltipBackground);
        setForegroundColor(ColorConstants.blue);
        setOpaque(true);
    }

    class OperationFigureBorder extends AbstractBorder {

        public Insets getInsets(IFigure figure) {
            return new Insets(5, 3, 3, 1);
        }

        public void paint(IFigure figure, Graphics graphics, Insets insets) {
            graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());
        }
    }
}
