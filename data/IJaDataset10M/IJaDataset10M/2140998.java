package mou.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * @author pb
 */
public class StringDrawer {

    private Graphics graphics;

    private int currentX;

    private int currentY;

    /**
	 * 
	 */
    public StringDrawer() {
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
        currentX = 0;
        currentY = 0;
    }

    /**
	 * Zeichnet den Text als die Fortsetzung der angefangener Textzeile
	 * 
	 * @param text
	 * @param foreground
	 * @param background
	 *            wenn nicht null dann wird der Text in einem mit dieser Farbe gef�lltem Rechteck
	 *            gezeichnet
	 * @return gezeichnete Area
	 */
    public Rectangle drawString(String text, Color foreground, Color background) {
        int textWidth = graphics.getFontMetrics().stringWidth(text);
        if (background != null) {
            graphics.setColor(background);
            graphics.fillRect(currentX, currentY, textWidth + 2, computeLineHeight());
        }
        graphics.setColor(foreground);
        graphics.drawString(text, currentX + 1, computeTextBaselineY());
        Rectangle rect = new Rectangle(currentX, currentY, textWidth + 2, computeLineHeight());
        currentX += textWidth + 2;
        return rect;
    }

    /**
	 * F�gt einen Freiraum zwischen zwei Textteilen einer Zeile
	 * 
	 * @param width
	 *            Gro�e in Pixel
	 */
    public void insertStrut(int width) {
        currentX += width;
    }

    private int computeTextBaselineY() {
        return currentY + graphics.getFontMetrics().getAscent();
    }

    private int computeLineHeight() {
        return graphics.getFontMetrics().getAscent() + graphics.getFontMetrics().getDescent();
    }

    /**
	 * Erzwing eine neue Textzeile
	 */
    public void beginNewLine() {
        currentY += computeLineHeight();
        currentX = 0;
    }
}
