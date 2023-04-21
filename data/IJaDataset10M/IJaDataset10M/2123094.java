package org.wsmostudio.bpmo.figures;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;

public class ConditionFigure extends Shape {

    public ConditionFigure() {
        super();
        setBorder(new MarginBorder(3, 5, 3, 5));
        setOpaque(false);
    }

    @Override
    protected void fillShape(Graphics graphics) {
        Rectangle r = getBounds();
        graphics.fillPolygon(new int[] { r.x, r.y + r.height / 2, r.x + r.width / 2, r.y, r.x + r.width, r.y + r.height / 2, r.x + r.width / 2, r.y + r.height });
    }

    @Override
    protected void outlineShape(Graphics graphics) {
        Rectangle r = getBounds();
        int centerX = r.x + r.width / 2;
        int centerY = r.y + r.height / 2;
        graphics.drawPolygon(new int[] { centerX - r.width / 2, centerY, centerX, centerY - r.height / 2, centerX + r.width / 2, centerY, centerX, centerY + r.height / 2 });
    }
}
