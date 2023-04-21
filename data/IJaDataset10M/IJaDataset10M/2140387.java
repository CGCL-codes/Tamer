package homura.hde.core.scene.intersection;

import homura.hde.core.scene.batch.GeomBatch;
import homura.hde.util.maths.Ray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * <code>PickResults</code> contains information resulting from a pick test.
 * The results will contain a list of every node that was "struck" during a
 * pick test. Distance can be used to order the results. If <code>checkDistance</code> 
 * is set, objects will be ordered with the first element in the list being the
 * closest picked object.
 *
 * @author Mark Powell
 * @version $Id: PickResults.java,v 1.14 2007/08/27 09:18:48 irrisor Exp $
 */
public abstract class PickResults {

    private ArrayList<PickData> nodeList;

    private boolean checkDistance;

    private DistanceComparator distanceCompare;

    /**
     * Constructor instantiates a new <code>PickResults</code> object.
     */
    public PickResults() {
        nodeList = new ArrayList<PickData>();
    }

    /**
     * remember modification of the list to allow sorting after all picks have been added - not each time.
     */
    private boolean modified = false;

    /**
     * <code>addGeometry</code> places a new <code>Geometry</code> spatial into the
     * results list.
     *
     * @param data the geometry to be placed in the results list.
     */
    public void addPickData(PickData data) {
        nodeList.add(data);
        modified = true;
    }

    /**
     * <code>getNumber</code> retrieves the number of geometries that have been
     * placed in the results.
     *
     * @return the number of Geometry objects in the list.
     */
    public int getNumber() {
        return nodeList.size();
    }

    /**
     * <code>getGeometry</code> retrieves a Geometry from a specific index.
     *
     * @param i the index requested.
     * @return the Geometry at the specified index.
     */
    public PickData getPickData(int i) {
        if (modified) {
            if (checkDistance) {
                Collections.sort(nodeList, distanceCompare);
            }
            modified = false;
        }
        return nodeList.get(i);
    }

    /**
     * <code>clear</code> clears the list of all Geometry objects.
     */
    public void clear() {
        nodeList.clear();
    }

    /**
     * <code>addPick</code> generates an entry to be added to the list
     * of picked objects. If checkDistance is true, the implementing class
     * should order the object.
     * @param ray the ray that was cast for the pick calculation.
     * @param s the object to add to the pick data.
     */
    public abstract void addPick(Ray ray, GeomBatch s);

    /**
     * Optional method that can be implemented by sub classes to define 
     * methods for handling picked objects. After calculating all pick results
     * this method is called.
     *
     */
    public abstract void processPick();

    /**
     * Reports if these pick results will order the data by distance from the
     * origin of the Ray.
     * @return true if objects will be ordered by distance, false otherwise.
     */
    public boolean willCheckDistance() {
        return checkDistance;
    }

    /**
     * Sets if these pick results will order the data by distance from the origin
     * of the Ray.
     * @param checkDistance true if objects will be ordered by distance, false otherwise.
     */
    public void setCheckDistance(boolean checkDistance) {
        this.checkDistance = checkDistance;
        if (checkDistance) {
            distanceCompare = new DistanceComparator();
        }
    }

    /**
     * Implementation of comparator that uses the distance set in the pick data
     * to order the objects.
     * @author mpowell
     *
     */
    private class DistanceComparator implements Comparator<PickData> {

        public int compare(PickData o1, PickData o2) {
            if (o1.getDistance() <= o2.getDistance()) return -1;
            return 1;
        }
    }
}
