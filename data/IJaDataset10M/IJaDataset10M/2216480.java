package com.substanceofcode.twitter.views;

import com.substanceofcode.utils.StringUtil;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Tommi Laukkanen
 */
public class ListTalkBalloon implements TalkBalloon {

    public static final Font textFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);

    public static final Font nameFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);

    public int screenWidth;

    public int screenHeight;

    private static final int BORDER_COLOR = 0x888888;

    private static final int BORDER_SHADOW_COLOR = 0xBBBBBB;

    private int fontHeight;

    private int textWidth;

    /** Create new instanc of TalkBalloon. */
    public ListTalkBalloon(int screenWidth, int screenHeight) {
        this.fontHeight = textFont.getHeight();
        setSize(screenWidth, screenHeight);
    }

    public Font getFont() {
        return textFont;
    }

    public int draw(Graphics g, String text, String talkerText, int y) {
        String[] originalText = { text };
        String[] textLines = StringUtil.formatMessage(originalText, screenWidth - textFont.getHeight(), textFont);
        return draw(g, textLines, talkerText, y, false);
    }

    public int draw(Graphics g, String text, String talkerText, int y, boolean isSelected) {
        String[] originalText = { text };
        String[] textLines = StringUtil.formatMessage(originalText, screenWidth - textFont.getHeight(), textFont);
        return draw(g, textLines, talkerText, y, isSelected);
    }

    /**
     * Draw talk balloon in given coordinates and in given size.
     * @param g             Graphics.
     * @param text          Text inside balloon.
     * @param talkerText    Text below balloon.
     * @param y             Y coordinate of balloon.
     */
    public int draw(Graphics g, String[] textLines, String talkerText, int y, boolean isSelected) {
        int textHeight = (textLines.length) * fontHeight + fontHeight;
        if (!isSelected) {
            g.setColor(0xffffff);
        } else {
            g.setColor(Theme.COLOR_SELECTED_BG);
        }
        int x = 0;
        g.fillRect(x, y, this.screenWidth, (textLines.length) * fontHeight + fontHeight * 2 + 1);
        g.setColor(BORDER_COLOR);
        g.drawLine(x, y, screenWidth, y);
        g.setColor(BORDER_SHADOW_COLOR);
        g.drawLine(x, y + 1, screenWidth, y + 1);
        g.setColor(Theme.COLOR_TEXT);
        g.setFont(textFont);
        int textRow = y + fontHeight + fontHeight / 2;
        for (int line = 0; line < textLines.length; line++) {
            g.drawString(textLines[line], x + fontHeight / 2, textRow, Graphics.LEFT | Graphics.BOTTOM);
            textRow += fontHeight;
        }
        g.setColor(Theme.COLOR_USER_TEXT);
        g.setFont(nameFont);
        int nameWidth = nameFont.stringWidth(talkerText);
        g.drawString(talkerText, screenWidth - fontHeight / 2 - nameWidth, textRow, Graphics.LEFT | Graphics.BOTTOM);
        return (int) ((textLines.length) * fontHeight + fontHeight * 2);
    }

    public void setSize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.textWidth = screenWidth - fontHeight * 3;
    }
}
