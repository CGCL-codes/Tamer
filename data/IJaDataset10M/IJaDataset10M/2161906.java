package fluid.control;

import java.util.NoSuchElementException;

/** Class to represent a single node as an enumeration.
 * @author John Tang Boyland
 */
class SingleControlEdgeEnumeration extends ControlEdgeEnumeration {

    private ControlEdge next;

    private boolean returned = false;

    /** Create an enumeration of control nodes.
   * @param elem the node to be returned (null if enumeration is to be empty
   */
    SingleControlEdgeEnumeration(ControlEdge elem) {
        this.next = elem;
    }

    public boolean hasMoreElements() {
        return !returned;
    }

    public ControlEdge nextControlEdge() throws NoSuchElementException {
        if (!returned) {
            returned = true;
            return next;
        } else {
            throw new NoSuchElementException();
        }
    }
}
