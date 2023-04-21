package view;

import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import model.AlignObject;

/**
 * Defines the view properties for the alignment.
 * @author Thilo Muth
 *
 */
public class AlignViewProps {

    /**
	 * Alignment object
	 */
    private AlignObject alObj;

    /**
	 * x start position
	 */
    private int x_start;

    /**
	 * y end position
	 */
    private int x_end;

    /**
	 * y start position
	 */
    private int y_start;

    /**
	 * y end position
	 */
    private int y_end;

    /**
	 * Font object
	 */
    private Font font;

    /**
	 * Width of a character
	 */
    private int charWidth;

    /**
	 * Height of a character
	 */
    private int charHeight;

    /**
	 * Constructor for the alignment view properties
	 * @param alObj
	 */
    public AlignViewProps(AlignObject alObj) {
        this.alObj = alObj;
        init();
    }

    /**
	 * Initializes the alignment view propierties.
	 */
    private void init() {
        this.x_start = 0;
        this.x_end = alObj.getWidth();
        this.y_start = 0;
        this.y_end = alObj.getHeight();
        setFont(new Font("SansSerif", 1, 12));
    }

    /**
	 * Returns the alignment object
	 * @return alObj AlignObject
	 */
    public AlignObject getAlObj() {
        return alObj;
    }

    /**
	 * Sets the alignment object
	 * @param alObj
	 */
    public void setAlObj(AlignObject alObj) {
        this.alObj = alObj;
    }

    /**
	 * Returns the x start position.
	 * @return x_start
	 */
    public int getXStart() {
        return x_start;
    }

    /**
	 * Sets the x start position.
	 * @param x_start
	 */
    public void setXStart(int x_start) {
        this.x_start = x_start;
    }

    /**
	 * Returns the x end position.
	 * @return x_end
	 */
    public int getXEnd() {
        return x_end;
    }

    /**
	 * Sets the x end position.
	 * @param x_end
	 */
    public void setXEnd(int x_end) {
        this.x_end = x_end;
    }

    /**
	 * Returns the y start position.
	 * @return y_start
	 */
    public int getYStart() {
        return y_start;
    }

    /**
	 * Sets the y start position.
	 * @param y_start
	 */
    public void setYStart(int y_start) {
        this.y_start = y_start;
    }

    /**
	 * Returns the y end position.
	 * @return y_end
	 */
    public int getYEnd() {
        return y_end;
    }

    /**
	 * Sets the y end position.
	 * @param y_end
	 */
    public void setYEnd(int y_end) {
        this.y_end = y_end;
    }

    /**
	 * Returns the font object
	 * @return font Font
	 */
    public Font getFont() {
        return this.font;
    }

    /**
	 * Sets the char width.
	 * @param width
	 */
    public void setCharWidth(int width) {
        this.charWidth = width;
    }

    /**
	 * Returns the char width.
	 * @return charWidth
	 */
    public int getCharWidth() {
        return charWidth;
    }

    /**
	 * Sets the char height.
	 * @param height
	 */
    public void setCharHeight(int height) {
        this.charHeight = height;
    }

    /**
	 * Returns the char height.
	 * @return charHeight
	 */
    public int getCharHeight() {
        return charHeight;
    }

    /**
     * Sets the font object and does some font metrics stuff.
     * @param font
     */
    public void setFont(Font font) {
        this.font = font;
        Container container = new Container();
        FontMetrics fontMetrics = container.getFontMetrics(font);
        setCharHeight(fontMetrics.getHeight());
        setCharWidth(fontMetrics.charWidth('M'));
    }
}
