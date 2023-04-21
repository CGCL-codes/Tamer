package uchicago.src.sim.space;

import java.util.Vector;

public class VNNeighborhooder extends AbstractNeighborhooder {

    private boolean torus = false;

    public VNNeighborhooder(Discrete2DSpace space) {
        super(space);
        torus = space instanceof Torus;
    }

    public Vector getNeighbors(int x, int y, int[] extents, boolean returnNulls) {
        if (extents.length != 2) throw new IllegalArgumentException("Von Neumann neighborhoods require an extents array of 2 integers");
        int xExtent = extents[0];
        int yExtent = extents[1];
        Vector v = new Vector((xExtent * 2) + (yExtent * 2));
        int xLeft = xExtent;
        int xRight = xExtent;
        if (!torus) {
            if (x - xLeft < 0) xLeft = x;
            if (x + xRight > space.getSizeX() - 1) xRight = space.getSizeX() - 1 - x;
        }
        for (int i = x - xLeft; i < x; i++) {
            Object o = space.getObjectAt(i, y);
            if (returnNulls) v.add(o); else if (o != null) v.add(o);
        }
        for (int i = x + xRight; i > x; i--) {
            Object o = space.getObjectAt(i, y);
            if (returnNulls) v.add(o); else if (o != null) v.add(o);
        }
        int yTop = yExtent;
        int yBottom = yExtent;
        if (!torus) {
            if (y + yBottom > space.getSizeY() - 1) yBottom = space.getSizeY() - 1 - y;
            if (y - yTop < 0) yTop = y;
        }
        for (int i = y - yTop; i < y; i++) {
            Object o = space.getObjectAt(x, i);
            if (returnNulls) v.add(o); else if (o != null) v.add(o);
        }
        for (int i = y + yBottom; i > y; i--) {
            Object o = space.getObjectAt(x, i);
            if (returnNulls) v.add(o); else if (o != null) v.add(o);
        }
        return v;
    }
}
