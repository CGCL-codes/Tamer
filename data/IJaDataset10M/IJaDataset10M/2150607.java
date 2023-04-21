package net.sourceforge.geeboss.view.widget.lcd;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * A 16x2 LCD display
 * @author <a href="mailto:fborry@free.fr">Frederic BORRY</a>
 */
public class Lcd16x2 extends LcdControl {

    /** The bg image */
    private static final Image LCD_16x2_BACKGROUND = new Image(Display.getCurrent(), Lcd16x2.class.getResourceAsStream("images/16x2.png"));

    /** The pixel color */
    private static final Color PIXEL_COLOR = new Color(Display.getCurrent(), 0, 192, 0);

    /** The pixel color */
    private static final Color SCREEN_COLOR = new Color(Display.getCurrent(), 0, 0, 0);

    /** The pixel size */
    private static final Point PIXEL_SIZE = new Point(2, 2);

    /** The number of columns */
    private static final int COLUMN_NUMBER = 16;

    /** The number of rows */
    private static final int ROW_NUMBER = 2;

    /**
     * Construct a new LcdControl given a background image
     * @param comp  the parent composite
     * @param flag  the style flag
     * @param background  the background image
     */
    public Lcd16x2(Composite comp, int flag, Image background) {
        super(Lcd5x7Font.getInstance(), comp, flag, background);
    }

    /**
	 * Returns this control's wdth
	 * @return this control's wdth
	 */
    public static int getWidth() {
        return computeWidth(COLUMN_NUMBER, Lcd5x7Font.getInstance().getCharacterWidth(), PIXEL_SIZE.x);
    }

    /**
	 * Returns this control's height
	 * @return this control's height
	 */
    public static int getHeight() {
        return computeHeight(ROW_NUMBER, Lcd5x7Font.getInstance().getCharacterHeight(), PIXEL_SIZE.y);
    }

    /**
     * Get the image to display as a background for the LCD
     * @return the lcd background image
     */
    protected Image getLcdBackgroundImage() {
        return LCD_16x2_BACKGROUND;
    }

    /**
     * Get the size of a pixel
     * @return the size of a pixel
     */
    protected Point getPixelSize() {
        return PIXEL_SIZE;
    }

    /**
     * Get the pixel color
     * @return the pixel color
     */
    protected Color getPixelColor() {
        return PIXEL_COLOR;
    }

    /**
     * Get the screen color
     * @return the screen color
     */
    protected Color getScreenColor() {
        return SCREEN_COLOR;
    }

    /**
     * Get the number of rows of the screen
     * @return the number of rows of the screen
     */
    protected int getRowNumber() {
        return 2;
    }

    /**
     * Get the number of columns of the screen
     * @return the number of columns of the screen
     */
    protected int getColumnNumber() {
        return 16;
    }
}
