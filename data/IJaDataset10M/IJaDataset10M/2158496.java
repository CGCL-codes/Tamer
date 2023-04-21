package kmeanstests;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;
import tools.Export;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.AICScore;
import net.sf.javaml.clustering.evaluation.BICScore;
import net.sf.javaml.clustering.evaluation.CIndex;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.GPlus;
import net.sf.javaml.clustering.evaluation.Gamma;
import net.sf.javaml.clustering.evaluation.MinMaxCut;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.clustering.evaluation.Tau;
import net.sf.javaml.clustering.evaluation.WB;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.data.DatasetLoader;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;

public class DatasetTestDim {

    public static void main(String[] args) throws Exception {
        int repeats = Integer.parseInt(args[0]);
        new DatasetTestDim(repeats);
    }

    public DatasetTestDim(int repeats) throws Exception {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(12);
        System.out.println("repeats = " + repeats + "\t dm = euclidean");
        for (int i = 2; i <= 1024; i *= 2) {
            for (int j = 1; j <= repeats; j++) {
                Dataset data = DatasetLoader.loadDataset(new File("syntheticdatasets/dim" + i + ".data"));
                KMeans clustering = new KMeans();
                long time = System.currentTimeMillis();
                Dataset[] clusters = clustering.executeClustering(data);
                long timediff = System.currentTimeMillis() - time;
                System.out.print("dim " + i + "\t repeat " + j + "\t time " + timediff + "\t clustersizes \t");
                for (int k = 0; k < clusters.length; k++) {
                    System.out.print(nf.format(clusters[k].size()) + "\t");
                }
                double score;
                ClusterEvaluation ce;
                DistanceMeasure dm = new EuclideanDistance();
                long time2 = System.currentTimeMillis();
                ce = new AICScore();
                score = ce.score(clusters);
                long timediff2 = System.currentTimeMillis() - time2;
                System.out.print("aic \t" + nf.format(score) + "\t time \t" + timediff2 + "\t");
                long time3 = System.currentTimeMillis();
                ce = new BICScore();
                score = ce.score(clusters);
                long timediff3 = System.currentTimeMillis() - time3;
                System.out.print("bic \t" + nf.format(score) + "\t time \t" + timediff3 + "\t");
                long time4 = System.currentTimeMillis();
                ce = new CIndex(dm);
                score = ce.score(clusters);
                long timediff4 = System.currentTimeMillis() - time4;
                System.out.print("CI \t" + nf.format(score) + "\t time \t" + timediff4 + "\t");
                long time5 = System.currentTimeMillis();
                ce = new Gamma(dm);
                score = ce.score(clusters);
                long timediff5 = System.currentTimeMillis() - time5;
                System.out.print("Gamma \t" + nf.format(score) + "\t time \t" + timediff5 + "\t");
                long time6 = System.currentTimeMillis();
                ce = new GPlus(dm);
                score = ce.score(clusters);
                long timediff6 = System.currentTimeMillis() - time6;
                System.out.print("G+ \t" + nf.format(score) + "\t time \t" + timediff6 + "\t");
                long time7 = System.currentTimeMillis();
                ce = new MinMaxCut(dm);
                score = ce.score(clusters);
                long timediff7 = System.currentTimeMillis() - time7;
                System.out.print("MMC \t" + nf.format(score) + "\t time \t" + timediff7 + "\t");
                long time8 = System.currentTimeMillis();
                ce = new SumOfSquaredErrors(dm);
                score = ce.score(clusters);
                long timediff8 = System.currentTimeMillis() - time8;
                System.out.print("SSE \t" + nf.format(score) + "\t time \t" + timediff8 + "\t");
                long time9 = System.currentTimeMillis();
                ce = new Tau(dm);
                score = ce.score(clusters);
                long timediff9 = System.currentTimeMillis() - time9;
                System.out.print("Tau \t" + nf.format(score) + "\t time \t" + timediff9 + "\t");
                long time10 = System.currentTimeMillis();
                ce = new WB(dm);
                score = ce.score(clusters);
                long timediff10 = System.currentTimeMillis() - time10;
                System.out.print("WB \t" + nf.format(score) + "\t time \t" + timediff10 + "\t");
                System.out.println();
                Export e = new Export();
                e.export(clusters, "results_kmeans/" + KMeans.class.getName().substring(KMeans.class.getName().lastIndexOf('.') + 1) + "dim" + i + "repeat" + j);
            }
        }
    }
}
