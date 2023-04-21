package tilemaster.editor.paintingtools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Hj. Malthaner
 */
public class FillRectTool extends PaintingToolBase {

    private int firstX, firstY;

    /**
     * @return The redraw mode to use for this tool.
     */
    public int getRefreshMode() {
        return REPAINT_REFRESH;
    }

    /**
     * @return The name to display for this tool.
     */
    public String getToolName() {
        return "Fill";
    }

    /**
     * Sets the drawing area for tools which need direct access
     *
     * @param image The current tile image, writeable
     */
    public void setCanvas(BufferedImage image) {
    }

    /**
     * User clicked a new location
     */
    public void firstClick(Graphics gr, int x, int y) {
        firstX = x;
        firstY = y;
        gr.fillRect(firstX, firstY, 1, 1);
    }

    /**
     * User drags mouse
     */
    public void paint(final Graphics gr, final int x, final int y, boolean filled) {
        final int l = Math.min(firstX, x);
        final int t = Math.min(firstY, y);
        final int w = Math.abs(x - firstX) + (filled ? 1 : 0);
        final int h = Math.abs(y - firstY) + (filled ? 1 : 0);
        if (filled) {
            gr.fillRect(l, t, w, h);
        } else {
            gr.drawRect(l, t, w, h);
        }
    }

    /**
     * This is called once the user clicked a color. The index of the
     * selected color is passed as parameter.
     * @param colorIndex The color's index in the color map.
     * @author Hj. Malthaner
     */
    public void onColorSelected(int colorIndex) {
    }

    /** Creates a new instance of FillRectTool */
    public FillRectTool() {
    }
}
