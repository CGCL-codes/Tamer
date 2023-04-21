package shellkk.qiq.jdm.common.distance;

import shellkk.qiq.jdm.common.Vector;
import shellkk.qiq.math.distance.Distance;

public class ChebychevDistance implements Distance {

    public double distance(Object o1, Object o2) {
        Vector v1 = (Vector) o1;
        Vector v2 = (Vector) o2;
        double d = 0;
        if (v1.number != null) {
            for (int i = 0; i < v1.number.length; i++) {
                double di = Math.abs(v1.number[i] - v2.number[i]);
                if (di > d) {
                    d = di;
                }
            }
        }
        if (v1.category != null) {
            for (int i = 0; i < v1.category.length; i++) {
                if (v1.category[i] != v2.category[i]) {
                    double di = Math.sqrt(v1.categoryWeight[i] + v1.categoryWeight[i]);
                    if (di > d) {
                        d = di;
                    }
                }
            }
        }
        return d;
    }
}
