package mkmtests;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;
import tools.Export;
import net.sf.javaml.clustering.MultiKMeans;
import net.sf.javaml.clustering.evaluation.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.data.DatasetLoader;
import net.sf.javaml.distance.EuclideanDistance;

public class IterationTest {

    public static void main(String[] args) throws Exception {
        int minIterations = Integer.parseInt(args[0]);
        int maxIterations = Integer.parseInt(args[1]);
        new IterationTest(minIterations, maxIterations);
    }

    public IterationTest(int minIterations, int maxIterations) throws Exception {
        int numberOfClusters = 4;
        int repeats = 10;
        ClusterEvaluation ce = new Tau(new EuclideanDistance());
        Dataset data = DatasetLoader.loadDataset(new File("syntheticdatasets/clusternumber4.data"));
        System.out.println("repeats  = 10\t dm = euclidean\t ce = tau(euclidean)");
        System.out.println("Iterations\t Clusters\t Clustersizes");
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(12);
        for (int i = minIterations; i <= maxIterations; i++) {
            MultiKMeans clustering = new MultiKMeans(numberOfClusters, i, repeats, new EuclideanDistance(), ce);
            Dataset[] clusters = clustering.executeClustering(data);
            System.out.print(i + "\t" + clusters.length + "\t");
            for (int j = 0; j < clusters.length; j++) {
                System.out.print(nf.format(clusters[j].size()) + "\t");
            }
            System.out.println();
            Export e = new Export();
            e.export(clusters, "results_mkm/" + MultiKMeans.class.getName().substring(MultiKMeans.class.getName().lastIndexOf(('.') + 1)) + "_iterations_" + i);
        }
    }
}
