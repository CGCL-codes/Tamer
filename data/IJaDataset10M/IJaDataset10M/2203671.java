package physicssite.ui.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import physicssite.serialization.SIM;
import physicssite.serialization.SIMOutputStream;
import physicssite.ui.Canvas;
import physicssite.ui.Editor;

/**
 * ein Objekt
 * 
 * @author Weltall 7
 *
 */
public abstract class WorldObject implements Cloneable {

    public Vec2 position;

    public Vec2 getPixelPos() {
        return Canvas.calculateUnroundedPixelPos(position);
    }

    public double rotation;

    private Color color = Color.gray;

    private Color icolor = Color.gray;

    private Color color2 = Color.darkGray;

    public double initialRotation;

    public Vec2 initialPosition;

    public Vec2 initialPosRelToMouse;

    public WorldObject() {
    }

    protected abstract void ipaint(Graphics2D g, Vec2 position, double rotation, Vec2 relPos);

    protected abstract void ipaintHighlight(Graphics2D g, Vec2 position, double rotation, Vec2 relPos);

    public void paint(final Graphics2D g) {
        if (Canvas.selected.contains(this)) ipaintHighlight(g, position, 0, new Vec2()); else ipaint(g, position, 0, new Vec2());
    }

    public void paint(final Graphics2D g, final Fixture fixture) {
        ipaint(g, fixture.getBody().getPosition(), fixture.getBody().getAngle(), position.sub(((physicssite.ui.world.Shape) fixture.getUserData()).position));
    }

    public abstract void resize(double delta);

    public abstract java.awt.Shape getArea();

    public final boolean intersects(final java.awt.Rectangle r) {
        return getArea().intersects(r);
    }

    public final boolean contains(final Point p) {
        return getArea().contains(p);
    }

    public void delete() {
        Editor.simulation.remove(this);
    }

    public void undelete() {
        Editor.simulation.add(this);
    }

    public abstract Types getType();

    public void setColor(final Color color) {
        this.color = color;
        icolor = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
        color2 = new Color(color.getRed() / 2 + 50, color.getGreen() / 2 + 50, color.getBlue() / 2 + 50, color.getAlpha());
    }

    public Color getColor() {
        return color;
    }

    public Color getIColor() {
        return icolor;
    }

    public Color getColor2() {
        return color2;
    }

    @Override
    public WorldObject clone() {
        WorldObject o;
        o = Canvas.clonedObjects.get(Canvas.objectsToClone.indexOf(this));
        if (o != null) return o;
        try {
            o = (WorldObject) super.clone();
            o.position = position.clone();
            o.setColor(new Color(getColor().getRed(), getColor().getGreen(), getColor().getBlue(), getColor().getAlpha()));
            Canvas.clonedObjects.set(Canvas.objectsToClone.indexOf(this), o);
        } catch (final CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    public void save(final SIMOutputStream os) throws IOException {
        os.writeVec2(SIM.d_position, position);
        os.writeNumber(SIM.d_rotation, rotation);
        os.writeColor(SIM.d_color, color);
    }
}
