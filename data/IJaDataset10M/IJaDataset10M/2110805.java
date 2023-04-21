package com.substanceofcode.twitter.views;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Tommi Laukkanen
 */
public interface TalkBalloon {

    public int draw(Graphics g, String text, String talkerText, int y);

    public int draw(Graphics g, String text, String talkerText, int y, boolean isSelected);

    /**
     * Draw talk balloon in given coordinates and in given size.
     * @param g             Graphics.
     * @param text          Text inside balloon.
     * @param talkerText    Text below balloon.
     * @param y             Y coordinate of balloon.
     */
    public int draw(Graphics g, String[] textLines, String talkerText, int y, boolean isSelected);

    public Font getFont();

    public void setSize(int width, int height);
}
