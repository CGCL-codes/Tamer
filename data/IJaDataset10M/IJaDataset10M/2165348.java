package maltcms.datastructures.peak;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import maltcms.tools.ArrayTools;
import ucar.ma2.Array;

/**
 * Reprensents a singel snake.
 * 
 * @author Mathias Wilhelm(mwilhelm A T TechFak.Uni-Bielefeld.DE)
 */
public class PeakArea2D {

    private Point seedPoint;

    private List<Point> regionList;

    private List<Point> boundary;

    private FiFoQueue<Point> activeList;

    private Map<Point, Double> intensities;

    private Map<Integer, Boolean> pixelMap;

    private Map<Integer, Boolean> activeMap;

    private Map<Integer, Boolean> boundaryMap;

    private int maxHeight = 0;

    private int maxWidth = 0;

    private int scansPerModulation = 0;

    private int index = -1;

    private double areaIntensity = 0.0d;

    private double seedIntensity = 0.0d;

    private Map<Integer, Double> areaUniqueMassIntensities;

    private Array sumMS = null;

    private Array seedMS;

    private boolean merged = false;

    /**
	 * Default constructor.
	 * 
	 * @param seed
	 *            seed point
	 * @param ms
	 *            mass spectra of the seed point
	 * @param intensity
	 *            intensity of the seed point
	 * @param sindex
	 *            scan index of the seed point
	 * @param spm
	 *            scans per modulation is used to build an index to search
	 *            efficiently in current region and border list
	 */
    public PeakArea2D(final Point seed, final Array ms, final double intensity, final int sindex, final int spm) {
        init(seed, ms, intensity, sindex, spm);
    }

    private void init(final Point seed, final Array ms, final double intensity, final int sindex, final int spm) {
        this.regionList = new ArrayList<Point>();
        this.seedPoint = seed;
        this.boundary = new ArrayList<Point>();
        this.activeList = new FiFoQueue<Point>();
        this.pixelMap = new HashMap<Integer, Boolean>();
        this.activeMap = new HashMap<Integer, Boolean>();
        this.boundaryMap = new HashMap<Integer, Boolean>();
        this.seedMS = ms.copy();
        this.sumMS = ms.copy();
        this.seedIntensity = intensity;
        this.areaIntensity = intensity;
        this.scansPerModulation = spm;
        this.index = sindex;
        this.areaUniqueMassIntensities = new HashMap<Integer, Double>();
        this.intensities = new HashMap<Point, Double>();
    }

    public void clear() {
        this.regionList = new ArrayList<Point>();
        this.boundary = new ArrayList<Point>();
        this.activeList = new FiFoQueue<Point>();
        this.pixelMap = new HashMap<Integer, Boolean>();
        this.activeMap = new HashMap<Integer, Boolean>();
        this.boundaryMap = new HashMap<Integer, Boolean>();
        this.areaIntensity = 0;
        this.sumMS = this.seedMS.copy();
        this.areaUniqueMassIntensities = new HashMap<Integer, Double>();
        this.intensities = new HashMap<Point, Double>();
    }

    /**
	 * Check, if the point p is in the active list or not.
	 * 
	 * @param p
	 *            point
	 * @return <code>true</code> if p is in active point list, else
	 *         <code>false</code>
	 */
    private boolean activeContains(final Point p) {
        return this.activeMap.containsKey(getIndex(p));
    }

    /**
	 * Adds a point to list of active points.
	 * 
	 * @param p
	 *            point
	 */
    public void addActivePoint(final Point p) {
        if (!this.activeMap.containsKey(getIndex(p))) {
            this.activeList.add(p);
            this.activeMap.put(getIndex(p), true);
        }
    }

    /**
	 * Setter.
	 * 
	 * @param mass
	 *            mass
	 * @param value
	 *            area sum
	 */
    public void addAreaIntensity(final Integer mass, final Double value) {
        this.areaUniqueMassIntensities.put(mass, value);
    }

    /**
	 * Add a point to this list of boundaries.
	 * 
	 * @param p
	 *            boundary point
	 */
    public void addBoundaryPoint(final Point p) {
        this.boundary.add(p);
        this.boundaryMap.put(getIndex(p), true);
    }

    /**
	 * Adds all neighbours of p to the active point list.
	 * 
	 * @param p
	 *            center
	 * @return active list is not empty
	 */
    public boolean addNeighOf(final Point p) {
        final List<Point> neigh = getNeighbours(p, false);
        for (final Point np : neigh) {
            addActivePoint(np);
        }
        return (!this.activeList.isEmpty());
    }

    /**
	 * Will add the point p to the list.
	 * 
	 * @param point
	 *            point
	 * @param ms
	 *            mass spectra of this point
	 * @param intensity
	 *            intensity of this point
	 */
    public void addRegionPoint(final Point point, final Array ms, final double intensity) {
        this.regionList.add(point);
        this.pixelMap.put(getIndex(point), true);
        final int x = point.x;
        final int y = point.y;
        if (Math.abs(this.seedPoint.x - x) > this.maxWidth) {
            this.maxWidth = Math.abs(this.seedPoint.x - x);
        }
        if (Math.abs(this.seedPoint.y - y) > this.maxHeight) {
            this.maxHeight = Math.abs(this.seedPoint.y - y);
        }
        this.sumMS = ArrayTools.sum(this.sumMS, ms);
        this.areaIntensity += intensity;
        this.intensities.put(point, intensity);
    }

    /**
	 * Checks if the point p is in the boundary list of this snake.
	 * 
	 * @param p
	 *            point
	 * @return <code>true</code> if p is in boundary list, else
	 *         <code>false</code>
	 */
    private boolean boundaryContains(final Point p) {
        return this.boundaryMap.containsKey(getIndex(p));
    }

    /**
	 * Getter.
	 * 
	 * @return sum of area unique mass intensitites
	 */
    public Map<Integer, Double> getAreaIntensities() {
        return this.areaUniqueMassIntensities;
    }

    /**
	 * Getter.
	 * 
	 * @return sum of intensities over the peak area
	 */
    public double getAreaIntensity() {
        return this.areaIntensity;
    }

    /**
	 * Getter. ATTENTION: This method will clear the current boundary list.
	 * 
	 * @return a list of all boundary points
	 */
    public List<Point> getBoundaryPoints() {
        final List<Point> copyList = new ArrayList<Point>(this.boundary);
        this.boundary = new ArrayList<Point>();
        this.boundaryMap = new HashMap<Integer, Boolean>();
        return copyList;
    }

    /**
	 * Getter. This method will not clear the current boundary list.
	 * 
	 * @return a list of all boundary points
	 */
    public List<Point> getBoundaryPointsCopy() {
        return new ArrayList<Point>(this.boundary);
    }

    /**
	 * Getter.
	 * 
	 * @return size of boundary point list
	 */
    public int getBoundaryPointsSize() {
        return this.boundary.size();
    }

    /**
	 * Getter.
	 * 
	 * @return scan index of the seed point
	 */
    public int getIndex() {
        return this.index;
    }

    /**
	 * Index mapping from NxN to N.
	 * 
	 * @param p
	 *            point
	 * @return index
	 */
    private int getIndex(final Point p) {
        return (this.scansPerModulation * p.x + p.y);
    }

    /**
	 * Getter.
	 * 
	 * @return maximum height difference between seed and area points
	 */
    public int getMaxHeight() {
        return this.maxHeight;
    }

    /**
	 * Getter.
	 * 
	 * @return maximum width between seed point and area points
	 */
    public int getMaxWidth() {
        return this.maxWidth;
    }

    /**
	 * Getter.
	 * 
	 * @return mean mass spectra of this peak area
	 */
    public Array getMeanMS() {
        return ArrayTools.mult(this.sumMS, this.regionList.size() + 1);
    }

    /**
	 * Getter.
	 * 
	 * @param p
	 *            center point
	 * @param allNeighbours
	 *            <code>true</code> if all neighbours should be returned, else
	 *            <code>false</code>
	 * @return list of points. ATTENTION: Could contain point which are out of
	 *         bounds.
	 */
    private List<Point> getNeighbours(final Point p, final boolean allNeighbours) {
        final List<Point> neigh = new ArrayList<Point>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                final Point np = new Point(p.x + i, p.y + j);
                if (allNeighbours) {
                    neigh.add(np);
                } else {
                    if (!activeContains(np) && !regionContains(np) && !boundaryContains(np) && !this.seedPoint.equals(np)) {
                        neigh.add(np);
                    }
                }
            }
        }
        return neigh;
    }

    /**
	 * Getter.
	 * 
	 * @return list
	 */
    public List<Point> getRegionPoints() {
        return new ArrayList<Point>(this.regionList);
    }

    /**
	 * Getter.
	 * 
	 * @return seed intensity
	 */
    public double getSeedIntensity() {
        return this.seedIntensity;
    }

    /**
	 * Getter.
	 * 
	 * @return seed mass spectra
	 */
    public Array getSeedMS() {
        return this.seedMS;
    }

    /**
	 * Getter.
	 * 
	 * @return sedd point
	 */
    public Point getSeedPoint() {
        return this.seedPoint;
    }

    /**
	 * Checks, if this snake has an not empty list of active points.
	 * 
	 * @return <code>true</code> is the active point list is not empty, else
	 *         <code>false</code>
	 */
    public boolean hasActivePoints() {
        return !this.activeList.isEmpty();
    }

    /**
	 * Pops the first active point in the active list.
	 * 
	 * @return first point
	 */
    public Point popActivePoint() {
        if (!this.activeList.isEmpty()) {
            final Point p = this.activeList.poll();
            this.activeMap.remove(getIndex(p));
            return p;
        } else {
            return null;
        }
    }

    /**
	 * Contains.
	 * 
	 * @param point
	 *            point
	 * @return true if yes, else false
	 */
    public boolean regionContains(final Point point) {
        final boolean pixelMapContainsPoint = this.pixelMap.containsKey(getIndex(point));
        final boolean seedPointIsPoint = (point.x == this.seedPoint.x) && (point.y == this.seedPoint.y);
        if (pixelMapContainsPoint || seedPointIsPoint) {
            return true;
        }
        return false;
    }

    public void setSeedPoint(Point p) {
        this.seedPoint = p;
    }

    /**
	 * Getter.
	 * 
	 * @return size of the peak area
	 */
    public int size() {
        return this.regionList.size();
    }

    public void findAndSetBoundary() {
        for (Point p : this.regionList) {
            for (Point b : getNeighbours(p, false)) {
                addBoundaryPoint(b);
            }
        }
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public boolean isMerged() {
        return this.merged;
    }

    @Override
    public String toString() {
        return this.hashCode() + ": " + seedPoint.x + "," + seedPoint.y;
    }

    public void normalizeTo(Peak2D reference) {
        this.areaIntensity /= reference.getPeakArea().getAreaIntensity();
    }
}
