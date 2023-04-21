package vidis.util.graphs.graph;

/**
 * Represents a directed and weighted edge in a graph.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */
public class DirectedWeightedEdge extends DirectedEdge {

    /**
   * The weight of the edge.
   */
    protected double weight;

    /**
   * Creates a DirectedWeightedEdge object whose source and sink vertices and
   * weight are specified by the parameters.
   */
    public DirectedWeightedEdge(Vertex sourceVertex, Vertex sinkVertex, double weight) {
        super(sourceVertex, sinkVertex);
        this.weight = weight;
    }

    /**
    * Returns a String representation of the DirectedWeightedEdge.
    *
    * @return	The String representation of the Edge
    * @see		Vertex
    */
    public String toString() {
        return this.getSource().toString() + "->" + this.getSink().toString() + " (" + this.weight + ")";
    }

    /**
   * Returns the weight of the edge.
   */
    public double getWeight() {
        return this.weight;
    }

    /**
   * Sets the weight of the edge.
   *
   * @param   weight    The new weight of the edge
   */
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
