package net.java.dev.joode.geom;

import net.java.dev.joode.ClonedReferences;
import net.java.dev.joode.space.Space;
import net.java.dev.joode.util.*;

/**
 * @author Arne Mueller
 * 
 */
public class Box extends Geom {

    private static final long serialVersionUID = 6728403824541923859L;

    private Vector3 side;

    public Box(Space space, Vector3 side) {
        super(space, GEOM_TYPE_BOX, true);
        assert side.getX() > 0 : "side.x must be greater than zero";
        assert side.getY() > 0 : "side.y must be greater than zero";
        assert side.getZ() > 0 : "side.z must be greater than zero";
        this.side = side;
        if (space != null) space.add(this);
    }

    private Box() {
        super(GEOM_TYPE_BOX, true);
    }

    /**
     *
     * @param space if you get strange null pointer exceptions, try passing the space argument in as null,
     * and later setting it manually
     * @param x
     * @param y
     * @param z
     */
    public Box(Space space, float x, float y, float z) {
        this(space, new Vector3(x, y, z));
    }

    /** sets the size of the box (side length)
     * 
     * @param side
     */
    public final void setSide(Vector3 side) {
        this.side = side;
        Space.onGeomMoved(this);
    }

    /**
     * sets the size of the box (side length)
     * @param x
     * @param y
     * @param z
     */
    public final void setSide(float x, float y, float z) {
        this.side.setX(x);
        this.side.setY(y);
        this.side.setZ(z);
        Space.onGeomMoved(this);
    }

    /**
     * gets the size of the box
     * @return
     */
    public final Vector3 getSide() {
        return this.side;
    }

    @Override
    public void computeAABB() {
        final Vector3 pos = this.getPosition();
        final Matrix3 rot = this.getRotation();
        float xrange = 0.5f * (Math.abs(rot.get(0, 0) * this.side.getX()) + Math.abs(rot.get(1, 0) * this.side.getY()) + Math.abs(rot.get(2, 0) * this.side.getZ()));
        float yrange = 0.5f * (Math.abs(rot.get(0, 1) * this.side.getX()) + Math.abs(rot.get(1, 1) * this.side.getY()) + Math.abs(rot.get(2, 1) * this.side.getZ()));
        float zrange = 0.5f * (Math.abs(rot.get(0, 2) * this.side.getX()) + Math.abs(rot.get(1, 2) * this.side.getY()) + Math.abs(rot.get(2, 2) * this.side.getZ()));
        getAABB().set(pos.getX() - xrange, pos.getX() + xrange, pos.getY() - yrange, pos.getY() + yrange, pos.getZ() - zrange, pos.getZ() + zrange);
    }

    public float pointDepth(float x, float y, float z) {
        final Vector3 pos = this.getPosition();
        Vector3 p, q;
        p = new Vector3(x - pos.getX(), y - pos.getY(), z - pos.getZ());
        q = new Vector3();
        MathUtils.dMULTIPLY1_331(q, this.getRotation(), p);
        float[] dist = new float[6];
        int i;
        boolean inside = true;
        for (i = 0; i < 3; i++) {
            float s = side.get(i) * 0.5f;
            dist[i] = s - q.get(i);
            dist[i + 3] = s + q.get(i);
            if ((dist[i] < 0) || (dist[i + 3] < 0)) {
                inside = false;
            }
        }
        if (inside) {
            float smallest_dist = Float.MAX_VALUE;
            for (i = 0; i < 6; i++) {
                if (dist[i] < smallest_dist) smallest_dist = dist[i];
            }
            return smallest_dist;
        }
        float largest_dist = 0f;
        for (i = 0; i < 6; i++) {
            if (dist[i] > largest_dist) largest_dist = dist[i];
        }
        return -largest_dist;
    }

    public Box cloneState(ClonedReferences util) {
        Box clone = new Box();
        clonePartialGeom(clone, util);
        clone.side = new Vector3();
        clone.side.set(side);
        return clone;
    }
}
