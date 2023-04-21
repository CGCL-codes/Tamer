package org.wilmascope.forcelayout;

import org.wilmascope.graph.NodeList;
import org.wilmascope.graph.Node;
import org.wilmascope.graph.Cluster;
import javax.vecmath.*;

/**
 * A repulsive (electrostatic style) force between all nodes.  Note, this
 * version of repulsion is compatible with RepulsionlessSpring.
 */
public class RepulsionSpring extends Force {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9046192945171864218L;

    public RepulsionSpring(float strengthConstant, float limitRadius) {
        super(strengthConstant, "Repulsion");
        this.limitRadiusSquared = limitRadius * limitRadius;
    }

    public void setCluster(Cluster root) {
        nodes = root.getNodes();
    }

    /** Calculate deltas for the repulsive (electrostatic) forces
   *  between all nodes
   * @param nodes The nodes to which this force applies
   * @param edges Not used in this method (here to match the interface)
   */
    public void calculate() {
        Node node1, node2;
        NodeForceLayout nodeLayout1;
        for (int i = 0; i < nodes.size(); i++) {
            node1 = nodes.get(i);
            nodeLayout1 = (NodeForceLayout) node1.getLayout();
            Vector3f repulsion = new Vector3f();
            for (int j = 0; j < nodes.size(); j++) {
                if (j == i) continue;
                node2 = nodes.get(j);
                Vector3f v = new Vector3f(node1.getPosition());
                v.sub(node2.getPosition());
                repulsion.add(calculate(nodeLayout1, (NodeForceLayout) node2.getLayout(), v));
            }
            nodeLayout1.addForce(repulsion);
        }
    }

    public Vector3f calculate(NodeForceLayout nl1, NodeForceLayout nl2, Vector3f vector) {
        Vector3f v = new Vector3f(vector);
        float separation = v.length();
        if (separation != 0) {
            if (separation < limitRadiusSquared) {
                float mass1 = 1;
                float mass2 = 1;
                v.scale(strengthConstant * mass1 * mass2 / (separation * separation));
            }
        } else {
            v.set(org.wilmascope.global.RandomGenerator.getVector3f());
        }
        return v;
    }

    private float limitRadiusSquared;

    private NodeList nodes;
}
