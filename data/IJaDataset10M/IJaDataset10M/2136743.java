package org.dag.dmj;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

public class InvisibleWall extends Wall {

    public InvisibleWall() {
        super();
        mapchar = 'i';
        canPassMons = false;
        canPassImmaterial = true;
    }

    public void drawPic(int row, int col, int xc, int yc, Graphics2D g, ImageObserver obs) {
        if (DMJava.dispell > 0) {
            if (col == 3) xc = xc - wallpic[row][3].getWidth(null); else if (hasMons && col == 2 && row == 1 && DMJava.magicvision > 0) {
                drawContents(g, 3 - row, col - 1);
            }
            if (DMJava.mirrorback) DMJava.dview.offg2.drawImage(wallpic[row][col], xc, yc, obs); else DMJava.dview.offg2.drawImage(altpic[row][col], xc, yc, obs);
        } else if (hasMons && row > 0 && col > 0 && col < 4) {
            drawContents(g, 3 - row, col - 1);
        }
    }
}
