package org.sdf4j.optimisations.clustering;

import java.util.HashMap;
import java.util.Vector;
import org.sdf4j.SDFMath;
import org.sdf4j.model.parameters.InvalidExpressionException;
import org.sdf4j.model.sdf.SDFAbstractVertex;
import org.sdf4j.model.sdf.SDFEdge;
import org.sdf4j.model.sdf.SDFGraph;

/**
 * Class to test if  a cluster respect the clustering conditions. 
 * 
 * @author pthebault
 *
 */
public class ClusteringConditions {

    /**
	 * Function to test if a cycle is introduces by the cluster
	 * @param path is a vector which contains the first vertex of the path
	 * @param graph is the vertices'SDFGraph 
	 * @param Target is the second vertex of the cluster
	 * @return true if a cycle is introduce and false when there is no cycle introduction
	 * @throws InvalidExpressionException 
	 */
    public static boolean sdfClusterCycleIntroduction(Vector<SDFAbstractVertex> path, SDFGraph graph, SDFAbstractVertex Target) throws InvalidExpressionException {
        boolean cycle_introduce = false;
        for (SDFEdge edge : graph.edgesOf(path.lastElement())) {
            if (edge.getSource() == path.lastElement() || cycle_introduce == true) {
                if (edge.getTarget() == Target && path.size() > 1) {
                    return true;
                }
                if (edge.getDelay().intValue() != 0 && !path.contains(edge.getTarget())) {
                    path.add(edge.getTarget());
                    cycle_introduce = sdfClusterCycleIntroduction(path, graph, Target);
                }
            }
            if (cycle_introduce == true) {
                return true;
            }
        }
        path.removeElementAt(path.size() - 1);
        return false;
    }

    /**
	 * Function test
	 * @param edge An edge between the two vertex to cluster
	 * @param graph The SDFGraph in which we cluster
	 * @param vrb is the repetition vector of the SDFGraph
	 * @return true if clustering conditions are respected or false if clustering conditions are not respected
	 * @throws InvalidExpressionException 
	 */
    public static boolean SDFClusteringConditions(SDFEdge edge, SDFGraph graph, HashMap<SDFAbstractVertex, Integer> vrb) throws InvalidExpressionException {
        int Q_xy = vrb.get(edge.getSource()).intValue() / SDFMath.gcd(vrb.get(edge.getSource()).intValue(), vrb.get(edge.getTarget()).intValue());
        for (SDFEdge alpha : graph.edgesOf(edge.getSource())) {
            if (alpha.getTarget() == edge.getSource() && alpha.getSource() != edge.getTarget() && alpha.getSource() != edge.getSource()) {
                int k1, k2;
                k1 = alpha.getProd().intValue() / (alpha.getCons().intValue() * Q_xy);
                k2 = alpha.getDelay().intValue() / (alpha.getCons().intValue() * Q_xy);
                if (k1 <= 0 || k2 < 0) {
                    return (false);
                }
            }
            if (alpha.getSource() == edge.getSource() && alpha.getTarget() != edge.getTarget() && alpha.getTarget() != edge.getSource()) {
                int k1, k2;
                k1 = alpha.getCons().intValue() / (alpha.getProd().intValue() * Q_xy);
                k2 = alpha.getDelay().intValue() / (alpha.getProd().intValue() * Q_xy);
                if (k1 <= 0 || k2 < 0) {
                    return (false);
                }
            }
        }
        for (SDFEdge alpha : graph.edgesOf(edge.getTarget())) {
            if (alpha.getTarget() == edge.getTarget() && alpha.getSource() != edge.getTarget() && alpha.getSource() != edge.getSource()) {
                int k1, k2;
                k1 = alpha.getProd().intValue() / (alpha.getCons().intValue() * Q_xy);
                k2 = alpha.getDelay().intValue() / (alpha.getCons().intValue() * Q_xy);
                if (k1 <= 0 || k2 < 0) {
                    return (false);
                }
            }
            if (alpha.getSource() == edge.getTarget() && alpha.getTarget() != edge.getTarget() && alpha.getTarget() != edge.getSource()) {
                int k1, k2;
                k1 = alpha.getCons().intValue() / (alpha.getProd().intValue() * Q_xy);
                k2 = alpha.getDelay().intValue() / (alpha.getProd().intValue() * Q_xy);
                if (k1 <= 0 || k2 < 0) {
                    return (false);
                }
            }
        }
        boolean ZeroDelay = false;
        for (SDFEdge alpha : graph.getAllEdges(edge.getSource(), edge.getTarget())) {
            if (alpha.getDelay().intValue() == 0) {
                ZeroDelay = true;
                break;
            }
        }
        if (ZeroDelay == false) {
            for (SDFEdge alpha : graph.getAllEdges(edge.getTarget(), edge.getSource())) {
                if (alpha.getDelay().intValue() == 0) {
                    ZeroDelay = true;
                    break;
                }
            }
        }
        if (ZeroDelay == false) {
            return false;
        }
        int k1, k2;
        k1 = vrb.get(edge.getSource()) / vrb.get(edge.getTarget());
        k2 = vrb.get(edge.getTarget()) / vrb.get(edge.getSource());
        if (k1 <= 0 && k2 <= 0) {
            return false;
        }
        Vector<SDFAbstractVertex> path = new Vector<SDFAbstractVertex>();
        path.add(edge.getSource());
        if (sdfClusterCycleIntroduction(path, graph, edge.getTarget())) {
            return false;
        }
        return true;
    }
}
