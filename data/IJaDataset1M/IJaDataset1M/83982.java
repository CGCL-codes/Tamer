package bee.gui;

import bee.core.Log;
import bee.core.Vec2dInt;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author boto
 */
public class TextEdit extends Element {

    private List<Character> str = new ArrayList<Character>();

    private int cursorPos = 0;

    private int maxLen = 16;

    private boolean drawBorder = true;

    private boolean drawBkg = true;

    private Color colBkg = Color.darkGray;

    private Color colBorder = Color.yellow;

    /**
     * Create an edit text.
     */
    public TextEdit() {
        font = GUI.get().getFont(fontStyle, fontSize);
    }

    /**
     * Create an edit text with given initial text.
     */
    public TextEdit(String text) {
        this();
        setSize(new Vec2dInt(50, 16));
        setText(text);
    }

    /**
     * Set the font size.
     */
    @Override
    public void setFontSize(int size) {
        super.setFontSize(size);
        font = GUI.get().getFont(fontStyle, size);
    }

    /**
     * Set the font style (one of java.awt.Font styles).
     */
    @Override
    public void setFontStyle(int style) {
        super.setFontStyle(style);
        font = GUI.get().getFont(style, fontSize);
    }

    /**
     * Set element text.
     */
    @Override
    public void setText(String text) {
        if ((maxLen >= 0) && (text.length() > maxLen)) {
            text = text.substring(0, maxLen);
        }
        str.clear();
        for (int cnt = 0; cnt < text.length(); cnt++) str.add(text.charAt(cnt));
        cursorPos = str.size();
        updateText(str);
    }

    /**
     * Get element text.
     */
    @Override
    public String getText() {
        String t = new String();
        for (int cnt = 0; cnt < str.size(); cnt++) t += str.get(cnt);
        return t;
    }

    /**
     * Set the maximal text length. Set -1 for no limit.
     */
    public void setMaxTextLength(int len) {
        maxLen = len;
        if ((maxLen >= 0) && (str.size() > maxLen)) {
            int diff = str.size() - maxLen;
            for (int cnt = 0; cnt < diff; cnt++) str.remove(str.size() - 1);
        }
    }

    /**
     * Get the maximal text length.
     */
    public int getMaxTextLength() {
        return maxLen;
    }

    /**
     * Set/unset the element focus.
     */
    @Override
    public void setFocused(boolean focus) {
        hasFocus = focus;
    }

    /**
     * Enable/disable border drawing.
     */
    public void enableBorder(boolean en) {
        drawBorder = en;
    }

    /**
     * Is border drawing enabled?
     */
    public boolean isEnableBorder() {
        return drawBorder;
    }

    /**
     * Enable background drawing.
     */
    public void enableBackground(boolean en) {
        drawBkg = en;
    }

    /**
     * Is background drawing enabled?
     */
    public boolean isEnableBackground() {
        return drawBkg;
    }

    /**
     * Set the sheet background color.
     */
    public void setBackgroundColor(Color col) {
        colBkg = col;
    }

    /**
     * Get the sheet background color.
     */
    public Color getBackgroundColor() {
        return colBkg;
    }

    /**
     * Set the sheet border color.
     */
    public void setBorderColor(Color col) {
        colBorder = col;
    }

    /**
     * Get the sheet border color.
     */
    public Color getBorderColor() {
        return colBorder;
    }

    /**
     * This class does not provide addChild functionality.
     */
    @Override
    public void addChild(Element child) {
        Log.error(getClass().getSimpleName() + ": element does not provide addChild functionality");
    }

    /**
     * This class does not provide removeChild functionality.
     */
    @Override
    public boolean removeChild(Element child) {
        Log.error(getClass().getSimpleName() + ": element does not provide removeChild functionality");
        return false;
    }

    /**
     * Update the text string for drawing.
     */
    private void updateText(List<Character> s) {
        text = "";
        int len = s.size();
        for (int cnt = 0; cnt < s.size(); cnt++) {
            if (cnt == cursorPos) text += "|";
            text += s.get(cnt);
        }
        if (cursorPos == len) text += "|";
    }

    /**
     * Handle key events such as press, release, etc.
     */
    @Override
    protected void handleKeyEvent(int eventtype, char character, int code) {
        if (!isEnabled() || !isFocused()) return;
        if (eventtype == Element.KEY_EVT_PRESSED) {
            switch(code) {
                case java.awt.event.KeyEvent.VK_LEFT:
                    if (cursorPos > 0) {
                        cursorPos--;
                        updateText(str);
                    }
                    break;
                case java.awt.event.KeyEvent.VK_RIGHT:
                    if (cursorPos < text.length() - 1) {
                        cursorPos++;
                        updateText(str);
                    }
                    break;
            }
        } else if (eventtype == Element.KEY_EVT_TYPED) {
            switch(character) {
                case java.awt.event.KeyEvent.VK_DELETE:
                    if (text.length() > (cursorPos + 1)) {
                        str.remove(cursorPos);
                        updateText(str);
                    }
                    break;
                case java.awt.event.KeyEvent.VK_BACK_SPACE:
                    if ((text.length() > 0) && (cursorPos > 0)) {
                        str.remove(cursorPos - 1);
                        cursorPos--;
                        updateText(str);
                    }
                    break;
                default:
                    if ((maxLen < 0) || (str.size() < maxLen)) {
                        str.add(cursorPos, character);
                        cursorPos++;
                        updateText(str);
                    }
            }
        }
    }

    /**
     * Draw
     */
    @Override
    protected void draw(Graphics g, Vec2dInt pos) {
        if (!isVisible()) return;
        Vec2dInt fieldpos = pos.add(position);
        if (drawBkg) {
            if ((size.x != 0) && (size.y != 0)) {
                g.setColor(colBkg);
                g.fillRoundRect(fieldpos.x, fieldpos.y, size.x, size.y, 5, 5);
            }
        }
        if (drawBorder) {
            if ((size.x != 0) && (size.y != 0)) {
                if (isFocused()) g.setColor(Color.darkGray); else g.setColor(colBorder);
                g.drawRoundRect(fieldpos.x, fieldpos.y, size.x, size.y, 5, 5);
            }
        }
        if (text.length() > 0) {
            Graphics2D g2 = (Graphics2D) g;
            FontRenderContext frc = g2.getFontRenderContext();
            TextLayout tl = new TextLayout(text, font, frc);
            Rectangle2D bbox = tl.getBounds();
            int height = (int) bbox.getHeight();
            g2.setColor(colText);
            tl.draw(g2, fieldpos.x + 2, fieldpos.y + size.y - height / 2);
        }
    }
}
