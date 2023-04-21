package netexporter;

import java.util.ArrayList;

/**
 *
 * @author fbustos
 */
public class GenericEdge {

    private ArrayList<GenericArc> arcs;

    public GenericEdge(double nv1, double nv2, double weight) {
        arcs = new ArrayList<GenericArc>();
        arcs.add(new GenericArc(nv1, nv2, weight));
    }

    public GenericEdge(GenericArc arc) {
        arcs = new ArrayList<GenericArc>();
        arcs.add(arc);
    }

    public ArrayList<GenericArc> getArcs() {
        return arcs;
    }

    public void setArcs(ArrayList<GenericArc> arcs) {
        this.arcs = arcs;
    }

    public void addArc(GenericArc arc) {
        this.arcs.add(arc);
    }

    public double getAddedWeight() {
        double total = 0;
        if (arcs != null) {
            for (int i = 0; i < arcs.size(); ++i) {
                total = total + arcs.get(i).getWeight();
            }
        }
        return total;
    }

    public double getAverageWeight() {
        double total = 0;
        if (arcs != null) {
            int size = arcs.size();
            if (size > 0) {
                for (int i = 0; i < arcs.size(); ++i) {
                    total = total + arcs.get(i).getWeight();
                }
                total = total / size;
            }
        }
        return total;
    }

    @Override
    public String toString() {
        return "E";
    }
}
