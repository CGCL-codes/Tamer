package geocosm.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import uk.me.jstott.jcoord.LatLng;

public class HintFlag {

    protected boolean visible = true;

    protected Point position = new Point();

    protected LatLng coord = new LatLng(0, 0);

    protected String name = new String();

    protected Rectangle drawRect = null;

    public void paint(Graphics g) {
        g.setColor(Color.GREEN);
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        xPoints[0] = position.x;
        yPoints[0] = position.y;
        xPoints[1] = position.x - 10;
        yPoints[1] = position.y - 30;
        xPoints[2] = position.x + 10;
        yPoints[2] = position.y - 30;
        xPoints[3] = position.x;
        yPoints[3] = position.y;
        g.fillPolygon(xPoints, yPoints, 4);
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 12);
        g.setFont(font);
        int width = g.getFontMetrics().bytesWidth(name.getBytes(), 0, name.length()) + 30;
        drawRect = new Rectangle(position.x - 10, position.y - 50, width, 20);
        g.fillRect(position.x - 10, position.y - 50, width, 20);
        g.setColor(Color.BLACK);
        g.drawBytes(name.getBytes(), 0, name.length(), position.x, position.y - 35);
    }

    public boolean isInRect(Point p) {
        boolean ret = false;
        if (drawRect != null) {
            ret = drawRect.contains(p);
        }
        return ret;
    }

    /**
	 * @return the visible
	 */
    public boolean isVisible() {
        return visible;
    }

    /**
	 * @param visible the visible to set
	 */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
	 * @return the position
	 */
    public Point getPosition() {
        return position;
    }

    /**
	 * @param position the position to set
	 */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
	 * @return the coord
	 */
    public LatLng getCoord() {
        return coord;
    }

    /**
	 * @param coord the coord to set
	 */
    public void setCoord(LatLng coord) {
        this.coord = coord;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }
}
