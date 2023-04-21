package leon.daoi;

import java.util.List;
import java.util.ListIterator;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.awt.geom.*;
import leon.tia.LocalExtremum;

public class ExtremaIconJAI extends IconJAI {

    static final Color red = Color.red;

    static final Color blue = Color.blue;

    static final BasicStroke stroke = new BasicStroke(2.0f);

    static final BasicStroke wideStroke = new BasicStroke(8.0f);

    List maxima = null;

    List minima = null;

    static final boolean debug = false;

    public ExtremaIconJAI(RenderedImage im, List maxima, List minima) {
        super(im);
        this.maxima = maxima;
        this.minima = minima;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        super.paintIcon(c, g, x, y);
        if (debug) System.out.println("In " + getClass() + " method paintIcon(Component c, Graphics g, int x, int y)");
        if (debug) {
            System.out.println("translation x: " + x);
            System.out.println("translation y: " + y);
        }
        Graphics2D g2D = (Graphics2D) g;
        int transX = x;
        int transY = y;
        AffineTransform transform = AffineTransform.getTranslateInstance(transX, transY);
        g2D.transform(transform);
        g2D.setStroke(stroke);
        g2D.setPaint(red);
        double maxAbsoluteValue = 0;
        if (maxima != null) {
            ListIterator maximaIt = maxima.listIterator();
            while (maximaIt.hasNext()) {
                LocalExtremum m = (LocalExtremum) maximaIt.next();
                maxAbsoluteValue = Math.max(maxAbsoluteValue, Math.abs(m.getValue()));
            }
        }
        if (minima != null) {
            ListIterator minimaIt = minima.listIterator();
            while (minimaIt.hasNext()) {
                LocalExtremum m = (LocalExtremum) minimaIt.next();
                maxAbsoluteValue = Math.max(maxAbsoluteValue, Math.abs(m.getValue()));
            }
        }
        double sizeScaling = (maxAbsoluteValue == 0) ? 1.0D : 10.0D / maxAbsoluteValue;
        if (maxima != null) {
            ListIterator maximaIt = maxima.listIterator();
            while (maximaIt.hasNext()) {
                LocalExtremum m = (LocalExtremum) maximaIt.next();
                int radius = (int) (sizeScaling * Math.abs(m.getValue()));
                radius = (radius == 0) ? 1 : radius;
                g2D.draw(new Ellipse2D.Double(m.getX() - radius, m.getY() - radius, 2 * radius + 1, 2 * radius + 1));
            }
        }
        g2D.setPaint(blue);
        if (minima != null) {
            ListIterator minimaIt = minima.listIterator();
            while (minimaIt.hasNext()) {
                LocalExtremum m = (LocalExtremum) minimaIt.next();
                int radius = (int) (sizeScaling * Math.abs(m.getValue()));
                radius = (radius == 0) ? 1 : radius;
                g2D.draw(new Ellipse2D.Double(m.getX() - radius, m.getY() - radius, 2 * radius + 1, 2 * radius + 1));
            }
        }
        if (debug) System.out.println("Out " + getClass() + " method paintIcon(Component c, Graphics g, int x, int y)");
    }
}
