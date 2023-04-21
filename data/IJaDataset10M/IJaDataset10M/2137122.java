package org.egonet.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class KPlexes<N> {

    public Map<N, Integer> connectionsByNode(Map<N, Set<N>> graph) {
        Map<N, Integer> result = Maps.newHashMap();
        for (N n : graph.keySet()) {
            result.put(n, graph.get(n).size());
        }
        return result;
    }

    public Map<N, Integer> connectednessByNode(Map<N, Set<N>> graph) {
        Map<N, Integer> connectionsByNode = connectionsByNode(graph);
        Map<N, Integer> results = Maps.newHashMap();
        for (N n1 : graph.keySet()) {
            List<Integer> connectednessOfConnections = Lists.newArrayList();
            for (N n2 : graph.get(n1)) {
                connectednessOfConnections.add(connectionsByNode.get(n2));
            }
            Collections.sort(connectednessOfConnections);
            Collections.reverse(connectednessOfConnections);
            Integer result = 0;
            for (Integer i = 0; i < connectednessOfConnections.size(); i++) {
                if (i < connectednessOfConnections.get(i)) {
                    result = i + 1;
                }
            }
            results.put(n1, result);
        }
        return results;
    }

    public Map<N, Integer> connectionsWithinSubgroup(Map<N, Set<N>> graph, Set<N> subgroup) {
        Map<N, Integer> result = Maps.newHashMap();
        for (N n : graph.keySet()) {
            result.put(n, Sets.intersection(graph.get(n), subgroup).size());
        }
        return result;
    }

    public Integer highestConnectedness(Map<N, Set<N>> graph) {
        Integer highest = 0;
        for (Integer connectedness : connectednessByNode(graph).values()) {
            if (connectedness > highest) {
                highest = connectedness;
            }
        }
        return highest;
    }

    public Set<N> meetConnectednessThreshold(Map<N, Set<N>> graph, Integer threshold) {
        Set<N> result = Sets.newHashSet();
        Map<N, Integer> connectedness = connectednessByNode(graph);
        for (N n : graph.keySet()) {
            if (connectedness.get(n) > threshold - 1) {
                result.add(n);
            }
        }
        return result;
    }

    public Integer largestPossibleKPlex(Map<N, Set<N>> graph, Integer k) {
        return highestConnectedness(graph) + k;
    }

    public Set<N> criticalNodesInKPlex(Map<N, Set<N>> graph, Set<N> kplex, Integer k) {
        Map<N, Integer> connectionsWithinKPlex = connectionsWithinSubgroup(graph, kplex);
        Set<N> criticalNodes = Sets.newHashSet();
        for (N n : kplex) {
            Integer connections = connectionsWithinKPlex.get(n);
            if (connections == null) {
                throw new RuntimeException("null connections for " + n + " in " + kplex + " of graph " + graph);
            }
            if (connections < kplex.size() - k + 1) {
                criticalNodes.add(n);
            }
        }
        return criticalNodes;
    }

    public Set<N> nodesThatCanBeAddedToKPlex(Map<N, Set<N>> graph, Set<N> kplex, Integer k) {
        if (kplex.isEmpty()) {
            if (k > 0) {
                return graph.keySet();
            } else {
                return new HashSet<N>();
            }
        }
        Set<N> criticalNodes = criticalNodesInKPlex(graph, kplex, k);
        if (criticalNodes.isEmpty()) {
            Map<N, Integer> connectionsWithinKPlex = connectionsWithinSubgroup(graph, kplex);
            Set<N> neighbors = Sets.newHashSet();
            for (N n : graph.keySet()) {
                if (connectionsWithinKPlex.get(n) > 0) {
                    neighbors.add(n);
                }
            }
            neighbors.removeAll(kplex);
            return Sets.difference(neighbors, kplex);
        }
        Set<N> eligible = Sets.difference(graph.keySet(), kplex);
        for (N n : criticalNodes) {
            eligible = Sets.intersection(eligible, graph.get(n));
        }
        return eligible;
    }

    public Map<N, Set<N>> createSubgraph(Map<N, Set<N>> graph, Set<N> nodes) {
        Map<N, Set<N>> subgraph = Maps.newHashMap();
        for (N n : nodes) {
            subgraph.put(n, Sets.intersection(graph.get(n), nodes));
        }
        return subgraph;
    }

    public Map<N, Set<N>> subgraphBoundingFinalKPlex(Map<N, Set<N>> graph, Set<N> kplex, Integer k, Integer targetSize) {
        Set<N> includeInSubgraph = Sets.union(kplex, Sets.intersection(meetConnectednessThreshold(graph, targetSize - k), nodesThatCanBeAddedToKPlex(graph, kplex, k)));
        return createSubgraph(graph, includeInSubgraph);
    }

    public N chooseNodeForInclusionInKPlex(Map<N, Set<N>> graph, Set<N> kplex, Integer k) {
        Integer highScore = 0;
        N choice = null;
        Map<N, Integer> connectedness = connectednessByNode(graph);
        Map<N, Integer> connectionsWithinKPlex = connectionsWithinSubgroup(graph, kplex);
        Set<N> addable = nodesThatCanBeAddedToKPlex(graph, kplex, k);
        for (N n : addable) {
            Integer score = connectedness.get(n) + connectionsWithinKPlex.get(n);
            if (score > highScore) {
                highScore = score;
                choice = n;
            }
        }
        return choice;
    }

    public Set<N> growKPlex(Map<N, Set<N>> graph, Set<N> kplex, Integer k, Integer targetSize) {
        Map<N, Set<N>> boundingGraph = subgraphBoundingFinalKPlex(graph, kplex, k, targetSize);
        N newNode = chooseNodeForInclusionInKPlex(graph, kplex, k);
        if (newNode == null) {
            return kplex;
        }
        Set<N> newKPlex = Sets.newHashSet();
        newKPlex.add(newNode);
        newKPlex.addAll(kplex);
        return growKPlex(boundingGraph, newKPlex, k, targetSize);
    }

    public Set<N> findLargeKPlex(Map<N, Set<N>> graph, Integer k) {
        Set<N> largestFound = Sets.newHashSet();
        for (Integer targetSize = largestPossibleKPlex(graph, k); targetSize > largestFound.size(); targetSize--) {
            Set<N> seeds = meetConnectednessThreshold(graph, targetSize - k);
            Map<N, Set<N>> boundedGraph = createSubgraph(graph, seeds);
            for (N seed : seeds) {
                Set<N> kplex = Sets.newHashSet();
                kplex.add(seed);
                kplex = growKPlex(boundedGraph, kplex, k, targetSize);
                if (kplex.size() > largestFound.size()) {
                    largestFound = kplex;
                }
            }
        }
        return largestFound;
    }
}
