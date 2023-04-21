package org.nakedobjects.nof.testsystem;

import org.nakedobjects.noa.NakedObjectRuntimeException;
import org.nakedobjects.noa.adapter.EntryTextParser;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.Oid;
import org.nakedobjects.noa.adapter.ResolveState;
import org.nakedobjects.noa.persist.IdentityMap;
import org.nakedobjects.noa.util.DebugString;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.core.util.Assert;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class TestProxyIdentityMap implements IdentityMap {

    private final Hashtable identities = new Hashtable();

    private final Hashtable objectAdapters = new Hashtable();

    private final Hashtable collectionAdapters = new Hashtable();

    private final Hashtable recreatedPersistent = new Hashtable();

    private final Vector recreatedTransient = new Vector();

    private final Hashtable valueAdapters = new Hashtable();

    public void addAdapter(final Object object, final NakedObject nakedobject) {
        if (nakedobject.getSpecification().isCollection()) {
            collectionAdapters.put(object, nakedobject);
        } else {
            objectAdapters.put(object, nakedobject);
        }
    }

    public void addAdapter(final Object object, final EntryTextParser value) {
        valueAdapters.put(object, value);
    }

    public void addIdentity(final Oid oid, final NakedObject adapter) {
        identities.put(oid, adapter);
    }

    public void addRecreated(final Oid oid, final NakedObject adapter) {
        recreatedPersistent.put(oid, adapter);
    }

    public void addRecreatedTransient(final NakedObject adapter) {
        recreatedTransient.addElement(adapter);
    }

    public NakedObject getAdapterFor(final Object object) {
        return (NakedObject) objectAdapters.get(object);
    }

    public NakedObject getAdapterFor(final Oid oid) {
        processChangedOid(oid);
        final NakedObject no = (NakedObject) identities.get(oid);
        return no;
    }

    public Enumeration getIdentifiedObjects() {
        throw new NakedObjectRuntimeException();
    }

    public void init() {
    }

    public void initDomainObject(final Object domainObject) {
    }

    public boolean isIdentityKnown(final Oid oid) {
        return identities.containsKey(oid);
    }

    public void madePersistent(final NakedObject adapter) {
        final Oid oid = adapter.getOid();
        identities.remove(oid);
        NakedObjectsContext.getObjectPersistor().convertTransientToPersistentOid(oid);
        Assert.assertTrue("Adapter's pojo should exist in pojo map and return the adapter", objectAdapters.get(adapter.getObject()) == adapter);
        Assert.assertNull("Changed OID should not already map to a known adapter " + oid, identities.get(oid));
        identities.put(oid, adapter);
        adapter.changeState(ResolveState.RESOLVED);
    }

    private void processChangedOid(final Oid oid) {
        Assert.assertNotNull("No OID", oid);
        if (oid.hasPrevious()) {
            final Oid previous = oid.getPrevious();
            final NakedObject object = (NakedObject) identities.get(previous);
            if (object != null) {
                identities.remove(previous);
                final Oid oidFromObject = object.getOid();
                oidFromObject.copyFrom(oid);
                identities.put(oidFromObject, object);
            }
        }
    }

    public void reset() {
        identities.clear();
        objectAdapters.clear();
        recreatedPersistent.clear();
        recreatedTransient.clear();
        valueAdapters.clear();
    }

    public void shutdown() {
    }

    public void unloaded(final NakedObject object) {
        throw new NakedObjectRuntimeException();
    }

    public void addAdapter(final NakedObject object) {
        identities.put(object.getOid(), object);
        objectAdapters.put(object.getObject(), object);
    }

    public void debug(final DebugString debug) {
    }
}
