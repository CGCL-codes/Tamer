package locusts.common.entities;

import locusts.server.collisions.*;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import locusts.lib.geom.Shapes;

/**
 *
 * @author Hamish Morgan
 */
public class CollidableEntity extends Entity implements Collidable, Serializable, Cloneable {

    private transient Area area = null;

    private transient Shape shape = null;

    private double maxRadius;

    public CollidableEntity(int typeId, double x, double y, Shape shape) {
        super(typeId, x, y);
        this.shape = shape;
        this.area = new Area(shape);
        maxRadius = Shapes.calculateMaxRadius(shape);
    }

    public CollidableEntity(int typeId, double x, double y, Shape shape, double orientation) {
        super(typeId, x, y, orientation);
        this.shape = shape;
        this.area = new Area(shape);
        maxRadius = Shapes.calculateMaxRadius(shape);
    }

    public CollidableEntity(int typeId, double x, double y, Shape shape, double orientation, double energy) {
        super(typeId, x, y, orientation, energy);
        this.shape = shape;
        this.area = new Area(shape);
        maxRadius = Shapes.calculateMaxRadius(shape);
    }

    public CollidableEntity(CollidableEntity that) {
        super(that);
        this.area = (Area) that.area.clone();
        this.shape = that.shape;
        this.maxRadius = that.maxRadius;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        Shapes.writeShape(shape, out);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        shape = Shapes.readShape(in);
        area = new Area(shape);
        maxRadius = Shapes.calculateMaxRadius(shape);
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
        this.area = new Area(shape);
        this.maxRadius = Shapes.calculateMaxRadius(shape);
    }

    public Area getArea() {
        return area;
    }

    public Area geTransformedArea() {
        return getArea().createTransformedArea(getAffineTransform());
    }

    public double getMaxRadius() {
        return maxRadius;
    }

    @Override
    public CollidableEntity clone() {
        return new CollidableEntity(this);
    }
}
