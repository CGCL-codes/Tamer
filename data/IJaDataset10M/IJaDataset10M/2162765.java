package edu.byu.ece.edif.tools.replicate;

import edu.byu.ece.edif.core.NamedPropertyObject;
import edu.byu.ece.edif.tools.replicate.nmr.EdifNameableStringReference;

public class Triplication extends AbstractReplication {

    public static final long serialVersionUID = 42L;

    public Triplication(Object toBeReplicated) throws ReplicationException {
        super(toBeReplicated);
        _replicationFactor = 3;
        _replicationType = ReplicationType.TRIPLICATE;
    }

    public int getReplicationFactor() {
        return _replicationFactor;
    }

    public Triplication toStringReference() throws ReplicationException {
        if (!(_toBeReplicated instanceof EdifNameableStringReference)) return new Triplication(new EdifNameableStringReference((NamedPropertyObject) _toBeReplicated)); else return null;
    }

    protected int _replicationFactor;
}
