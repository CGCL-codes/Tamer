package dbscantests;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;
import net.sf.javaml.clustering.DensityBasedSpatialClustering;
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
import tools.Export;

public class ParameterTestC4DS {

    public static void main(String[] args) throws Exception {
        double paramMin = Double.parseDouble(args[0]);
        double paramMax = Double.parseDouble(args[1]);
        double paramDelta = Double.parseDouble(args[2]);
        new ParameterTestC4DS(paramMin, paramMax, paramDelta);
    }

    public ParameterTestC4DS(double paramMin, double paramMax, double paramDelta) throws Exception {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(12);
        for (double j = paramMin; j <= paramMax; j += paramDelta) {
            Dataset data = DatasetLoader.loadDataset(new File("syntheticdatasets/clusterdensity500.data"));
            DensityBasedSpatialClustering clustering = new DensityBasedSpatialClustering(j, 6);
            long time = System.currentTimeMillis();
            Dataset[] clusters = clustering.executeClustering(data);
            long timediff = System.currentTimeMillis() - time;
            System.out.print("epsilon " + j + "\t time " + timediff + "\t clustersizes \t");
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
            e.export(clusters, "results_dbscan/" + DensityBasedSpatialClustering.class.getName().substring(DensityBasedSpatialClustering.class.getName().lastIndexOf('.') + 1) + "_c4_epsilon_" + j);
        }
    }
}
