package de.javacus.grafmach.twoD.plain;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import de.javacus.grafmach.twoD.Drawable;
import de.javacus.grafmach.twoD.IBasicGO;
import de.javacus.grafmach.twoD.ICircle;
import de.javacus.grafmach.twoD.IPoint;
import de.javacus.grafmach.twoD.IRect;

/**
 * 
 * @author 2010 Burkhard Loesel, www.spirit-and-emotion.de
 */
public class Circle extends Ellipse2D.Double implements ICircle, Drawable {

    private static final long serialVersionUID = 5299993571947070745L;

    private String grafName = "?";

    private boolean grafNamesVisible = false;

    /** a default circle */
    public Circle() {
        super(100, 100, 100, 100);
    }

    public Circle(double xCenter, double yCenter, double diameter) {
        super(xCenter - diameter / 2.0, yCenter - diameter / 2.0, diameter, diameter);
    }

    public Circle(String grafName, IPoint pCenter, double diameter) {
        super((int) (pCenter.getX() - diameter / 2.0), (int) (pCenter.getY() - diameter / 2.0), diameter, diameter);
        this.grafName = grafName;
    }

    public String toString() {
        return getGrafName() + "[pMid= " + getPCenter() + ", diameter= " + getDiameter() + "]";
    }

    public void paint(Graphics2D g2D) {
        if (g2D != null) {
            if (grafNamesVisible) {
                Composite compositeOld = g2D.getComposite();
                g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2D.drawString(getGrafName(), (int) getBounds().getCenterX(), (int) getBounds().getCenterY());
                g2D.setComposite(compositeOld);
            }
            g2D.draw(circleMirrored);
        } else {
            System.err.println("g2D ist null.");
        }
    }

    public void setDiameter(double diameter) {
        setFrame(getX(), getY(), diameter, diameter);
    }

    public void moveX(double xShift) {
        setFrame(getX() + xShift, getY(), getWidth(), getHeight());
    }

    public void moveY(double yShift) {
        setFrame(getX(), getY() + yShift, getWidth(), getHeight());
    }

    public void scaleX(double factor) {
        setFrame(getX(), getY(), getWidth() * factor, getHeight());
    }

    public void scaleY(double factor) {
        setFrame(getX(), getY(), getWidth(), getHeight() * factor);
    }

    public double getDiameter() {
        return getWidth();
    }

    public IPoint getPCenter() {
        Point pCenter = new Point(getCenterX(), getCenterY());
        return pCenter;
    }

    public void turn(double alfaInDegrees) {
    }

    public boolean getGrafNamesVisible() {
        return grafNamesVisible;
    }

    public void setPCenter(IPoint center) {
        super.x = center.getX() - getDiameter() / 2.0;
        super.y = center.getY() - getDiameter() / 2.0;
    }

    public IRect getLimits() {
        return new Rect(getMinX(), getMinY(), getDiameter(), getDiameter());
    }

    public void setGrafName(String grafName) {
        this.grafName = grafName;
    }

    public String getGrafName() {
        return grafName;
    }

    public void setGrafNamesVisible(boolean grafNamesVisible) {
        this.grafNamesVisible = grafNamesVisible;
    }

    public Object newInstance() {
        return copyDeep(this);
    }

    public static ICircle copyDeep(ICircle circleOld) {
        Circle circleNew = new Circle();
        circleNew.setGrafName(circleOld.getGrafName());
        circleNew.setGrafNamesVisible(circleOld.getGrafNamesVisible());
        circleNew.setDiameter(circleOld.getDiameter());
        circleNew.setPCenter(circleOld.getPCenter());
        return circleNew;
    }

    public void copyDeep(IBasicGO iBasicGOSource, IBasicGO iBasicGODest) {
    }

    public void copyShallow(IBasicGO iBasicGOSource, IBasicGO iBasicGODest) {
    }

    /** because java2D has a mirrored y-axis, we need a mirrored copy of the circle
	 * 
	 */
    private Circle circleMirrored = null;

    /**
	 * mirrows in a way, that -y gets + y does not touch color etc.
	 * 
	 * the original circle is not touched
	 */
    private void mirrorY() {
        circleMirrored = new Circle();
        double yCenterNew = hWindow - getPCenter().getY();
        IPoint pCenterNew = new Point(getPCenter().getX(), yCenterNew);
        circleMirrored.setPCenter(pCenterNew);
    }

    private int hWindow = 0;

    @Override
    public void paint(Graphics2D g2D, int hWindow) {
        if (g2D != null) {
            if (this.hWindow != hWindow) {
                this.hWindow = hWindow;
                mirrorY();
            }
            paint(g2D);
        } else {
            System.err.println("g2D ist null.");
        }
    }

    @Override
    public IBasicGO copyDeepFrom(IBasicGO iBasicGOSource) {
        return null;
    }
}
