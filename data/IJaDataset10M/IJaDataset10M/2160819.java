package mujmail.html.element;

import javax.microedition.lcdui.Graphics;
import mujmail.html.Browser;

/**
 * Represents &lt;hr&gt; tag - horizontal line.
 * 
 * @author Betlista
 */
public class HRElement extends AElement {

    /**
     * Constructor that simply calls super("hr");
     * 
     * @see AElement#AElement(String) ancestor constructor
     */
    public HRElement() {
        super("hr");
    }

    /**
     * Paints line.<br>
     * If position is not at the beginning of the screen (left border), than it
     * move position to next line and at the beginning of the line and prints
     * line.
     */
    public Point draw(Graphics g, int x, int y) {
        Point point = new Point(x, y + 1);
        if (x > 1) {
            point.y += g.getFont().getHeight();
            point.x = 1;
        }
        final Browser browser = Browser.getActualBrowser();
        final int width = browser.getWidth();
        g.drawLine(point.x, point.y, point.x + width - 2, point.y);
        point.y++;
        return point;
    }
}
