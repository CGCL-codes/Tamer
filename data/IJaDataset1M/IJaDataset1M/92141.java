package gov.nasa.jpf.jvm.abstraction.state;

import gov.nasa.jpf.util.IntVector;
import gov.nasa.jpf.util.ObjVector;

/**
 * Encapsulates the state of an object on the heap.  This class allows the
 * heap to be traversed/transformed without running into non-heap nodes
 * (assuming you don't want to).
 * 
 * @author peterd
 */
public abstract class ObjectNode extends StateNode {

    public int classId;

    public abstract Iterable<? extends ObjectNode> getHeapRefs();

    public void addRefs(ObjVector<StateNode> v) {
        v.addAll(getHeapRefs());
    }

    public void addPrimData(IntVector v) {
        v.add(classId);
    }
}
