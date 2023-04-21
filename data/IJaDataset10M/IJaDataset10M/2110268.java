package feynman.framework.system;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

/**
 * A <b>ObjectSystem</b> is a collection of <b>Objects</b> that are being studied.
 * This Interface describes the operations that can be performed on the system.
 *
 * @author Wes Bailey
 * @version $Revision: 1.1.1.1 $ $Date: 2002/11/12 02:25:43 $
 */
public abstract class PhysicalSystem {

    Set system = new HashSet();

    PhysicalSystem() {
    }

    /**
	 * Use this method to query if a Object is part of the system.
	 */
    public boolean contains(Object item) {
        if (system.contains(item)) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Override this method to add an object of your defined type to the system.
	 */
    public abstract boolean add(Object item);

    /**
	 * Use this method to remove a Object from the system.
	 */
    public boolean remove(Object item) {
        if (system.remove(item)) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Use this method to determine the number of Objects in the system.
	 */
    public int size() {
        return system.size();
    }

    /**
	 * Use this method to query the system to see if there are any objects in it.
	 */
    public boolean isEmpty() {
        return system.isEmpty();
    }

    /**
	 * Use this method to get and iterator over all objects in the system.
	 * <p/>
	 * <b>NOTE: </b> This method is preferred to using and array or other means to accomplish
	 * the task of looping through the objects in the system.
	 */
    public Iterator iterator() {
        return system.iterator();
    }

    /**
	 * Use this method to get an array that contains all of the objects in the system.
	 * <p/>
	 * <b>NOTE: </b> In the instance of studying N particle systems where the number of
	 * calculation is proportional to N2, then use this method along with the following
	 * to get a factor of 2 reduction in the number of force calculations that need to 
	 * be performed.
	 * <p/>
	 * <pre>
	 * Object pscopy[] = ps.toArray();
	 * for (int i=0; i < pscopy.length-1; i++) {
     *     ...
	 *     for (int j=i+1; j < pscopy.length; j++) {
	 *	       ...
	 *     }
	 * }
	 * </pre>
	 */
    public Object[] toArray() {
        return system.toArray();
    }
}
