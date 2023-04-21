package tools;

import java.awt.Rectangle;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class Full3DCell implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient List<Full3DPortal> portalsList;

    private transient Full3DCellController controller;

    private transient Rectangle enclosingRectangle;

    private List<float[]> topWalls;

    private List<float[]> bottomWalls;

    private List<float[]> leftWalls;

    private List<float[]> rightWalls;

    private List<float[]> ceilWalls;

    private List<float[]> floorWalls;

    private List<float[]> leftPortals;

    private List<float[]> rightPortals;

    private List<float[]> topPortals;

    private List<float[]> bottomPortals;

    private List<float[]> ceilPortals;

    private List<float[]> floorPortals;

    public Full3DCell() {
        portalsList = new ArrayList<Full3DPortal>();
        topWalls = new ArrayList<float[]>();
        bottomWalls = new ArrayList<float[]>();
        topPortals = new ArrayList<float[]>();
        bottomPortals = new ArrayList<float[]>();
        leftWalls = new ArrayList<float[]>();
        rightWalls = new ArrayList<float[]>();
        leftPortals = new ArrayList<float[]>();
        rightPortals = new ArrayList<float[]>();
        ceilWalls = new ArrayList<float[]>();
        floorWalls = new ArrayList<float[]>();
        ceilPortals = new ArrayList<float[]>();
        floorPortals = new ArrayList<float[]>();
        enclosingRectangle = new Rectangle();
    }

    public final Object readResolve() throws ObjectStreamException {
        if (portalsList == null) portalsList = new ArrayList<Full3DPortal>();
        if (enclosingRectangle == null) enclosingRectangle = new Rectangle();
        computeEnclosingRectangle();
        controller = null;
        return (this);
    }

    public final boolean equals(Object o) {
        if (o == null || !(o instanceof Full3DCell)) return (false); else {
            Full3DCell c = (Full3DCell) o;
            return (topWalls.equals(c.getTopWalls()) && topPortals.equals(c.getTopPortals()) && bottomWalls.equals(c.getBottomWalls()) && bottomPortals.equals(c.getBottomPortals()) && leftWalls.equals(c.getLeftWalls()) && leftPortals.equals(c.getLeftPortals()) && rightWalls.equals(c.getRightWalls()) && rightPortals.equals(c.getRightPortals()));
        }
    }

    final void computeEnclosingRectangle() {
        float minx = Float.MAX_VALUE, minz = Float.MAX_VALUE, maxx = Float.MIN_VALUE, maxz = Float.MIN_VALUE;
        for (float[] vertex : topWalls) {
            if (vertex[2] < minx) minx = vertex[2]; else if (vertex[2] > maxx) maxx = vertex[2];
            if (vertex[4] < minz) minz = vertex[4]; else if (vertex[4] > maxz) maxz = vertex[4];
        }
        for (float[] vertex : bottomWalls) {
            if (vertex[2] < minx) minx = vertex[2]; else if (vertex[2] > maxx) maxx = vertex[2];
            if (vertex[4] < minz) minz = vertex[4]; else if (vertex[4] > maxz) maxz = vertex[4];
        }
        for (float[] vertex : leftWalls) {
            if (vertex[2] < minx) minx = vertex[2]; else if (vertex[2] > maxx) maxx = vertex[2];
            if (vertex[4] < minz) minz = vertex[4]; else if (vertex[4] > maxz) maxz = vertex[4];
        }
        for (float[] vertex : rightWalls) {
            if (vertex[2] < minx) minx = vertex[2]; else if (vertex[2] > maxx) maxx = vertex[2];
            if (vertex[4] < minz) minz = vertex[4]; else if (vertex[4] > maxz) maxz = vertex[4];
        }
        for (float[] vertex : topPortals) {
            if (vertex[2] < minx) minx = vertex[2]; else if (vertex[2] > maxx) maxx = vertex[2];
            if (vertex[4] < minz) minz = vertex[4]; else if (vertex[4] > maxz) maxz = vertex[4];
        }
        for (float[] vertex : bottomPortals) {
            if (vertex[2] < minx) minx = vertex[2]; else if (vertex[2] > maxx) maxx = vertex[2];
            if (vertex[4] < minz) minz = vertex[4]; else if (vertex[4] > maxz) maxz = vertex[4];
        }
        for (float[] vertex : leftPortals) {
            if (vertex[2] < minx) minx = vertex[2]; else if (vertex[2] > maxx) maxx = vertex[2];
            if (vertex[4] < minz) minz = vertex[4]; else if (vertex[4] > maxz) maxz = vertex[4];
        }
        for (float[] vertex : rightPortals) {
            if (vertex[2] < minx) minx = vertex[2]; else if (vertex[2] > maxx) maxx = vertex[2];
            if (vertex[4] < minz) minz = vertex[4]; else if (vertex[4] > maxz) maxz = vertex[4];
        }
        enclosingRectangle.setFrameFromDiagonal(minx, minz, maxx, maxz);
    }

    public final Rectangle getEnclosingRectangle() {
        return (enclosingRectangle);
    }

    public final List<float[]> getTopWalls() {
        return (topWalls);
    }

    public final void addTopWall(float[] topWall) {
        topWalls.add(topWall);
    }

    public final void setTopWalls(List<float[]> topWalls) {
        this.topWalls = topWalls;
    }

    public final List<float[]> getBottomWalls() {
        return (bottomWalls);
    }

    public final void addBottomWall(float[] bottomWall) {
        bottomWalls.add(bottomWall);
    }

    public final void setBottomWalls(List<float[]> bottomWalls) {
        this.bottomWalls = bottomWalls;
    }

    public final List<float[]> getTopPortals() {
        return (topPortals);
    }

    public final void addTopPortal(float[] topPortal) {
        this.topPortals.add(topPortal);
    }

    public final void setTopPortals(List<float[]> topPortals) {
        this.topPortals = topPortals;
    }

    public final List<float[]> getBottomPortals() {
        return (bottomPortals);
    }

    public final void addBottomPortal(float[] bottomPortal) {
        bottomPortals.add(bottomPortal);
    }

    public final void setBottomPortals(List<float[]> bottomPortals) {
        this.bottomPortals = bottomPortals;
    }

    public final List<float[]> getLeftWalls() {
        return (leftWalls);
    }

    public final void addLeftWall(float[] leftWall) {
        leftWalls.add(leftWall);
    }

    public final void setLeftWalls(List<float[]> leftWalls) {
        this.leftWalls = leftWalls;
    }

    public final List<float[]> getRightWalls() {
        return (rightWalls);
    }

    public final void addRightWall(float[] rightWall) {
        rightWalls.add(rightWall);
    }

    public final void setRightWalls(List<float[]> rightWalls) {
        this.rightWalls = rightWalls;
    }

    public final List<float[]> getLeftPortals() {
        return (leftPortals);
    }

    public final void addLeftPortal(float[] leftPortal) {
        leftPortals.add(leftPortal);
    }

    public final void setLeftPortals(List<float[]> leftPortals) {
        this.leftPortals = leftPortals;
    }

    public final List<float[]> getRightPortals() {
        return (rightPortals);
    }

    public final void addRightPortal(float[] rightPortal) {
        rightPortals.add(rightPortal);
    }

    public final void setRightPortals(List<float[]> rightPortals) {
        this.rightPortals = rightPortals;
    }

    public final void setEnclosingRectangle(Rectangle enclosingRectangle) {
        this.enclosingRectangle = enclosingRectangle;
    }

    public final boolean contains(float[] point) {
        return (contains(point[0], point[1], point[2]));
    }

    public final boolean contains(float x, float y, float z) {
        return (this.enclosingRectangle.contains(x, z));
    }

    public final List<float[]> getCeilWalls() {
        return (ceilWalls);
    }

    public final void addCeilWall(float[] ceilWall) {
        ceilWalls.add(ceilWall);
    }

    public final void setCeilWalls(List<float[]> ceilWalls) {
        this.ceilWalls = ceilWalls;
    }

    public final List<float[]> getFloorWalls() {
        return (floorWalls);
    }

    public final void addFloorWall(float[] floorWall) {
        floorWalls.add(floorWall);
    }

    public final void setFloorWalls(List<float[]> floorWalls) {
        this.floorWalls = floorWalls;
    }

    public final void addPortal(Full3DPortal portal) {
        this.portalsList.add(portal);
    }

    public final List<Full3DPortal> getPortalsList() {
        return (portalsList);
    }

    /**
     * 
     * @param neighbourCell neighbor cell of this cell
     * @return portal that links this cell to the neighbor cell if any, 
     *         otherwise null
     */
    public final Full3DPortal getPortal(Full3DCell neighbourCell) {
        Full3DPortal portal = null;
        Full3DCell[] linkedCells;
        for (Full3DPortal currentPortal : portalsList) {
            linkedCells = currentPortal.getLinkedCells();
            if (neighbourCell == linkedCells[0] || neighbourCell == linkedCells[1]) {
                portal = currentPortal;
                break;
            }
        }
        return (portal);
    }

    public final Full3DPortal getPortal(int index) {
        return (portalsList.get(index));
    }

    public final int getNeighboursCount() {
        return (portalsList.size());
    }

    public final Full3DCell getNeighbourCell(int index) {
        Full3DCell[] linkedCells = portalsList.get(index).getLinkedCells();
        return (linkedCells[0] == this ? linkedCells[1] : linkedCells[0]);
    }

    public final List<float[]> getCeilPortals() {
        return (ceilPortals);
    }

    public final void addCeilPortal(float[] ceilPortal) {
        ceilPortals.add(ceilPortal);
    }

    public final void setCeilPortals(List<float[]> ceilPortals) {
        this.ceilPortals = ceilPortals;
    }

    public final List<float[]> getFloorPortals() {
        return (floorPortals);
    }

    public final void addFloorPortal(float[] floorPortal) {
        floorPortals.add(floorPortal);
    }

    public final void setFloorPortals(List<float[]> floorPortals) {
        this.floorPortals = floorPortals;
    }

    public final Full3DCellController getController() {
        return (controller);
    }

    public final void setController(Full3DCellController controller) {
        this.controller = controller;
    }
}
