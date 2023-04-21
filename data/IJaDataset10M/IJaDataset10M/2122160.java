package de.CB_GL.Ui;

import android.graphics.Rect;

/**
 * Die Size Structur enth�lt die Member width und height
 * 
 * 
 * @author Longri
 *
 */
public class Size {

    public int width;

    public int height;

    /**
	 * Constructor
	 * @param width
	 * @param height
	 */
    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Rect getBounds() {
        return getBounds(0, 0);
    }

    public Rect getBounds(int x, int y) {
        return new Rect(x, y, width + x, height + y);
    }

    public Rect getBounds(int x, int y, int k, int l) {
        return new Rect(x, y, width + x + k, height + y + l);
    }
}
