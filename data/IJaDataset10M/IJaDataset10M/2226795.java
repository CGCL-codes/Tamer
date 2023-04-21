package org.nakedobjects.persistence.cache;

import org.nakedobjects.object.DirtyObjectSet;
import org.nakedobjects.object.InstancesCriteria;
import org.nakedobjects.object.NakedClass;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectField;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedObjects;
import org.nakedobjects.object.ObjectNotFoundException;
import org.nakedobjects.object.Oid;
import org.nakedobjects.object.UnsupportedFindException;
import org.nakedobjects.object.defaults.AbstracObjectPersistor;
import org.nakedobjects.object.defaults.NullDirtyObjectSet;
import org.nakedobjects.object.io.Memento;
import org.nakedobjects.object.persistence.OidGenerator;
import org.nakedobjects.utility.Assert;
import org.nakedobjects.utility.DebugString;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import test.org.nakedobjects.object.repository.object.persistence.ObjectStoreException;

public class CachePersistor extends AbstracObjectPersistor {

    private Journal journal;

    private final Hashtable nakedClasses = new Hashtable();

    private ObjectStore objectStore;

    private OidGenerator oidGenerator;

    private DirtyObjectSet objectsToRefreshViewsFor = new NullDirtyObjectSet();

    private SnapshotFactory snapshotFactory;

    public void abortTransaction() {
    }

    public void addObjectChangedListener(DirtyObjectSet listener) {
        Assert.assertNotNull("must set a listener", listener);
        this.objectsToRefreshViewsFor = listener;
    }

    public void destroyObject(NakedObject object) {
        journal.writeJournal("delete", new Memento(object));
        objectStore.instances(object.getSpecification()).remove(object);
    }

    public void endTransaction() {
    }

    public String getDebugTitle() {
        return null;
    }

    protected NakedObject[] getInstances(InstancesCriteria criteria) throws ObjectStoreException, UnsupportedFindException {
        Vector instances = new Vector();
        Enumeration objects = objectStore.instances(criteria.getSpecification()).instances();
        while (objects.hasMoreElements()) {
            NakedObject instance = (NakedObject) objects.nextElement();
            if (criteria.matches(instance)) {
                instances.addElement(instance);
            }
        }
        return toArray(instances);
    }

    protected NakedObject[] getInstances(NakedObjectSpecification specification, boolean includeSubclasses) {
        Vector instances = new Vector();
        Enumeration objects = objectStore.instances(specification).instances();
        while (objects.hasMoreElements()) {
            NakedObject instance = (NakedObject) objects.nextElement();
            instances.addElement(instance);
        }
        return toArray(instances);
    }

    public NakedClass getNakedClass(NakedObjectSpecification specification) {
        if (nakedClasses.contains(specification)) {
            return (NakedClass) nakedClasses.get(specification);
        }
        NakedClass spec;
        spec = new NakedClass(specification.getFullName());
        nakedClasses.put(specification, spec);
        return spec;
    }

    public NakedObject getObject(Oid oid, NakedObjectSpecification hint) {
        NakedObject object = (NakedObject) objectStore.instances(hint).read(oid);
        if (object == null) {
            throw new ObjectNotFoundException(oid);
        }
        return object;
    }

    public boolean hasInstances(NakedObjectSpecification specification, boolean includeSubclasses) {
        return numberOfInstances(specification, false) > 0;
    }

    public void init() {
        objectStore = new ObjectStore(snapshotFactory);
        objectStore.init();
        journal.applyJournals();
        journal.openJounal();
    }

    public void makePersistent(NakedObject object) {
        NakedObjects.getObjectLoader().madePersistent(object, oidGenerator.next(object));
        journal.writeJournal("create", new Memento(object));
        objectStore.instances(object.getSpecification()).create(object);
    }

    public String name() {
        return "Cache Object Manager/Store";
    }

    public int numberOfInstances(NakedObjectSpecification specification, boolean includeSubclasses) {
        return objectStore.instances(specification).numberInstances();
    }

    public void objectChanged(NakedObject object) {
        if (object.getResolveState().respondToChangesInPersistentObjects()) {
            journal.writeJournal("save", new Memento(object));
            objectsToRefreshViewsFor.addDirty(object);
        }
    }

    public void reload(NakedObject object) {
    }

    public void reset() {
    }

    public void resolveImmediately(NakedObject object) {
    }

    public void resolveField(NakedObject object, NakedObjectField field) {
    }

    public void saveChanges() {
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public void setOidGenerator(OidGenerator oidGenerator) {
        this.oidGenerator = oidGenerator;
    }

    public void setSnapshotFactory(SnapshotFactory snapshotFactory) {
        this.snapshotFactory = snapshotFactory;
    }

    public void shutdown() {
        objectStore.shutdown();
        journal.closeJournal();
    }

    public void startTransaction() {
    }

    private NakedObject[] toArray(Vector instances) {
        NakedObject[] instanceArray = new NakedObject[instances.size()];
        instances.copyInto(instanceArray);
        return instanceArray;
    }
}
