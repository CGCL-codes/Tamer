package Drawing;

import Game.Tank;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

/**
 *
 * Compiler: jdk1.6.0_14 <br />
 * IDE: NetBeans 6.7.1 <br />
 * @author Andrew Smith <br />
 */
public class TankImage {

    /** the tank object that this image draws */
    private Tank tank;

    public TankImage(Tank tank) {
        this.tank = tank;
    }

    public int[] getPointsX() {
        return getTankShape().xpoints;
    }

    public int[] getPointsY() {
        return getTankShape().ypoints;
    }

    /**
     * Gets the shape (as a polygon) of this tank image
     * @return a polygon representing this tank
     */
    public Polygon getTankShape() {
        int centerX = tank.getTankX();
        int centerY = tank.getTankY();
        int[] xPoints = new int[15];
        int[] yPoints = new int[15];
        xPoints[0] = centerX - (tankWidth / 2);
        yPoints[0] = centerY - (tankHeight / 2);
        xPoints[1] = xPoints[0] + tankWidth;
        yPoints[1] = yPoints[0];
        xPoints[2] = xPoints[1];
        yPoints[2] = yPoints[1] + tankHeight;
        xPoints[3] = xPoints[2] - tankWidth;
        yPoints[3] = yPoints[2];
        xPoints[4] = xPoints[0];
        yPoints[4] = yPoints[0];
        xPoints[5] = centerX - (turretWidth / 2);
        yPoints[5] = yPoints[4];
        xPoints[6] = xPoints[5];
        yPoints[6] = yPoints[5] + (turretHeight / 3 * 2);
        xPoints[7] = xPoints[6] + turretWidth;
        yPoints[7] = yPoints[6];
        xPoints[8] = xPoints[7];
        yPoints[8] = yPoints[7] - turretHeight;
        xPoints[9] = xPoints[8] - turretWidth;
        yPoints[9] = yPoints[8];
        xPoints[10] = xPoints[5];
        yPoints[10] = yPoints[5];
        Polygon shape = new Polygon(xPoints, yPoints, 11);
        return shape;
    }

    /**
     * Draws the tank on the grahics class passed to it.
     * @param g the graphics class to draw on
     */
    public void draw(Graphics g) {
        if (tank.getShot() != null) {
            drawShot(g);
        }
        drawTankOutline(g);
    }

    public static final int tankWidth = 25;

    public static final int tankHeight = 30;

    public static final int turretWidth = tankWidth / 6;

    public static final int turretHeight = (tankHeight / 2) + (tankHeight / 3);

    /**
     * Draws the outline of the tank
     * @param g
     */
    private void drawTankOutline(Graphics g) {
        Shape shape = getTankShape();
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform old = g2.getTransform();
        AffineTransform affineTransform = AffineTransform.getTranslateInstance(0, 0);
        g2.transform(affineTransform);
        g2.setPaint(Color.GREEN);
        g2.transform(AffineTransform.getRotateInstance(tank.getDegrees(), tank.getTankX(), tank.getTankY()));
        g2.draw(shape);
        g2.setColor(Color.BLACK);
        g2.setTransform(old);
    }

    /**
     * Draws the shot
     * @param g
     */
    private void drawShot(Graphics g) {
        tank.getShot().draw(g);
    }
}
