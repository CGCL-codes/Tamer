package com.codename1.ui.html;

import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Painter;
import com.codename1.ui.geom.Rectangle;
import com.codename1.ui.plaf.Style;

/**
 * A painter that draws the background of a component based on its CSS style
 * Unlike the regular CodenameOne bg painter, this also adds the following:
 * - Painting both a background color and a background image
 * - Tiling horizontally or vertically (or both)
 * - Background image position offset horizontal and vertical
 *
 * @author Ofir Leitner
 */
class CSSBgPainter implements Painter {

    private Component parent, scrollableParent;

    int horizPos, vertPos;

    boolean horizIsPercentage, vertIsPercentage;

    boolean fixed;

    /**
     * Construct a background painter for the given component
     *
     * @param the parent component
     */
    CSSBgPainter(Component parent) {
        this.parent = parent;
    }

    /**
     * Sets the horizontal/vertical position of the background image
     *
     * @param property CSSElement.CSS_BACKGROUND_POSITION_X or Y for horizontal/vertical
     * @param pos The position as a CSS length value
     */
    void setPosition(int property, int pos) {
        boolean isPercentage = false;
        if ((pos & CSSElement.VAL_PERCENTAGE) != 0) {
            pos -= CSSElement.VAL_PERCENTAGE;
            isPercentage = true;
        }
        if (property == CSSElement.CSS_BACKGROUND_POSITION_X) {
            setHorizPosition(pos, isPercentage);
        } else {
            setVertPosition(pos, isPercentage);
        }
    }

    /**
     * Sets the horizontal position of the background image
     * 
     * @param pos The position either in pixels or percentage
     * @param isPercentage if true then the numeric value give at 'pos' is actually percentages
     */
    private void setHorizPosition(int pos, boolean isPercentage) {
        horizPos = pos;
        horizIsPercentage = isPercentage;
    }

    /**
     * Sets the vertical position of the background image
     *
     * @param pos The position either in pixels or percentage
     * @param isPercentage if true then the numeric value give at 'pos' is actually percentages
     */
    private void setVertPosition(int pos, boolean isPercentage) {
        vertPos = pos;
        vertIsPercentage = isPercentage;
    }

    /**
     * Sets this background to be fixed, which means it isn't affected by scrolling, but stays fixed in the back of the component it is applied on.
     */
    void setFixed() {
        fixed = true;
    }

    /**
     * Returns a parent container that is scrollable or null if no parent is
     * scrollable.
     * This was copied from Container.getScrollableParent (Which can't be used as it is private)
     *
     * @return a parent container that is scrollable or null if no parent is
     * scrollable.
     */
    private Container getScrollableParent(Component parent) {
        Container cont;
        if (parent instanceof Container) {
            cont = (Container) parent;
        } else {
            cont = parent.getParent();
        }
        while (cont != null) {
            if ((cont.isScrollableX()) || (cont.isScrollableY())) {
                return cont;
            }
            cont = cont.getParent();
        }
        return null;
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
        g.setColor(s.getBgColor());
        g.fillRect(x, y, width, height, s.getBgTransparency());
        Image bgImage = s.getBgImage();
        if (bgImage == null) {
            return;
        }
        if (fixed) {
            if (scrollableParent == null) {
                scrollableParent = getScrollableParent(parent);
            }
            if (scrollableParent != null) {
                x += scrollableParent.getScrollX();
                y += scrollableParent.getScrollY();
                width = scrollableParent.getWidth();
                height = scrollableParent.getHeight();
            }
        }
        int iW = bgImage.getWidth();
        int iH = bgImage.getHeight();
        int offsetX = horizPos;
        int offsetY = vertPos;
        if (horizIsPercentage) {
            offsetX = (width - iW) * offsetX / 100;
        }
        if (vertIsPercentage) {
            offsetY = (height - iH) * offsetY / 100;
        }
        switch(s.getBackgroundType()) {
            case 0:
                g.drawImage(s.getBgImage(), x + offsetX, y + offsetY);
                return;
            case Style.BACKGROUND_IMAGE_TILE_BOTH:
                for (int xPos = getTiledPosition(offsetX, iW); xPos <= width; xPos += iW) {
                    for (int yPos = getTiledPosition(offsetY, iH); yPos <= height; yPos += iH) {
                        g.drawImage(s.getBgImage(), x + xPos, y + yPos);
                    }
                }
                return;
            case Style.BACKGROUND_IMAGE_TILE_HORIZONTAL:
                for (int xPos = getTiledPosition(offsetX, iW); xPos <= width; xPos += iW) {
                    g.drawImage(s.getBgImage(), x + xPos, y + offsetY);
                }
                return;
            case Style.BACKGROUND_IMAGE_TILE_VERTICAL:
                for (int yPos = getTiledPosition(offsetY, iH); yPos <= height; yPos += iH) {
                    g.drawImage(s.getBgImage(), x + offsetX, y + yPos);
                }
                return;
        }
    }

    /**
     * Calculates the starting drawing position for a certain dimension (horizontal/vertical) considering the given offset
     * 
     * @param offset The offset in the dimension
     * @param imageDim The image dimension (width/height)
     * @return The starting drawing position - 0 is the offset is 0 or a negative value if there is an offset
     */
    private int getTiledPosition(int offset, int imageDim) {
        if (offset != 0) {
            return offset % imageDim - imageDim;
        }
        return 0;
    }
}
