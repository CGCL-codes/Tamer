package com.codename1.ui.painter;

import com.codename1.ui.Component;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Painter;
import com.codename1.ui.geom.Rectangle;
import com.codename1.ui.plaf.Style;

/**
 * A painter that draws the background of a component based on its style
 *
 * @author Shai Almog
 */
public class BackgroundPainter implements Painter {

    private Component parent;

    /**
     * Construct a background painter for the given component
     * 
     * @param parent the parent component
     */
    public BackgroundPainter(Component parent) {
        this.parent = parent;
    }

    /**
     * @inheritDoc
     */
    public void paint(Graphics g, Rectangle rect) {
        Style s = parent.getStyle();
        int x = rect.getX();
        int y = rect.getY();
        int width = rect.getSize().getWidth();
        int height = rect.getSize().getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        Image bgImage = s.getBgImage();
        if (bgImage == null) {
            g.setColor(s.getBgColor());
            g.fillRect(x, y, width, height, s.getBgTransparency());
        } else {
            if (s.getBackgroundType() == Style.BACKGROUND_IMAGE_SCALED) {
                if (bgImage.getWidth() != width || bgImage.getHeight() != height) {
                    bgImage = bgImage.scaled(width, height);
                    s.setBgImage(bgImage, true);
                }
            } else {
                int iW = bgImage.getWidth();
                int iH = bgImage.getHeight();
                for (int xPos = 0; xPos < width; xPos += iW) {
                    for (int yPos = 0; yPos < height; yPos += iH) {
                        g.drawImage(s.getBgImage(), x + xPos, y + yPos);
                    }
                }
                return;
            }
            g.drawImage(s.getBgImage(), x, y);
        }
    }
}
