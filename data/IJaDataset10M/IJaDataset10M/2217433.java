package graphs4students.structures;

/**
 *
 * @author Marcos Felipe
 */
public class Edge {

    private Node neighbour;

    private boolean weighted;

    private int weight;

    public Edge(Node neighbour) {
        this.neighbour = neighbour;
        weighted = false;
    }

    public Edge(Node neighbour, int weight) {
        this.neighbour = neighbour;
        this.weighted = true;
        this.weight = weight;
    }

    public Node getNeighbour() {
        return neighbour;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isWeighted() {
        return weighted;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            return neighbour.equals(((Edge) obj).getNeighbour());
        }
        return neighbour.equals(obj);
    }

    @Override
    public String toString() {
        return neighbour.getName() + (weighted ? "Weight: " + weight : "");
    }
}
