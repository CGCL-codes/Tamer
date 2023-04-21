package projectiles;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;
import core.ShootingUnit;
import core.Vector;
import enemies.Enemy;

public class Nuke extends Projectile {

    private static final ImageIcon image = new ImageIcon(Nuke.class.getResource("../images/nuke.gif"));

    public static final int damage = 600;

    public static final int speed = 1125;

    private Enemy des;

    public Nuke(ShootingUnit origin, Point2D.Double position, int viewAngle, Enemy destn) {
        super(origin, position, viewAngle, new Dimension(Nuke.image.getIconWidth(), Nuke.image.getIconHeight()), Nuke.image, Nuke.damage, destn.getPosition(), Nuke.speed);
        this.des = destn;
    }

    public boolean update() {
        if (des.getHealth() > 0) {
            super.direction = new Vector(des.getPosition().x - super.position.x, des.getPosition().y - super.position.y).normalize();
            calcViewAngle();
        } else {
        }
        super.update();
        return true;
    }

    private void calcViewAngle() {
        Point2D.Double enemyPos = des.getPosition();
        double x = enemyPos.getX() - position.x;
        double y = enemyPos.getY() - position.y;
        if (x < 0) {
            x *= -1;
            if (y < 0) {
                y *= -1;
                super.viewAngle = (int) Math.toDegrees((Math.atan((y / x)))) + 90;
            } else if (y > 0) {
                super.viewAngle = (int) Math.toDegrees((Math.atan((x / y))));
            }
        } else if (x >= 0) {
            if (y < 0) {
                y *= -1;
                super.viewAngle = (int) Math.toDegrees((Math.atan((x / y)))) + 180;
            } else if (y > 0) {
                super.viewAngle = (int) Math.toDegrees((Math.atan((y / x)))) + 270;
            }
        }
    }

    public void setEnemy(Enemy e) {
        this.des = e;
    }

    public Enemy getEnemy() {
        return des;
    }

    public boolean calcEnemyForNuke(ArrayList<Enemy> enemies) {
        Collections.shuffle(enemies);
        for (Enemy x : enemies) {
            if (x.isOnRoad() && !x.isOutsideView() && x.getHealth() > 0) {
                setEnemy(x);
                return true;
            }
        }
        return false;
    }
}
