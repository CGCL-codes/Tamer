package watford.util.quadtree;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.util.Collection;
import java.util.Observable;

/** AbstractKineticObject provides a convenient wrapper around the
 * 1 class and 2 interfaces you need to embody in order to be part
 * of a Quadtree. Polymorphism that allowed interfaces to implement
 * interfaces would be very nice and could solve such a problem.
 * 
 * @author Christopher A. Watford
 *
 */
public abstract class AbstractKineticObject extends Observable implements ISpatialObject {

    private transient AbstractSpatialSet parent;

    /**
	 * Create an AbstractKineticObject with no parent.
	 *
	 */
    public AbstractKineticObject() {
        this.parent = null;
    }

    /**
	 * Create an AbstractKineticObject with a parent.
	 * @param parent Object who recieves ISpatialSet messages
	 */
    public AbstractKineticObject(AbstractSpatialSet parent) {
        addParent(parent);
    }

    /**
	 * Move the object to the point.
	 * 
	 * NB: You must notify the parent node if the object
	 * moves in a manner that affects the parent!
	 * @param p Point to move the object to.
	 */
    public abstract void translate(Point p);

    /**
	 * Move the object by dx and dy.
	 * 
	 * NB: You must notify the parent node if the object
	 * moves in a manner that affects the parent!
	 * @param dx Distance to move horizontally
	 * @param dy Distance to move vertically
	 */
    public abstract void translate(int dx, int dy);

    public abstract Rectangle getBounds();

    public abstract boolean containedPartiallyBy(Rectangle rect);

    public abstract boolean intersects(GeneralPath path);

    public abstract boolean intersects(Rectangle rect);

    public abstract boolean intersects(Point pnt);

    /**
	 * Sets the parent node of the object, adding the new
	 * parent as an observer.
	 * @param p Set the parent node of this object
	 */
    public final void addParent(AbstractSpatialSet p) {
        deleteParent(this.parent);
        this.parent = p;
        if (p != null) this.addObserver(p);
    }

    public final void deleteParent(AbstractSpatialSet p) {
        this.deleteObserver(p);
        if (p == this.parent) this.parent = null;
    }

    public final boolean hasParent(AbstractSpatialSet p) {
        return (this.parent == p);
    }

    public Collection getParents() {
        return null;
    }

    public final AbstractSpatialSet getParent() {
        return this.parent;
    }
}
