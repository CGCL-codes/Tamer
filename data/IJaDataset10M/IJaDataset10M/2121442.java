package jbnc.graphs;

/**
 *  Description of the Class
 *
 * @author     Jarek Sacha
 * @since      June 1, 1999
 */
public class EdgeWithWeight extends Edge {

    /** */
    protected double weight;

    /** */
    public EdgeWithWeight() {
        super();
        weight = 0;
    }

    /**
   * @param  x  Description of Parameter
   * @param  y  Description of Parameter
   */
    public EdgeWithWeight(Vertex x, Vertex y) {
        super(x, y);
        weight = 0;
    }

    /**
   * @param  x       Description of Parameter
   * @param  y       Description of Parameter
   * @param  weight  Description of Parameter
   */
    public EdgeWithWeight(Vertex x, Vertex y, double weight) {
        super(x, y);
        this.weight = weight;
    }

    /**
   * @param  weight  The new Weight value
   */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
   * @return    The Weight value
   */
    public double getWeight() {
        return weight;
    }

    /**
   *  Description of the Method
   *
   * @return    Description of the Returned Value
   */
    public String toString() {
        String inS = (in == null) ? "null" : in.toString();
        String outS = (out == null) ? "null" : out.toString();
        String s = "(" + inS + "->" + outS + ", " + weight + ")";
        return s;
    }
}
