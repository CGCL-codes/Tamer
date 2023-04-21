package moea.commons;

import java.util.ArrayList;

public class ObjectiveVector extends ArrayList<Double> {

    private static final long serialVersionUID = 1L;

    public ObjectiveVector() {
        super();
        for (int i = 0; i < Entity.N; ++i) super.add(0.0);
    }

    public ObjectiveVector(int n) {
        super();
        for (int i = 0; i < n; ++i) super.add(0.0);
    }

    public ObjectiveVector(ObjectiveVector src) {
        super();
        for (int i = 0; i < src.size(); i++) add(new Double(src.get(i)));
    }

    public double euclideanDistance(ObjectiveVector other) {
        double sum = 0;
        for (int i = 0; i < size(); i++) sum += ((get(i) - other.get(i)) * (get(i) - other.get(i)));
        return Math.sqrt(sum);
    }
}
