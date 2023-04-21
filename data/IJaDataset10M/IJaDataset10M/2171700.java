package physicssite.ui.world;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.io.IOException;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.JointDef;
import physicssite.serialization.SIMOutputStream;
import physicssite.ui.Canvas;

/**
 * Kein eigentlicher JBox2D-Joint; verbundene Objekte werden zu einem Body zusammengefügt
 * 
 * @author Weltall 7
 *
 */
public class FixedJoint extends Joint {

    public FixedJoint(final Shape shape1, final Shape shape2, final Vec2 position) {
        super(shape1, shape2);
        this.position = position;
    }

    @Override
    protected void ipaint(final Graphics2D g, final Vec2 position, final double rotation, final Vec2 relPos) {
        final Point p = Canvas.calculatePixelPos(position);
        final Stroke s = g.getStroke();
        g.setStroke(new BasicStroke(3));
        g.setColor(getColor());
        g.drawLine(p.x - 8, p.y - 8, p.x + 8, p.y + 8);
        g.drawLine(p.x + 8, p.y - 8, p.x - 8, p.y + 8);
        g.setStroke(s);
    }

    @Override
    protected void ipaintHighlight(final Graphics2D g, final Vec2 position, final double rotation, final Vec2 relPos) {
        final Point p = Canvas.calculatePixelPos(position);
        final Stroke s = g.getStroke();
        g.setStroke(new BasicStroke(5));
        g.setColor(getIColor());
        g.drawLine(p.x - 8, p.y - 8, p.x + 8, p.y + 8);
        g.drawLine(p.x + 8, p.y - 8, p.x - 8, p.y + 8);
        g.setStroke(new BasicStroke(3));
        g.setColor(getColor());
        g.drawLine(p.x - 8, p.y - 8, p.x + 8, p.y + 8);
        g.drawLine(p.x + 8, p.y - 8, p.x - 8, p.y + 8);
        g.setStroke(s);
    }

    @Override
    public void resize(final double delta) {
    }

    @Override
    public java.awt.Rectangle getArea() {
        final Point p = Canvas.calculatePixelPos(position);
        return new java.awt.Rectangle(p.x - 10, p.y - 10, 20, 20);
    }

    @Override
    public Types getType() {
        return Types.FIXEDJOINT;
    }

    @Override
    public FixedJoint clone() {
        final FixedJoint o = (FixedJoint) super.clone();
        if (Canvas.clonedObjects.contains(o)) return o;
        return o;
    }

    @Override
    public JointDef create() {
        return null;
    }

    @Override
    public void save(final SIMOutputStream os) throws IOException {
        super.save(os);
    }
}
