package hebClustering;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import hebClustering.clusteringAlgorithms.BisectingKMeansClustering;
import hebClustering.clusteringAlgorithms.EpsilonDeltaMeansClustering;
import hebClustering.clusteringAlgorithms.HierarchicalClustering;
import hebClustering.clusteringAlgorithms.HierarchicalKMeansClustering;
import hebClustering.clusteringAlgorithms.IClusteringAlgorithm;
import hebClustering.clusteringAlgorithms.KMeansClustering;
import hebClustering.clusteringAlgorithms.KMeansPPClustering;
import hebClustering.clusteringAlgorithms.MSTClustering;
import hebClustering.clusteringAlgorithms.SpectralClustering;
import hebClustering.documentTypes.Document;
import hebClustering.nlp.Corpus;
import hebClustering.nlp.HebrewNLP;
import hebClustering.nlp.INLP;
import hebClustering.vectorSpace.DocumentVector;
import hebClustering.vectorSpace.IVector;
import hebClustering.vectorSpace.MetricSpace;
import hebClustering.vectorSpace.distances.ChebyshevDistance;
import hebClustering.vectorSpace.distances.EuclidianDistance;
import hebClustering.vectorSpace.distances.IDistance;
import hebClustering.vectorSpace.distances.ManhattanDistance;

public class ClusteringMethodsTest {

    private static String methods[] = { "EDM", "MST", "KPP" };

    private static String distances[] = { "MAN", "CHE", "EUC" };

    private static String datasets[] = { "twins", "sinai", "lev", "random" };

    private static String outputFolder = "C:/Program Files/Eclipse/Workspace/heb-clustering/test results/";

    private static INLP nlp;

    public static void main(String[] args) {
        nlp = new HebrewNLP();
        printExternalCheckResults(5);
    }

    public static void printExternalCheckResults(int N) {
        MetricSpace space;
        int K;
        try {
            FileWriter fstream = new FileWriter(outputFolder + "external check results" + ".txt");
            BufferedWriter out = new BufferedWriter(fstream);
            for (String d : distances) {
                for (String set : datasets) {
                    for (String m : methods) {
                        if (set.equals("sinai")) K = 9; else if (set.equals("twins")) K = 10; else if (set.equals("lev")) K = 11; else if (set.equals("lev")) K = 8; else K = 8;
                        out.write("Method: " + m + ", Distance: " + d + ", Dataset: " + set + "=\r\n");
                        double MSTres = 0.0;
                        for (int i = 1; i <= N; i++) {
                            if (m.equals("MST")) {
                                if (i > 1) {
                                    out.write(MSTres + "\r\n");
                                } else {
                                    space = cluster(m, d, set);
                                    MSTres = getRandIndex(space, K);
                                    out.write(MSTres + "\r\n");
                                }
                                continue;
                            }
                            space = cluster(m, d, set);
                            out.write(getRandIndex(space, K) + "\r\n");
                        }
                        out.write("\r\n");
                        out.flush();
                        System.out.println("==========flushed- " + "Method: " + m + ", Distance: " + d + ", Dataset: " + set);
                    }
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private static MetricSpace cluster(String method, String dist, String set) {
        Corpus corpus = Corpus.getCorpus();
        Set<Document> documentSet = ((HebrewNLP) nlp).rawFilesToDocuments("../heb-clustering/lib/Datasets/" + set);
        corpus.addDocuments(documentSet);
        MetricSpace metricSpace = new MetricSpace(corpus.getNumOfTokens(), documentSet);
        final int I = 100;
        final int P = 5;
        int K;
        if (set.equals("sinai")) K = 9; else if (set.equals("twins")) K = 10; else if (set.equals("lev")) K = 11; else if (set.equals("lev")) K = 8; else K = 8;
        IDistance distance = parseDistance(dist);
        IClusteringAlgorithm algorithm = parseClusteringAlgorithm(method, K, I, P, distance);
        ClusterSet clusteringResult = algorithm.cluster(metricSpace.getVectors());
        clusteringResult.setRepresentatives();
        metricSpace.setClusters(clusteringResult);
        corpus.reset();
        return metricSpace;
    }

    private static double getRandIndex(MetricSpace space, int numOfDesiredClusters) {
        double a = 0.0, b = 0.0, c = 0.0, d = 0.0;
        IVector vectors[] = new DocumentVector[space.numOfVectors()];
        space.getVectors().toArray(vectors);
        for (int i = 0; i < vectors.length; i++) {
            String v1title = ((DocumentVector) vectors[i]).getInfo().getTitle();
            int spaceIndex = v1title.indexOf(' ');
            if (spaceIndex == -1) {
                System.err.println("There is no tag for the doc named \"" + v1title + "\"");
                return -1;
            }
            int v1tag = Integer.parseInt(v1title.substring(0, spaceIndex));
            for (int j = i + 1; j < vectors.length; j++) {
                String v2title = ((DocumentVector) vectors[j]).getInfo().getTitle();
                spaceIndex = v2title.indexOf(' ');
                if (spaceIndex == -1) {
                    System.err.println("There is no tag for the doc named \"" + v2title + "\"");
                    return -1;
                }
                int v2tag = Integer.parseInt(v2title.substring(0, spaceIndex));
                boolean sameTag = v2tag == v1tag;
                boolean sameCluster = space.getCluster(vectors[i]) == space.getCluster(vectors[j]);
                if (sameTag && sameCluster) a++; else if (sameTag && !sameCluster) b++; else if (!sameTag && sameCluster) c++; else if (!sameTag && !sameCluster) d++;
            }
        }
        return (a + d) / (a + b + c + d);
    }

    private int choose(int n, int k) {
        if (n < k) return 0;
        if (k == 0 || k == n) return 1;
        return choose(n - 1, k - 1) + choose(n - 1, k);
    }

    private static void printClusteringResults() {
        int N = 5;
        for (String m : methods) {
            for (String d : distances) {
                for (String set : datasets) {
                    for (int i = 1; i <= N; i++) {
                        if (i > 1 && m.equals("MST")) break;
                        try {
                            FileWriter fstream = new FileWriter(outputFolder + m + "-" + d + "-" + set + (m.equals("MST") ? "" : i) + ".txt");
                            BufferedWriter out = new BufferedWriter(fstream);
                            MetricSpace metricSpace = cluster(m, d, set);
                            out.write(metricSpace.toString());
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private static void printDistancesBetweenDocs() {
        Corpus corpus = Corpus.getCorpus();
        for (String d : distances) {
            for (String set : datasets) {
                try {
                    FileWriter fstream = new FileWriter(outputFolder + d + "-" + set + ".txt");
                    BufferedWriter out = new BufferedWriter(fstream);
                    Set<Document> documentSet = ((HebrewNLP) nlp).rawFilesToDocuments("../heb-clustering/lib/Datasets/" + set);
                    corpus.addDocuments(documentSet);
                    MetricSpace metricSpace = new MetricSpace(corpus.getNumOfTokens(), documentSet);
                    out.write(metricSpace.printDebug(parseDistance(d)));
                    out.close();
                    corpus.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static IClusteringAlgorithm parseClusteringAlgorithm(String clusteringAlgorithm, int K, int I, int P, IDistance distance) {
        if (clusteringAlgorithm.equals("KPP")) return new KMeansPPClustering(K, I, distance); else if (clusteringAlgorithm.equals("K")) return new KMeansClustering(K, I, distance); else if (clusteringAlgorithm.equals("BK")) return new BisectingKMeansClustering(K, distance); else if (clusteringAlgorithm.equals("S")) return new SpectralClustering(K, I, distance); else if (clusteringAlgorithm.equals("H")) return new HierarchicalClustering(K, distance); else if (clusteringAlgorithm.equals("HK")) return new HierarchicalKMeansClustering(K, I, P, distance); else if (clusteringAlgorithm.equals("EDM")) return new EpsilonDeltaMeansClustering(15, 0.27, distance); else if (clusteringAlgorithm.equals("MST")) return new MSTClustering(distance, K); else return null;
    }

    private static IDistance parseDistance(String distance) {
        if (distance.equals("CHE")) return new ChebyshevDistance(); else if (distance.equals("EUC")) return new EuclidianDistance(); else if (distance.equals("MAN")) return new ManhattanDistance(); else return null;
    }
}
