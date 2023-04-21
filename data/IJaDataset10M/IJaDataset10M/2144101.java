package net.sf.openforge.util.graphviz;

/**
 * A box node.
 *
 * @author  Stephen Edwards
 * @version $Id: Box.java 2 2005-06-09 20:00:48Z imiller $
 */
public class Box extends Node {

    private static final String rcs_id = "RCS_REVISION: $Rev: 2 $";

    /**
     * Constructs a new Box node.
     *
     * @param id the identifier of the node
     */
    public Box(String id) {
        super(id, "box");
    }
}
