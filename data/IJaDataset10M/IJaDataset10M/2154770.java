package rescuecore;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
   This is an implementation of Memory that stores the data in a hashtable
 */
public class HashMemory extends Memory {

    protected Map data;

    /**
	   Construct a new empty memory
	*/
    public HashMemory() {
        data = new HashMap();
    }

    public Memory copy() {
        HashMemory m = new HashMemory();
        Iterator it = data.values().iterator();
        while (it.hasNext()) {
            RescueObject o = (RescueObject) it.next();
            m.add(o.copy(), 0, RescueConstants.SOURCE_UNKNOWN);
        }
        return m;
    }

    public RescueObject lookup(int id) {
        return (RescueObject) data.get(new Integer(id));
    }

    public Collection<RescueObject> getAllObjects() {
        return new HashSet<RescueObject>(data.values());
    }

    public void getObjectsOfType(Collection<RescueObject> result, int... types) {
        for (Iterator it = data.values().iterator(); it.hasNext(); ) {
            RescueObject next = (RescueObject) it.next();
            int type = next.getType();
            for (int nextType : types) {
                if (type == nextType) {
                    result.add(next);
                    break;
                }
            }
        }
    }

    public void add(RescueObject o, int timestamp, Object source) {
        data.put(new Integer(o.getID()), o);
        super.add(o, timestamp, source);
    }

    public void remove(RescueObject o) {
        data.remove(new Integer(o.getID()));
        super.remove(o);
    }
}
