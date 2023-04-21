package netexporter;

/**
 *
 * @author fbustos
 */
public class GenericArc {

    private double nv1;

    private double nv2;

    private double weight;

    public GenericArc(double nv1, double nv2, double weight) {
        this.nv1 = nv1;
        this.nv2 = nv2;
        this.weight = weight;
    }

    public double getNv1() {
        return nv1;
    }

    public void setNv1(double nv1) {
        this.nv1 = nv1;
    }

    public double getNv2() {
        return nv2;
    }

    public void setNv2(double nv2) {
        this.nv2 = nv2;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String toString() {
        return nv1 + " " + nv2 + " " + weight;
    }
}
