package org.ozoneDB.core.DbRemote;

import java.util.HashMap;
import java.util.Iterator;
import org.ozoneDB.OzoneProxy;
import org.ozoneDB.core.GarbageCollector;
import org.ozoneDB.core.ObjectID;

/**
    A gate for Proxy objects. Every Proxy which leaves the Database should wander
    through a ProxyObjectGate to be registered (so that the objects referenced by such a Proxy
    are known to be referenced and thus reachable).

    Every Proxy which left the Database, was registered and is now known to be unreachable should
    sign itself off exactly one time.

    @author <A HREF="http://www.medium.net/">Medium.net</A>
*/
public class ProxyObjectGate {

    /**
        This is a Mapping from {@link ObjectID} to Integer. It represents the
        count of references this client holds to the database object represented by the objectID.

        Entries have to be added when returning {@link OzoneProxy}s directly or indirectly.
        Entries have to be removed if a {#link OzoneProxy} is finalize()d on the client side.
        This way, the ozoneDB always know which objects are referenced and thus have to be
        considered reachable, even if they were not reachable internally.
    */
    protected HashMap objectsReferencesByClient;

    protected GarbageCollector garbageCollectorToBeNotifiedOfExportedReferences;

    /**
        This is an Integer which represents the number "1".
    */
    protected static final Integer one = new Integer(1);

    /**
        Creates a new ProxyObjectGate.
    */
    protected ProxyObjectGate() {
        objectsReferencesByClient = new HashMap();
    }

    public void addObjectReferencedByClient(OzoneProxy proxy) {
        if (proxy != null) {
            synchronized (objectsReferencesByClient) {
                ObjectID id = proxy.remoteID();
                if (garbageCollectorToBeNotifiedOfExportedReferences != null) {
                    garbageCollectorToBeNotifiedOfExportedReferences.notifyDatabaseObjectIsAboutToBeExported(id);
                }
                Object oldEntry = objectsReferencesByClient.put(id, one);
                if (oldEntry != null) {
                    objectsReferencesByClient.put(id, new Integer(((Integer) oldEntry).intValue() + 1));
                }
            }
        }
    }

    protected void removeObjectReferencedByClient(OzoneProxy proxy) {
        removeObjectReferencedByClient(proxy.remoteID());
    }

    protected void removeObjectReferencedByClient(ObjectID id) {
        synchronized (objectsReferencesByClient) {
            Object oldEntry = objectsReferencesByClient.remove(id);
            if (oldEntry != null) {
                if (oldEntry != one) {
                    int count = ((Integer) oldEntry).intValue();
                    count--;
                    if (count > 0) {
                        if (count > 1) {
                            objectsReferencesByClient.put(id, new Integer(count));
                        } else {
                            objectsReferencesByClient.put(id, one);
                        }
                    }
                } else {
                }
            } else {
            }
        }
    }

    /**
        Starts filtering references to database objects ({@link OzoneProxy}s) which
        are exported to the client.
        Every reference which is exported will be notified to the given GarbageCollector.
        Additionally, references which are known to be used by the client are notified to the
        given GarbageCollector within this call.
    */
    public void startFilterDatabaseObjectReferencesExports(GarbageCollector garbageCollector) {
        synchronized (objectsReferencesByClient) {
            this.garbageCollectorToBeNotifiedOfExportedReferences = garbageCollector;
            Iterator i = objectsReferencesByClient.keySet().iterator();
            while (i.hasNext()) {
                garbageCollector.notifyDatabaseObjectIsExported((ObjectID) i.next());
            }
        }
    }
}
