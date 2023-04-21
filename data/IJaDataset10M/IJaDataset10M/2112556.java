package mujmail.html.element;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * Represents &lt;b&gt; tag.
 * 
 * @author Betlista
 */
public class BElement extends AElement {

    public BElement() {
        super("b");
    }

    public Point draw(Graphics g, int x, int y) {
        Font f = g.getFont();
        Font newFont;
        if (type == START) {
            newFont = Font.getFont(f.getFace(), f.getStyle() | Font.STYLE_BOLD, f.getSize());
        } else {
            newFont = Font.getFont(f.getFace(), f.getStyle() ^ Font.STYLE_BOLD, f.getSize());
        }
        g.setFont(newFont);
        return new Point(x, y);
    }
}
