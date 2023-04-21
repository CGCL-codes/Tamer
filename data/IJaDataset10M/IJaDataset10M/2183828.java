package shellkk.qiq.math.ml.knn;

import org.apache.log4j.PropertyConfigurator;
import shellkk.qiq.math.StopException;
import shellkk.qiq.math.StopHandle;
import shellkk.qiq.math.distance.DefaultDistance;
import shellkk.qiq.math.distance.Distance;
import shellkk.qiq.math.ml.ArraySampleSet;
import shellkk.qiq.math.ml.LearnMachine;
import shellkk.qiq.math.ml.Sample;
import shellkk.qiq.math.ml.SampleSet;
import shellkk.qiq.math.ml.TrainException;

/**
 * K nearest neighbour
 * 
 * @author shellkk
 * 
 */
public class KNNRegress implements LearnMachine {

    protected Distance distance;

    protected int k = 1;

    protected Sample[] samples;

    public KNNRegress() {
        this(null);
    }

    public KNNRegress(Distance distance) {
        if (distance == null) distance = new DefaultDistance();
        this.distance = distance;
    }

    public double getY(Object x) {
        if (samples == null) {
            return Double.NaN;
        }
        int[] nn = new int[k];
        double[] nnd = new double[k];
        for (int i = 0; i < k; i++) {
            nn[i] = -1;
            nnd[i] = Double.MAX_VALUE;
        }
        int maxmin = 0;
        for (int i = 0; i < samples.length; i++) {
            double d = distance.distance(samples[i].x, x);
            if (d < nnd[maxmin]) {
                nn[maxmin] = i;
                nnd[maxmin] = d;
                int max = 0;
                for (int j = 0; j < k; j++) {
                    if (nnd[j] > nnd[max]) {
                        max = j;
                    }
                }
                maxmin = max;
            }
        }
        double w = 0;
        double y = 0;
        for (int i = 0; i < k; i++) {
            Sample s = samples[nn[i]];
            w += s.weight;
            y += s.weight * s.y;
        }
        y /= w;
        return y;
    }

    public void train(SampleSet sampleSet, StopHandle stopHandle) throws StopException, TrainException {
        samples = ArraySampleSet.toArraySampleSet(sampleSet, stopHandle).getSamples();
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Sample[] getSamples() {
        return samples;
    }

    public void setSamples(Sample[] samples) {
        this.samples = samples;
    }

    public static void main(String[] args) {
        try {
            PropertyConfigurator.configure("log4j.properties");
            Sample[] samples = new Sample[500];
            for (int i = 0; i < samples.length; i++) {
                double xi1 = Math.random();
                double xi2 = Math.random();
                double yi = foo(xi1, xi2);
                double[] Xi = { xi1, xi2 };
                samples[i] = new Sample(Xi, yi);
            }
            KNNRegress knn = new KNNRegress();
            knn.setK(1);
            long begin = System.currentTimeMillis();
            knn.train(new ArraySampleSet(samples), null);
            long end = System.currentTimeMillis();
            System.out.println("seconds used by training:" + ((end - begin) / 1000));
            double err1 = 0;
            for (int i = 0; i < samples.length; i++) {
                double yi = samples[i].y;
                double pi = knn.getY(samples[i].x);
                err1 += Math.abs(yi - pi);
            }
            err1 = err1 / samples.length;
            System.out.println("emp risk:" + err1);
            double err = 0;
            for (int i = 0; i < 1000; i++) {
                double xi1 = Math.random();
                double xi2 = Math.random();
                double yi = foo(xi1, xi2);
                double[] Xi = { xi1, xi2 };
                double pi = knn.getY(Xi);
                err += Math.abs(yi - pi);
            }
            err = err / 1000;
            System.out.println("expect risk:" + err);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double foo(double x1, double x2) {
        double v1 = (x1 * 20 - 10) * (x1 * 20 - 10);
        double v2 = (x2 * 20 - 10) * (x2 * 20 - 10);
        double d = Math.sqrt(v1 + v2);
        return d == 0 ? 1 : Math.sin(d) / d;
    }
}
