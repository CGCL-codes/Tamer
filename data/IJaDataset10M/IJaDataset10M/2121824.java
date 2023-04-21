package ec.gp;

import java.io.Serializable;

/**
 * GPNodeGatherer is a small container object for the GPNode.nodeInPosition(...)
 * method and GPNode.numNodes(...) method. 
 * It may be safely reused without being reinitialized.
 *
 * @author Sean Luke
 * @version 1.0 
 */
public class GPNodeGatherer implements Serializable {

    public GPNode node;

    /** Returns true if thisNode is the kind of node to be considered in the
        gather count for nodeInPosition(...) and GPNode.numNodes(GPNodeGatherer).
        The default form simply returns true.  */
    public boolean test(final GPNode thisNode) {
        return true;
    }
}
