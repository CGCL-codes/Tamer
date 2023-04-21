package org.wilmascope.forcelayout;

import javax.vecmath.Vector3f;
import org.wilmascope.graph.Cluster;
import org.wilmascope.graph.Edge;
import org.wilmascope.graph.EdgeList;
import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeLayout;

/**
 * A {@link Force} for use by nodes connected by an edge.
 * Attempts to restore the distance between nodes to the natural length of that edge.
 */
@SuppressWarnings("serial")
public class Spring extends Force {

    /**
   * Creates a new {@link Spring} with the given strength
   *
   * @param strengthConstant k in <a href="www.google.com">Hooke's Law</a>
   */
    public Spring(float strengthConstant) {
        super(strengthConstant, "Spring");
    }

    public void setCluster(Cluster root) {
        edges = root.getInternalEdges();
    }

    /** Calculate node deltas due to spring forces
   *  between all nodes connected by an edge
   */
    public void calculate() {
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            EdgeForceLayout edgeLayout = (EdgeForceLayout) edge.getLayout();
            edgeLength = edge.getLength();
            if (edgeLength != 0) {
                springForce = (edgeLength - edgeLayout.getRelaxedLength()) * strengthConstant * edgeLayout.getStiffness() / edgeLength;
                v.set(edge.getVector());
                v.scale(springForce);
            } else {
                v.set(Constants.minVector);
            }
            getForceNodeLayout(edge.getEnd()).subForce(v);
            getForceNodeLayout(edge.getStart()).addForce(v);
        }
    }

    private NodeForceLayout getForceNodeLayout(Node node) {
        NodeLayout l = node.getLayout();
        if (l instanceof NodeForceLayout) {
            return (NodeForceLayout) l;
        } else {
            return (NodeForceLayout) getForceNodeLayout(node.getOwner());
        }
    }

    private EdgeList edges;

    private Vector3f v = new Vector3f();

    private float springForce;

    private float edgeLength;
}
