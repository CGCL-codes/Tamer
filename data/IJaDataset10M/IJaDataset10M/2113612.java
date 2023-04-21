package org.isistan.flabot.edit.ucmeditor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * PathPointFigure
 * -	Figure for path points. Supports start points and end points.
 * 
 * @author $Author: franco $
 *
 */
public class PathPointFigure extends Shape {

    public static final Dimension defaultsize = new Dimension(15, 15);

    public static final Font defaultFont = new Font(Display.getCurrent(), "Verdana", 8, SWT.NONE);

    protected Label name = new Label();

    protected IFigure parent;

    private boolean isStartPoint;

    private boolean isEndPoint;

    private double slope;

    /**
	 * Instantiates an instance of PathPointFigure
	 * 
	 * @param fC the foreground color of the path point
	 */
    public PathPointFigure(RGB fC) {
        setSize(defaultsize);
        setOpaque(true);
        setForegroundColor(new Color(Display.getCurrent(), fC));
        name.setBackgroundColor(ColorConstants.buttonLightest);
        name.setOpaque(false);
        name.setFont(defaultFont);
    }

    /**
	 * @return <code>true</code> if the path point is a start point
	 */
    public boolean isStartPoint() {
        return isStartPoint;
    }

    /**
	 * @return <code>true</code> if the path point is an end point
	 */
    public boolean isEndPoint() {
        return isEndPoint;
    }

    /**
	 * Sets the slope of the path point, it is used to draw the end points with the path orientation.
	 * 
	 * @param slope the new slope
	 */
    public void setSlope(double slope) {
        this.slope = slope;
        repaint();
    }

    /**
	 * Sets/Unsets the point as the end point
	 * 
	 * @param isEndPoint <code>true</code> if the path point is an end point
	 */
    public void setEndPoint(boolean isEndPoint) {
        this.isEndPoint = isEndPoint;
        repaint();
    }

    /**
	 * Sets/Unsets the point as the start point
	 * 
	 * @param isEndPoint <code>true</code> if the path point is an start point
	 */
    public void setStartPoint(boolean isStartPoint) {
        this.isStartPoint = isStartPoint;
        repaint();
    }

    /**
	 * Sets the local parent of this figure
	 * 
	 * @param parent the new parent
	 */
    public void setLocalParent(IFigure parent) {
        this.parent = parent;
        updateName(name);
    }

    public void setBounds(Rectangle p) {
        super.setBounds(p);
        if (name.getText().length() > 0) {
            int midPoint = getLocation().x + getSize().width / 2 - (name.getTextBounds().width / 2);
            name.setBounds(new Rectangle(midPoint, getLocation().y - 15, name.getTextBounds().width, name.getTextBounds().height));
        }
    }

    /**
	 * Draw the path point
	 */
    public void outlineShape(Graphics graphics) {
        if (isStartPoint) {
            graphics.setBackgroundColor(getForegroundColor());
            graphics.fillOval(getLocation().x + 2, getLocation().y + 2, defaultsize.width - 4, defaultsize.height - 4);
        } else if (isEndPoint) {
            setSize(defaultsize);
            graphics.setLineWidth(3);
            float middleX = ((float) defaultsize.width) / 2;
            float middleY = ((float) defaultsize.height) / 2;
            Path drawPath = new Path(Display.getCurrent());
            drawPath.moveTo(getLocation().x + middleX, getLocation().y + middleY);
            drawPath.lineTo(getLocation().x, getLocation().y + (float) (-(slope * middleY) + middleY));
            drawPath.moveTo(getLocation().x + middleX, getLocation().y + middleY);
            drawPath.lineTo(getLocation().x + defaultsize.width, getLocation().y + (float) ((slope * middleY) + middleY));
            graphics.drawPath(drawPath);
        } else {
            Rectangle oval = new Rectangle(getLocation().x + 3, getLocation().y + 3, defaultsize.width / 2, defaultsize.height / 2);
            graphics.drawOval(oval);
        }
    }

    public void fillShape(Graphics graphics) {
    }

    /**
	 * Sets the name of the start point
	 * 
	 * @param name the new name
	 */
    public void setNamePoint(String name) {
        this.name.setText(name);
        updateName(this.name);
    }

    protected void updateName(Label label) {
        if (parent != null && label.getText().length() > 0) {
            if (!parent.getChildren().contains(label)) {
                parent.add(label);
                parent.repaint();
            }
        } else clear(label);
    }

    /**
	 * Removes the label from its parent
	 */
    public void clear(Label label) {
        if (parent != null && parent.getChildren().contains(label)) {
            parent.remove(label);
            parent.repaint();
        }
    }

    /**
	 * Removes the figure from its parent
	 */
    public void clear() {
        clear(name);
    }
}
