package graphclustering.algorithm;

import graphclustering.data.Cluster;
import graphclustering.data.DWGraph;
import graphclustering.util.ClusteringUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BitClusteringAlgorithm {

    private static Log logger = LogFactory.getLog(BitClusteringAlgorithm.class);

    private static int bitStopThreshold = 5;

    private static float bitPercentage = (float) 0.2;

    private static int maxClusterSize = 8;

    public static int rounds = 0;

    public static void analysis(DWGraph graph, Cluster parent) {
        List<Cluster> bitedClusterList = oneRoundBitClustering(graph);
        parent.clusterChildren.addAll(bitedClusterList);
        for (Cluster tempCluster : bitedClusterList) {
            tempCluster.parent = parent;
        }
        DWGraph newGraph = regenerateGraph(graph, bitedClusterList);
        if (newGraph.nodeSize < bitStopThreshold) {
            if (newGraph.nodeSize > 0) parent.atomicNodeIDList.addAll(newGraph.nodeIDList);
        } else {
            Cluster subParent = new Cluster();
            parent.clusterChildren.add(subParent);
            analysis(newGraph, subParent);
        }
        System.out.println(ClusteringUtils.getGraphClusterNumber(parent));
    }

    private static List<Cluster> oneRoundBitClustering(DWGraph graph) {
        rounds++;
        List<Cluster> clusterList = new ArrayList<Cluster>();
        for (int i = 0; i < graph.nodeSize; i++) {
            List<Cluster> tmpClusterList = ClusteringUtils.selectClusterCandidateforOneNode(graph, i, maxClusterSize);
            float bestClusterQuality = -100000000;
            Cluster selectedCluster = null;
            for (Cluster candidate : tmpClusterList) {
                float clusterQuality = ClusteringUtils.calcIMValueforCandCluster(candidate, graph);
                if (bestClusterQuality < clusterQuality) {
                    bestClusterQuality = clusterQuality;
                    selectedCluster = candidate;
                    selectedCluster.imQuality = clusterQuality;
                }
            }
            if (selectedCluster != null) clusterList.add(selectedCluster);
        }
        for (int i = 0; i < clusterList.size(); i++) {
            float currMaxIMValue = clusterList.get(i).imQuality;
            Cluster currBestCluster = null;
            int currBestClusterIndex = i;
            for (int j = i + 1; j < clusterList.size(); j++) {
                if (clusterList.get(j).imQuality > currMaxIMValue) {
                    currMaxIMValue = clusterList.get(j).imQuality;
                    currBestCluster = clusterList.get(j);
                    currBestClusterIndex = j;
                }
            }
            if (i != currBestClusterIndex && currBestCluster != null) {
                Cluster tmp = clusterList.get(i);
                clusterList.set(i, currBestCluster);
                clusterList.set(currBestClusterIndex, tmp);
            }
        }
        int toBeBitClusterNum = (int) (bitPercentage * graph.nodeSize);
        List<Cluster> selectedClusterList = new ArrayList<Cluster>();
        for (Cluster tmp : clusterList) {
            if (!ClusteringUtils.overlapped(selectedClusterList, tmp)) {
                selectedClusterList.add(tmp);
                List<Integer> noneSelectedNodeIndexList = getNoneSelectedNodeIndexList(selectedClusterList, graph);
                for (int i = 0; i < noneSelectedNodeIndexList.size(); i++) {
                    float weightSum = 0.0f;
                    int indexI = noneSelectedNodeIndexList.get(i);
                    for (int j = 0; j < noneSelectedNodeIndexList.size(); j++) {
                        int indexJ = noneSelectedNodeIndexList.get(j);
                        if (i != j) {
                            weightSum = weightSum + graph.graph[indexI][indexJ] + graph.graph[indexJ][indexI];
                        }
                    }
                    if (weightSum == 0.0f) {
                        tmp.atomicNodeIDList.add(graph.nodeIDList.get(indexI));
                    }
                }
            }
            if (selectedClusterList.size() > toBeBitClusterNum) break;
        }
        return selectedClusterList;
    }

    public static List<Integer> getNoneSelectedNodeIndexList(List<Cluster> selectedClusterList, DWGraph graph) {
        HashSet<String> selectedNodeIdList = new HashSet<String>();
        for (Cluster cluster : selectedClusterList) {
            selectedNodeIdList.addAll(cluster.atomicNodeIDList);
        }
        List<Integer> noneSelectedIdList = new ArrayList<Integer>();
        for (int i = 0; i < graph.nodeIDList.size(); i++) {
            String nodeId = graph.nodeIDList.get(i);
            if (!selectedNodeIdList.contains(nodeId)) {
                noneSelectedIdList.add(i);
            }
        }
        return noneSelectedIdList;
    }

    private static DWGraph regenerateGraph(DWGraph oldGraph, List<Cluster> bitedClusterList) {
        List<String> bittedNodeList = new ArrayList<String>();
        for (Cluster tmp : bitedClusterList) {
            bittedNodeList.addAll(tmp.atomicNodeIDList);
        }
        ClusteringUtils.eniminateDuplica(bittedNodeList);
        DWGraph newGraph = new DWGraph();
        newGraph.nodeSize = oldGraph.nodeSize - bittedNodeList.size();
        for (String nodeID : oldGraph.nodeIDList) if (bittedNodeList.indexOf(nodeID) < 0) newGraph.nodeIDList.add(nodeID);
        newGraph.graph = new float[newGraph.nodeSize][newGraph.nodeSize];
        int newI = 0, newJ = 0;
        for (int i = 0; i < oldGraph.nodeSize; i++) {
            if (newGraph.nodeIDList.contains(oldGraph.nodeIDList.get(i))) {
                newI = newGraph.nodeIDList.indexOf(oldGraph.nodeIDList.get(i));
            } else continue;
            for (int j = 0; j < oldGraph.nodeSize; j++) {
                if (newGraph.nodeIDList.contains(oldGraph.nodeIDList.get(j))) {
                    newJ = newGraph.nodeIDList.indexOf(oldGraph.nodeIDList.get(j));
                    ;
                } else continue;
                newGraph.graph[newI][newJ] = oldGraph.graph[i][j];
            }
        }
        return newGraph;
    }
}
