package net.sf.fkk.gui.brett;

import net.sf.fkk.gui.MediaManager;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import net.sf.fkk.global.Programmverwaltung;

class FigurZeichner {

    FigurZeichner(MediaManager mema) {
        Images = new BufferedImage[4];
        heights = new int[4];
        widths = new int[4];
        media = mema;
        Images[1] = media.getImage("figur_feuer.gif");
        Images[2] = media.getImage("figur_wasser.gif");
        Images[3] = media.getImage("figur_knueppel.gif");
        for (int i = 1; i < 4; ++i) {
            heights[i] = Images[i].getHeight();
            widths[i] = Images[i].getWidth();
        }
    }

    void zeichneFigur(Graphics2D gfx, Color col, int typ, int x, int y) {
        if ((typ < 1) || (typ > 3)) return;
        gfx.setColor(col);
        gfx.fillOval(x - 14, y - 14, 29, 29);
        gfx.setColor(Color.black);
        gfx.drawOval(x - 14, y - 14, 29, 29);
        gfx.setColor(Color.black);
        gfx.drawImage(Images[typ], null, x - widths[typ] / 2, y - heights[typ] / 2);
    }

    MediaManager media;

    BufferedImage[] Images;

    int[] heights;

    int[] widths;
}
