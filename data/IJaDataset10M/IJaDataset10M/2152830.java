package swarm.engine;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.util.Log;
import com.commsen.stopwatch.Meter;
import com.commsen.stopwatch.Stopwatch;
import swarm.Pair;
import swarm.SystemProperties;
import swarm.engine.MetricsCalculator.ClusterGrouping;
import swarm.engine.MetricsCalculator.Grouping;
import swarm.engine.MetricsCalculator.Metrics;
import swarm.engine.statistics.ClusterPositionFilter;
import swarm.engine.statistics.HeirarchialClusterer;
import swarm.engine.statistics.ObjectPermanenceProcessor;
import swarm.engine.statistics.Clusterer.Cluster;
import swarm.engine.statistics.ObjectPermanenceProcessor.Event;

/**
 * An extension to {@link PopulationSimulator} that also computes statistics
 * about the swarm as it runs. This includes clustering, which can be slow with
 * large numbers of {@link Individual}s.
 * 
 */
public class PopulationSimulatorWithStatistics extends PopulationSimulator {

    private static Logger log = Logger.getLogger(PopulationSimulatorWithStatistics.class.getCanonicalName());

    private ObjectPermanenceProcessor objectPermanenceProcessor = new ObjectPermanenceProcessor();

    private ClusterPositionFilter clusterPositionFilter = new ClusterPositionFilter();

    private MetricsCalculator metricsCalculator = new MetricsCalculator(this, new GroupingResolver() {

        public boolean isMember(Grouping grp, Individual ind) {
            if (grp instanceof ClusterGrouping && filteredClusters != null) {
                ClusterGrouping clGrp = (ClusterGrouping) grp;
                return clGrp.isMember(ind, filteredClusters);
            }
            return super.isMember(grp, ind);
        }
    });

    private Map<Integer, Cluster<Individual>> filteredClusters;

    private Map<Integer, Point2D.Double> filteredClusterPositions;

    private ExecutorService threadPool = Executors.newFixedThreadPool(1);

    private List<Cluster<Individual>> lastClustering = null;

    private Future<List<Cluster<Individual>>> clusteringFuture = null;

    private boolean clusteringIsSlow;

    public PopulationSimulatorWithStatistics(Population population) {
        super(population);
    }

    public synchronized void stepSimulation(Collection<Individual> temporaryIndividuals, int weightOfTemporaries) {
        long timerID = Stopwatch.start("simulation", "simulation");
        super.stepSimulation(temporaryIndividuals, weightOfTemporaries);
        Stopwatch.stop(timerID);
        timerID = Stopwatch.start("simulation", "findMaxRadius");
        metricsCalculator.clear();
        double splitRadius = getSplitRadius();
        if (splitRadius < 0) {
            Recipe recipe = new Recipe(getPopulation());
            double maxRadius = 0;
            for (Pair<Integer, Parameters> p : recipe.getParameters()) {
                double radius = p.second.getNeighborhoodRadius();
                maxRadius = Math.max(maxRadius, radius);
            }
            splitRadius = maxRadius * Math.abs(getSplitRadius());
        }
        Stopwatch.stop(timerID);
        timerID = Stopwatch.start("simulation", "cluster");
        List<Cluster<Individual>> res;
        if (getBackgroundClustering() && clusteringIsSlow) {
            if (clusteringFuture != null && clusteringFuture.isDone()) try {
                res = clusteringFuture.get();
            } catch (Exception e) {
                log.log(Level.WARNING, "Error computing clustering in other thread", e);
                final HeirarchialClusterer<Individual> clusterer = createClusterer(splitRadius);
                res = clusterer.run();
            } else {
                if (lastClustering != null) res = lastClustering; else {
                    final HeirarchialClusterer<Individual> clusterer = createClusterer(splitRadius);
                    res = clusterer.run();
                }
            }
            if (clusteringFuture == null || clusteringFuture.isDone()) {
                final double fsplitRadius = splitRadius;
                final HeirarchialClusterer<Individual> clusterer = createClusterer(fsplitRadius);
                clusteringFuture = threadPool.submit(new Callable<List<Cluster<Individual>>>() {

                    public List<Cluster<Individual>> call() throws Exception {
                        long start = System.currentTimeMillis();
                        Meter timer2 = Stopwatch.startMeter("simulation", "clustererRun");
                        try {
                            return clusterer.run();
                        } finally {
                            timer2.stop();
                            clusteringIsSlow = (System.currentTimeMillis() - start) > getOutOfThreadClusteringThreshold();
                        }
                    }
                });
            }
        } else {
            final HeirarchialClusterer<Individual> clusterer = createClusterer(splitRadius);
            long start = System.currentTimeMillis();
            Meter timer2 = Stopwatch.startMeter("simulation", "clustererRun");
            try {
                res = clusterer.run();
            } finally {
                timer2.stop();
                clusteringIsSlow = (System.currentTimeMillis() - start) > getOutOfThreadClusteringThreshold();
            }
        }
        lastClustering = res;
        Stopwatch.stop(timerID);
        timerID = Stopwatch.start("simulation", "objectPermanenceProcessor");
        filteredClusters = objectPermanenceProcessor.step(res);
        Stopwatch.stop(timerID);
        timerID = Stopwatch.start("simulation", "clusterPositionFilter");
        filteredClusterPositions = clusterPositionFilter.step(filteredClusters);
        Stopwatch.stop(timerID);
    }

    private long getOutOfThreadClusteringThreshold() {
        return 40;
    }

    private HeirarchialClusterer<Individual> createClusterer(double splitRadius) {
        long timerID = Stopwatch.start("simulation", "clustererInit");
        final HeirarchialClusterer<Individual> clusterer = new HeirarchialClusterer<Individual>();
        clusterer.setMinimumClusterSize(getMinimumClusterSize());
        clusterer.setMaxClusters(getMaximumClusters());
        clusterer.setSplitRadius(splitRadius);
        clusterer.initialize(getPopulation());
        Stopwatch.stop(timerID);
        return clusterer;
    }

    public static double getSplitRadius() {
        return SystemProperties.getDouble(PopulationSimulatorWithStatistics.class, "minimumSplitRadius", -1);
    }

    public static int getMinimumClusterSize() {
        return SystemProperties.getInt("minimumClusterSize", 10);
    }

    public static int getMaximumClusters() {
        return SystemProperties.getInt("maximumClusters", 5);
    }

    public static boolean getBackgroundClustering() {
        return SystemProperties.getBoolean("backgroundClustering", true);
    }

    /**
	 * Get the set of clusters after they have been filtered to maintain IDs and
	 * to remove glitches.
	 * 
	 * @return
	 */
    public Map<Integer, Cluster<Individual>> getFilteredClusters() {
        return filteredClusters;
    }

    /**
	 * Get the set of cluster positions after they have been filtered to reduce
	 * jitter and other small motions.
	 * 
	 * @return
	 */
    public Map<Integer, Point2D.Double> getFilteredClusterPositions() {
        return filteredClusterPositions;
    }

    /**
	 * Get clustering events and remove them from the internal queue.
	 * 
	 * @return
	 */
    public List<Event> pullEvents() {
        return objectPermanenceProcessor.pullEvents();
    }

    /**
	 * Get all the groupings that are usable with this simulator.
	 * 
	 * @return
	 */
    public List<Grouping> getGroupings() {
        return metricsCalculator.getGroupings();
    }

    /**
	 * Get metrics for the specified grouping.
	 * 
	 * @param grp
	 * @return
	 */
    public Metrics getMetrics(Grouping grp) {
        return metricsCalculator.getMetrics(grp);
    }

    public MetricsCalculator getMetricsCalculator() {
        return metricsCalculator;
    }
}
