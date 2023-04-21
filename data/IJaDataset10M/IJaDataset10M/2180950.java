package databionics.hut.ustar;

import databionics.hut.basic.DiscreteMatrix;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

/**
 *
 * @author lherrmann
 */
public class entropyFactory {

    /** */
    protected Map<Integer, Integer> m_distributions;

    /** Creates a new instance of entropyFactory */
    public entropyFactory() {
        m_distributions = new java.util.TreeMap<Integer, Integer>();
    }

    /**
     *
     */
    public double proceed(DiscreteMatrix m) {
        m_distributions.clear();
        int cls, freq;
        int size = 0;
        for (int i = 0; i < m.rows(); i++) for (int j = 0; j < m.columns(); j++) {
            cls = m.get(i, j);
            if (cls != 0) {
                Integer Cls = (Integer) cls;
                Integer Freq = m_distributions.get(Cls);
                freq = Freq == null ? 0 : (int) Freq;
                freq++;
                Freq = (Integer) freq;
                m_distributions.put(Cls, Freq);
                size++;
            }
        }
        final double dsize = size;
        Set<Integer> classes = m_distributions.keySet();
        Iterator<Integer> it = classes.iterator();
        double entropy = 0;
        int howmany = 0;
        while (it.hasNext()) {
            Integer Cls = it.next();
            Integer Freq = m_distributions.get(Cls);
            final double fraction = Freq.intValue() / dsize;
            entropy += -fraction * log(fraction);
            howmany++;
        }
        final double uniform_fraction = 1.0 / howmany;
        final double uniform_entropy = -log(uniform_fraction);
        final double result = (uniform_entropy > 0) ? (entropy / uniform_entropy) : 0;
        System.out.println("#classes " + howmany + "     entropy " + result);
        return result;
    }

    /**
     *
     */
    public static double log(double value) {
        return Math.log(value) / Math.log(2);
    }
}
