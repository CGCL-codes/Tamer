package cn.myapps.ui.painter;

import javax.microedition.lcdui.Graphics;
import cn.myapps.ui.Component;
import cn.myapps.ui.Painter;
import cn.myapps.ui.geom.Rectangle;

/**
 * A painter that draws the background of a component based on its style
 *
 * @author Chris
 */
public class BackgroundPainter implements Painter {

    /**
     * Construct a background painter for the given component
     * 
     * @param the parent component
     */
    public BackgroundPainter(Component parent) {
    }

    /**
     * @inheritDoc
     */
    public void paint(Graphics g, Rectangle rect) {
        int width = rect.getSize().getWidth();
        int height = rect.getSize().getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
    }
}
