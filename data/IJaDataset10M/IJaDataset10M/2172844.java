package perestrojka.common;

public class Vertex implements Comparable<Vertex> {

    private String format;

    private int distance;

    private Vertex predecessor;

    boolean isLossless;

    boolean isDistancePermanent;

    public Vertex(String format, boolean isLossless) {
        this.format = format;
        this.distance = Integer.MAX_VALUE;
        this.isLossless = isLossless;
        isDistancePermanent = false;
    }

    public void setPredecessor(Vertex predecessor) {
        this.predecessor = predecessor;
    }

    public Vertex getPredecessor() {
        return predecessor;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistancePermanent(boolean value) {
        isDistancePermanent = value;
    }

    public boolean isDistancePermanent() {
        return isDistancePermanent;
    }

    public boolean isLossless() {
        return isLossless;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Vertex)) return false;
        Vertex otherVertex = (Vertex) other;
        if (otherVertex.format.equalsIgnoreCase(format) && otherVertex.isLossless == isLossless) return true;
        return false;
    }

    @Override
    public String toString() {
        return "[" + format + "], lossless: " + isLossless + ", distance: " + distance;
    }

    public int compareTo(Vertex other) {
        if (this.distance < other.distance) return -1; else if (this.distance > other.distance) return 1; else {
            return format.compareTo(other.format);
        }
    }
}
