package maltcms.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import maltcms.commands.distances.ArrayDot;
import maltcms.commands.distances.ArrayLp;
import maltcms.datastructures.array.Sparse;
import org.slf4j.Logger;
import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import cross.Logging;

/**
 * Class providing static utility methods for
 * {@link maltcms.datastructures.array.Sparse} arrays.
 * 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 * 
 */
public class SparseTools {

    protected static final ExecutorService es = Executors.newFixedThreadPool(4);

    public static final Map<Sparse, Double> normalized = Collections.synchronizedMap(new HashMap<Sparse, Double>());

    private static Logger log = Logging.getLogger(SparseTools.class);

    public static double arccos(final Sparse s, final Sparse t) {
        return Math.acos(SparseTools.cos(s, t));
    }

    public static double cos(final Sparse s, final Sparse t) {
        synchronized (SparseTools.normalized) {
            if (!SparseTools.normalized.containsKey(s)) {
                SparseTools.normalized.put(s, SparseTools.norm(s));
            }
            if (!SparseTools.normalized.containsKey(t)) {
                SparseTools.normalized.put(t, SparseTools.norm(t));
            }
        }
        final double dot = SparseTools.dot(s, t);
        if (dot == 0.0d) {
            return Math.cos(dot);
        }
        double prod = 0.0d;
        synchronized (SparseTools.normalized) {
            prod = SparseTools.normalized.get(s) * SparseTools.normalized.get(t);
        }
        final double div = dot / prod;
        double cos = Math.cos(div);
        if (Double.isNaN(cos)) {
            cos = 0.0d;
        }
        return cos;
    }

    public static Array[] create(final List<Array> indices, final List<Array> values, final int minindex, final int maxindex, final int nbins, final double massPrecision) {
        if (indices.size() == values.size()) {
            final Iterator<Array> idx = indices.iterator();
            final Iterator<Array> vls = values.iterator();
            final Array[] s = new Array[indices.size()];
            int i = 0;
            SparseTools.log.info("Building {} Sparse Arrays!", values.size());
            while (idx.hasNext() && vls.hasNext()) {
                try {
                    final Array a = idx.next();
                    final Array b = vls.next();
                    idx.remove();
                    vls.remove();
                    s[i++] = new Sparse(a, b, minindex, maxindex, nbins, massPrecision);
                } catch (final ClassCastException cce) {
                    cce.printStackTrace();
                }
            }
            return s;
        }
        throw new IllegalArgumentException("Number of elements in argument lists differ!");
    }

    public static List<Array> createAsList(final List<Array> indices, final List<Array> values, final int minindex, final int maxindex, final int nbins, final double massPrecision) {
        final Array[] a = SparseTools.create(indices, values, minindex, maxindex, nbins, massPrecision);
        SparseTools.log.info("Length of Array[] created: {}", a.length);
        final ArrayList<Array> arr = new ArrayList<Array>(a.length);
        for (final Array element : a) {
            arr.add(element);
        }
        return arr;
    }

    public static double dist(final Sparse s, final Sparse t, final double type) {
        final ArrayLp alp = new ArrayLp();
        return alp.apply(0, 0, -1, -1, s, t);
    }

    public static double dot(final Sparse s, final Sparse t) {
        final ArrayDot ad = new ArrayDot();
        return ad.apply(0, 0, -1, -1, s, t);
    }

    public static Sparse mult(final Sparse s, final double d) {
        final IndexIterator sk = s.getIndexIterator();
        while (sk.hasNext()) {
            sk.setDoubleCurrent(sk.getDoubleNext() * d);
        }
        return s;
    }

    public static double norm(final Sparse s) {
        final double norm = Math.sqrt(SparseTools.dot(s, s));
        return norm;
    }

    public static Sparse randomGaussian(final int minindex, final int size, final double mean, final double stddev) {
        final Sparse s = new Sparse(size, minindex, minindex + size - 1);
        for (int i = 0; i < size; i++) {
            s.set(i, (ArrayTools.nextGaussian() - mean) * stddev);
        }
        return s;
    }

    public static Sparse randomUniform(final int minindex, final int size, final double mean, final double scale) {
        final Sparse s = new Sparse(size, minindex, minindex + size - 1);
        for (int i = 0; i < size; i++) {
            s.set(i, (ArrayTools.nextUniform() - mean) * scale);
        }
        return s;
    }
}
