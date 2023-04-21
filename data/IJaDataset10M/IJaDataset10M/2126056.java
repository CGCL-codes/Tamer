package ports;

import javax.microedition.lcdui.Graphics;
import UIPackage.GlogColor;

/**
 * This is the status bar for all download/upload processes.
 * Usage:
 * 
 * StatusBar sb = new StatusBar(); //not painting
 * FileUploader fu = new FileUploader();
 * 
 * fu.uploadImage(..., sb); //and sb will be updated automatically
 * fu.uploadImage(..., null); //for ignoring the status bar.
 * 
 * now for painting the status bar, we need
 * sb.paint(graphics, 0, 0, 100, 10);
 * or for a form we can use
 * 
 * @author Ray
 *
 */
public class StatusBar {

    /**
	 * The maximum length...
	 */
    private int maxValue;

    /**
	 * The current size uploaded/downloaded
	 */
    private int currentValue;

    /**
	 * This is the label
	 */
    private String label;

    /**
	 * Constructor without canvas
	 */
    public StatusBar() {
        maxValue = 100;
        currentValue = 0;
        label = null;
    }

    /**
	 * Constructor with our own max or min
	 */
    public StatusBar(int max, int current) {
        maxValue = max;
        current = 0;
        label = null;
    }

    /**
	 * set the value
	 * @param amount
	 */
    public void setValue(int amount) {
        this.currentValue = amount;
    }

    /**
	 * Get the Value
	 * @return the current value
	 */
    public int getValue() {
        return this.currentValue;
    }

    /**
	 * Get the maximum value
	 * @return the Maximum value
	 */
    public int getMaximumValue() {
        return this.maxValue;
    }

    /**
	 * This will increment the bar by the amount
	 * @param amount
	 */
    public void add(int amount) {
        currentValue += amount;
    }

    /**
	 * Set the total
	 * @param total
	 */
    public void setMaximum(int max) {
        this.maxValue = max;
    }

    /**
	 * Reset the status bar.
	 */
    public void reset() {
        this.maxValue = 0;
        this.currentValue = 0;
    }

    /**
	 * The label for this status bar.
	 * @param label
	 */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
	 * This will paint the progress bar on given
	 * canvas
	 * (x1,y1) (upper left corner)
	 * (width, height) are the width and height of the status box
	 */
    public void drawBar(Graphics g, int x1, int y1, int width, int height) {
        g.setColor(GlogColor.BLACK);
        g.drawRect(x1, y1, width, height);
        g.setColor(GlogColor.WHITE);
        g.fillRect(x1, y1, width, height);
        g.setColor(GlogColor.SKYBLUE);
        int ratioWidth = (this.currentValue * width) / this.maxValue;
        g.fillRect(x1, y1, ratioWidth, height);
        g.setColor(GlogColor.BLACK);
        g.drawString((this.currentValue * 100) / this.maxValue + "%", (x1 + width) / 2, y1, Graphics.HCENTER | Graphics.TOP);
    }
}
