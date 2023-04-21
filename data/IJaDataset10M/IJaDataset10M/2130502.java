package ppsim.types;

import java.awt.*;
import java.awt.geom.*;
import ppsim.server.types.*;

/** This class implements the image of an process object.*/
public class ProcObjectImage implements IObjectImage {

    protected PPSimObject ppsimObject;

    /** The process image.*/
    protected ProcessImage procImage;

    protected Graphics2D g2;

    /** The color of the pen.*/
    protected static Color penColor = Color.black;

    /** This attribute denote if the object is selected or not.*/
    protected boolean selected = false;

    /** The solid stroke.*/
    protected static final BasicStroke solidStroke = new BasicStroke(1.0f);

    private static final float dash1[] = { 3.0f };

    /** The dashed stroke is used for painting selected objects.*/
    protected static final BasicStroke dashedStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash1, 0.0f);

    /** The font size.*/
    protected int sizeFont = 0;

    /** The object title.*/
    protected String objectTitle = "";

    /**
	 * 
	 */
    public ProcObjectImage() {
        sizeFont = 0;
    }

    /**
	 * @param ppsimObject
	 * @param procImage
	 */
    public ProcObjectImage(PPSimObject ppsimObject, ProcessImage procImage) {
        this.ppsimObject = ppsimObject;
        this.procImage = procImage;
    }

    /**
	 * @param procImage
	 */
    public ProcObjectImage(ProcessImage procImage) {
        ppsimObject = new PPSimObject(null, null, 0, 0, 0, 0);
        this.procImage = procImage;
    }

    public void draw() {
    }

    protected void initDrawContext() {
        g2 = procImage.getGraphics2D();
        if (selected) g2.setStroke(dashedStroke); else g2.setStroke(solidStroke);
        if (ppsimObject.getObjectState()) g2.setColor(Color.RED); else g2.setColor(Color.BLACK);
    }

    /** 
	 * @param point 
	 * @return <true> if if the point is included in the object's area.*/
    public boolean contains(Point2D.Double point) {
        return (point.x >= ppsimObject.getOrgX() - ppsimObject.getWidthX() / 2) && (point.y >= ppsimObject.getOrgY() - ppsimObject.getWidthY() / 2) && (point.x <= ppsimObject.getOrgY() + ppsimObject.getWidthX() / 2) && (point.y <= ppsimObject.getOrgY() + ppsimObject.getWidthY() / 2);
    }

    /** Set the 'selected' attribute.
	 * @param value */
    public void setSelected(boolean value) {
        selected = value;
    }

    /** Get the 'selected' attribute.
	 * @return <true> if this object is selected.*/
    public boolean isSelected() {
        return selected;
    }

    protected void drawTitle(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.PLAIN, (int) (sizeFont * 1.6)));
        Point2D.Double p = new Point2D.Double(0, 0);
        p.x = ppsimObject.getOrgX() + 70;
        p.y = ppsimObject.getOrgY();
        Point2D.Double pt = procImage.makeScreenCoords(p);
        g2.drawString(objectTitle, (float) pt.x, (float) pt.y);
        g2.setFont(new Font("Arial", Font.PLAIN, sizeFont));
    }

    /**
	 * @return Returns the objectTitle.
	 */
    public String getObjectTitle() {
        return objectTitle;
    }

    /**
	 * @param objectTitle The objectTitle to set.
	 */
    public void setObjectTitle(String objectTitle) {
        this.objectTitle = objectTitle;
    }

    /**
	 * @see ppsim.types.IObjectImage#getOrgX()
	 */
    public double getOrgX() {
        if (ppsimObject == null) return 0;
        return ppsimObject.getOrgX();
    }

    /**
	 * @see ppsim.types.IObjectImage#getOrgY()
	 */
    public double getOrgY() {
        if (ppsimObject == null) return 0;
        return ppsimObject.getOrgY();
    }

    /**
	 * @see ppsim.types.IObjectImage#getWidthX()
	 */
    public double getWidthX() {
        if (ppsimObject == null) return 0;
        return ppsimObject.getWidthX();
    }

    /**
	 * @see ppsim.types.IObjectImage#getWidthY()
	 */
    public double getWidthY() {
        if (ppsimObject == null) return 0;
        return ppsimObject.getWidthY();
    }
}
