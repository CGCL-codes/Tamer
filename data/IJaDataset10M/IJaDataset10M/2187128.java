package fr.inria.zvtm.cluster;

/**
 * An Identifiable implementation that provides reasonable defaults.
 * Can be inherited by classes that should be replicated by ZVTM-cluster.
 */
public class DefaultIdentifiable implements Identifiable {

    private final ObjId objId = ObjIdFactory.next();

    private boolean replicated = false;

    /**
     * {@inheritDoc}
     */
    public ObjId getObjId() {
        return objId;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isReplicated() {
        return replicated;
    }

    public void setReplicated(boolean val) {
        this.replicated = val;
    }
}
