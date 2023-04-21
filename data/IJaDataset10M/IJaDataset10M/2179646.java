package de.sonivis.tool.ontology.view.partitioning;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import prefuse.data.Graph;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import de.sonivis.tool.core.datamodel.Node;
import de.sonivis.tool.ontology.view.prefuse.SonivisPrefuseMapping;
import de.sonivis.tool.ontology.view.prefuse.layout.ModifiedFruchtermanReingoldLayout;

/**
 * This class is an extension of FruchtermanReingoldLayout. This Layout is used
 * to visualize the Stuckenschmidt/Klein partitioning.
 * 
 * @author Maximilian Schr�der
 * @see de.sonivis.tool.ontology.view.partitioning.PartitioningLayout
 */
public class PartitioningLayout extends ModifiedFruchtermanReingoldLayout {

    /**
	 * SonivisPrefuseMapping
	 */
    private SonivisPrefuseMapping mapping;

    /**
	 * Map with normal keys and Wrapper-Elements(PartitionID,PartitionNode)
	 */
    private Map<Integer, MNode> partitionMap;

    public PartitioningLayout(String graph, SonivisPrefuseMapping mapping, Map<Integer, Set<? extends Node>> alphaModules) {
        super(graph, 700);
        this.mapping = mapping;
        this.partitionMap = prepareMappingNodes(alphaModules);
    }

    @Override
    public void calcAttraction(EdgeItem e) {
        double partitionAttraction = 1.0;
        NodeItem n1 = e.getSourceItem();
        Params n1p = getParams(n1);
        NodeItem n2 = e.getTargetItem();
        Params n2p = getParams(n2);
        int prefuseNode1Id = n1.getRow();
        int prefuseNode2Id = n2.getRow();
        int partitionNode1ID = partitionMap.get(prefuseNode1Id).getPartitionID();
        int partitionNode2ID = partitionMap.get(prefuseNode2Id).getPartitionID();
        if (partitionNode1ID == partitionNode2ID) {
            partitionAttraction = 12.5;
        }
        double xDelta = (n1p.getLoc()[0] - n2p.getLoc()[0]) * partitionAttraction;
        double yDelta = (n1p.getLoc()[1] - n2p.getLoc()[1]) * partitionAttraction;
        double deltaLength = Math.max(EPSILON, Math.sqrt(xDelta * xDelta + yDelta * yDelta));
        double force = (deltaLength * deltaLength) / forceConstant;
        if (Double.isNaN(force)) {
            System.err.println("Mathematical error...");
        }
        double xDisp = (xDelta / deltaLength) * force;
        double yDisp = (yDelta / deltaLength) * force;
        n1p.getDisp()[0] -= xDisp;
        n1p.getDisp()[1] -= yDisp;
        n2p.getDisp()[0] += xDisp;
        n2p.getDisp()[1] += yDisp;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void calcRepulsion(Graph g, NodeItem n1) {
        Params np = getParams(n1);
        np.getDisp()[0] = 0.0;
        np.getDisp()[1] = 0.0;
        for (Iterator iter2 = g.nodes(); iter2.hasNext(); ) {
            double partitionRepulsion;
            NodeItem n2 = (NodeItem) iter2.next();
            Params n2p = getParams(n2);
            if (n2.isFixed()) continue;
            if (n1 != n2) {
                int prefuseNode1Id = n1.getRow();
                int prefuseNode2Id = n2.getRow();
                int partitionNode1ID = partitionMap.get(prefuseNode1Id).getPartitionID();
                int partitionNode2ID = partitionMap.get(prefuseNode2Id).getPartitionID();
                n1.set("partitionNr", partitionNode1ID);
                n2.set("partitionNr", partitionNode2ID);
                if (partitionNode1ID == partitionNode2ID) {
                    partitionRepulsion = 0.9;
                } else {
                    partitionRepulsion = 1.0;
                }
                double xDelta = (np.getLoc()[0] - n2p.getLoc()[0]) * partitionRepulsion;
                double yDelta = (np.getLoc()[1] - n2p.getLoc()[1]) * partitionRepulsion;
                double deltaLength = Math.max(EPSILON, Math.sqrt(xDelta * xDelta + yDelta * yDelta));
                double force = (forceConstant * forceConstant) / deltaLength;
                if (Double.isNaN(force)) {
                    System.err.println("Mathematical error...");
                }
                np.getDisp()[0] += (xDelta / deltaLength) * force;
                np.getDisp()[1] += (yDelta / deltaLength) * force;
            }
        }
    }

    /**
	 * prepares incoming alphaModuleMap for modified Fruchterman Reingold 
	 * @param partitionMap: alphaModuleMap from StructuralPartitioning
	 * @return: HashMap with Node,PartitionIdentifier
	 */
    private Map<Integer, MNode> prepareMappingNodes(Map<Integer, Set<? extends Node>> partitionMap) {
        Map<Integer, MNode> allMNodes = new HashMap<Integer, MNode>();
        int partitionIdent = 1;
        int prefuseIdent = -1;
        for (Set<? extends Node> partitionSet : partitionMap.values()) {
            for (Node partitionNode : partitionSet) {
                prefuseIdent = mapping.getPrefuseNodeFromSonivisNode((Node) partitionNode);
                allMNodes.put(prefuseIdent, new MNode(partitionNode, partitionIdent));
            }
            partitionIdent++;
        }
        return allMNodes;
    }
}
