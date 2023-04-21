package org.nakedobjects.nof.persist.objectstore.inmemory;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.NakedObjectLoader;
import org.nakedobjects.noa.adapter.Oid;
import org.nakedobjects.noa.persist.InstancesCriteria;
import org.nakedobjects.nof.core.persist.TitleCriteria;

public class MemoryObjectStoreInstances {

    protected final Hashtable objectInstances = new Hashtable();

    protected final Hashtable titleIndex = new Hashtable();

    private NakedObjectLoader objectLoader;

    public MemoryObjectStoreInstances(final NakedObjectLoader objectLoader) {
        this.objectLoader = objectLoader;
    }

    public Enumeration elements() {
        Vector v = new Vector(objectInstances.size());
        for (Enumeration e = objectInstances.keys(); e.hasMoreElements(); ) {
            Oid oid = (Oid) e.nextElement();
            v.addElement(getObject(oid));
        }
        return v.elements();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        Logger.getLogger(MemoryObjectStoreInstances.class).info("finalizing instances");
    }

    public NakedObject getObject(final Oid oid) {
        NakedObject loadedObject = objectLoader.getAdapterFor(oid);
        if (loadedObject != null) {
            return loadedObject;
        } else {
            Object pojo = objectInstances.get(oid);
            if (pojo == null) {
                return null;
            }
            NakedObject adapter;
            adapter = objectLoader.recreateAdapter(oid, pojo);
            return adapter;
        }
    }

    public Oid getOidFor(final Object object) {
        Enumeration enumeration = objectInstances.keys();
        while (enumeration.hasMoreElements()) {
            Oid oid = (Oid) enumeration.nextElement();
            if (objectInstances.get(oid).equals(object)) {
                return oid;
            }
        }
        return null;
    }

    public boolean hasInstances() {
        return numberOfInstances() > 0;
    }

    public void instances(final InstancesCriteria criteria, final Vector instances) {
        if (criteria instanceof TitleCriteria) {
            String requiredTitle = ((TitleCriteria) criteria).getRequiredTitle();
            Object oid = titleIndex.get(requiredTitle);
            if (oid != null) {
                NakedObject object = getObject((Oid) oid);
                instances.addElement(object);
                return;
            }
        }
        Enumeration e = elements();
        while (e.hasMoreElements()) {
            NakedObject element = (NakedObject) e.nextElement();
            if (criteria.matches(element)) {
                instances.addElement(element);
            }
        }
    }

    public void instances(final Vector instances) {
        Enumeration e = elements();
        while (e.hasMoreElements()) {
            instances.addElement(e.nextElement());
        }
    }

    public int numberOfInstances() {
        return objectInstances.size();
    }

    public void remove(final Oid oid) {
        objectInstances.remove(oid);
    }

    public void save(final NakedObject object) {
        objectInstances.put(object.getOid(), object.getObject());
        titleIndex.put(object.titleString().toLowerCase(), object.getOid());
    }

    public void shutdown() {
        objectInstances.clear();
        titleIndex.clear();
    }

    public void reset() {
        for (Enumeration e = objectInstances.keys(); e.hasMoreElements(); ) {
        }
    }
}
