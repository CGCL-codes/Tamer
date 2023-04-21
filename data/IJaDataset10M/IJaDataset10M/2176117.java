package org.nakedobjects.object.defaults;

import org.nakedobjects.NakedObjects;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedError;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.persistence.NakedObjectManager;
import org.nakedobjects.object.persistence.ObjectStoreException;
import org.nakedobjects.object.persistence.Oid;
import org.nakedobjects.object.reflect.NakedObjectField;
import org.nakedobjects.object.reflect.OneToManyAssociation;
import org.nakedobjects.object.reflect.OneToOneAssociation;
import org.apache.log4j.Logger;

public abstract class AbstractNakedObject implements NakedObject {

    private static final Logger LOG = Logger.getLogger(AbstractNakedObject.class);

    private boolean isFinder = false;

    private transient boolean isResolved = false;

    private Oid oid;

    protected NakedObjectSpecification specification;

    public NakedError actionPersist() {
        makePersistent();
        return null;
    }

    /**
     * Copies the fields from the specified instance to the current instance.
     * Each NakedObject object reference is copied across and values for each
     * NakedValue object are copied across to the NakedValue objects in the
     * current instance.
     */
    public void copyObject(Naked objectToCopy) {
        if (objectToCopy.getClass() != getClass()) {
            throw new IllegalArgumentException("Copy object can only copy objects of the same type");
        }
        NakedObject object = (NakedObject) objectToCopy;
        NakedObjectField[] fields = getSpecification().getFields();
        for (int i = 0; i < fields.length; i++) {
            NakedObjectField field = fields[i];
            if (field instanceof OneToManyAssociation) {
                getField(field).copyObject(object.getField(field));
            } else if (field instanceof OneToOneAssociation) {
                setValue((OneToOneAssociation) field, (NakedObject) object.getField(field));
            } else {
            }
        }
    }

    public void created() {
    }

    /**
     * A utility method for creating new objects in the context of the system -
     * that is, it is added to the pool of objects the enterprise system
     * contains.
     */
    protected NakedObject createInstance(Class cls) {
        return getObjectManager().createInstance(cls.getName());
    }

    /**
     * hook method, see interface description for further details
     */
    public void deleted() {
    }

    /**
     */
    protected void destroy() throws ObjectStoreException {
        if (isPersistent()) {
            getObjectManager().destroyObject(this);
        }
    }

    /**
     * An object will be deemed to be equal if it: is this object; or has the
     * same OID.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof AbstractNakedObject) {
            AbstractNakedObject ano = (AbstractNakedObject) obj;
            if (ano.getOid() == null) {
                return false;
            } else {
                return ano.getOid().equals(getOid());
            }
        } else {
            return false;
        }
    }

    /**
     * Returns the short name from this objects NakedObjectSpecification
     * 
     * TODO allow the reflector to set up a icon name
     */
    public String getIconName() {
        return null;
    }

    public Object getObject() {
        return this;
    }

    private NakedObjectManager getObjectManager() {
        return NakedObjects.getObjectManager();
    }

    public Oid getOid() {
        return oid;
    }

    public NakedObjectSpecification getSpecification() {
        if (specification == null) {
            specification = NakedObjects.getSpecificationLoader().loadSpecification(getObject().getClass());
        }
        return specification;
    }

    /**
     * Returns false indicating that the object contains data.
     */
    public boolean isEmpty(NakedObjectField field) {
        return false;
    }

    public boolean isFinder() {
        return isFinder;
    }

    /**
     * Returns true if this object has an OID set.
     */
    public boolean isPersistent() {
        return getOid() != null;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void makeFinder() {
        if (isPersistent()) {
            throw new IllegalStateException("Can't make a persient object into a Finder");
        }
        isFinder = true;
    }

    public void makePersistent() {
        if (!isPersistent()) {
            LOG.debug("makePersistent(" + this + ")");
            getObjectManager().makePersistent(this);
        }
    }

    public void setNakedClass(NakedObjectSpecification nakedClass) {
        this.specification = nakedClass;
    }

    public void setOid(Oid oid) {
        if (this.oid == null) {
            this.oid = oid;
        } else {
            throw new IllegalStateException("The OID is already set (" + this + ")");
        }
    }

    public void setResolved() {
        if (isResolved) {
            throw new IllegalStateException("Object is already marked as resolved (" + this + ")");
        } else {
            isResolved = true;
        }
    }

    public void debugClearResolved() {
        isResolved = false;
    }

    private StringBuffer toStringBuffer = new StringBuffer();

    /**
	 * This is no longer thread-safe (performance tuning), hence synchronized.
	 */
    public synchronized String toString() {
        StringBuffer s = toStringBuffer;
        s.append(specification == null ? getClass().getName() : specification.getShortName());
        String asString = s.toString();
        s.setLength(0);
        return asString;
    }
}
