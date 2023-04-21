package org.wcb.gui.component.border;

import org.wcb.gui.util.UIHelper;
import javax.swing.border.AbstractBorder;
import java.awt.image.BufferedImage;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

/**
 * <small>
 * <p/>
 * Copyright (c)  2006  wbogaardt.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * <p/>
 * $File:  $ <br>
 * $Change:  $ submitted by $Author: wbogaardt $ at $DateTime: Sep 20, 2006 3:45:22 PM $ <br>
 * </small>
 *
 * @author wbogaardt
 *         This draws a border that makes a panel look like an open page of a book.
 */
public class BookBorder extends AbstractBorder {

    private BufferedImage topImg;

    private BufferedImage bottomImg;

    private BufferedImage rightImg;

    private BufferedImage topRightImg;

    private BufferedImage bottomRightImg;

    private BufferedImage leftSideImg;

    private BufferedImage topLeftImg;

    private BufferedImage bottomLeftImg;

    /**
     * Creates a booklike border.
     */
    public BookBorder() {
        bottomImg = (BufferedImage) UIHelper.loadImage("org/wcb/resources/gui/book/bottom.jpg");
        topImg = (BufferedImage) UIHelper.loadImage("org/wcb/resources/gui/book/top.jpg");
        rightImg = (BufferedImage) UIHelper.loadImage("org/wcb/resources/gui/book/rightside.jpg");
        topRightImg = (BufferedImage) UIHelper.loadImage("org/wcb/resources/gui/book/top-right-corner.jpg");
        bottomRightImg = (BufferedImage) UIHelper.loadImage("org/wcb/resources/gui/book/bottom-right-corner.jpg");
        leftSideImg = (BufferedImage) UIHelper.loadImage("org/wcb/resources/gui/book/rightpage-west.jpg");
        bottomLeftImg = (BufferedImage) UIHelper.loadImage("org/wcb/resources/gui/book/rightpage-southwest.jpg");
        topLeftImg = (BufferedImage) UIHelper.loadImage("org/wcb/resources/gui/book/rightpage-northwest.jpg");
    }

    /**
     * Overrides the component's paint method.
     * @param c the component.
     * @param g graphics object ref
     * @param x position
     * @param y position
     * @param width width of component
     * @param height height of component
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        int imgWidth;
        int imgHeight;
        imgHeight = rightImg.getHeight();
        imgWidth = rightImg.getWidth();
        for (int i = y; i < y + height; i += imgHeight) {
            g.drawImage(rightImg, x + width - imgWidth, i, null);
            g.drawImage(leftSideImg, 0, i, null);
        }
        for (int i = 0; i < width; i += imgWidth) {
            g.drawImage(topImg, i, 0, null);
        }
        imgHeight = bottomImg.getHeight();
        imgWidth = bottomImg.getWidth();
        for (int i = 0; i < width; i += imgWidth) {
            g.drawImage(bottomImg, i, height - imgHeight, null);
        }
        imgWidth = topRightImg.getWidth();
        g.drawImage(topRightImg, x + width - imgWidth, 0, null);
        imgHeight = bottomRightImg.getHeight();
        imgWidth = bottomRightImg.getWidth();
        g.drawImage(bottomRightImg, x + width - imgWidth, height - imgHeight, null);
        g.drawImage(topLeftImg, 0, 0, null);
        imgHeight = bottomLeftImg.getHeight();
        g.drawImage(bottomLeftImg, 0, height - imgHeight, null);
    }

    /**
     * Reference to the component insents.
     * @param c The component.
     * @return the insents of the image border.
     */
    public Insets getBorderInsets(Component c) {
        return new Insets(topImg.getHeight(), rightImg.getWidth(), bottomImg.getHeight(), rightImg.getWidth());
    }

    /**
     * Sets and lets the user know the border is opaque.
     * @return always return false
     */
    public boolean isBorderOpaque() {
        return false;
    }
}
