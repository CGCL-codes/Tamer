package lab.data.distance;

import java.util.Vector;

/**
 * 
 * @author jonathan
 *
 * Norm 2 distance function
 */
public class NormTwoDistance<T extends Number> implements DistanceFunction<T> {

    /**
	 * Computes the l2 distance between a and b
	 */
    public double distance(Vector<T> a, Vector<T> b) {
        double res = 0.0;
        if (a.size() == 0) {
            throw new RuntimeException("Vector is empty - is that possible?");
        }
        if (a.size() != b.size()) {
            throw new RuntimeException("PROBLEM - Size of vectors is not the same");
        }
        for (int i = 0; i < a.size(); i++) {
            res += Math.pow((a.elementAt(i).doubleValue() - b.elementAt(i).doubleValue()), 2);
        }
        return Math.sqrt(res);
    }
}
