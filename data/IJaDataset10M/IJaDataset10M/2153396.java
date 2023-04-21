package prime;

import java.util.Map;
import java.util.TreeMap;

public class PrimeNode implements Comparable<PrimeNode> {

    private Double distance;

    private Integer sourceName;

    private Integer name;

    private Map<Integer, Double> edges;

    public PrimeNode(Integer nodename) {
        this.name = nodename;
        this.edges = new TreeMap<Integer, Double>();
    }

    public int size() {
        return edges.size();
    }

    public Integer getName() {
        return name;
    }

    public void addEdge(Integer edgeName, Double sim) {
        this.edges.put(edgeName, sim);
    }

    public Double getEdgeWeight(Integer edgeName) {
        return edges.get(edgeName);
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getSourceName() {
        return sourceName;
    }

    public void setSourceName(Integer sourceName) {
        this.sourceName = sourceName;
    }

    public Map<Integer, Double> getEdges() {
        return edges;
    }

    public int compareTo(PrimeNode node) {
        return this.getName().compareTo(node.getName());
    }
}
