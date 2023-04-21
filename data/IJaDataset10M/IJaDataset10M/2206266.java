package backend.model.sketch;

import java.util.Iterator;
import java.util.LinkedList;
import javax.media.opengl.GL;
import backend.adt.Point2D;
import backend.geometry.Geometry2D;
import backend.global.AvoColors;
import backend.model.CSG.CSG_Face;
import backend.model.CSG.CSG_Polygon;
import backend.model.CSG.CSG_Vertex;

public class Region2D {

    private Prim2DCycle prim2DCycle = new Prim2DCycle();

    private CSG_Face csgFace = null;

    private LinkedList<Region2D> regionsToCut = new LinkedList<Region2D>();

    private boolean isSelected = false;

    private boolean isMousedOver = false;

    /**
	 * create a new 2D region defined by a valid Prim2DCycle.
	 * @param cycle a valid prim2DCycle
	 */
    public Region2D(Prim2DCycle cycle) {
        if (cycle != null && cycle.isValidCycle()) {
            this.prim2DCycle = cycle;
            this.csgFace = createCSG_Face();
        } else {
            System.out.println("Region2D(Constructor): Tried to construct a Region2D with an invalid cycle!");
        }
    }

    public LinkedList<Region2D> getRegionsToCut() {
        return regionsToCut;
    }

    public Prim2DCycle getPrims() {
        return prim2DCycle;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setMousedOver(boolean mouseIsOver) {
        this.isMousedOver = mouseIsOver;
    }

    /**
	 * get the total region area ("cut" regions are ignored).  
	 * @return the area of this region.
	 */
    public double getRegionArea() {
        if (csgFace == null) {
            return Double.MAX_VALUE;
        }
        return csgFace.getArea();
    }

    /**
	 * get the total area of the closed region minus the 
	 * area of all "cut" regions.
	 * @return the area of this region minus "cut" regions.
	 */
    public double getRegionAreaAfterCuts() {
        double subArea = 0.0;
        for (Region2D subReg : regionsToCut) {
            subArea += subReg.getRegionArea();
        }
        return getRegionArea() - subArea;
    }

    /**
	 * check to see if the given point is inside this Region.
	 * This is done by checking if the vertex is contained in
	 * any of the derived CSG_Face's convex polygons. vertices that 
	 * fall exactly on the edge are <em>not</em> considered to 
	 * be <em>inside</em> the Face.
	 * @param vert the CSG_Vertex to check
	 * @return true iff vertex is inside the face.
	 */
    public boolean regionContainsPoint2D(Point2D pt) {
        if (!this.csgFace.vertexIsInsideFace(new CSG_Vertex(pt, 0.0))) {
            return false;
        }
        Iterator<Region2D> iterReg = regionsToCut.iterator();
        while (iterReg.hasNext()) {
            Region2D subtractedRegion = iterReg.next();
            if (subtractedRegion.regionContainsPoint2D(pt)) {
                return false;
            }
        }
        return true;
    }

    /**
	 * generate and return a list of triangles that can be 
	 * used to fill the region when drawing or used otherwise
	 * to compute the total area of the region.
	 * @return a list of verticies (3*n) specifying triangles that comprise the region2D.
	 */
    public Point2DList getPoint2DListTriangles() {
        Point2DList p2DList = getPoint2DListEdges();
        if (prim2DCycle.size() == 3) {
            Point2D ptA = prim2DCycle.get(0).ptA;
            Point2D ptB = prim2DCycle.get(0).ptB;
            Point2D ptC = prim2DCycle.get(1).hasPtGetOther(ptB);
            if (prim2DCycle.isCCW()) {
                p2DList.add(new Point2D(ptA.getX(), ptA.getY()));
                p2DList.add(new Point2D(ptB.getX(), ptB.getY()));
                p2DList.add(new Point2D(ptC.getX(), ptC.getY()));
            } else {
                p2DList.add(new Point2D(ptC.getX(), ptC.getY()));
                p2DList.add(new Point2D(ptB.getX(), ptB.getY()));
                p2DList.add(new Point2D(ptA.getX(), ptA.getY()));
            }
        }
        return p2DList;
    }

    /**
	 * generate and return a list of point2D pairs that specify
	 * the region's outline. (no inner holes are represented here).
	 * @return a point2DList of the regions defining points, in pairs.
	 */
    public Point2DList getPoint2DListEdges() {
        Point2DList p2DList = new Point2DList();
        Point2D conPt = new Point2D(0.0, 0.0);
        if (prim2DCycle.size() > 0) {
            conPt = prim2DCycle.getFirst().ptA;
        }
        for (Prim2D prim : prim2DCycle) {
            p2DList.add(conPt);
            conPt = prim.hasPtGetOther(conPt);
            p2DList.add(conPt);
        }
        return p2DList;
    }

    /**
	 * generate and return a list of point2D quads that specify
	 * the region's outline. (a,b,b,a) for each line segment.
	 * @return a point2DList of the regions defining points, in quads.
	 */
    public Point2DList getPoint2DListEdgeQuad() {
        Point2DList p2DList = getPoint2DListEdges();
        Point2D conPt = new Point2D(0.0, 0.0);
        if (prim2DCycle.size() > 0) {
            conPt = prim2DCycle.getFirst().ptA;
        }
        for (Prim2D prim : prim2DCycle) {
            Point2D lastPt = conPt;
            p2DList.add(conPt);
            conPt = prim.hasPtGetOther(conPt);
            p2DList.add(conPt);
            p2DList.add(conPt);
            p2DList.add(lastPt);
        }
        return p2DList;
    }

    /**
	 * get the CSG_Face defined by this region.  Note that inner 
	 * regions are not cut from the face. If you want them removed 
	 * for a solid, construct a solid of the cut regions separately 
	 * and do a CSG boolean subtract operation.
	 * @return the CSG_Face defined by this region
	 */
    public CSG_Face getCSG_Face() {
        return csgFace;
    }

    /**
	 * get the list of points that defines the perimeter of this 
	 * region.  <b> This is an approximation subject to rounding/precision error!</b>
	 * @return
	 */
    public Point2DList getPeremeterPointList() {
        Point2DList pointList = new Point2DList();
        prim2DCycle.orientCycle();
        for (Prim2D prim : prim2DCycle) {
            pointList.addAll(prim.getVertexList(25));
            pointList.removeLast();
        }
        return pointList;
    }

    /**
	 * get the list of point2Dlists defining the perimeter of each region to cut from this region.
	 * @return the list of point2Dlists.  it may have a size==0 if there are no regions to cut.
	 */
    public LinkedList<Point2DList> getPeremeterPointListOfCutRegions() {
        LinkedList<Point2DList> allCutRegionPoints = new LinkedList<Point2DList>();
        for (Region2D cutReg : regionsToCut) {
            Point2DList newCutRegList = cutReg.getPeremeterPointList();
            if (newCutRegList != null && newCutRegList.size() > 0) {
                allCutRegionPoints.add(newCutRegList);
            } else {
                System.out.println(" ### found invalid regionToCut perimeter points when building via getPeremeterPointListOfCutRegion() !!");
            }
        }
        return allCutRegionPoints;
    }

    /**
	 * test to see if a particular region has already been added to 
	 * the list of "cut" regions for this region. (e.x., a drilled hole).
	 * @param cutRegion
	 * @return true iff region is in the list of regions to cut.
	 */
    public boolean regionHasBeenCut(Region2D cutRegion) {
        for (Region2D subReg : regionsToCut) {
            if (subReg.equals(cutRegion)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * tests to see if regionB is entirely within the 
	 * perimeter of this Region2D.  Edge points, in 
	 * this care, as still considered within.  There is
	 * no checking regarding subtractedRegions, so check
	 * for those separately if you care about that.
	 * @param regionB the region to test.
	 * @return true iff regionB is within this region.
	 */
    public boolean containsRegion(Region2D regionB) {
        if (this.equals(regionB)) {
            return true;
        }
        LinkedList<Point2D> pointsToTest = regionB.prim2DCycle.getPointList();
        for (Point2D pt : pointsToTest) {
            if (this.regionContainsPoint2D(pt)) {
                continue;
            }
            boolean ok = false;
            Iterator<Point2D> iter = this.prim2DCycle.getPointList().iterator();
            while (iter.hasNext()) {
                Point2D edgePoint = iter.next();
                if (edgePoint.equalsPt(pt)) {
                    ok = true;
                    break;
                }
            }
            if (ok) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
	 * test to see if this region and the region passed in share any common end points.
	 * @param regionB the region to compare this region with.
	 * @return true iff there is at least one end point in common.
	 */
    public boolean sharesAtLeastOneCommonPrimEndPoint(Region2D regionB) {
        for (Prim2D primA : prim2DCycle) {
            for (Prim2D primB : regionB.prim2DCycle) {
                if (primA.ptA.equalsPt(primB.ptA) || primA.ptA.equalsPt(primB.ptB) || primA.ptB.equalsPt(primB.ptA) || primA.ptB.equalsPt(primB.ptB)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * test to see if this region has the same prim2D points
	 * as the given region.  This can be used to quickly find
	 * duplicate regions (perhaps with just reversed or offset
	 * prim2D).
	 * @param regionB the region to compare to.
	 * @return true iff this region has the same defining points as the given region.
	 */
    public boolean hasSamePointsAsRegion(Region2D regionB) {
        if (this.prim2DCycle.size() != regionB.prim2DCycle.size()) {
            return false;
        }
        for (Prim2D prim : prim2DCycle) {
            if (!regionB.prim2DCycle.containsPt(prim.ptA) || !regionB.prim2DCycle.containsPt(prim.ptB) || !regionB.prim2DCycle.containsPt(prim.getCenterPtAlongPrim())) {
                return false;
            }
        }
        return true;
    }

    /**
	 * alter this region to have a hole cut in it that is
	 * defined by the given region.
	 * @param regionB the region2D to cut from this region.
	 */
    public void cutRegionFromRegion(Region2D regionB) {
        if (regionB != null) {
            regionsToCut.add(new Region2D(regionB.prim2DCycle));
            this.csgFace = createCSG_Face();
        } else {
            System.out.println(" ### cutRegionFromRegion was passed a null region (not doing anything). ");
        }
    }

    /** 
	 * find the closest distance from the vertices of this region2D
	 * to any edge/vertex of the given region.
	 * @param regionB the region to find the distance to.
	 * @return the closest distance.
	 */
    public double distanceFromVerticiesToRegion(Region2D regionB) {
        double distance = Double.MAX_VALUE;
        if (regionB == null) {
            System.out.println("** BAD NEWS! Null region passed to 'distanceFromVerticiesToRegion' **");
        }
        for (Point2D pt : this.prim2DCycle.getPointList()) {
            double d = regionB.prim2DCycle.getClosestDistanceToPoint(pt);
            if (d < distance) {
                distance = d;
            }
        }
        return distance;
    }

    /**
	 * create the CSG_Face used for Constructive Solid Geometry. 
	 * non-convex regions are "convexized." -- this can get a little tricky.
	 * @return the CSG_Face created, or null if face could not be created.
	 */
    private CSG_Face createCSG_Face() {
        CSG_Face face = null;
        Point2DList pointList = getPeremeterPointList();
        if (pointList.size() < 3) {
            System.out.println("Region2D(getCSG_Face): Invalid cycle.. Not enough points in list!  size=" + pointList.size());
            System.out.println("point0=" + pointList.get(0) + ", point1=" + pointList.get(1));
            return null;
        }
        Point2D lastLastPt = pointList.get(0);
        Point2D lastPt = pointList.get(1);
        int sub = 0;
        for (int i = 2; (i - sub) < pointList.size(); i++) {
            Point2D nextPt = pointList.get(i - sub);
            if (lastPt.equalsPt(nextPt)) {
                System.out.println("DUPLIACTE! {HACK!!}  -- i=" + i + ", " + nextPt);
                pointList.remove(i - sub);
                sub++;
                lastPt = pointList.get(i - sub - 1);
                nextPt = pointList.get(i - sub);
            } else {
                if (lastLastPt.equalsPt(nextPt)) {
                    System.out.println("ZERO REGION! {HACK!!}  -- i=" + i + ", " + nextPt);
                    pointList.remove(i - sub);
                    pointList.remove(i - sub - 1);
                    sub++;
                    sub++;
                    lastPt = pointList.get(i - sub - 1);
                    nextPt = pointList.get(i - sub);
                }
            }
            lastLastPt = lastPt;
            lastPt = nextPt;
        }
        double TOL = 1e-10;
        Point2DList polyPoints = new Point2DList();
        int index = 0;
        while (pointList.size() >= 3 && index < pointList.size()) {
            polyPoints.clear();
            int listSize = pointList.size();
            Point2D ptA = pointList.get(index % listSize);
            Point2D ptB = pointList.get((index + 1) % listSize);
            Point2D ptC = pointList.get((index + 2) % listSize);
            polyPoints.add(ptA);
            polyPoints.add(ptB);
            polyPoints.add(ptC);
            double angle = Geometry2D.threePtAngle(ptA, ptB, ptC);
            if (angle > TOL) {
                CSG_Polygon poly = new CSG_Polygon(new CSG_Vertex(ptA, 0.0), new CSG_Vertex(ptB, 0.0), new CSG_Vertex(ptC, 0.0));
                if (!polygonContainsPoints(poly, pointList, polyPoints)) {
                    int indexStart = index;
                    for (int i = index; i - indexStart < (listSize - 3); i++) {
                        Point2D ptD = pointList.get((i + 3) % listSize);
                        CSG_Vertex vertLastLast = poly.getVertAtModIndex(poly.getNumberVertices() - 2);
                        CSG_Vertex vertLast = poly.getVertAtModIndex(poly.getNumberVertices() - 1);
                        Point2D ptLastLast = new Point2D(vertLastLast.getX(), vertLastLast.getY());
                        Point2D ptLast = new Point2D(vertLast.getX(), vertLast.getY());
                        double angleA = Geometry2D.threePtAngle(ptLastLast, ptLast, ptD);
                        double angleB = Geometry2D.threePtAngle(ptLast, ptD, ptA);
                        double angleC = Geometry2D.threePtAngle(ptD, ptA, ptB);
                        if (angleA > TOL && angleB > TOL && angleC > TOL) {
                            CSG_Polygon polyCopy = poly.deepCopy();
                            polyCopy.addVertex(new CSG_Vertex(ptD, 0.0));
                            polyPoints.add(ptD);
                            if (!polygonContainsPoints(polyCopy, pointList, polyPoints)) {
                                poly.addVertex(new CSG_Vertex(ptD, 0.0));
                            } else {
                                polyPoints.removeLast();
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (face == null) {
                        face = new CSG_Face(poly);
                        LinkedList<CSG_Vertex> perimVerts = new LinkedList<CSG_Vertex>();
                        for (Point2D point : pointList) {
                            perimVerts.add(new CSG_Vertex(point, 0.0));
                        }
                    } else {
                        face.addPolygon(poly);
                    }
                    polyPoints.removeLast();
                    polyPoints.removeFirst();
                    for (Point2D p : polyPoints) {
                        pointList.remove(p);
                    }
                    index = 0;
                } else {
                    index++;
                }
            } else {
                index++;
            }
        }
        return face;
    }

    /**
	 * check to see if the polygon contains all of the points that 
	 * are in the pointList AND not in the invalidPoints.  this is 
	 * a specialized private method for helping out the "convexize" 
	 * algorithm.
	 * @param poly the CSG_Polygon
	 * @param pointList 
	 * @param invalidPoints
	 * @return true iff polygon contains all points in (pointList && NOT in invalidPoints).
	 */
    private boolean polygonContainsPoints(CSG_Polygon poly, Point2DList pointList, Point2DList invalidPoints) {
        boolean polyOverlapsOtherPoints = false;
        for (Point2D point : pointList) {
            boolean doNotTestPoint = false;
            for (Point2D ptInvalid : invalidPoints) {
                if (point.equalsPt(ptInvalid)) {
                    doNotTestPoint = true;
                    break;
                }
            }
            if (!doNotTestPoint && poly.vertexIsInsideOrOnEdgeOfPolygon(new CSG_Vertex(point, 0.0))) {
                polyOverlapsOtherPoints = true;
            }
        }
        return polyOverlapsOtherPoints;
    }

    /**
	 * draw the region. the state of selection, mouseover, 
	 * etc. will be handled automatically.
	 * @param gl
	 */
    public void glDrawRegion(GL gl) {
        if (isSelected) {
            if (isMousedOver) {
                float[] color = AvoColors.GL_COLOR4_2D_REG_SELMO;
                gl.glColor4f(color[0], color[1], color[2], color[3]);
            } else {
                float[] color = AvoColors.GL_COLOR4_2D_REG_SEL;
                gl.glColor4f(color[0], color[1], color[2], color[3]);
            }
        } else {
            if (isMousedOver) {
                float[] color = AvoColors.GL_COLOR4_2D_REG_UNSELMO;
                gl.glColor4f(color[0], color[1], color[2], color[3]);
            } else {
                float[] color = AvoColors.GL_COLOR4_2D_REG_UNSEL;
                gl.glColor4f(color[0], color[1], color[2], color[3]);
            }
        }
        if (csgFace != null) {
            Iterator<CSG_Polygon> polyIter = csgFace.getPolygonIterator();
            while (polyIter.hasNext()) {
                CSG_Polygon poly = polyIter.next();
                poly.glDrawPolygon(gl);
            }
        }
    }

    public String toString() {
        return "Region2D: area=" + getRegionAreaAfterCuts() + ", regions to cut: " + regionsToCut.size();
    }
}
