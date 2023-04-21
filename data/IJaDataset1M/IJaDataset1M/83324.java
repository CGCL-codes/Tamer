package net.sf.wubiq.wrappers;

import java.awt.geom.Rectangle2D;
import java.awt.print.Paper;
import java.io.Serializable;

/**
 * Wraps a paper object.
 * @author Federico Alcantara
 *
 */
public class PaperWrapper extends Paper implements Serializable {

    private static final long serialVersionUID = 1L;

    private double width;

    private double height;

    private Rectangle2D.Double imageableArea;

    public PaperWrapper() {
    }

    public PaperWrapper(Paper paper) {
        width = paper.getWidth();
        height = paper.getHeight();
        imageableArea = new Rectangle2D.Double(paper.getImageableX(), paper.getImageableY(), paper.getImageableWidth(), paper.getImageableHeight());
    }

    /**
	 * @return the width
	 */
    @Override
    public double getWidth() {
        return width;
    }

    /**
	 * @return the height
	 */
    @Override
    public double getHeight() {
        return height;
    }

    /**
	 * @return the imageableX
	 */
    @Override
    public double getImageableX() {
        return imageableArea.x;
    }

    /**
	 * @return the imageableY
	 */
    @Override
    public double getImageableY() {
        return imageableArea.y;
    }

    /**
	 * @return the imageableWidth
	 */
    @Override
    public double getImageableWidth() {
        return imageableArea.width;
    }

    /**
	 * @return the imageableHeight
	 */
    @Override
    public double getImageableHeight() {
        return imageableArea.height;
    }

    @Override
    public void setImageableArea(double x, double y, double width, double height) {
        imageableArea = new Rectangle2D.Double(x, y, width, height);
    }

    @Override
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Object clone() {
        PaperWrapper returnValue = new PaperWrapper();
        returnValue.setImageableArea(imageableArea.x, imageableArea.y, imageableArea.width, imageableArea.height);
        returnValue.setSize(width, height);
        return returnValue;
    }

    @Override
    public String toString() {
        return "PaperWrapper [width=" + width + ", height=" + height + ", imageableArea=" + imageableArea + "]";
    }
}
