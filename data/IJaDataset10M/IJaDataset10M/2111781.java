package net.sourceforge.jruntimedesigner.widgets.graphic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.SwingConstants;

/**
 * @author dleszyk
 */
public class RollerPainter {

    private int orientation;

    private int rollCount;

    private int hatSize = 7;

    private int rollSize = 5;

    private int cut = 4;

    public RollerPainter() {
        this.orientation = SwingConstants.VERTICAL;
        this.rollCount = 10;
    }

    /**
	 * Sets orientation of painting. Painting could be vertical or horizontal.
	 * Correct values are: <code>SwingConstants.VERTICAL</code> and
	 * <code>SwingConstants.HORIZONTAL</code>.
	 * 
	 * @param orientation
	 */
    public void setOrientation(int orientation) {
        if (orientation != SwingConstants.HORIZONTAL && orientation != SwingConstants.VERTICAL) {
            throw new AssertionError("Geometry should be one of: " + "VERTICAL or HORIZONTAL, not: '" + orientation + "'");
        }
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }

    /**
	 * Sets amount of rolling parts.
	 * 
	 * @param count
	 */
    public void setRollPartCount(int count) {
        this.rollCount = count;
    }

    public int getRollPartCount() {
        return rollCount;
    }

    /**
	 * Sets size (widht or height - it depends on orientation) of hat element of
	 * roller. Hat element is the part at the bottom and at the top (or on the
	 * left and on the right)
	 * 
	 * @param size
	 */
    public void setHatSize(int size) {
        this.hatSize = size;
    }

    public void paint(Graphics g, Rectangle bounds, Color bgColor) {
        Graphics gCopy = g.create();
        if (orientation == SwingConstants.VERTICAL) {
            paintVertically(gCopy, bounds, bgColor);
        } else {
            paintHorizontally(gCopy, bounds, bgColor);
        }
        gCopy.dispose();
    }

    private void paintVertically(Graphics g, Rectangle bounds, Color bgColor) {
        int rectHeight = bounds.height - 2 * (hatSize + rollSize) - 1;
        int width = bounds.width - 1;
        double rollDistance = ((double) (rectHeight - (rollCount * rollSize))) / (rollCount + 1);
        g.setColor(Color.BLACK);
        g.drawRect(cut, bounds.y, width - (2 * cut), hatSize);
        g.drawRect(0, bounds.y + hatSize, width, rollSize);
        g.drawRect(cut, bounds.y + hatSize + rollSize, width - (2 * cut), rectHeight);
        double begin = bounds.y + hatSize + rollSize + rollDistance;
        for (int i = 0; i < rollCount; i++) {
            g.setColor(Color.BLACK);
            g.drawRect(0, (int) begin, width, rollSize);
            g.setColor(bgColor);
            g.fillRect(1, 1 + (int) begin, width - 1, rollSize - 1);
            begin += rollDistance + rollSize;
        }
        g.setColor(Color.BLACK);
        g.drawRect(0, bounds.y + hatSize + rollSize + rectHeight, width, rollSize);
        g.drawRect(cut, bounds.y + hatSize + (2 * rollSize) + rectHeight, width - (2 * cut), hatSize);
    }

    private void paintHorizontally(Graphics g, Rectangle bounds, Color bgColor) {
        int rectWidth = bounds.width - 2 * (hatSize + rollSize) - 1;
        int height = bounds.height - 1;
        double rollDistance = ((double) (rectWidth - (rollCount * rollSize))) / (rollCount + 1);
        g.setColor(Color.BLACK);
        g.drawRect(bounds.x, cut, hatSize, height - (2 * cut));
        g.drawRect(bounds.x + hatSize, 0, rollSize, height);
        g.drawRect(bounds.x + rollSize + hatSize, cut, rectWidth, height - (2 * cut));
        double begin = bounds.x + rollSize + hatSize + rollDistance;
        for (int i = 0; i < rollCount; i++) {
            g.setColor(Color.BLACK);
            g.drawRect((int) begin, 0, rollSize, height);
            g.setColor(bgColor);
            g.fillRect((int) begin + 1, 1, rollSize - 1, height - 1);
            begin += rollDistance + rollSize;
        }
        g.setColor(Color.BLACK);
        g.drawRect(bounds.x + hatSize + rollSize + rectWidth, 0, rollSize, height);
        g.drawRect(bounds.x + hatSize + (2 * rollSize) + rectWidth, cut, hatSize, height - (2 * cut));
    }

    /**
	 * Returns height or width of the 'hat' part of the roller. If there is
	 * horizontal orientation set then width is returned; for vertical orientation
	 * - height is returned. Hat element is a part of the roller that is at the
	 * beginning of the roller (and also at the end) which resembles a little to a
	 * hat.
	 */
    public int getHatSize() {
        return hatSize;
    }

    public int getRollSize() {
        return rollSize;
    }

    /**
	 * Sets the roll size of the component.
	 * 
	 * @param rollSize
	 */
    public void setRollSize(int rollSize) {
        this.rollSize = rollSize;
    }

    /**
	 * Returns height or width of the 'slabs' part of the roller. If there is
	 * horizontal orientation set then width is returned; for vertical orientation
	 * - height is returned. 'Slabs' part is the part of the roller where slabs
	 * lie. It is computed dynamically on the basis of the given bounds. The same
	 * argument should be provided as for <code>paint</code> method.
	 */
    public int getSlabsPart(Rectangle bounds) {
        return (orientation == SwingConstants.VERTICAL) ? bounds.height - 2 * (hatSize + rollSize) - 1 : bounds.width - 2 * (hatSize + rollSize) - 1;
    }

    /**
	 * Sets the size (width or height) of the cut parameter. This parameter
	 * describes the distance between the roller element edge and non-roller
	 * element edge (i.e. the core part of roller).
	 * 
	 * @param size
	 */
    public void setCutSize(int size) {
        this.cut = size;
    }

    public int getCutSize() {
        return cut;
    }
}
