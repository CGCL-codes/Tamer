package treemap;

/**
 * The TMComputeSizeAdapter class implements a 
 * adapter for the TMComputeSize interface for users
 * of the TMModelNode interface.
 */
public abstract class TMComputeSizeAdapter implements TMComputeSize {

    /**
     * DO NOT OVERLOAD.
     */
    public boolean isCompatibleWith(TMNode node) {
        if (node instanceof TMNodeEncapsulator) {
            TMNodeEncapsulator n = (TMNodeEncapsulator) node;
            return isCompatibleWithObject(n.getNode());
        } else {
            return false;
        }
    }

    /**
     * DO NOT OVERLOAD.
     */
    public float getSize(TMNode node) throws TMExceptionBadTMNodeKind {
        if (isCompatibleWith(node)) {
            TMNodeEncapsulator n = (TMNodeEncapsulator) node;
            return getSizeOfObject(n.getNode());
        } else {
            throw new TMExceptionBadTMNodeKind(this, node);
        }
    }

    /**
     * TO BE IMPLEMENTED.
     */
    public abstract boolean isCompatibleWithObject(Object node);

    /**
     * TO BE IMPLEMENTED.
     */
    public abstract float getSizeOfObject(Object node);
}
