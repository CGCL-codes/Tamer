package com.javaeedev.j2megame.tank;

import java.util.*;
import javax.microedition.lcdui.*;

public class BulletManager {

    private GameMap map;

    private Vector bullets = new Vector();

    public BulletManager(GameMap map) {
        this.map = map;
    }

    public void paint(Graphics g) {
        g.setColor(0xffffff);
        int n = 0;
        while (n < bullets.size()) {
            Bullet bullet = (Bullet) bullets.elementAt(n);
            if (bullet.move()) {
                bullets.removeElementAt(n);
                continue;
            }
            if (!map.canPass(bullet.direction, bullet.getX(), bullet.getY())) {
                bullets.removeElementAt(n);
                continue;
            }
            n++;
            g.fillRect(bullet.getX() - 1, bullet.getY() - 1, 1, 1);
        }
    }

    public void newBullet(Bullet bullet) {
        if (bullet != null) bullets.addElement(bullet);
    }
}
