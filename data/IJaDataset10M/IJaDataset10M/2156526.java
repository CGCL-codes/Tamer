package util.misc;

public class Edge {

    private double value = 0d;

    private Node source = null;

    private Node destination = null;

    public Edge() {
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Node getSource() {
        return source;
    }

    public void setDestination(Node destination) {
        this.destination = destination;
    }

    public Node getDestination() {
        return destination;
    }

    public boolean equals(Edge e) {
        if (e.getSource() == source && e.getDestination() == destination) {
            return true;
        }
        return false;
    }

    public String toString() {
        return source.getName() + " --> " + destination.getName() + ": " + value;
    }
}
