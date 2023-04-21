package aqbctests;

import java.text.NumberFormat;
import java.util.Locale;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.AQBC;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.data.DatasetLoader;
import org.apache.commons.math.stat.StatUtils;

public class SpeedTest {

    public static void main(String[] args) throws Exception {
        new SpeedTest(new AQBC());
    }

    public SpeedTest(Clusterer c) throws Exception {
        System.out.println("Speedtest: " + c.getClass().getName());
        System.out.println("Size\tMean C\tMin C\tMax C\tMean T\tMin T\tMax T");
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(2);
        for (int i = 100; i <= 25000; i += 100) {
            Dataset data = DatasetLoader.loadDataset("data/3D_clustersize" + i + ".data");
            double[] times = new double[10];
            double[] clusterNumber = new double[10];
            for (int j = 0; j < 5; j++) {
                long time = System.currentTimeMillis();
                Dataset[] clusters = c.executeClustering(data);
                times[j] = System.currentTimeMillis() - time;
                clusterNumber[j] = clusters.length;
            }
            System.out.println((4 * i) + "\t" + nf.format(StatUtils.mean(clusterNumber)) + "\t" + nf.format(StatUtils.min(clusterNumber)) + "\t" + nf.format(StatUtils.max(clusterNumber)) + "\t" + nf.format(StatUtils.mean(times)) + "\t" + nf.format(StatUtils.min(times)) + "\t" + nf.format(StatUtils.max(times)));
        }
        for (int i = 26000; i <= 100000; i += 1000) {
            Dataset data = DatasetLoader.loadDataset("data/clustersize" + i + ".data");
            double[] times = new double[10];
            double[] clusterNumber = new double[10];
            for (int j = 0; j < 5; j++) {
                long time = System.currentTimeMillis();
                Dataset[] clusters = c.executeClustering(data);
                times[j] = System.currentTimeMillis() - time;
                clusterNumber[j] = clusters.length;
            }
            System.out.println((4 * i) + "\t" + nf.format(StatUtils.mean(clusterNumber)) + "\t" + nf.format(StatUtils.min(clusterNumber)) + "\t" + nf.format(StatUtils.max(clusterNumber)) + "\t" + nf.format(StatUtils.mean(times)) + "\t" + nf.format(StatUtils.min(times)) + "\t" + nf.format(StatUtils.max(times)));
        }
    }
}
