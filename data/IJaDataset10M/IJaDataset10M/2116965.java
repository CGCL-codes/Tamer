package org.sosy_lab.ccvisu.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.sosy_lab.ccvisu.graph.GraphData;
import org.sosy_lab.ccvisu.graph.GraphVertex;
import org.sosy_lab.ccvisu.graph.Group;
import org.sosy_lab.ccvisu.graph.Group.GroupKind;
import org.sosy_lab.ccvisu.graph.RadiusOfGroup;
import org.sosy_lab.util.Stopwatch;
import com.google.common.collect.MinMaxPriorityQueue;

/**
 * Implementation of a clustering algorithm based on
 * @see org.sosy-lab.ccvisu.clustering.ClustererMinDist
 * with some heuristics that enhance performance.
 */
public class ClustererMinDistPerc extends ClustererMinDist {

    /**
   * Put all nodes with a distance of at most n percent
   * of the diagonal of the layout in one cluster.
   * Heuristic that helps to minimize execution time of the algorithm.
   * A good value is 0.05
   */
    private final double maxDistancePercentToAutoMerge;

    /**
   * Stop creating clusters of the graph after all clusters
   * have a certain distance (percent of the diagonal of the layout). */
    private final double minClusterDistancePercent;

    /**
   * Constructor.
   *
   * @param graph  Graph that should be clustered.
   * @param numberOfClusters   Number of partitions hat should be found at least.
   * @param maxDistancePercentToAutoMerge  @see ClustererMinDistPerc#maxDistancePercentToAutoMerge
   * @param minClusterDistancePercent @see ClustererMinDistPerc#minClusterDistancePercent
   */
    public ClustererMinDistPerc(GraphData graph, int numberOfClusters, double maxDistancePercentToAutoMerge, double minClusterDistancePercent) {
        super(graph, numberOfClusters);
        this.maxDistancePercentToAutoMerge = maxDistancePercentToAutoMerge;
        this.minClusterDistancePercent = minClusterDistancePercent;
    }

    @Override
    protected List<Group> internalCreateClustersOfLayout() throws InterruptedException {
        Stopwatch stopwatch = Stopwatch.createAndStart();
        List<Group> clusters = new ArrayList<Group>();
        List<GraphVertex> vertices = graphData.getVertices();
        setProgress(0, vertices.size(), "Creating initial clusters.");
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double maxZ = Double.MIN_VALUE;
        int clusterSeqNo = 0;
        for (GraphVertex vertex : vertices) {
            Group vertexCluster = new Group("Cluster " + clusterSeqNo++, graphData);
            vertexCluster.setKind(GroupKind.CLUSTER);
            vertexCluster.addNode(vertex);
            clusters.add(vertexCluster);
            maxX = Math.max(maxX, vertex.getPosition().x);
            maxY = Math.max(maxY, vertex.getPosition().y);
            maxZ = Math.max(maxZ, vertex.getPosition().z);
            minX = Math.min(minX, vertex.getPosition().x);
            minY = Math.min(minY, vertex.getPosition().y);
            minZ = Math.min(minZ, vertex.getPosition().z);
        }
        double layoutDistanceX = Math.abs(maxX - minX);
        double layoutDistanceY = Math.abs(maxY - minY);
        double layoutDistanceZ = Math.abs(maxZ - minZ);
        double layoutDiagonal = Math.sqrt(layoutDistanceX * layoutDistanceX + layoutDistanceZ * layoutDistanceZ + layoutDistanceY * layoutDistanceY);
        int initialNumOfClusters = clusters.size();
        int numberOfClustersWithNodes = initialNumOfClusters;
        double maxDistanceToAutoMerge = layoutDiagonal * maxDistancePercentToAutoMerge;
        double minClusterDistanceAbsoulte = layoutDiagonal * minClusterDistancePercent;
        int iterationNumber = 0;
        int mergesInIteration = 0;
        do {
            iterationNumber++;
            mergesInIteration = 0;
            HashMap<Group, RadiusOfGroup> fixedBarycenters = new HashMap<Group, RadiusOfGroup>();
            setProgress(initialNumOfClusters - numberOfClustersWithNodes, initialNumOfClusters, "Creating clusters");
            System.out.println("Num of non-empty clusters: " + numberOfClustersWithNodes);
            MinMaxPriorityQueue<ClusterPair> nearestPairs = MinMaxPriorityQueue.maximumSize(100).create();
            int highestClusterWithRadius = -1;
            for (int a = clusters.size() - 1; a >= 0; a--) {
                Group clusterA = clusters.get(a);
                if (clusterA.getNodes().size() > 0) {
                    RadiusOfGroup barycenterA = null;
                    if (a > highestClusterWithRadius) {
                        fixedBarycenters.put(clusterA, new RadiusOfGroup(clusterA.getNodes()));
                        highestClusterWithRadius = a;
                    } else {
                        barycenterA = fixedBarycenters.get(clusterA);
                    }
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    for (int b = a - 1; b >= 0; b--) {
                        Group clusterB = clusters.get(b);
                        if (clusterB.getNodes().size() > 0) {
                            RadiusOfGroup barycenterB = null;
                            if (b > highestClusterWithRadius) {
                                fixedBarycenters.put(clusterB, new RadiusOfGroup(clusterB.getNodes()));
                                highestClusterWithRadius = b;
                            } else {
                                barycenterB = fixedBarycenters.get(clusterB);
                            }
                            ClusterPair clusterPair = new ClusterPair(clusterA, clusterB, barycenterA, barycenterB);
                            double pairDistance = clusterPair.getEucDistanceBetweenBarycenters();
                            if (pairDistance <= minClusterDistanceAbsoulte) {
                                if (pairDistance < maxDistanceToAutoMerge) {
                                    if (numberOfClustersWithNodes > numberOfClusters) {
                                        mergeClusters(clusterB, clusterA);
                                        mergesInIteration++;
                                        numberOfClustersWithNodes--;
                                    }
                                } else {
                                    nearestPairs.add(clusterPair);
                                }
                            }
                        }
                    }
                }
            }
            int mergesIndSecondPhase = 0;
            double nearestPairDistance = -1;
            do {
                if (numberOfClustersWithNodes > numberOfClusters) {
                    ClusterPair pair = nearestPairs.poll();
                    if (pair != null) {
                        double pairDistance = pair.getEucDistanceBetweenBarycenters();
                        if (nearestPairDistance == -1) {
                            nearestPairDistance = pairDistance;
                        }
                        if (mergesIndSecondPhase == 0 || ((pairDistance / nearestPairDistance) - 1 <= 0.01)) {
                            Group sourceGroup = pair.clusterA;
                            Group targetGroup = pair.clusterB;
                            if (targetGroup.getNodes().size() == 0) {
                                sourceGroup = pair.clusterB;
                                targetGroup = pair.clusterA;
                            }
                            if (sourceGroup.getNodes().size() > 0 && targetGroup.getNodes().size() > 0) {
                                if (numberOfClustersWithNodes > numberOfClusters) {
                                    mergeClusters(sourceGroup, targetGroup);
                                    numberOfClustersWithNodes--;
                                    mergesInIteration++;
                                    mergesIndSecondPhase++;
                                }
                            }
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            } while (true);
            System.out.println(String.format("%d merges in iteration %d", mergesInIteration, iterationNumber));
        } while (mergesInIteration > 0);
        for (int i = clusters.size() - 1; i > 0; i--) {
            Group group = clusters.get(i);
            if (group.getNodes().size() == 0) {
                clusters.remove(i);
            } else {
                System.out.println(String.format("%s with %d nodes.", group.getName(), group.getNodes().size()));
            }
        }
        setProgress(1, 1, stopwatch.stop().toString());
        return clusters;
    }
}
