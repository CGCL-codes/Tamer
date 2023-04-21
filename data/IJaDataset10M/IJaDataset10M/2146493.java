package shellkk.qiq.math.distance;

public class DefaultDistance implements Distance {

    public double distance(Object o1, Object o2) {
        double[] a = (double[]) o1;
        double[] b = (double[]) o2;
        double d = 0;
        double di = 0;
        for (int i = 0; i < a.length; i++) {
            di = a[i] - b[i];
            d += di * di;
        }
        return Math.sqrt(d);
    }
}
