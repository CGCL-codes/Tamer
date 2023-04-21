package maltcms.db.similarities;

import ucar.ma2.Array;
import cross.datastructures.tuple.Tuple2D;

public class Similarity<T> {

    private double minSim = 0;

    private double maxSim = 1;

    public double get(Tuple2D<Array, Array> query, T t1) {
        return minSim;
    }

    public double get(T t1, T t2) {
        return minSim;
    }
}
