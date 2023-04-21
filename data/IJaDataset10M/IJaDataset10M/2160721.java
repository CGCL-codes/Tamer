package mimosa.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * This class implements a two way relation between objects.
 * 
 * @author Jean-Pierre Muller
 *
 */
public class TwoWayTable {

    private HashMap<Object, Object> convertTable = new HashMap<Object, Object>();

    /**
	 * The empty constructor.
	 */
    public TwoWayTable() {
        super();
    }

    /**
	 * Adds a conversion of an object into another.
	 * @param obj1 the first object
	 * @param obj2 the second object
	 */
    @SuppressWarnings("unchecked")
    public void addConversion(Object obj1, Object obj2) {
        Vector<Object> res1 = (Vector<Object>) convertTable.get(obj1);
        if (res1 == null) {
            res1 = new Vector<Object>();
            res1.add(obj2);
            convertTable.put(obj1, res1);
        } else {
            res1.add(obj2);
        }
        if (!obj1.equals(obj2)) {
            Vector<Object> res2 = (Vector<Object>) convertTable.get(obj2);
            if (res2 == null) {
                res2 = new Vector<Object>();
                res2.add(obj1);
                convertTable.put(obj2, res2);
            } else {
                res2.add(obj1);
            }
        }
    }

    /**
	 * Adds a conversion of an object into a set of others.
	 * @param obj the object
	 * @param objs the set of other objects
	 */
    @SuppressWarnings("unchecked")
    public void addConversion(Object obj, Collection<Object> objs) {
        Vector<Object> res1 = (Vector<Object>) convertTable.get(obj);
        if (res1 == null) {
            res1 = new Vector<Object>(10);
            res1.addAll(objs);
            convertTable.put(obj, res1);
        } else res1.addAll(objs);
        for (Iterator<Object> i = objs.iterator(); i.hasNext(); ) {
            Object obj2 = i.next();
            if (!obj.equals(obj2)) {
                Vector<Object> res2 = (Vector<Object>) convertTable.get(obj2);
                if (res2 == null) {
                    res2 = new Vector<Object>(10);
                    res2.add(obj);
                    convertTable.put(obj2, res2);
                } else res2.add(obj);
            }
        }
    }

    /**
	 * Removes a conversion of an object into another.
	 * @param obj1 the first object
	 * @param obj2 the second object
	 */
    @SuppressWarnings("unchecked")
    public void removeConversion(Object obj1, Object obj2) {
        Vector<Object> res1 = (Vector<Object>) convertTable.get(obj1);
        if (res1 != null) res1.remove(obj2);
        res1 = (Vector<Object>) convertTable.get(obj2);
        if (res1 != null) res1.remove(obj1);
    }

    /**
	 * Converts an object into a set of other.
	 * @param obj the object to convert
	 * @return the collection of related objects
	 */
    @SuppressWarnings("unchecked")
    public Vector<Object> convert(Object obj) {
        return (Vector<Object>) convertTable.get(obj);
    }

    /**
	 * Converts an object into another.
	 * @param obj the object to convert
	 */
    @SuppressWarnings("unchecked")
    public Object convertOne(Object obj) throws NotUniqueException {
        Vector<Object> res = (Vector<Object>) convertTable.get(obj);
        if (res.size() != 1) throw new NotUniqueException(); else return res.get(0);
    }

    /**
	 * @return the collection of keys.
	 */
    public Collection<Object> getKeys() {
        return convertTable.keySet();
    }
}
