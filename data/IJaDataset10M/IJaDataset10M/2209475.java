package org.gvsig.fmap.algorithm.triangulation.pirol;

import java.util.ArrayList;
import java.util.Iterator;
import org.gvsig.fmap.algorithm.triangulation.pirol.apiTools.CollectionsTools;

/**
 * Class to descibe a point that is part of a Delaunay graph. Holds information
 * on which points it builds what triangles using which lines with. This class
 * is from a time when we were not sure if we should use german or english
 * language in our classes - this one is german as you see... Sorry about that!
 * 
 * <pre>
 * german,         english:
 * -----------------------------
 * Punkt           point
 * Nachbar (von)   neighbor (of)
 * Kante           line
 * Dreieck         triangle
 * geh�rt zu       is part of
 * </pre>
 * 
 * @author Ole Rahn <br>
 *         <br>
 *         FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 *         <br>
 *         Project: PIROL (2005), <br>
 *         Subproject: Daten- und Wissensmanagement
 * 
 * @version $Rev: 2446 $
 */
public class DelaunayPunkt extends PirolPoint {

    protected ArrayList nachbarVon = new ArrayList();

    protected DelaunayPunkt[] nachbarVonArray = null;

    protected ArrayList bildetKanten = new ArrayList();

    protected PirolEdge[] bildetKantenArray = null;

    protected ArrayList belongsToDreieck = new ArrayList();

    protected Integer[] gehoertZuDreieckArray = null;

    protected boolean wasCompiled = false;

    public DelaunayPunkt() {
        super();
    }

    public DelaunayPunkt(double[] coords, int index) {
        super(coords, index);
    }

    public DelaunayPunkt(double[] coords, int index, ScaleChanger scaler) {
        super(coords, index, scaler);
    }

    public DelaunayPunkt(double[] coords, int index, ScaleChanger scaler, boolean prescaled) {
        super(coords, index, scaler, prescaled);
    }

    /**
	 * Turns the "STL" datatypes into more efficient low-level arrays -->
	 * faster. After this method was invoked, you can not add further neighbors,
	 * lines this points is a part of, etc. Invoke if the points don't need to
	 * be altered anymore.
	 */
    public void compile() {
        if (!this.wasCompiled) {
            this.nachbarVonArray = (DelaunayPunkt[]) this.nachbarVon.toArray(new DelaunayPunkt[0]);
            this.bildetKantenArray = (PirolEdge[]) this.bildetKanten.toArray(new PirolEdge[0]);
            this.gehoertZuDreieckArray = (Integer[]) this.belongsToDreieck.toArray(new Integer[0]);
            this.wasCompiled = true;
        }
    }

    /**
	 * @return a list of DelaunayPunkt objects that are part of triangles
	 *         together with this one
	 */
    public ArrayList getNeighbours() {
        if (!this.wasCompiled) {
            return nachbarVon;
        }
        ArrayList list = new ArrayList();
        CollectionsTools.addArrayToList(list, this.nachbarVonArray);
        return list;
    }

    /**
	 * @return an array of DelaunayPunkt objects that are part of triangles
	 *         together with this one
	 */
    public DelaunayPunkt[] getNeighboursArray() {
        if (!this.wasCompiled) {
            return (DelaunayPunkt[]) nachbarVon.toArray(new DelaunayPunkt[0]);
        }
        return this.nachbarVonArray;
    }

    /**
	 * register a line to connect this point with an other one (to build a
	 * triangle)
	 * 
	 * @param k
	 *            the line to be registered
	 */
    public void attendsEdge(PirolEdge k) {
        if (!this.wasCompiled && !this.bildetKanten.contains(k)) {
            this.bildetKanten.add(k);
        } else if (this.wasCompiled) throw new RuntimeException("this object was compiled already - can not add nay lines anymore");
    }

    /**
	 * get a list of lines (beeing parts of triangles) this point is part of
	 * 
	 * @return list of lines
	 * @see PirolEdge
	 */
    public ArrayList getBelongingEdges() {
        return bildetKanten;
    }

    /**
	 * get an array of lines (beeing parts of triangles) this point is part of
	 * 
	 * @return list of lines
	 * @see PirolEdge
	 */
    public PirolEdge[] getBildetKantenArray() {
        if (!this.wasCompiled) return (PirolEdge[]) bildetKanten.toArray(new PirolEdge[0]);
        return this.bildetKantenArray;
    }

    /**
	 * register this point to part of the triangle with the given index
	 * 
	 * @param nrDreieck
	 *            triangle index
	 */
    public void setAttendsTriangle(int nrDreieck) {
        if (!this.belongsTo(nrDreieck) && !this.wasCompiled) this.belongsToDreieck.add(new Integer(nrDreieck)); else if (this.wasCompiled) throw new RuntimeException("can not do this after this point was compiled");
    }

    /**
	 * @param dreieckTriangle
	 *            index of a triangle
	 * @return true if this point is part of the triangle with the given index,
	 *         false if not
	 */
    protected boolean belongsTo(int dreieckTriangle) {
        Iterator iter = this.belongsToDreieck.iterator();
        int nummer;
        while (iter.hasNext()) {
            nummer = ((Integer) iter.next()).intValue();
            if (nummer == dreieckTriangle) return true;
        }
        return false;
    }

    public boolean attendsTriangleTogetherWith(DelaunayPunkt p1, DelaunayPunkt p2) {
        Integer[] triangleIndices = null;
        int nummer;
        if (!this.wasCompiled) triangleIndices = (Integer[]) this.belongsToDreieck.toArray(new Integer[0]); else triangleIndices = this.gehoertZuDreieckArray;
        for (int i = 0; i < triangleIndices.length; i++) {
            nummer = triangleIndices[i].intValue();
            if (p1.belongsTo(nummer) && p2.belongsTo(nummer)) return true;
        }
        return false;
    }

    protected boolean gehoertZuMehrAlsEinemDreieckMit(DelaunayPunkt p) {
        Integer[] triangleIndices = null;
        if (!this.wasCompiled) triangleIndices = (Integer[]) this.belongsToDreieck.toArray(new Integer[0]); else triangleIndices = this.gehoertZuDreieckArray;
        int nummer;
        int zaehler = 0;
        for (int i = 0; i < triangleIndices.length; i++) {
            nummer = triangleIndices[i].intValue();
            if (p.belongsTo(nummer)) zaehler++;
        }
        if (zaehler < 2) return false;
        return true;
    }

    public void setNeighborOf(PirolPoint p) {
        if (!this.wasCompiled) this.nachbarVon.add(p); else throw new RuntimeException("this point was already compiled, can not add further neighbors!");
    }

    public boolean isNeighborOf(PirolPoint p) {
        if (!this.wasCompiled) return this.nachbarVon.contains(p);
        int numNeighbors = this.nachbarVonArray.length;
        DelaunayPunkt pkt;
        for (int i = 0; i < numNeighbors; i++) {
            pkt = this.nachbarVonArray[i];
            if (pkt.equals(p)) return true;
        }
        return false;
    }

    public boolean istNachbarVon(int pIndex) {
        if (!this.wasCompiled) {
            Iterator iter = this.nachbarVon.iterator();
            while (iter.hasNext()) {
                if (((PirolPoint) iter.next()).getIndex() == pIndex) return true;
            }
        } else {
            int numNeighbors = this.nachbarVonArray.length;
            DelaunayPunkt pkt;
            for (int i = 0; i < numNeighbors; i++) {
                pkt = this.nachbarVonArray[i];
                if (pkt.getIndex() == pIndex) return true;
            }
        }
        return false;
    }

    /**
	 * get the other 2 points creating the triangle with the given index
	 * together with this one
	 * 
	 * @param nr
	 *            index of a triangle
	 * @return the other 2 points, or null if this point is not part of the
	 *         indexed triangle
	 */
    public DelaunayPunkt[] getTheOther2PointsFromDreieckTriangle(int nr) {
        if (!this.belongsTo(nr)) return null;
        DelaunayPunkt[] result = new DelaunayPunkt[] { null, null };
        DelaunayPunkt[] pointsArray;
        if (!this.wasCompiled) pointsArray = (DelaunayPunkt[]) this.nachbarVon.toArray(new DelaunayPunkt[0]); else pointsArray = this.nachbarVonArray;
        int numNeighbors = pointsArray.length;
        int arrayInd = 0;
        DelaunayPunkt pkt;
        for (int i = 0; i < numNeighbors; i++) {
            pkt = pointsArray[i];
            if (pkt.belongsTo(nr)) {
                result[arrayInd] = pkt;
                arrayInd++;
                if (arrayInd >= 2) break;
            }
        }
        return result;
    }

    /**
	 * @return a list of indices (Integer) of triangles, this point is a part of
	 */
    public ArrayList getGehoertZuDreieck() {
        return belongsToDreieck;
    }

    /**
	 * @return an array of indices (Integer) of triangles, this point is a part
	 *         of
	 */
    public Integer[] getDreieckTrianglesBelongingToArray() {
        if (!this.wasCompiled) return (Integer[]) belongsToDreieck.toArray(new Integer[0]);
        return this.gehoertZuDreieckArray;
    }

    /**
	 * Method to create a deep copy of the given punkt object, that doesn't
	 * share any references with the original.<br>
	 * Attention: for DelaunayPunkt objects, the ArrayLists will be cloned, but
	 * their item will not be cloned, because - at least for Kante objects - you
	 * will need the same objects in different points, so be alert!
	 * 
	 * @param pkt
	 *            the DelaunayPunkt object to clone
	 * @return copy of the given DelaunayPunkt object
	 * @since 1.8
	 */
    public static DelaunayPunkt clone(DelaunayPunkt pkt) {
        return DelaunayPunkt.clone(pkt, pkt.getDimension());
    }

    /**
	 * Method to create a deep copy of the given punkt object (and by doing this
	 * changing it's dimension to the given one), that doesn't share any
	 * references with the original.<br>
	 * Attention: for DelaunayPunkt objects, the ArrayLists will be cloned, but
	 * their item will not be cloned, because - at least for Kante objects - you
	 * will need the same objects in different points, so be alert!
	 * 
	 * @param pkt
	 *            the DelaunayPunkt object to clone
	 * @param dimension
	 *            the dimension you want in the copy
	 * @return copy of the given DelaunayPunkt object
	 * @since 1.8
	 */
    public static DelaunayPunkt clone(DelaunayPunkt pkt, int dimension) {
        double[] newCoords = new double[dimension];
        for (int i = 0; i < Math.min(pkt.getDimension(), dimension); i++) {
            try {
                newCoords[i] = pkt.getCoordinate(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DelaunayPunkt cloned = new DelaunayPunkt(newCoords, pkt.index);
        cloned.setMarked(pkt.isMarked());
        cloned.setScaler(pkt.scaler);
        cloned.setSortFor(pkt.sortFor);
        cloned.bildetKanten = (ArrayList) pkt.bildetKanten.clone();
        cloned.belongsToDreieck = (ArrayList) pkt.belongsToDreieck.clone();
        cloned.nachbarVon = (ArrayList) pkt.nachbarVon.clone();
        if (pkt.wasCompiled) cloned.compile();
        return cloned;
    }

    /**
	 * non-static version of clone()
	 * 
	 * @return deep copy of this point
	 * @since 1.8
	 */
    public DelaunayPunkt cloneDelaunayPunkt() {
        return DelaunayPunkt.clone(this);
    }
}
