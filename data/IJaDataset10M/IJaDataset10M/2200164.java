package org.openwar.victory.geom;

import org.openwar.victory.GameRuntimeException;
import org.openwar.victory.NotImplementedException;
import org.openwar.victory.math.Vector2;

/**
 * A box, which has a position and a size.<br/>
 * @author Bart van Heukelom
 */
public class Rectangle implements Volume {

    private Point position;

    private Vector2 size;

    /**
     * Create a box at 0,0 with size 0 x 0.
     */
    public Rectangle() {
        position = new Point();
        size = new Vector2();
    }

    /**
     * Create a box.
     * @param thePosition The position of the box.
     * @param theSize The size of the box.
     * @throws NullPointerException if either the position or size are null.
     */
    public Rectangle(final Point thePosition, final Vector2 theSize) {
        if (thePosition == null || theSize == null) {
            throw new NullPointerException("Position and/or Size cannot be NULL");
        }
        position = thePosition.clone();
        size = theSize.clone();
    }

    /**
     * Create a box.
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     * @param width The width.
     * @param height The height.
     */
    public Rectangle(final double x, final double y, final double width, final double height) {
        this.position = new Point(x, y);
        this.size = new Vector2(width, height);
    }

    /**
     * Create a box at 0,0 with a specified size.
     * @param width The width.
     * @param height The height.
     */
    public Rectangle(final double width, final double height) {
        this.position = new Point();
        this.size = new Vector2(width, height);
    }

    /**
     * Create a box with a specified width/height and a position -width/2,
     * -height/2.
     * @param width The width of the box.
     * @param height The height of the box.
     * @return The new <code>Box</code>
     */
    public static Rectangle centeredBox(final double width, final double height) {
        return new Rectangle(-width / 2, -height / 2, width, height);
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle clone() {
        return new Rectangle(position, size);
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle moved(final Vector2 d) {
        final Rectangle b = new Rectangle(position, size);
        b.position.add(d);
        return b;
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle move(final Vector2 d) {
        position.add(d);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle scale(final Vector2 factor) {
        size.multiply(factor);
        position.multiply(factor);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle scaled(final Vector2 factor) {
        return clone().scale(factor);
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle fit(final Rectangle d) {
        return adapt(d);
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle fitted(final Rectangle d) {
        return clone().adapt(d);
    }

    /**
     * Expand this box on all sides.
     * @param x The horizontal expansion.
     * @param y The vertical expansion.
     * @return This box.
     */
    public Rectangle expand(final double x, final double y) {
        position.add(-x, -y);
        size.add(2 * x, 2 * y);
        return this;
    }

    /**
     * Expand this box on all sides.
     * @param x The horizontal expansion.
     * @param y The vertical expansion.
     * @return The result as new box.
     */
    public Rectangle expanded(final double x, final double y) {
        return new Rectangle(position.x() - x, position.y() - y, size.x() + 2 * x, size.y() + 2 * y);
    }

    /**
     * @return The top left corner of the box
     */
    public Point getTopLeft() {
        return new Point(getLeft(), getTop());
    }

    /**
     * @return The top right corner of the box
     */
    public Point getTopRight() {
        return new Point(getRight(), getTop());
    }

    /**
     * @return The bottom left corner of the box
     */
    public Point getBottomLeft() {
        return new Point(getLeft(), getBottom());
    }

    /**
     * @return The bottom right corner of the box
     */
    public Point getBottomRight() {
        return new Point(getRight(), getBottom());
    }

    /**
     * @return The center point in this box.
     */
    public Point getCenter() {
        return (Point) position.clone().add(size.clone().multiply(0.5, 0.5));
    }

    /**
     * @return The y-coordinate of the top side of the box.
     */
    public double getTop() {
        if (size.y() < 0) {
            return position.y() + size.y();
        }
        return position.y();
    }

    /**
     * @return The x-coordinate of the left side of the box.
     */
    public double getLeft() {
        if (size.x() < 0) {
            return position.x() + size.x();
        }
        return position.x();
    }

    public Line getLeftSide() {
        return new Line(getTopLeft(), getBottomLeft());
    }

    public Line getRightSide() {
        return new Line(getTopRight(), getBottomRight());
    }

    public Line getTopSide() {
        return new Line(getTopLeft(), getTopRight());
    }

    public Line getBottomSide() {
        return new Line(getBottomLeft(), getBottomRight());
    }

    public Rectangle absolutize() {
        position.set(getLeft(), getTop());
        size.absolutize();
        return this;
    }

    public Rectangle absolutized() {
        return clone().absolutize();
    }

    /**
     * @return The x-coordinate of the right side of the box.
     */
    public double getRight() {
        if (size.x() < 0) {
            return position.x();
        }
        return position.x() + size.x();
    }

    /**
     * @return The y-coordinate of the bottom side of the box.
     */
    public double getBottom() {
        if (size.y() < 0) {
            return position.y();
        }
        return position.y() + size.y();
    }

    /**
     * @return The diagonal length of this box. Equal to <code>getSize().getLength()</code>.
     */
    public double getDiagonal() {
        return size.getLength();
    }

    /** {@inheritDoc} */
    @Override
    public boolean overlaps(final Geometry other) {
        return overlaps(other, null, null);
    }

    /** {@inheritDoc} */
    @Override
    public boolean overlaps(final Geometry other, final Point myPos, final Point otherPos) {
        if (other instanceof Rectangle) {
            return overlapsBox((Rectangle) other, myPos, otherPos);
        } else if (other instanceof Point) {
            return containsPoint((Point) other, myPos, otherPos);
        } else {
            throw new GameRuntimeException("Box.overlaps(" + other.getClass().getSimpleName() + ") not implemented");
        }
    }

    private boolean overlapsBox(final Rectangle other, final Point myPos, final Point otherPos) {
        final Rectangle me = (myPos == null) ? this : this.moved(myPos);
        final Rectangle him = (otherPos == null) ? other : other.moved(otherPos);
        return (me.getRight() > him.position.x() && me.position.x() < him.getRight() && me.getBottom() > him.position.y() && me.position.y() < him.getBottom());
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Geometry other) {
        return contains(other, null, null);
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Geometry other, final Point myPos, final Point otherPos) {
        if (other instanceof Rectangle) {
            return containsBox((Rectangle) other, myPos, otherPos);
        } else if (other instanceof Vector2) {
            return containsPoint((Point) other, myPos, otherPos);
        } else if (other instanceof Line) {
            return containsLine((Line) other, myPos, otherPos);
        } else if (other instanceof Circle) {
            return containsBox(((Circle) other).getBoundingRectangle(), myPos, otherPos);
        } else if (other instanceof Triangle) {
            return containsTriangle((Triangle) other, myPos, otherPos);
        }
        throw new GameRuntimeException("Box.contains(" + other.getClass().getSimpleName() + ") not implemented");
    }

    private boolean containsBox(final Rectangle other, final Vector2 myPos, final Vector2 otherPos) {
        Rectangle absThis, absOther;
        absThis = (myPos == null ? this : clone().move(myPos));
        absOther = (otherPos == null ? other : other.clone().move(myPos));
        return (absThis.getRight() >= absOther.getRight() && absThis.getLeft() <= absOther.getLeft() && absThis.getBottom() >= absOther.getBottom() && absThis.getTop() <= absOther.getTop());
    }

    private boolean containsPoint(final Point other, final Point myPos, final Point otherPos) {
        Rectangle absThis;
        Point absOther;
        absThis = (myPos == null ? this : clone().move(myPos));
        absOther = (otherPos == null ? other : other.clone().move(myPos));
        return (absOther.x() <= absThis.getRight() && absOther.x() >= absThis.getLeft() && absOther.y() <= absThis.getBottom() && absOther.y() >= absThis.getTop());
    }

    private boolean containsLine(final Line line, final Point myPos, final Point otherPos) {
        return (this.containsPoint(line.getStart(), myPos, otherPos) && this.containsPoint(line.getEnd(), myPos, otherPos));
    }

    private boolean containsTriangle(final Triangle triangle, final Point myPos, final Point otherPos) {
        return (this.containsPoint(triangle.getVertices()[0], myPos, otherPos) && this.containsPoint(triangle.getVertices()[1], myPos, otherPos) && this.containsPoint(triangle.getVertices()[2], myPos, otherPos));
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle adapt(final Geometry other) {
        if (other instanceof Rectangle) {
            position.set(((Rectangle) other).position);
            size.set(((Rectangle) other).size);
            return this;
        } else {
            throw new GameRuntimeException("Other is not a box");
        }
    }

    /**
     * Set the position (top-left corner) of the box.
     * @param newPosition The new position.
     */
    public void setPosition(final Point newPosition) {
        position.set(newPosition);
    }

    /**
     * @return Get the position (top-left corner) of the box.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Set the size of the box.
     * @param newSize The new size.
     */
    public void setSize(final Vector2 newSize) {
        size.set(newSize);
    }

    /**
     * @return The size of the box.
     */
    public Vector2 getSize() {
        return size;
    }

    /** {@inheritDoc} */
    @Override
    public Rectangle getBoundingRectangle() {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Circle getBoundingCircle() {
        return new Circle(getCenter(), getDiagonal() / 2);
    }

    /** {@inheritDoc} */
    @Override
    public Polygon getPolygon(final int segments) {
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        return String.format("Rectangle (%f, %f) (%f x %f)", position.x(), position.y(), size.x(), size.y());
    }
}
