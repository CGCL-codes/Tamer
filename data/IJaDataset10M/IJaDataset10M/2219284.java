package org.nakedobjects.nos.store.hibernate;

import org.apache.log4j.Logger;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.Persistable;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.nof.core.util.ToString;
import org.nakedobjects.nof.persist.PersistAlgorithm;
import org.nakedobjects.nof.persist.PersistedObjectAdder;

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
        object.getSpecification().lifecycleEvent(object, NakedObjectSpecification.SAVING);
        adders.addPersistedObject(object);
        object.getSpecification().lifecycleEvent(object, NakedObjectSpecification.SAVED);
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
