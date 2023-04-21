package skellib.tools;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

public class PredictionSummary {

    Integer comparator = new Integer(0);

    Hashtable<String, Integer> map;

    Hashtable<Integer, String> rev_map;

    Vector<Prediction> p = new Vector<Prediction>();

    int count = 0;

    boolean closed = false;

    float[][] values;

    public class Prediction implements Comparable<Prediction> {

        float[] vals;

        String name;

        Prediction(String _name, float[] _vals) {
            vals = _vals;
            name = _name;
        }

        public int compareTo(Prediction o) {
            float v_i = vals[comparator];
            float v_j = o.vals[comparator];
            if (v_i < v_j) return -1;
            if (v_i == v_j) return 0;
            return 1;
        }

        public String toString() {
            String s = name;
            for (int i = 0; i < vals.length; i++) {
                s += "\t" + rev_map.get(i) + "\t" + vals[i];
            }
            return s;
        }
    }

    public PredictionSummary(String[] statistics) {
        map = new Hashtable<String, Integer>();
        rev_map = new Hashtable<Integer, String>();
        for (int i = 0; i < statistics.length; i++) {
            map.put(statistics[i], i);
            rev_map.put(i, statistics[i]);
        }
    }

    public void addPrediction(float[] vals) {
        addPrediction("" + count, vals);
    }

    public void addPrediction(String name, float[] vals) {
        if (!(vals.length == map.size())) {
            System.err.println("Incompatible prediction types!");
            return;
        }
        for (int i = 0; i < vals.length; i++) {
            if (Float.isNaN(vals[i])) {
                System.err.println("Prediction " + name + " skipped due to NANS");
                return;
            }
        }
        Prediction t = new Prediction(name, vals);
        p.add(t);
        closed = false;
        count++;
    }

    public void setComparator(String type) {
        comparator = map.get(type);
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < count; i++) {
            s += p.get(i).toString() + "\n";
        }
        return s;
    }

    public void sortAccordingTo(String property) {
        comparator = new Integer(map.get(property));
        Collections.sort(p);
    }

    public void close() {
        if (closed) return;
        values = new float[map.size()][count];
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < p.size(); j++) {
                values[i][j] = p.get(j).vals[i];
            }
        }
        closed = true;
    }

    public float[] getMeans() {
        if (!closed) {
            close();
        }
        float[] stats = new float[map.size()];
        for (int i = 0; i < stats.length; i++) {
            stats[i] = StatisticUtils.mean(values[i]);
        }
        return stats;
    }

    public float[] getErrorBarMean(String property) {
        if (!closed) {
            close();
        }
        int i = map.get(property);
        double mean = StatisticUtils.mean(values[i]);
        double esum = 0.0;
        for (int j = 0; j < values[i].length; j++) {
            esum += ((values[i][j] - mean) * (values[i][j] - mean));
        }
        esum /= (double) values[i].length;
        double stddev = Math.sqrt(esum);
        return new float[] { (float) mean, (float) stddev };
    }

    public float getCorrelation(String prop1, String prop2) {
        if (!closed) {
            close();
        }
        int i = map.get(prop1);
        int j = map.get(prop2);
        return (float) StatisticUtils.getCorrelationCoefficient(values[i], values[j]);
    }

    public float getRMSE(String prop1, String prop2) {
        if (!closed) {
            close();
        }
        int i = map.get(prop1);
        int j = map.get(prop2);
        return (float) StatisticUtils.rmsError(values[i], values[j]);
    }
}
