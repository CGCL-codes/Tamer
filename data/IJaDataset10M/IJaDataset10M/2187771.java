package org.nakedobjects.object.persistence;

import org.nakedobjects.object.InternalCollection;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectField;
import org.nakedobjects.object.NakedObjects;
import org.nakedobjects.object.Oid;
import org.nakedobjects.object.OneToManyAssociation;
import org.nakedobjects.object.Persistable;
import org.nakedobjects.object.ResolveState;
import org.nakedobjects.utility.Assert;
import org.nakedobjects.utility.NakedObjectRuntimeException;
import org.nakedobjects.utility.ToString;
import org.apache.log4j.Logger;

public class DefaultPersistAlgorithm implements PersistAlgorithm {

    private static final Logger LOG = Logger.getLogger(DefaultPersistAlgorithm.class);

    private OidGenerator oidGenerator;

    protected final synchronized Oid createOid(Naked object) {
        Oid oid = oidGenerator.next(object);
        LOG.debug("createOid " + oid);
        return oid;
    }

    public void init() {
        Assert.assertNotNull("oid generator required", oidGenerator);
        oidGenerator.init();
    }

    public void makePersistent(NakedObject object, PersistedObjectAdder manager) {
        if (object.getResolveState().isPersistent() || object.persistable() == Persistable.TRANSIENT) {
            LOG.warn("can't make object persistent - either already persistent, or transient only: " + object);
            return;
        }
        NakedObjectField[] fields = object.getFields();
        for (int i = 0; i < fields.length; i++) {
            NakedObjectField field = fields[i];
            if (field.isDerived() || field.isValue()) {
                continue;
            }
        }
        LOG.info("persist " + object);
        NakedObjects.getObjectLoader().madePersistent(object, createOid(object));
        for (int i = 0; i < fields.length; i++) {
            NakedObjectField field = fields[i];
            if (field.isDerived()) {
                continue;
            } else if (field.isValue()) {
                continue;
            } else if (field instanceof OneToManyAssociation) {
                InternalCollection collection = (InternalCollection) object.getField(field);
                makePersistent(collection, manager);
            } else {
                Object fieldValue = object.getField(field);
                if (fieldValue == null) {
                    continue;
                }
                if (!(fieldValue instanceof NakedObject)) {
                    throw new NakedObjectRuntimeException(fieldValue + " is not a NakedObject");
                }
                makePersistent((NakedObject) fieldValue, manager);
            }
        }
        manager.createObject(object);
    }

    protected void makePersistent(InternalCollection collection, PersistedObjectAdder manager) {
        LOG.info("persist " + collection);
        if (collection.getResolveState() == ResolveState.GHOST) {
            NakedObjects.getObjectLoader().start(collection, ResolveState.RESOLVING);
            NakedObjects.getObjectLoader().end(collection);
        }
        for (int j = 0; j < collection.size(); j++) {
            makePersistent(collection.elementAt(j), manager);
        }
    }

    public String name() {
        return "Simple Bottom Up Persistence Walker";
    }

    /**
     * Expose as a .NET property
     * 
     * @property
     */
    public void set_OidGenerator(OidGenerator oidGenerator) {
        this.oidGenerator = oidGenerator;
    }

    public void setOidGenerator(OidGenerator oidGenerator) {
        this.oidGenerator = oidGenerator;
    }

    public void shutdown() {
        oidGenerator.shutdown();
        oidGenerator = null;
    }

    public String toString() {
        ToString toString = new ToString(this);
        if (oidGenerator != null) {
            toString.append("oidGenerator", oidGenerator.name());
        }
        return toString.toString();
    }
}
