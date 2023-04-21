package org.expasy.jpl.matching.clustering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expasy.jpl.experimental.ms.lcmsms.filtering.filter.JPLIntensityTransformer;
import org.expasy.jpl.experimental.ms.lcmsms.filtering.filter.JPLNHighestPeaksFilter;
import org.expasy.jpl.experimental.ms.peaklist.JPLFragmentationSpectrum;
import org.expasy.jpl.matching.algo.JPLSpectralMatcher;
import org.expasy.jpl.matching.binning.JPLBinnedPeakListBuilder;
import org.expasy.jpl.matching.binning.JPLIBinnedPeakList;
import org.expasy.jpl.utils.file.JPLTabFileWriter;
import org.expasy.jpl.utils.math.JPLStatScoreCalculator;
import org.expasy.jpl.utils.sort.SimpleTypeArray;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.graph.UndirectedSubgraph;

public class JPLSpectrumClusterer {

    /** Logger */
    Log log = LogFactory.getLog(JPLSpectrumClusterer.class);

    /** ArrayList with all spectra */
    private ArrayList<JPLFragmentationSpectrum> spectra;

    /** ArrayLists of spectrum stubs */
    private final ArrayList<JPLIBinnedPeakList<JPLFragmentationSpectrum>> spectraStubs = new ArrayList<JPLIBinnedPeakList<JPLFragmentationSpectrum>>();

    /** Similarity graph between spectra */
    private final SimpleWeightedGraph<Integer, DefaultWeightedEdge> similarityGraph = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);

    /** Elements never to be clustered */
    private final HashSet<Integer> singletons = new HashSet<Integer>();

    /** boundaries for score histogram */
    private double scoreThresh, logRatioThresh, clusterMzTol, clusterSimThresh, fdrThreshold;

    private int nrOfStubPeaks = 10;

    /** Object to store statistical scores*/
    private final JPLStatScoreCalculator statScorer = new JPLStatScoreCalculator();

    private String reportTag;

    private double minMz, maxMz, binWidth = 10.0;

    private boolean changed;

    public JPLSpectrumClusterer() {
        scoreThresh = 0.3;
        logRatioThresh = 0.0;
        clusterMzTol = 0.01;
        clusterSimThresh = -0.2;
        fdrThreshold = 0.01;
        changed = true;
    }

    public JPLSpectrumClusterer(ArrayList<JPLFragmentationSpectrum> spectra) {
        this.spectra = spectra;
        scoreThresh = 0.3;
        logRatioThresh = 0.0;
        clusterMzTol = 0.01;
        clusterSimThresh = -0.2;
        fdrThreshold = 0.01;
        changed = true;
    }

    public void process() throws IOException {
        prepare();
        buildSimGraph();
        calcStatistics();
        List<Set<Integer>> connectedNodes = clusterGraph(similarityGraph, singletons);
        JPLClusterReporter.writeAllClusterLogRatios(connectedNodes, similarityGraph, statScorer, logRatioThresh, reportTag);
        JPLClusterReporter.writeAllCluster2MGF(connectedNodes, similarityGraph, spectra, reportTag);
        JPLClusterReporter.writeAllCluster2MGF(connectedNodes, similarityGraph, spectra, reportTag);
        JPLClusterReporter.writeAllClusterScores(connectedNodes, similarityGraph, spectra, this.clusterMzTol, minMz, maxMz, 5, reportTag);
    }

    private void prepare() {
        System.out.println("Start ms2 spectrum clustering routine...");
        System.out.println(spectra.size() + " ms2 spectra as input...");
        System.out.println();
        minMz = 1000000;
        maxMz = 0;
        double[] parentMasses = new double[spectra.size()];
        int i = 0;
        for (JPLFragmentationSpectrum spectrum : spectra) {
            parentMasses[i] = spectrum.getPrecursor().getMz();
            i++;
            if (spectrum.getNbPeak() == 0) {
                continue;
            }
            minMz = Math.min(minMz, spectrum.getMzAt(0));
            maxMz = Math.max(maxMz, spectrum.getMzAt(spectrum.getMzs().length - 1));
        }
        int[] indices = SimpleTypeArray.sortIndexesUp(parentMasses);
        spectra = mappSpectra(spectra, indices);
        JPLNHighestPeaksFilter peakFilter = new JPLNHighestPeaksFilter(nrOfStubPeaks);
        i = 0;
        for (JPLFragmentationSpectrum spectrum : spectra) {
            JPLFragmentationSpectrum sp = spectrum.clone();
            peakFilter.process(sp);
            JPLIBinnedPeakList<JPLFragmentationSpectrum> binnedStub;
            binnedStub = new JPLBinnedPeakListBuilder<JPLFragmentationSpectrum>(sp).mzMin(minMz).mzMax(maxMz).binWidth(binWidth).build();
            spectraStubs.add(i, binnedStub);
            i++;
        }
    }

    private void buildSimGraph() {
        @SuppressWarnings("unused") JPLIntensityTransformer transformer = new JPLIntensityTransformer(JPLIntensityTransformer.method.RANK);
        ArrayList<Double> badScores = new ArrayList<Double>();
        ArrayList<Double> goodScores = new ArrayList<Double>();
        int repCnt = (int) Math.floor(spectraStubs.size() / 20.0);
        int pctCnt = 0;
        System.out.println("Build similarity graph :");
        System.out.print("           ");
        for (int i = 0; i < spectraStubs.size(); i++) {
            if (i % repCnt == 0) {
                System.out.print(" " + pctCnt * 5 + "%");
                pctCnt++;
            }
            JPLIBinnedPeakList<JPLFragmentationSpectrum> binnedStub = spectraStubs.get(i);
            JPLFragmentationSpectrum sp = spectra.get(i);
            JPLIBinnedPeakList<JPLFragmentationSpectrum> binnedSp1;
            binnedSp1 = new JPLBinnedPeakListBuilder<JPLFragmentationSpectrum>(sp).mzMin(minMz).mzMax(maxMz).binWidth(1).build();
            double mz = sp.getPrecursor().getMz();
            singletons.add(i);
            for (int j = i - 1; j >= 0; j--) {
                JPLFragmentationSpectrum sp2 = spectra.get(j);
                JPLIBinnedPeakList<JPLFragmentationSpectrum> binnedStub2 = spectraStubs.get(j);
                double mz2 = sp2.getPrecursor().getMz();
                if (mz - mz2 > clusterMzTol) break;
                JPLSpectralMatcher stubMatcher = new JPLSpectralMatcher(0.1);
                stubMatcher.computeMatch(binnedStub, binnedStub2);
                double stubScore = stubMatcher.getScore();
                JPLIBinnedPeakList<JPLFragmentationSpectrum> binnedSp2;
                binnedSp2 = new JPLBinnedPeakListBuilder<JPLFragmentationSpectrum>(sp2).mzMin(minMz).mzMax(maxMz).binWidth(1).build();
                double[] stub1 = binnedSp1.getBinsIntensities();
                double[] stub2 = binnedSp2.getBinsIntensities();
                double totI1 = 0;
                double totI2 = 0;
                double corr = 0;
                double maxI1 = 0;
                double maxI2 = 0;
                for (int k = 0; k < stub1.length; k++) {
                    totI1 += stub1[k] * stub1[k];
                    totI2 += stub2[k] * stub2[k];
                    corr += stub1[k] * stub2[k];
                    maxI1 = Math.max(maxI1, stub1[k]);
                    maxI2 = Math.max(maxI2, stub2[k]);
                }
                double score = corr / Math.sqrt(totI1 * totI2);
                if (stubScore < 5) {
                    badScores.add(score);
                } else {
                    goodScores.add(score);
                }
                if (score > scoreThresh) {
                    if (!similarityGraph.containsVertex(i)) {
                        similarityGraph.addVertex(i);
                    }
                    if (!similarityGraph.containsVertex(j)) {
                        similarityGraph.addVertex(j);
                    }
                    DefaultWeightedEdge e = similarityGraph.addEdge(i, j);
                    similarityGraph.setEdgeWeight(e, score);
                    singletons.remove((Integer) i);
                    singletons.remove((Integer) j);
                }
            }
        }
        System.out.println("");
        System.out.println("");
        statScorer.setPositiveNegativeScores(goodScores, badScores);
        statScorer.calcScoreBinParamsFromData();
    }

    private void calcStatistics() throws IOException {
        System.out.println("Calculate statistical scores...");
        ArrayList<Double> logProbRatio = statScorer.getLogProbRatio(false);
        ArrayList<Double> fdr = statScorer.getFDR();
        ArrayList<Double> good = statScorer.getPositiveHisto();
        ArrayList<Double> bad = statScorer.getNegativeHisto();
        int idx = fdr.size() - 1;
        for (int i = 0; i < fdr.size(); i++) {
            if (fdr.get(i) < fdrThreshold) {
                idx = i;
                break;
            }
        }
        logRatioThresh = logProbRatio.get(idx);
        System.out.println("logRatio threshold: " + logRatioThresh);
        System.out.println();
        JPLTabFileWriter.setAppend(false);
        JPLTabFileWriter.setNrFractDecDigits(3);
        JPLTabFileWriter.setSeparator(",");
        JPLTabFileWriter.addValues(statScorer.getScoreBins());
        JPLTabFileWriter.addValues(good);
        JPLTabFileWriter.addValues(bad);
        JPLTabFileWriter.addValues(fdr);
        JPLTabFileWriter.addValues(logProbRatio);
        JPLTabFileWriter.addColumnName("scores");
        JPLTabFileWriter.addColumnName("goodScores");
        JPLTabFileWriter.addColumnName("badScores");
        JPLTabFileWriter.addColumnName("fdr");
        JPLTabFileWriter.addColumnName("logProbRatio");
        JPLTabFileWriter.write(JPLClusterReporter.getOutputDir() + "statScore_" + reportTag + ".csv");
        JPLTabFileWriter.reset();
    }

    private ArrayList<JPLFragmentationSpectrum> mappSpectra(ArrayList<JPLFragmentationSpectrum> spectra, int[] indices) {
        if (indices.length > spectra.size()) {
            throw new IllegalArgumentException("too many indices");
        }
        ArrayList<JPLFragmentationSpectrum> values = new ArrayList<JPLFragmentationSpectrum>(indices.length);
        int i = 0;
        for (int index : indices) {
            values.add(i, spectra.get(index));
            i++;
        }
        return values;
    }

    private ArrayList<Integer> mappNodes(ArrayList<Integer> nodes, int[] indices) {
        if (indices.length > nodes.size()) {
            throw new IllegalArgumentException("too many indices");
        }
        ArrayList<Integer> values = new ArrayList<Integer>(indices.length);
        int i = 0;
        for (int index : indices) {
            values.add(i, nodes.get(index));
            i++;
        }
        return values;
    }

    private List<Set<Integer>> mappClusters(List<Set<Integer>> connectedNodes, int[] indices) {
        if (indices.length > connectedNodes.size()) {
            throw new IllegalArgumentException("too many indices");
        }
        ArrayList<Set<Integer>> values = new ArrayList<Set<Integer>>();
        int i = 0;
        for (int index : indices) {
            values.add(i, connectedNodes.get(index));
            i++;
        }
        return values;
    }

    @SuppressWarnings("unused")
    private double calcEdgeWeightThreshold(List<Set<Integer>> connectedNodes, UndirectedGraph<Integer, DefaultWeightedEdge> graph, double percentile) {
        double threshold = 0.0;
        ArrayList<Double> weights = new ArrayList<Double>();
        for (Set<Integer> cluster : connectedNodes) {
            ArrayList<DefaultWeightedEdge> edges = new ArrayList<DefaultWeightedEdge>();
            Iterator<Integer> iterator = cluster.iterator();
            while (iterator.hasNext()) {
                Integer node = iterator.next();
                Set<DefaultWeightedEdge> edgesOfNode = graph.edgesOf(node);
                for (DefaultWeightedEdge e : edgesOfNode) {
                    if (!edges.contains(e)) {
                        edges.add(e);
                    }
                }
            }
            Iterator<DefaultWeightedEdge> edgeIterator = edges.iterator();
            double meanScore = 0.0;
            int cnt = 0;
            while (edgeIterator.hasNext()) {
                DefaultWeightedEdge e = edgeIterator.next();
                meanScore += graph.getEdgeWeight(e);
                cnt++;
            }
            meanScore /= cnt;
            double sigScore = 0.0;
            edgeIterator = edges.iterator();
            while (edgeIterator.hasNext()) {
                DefaultWeightedEdge e = edgeIterator.next();
                double tmp = (graph.getEdgeWeight(e) - meanScore);
                sigScore += tmp * tmp;
            }
            sigScore = Math.sqrt(sigScore / cnt);
            iterator = cluster.iterator();
            while (iterator.hasNext()) {
                Integer node = iterator.next();
                Set<DefaultWeightedEdge> edgesOfNode = graph.edgesOf(node);
                double meanVertexScore = 0.0;
                for (DefaultWeightedEdge e : edgesOfNode) {
                    meanVertexScore += graph.getEdgeWeight(e);
                }
                meanVertexScore /= edgesOfNode.size();
                double zScore = (meanVertexScore - meanScore) / sigScore;
                weights.add(zScore);
            }
        }
        double[] w = new double[weights.size()];
        for (int i = 0; i < weights.size(); i++) {
            w[i] = weights.get(i);
        }
        Arrays.sort(w);
        int nr = w.length - (int) Math.round(percentile * w.length);
        threshold = w[nr];
        return threshold;
    }

    @SuppressWarnings("unused")
    private double calcEdgeCntThreshold(List<Set<Integer>> connectedNodes, UndirectedGraph<Integer, DefaultWeightedEdge> graph, double percentile) {
        double threshold = 0.0;
        ArrayList<Double> cnts = new ArrayList<Double>();
        for (Set<Integer> cluster : connectedNodes) {
            if (cluster.size() < 4) continue;
            double meanEdgeCnt = 0.0;
            Iterator<Integer> iterator = cluster.iterator();
            while (iterator.hasNext()) {
                Integer node = iterator.next();
                Set<DefaultWeightedEdge> edgesOfNode = graph.edgesOf(node);
                meanEdgeCnt += edgesOfNode.size();
            }
            meanEdgeCnt /= cluster.size() * (cluster.size() - 1);
            cnts.add(meanEdgeCnt);
        }
        double[] v = new double[cnts.size()];
        for (int i = 0; i < cnts.size(); i++) {
            v[i] = cnts.get(i);
        }
        Arrays.sort(v);
        int nr = (int) Math.round(percentile * v.length);
        threshold = v[nr];
        return threshold;
    }

    private List<Set<Integer>> clusterGraph(UndirectedGraph<Integer, DefaultWeightedEdge> graph, HashSet<Integer> singletons) {
        if (!changed) return null;
        ConnectivityInspector<Integer, DefaultWeightedEdge> ci = new ConnectivityInspector<Integer, DefaultWeightedEdge>(graph);
        List<Set<Integer>> connectedNodes = ci.connectedSets();
        HashSet<Integer> outliers = new HashSet<Integer>();
        ArrayList<Set<Integer>> removedClusters = new ArrayList<Set<Integer>>();
        for (Set<Integer> cluster : connectedNodes) {
            if (cluster.size() == 1) {
                Integer node = cluster.iterator().next();
                graph.removeVertex(node);
                cluster.clear();
                singletons.add(node);
                removedClusters.add(cluster);
            }
        }
        connectedNodes.removeAll(removedClusters);
        int nrSpectra = spectra.size();
        int cnt = 0;
        int cnt1 = 0;
        for (Set<Integer> cluster : connectedNodes) {
            cnt += cluster.size();
            if (cluster.size() == 1) cnt1++;
        }
        System.out.println("Initial:: Nr spectra: " + nrSpectra + ", in cluster: " + cnt + ", nr clusters: " + connectedNodes.size() + ", outliers: " + outliers.size() + ", singletons: " + singletons.size());
        for (Set<Integer> cluster : connectedNodes) {
            if (cluster.size() == 1) continue;
            cleanCluster(cluster, graph, outliers, logRatioThresh);
        }
        cnt = 0;
        cnt1 = 0;
        for (Set<Integer> cluster : connectedNodes) {
            cnt += cluster.size();
            if (cluster.size() == 1) cnt1++;
        }
        System.out.println("After cleanCluster:: Nr spectra: " + nrSpectra + ", in cluster: " + cnt + ", nr clusters: " + connectedNodes.size() + ", outliers: " + outliers.size() + ", singletons: " + singletons.size());
        for (Set<Integer> cluster : connectedNodes) {
            if (cluster.size() == 1) continue;
            assignOutliers(cluster, graph, outliers, logRatioThresh);
        }
        cnt = 0;
        cnt1 = 0;
        removedClusters.clear();
        for (Set<Integer> cluster : connectedNodes) {
            if (cluster.size() == 0) {
                removedClusters.add(cluster);
            }
        }
        connectedNodes.removeAll(removedClusters);
        cnt = 0;
        cnt1 = 0;
        for (Set<Integer> cluster : connectedNodes) {
            cnt += cluster.size();
            if (cluster.size() == 1) cnt1++;
        }
        System.out.println("After assignOutliers:: Nr spectra: " + nrSpectra + ", in cluster: " + cnt + ", nr clusters: " + connectedNodes.size() + ", outliers: " + outliers.size() + ", singletons: " + singletons.size());
        if (connectedNodes.size() > 0) {
            List<Set<Integer>> clusters = mergeOutliers(outliers, graph);
            if (clusters != null && clusters.size() > 0) {
                connectedNodes.addAll(clusters);
            }
        }
        singletons.addAll(outliers);
        outliers.clear();
        cnt = 0;
        cnt1 = 0;
        for (Set<Integer> cluster : connectedNodes) {
            cnt += cluster.size();
            if (cluster.size() == 1) cnt1++;
        }
        System.out.println("After mergeOutliers:: Nr spectra: " + nrSpectra + ", in cluster: " + cnt + ", nr clusters: " + connectedNodes.size() + ", outliers: " + outliers.size() + ", singletons: " + singletons.size());
        double[] parentMasses = new double[connectedNodes.size()];
        sortClustersByMass(connectedNodes, parentMasses);
        mergeClusters(connectedNodes, parentMasses, graph);
        cnt = 0;
        cnt1 = 0;
        for (Set<Integer> cluster : connectedNodes) {
            cnt += cluster.size();
        }
        System.out.println("After mergeClusters:: Nr spectra: " + nrSpectra + ", in cluster: " + cnt + ", nr clusters: " + connectedNodes.size() + ", outliers: " + outliers.size() + ", singletons: " + singletons.size());
        return connectedNodes;
    }

    private void cleanCluster(Set<Integer> cluster, UndirectedGraph<Integer, DefaultWeightedEdge> graph, HashSet<Integer> outliers, double logRatioThresh) {
        UndirectedSubgraph<Integer, DefaultWeightedEdge> clusterGraph = new UndirectedSubgraph<Integer, DefaultWeightedEdge>(graph, cluster, null);
        boolean changed = true;
        while (changed) {
            changed = false;
            ArrayList<Integer> aliens = new ArrayList<Integer>();
            int size = cluster.size() - 1;
            for (Integer node : cluster) {
                Set<DefaultWeightedEdge> edgesOfNode = clusterGraph.edgesOf(node);
                ArrayList<Double> scores = new ArrayList<Double>();
                for (DefaultWeightedEdge e : edgesOfNode) {
                    scores.add(clusterGraph.getEdgeWeight(e));
                }
                double logRatio = statScorer.calcLogProbRatio(cluster.size(), scoreThresh, scores);
                if (logRatio < logRatioThresh) {
                    aliens.add(node);
                    clusterGraph.removeVertex(node);
                    size--;
                    changed = true;
                }
            }
            cluster.removeAll(aliens);
            outliers.addAll(aliens);
            if (cluster.size() == 1) {
                Integer node = cluster.iterator().next();
                clusterGraph.removeVertex(node);
                cluster.clear();
                outliers.add(node);
            }
        }
    }

    private boolean assignOutliers(Set<Integer> cluster, UndirectedGraph<Integer, DefaultWeightedEdge> graph, HashSet<Integer> outliers, double logRatioThresh) {
        boolean changed = false;
        ArrayList<Integer> friends = new ArrayList<Integer>();
        for (Integer node : cluster) {
            Set<DefaultWeightedEdge> edgesOfNode = graph.edgesOf(node);
            int size = cluster.size() - 1;
            ArrayList<Double> scores = new ArrayList<Double>();
            for (DefaultWeightedEdge e : edgesOfNode) {
                if (cluster.contains(graph.getEdgeTarget(e)) || cluster.contains(graph.getEdgeSource(e))) {
                    scores.add(graph.getEdgeWeight(e));
                }
            }
            double logRatio = statScorer.calcLogProbRatio(cluster.size(), scoreThresh, scores);
            if (logRatio > logRatioThresh) {
                friends.add(node);
                cluster.add(node);
                changed = true;
                size++;
            }
        }
        outliers.removeAll(friends);
        return changed;
    }

    private List<Set<Integer>> mergeOutliers(HashSet<Integer> outliers, UndirectedGraph<Integer, DefaultWeightedEdge> graph) {
        if (!changed) return null;
        UndirectedSubgraph<Integer, DefaultWeightedEdge> outlierGraph = new UndirectedSubgraph<Integer, DefaultWeightedEdge>(graph, outliers, null);
        System.out.println("o********* " + changed + " " + outliers.size());
        outliers.clear();
        System.out.println("o********* " + changed + " " + outliers.size());
        List<Set<Integer>> clusters = clusterGraph(outlierGraph, outliers);
        System.out.println("c********* " + changed + " " + clusters.size());
        if (clusters == null || clusters.size() == 0) {
            changed = false;
        }
        return clusters;
    }

    @SuppressWarnings("unused")
    private ArrayList<Integer> sortOutliersByMass(HashSet<Integer> outliers, double[] parentMasses) {
        int i = 0;
        ArrayList<Integer> ol = new ArrayList<Integer>();
        for (Integer outlier : outliers) {
            parentMasses[i] = spectra.get(outlier).getPrecursor().getMz();
            ol.add(i, outlier);
            i++;
        }
        int[] indices = SimpleTypeArray.sortIndexesUp(parentMasses);
        ol = mappNodes(ol, indices);
        Arrays.sort(parentMasses);
        return ol;
    }

    private void sortClustersByMass(List<Set<Integer>> clusters, double[] parentMasses) {
        int i = 0;
        for (Set<Integer> cluster : clusters) {
            if (cluster.size() == 0) continue;
            double mz = 0.0;
            for (Integer node : cluster) {
                mz += spectra.get(node).getPrecursor().getMz();
            }
            mz /= cluster.size();
            parentMasses[i] = mz;
            i++;
        }
        int[] indices = SimpleTypeArray.sortIndexesUp(parentMasses);
        clusters = mappClusters(clusters, indices);
        Arrays.sort(parentMasses);
    }

    private double calcClusterSim(Set<Integer> cluster1, Set<Integer> cluster2, UndirectedGraph<Integer, DefaultWeightedEdge> graph) {
        ArrayList<Double> scoresInter = new ArrayList<Double>();
        ArrayList<Double> scoresIntra = new ArrayList<Double>();
        for (Integer node : cluster1) {
            Set<DefaultWeightedEdge> edgesOfNode = graph.edgesOf(node);
            for (DefaultWeightedEdge e : edgesOfNode) {
                if (cluster2.contains(graph.getEdgeTarget(e)) || cluster2.contains(graph.getEdgeSource(e))) {
                    scoresInter.add(graph.getEdgeWeight(e));
                }
                if (cluster1.contains(graph.getEdgeTarget(e)) && cluster1.contains(graph.getEdgeSource(e))) {
                    scoresIntra.add(graph.getEdgeWeight(e));
                }
            }
        }
        int size = cluster1.size() * cluster2.size();
        double logRatioInter = statScorer.calcLogProbRatio(size, scoreThresh, scoresInter) / size;
        size = cluster1.size() * (cluster1.size() - 1);
        double logRatioIntra = statScorer.calcLogProbRatio(size, scoreThresh, scoresIntra) / size;
        double sim = logRatioInter - logRatioIntra;
        return sim;
    }

    private void mergeClusters(List<Set<Integer>> clusters, double[] parentMasses, UndirectedGraph<Integer, DefaultWeightedEdge> graph) {
        ArrayList<Integer> removedClusters = new ArrayList<Integer>();
        for (int i = 0; i < clusters.size(); i++) {
            double mz = parentMasses[i];
            for (int j = i - 1; j >= 0; j--) {
                if (removedClusters.contains(j)) continue;
                double sim = 0.0;
                double mz2 = parentMasses[j];
                if (mz - mz2 > clusterMzTol) break;
                sim = calcClusterSim(clusters.get(i), clusters.get(j), graph);
                if (sim > clusterSimThresh) {
                    Set<Integer> cluster = clusters.get(i);
                    cluster.addAll(clusters.get(j));
                    removedClusters.add(j);
                }
            }
        }
        clusters.removeAll(removedClusters);
    }

    public ArrayList<Set<Integer>> getBadClusters(List<Set<Integer>> connectedNodes, UndirectedGraph<Integer, DefaultWeightedEdge> graph, double minEdgeCnt) {
        ArrayList<Set<Integer>> badClusters = new ArrayList<Set<Integer>>();
        int clusterID = 0;
        for (Set<Integer> cluster : connectedNodes) {
            clusterID++;
            double meanNumberEdges = 0.0;
            Iterator<Integer> iterator = cluster.iterator();
            while (iterator.hasNext()) {
                Integer node = iterator.next();
                Set<DefaultWeightedEdge> edgesOfNode = graph.edgesOf(node);
                meanNumberEdges += edgesOfNode.size();
            }
            meanNumberEdges /= cluster.size() * (cluster.size() - 1);
            if (meanNumberEdges < minEdgeCnt) {
                badClusters.add(cluster);
            }
        }
        return badClusters;
    }

    /**
	 * Set tag that is used in report filenames
	 * @param reportTag : Tag that is used in report filenames
	 */
    public void setReportTag(String reportTag) {
        this.reportTag = reportTag;
    }

    /**
	 * Get tag that is used in report filenames
	 * @return report tag
	 */
    public String getReportTag() {
        return reportTag;
    }
}
