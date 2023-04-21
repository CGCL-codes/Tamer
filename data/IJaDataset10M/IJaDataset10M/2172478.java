package net.borderwars.robots;

import net.borderwars.map.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * @author Eric
 *         Date: Feb 2, 2003
 *         Time: 9:19:17 PM
 */
public class Robot extends AbstractMovable implements Movable {

    private int direction = 0;

    private int speed;

    public int numMissles = 0;

    private int damage;

    private long damageDealt;

    public int getLastScan() {
        return lastScan;
    }

    private int lastScan = -1;

    public int getLastResoultion() {
        return lastResoultion;
    }

    private int lastResoultion = 0;

    public int sin(int degree) {
        return super.sin(degree);
    }

    public int cos(int degree) {
        return super.cos(degree);
    }

    public int tan(int degree) {
        return super.tan(degree);
    }

    public int rand(int limit) {
        return super.rand(limit);
    }

    public int sqrt(int number) {
        return super.sqrt(number);
    }

    public int atan(int ratio) {
        return super.atan(ratio);
    }

    public int scan(int degree, int resolution) {
        degree = degree % 360;
        lastScan = degree;
        lastResoultion = resolution;
        int closest = Integer.MAX_VALUE;
        Mappable closeRobot = null;
        int end = (degree + resolution) % 360;
        for (Mappable other : map.getMappables()) {
            if (other != this) {
                int mang = angleTo(other);
                for (int start = (degree - resolution) % 360; start < end; start++) {
                    if (start % 360 == mang) {
                        int dist = 0;
                        dist = calcDistance(this.loc_x(), this.loc_y(), other.loc_x(), other.loc_y());
                        if (dist < closest) {
                            closest = dist;
                            closeRobot = other;
                        }
                    }
                }
            }
        }
        if (closest != Integer.MAX_VALUE) {
            return (closest);
        }
        return (0);
    }

    int lastFire = 0;

    public boolean cannon(int degree, int range) {
        lastScan = -1;
        if (numMissles >= 2) {
            return (false);
        }
        numMissles++;
        lastFire = degree;
        return (true);
    }

    public void drive(int degree, int speed) {
        degree %= 360;
        lastScan = -1;
        this.speed = Math.min(100, speed);
        direction = degree;
    }

    public int damage() {
        return (damage);
    }

    public int speed() {
        return (speed);
    }

    public int direction() {
        return direction;
    }

    public void setX(int nx) {
        this.x = nx;
    }

    public void setY(int ny) {
        this.y = ny;
    }

    public void setSpeed(int i) {
        this.speed = i;
    }

    public Location getLocation() {
        return null;
    }

    public Velocity getVelocity() {
        return null;
    }

    public Location getDestination() {
        return null;
    }

    public int loc_x() {
        return (x);
    }

    public int loc_y() {
        return (y);
    }

    public void draw(int x, int y, Graphics g) {
    }

    public void draw(int x, int y, Graphics g, MapPane mp) {
        String name = g.getFont().getName();
        g.setFont(new Font(name, Font.BOLD, 20));
        g.setColor(Color.YELLOW);
        g.drawString("" + id, x - 10, y - 10);
        if (lastScan != -1) {
            g.setColor(Color.GREEN);
            int end = (lastScan + lastResoultion) % 360;
            int start = (lastScan - lastResoultion) % 360;
            int sx = (int) (Math.cos(Math.toRadians(start)) * 700);
            int sy = (int) (Math.sin(Math.toRadians(start)) * 700);
            int ex = (int) (Math.cos(Math.toRadians(end)) * 700);
            int ey = (int) (Math.sin(Math.toRadians(end)) * 700);
            sx += x;
            ex += x;
            sy += y;
            ey += y;
            sx = mp.transX(sx);
            ex = mp.transX(ex);
            sy = mp.transY(sy);
            ey = mp.transY(ey);
            prettyLine(g, x, y, sx, sy);
            prettyLine(g, x, y, ex, ey);
            prettyLine(g, sx, sy, ex, ey);
        }
    }

    private void prettyLine(Graphics g, int x, int y, int xx, int yy) {
        g.drawLine(x, y, xx, yy);
        g.drawLine(x, y, xx - 1, yy - 1);
        g.drawLine(x, y, xx + 1, yy + 1);
    }

    public void gotHit(int dmg) {
        damage += dmg;
    }

    LinkedList<String> problems = new LinkedList<String>();

    protected void reportProblem(String s) {
        problems.addLast(s);
    }

    public String toString() {
        return "Robot{" + "direction=" + direction + ", speed=" + speed + ", damage=" + damage + ", x=" + x + ", y=" + y + ", id=" + id + "}";
    }

    public void println(String s) {
        System.out.println(s);
    }

    public void printInt(int heading) {
        System.out.println(this.getVM().getName() + " " + heading);
    }

    public void incrementDamageDealt(long inc) {
        damageDealt += inc;
    }

    public long getDamageDealt() {
        return (damageDealt);
    }
}
