package mapwiz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public abstract class Showable {

    /**
	 * String for the href section in area tag 
	 */
    protected String href;

    /**
	 * String for the alt section in area tag 
	 */
    protected String alt;

    /**
	 * String for the title section in area tag 
	 */
    protected String title;

    /**
	 * Width of a corner of a showable in pixel 
	 */
    protected int width = 6;

    protected Color colorCorner = Color.green;

    protected Color colorLine = Color.green;

    protected Color colorSelected = Color.blue;

    public abstract void paint(Graphics g);

    public abstract String toHTMLString();

    public abstract String getShape();

    public abstract String getCoordinateString();

    public abstract boolean contains(Point p);

    public abstract void select(Point p);

    public abstract void updateCorner(Point p);

    public abstract void deselect();

    public Color getColorCorner() {
        return colorCorner;
    }

    public void setColorCorner(Color colorCorner) {
        this.colorCorner = colorCorner;
    }

    public Color getColorLine() {
        return colorLine;
    }

    public void setColorLine(Color colorLine) {
        this.colorLine = colorLine;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean cornerContains(Point corner, Point p) {
        if (p.x > corner.x - width / 2 && p.x < corner.x + width / 2 && p.y > corner.y - width / 2 && p.y < corner.y + width / 2) {
            return true;
        } else return false;
    }

    public String getAlt() {
        if (alt != null) {
            return alt;
        } else {
            return "";
        }
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getHref() {
        if (href != null) {
            return href;
        } else {
            return "";
        }
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTitle() {
        if (title != null) {
            return title;
        } else {
            return "";
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
