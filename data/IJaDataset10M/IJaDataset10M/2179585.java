package com.acworks.acroute.v2;

import com.acworks.acroute.v2.impl.ArcChangeListener;
import java.util.Set;

/**
 *
 * @author nikita
 */
public interface Arc {

    boolean isIncident(Node node);

    /**
     * change Arc direction: convert this Arc to a directed arc, such that getSourceNode().equals(originNode);
     * @param originNode
     * @throws IllegalArgumentException if originNode is neither sourceNode nor targetNode
     *
     * @return true if this Arc's direction was actually changed
     */
    boolean makeDirected(Node originNode);

    Node getSourceNode();

    Node getTargetNode();

    ArcDirectionType getType();

    Set<ArcChangeListener> getArcChangeListeners();
}
