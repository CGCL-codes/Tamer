package ao.cps511.a1.game.weapon.impl.ivan;

import ao.cps511.a1.game.Bullet;
import ao.cps511.a1.game.weapon.Weapon;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class InsaneGun implements Weapon {

    public List<Bullet> shoot(double x, double y, double xDirection, double yDirection, boolean shotByPlayer, Color colour) {
        List<Bullet> bullets = new ArrayList<Bullet>();
        bullets.add(new Bullet(x, y, xDirection, yDirection, shotByPlayer, colour));
        bullets.add(new Bullet(x + 5, y, xDirection + 0.5, yDirection, shotByPlayer, colour));
        bullets.add(new Bullet(x - 5, y, xDirection - 0.5, yDirection, shotByPlayer, new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256))));
        bullets.add(new Bullet(x + 5, y, xDirection - 0.5, yDirection, shotByPlayer, new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256))));
        bullets.add(new Bullet(x - 7, y, xDirection - 0.2, yDirection, shotByPlayer, new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256))));
        return bullets;
    }
}
