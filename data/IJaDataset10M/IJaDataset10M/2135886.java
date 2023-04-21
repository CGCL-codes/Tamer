package kmedoidtests;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;
import tools.Export;
import net.sf.javaml.clustering.KMedoids;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.data.DatasetLoader;
import net.sf.javaml.distance.EuclideanDistance;

public class IterationTest {

    public static void main(String[] args) throws Exception {
        int iterations = Integer.parseInt(args[0]);
        int repeats = Integer.parseInt(args[1]);
        int numberOfClusters = Integer.parseInt(args[2]);
        new IterationTest(iterations, repeats, numberOfClusters);
    }

    public IterationTest(int iterations, int repeats, int numberOfClusters) throws Exception {
        Dataset data = DatasetLoader.loadDataset(new File("syntheticdatasets/clusternumber" + numberOfClusters + ".data"));
        System.out.println("dm = euclidean" + "\t clusters = " + numberOfClusters + "\t iterations = " + iterations);
        System.out.println("Repeat\t Clusters");
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(12);
        for (int i = 0; i < repeats; i++) {
            KMedoids clustering = new KMedoids(numberOfClusters, iterations, new EuclideanDistance());
            Dataset[] clusters = clustering.executeClustering(data);
            System.out.print(i + "\t");
            for (int j = 0; j < clusters.length; j++) {
                System.out.print(nf.format(clusters[j].size()) + "\t");
            }
            System.out.println();
        }
    }
}
