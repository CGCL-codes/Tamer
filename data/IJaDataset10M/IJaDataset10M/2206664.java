package zcatalog.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import zcatalog.Globals;
import zcatalog.db.ZCatObject;
import zcatalog.xml.jaxb.IconSize;

/**
 * Renderer for an item in the Explorer as a "Small Icon"
 * @author Alessandro Zigliani
 * @version 0.9
 * @since ZCatalog 0.9
 */
class SmallIconBox extends JLabel {

    private Color c = new Color(0xdc, 0xdc, 0xdc);

    private ZCatObject o;

    private boolean selected;

    public SmallIconBox(ZCatObject o, boolean selected) {
        super(o.getName() + "  ");
        this.o = o;
        this.selected = selected;
        this.setIcon(o.getIcon(IconSize._24));
    }

    @Override
    public void paint(Graphics g) {
        Dimension d = getSize();
        if (selected) {
            g.setColor(c);
            g.fillRect(0, 0, d.width, d.height);
        }
        super.paint(g);
    }

    public static Dimension getBoxSize() {
        int icon = Globals.PREFERENCES.getSmallIconSize().value();
        return new Dimension(-1, -1);
    }
}
