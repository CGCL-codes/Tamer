package jalds.alds.ds.graphs;

/**
 * Represents a Vertex in a graph.
 * 
 * @author Devender
 * 
 */
public final class Vertex {

    private final String name;

    public Vertex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Vertex)) {
            return false;
        }
        Vertex other = (Vertex) obj;
        return (this.name.equals(other.name));
    }

    public int hashCode() {
        int result = 19;
        if (name != null) {
            result = 31 + name.hashCode();
        }
        return result;
    }
}
