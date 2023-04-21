package kmeanstests;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;
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

public class UciDatasetTest {

    public static void main(String[] args) throws Exception {
        int repeats = Integer.parseInt(args[0]);
        new UciDatasetTest(repeats);
    }

    public UciDatasetTest(int repeats) throws Exception {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(12);
        System.out.println("repeats = " + repeats + "\t dm = euclidean");
        System.out.print("BalanceScaleWeight-Distance \t");
        for (int j = 0; j <= repeats; j++) {
            Dataset data = DatasetLoader.loadDataset(new File("ucidatasets/BalanceScaleWeight-Distance.data"));
            KMeans clustering = new KMeans(3, 100, new EuclideanDistance());
            long time = System.currentTimeMillis();
            Dataset[] clusters = clustering.executeClustering(data);
            long timediff = System.currentTimeMillis() - time;
            System.out.println("aantal clusters " + clusters.length + "\t repeat " + j + "\t time " + timediff);
            for (int k = 0; k < clusters.length; k++) {
                System.out.print(nf.format(clusters[k].size()) + "\t");
            }
            System.out.println();
            double score;
            ClusterEvaluation ce;
            DistanceMeasure dm = new EuclideanDistance();
            long time2 = System.currentTimeMillis();
            ce = new AICScore();
            score = ce.score(clusters);
            long timediff2 = System.currentTimeMillis() - time2;
            System.out.print(nf.format(score) + "\t time " + timediff2 + "\t");
            long time3 = System.currentTimeMillis();
            ce = new BICScore();
            score = ce.score(clusters);
            long timediff3 = System.currentTimeMillis() - time3;
            System.out.print(nf.format(score) + "\t time " + timediff3 + "\t");
            long time4 = System.currentTimeMillis();
            ce = new CIndex(dm);
            score = ce.score(clusters);
            long timediff4 = System.currentTimeMillis() - time4;
            System.out.print(nf.format(score) + "\t time " + timediff4 + "\t");
            long time5 = System.currentTimeMillis();
            ce = new Gamma(dm);
            score = ce.score(clusters);
            long timediff5 = System.currentTimeMillis() - time5;
            System.out.print(nf.format(score) + "\t time " + timediff5 + "\t");
            long time6 = System.currentTimeMillis();
            ce = new GPlus(dm);
            score = ce.score(clusters);
            long timediff6 = System.currentTimeMillis() - time6;
            System.out.print(nf.format(score) + "\t time " + timediff6 + "\t");
            long time7 = System.currentTimeMillis();
            ce = new MinMaxCut(dm);
            score = ce.score(clusters);
            long timediff7 = System.currentTimeMillis() - time7;
            System.out.print(nf.format(score) + "\t time " + timediff7 + "\t");
            long time8 = System.currentTimeMillis();
            ce = new SumOfSquaredErrors(dm);
            score = ce.score(clusters);
            long timediff8 = System.currentTimeMillis() - time8;
            System.out.print(nf.format(score) + "\t time " + timediff8 + "\t");
            long time9 = System.currentTimeMillis();
            ce = new Tau(dm);
            score = ce.score(clusters);
            long timediff9 = System.currentTimeMillis() - time9;
            System.out.print(nf.format(score) + "\t time " + timediff9 + "\t");
            long time10 = System.currentTimeMillis();
            ce = new WB(dm);
            score = ce.score(clusters);
            long timediff10 = System.currentTimeMillis() - time10;
            System.out.print(nf.format(score) + "\t time " + timediff10 + "\t");
            System.out.println();
        }
        System.out.print("BUPALiverDisorders \t");
        for (int j = 0; j <= repeats; j++) {
            Dataset data = DatasetLoader.loadDataset(new File("ucidatasets/BUPALiverDisorders.data"), 6);
            KMeans clustering = new KMeans(2, 100, new EuclideanDistance());
            long time = System.currentTimeMillis();
            Dataset[] clusters = clustering.executeClustering(data);
            long timediff = System.currentTimeMillis() - time;
            System.out.print("aantal clusters " + clusters.length + "\t repeat " + j + "\t time " + timediff);
            System.out.println();
            int c1 = 0, c2 = 0;
            for (int k = 0; k < clusters.length; k++) {
                System.out.print("clustersize \t" + nf.format(clusters[k].size()));
                for (int l = 0; l < clusters[k].size(); l++) {
                    int classval = clusters[k].getInstance(l).getClassValue();
                    if (classval == 0) {
                        c1++;
                    } else if (classval == 1) {
                        c2++;
                    }
                }
                System.out.print("\t class distr: c1 \t" + c1 + "\t c2 \t" + c2);
                System.out.println();
                c1 = c2 = 0;
            }
            double score;
            ClusterEvaluation ce;
            DistanceMeasure dm = new EuclideanDistance();
            long time2 = System.currentTimeMillis();
            ce = new AICScore();
            score = ce.score(clusters);
            long timediff2 = System.currentTimeMillis() - time2;
            System.out.print(nf.format(score) + "\t time " + timediff2 + "\t");
            long time3 = System.currentTimeMillis();
            ce = new BICScore();
            score = ce.score(clusters);
            long timediff3 = System.currentTimeMillis() - time3;
            System.out.print(nf.format(score) + "\t time " + timediff3 + "\t");
            long time4 = System.currentTimeMillis();
            ce = new CIndex(dm);
            score = ce.score(clusters);
            long timediff4 = System.currentTimeMillis() - time4;
            System.out.print(nf.format(score) + "\t time " + timediff4 + "\t");
            long time5 = System.currentTimeMillis();
            ce = new Gamma(dm);
            score = ce.score(clusters);
            long timediff5 = System.currentTimeMillis() - time5;
            System.out.print(nf.format(score) + "\t time " + timediff5 + "\t");
            long time6 = System.currentTimeMillis();
            ce = new GPlus(dm);
            score = ce.score(clusters);
            long timediff6 = System.currentTimeMillis() - time6;
            System.out.print(nf.format(score) + "\t time " + timediff6 + "\t");
            long time7 = System.currentTimeMillis();
            ce = new MinMaxCut(dm);
            score = ce.score(clusters);
            long timediff7 = System.currentTimeMillis() - time7;
            System.out.print(nf.format(score) + "\t time " + timediff7 + "\t");
            long time8 = System.currentTimeMillis();
            ce = new SumOfSquaredErrors(dm);
            score = ce.score(clusters);
            long timediff8 = System.currentTimeMillis() - time8;
            System.out.print(nf.format(score) + "\t time " + timediff8 + "\t");
            long time9 = System.currentTimeMillis();
            ce = new Tau(dm);
            score = ce.score(clusters);
            long timediff9 = System.currentTimeMillis() - time9;
            System.out.print(nf.format(score) + "\t time " + timediff9 + "\t");
            long time10 = System.currentTimeMillis();
            ce = new WB(dm);
            score = ce.score(clusters);
            long timediff10 = System.currentTimeMillis() - time10;
            System.out.print(nf.format(score) + "\t time " + timediff10 + "\t");
            System.out.println();
        }
        System.out.print("CancerWisconson \t");
        for (int j = 0; j <= repeats; j++) {
            Dataset data = DatasetLoader.loadDataset(new File("ucidatasets/CancerWisconson.data"), 9);
            KMeans clustering = new KMeans(2, 100, new EuclideanDistance());
            long time = System.currentTimeMillis();
            Dataset[] clusters = clustering.executeClustering(data);
            long timediff = System.currentTimeMillis() - time;
            System.out.println("aantal clusters " + clusters.length + "\t repeat " + j + "\t time " + timediff);
            int c1 = 0, c2 = 0;
            for (int k = 0; k < clusters.length; k++) {
                System.out.print("clustersize \t" + nf.format(clusters[k].size()));
                for (int l = 0; l < clusters[k].size(); l++) {
                    int classval = clusters[k].getInstance(l).getClassValue();
                    if (classval == 2) {
                        c1++;
                    } else if (classval == 4) {
                        c2++;
                    }
                }
                System.out.print("\t class distr: c1 \t" + c1 + "\t c2 \t" + c2);
                System.out.println();
                c1 = c2 = 0;
            }
            double score;
            ClusterEvaluation ce;
            DistanceMeasure dm = new EuclideanDistance();
            long time2 = System.currentTimeMillis();
            ce = new AICScore();
            score = ce.score(clusters);
            long timediff2 = System.currentTimeMillis() - time2;
            System.out.print(nf.format(score) + "\t time " + timediff2 + "\t");
            long time3 = System.currentTimeMillis();
            ce = new BICScore();
            score = ce.score(clusters);
            long timediff3 = System.currentTimeMillis() - time3;
            System.out.print(nf.format(score) + "\t time " + timediff3 + "\t");
            long time4 = System.currentTimeMillis();
            ce = new CIndex(dm);
            score = ce.score(clusters);
            long timediff4 = System.currentTimeMillis() - time4;
            System.out.print(nf.format(score) + "\t time " + timediff4 + "\t");
            long time5 = System.currentTimeMillis();
            ce = new Gamma(dm);
            score = ce.score(clusters);
            long timediff5 = System.currentTimeMillis() - time5;
            System.out.print(nf.format(score) + "\t time " + timediff5 + "\t");
            long time6 = System.currentTimeMillis();
            ce = new GPlus(dm);
            score = ce.score(clusters);
            long timediff6 = System.currentTimeMillis() - time6;
            System.out.print(nf.format(score) + "\t time " + timediff6 + "\t");
            long time7 = System.currentTimeMillis();
            ce = new MinMaxCut(dm);
            score = ce.score(clusters);
            long timediff7 = System.currentTimeMillis() - time7;
            System.out.print(nf.format(score) + "\t time " + timediff7 + "\t");
            long time8 = System.currentTimeMillis();
            ce = new SumOfSquaredErrors(dm);
            score = ce.score(clusters);
            long timediff8 = System.currentTimeMillis() - time8;
            System.out.print(nf.format(score) + "\t time " + timediff8 + "\t");
            long time9 = System.currentTimeMillis();
            ce = new Tau(dm);
            score = ce.score(clusters);
            long timediff9 = System.currentTimeMillis() - time9;
            System.out.print(nf.format(score) + "\t time " + timediff9 + "\t");
            long time10 = System.currentTimeMillis();
            ce = new WB(dm);
            score = ce.score(clusters);
            long timediff10 = System.currentTimeMillis() - time10;
            System.out.print(nf.format(score) + "\t time " + timediff10 + "\t");
            System.out.println();
        }
        System.out.print("ContraceptiveMethodChoice \t");
        for (int j = 0; j <= repeats; j++) {
            Dataset data = DatasetLoader.loadDataset(new File("ucidatasets/ContraceptiveMethodChoice.data"), 9);
            KMeans clustering = new KMeans(3, 100, new EuclideanDistance());
            long time = System.currentTimeMillis();
            Dataset[] clusters = clustering.executeClustering(data);
            long timediff = System.currentTimeMillis() - time;
            System.out.println("aantal clusters " + clusters.length + "\t repeat " + j + "\t time " + timediff);
            int c1 = 0, c2 = 0, c3 = 0;
            for (int k = 0; k < clusters.length; k++) {
                System.out.print("\t clustersize \t" + nf.format(clusters[k].size()));
                for (int l = 0; l < clusters[k].size(); l++) {
                    int classval = clusters[k].getInstance(l).getClassValue();
                    if (classval == 1) {
                        c1++;
                    } else if (classval == 2) {
                        c2++;
                    } else if (classval == 3) {
                        c3++;
                    }
                }
                System.out.print("\t class distr: c1 \t" + c1 + "\t c2 \t" + c2 + "\t c3 \t" + c3);
                System.out.println();
                c1 = c2 = c3 = 0;
            }
            double score;
            ClusterEvaluation ce;
            DistanceMeasure dm = new EuclideanDistance();
            long time2 = System.currentTimeMillis();
            ce = new AICScore();
            score = ce.score(clusters);
            long timediff2 = System.currentTimeMillis() - time2;
            System.out.print(nf.format(score) + "\t time " + timediff2 + "\t");
            long time3 = System.currentTimeMillis();
            ce = new BICScore();
            score = ce.score(clusters);
            long timediff3 = System.currentTimeMillis() - time3;
            System.out.print(nf.format(score) + "\t time " + timediff3 + "\t");
            long time4 = System.currentTimeMillis();
            ce = new CIndex(dm);
            score = ce.score(clusters);
            long timediff4 = System.currentTimeMillis() - time4;
            System.out.print(nf.format(score) + "\t time " + timediff4 + "\t");
            long time5 = System.currentTimeMillis();
            ce = new Gamma(dm);
            score = ce.score(clusters);
            long timediff5 = System.currentTimeMillis() - time5;
            System.out.print(nf.format(score) + "\t time " + timediff5 + "\t");
            long time6 = System.currentTimeMillis();
            ce = new GPlus(dm);
            score = ce.score(clusters);
            long timediff6 = System.currentTimeMillis() - time6;
            System.out.print(nf.format(score) + "\t time " + timediff6 + "\t");
            long time7 = System.currentTimeMillis();
            ce = new MinMaxCut(dm);
            score = ce.score(clusters);
            long timediff7 = System.currentTimeMillis() - time7;
            System.out.print(nf.format(score) + "\t time " + timediff7 + "\t");
            long time8 = System.currentTimeMillis();
            ce = new SumOfSquaredErrors(dm);
            score = ce.score(clusters);
            long timediff8 = System.currentTimeMillis() - time8;
            System.out.print(nf.format(score) + "\t time " + timediff8 + "\t");
            long time9 = System.currentTimeMillis();
            ce = new Tau(dm);
            score = ce.score(clusters);
            long timediff9 = System.currentTimeMillis() - time9;
            System.out.print(nf.format(score) + "\t time " + timediff9 + "\t");
            long time10 = System.currentTimeMillis();
            ce = new WB(dm);
            score = ce.score(clusters);
            long timediff10 = System.currentTimeMillis() - time10;
            System.out.print(nf.format(score) + "\t time " + timediff10 + "\t");
            System.out.println();
        }
        System.out.print("GlassIdentification \t");
        for (int j = 0; j <= repeats; j++) {
            Dataset data = DatasetLoader.loadDataset(new File("ucidatasets/GlassIdentification.data"), 9);
            KMeans clustering = new KMeans(6, 100, new EuclideanDistance());
            long time = System.currentTimeMillis();
            Dataset[] clusters = clustering.executeClustering(data);
            long timediff = System.currentTimeMillis() - time;
            System.out.println("aantal clusters " + clusters.length + "\t repeat " + j + "\t time " + timediff);
            int c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0, c6 = 0;
            for (int k = 0; k < clusters.length; k++) {
                System.out.print("\t clustersize \t" + nf.format(clusters[k].size()));
                for (int l = 0; l < clusters[k].size(); l++) {
                    int classval = clusters[k].getInstance(l).getClassValue();
                    if (classval == 0) {
                        c1++;
                    } else if (classval == 1) {
                        c2++;
                    } else if (classval == 2) {
                        c3++;
                    } else if (classval == 3) {
                        c4++;
                    } else if (classval == 4) {
                        c5++;
                    } else if (classval == 5) {
                        c6++;
                    }
                }
                System.out.print("\t class distr: c1 \t" + c1 + "\t c2 \t" + c2 + "\t c3 \t" + c3 + "\t c4 \t" + c4 + "\t c5 \t" + c5 + "\t c6 \t" + c6);
                System.out.println();
                c1 = c2 = c3 = c4 = c5 = c6 = 0;
            }
            double score;
            ClusterEvaluation ce;
            DistanceMeasure dm = new EuclideanDistance();
            long time2 = System.currentTimeMillis();
            ce = new AICScore();
            score = ce.score(clusters);
            long timediff2 = System.currentTimeMillis() - time2;
            System.out.print(nf.format(score) + "\t time " + timediff2 + "\t");
            long time3 = System.currentTimeMillis();
            ce = new BICScore();
            score = ce.score(clusters);
            long timediff3 = System.currentTimeMillis() - time3;
            System.out.print(nf.format(score) + "\t time " + timediff3 + "\t");
            long time4 = System.currentTimeMillis();
            ce = new CIndex(dm);
            score = ce.score(clusters);
            long timediff4 = System.currentTimeMillis() - time4;
            System.out.print(nf.format(score) + "\t time " + timediff4 + "\t");
            long time5 = System.currentTimeMillis();
            ce = new Gamma(dm);
            score = ce.score(clusters);
            long timediff5 = System.currentTimeMillis() - time5;
            System.out.print(nf.format(score) + "\t time " + timediff5 + "\t");
            long time6 = System.currentTimeMillis();
            ce = new GPlus(dm);
            score = ce.score(clusters);
            long timediff6 = System.currentTimeMillis() - time6;
            System.out.print(nf.format(score) + "\t time " + timediff6 + "\t");
            long time7 = System.currentTimeMillis();
            ce = new MinMaxCut(dm);
            score = ce.score(clusters);
            long timediff7 = System.currentTimeMillis() - time7;
            System.out.print(nf.format(score) + "\t time " + timediff7 + "\t");
            long time8 = System.currentTimeMillis();
            ce = new SumOfSquaredErrors(dm);
            score = ce.score(clusters);
            long timediff8 = System.currentTimeMillis() - time8;
            System.out.print(nf.format(score) + "\t time " + timediff8 + "\t");
            long time9 = System.currentTimeMillis();
            ce = new Tau(dm);
            score = ce.score(clusters);
            long timediff9 = System.currentTimeMillis() - time9;
            System.out.print(nf.format(score) + "\t time " + timediff9 + "\t");
            long time10 = System.currentTimeMillis();
            ce = new WB(dm);
            score = ce.score(clusters);
            long timediff10 = System.currentTimeMillis() - time10;
            System.out.print(nf.format(score) + "\t time " + timediff10 + "\t");
            System.out.println();
        }
        System.out.print("HabermanSurvival \t");
        for (int j = 0; j <= repeats; j++) {
            Dataset data = DatasetLoader.loadDataset(new File("ucidatasets/HabermanSurvival.data"), 3);
            KMeans clustering = new KMeans(2, 100, new EuclideanDistance());
            long time = System.currentTimeMillis();
            Dataset[] clusters = clustering.executeClustering(data);
            long timediff = System.currentTimeMillis() - time;
            System.out.println("aantal clusters " + clusters.length + "\t repeat " + j + "\t time " + timediff);
            int c1 = 0, c2 = 0;
            for (int k = 0; k < clusters.length; k++) {
                System.out.print("clustersize \t" + nf.format(clusters[k].size()));
                for (int l = 0; l < clusters[k].size(); l++) {
                    int classval = clusters[k].getInstance(l).getClassValue();
                    if (classval == 1) {
                        c1++;
                    } else if (classval == 2) {
                        c2++;
                    }
                }
                System.out.print("\t class distr: c1 \t" + c1 + "\t c2 \t" + c2);
                System.out.println();
                c1 = c2 = 0;
            }
            double score;
            ClusterEvaluation ce;
            DistanceMeasure dm = new EuclideanDistance();
            long time2 = System.currentTimeMillis();
            ce = new AICScore();
            score = ce.score(clusters);
            long timediff2 = System.currentTimeMillis() - time2;
            System.out.print(nf.format(score) + "\t time " + timediff2 + "\t");
            long time3 = System.currentTimeMillis();
            ce = new BICScore();
            score = ce.score(clusters);
            long timediff3 = System.currentTimeMillis() - time3;
            System.out.print(nf.format(score) + "\t time " + timediff3 + "\t");
            long time4 = System.currentTimeMillis();
            ce = new CIndex(dm);
            score = ce.score(clusters);
            long timediff4 = System.currentTimeMillis() - time4;
            System.out.print(nf.format(score) + "\t time " + timediff4 + "\t");
            long time5 = System.currentTimeMillis();
            ce = new Gamma(dm);
            score = ce.score(clusters);
            long timediff5 = System.currentTimeMillis() - time5;
            System.out.print(nf.format(score) + "\t time " + timediff5 + "\t");
            long time6 = System.currentTimeMillis();
            ce = new GPlus(dm);
            score = ce.score(clusters);
            long timediff6 = System.currentTimeMillis() - time6;
            System.out.print(nf.format(score) + "\t time " + timediff6 + "\t");
            long time7 = System.currentTimeMillis();
            ce = new MinMaxCut(dm);
            score = ce.score(clusters);
            long timediff7 = System.currentTimeMillis() - time7;
            System.out.print(nf.format(score) + "\t time " + timediff7 + "\t");
            long time8 = System.currentTimeMillis();
            ce = new SumOfSquaredErrors(dm);
            score = ce.score(clusters);
            long timediff8 = System.currentTimeMillis() - time8;
            System.out.print(nf.format(score) + "\t time " + timediff8 + "\t");
            long time9 = System.currentTimeMillis();
            ce = new Tau(dm);
            score = ce.score(clusters);
            long timediff9 = System.currentTimeMillis() - time9;
            System.out.print(nf.format(score) + "\t time " + timediff9 + "\t");
            long time10 = System.currentTimeMillis();
            ce = new WB(dm);
            score = ce.score(clusters);
            long timediff10 = System.currentTimeMillis() - time10;
            System.out.print(nf.format(score) + "\t time " + timediff10 + "\t");
            System.out.println();
        }
        System.out.print("Hayes-Roth \t");
        for (int j = 0; j <= repeats; j++) {
            Dataset data = DatasetLoader.loadDataset(new File("ucidatasets/Hayes-Roth.data"), 5);
            KMeans clustering = new KMeans(3, 100, new EuclideanDistance());
            long time = System.currentTimeMillis();
            Dataset[] clusters = clustering.executeClustering(data);
            long timediff = System.currentTimeMillis() - time;
            System.out.println("aantal clusters " + clusters.length + "\t repeat " + j + "\t time " + timediff);
            int c1 = 0, c2 = 0, c3 = 0;
            for (int k = 0; k < clusters.length; k++) {
                System.out.print("\t clustersize \t" + nf.format(clusters[k].size()));
                for (int l = 0; l < clusters[k].size(); l++) {
                    int classval = clusters[k].getInstance(l).getClassValue();
                    if (classval == 0) {
                        c1++;
                    } else if (classval == 1) {
                        c2++;
                    } else if (classval == 2) {
                        c3++;
                    }
                }
                System.out.print("\t class distr: c1 \t" + c1 + "\t c2 \t" + c2 + "\t c3 \t" + c3);
                System.out.println();
                c1 = c2 = c3 = 0;
            }
            double score;
            ClusterEvaluation ce;
            DistanceMeasure dm = new EuclideanDistance();
            long time2 = System.currentTimeMillis();
            ce = new AICScore();
            score = ce.score(clusters);
            long timediff2 = System.currentTimeMillis() - time2;
            System.out.print(nf.format(score) + "\t time " + timediff2 + "\t");
            long time3 = System.currentTimeMillis();
            ce = new BICScore();
            score = ce.score(clusters);
            long timediff3 = System.currentTimeMillis() - time3;
            System.out.print(nf.format(score) + "\t time " + timediff3 + "\t");
            long time4 = System.currentTimeMillis();
            ce = new CIndex(dm);
            score = ce.score(clusters);
            long timediff4 = System.currentTimeMillis() - time4;
            System.out.print(nf.format(score) + "\t time " + timediff4 + "\t");
            long time5 = System.currentTimeMillis();
            ce = new Gamma(dm);
            score = ce.score(clusters);
            long timediff5 = System.currentTimeMillis() - time5;
            System.out.print(nf.format(score) + "\t time " + timediff5 + "\t");
            long time6 = System.currentTimeMillis();
            ce = new GPlus(dm);
            score = ce.score(clusters);
            long timediff6 = System.currentTimeMillis() - time6;
            System.out.print(nf.format(score) + "\t time " + timediff6 + "\t");
            long time7 = System.currentTimeMillis();
            ce = new MinMaxCut(dm);
            score = ce.score(clusters);
            long timediff7 = System.currentTimeMillis() - time7;
            System.out.print(nf.format(score) + "\t time " + timediff7 + "\t");
            long time8 = System.currentTimeMillis();
            ce = new SumOfSquaredErrors(dm);
            score = ce.score(clusters);
            long timediff8 = System.currentTimeMillis() - time8;
            System.out.print(nf.format(score) + "\t time " + timediff8 + "\t");
            long time9 = System.currentTimeMillis();
            ce = new Tau(dm);
            score = ce.score(clusters);
            long timediff9 = System.currentTimeMillis() - time9;
            System.out.print(nf.format(score) + "\t time " + timediff9 + "\t");
            long time10 = System.currentTimeMillis();
            ce = new WB(dm);
            score = ce.score(clusters);
            long timediff10 = System.currentTimeMillis() - time10;
            System.out.print(nf.format(score) + "\t time " + timediff10 + "\t");
            System.out.println();
        }
        System.out.print("LungCancer \t");
        for (int j = 0; j <= repeats; j++) {
            Dataset data = DatasetLoader.loadDataset(new File("ucidatasets/LungCancer.data"));
            KMeans clustering = new KMeans(3, 100, new EuclideanDistance());
            long time = System.currentTimeMillis();
            Dataset[] clusters = clustering.executeClustering(data);
            long timediff = System.currentTimeMillis() - time;
            System.out.println("aantal clusters " + clusters.length + "\t repeat " + j + "\t time " + timediff);
            for (int k = 0; k < clusters.length; k++) {
                System.out.print(nf.format(clusters[k].size()) + "\t");
            }
            System.out.println();
            double score;
            ClusterEvaluation ce;
            DistanceMeasure dm = new EuclideanDistance();
            long time2 = System.currentTimeMillis();
            ce = new AICScore();
            score = ce.score(clusters);
            long timediff2 = System.currentTimeMillis() - time2;
            System.out.print(nf.format(score) + "\t time " + timediff2 + "\t");
            long time3 = System.currentTimeMillis();
            ce = new BICScore();
            score = ce.score(clusters);
            long timediff3 = System.currentTimeMillis() - time3;
            System.out.print(nf.format(score) + "\t time " + timediff3 + "\t");
            long time4 = System.currentTimeMillis();
            ce = new CIndex(dm);
            score = ce.score(clusters);
            long timediff4 = System.currentTimeMillis() - time4;
            System.out.print(nf.format(score) + "\t time " + timediff4 + "\t");
            long time5 = System.currentTimeMillis();
            ce = new Gamma(dm);
            score = ce.score(clusters);
            long timediff5 = System.currentTimeMillis() - time5;
            System.out.print(nf.format(score) + "\t time " + timediff5 + "\t");
            long time6 = System.currentTimeMillis();
            ce = new GPlus(dm);
            score = ce.score(clusters);
            long timediff6 = System.currentTimeMillis() - time6;
            System.out.print(nf.format(score) + "\t time " + timediff6 + "\t");
            long time7 = System.currentTimeMillis();
            ce = new MinMaxCut(dm);
            score = ce.score(clusters);
            long timediff7 = System.currentTimeMillis() - time7;
            System.out.print(nf.format(score) + "\t time " + timediff7 + "\t");
            long time8 = System.currentTimeMillis();
            ce = new SumOfSquaredErrors(dm);
            score = ce.score(clusters);
            long timediff8 = System.currentTimeMillis() - time8;
            System.out.print(nf.format(score) + "\t time " + timediff8 + "\t");
            long time9 = System.currentTimeMillis();
            ce = new Tau(dm);
            score = ce.score(clusters);
            long timediff9 = System.currentTimeMillis() - time9;
            System.out.print(nf.format(score) + "\t time " + timediff9 + "\t");
            long time10 = System.currentTimeMillis();
            ce = new WB(dm);
            score = ce.score(clusters);
            long timediff10 = System.currentTimeMillis() - time10;
            System.out.print(nf.format(score) + "\t time " + timediff10 + "\t");
            System.out.println();
        }
        System.out.print("PimaIndiansDiabetes \t");
        for (int j = 0; j <= repeats; j++) {
            Dataset data = DatasetLoader.loadDataset(new File("ucidatasets/PimaIndiansDiabetes.data"), 8);
            KMeans clustering = new KMeans(2, 100, new EuclideanDistance());
            long time = System.currentTimeMillis();
            Dataset[] clusters = clustering.executeClustering(data);
            long timediff = System.currentTimeMillis() - time;
            System.out.println("aantal clusters " + clusters.length + "\t repeat " + j + "\t time " + timediff);
            int c1 = 0, c2 = 0;
            for (int k = 0; k < clusters.length; k++) {
                System.out.print("clustersize \t" + nf.format(clusters[k].size()));
                for (int l = 0; l < clusters[k].size(); l++) {
                    int classval = clusters[k].getInstance(l).getClassValue();
                    if (classval == 0) {
                        c1++;
                    } else if (classval == 1) {
                        c2++;
                    }
                }
                System.out.print("\t class distr: c1 \t" + c1 + "\t c2 \t" + c2);
                System.out.println();
                c1 = c2 = 0;
            }
            double score;
            ClusterEvaluation ce;
            DistanceMeasure dm = new EuclideanDistance();
            long time2 = System.currentTimeMillis();
            ce = new AICScore();
            score = ce.score(clusters);
            long timediff2 = System.currentTimeMillis() - time2;
            System.out.print(nf.format(score) + "\t time " + timediff2 + "\t");
            long time3 = System.currentTimeMillis();
            ce = new BICScore();
            score = ce.score(clusters);
            long timediff3 = System.currentTimeMillis() - time3;
            System.out.print(nf.format(score) + "\t time " + timediff3 + "\t");
            long time4 = System.currentTimeMillis();
            ce = new CIndex(dm);
            score = ce.score(clusters);
            long timediff4 = System.currentTimeMillis() - time4;
            System.out.print(nf.format(score) + "\t time " + timediff4 + "\t");
            long time5 = System.currentTimeMillis();
            ce = new Gamma(dm);
            score = ce.score(clusters);
            long timediff5 = System.currentTimeMillis() - time5;
            System.out.print(nf.format(score) + "\t time " + timediff5 + "\t");
            long time6 = System.currentTimeMillis();
            ce = new GPlus(dm);
            score = ce.score(clusters);
            long timediff6 = System.currentTimeMillis() - time6;
            System.out.print(nf.format(score) + "\t time " + timediff6 + "\t");
            long time7 = System.currentTimeMillis();
            ce = new MinMaxCut(dm);
            score = ce.score(clusters);
            long timediff7 = System.currentTimeMillis() - time7;
            System.out.print(nf.format(score) + "\t time " + timediff7 + "\t");
            long time8 = System.currentTimeMillis();
            ce = new SumOfSquaredErrors(dm);
            score = ce.score(clusters);
            long timediff8 = System.currentTimeMillis() - time8;
            System.out.print(nf.format(score) + "\t time " + timediff8 + "\t");
            long time9 = System.currentTimeMillis();
            ce = new Tau(dm);
            score = ce.score(clusters);
            long timediff9 = System.currentTimeMillis() - time9;
            System.out.print(nf.format(score) + "\t time " + timediff9 + "\t");
            long time10 = System.currentTimeMillis();
            ce = new WB(dm);
            score = ce.score(clusters);
            long timediff10 = System.currentTimeMillis() - time10;
            System.out.print(nf.format(score) + "\t time " + timediff10 + "\t");
            System.out.println();
        }
        System.out.print("SPECTFHeartTesting \t");
        for (int j = 0; j <= repeats; j++) {
            Dataset data = DatasetLoader.loadDataset(new File("ucidatasets/SPECTFHeartTesting.data"));
            KMeans clustering = new KMeans(2, 100, new EuclideanDistance());
            long time = System.currentTimeMillis();
            Dataset[] clusters = clustering.executeClustering(data);
            long timediff = System.currentTimeMillis() - time;
            System.out.println("aantal clusters " + clusters.length + "\t repeat " + j + "\t time " + timediff);
            for (int k = 0; k < clusters.length; k++) {
                System.out.print(nf.format(clusters[k].size()) + "\t");
            }
            System.out.println();
            double score;
            ClusterEvaluation ce;
            DistanceMeasure dm = new EuclideanDistance();
            long time2 = System.currentTimeMillis();
            ce = new AICScore();
            score = ce.score(clusters);
            long timediff2 = System.currentTimeMillis() - time2;
            System.out.print(nf.format(score) + "\t time " + timediff2 + "\t");
            long time3 = System.currentTimeMillis();
            ce = new BICScore();
            score = ce.score(clusters);
            long timediff3 = System.currentTimeMillis() - time3;
            System.out.print(nf.format(score) + "\t time " + timediff3 + "\t");
            long time4 = System.currentTimeMillis();
            ce = new CIndex(dm);
            score = ce.score(clusters);
            long timediff4 = System.currentTimeMillis() - time4;
            System.out.print(nf.format(score) + "\t time " + timediff4 + "\t");
            long time5 = System.currentTimeMillis();
            ce = new Gamma(dm);
            score = ce.score(clusters);
            long timediff5 = System.currentTimeMillis() - time5;
            System.out.print(nf.format(score) + "\t time " + timediff5 + "\t");
            long time6 = System.currentTimeMillis();
            ce = new GPlus(dm);
            score = ce.score(clusters);
            long timediff6 = System.currentTimeMillis() - time6;
            System.out.print(nf.format(score) + "\t time " + timediff6 + "\t");
            long time7 = System.currentTimeMillis();
            ce = new MinMaxCut(dm);
            score = ce.score(clusters);
            long timediff7 = System.currentTimeMillis() - time7;
            System.out.print(nf.format(score) + "\t time " + timediff7 + "\t");
            long time8 = System.currentTimeMillis();
            ce = new SumOfSquaredErrors(dm);
            score = ce.score(clusters);
            long timediff8 = System.currentTimeMillis() - time8;
            System.out.print(nf.format(score) + "\t time " + timediff8 + "\t");
            long time9 = System.currentTimeMillis();
            ce = new Tau(dm);
            score = ce.score(clusters);
            long timediff9 = System.currentTimeMillis() - time9;
            System.out.print(nf.format(score) + "\t time " + timediff9 + "\t");
            long time10 = System.currentTimeMillis();
            ce = new WB(dm);
            score = ce.score(clusters);
            long timediff10 = System.currentTimeMillis() - time10;
            System.out.print(nf.format(score) + "\t time " + timediff10 + "\t");
            System.out.println();
        }
        System.out.print("SPECTFHeartTraining \t");
        for (int j = 0; j <= repeats; j++) {
            Dataset data = DatasetLoader.loadDataset(new File("ucidatasets/SPECTFHeartTraining.data"));
            KMeans clustering = new KMeans(2, 100, new EuclideanDistance());
            long time = System.currentTimeMillis();
            Dataset[] clusters = clustering.executeClustering(data);
            long timediff = System.currentTimeMillis() - time;
            System.out.println("aantal clusters " + clusters.length + "\t repeat " + j + "\t time " + timediff);
            for (int k = 0; k < clusters.length; k++) {
                System.out.print(nf.format(clusters[k].size()) + "\t");
            }
            System.out.println();
            double score;
            ClusterEvaluation ce;
            DistanceMeasure dm = new EuclideanDistance();
            long time2 = System.currentTimeMillis();
            ce = new AICScore();
            score = ce.score(clusters);
            long timediff2 = System.currentTimeMillis() - time2;
            System.out.print(nf.format(score) + "\t time " + timediff2 + "\t");
            long time3 = System.currentTimeMillis();
            ce = new BICScore();
            score = ce.score(clusters);
            long timediff3 = System.currentTimeMillis() - time3;
            System.out.print(nf.format(score) + "\t time " + timediff3 + "\t");
            long time4 = System.currentTimeMillis();
            ce = new CIndex(dm);
            score = ce.score(clusters);
            long timediff4 = System.currentTimeMillis() - time4;
            System.out.print(nf.format(score) + "\t time " + timediff4 + "\t");
            long time5 = System.currentTimeMillis();
            ce = new Gamma(dm);
            score = ce.score(clusters);
            long timediff5 = System.currentTimeMillis() - time5;
            System.out.print(nf.format(score) + "\t time " + timediff5 + "\t");
            long time6 = System.currentTimeMillis();
            ce = new GPlus(dm);
            score = ce.score(clusters);
            long timediff6 = System.currentTimeMillis() - time6;
            System.out.print(nf.format(score) + "\t time " + timediff6 + "\t");
            long time7 = System.currentTimeMillis();
            ce = new MinMaxCut(dm);
            score = ce.score(clusters);
            long timediff7 = System.currentTimeMillis() - time7;
            System.out.print(nf.format(score) + "\t time " + timediff7 + "\t");
            long time8 = System.currentTimeMillis();
            ce = new SumOfSquaredErrors(dm);
            score = ce.score(clusters);
            long timediff8 = System.currentTimeMillis() - time8;
            System.out.print(nf.format(score) + "\t time " + timediff8 + "\t");
            long time9 = System.currentTimeMillis();
            ce = new Tau(dm);
            score = ce.score(clusters);
            long timediff9 = System.currentTimeMillis() - time9;
            System.out.print(nf.format(score) + "\t time " + timediff9 + "\t");
            long time10 = System.currentTimeMillis();
            ce = new WB(dm);
            score = ce.score(clusters);
            long timediff10 = System.currentTimeMillis() - time10;
            System.out.print(nf.format(score) + "\t time " + timediff10 + "\t");
            System.out.println();
        }
        System.out.print("SPECTHeartTesting \t");
        for (int j = 0; j <= repeats; j++) {
            Dataset data = DatasetLoader.loadDataset(new File("ucidatasets/SPECTHeartTesting.data"));
            KMeans clustering = new KMeans(2, 100, new EuclideanDistance());
            long time = System.currentTimeMillis();
            Dataset[] clusters = clustering.executeClustering(data);
            long timediff = System.currentTimeMillis() - time;
            System.out.println("aantal clusters " + clusters.length + "\t repeat " + j + "\t time " + timediff);
            for (int k = 0; k < clusters.length; k++) {
                System.out.print(nf.format(clusters[k].size()) + "\t");
            }
            System.out.println();
            double score;
            ClusterEvaluation ce;
            DistanceMeasure dm = new EuclideanDistance();
            long time2 = System.currentTimeMillis();
            ce = new AICScore();
            score = ce.score(clusters);
            long timediff2 = System.currentTimeMillis() - time2;
            System.out.print(nf.format(score) + "\t time " + timediff2 + "\t");
            long time3 = System.currentTimeMillis();
            ce = new BICScore();
            score = ce.score(clusters);
            long timediff3 = System.currentTimeMillis() - time3;
            System.out.print(nf.format(score) + "\t time " + timediff3 + "\t");
            long time4 = System.currentTimeMillis();
            ce = new CIndex(dm);
            score = ce.score(clusters);
            long timediff4 = System.currentTimeMillis() - time4;
            System.out.print(nf.format(score) + "\t time " + timediff4 + "\t");
            long time5 = System.currentTimeMillis();
            ce = new Gamma(dm);
            score = ce.score(clusters);
            long timediff5 = System.currentTimeMillis() - time5;
            System.out.print(nf.format(score) + "\t time " + timediff5 + "\t");
            long time6 = System.currentTimeMillis();
            ce = new GPlus(dm);
            score = ce.score(clusters);
            long timediff6 = System.currentTimeMillis() - time6;
            System.out.print(nf.format(score) + "\t time " + timediff6 + "\t");
            long time7 = System.currentTimeMillis();
            ce = new MinMaxCut(dm);
            score = ce.score(clusters);
            long timediff7 = System.currentTimeMillis() - time7;
            System.out.print(nf.format(score) + "\t time " + timediff7 + "\t");
            long time8 = System.currentTimeMillis();
            ce = new SumOfSquaredErrors(dm);
            score = ce.score(clusters);
            long timediff8 = System.currentTimeMillis() - time8;
            System.out.print(nf.format(score) + "\t time " + timediff8 + "\t");
            long time9 = System.currentTimeMillis();
            ce = new Tau(dm);
            score = ce.score(clusters);
            long timediff9 = System.currentTimeMillis() - time9;
            System.out.print(nf.format(score) + "\t time " + timediff9 + "\t");
            long time10 = System.currentTimeMillis();
            ce = new WB(dm);
            score = ce.score(clusters);
            long timediff10 = System.currentTimeMillis() - time10;
            System.out.print(nf.format(score) + "\t time " + timediff10 + "\t");
            System.out.println();
        }
        System.out.print("SPECTHeartTraining \t");
        for (int j = 0; j <= repeats; j++) {
            Dataset data = DatasetLoader.loadDataset(new File("ucidatasets/SPECTHeartTraining.data"));
            KMeans clustering = new KMeans(2, 100, new EuclideanDistance());
            long time = System.currentTimeMillis();
            Dataset[] clusters = clustering.executeClustering(data);
            long timediff = System.currentTimeMillis() - time;
            System.out.println("aantal clusters " + clusters.length + "\t repeat " + j + "\t time " + timediff);
            for (int k = 0; k < clusters.length; k++) {
                System.out.print(nf.format(clusters[k].size()) + "\t");
            }
            System.out.println();
            double score;
            ClusterEvaluation ce;
            DistanceMeasure dm = new EuclideanDistance();
            long time2 = System.currentTimeMillis();
            ce = new AICScore();
            score = ce.score(clusters);
            long timediff2 = System.currentTimeMillis() - time2;
            System.out.print(nf.format(score) + "\t time " + timediff2 + "\t");
            long time3 = System.currentTimeMillis();
            ce = new BICScore();
            score = ce.score(clusters);
            long timediff3 = System.currentTimeMillis() - time3;
            System.out.print(nf.format(score) + "\t time " + timediff3 + "\t");
            long time4 = System.currentTimeMillis();
            ce = new CIndex(dm);
            score = ce.score(clusters);
            long timediff4 = System.currentTimeMillis() - time4;
            System.out.print(nf.format(score) + "\t time " + timediff4 + "\t");
            long time5 = System.currentTimeMillis();
            ce = new Gamma(dm);
            score = ce.score(clusters);
            long timediff5 = System.currentTimeMillis() - time5;
            System.out.print(nf.format(score) + "\t time " + timediff5 + "\t");
            long time6 = System.currentTimeMillis();
            ce = new GPlus(dm);
            score = ce.score(clusters);
            long timediff6 = System.currentTimeMillis() - time6;
            System.out.print(nf.format(score) + "\t time " + timediff6 + "\t");
            long time7 = System.currentTimeMillis();
            ce = new MinMaxCut(dm);
            score = ce.score(clusters);
            long timediff7 = System.currentTimeMillis() - time7;
            System.out.print(nf.format(score) + "\t time " + timediff7 + "\t");
            long time8 = System.currentTimeMillis();
            ce = new SumOfSquaredErrors(dm);
            score = ce.score(clusters);
            long timediff8 = System.currentTimeMillis() - time8;
            System.out.print(nf.format(score) + "\t time " + timediff8 + "\t");
            long time9 = System.currentTimeMillis();
            ce = new Tau(dm);
            score = ce.score(clusters);
            long timediff9 = System.currentTimeMillis() - time9;
            System.out.print(nf.format(score) + "\t time " + timediff9 + "\t");
            long time10 = System.currentTimeMillis();
            ce = new WB(dm);
            score = ce.score(clusters);
            long timediff10 = System.currentTimeMillis() - time10;
            System.out.print(nf.format(score) + "\t time " + timediff10 + "\t");
            System.out.println();
        }
    }
}
