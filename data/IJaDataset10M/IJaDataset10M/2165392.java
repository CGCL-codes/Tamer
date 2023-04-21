package ao.cps511.a1.game.weapon.impl;

import ao.cps511.a1.game.Bullet;
import ao.cps511.a1.game.weapon.Weapon;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RandomFire implements Weapon {

    public List<Bullet> shoot(double x, double y, double xDirection, double yDirection, boolean shotByPlayer, Color colour) {
        List<Bullet> bullets = new ArrayList<Bullet>();
        bullets.add(new Bullet(x, y, xDirection, yDirection, shotByPlayer, colour.brighter().brighter().brighter()));
        for (int i = 0; i < 4; i++) {
            bullets.add(new Bullet(x + (Math.random() - 0.5) * 6, y, xDirection + (Math.random() - 0.5) * 2, Math.random() * yDirection * 1.5, shotByPlayer, colour.darker().darker().darker()));
        }
        return bullets;
    }
}
