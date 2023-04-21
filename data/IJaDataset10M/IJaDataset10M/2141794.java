package edu.ou.spacewar.objects.shadows;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import edu.ou.mlfw.gui.Shadow2D;
import edu.ou.spacewar.objects.Bullet;
import edu.ou.utils.Vector2D;

/**
 * The default shadow class for a Bullet...
 */
public class BulletShadow extends Shadow2D {

    public static final Color BULLET_COLOR = Color.WHITE;

    final Bullet bullet;

    public BulletShadow(Bullet bullet) {
        super((int) (Bullet.BULLET_RADIUS * 2), (int) (Bullet.BULLET_RADIUS * 2));
        this.bullet = bullet;
    }

    public Vector2D getRealPosition() {
        return bullet.getPosition();
    }

    public boolean drawMe() {
        return bullet.isAlive();
    }

    public void draw(Graphics2D g) {
        float radius = bullet.getRadius();
        float diameter = radius * 2;
        g.setColor(BULLET_COLOR);
        g.fill(new Ellipse2D.Float(drawposition.getX() - radius, drawposition.getY() - radius, diameter, diameter));
    }

    public void cleanUp() {
    }
}
