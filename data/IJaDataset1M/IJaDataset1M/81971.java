package source.model;

import java.util.Vector;
import source.view.MainScreen;

/**
 * IdList class is used to store ID's for the OwnershipMap and PositionMap class.
 * @author Joe
 *
 */
public class IdList {

    Vector<ObjectID> ids;

    IdList() {
        ids = new Vector<ObjectID>();
    }

    public boolean add(ObjectID id) {
        if (isIn(id)) return false;
        return ids.add(id);
    }

    public boolean remove(ObjectID id) {
        return ids.remove(id);
    }

    private boolean isIn(ObjectID id) {
        return ids.contains(id);
    }

    public ObjectID[] getIds() {
        ObjectID[] oids = new ObjectID[ids.size()];
        int i = 0;
        for (ObjectID oid : ids) {
            oids[i] = oid;
            i++;
        }
        return oids;
    }
}
