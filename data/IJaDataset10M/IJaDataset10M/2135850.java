package netexporter;

import java.util.ArrayList;

/**
 *
 * @author fbustos
 */
public class EdgeWeightFiltered implements EdgeWeightInterface {

    private double minEdge;

    private double minVectorModule;

    public EdgeWeightFiltered(double minEdge, double minVectorModule) {
        this.minEdge = minEdge;
        this.minVectorModule = minVectorModule;
    }

    public Double transform(GenericEdge i) {
        ArrayList<GenericArc> ma = i.getArcs();
        int l = ma.size();
        ArrayList<Double> data = new ArrayList<Double>();
        for (int j = 0; j < l; ++j) {
            double w = ma.get(j).getWeight();
            if (w > minEdge) {
                data.add(new Double(w));
            }
        }
        if (data == null) {
            return 0.0;
        }
        int d = data.size();
        if (d == 0) {
            return 0.0;
        }
        double sum = 0;
        for (int j = 0; j < d; ++j) {
            sum += data.get(j);
        }
        return sum / d;
    }
}
