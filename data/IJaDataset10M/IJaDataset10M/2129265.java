package org.nakedobjects.persistence.hibernate;

import org.apache.log4j.Logger;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.Persistable;
import org.nakedobjects.object.persistence.PersistAlgorithm;
import org.nakedobjects.object.persistence.PersistedObjectAdder;
import org.nakedobjects.utility.ToString;

/**
 * A PersistAlgorithm which simply saves the object made persistent.
 * This allows Hibernate to determine which related objects should be saved - mappings
 * should be created with cascade="save-update".
 */
public class SimplePersistAlgorithm implements PersistAlgorithm {

    private static final Logger LOG = Logger.getLogger(PersistAlgorithm.class);

    public void makePersistent(final NakedObject object, final PersistedObjectAdder adders) {
        if (object.getResolveState().isPersistent() || object.persistable() == Persistable.TRANSIENT) {
            return;
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("persist " + object);
        }
        adders.createObject(object);
    }

    public String name() {
        return "SimplePersistAlgorithm";
    }

    public void init() {
    }

    public void shutdown() {
    }

    public String toString() {
        ToString toString = new ToString(this);
        return toString.toString();
    }
}
