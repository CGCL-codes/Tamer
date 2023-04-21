package csimage.demo.openhouse.project2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import csimage.TheMatrix;

public class TestEllipse {

    public static void main(String[] args) {
        TheMatrix m = new TheMatrix(500, 500);
        m.show();
        Graphics2D g2 = m.getGraphics2D();
        Point2D.Double corner = new Point2D.Double(25, 25);
        Ellipse2D.Double ellipse = new Ellipse2D.Double(449, 25, 50, 50);
        g2.setColor(Color.BLACK);
        g2.drawOval((int) ellipse.getX(), (int) ellipse.getY(), 1, 1);
        g2.drawOval((int) (ellipse.getX() + ellipse.getWidth()), (int) (ellipse.getY() + ellipse.getHeight()), 1, 1);
        g2.drawOval((int) (ellipse.getX()), (int) (ellipse.getY() + ellipse.getHeight()), 1, 1);
        g2.drawOval((int) (ellipse.getX() + ellipse.getWidth()), (int) (ellipse.getY()), 1, 1);
        g2.draw(ellipse);
        m.repaint();
    }
}
