package org.isistan.flabot.edit.componenteditor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * RequiredInterfaceFigure
 * -	The figure used for required interfaces
 * 
 *  @author $Author: mblech $
 */
public class RequiredInterfaceFigure extends InterfaceFigure {

    /**
	 * Instantiates an instance of RequiredInterfaceFigure
	 */
    public RequiredInterfaceFigure() {
        anchor.setOffsetV(7);
    }

    /**
	 * Draw the required interface.
	 */
    protected void paintFigure(Graphics graphics) {
        graphics.setForegroundColor(ColorConstants.black);
        graphics.setBackgroundColor(ColorConstants.black);
        Rectangle r = getBounds().getCopy();
        r.crop(new Insets(2, 0, 2, 0));
        int x1 = 0;
        int degree = 0;
        if (getSide().equals(PortFigure.LEFT_SIDE)) {
            x1 = r.x - 6;
            degree = 270;
        } else if (getSide().equals(PortFigure.RIGHT_SIDE)) {
            x1 = r.x + r.width - 11;
            degree = 90;
        }
        graphics.setLineWidth(2);
        Rectangle oval = new Rectangle(x1, (r.y + r.height / 2) - 6, 12, 12);
        graphics.drawArc(oval, degree, 180);
        graphics.drawLine(r.x + 5, (r.y + r.height / 2), r.x + 30, (r.y + r.height / 2));
    }

    /**
	 * Set the side of the interface figure in the component, and updates the connection anchor according to this side.
	 * @param side the side of the interface figure
	 */
    public void setSide(String side) {
        super.setSide(side);
        if (side.equals(PortFigure.LEFT_SIDE)) {
            anchor.setOffsetH(5);
        } else if (side.equals(PortFigure.RIGHT_SIDE)) {
            anchor.setOffsetH(30);
        }
    }
}
