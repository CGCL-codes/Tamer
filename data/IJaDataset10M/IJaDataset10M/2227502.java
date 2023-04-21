package zombiedefense.ui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;

/**
 * This class defines a framework for a ui element that
 * displays text output.
 * @author &{user}
 */
public abstract class TextOutput {

    protected Point location;

    protected Font font;

    protected int maxlines;

    protected int lineHeight;

    protected LinkedList<String> lines;

    public TextOutput(int x, int y, Font font, int lineHeight, int maxlines) {
        location = new Point(x, y);
        this.font = font;
        this.maxlines = maxlines;
        this.lineHeight = lineHeight;
        lines = new LinkedList<String>();
    }

    /**
     * Prints a line of text to the text output.
     * @param text 
     */
    public void print(String text) {
        if (lines.size() >= maxlines) {
            lines.removeFirst();
        }
        lines.addLast(text);
    }

    /**
     * Update time-based components of the text output.
     * @param elapsedTime 
     */
    public abstract void update(long elapsedTime);

    /**
     * Draws the text to the screen.
     * @param g 
     */
    public void draw(Graphics2D g) {
        g.setFont(font);
        for (int i = 1; i <= lines.size(); i++) {
            g.drawString(lines.get(i - 1), location.x, location.y + lineHeight * i);
        }
    }
}
