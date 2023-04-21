package objectDraw.canvas;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.*;

/** Class that represents a free hand drawing on the canvas.
 *
 */
public class FreeHandFigure extends AbstractFigure implements Figure {

    private java.awt.geom.Path2D.Double path;

    public FreeHandFigure(java.awt.geom.Path2D.Double p, Color figColor) {
        path = p;
        setColor(figColor);
        path = new java.awt.geom.Path2D.Double();
        setHandle(new HandleImpl(this));
    }

    @Override
    public boolean contains(java.awt.geom.Point2D.Double point) {
        return path.contains(point);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.draw(path);
    }

    @Override
    public void drawHandle(Graphics2D g) {
        g.setColor(Color.CYAN);
        Ellipse2D.Double ellipse = new Ellipse2D.Double((path.getBounds().getX() + path.getBounds().getWidth() - handle.getSize().getWidth() / 2), path.getBounds().getY() + path.getBounds().getHeight() - handle.getSize().getHeight() / 2, handle.getSize().getWidth(), handle.getSize().getHeight());
        g.fill(ellipse);
        g.setColor(Color.BLACK);
        g.draw(ellipse);
    }

    @Override
    public java.awt.geom.Rectangle2D getBounds() {
        return path.getBounds2D();
    }

    @Override
    public void setBounds(java.awt.geom.Rectangle2D.Double bounds) {
        AffineTransform at = new AffineTransform();
        double dX = bounds.getX() - path.getBounds().getX();
        double dY = bounds.getY() - path.getBounds().getX();
        double dW = bounds.getWidth() - path.getBounds().getWidth();
        double dH = bounds.getHeight() - path.getBounds().getHeight();
        at.setToTranslation(dX, dY);
        at.scale(dH, dW);
        path = (java.awt.geom.Path2D.Double) path.createTransformedShape(at);
        Rectangle r = new Rectangle((int) (path.getBounds().getX() + path.getBounds().getWidth() - handle.getSize().getWidth() / 2), (int) (path.getBounds().getY() + path.getBounds().getHeight() - handle.getSize().getHeight() / 2), (int) handle.getSize().getWidth(), (int) handle.getSize().getHeight());
        handle.setSize(r);
    }
}
