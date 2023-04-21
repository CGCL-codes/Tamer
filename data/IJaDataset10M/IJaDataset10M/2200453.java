package zcatalog.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import zcatalog.Globals;
import zcatalog.db.ZCatObject;
import zcatalog.xml.jaxb.IconSize;

/**
 * Renderer for an item in the Explorer as a "Big Icon"
 * @author Alessandro Zigliani
 * @version 0.9
 * @since ZCatalog 0.9
 */
class BigIconBox extends JPanel {

    public static final double boxWidth = 9, boxHeight = 3;

    protected static final Color c = new Color(0xdc, 0xdc, 0xdc);

    protected ZCatObject o;

    protected Image i;

    protected volatile boolean selected;

    protected BigIconBox(ZCatObject o, boolean selected, IconSize size) {
        this.o = o;
        this.selected = selected;
        i = o.getIcon(size).getImage();
        setToolTipText(o.getName());
    }

    ;

    public BigIconBox(ZCatObject o, boolean selected) {
        this(o, selected, Globals.PREFERENCES.getBigIconSize());
    }

    protected void loadImage() {
    }

    public final void paint(Graphics g) {
        Dimension d = getSize();
        if (selected) {
            g.setColor(c);
            g.fillRect(0, 0, d.width, d.height);
        }
        g.setColor(getForeground());
        drawText(g, d);
        drawIcon(g, d);
        super.paint(g);
    }

    protected void drawIcon(Graphics g, Dimension bounds) {
        int x = 4, y = (bounds.height - i.getHeight(null)) / 2;
        g.drawImage(i, x, y, null);
    }

    protected void drawText(Graphics g, Dimension bounds) {
        drawText(g, i.getWidth(null) + 8, 5, bounds.width - 15, bounds.height - 5);
    }

    protected final void drawText(Graphics g, int xStart, int yStart, int xEnd, int yEnd) {
        FontMetrics fm = g.getFontMetrics();
        int cHeight = fm.getHeight(), linePixLen = xEnd - xStart, lineMaxCount = (yEnd - yStart) / cHeight;
        String name = o.getName();
        char chars[] = name.toCharArray();
        int cStart = 0, cEnd = 0;
        String lines[] = new String[lineMaxCount];
        for (int line = 0; line < lineMaxCount; line++) {
            int pixLen = 0;
            while (true) {
                if (pixLen >= linePixLen) {
                    break;
                } else if (cEnd == chars.length - 1) {
                    cEnd++;
                    lineMaxCount = line + 1;
                    break;
                }
                pixLen += fm.charWidth(chars[++cEnd]);
            }
            lines[line] = name.substring(cStart, cEnd);
            cStart = cEnd;
        }
        if (cEnd < chars.length - 1) {
            String s = lines[lineMaxCount - 1];
            s = s.substring(0, s.length() - 4) + "...";
            lines[lineMaxCount - 1] = s;
        }
        yStart += (cHeight + yEnd - yStart - (lineMaxCount * cHeight)) / 2;
        for (int i = 0; i < lineMaxCount; i++) {
            g.drawString(lines[i], xStart, yStart);
            yStart += cHeight;
        }
    }

    public static Dimension getBoxSize() {
        int icon = Globals.PREFERENCES.getBigIconSize().value();
        return new Dimension(icon * 4, (int) (icon * 1.5));
    }
}
