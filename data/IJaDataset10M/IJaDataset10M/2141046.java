package org.jalgo.module.heapsort.renderer;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>The CanvasEntity class represents an object on the virtual canvas. Each such object has
 * an affine transformation, which describes the relationship between the ("relative", or local)
 * coordinates wrt. to the entity and the ("absolute") coordinates which are local to its parent.
 * </p>
 * 
 * <p>A canvas entity keeps a log of where the image has to be redrawn, the so-called dirty region.
 * The dirty region is a rectangle expressed in absolute coordinates (which is needed in case
 * the affine transform is changed, and in particular, when the new transform has no inverse).
 * (Imagine how you would express some non-empty box in local coordinates when the transform
 * maps everything to one particular point.)
 * </p>
 * 
 * <p>Furthermore, canvas entities have a bounding box (expressed in local coordinates) and an
 * integer describing the position in the z order of the canvas. Note that, if the bounding box
 * were in absolute coordinates, it could grow every time a non-quadrant rotation is applied.
 * Besides, the transformation could not be reset, only concatenated.</p>
 * 
 * @author mbue
 */
public class CanvasEntity {

    protected Rectangle bounds = new Rectangle(0, 0, -1, -1);

    protected Rectangle dirty = new Rectangle(0, 0, -1, -1);

    protected AffineTransform trans = new AffineTransform();

    protected int zorder = 0;

    protected Set<CanvasEntity> children = new TreeSet<CanvasEntity>(CanvasEntityComparator.getInstance());

    protected CanvasEntity() {
    }

    /**
	 * <p>Adds canvas entity <code>c</code> to the children of this node.
	 * Does nothing if <code>c</code> already in this hierarchy.</p>
	 * <p>Note: This will of course not stop you from adding the same canvas
	 * entity twice to some tree by adding it to some node and then
	 * to another one which is deeper in the tree. Just don't do it.</p>
	 *  
	 * @param c canvas entity to be added
	 */
    public void addChild(CanvasEntity c) {
        if (findParentOf(c) == null) children.add(c);
    }

    /**
	 * Finds the parent node of a given canvas entity <code>c</code>.
	 * Returns <code>null</code> iff this node is not contained in the
	 * subtrees below <code>this</code>.
	 * 
	 * @param c
	 * @return parent of <code>c</code>, or <code>null</code> if
	 * <code>c</code> not in this hierarchy
	 */
    public CanvasEntity findParentOf(CanvasEntity c) {
        if (children.isEmpty()) return null;
        if (children.contains(c)) return this;
        CanvasEntity result = null;
        for (CanvasEntity ch : children) {
            result = ch.findParentOf(c);
            if (result != null) return result;
        }
        return result;
    }

    /**
	 * Remove <code>c</code> from the children of this node.
	 * Adjust dirty region accordingly.
	 * Does nothing if <code>c</code> is no child of this node.
	 * 
	 * @param c canvas entity to be removed
	 */
    public void removeChild(CanvasEntity c) {
        if (children.contains(c)) {
            c.invalidate();
            dirty.add(transformBounds(c.dirty, trans));
            children.remove(c);
        }
    }

    /**
	 * Apply the visitor <code>v</code> to every canvas entity in this subtree.
	 * 
	 * @param v
	 */
    public void fold(CanvasEntityVisitor v) {
        fold(v, new AffineTransform());
    }

    protected void fold(CanvasEntityVisitor v, AffineTransform t) {
        AffineTransform t1 = new AffineTransform(t);
        t1.concatenate(trans);
        v.invoke(this, t1, null);
        for (CanvasEntity e : children) e.fold(v, t1);
    }

    /**
	 * <p>Apply the visitor <code>v</code> to every canvas entity in this subtree, provided that
	 * they intersect with the rectangle <code>r</code>, which is regarded as being in absolute
	 * coordinates wrt. <code>this</code>.</p>
	 * 
	 * <p>As an example scenario, the rectangle <code>r</code> could be the intersection of
	 * some visible area with the dirty region of <code>this</code>.</p> 
	 * 
	 * @param v
	 * @param r
	 * @param t
	 */
    public void foldVisible(CanvasEntityVisitor v, Rectangle r) {
        foldVisible(v, r, new AffineTransform());
    }

    protected void foldVisible(CanvasEntityVisitor v, Rectangle r, AffineTransform t) {
        AffineTransform t1 = new AffineTransform(t);
        t1.concatenate(trans);
        if (transformBounds(bounds, t1).intersects(r)) {
            v.invoke(this, t1, r);
            for (CanvasEntity e : children) e.foldVisible(v, r, t1);
        }
    }

    /**
	 * <p>Adds the bounding box to the dirty region causing this entity
	 * (and others underneath or above) to be redrawn.</p>
	 * 
	 * <p>NOTE that if you change the transformation, you have to call
	 * this beforehands <em>and</em> afterwards!
	 */
    public void invalidate() {
        dirty.add(transformBounds(bounds, trans));
    }

    /**
	 * Sets the bounding box of this canvas entity to <code>newBounds</code>, adding to
	 * the dirty region both the old and the new bounding box.
	 * 
	 * @param newBounds
	 */
    public void setBounds(Rectangle newBounds) {
        if (!bounds.equals(newBounds)) {
            invalidate();
            bounds.setBounds(newBounds);
            invalidate();
        }
    }

    /**
	 * Returns the z order value.
	 * @return
	 */
    public int getZorder() {
        return zorder;
    }

    /**
	 * Changes the Z order of this entity. This will invalidate the current image of the entity.
	 * 
	 * @param newz
	 */
    public void setZorder(int newz) {
        if (newz != zorder) {
            zorder = newz;
            invalidate();
        }
    }

    /**
	 * Compute the dirty region of the whole entity subtree (starting at <code>this</code>),
	 * which is the union of the dirty regions of the entities, transformed into the absolute
	 * coordinate system wrt. <code>this</code>.
	 * 
	 * @return Union of dirty regions of subtree
	 */
    public Rectangle computeDirtyRegion() {
        ComputeDirtyVisitor v = new ComputeDirtyVisitor(dirty);
        fold(v);
        return v.dirty;
    }

    /**
     * Clear the dirty regions of the whole entity subtree starting at <code>this</code>.
     *
     */
    public void clearDirtyRegion() {
        fold(ClearDirtyVisitor.getInstance());
    }

    /**
     * Compute the bounding box of the rectangle which is obtained by
     * applying the AffineTransform <code>t</code> to the rectangle
     * <code>b</code>.
     * 
     * @param b
     * @param t
     * @return
     */
    public static Rectangle transformBounds(Rectangle b, AffineTransform t) {
        if (b.width < 0 || b.height < 0) return b;
        Point2D[] s = { new Point2D.Double(b.x, b.y), new Point2D.Double(b.x, b.y + b.height), new Point2D.Double(b.x + b.width, b.y), new Point2D.Double(b.x + b.width, b.y + b.height) };
        Point2D[] d = new Point2D.Double[4];
        t.transform(s, 0, d, 0, 4);
        double minx = d[0].getX();
        double maxx = d[0].getX();
        double miny = d[0].getY();
        double maxy = d[0].getY();
        for (int i = 1; i < 4; i++) {
            if (d[i].getX() < minx) minx = d[i].getX();
            if (d[i].getX() > maxx) maxx = d[i].getX();
            if (d[i].getY() < miny) miny = d[i].getY();
            if (d[i].getY() > maxy) maxy = d[i].getY();
        }
        return new Rectangle((int) minx - 1, (int) miny - 1, (int) (maxx - minx) + 2, (int) (maxy - miny) + 2);
    }

    private static class ComputeDirtyVisitor implements CanvasEntityVisitor {

        public Rectangle dirty = null;

        public ComputeDirtyVisitor() {
            this(new Rectangle(0, 0, -1, -1));
        }

        public ComputeDirtyVisitor(Rectangle dirty) {
            this.dirty = dirty;
        }

        public void invoke(CanvasEntity e, AffineTransform t, Rectangle clip) {
            for (CanvasEntity c : e.children) {
                Rectangle dtrans = transformBounds(c.dirty, t);
                dirty.add(dtrans);
            }
        }
    }

    private static class ClearDirtyVisitor implements CanvasEntityVisitor {

        private static ClearDirtyVisitor arnie = new ClearDirtyVisitor();

        public void invoke(CanvasEntity e, AffineTransform t, Rectangle clip) {
            e.dirty.height = -1;
            e.dirty.width = -1;
        }

        public static ClearDirtyVisitor getInstance() {
            return arnie;
        }
    }

    /**
     * <p>A comparator for canvas entities.</p>
     * 
     * <p><code>c1 &lt;= c2</code> iff<br>
     * 
     * <code>c1.zorder &lt;= c2.zorder</code> or<br>
     * <code>c1.zorder == c2.zorder</code> implies <code>c1.hashCode() &lt;= c2.hashCode()</code>.</p>
     * 
     * @author mbue
     */
    private static class CanvasEntityComparator implements Comparator<CanvasEntity> {

        private static CanvasEntityComparator comp = new CanvasEntityComparator();

        public static Comparator<CanvasEntity> getInstance() {
            return comp;
        }

        public int compare(CanvasEntity o1, CanvasEntity o2) {
            int res = o1.zorder - o2.zorder;
            if (res == 0) return o1.hashCode() - o2.hashCode(); else return res;
        }
    }
}
