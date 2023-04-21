package org.fgraph.traverse;

import org.fgraph.Direction;
import org.fgraph.Edge;
import org.fgraph.Node;

/**
 *  Represents a single path step from one
 *  Node to another through an Edge.
 *
 *  @version   $Revision: 175 $
 *  @author    Paul Speed
 */
public interface Step {

    /**
     *  Returns the starting node for this step.
     */
    public Node getSource();

    /**
     *  Returns the destination of this step.
     */
    public Node getTarget();

    /**
     *  Returns the edge that was traversed to form this step.
     */
    public Edge getEdge();

    /**
     *  Returns the direction that was taken for this step's edge
     *  as it relates to the source.  So, when target = edge.head
     *  then direction was OUT.  When target = edge.tail then the
     *  direction was IN.
     */
    public Direction getDirection();
}
