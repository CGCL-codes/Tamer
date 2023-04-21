package playground.johannes.sna.snowball.analysis;

import gnu.trove.TObjectDoubleHashMap;
import java.util.Set;
import org.apache.log4j.Logger;
import playground.johannes.sna.graph.Graph;
import playground.johannes.sna.graph.Vertex;
import playground.johannes.sna.graph.analysis.Degree;
import playground.johannes.sna.math.DescriptivePiStatistics;
import playground.johannes.sna.math.DescriptivePiStatisticsFactory;
import playground.johannes.sna.snowball.SampledEdge;
import playground.johannes.sna.snowball.SampledGraph;
import playground.johannes.sna.snowball.SampledVertex;

/**
 * A class that provides functionality to estimate degree related
 * graph-properties on a snowball sample.
 * 
 * @author illenberger
 *
 */
public class EstimatedDegree extends Degree {

    private static final Logger logger = Logger.getLogger(EstimatedDegree.class);

    private PiEstimator piEstimator;

    private DescriptivePiStatisticsFactory factory;

    /**
	 * Creates new estimated degree object configured with <tt>estimator</tt> as
	 * pi-estimator and <tt>factory</tt> as the factory for creating new
	 * instances of {@link DescriptivePiStatistics} which does the actual
	 * estimation of degree properties.
	 * 
	 * @param estimator a pi-estimator
	 * @param factory a factory for creating new instance of {@link DescriptivePiStatistics}.
	 */
    public EstimatedDegree(PiEstimator estimator, DescriptivePiStatisticsFactory factory) {
        this.piEstimator = estimator;
        this.factory = factory;
    }

    /**
	 * Returns a descriptive statistics object containing all sampled vertices
	 * each associated a pi-value.
	 * 
	 * @param vertices
	 *            a set of sampled vertices
	 * @return a descriptive statistics object.
	 */
    @SuppressWarnings("unchecked")
    @Override
    public DescriptivePiStatistics statistics(Set<? extends Vertex> vertices) {
        DescriptivePiStatistics stats = factory.newInstance();
        int p0 = 0;
        Set<SampledVertex> samples = SnowballPartitions.<SampledVertex>createSampledPartition((Set<SampledVertex>) vertices);
        for (SampledVertex vertex : samples) {
            double p = piEstimator.probability(vertex);
            if (p > 0) {
                stats.addValue(vertex.getNeighbours().size(), p);
            } else p0++;
        }
        if (p0 > 0) logger.warn(String.format("There are %1$s vertices with probability 0!", p0));
        return stats;
    }

    /**
	 * @param vertices
	 *            a set of sampled vertices
	 * @return an object-double map containing all sampled vertices and their
	 *         degree.
	 */
    @SuppressWarnings("unchecked")
    @Override
    public TObjectDoubleHashMap<Vertex> values(Set<? extends Vertex> vertices) {
        return super.values(SnowballPartitions.createSampledPartition((Set<SampledVertex>) vertices));
    }

    /**
	 * Estimates the degree-degree correlation of a sampled graph.
	 * 
	 * @param g
	 *            a sampled graph
	 * @return the estimated degree-degree correlation.
	 */
    public double assortativity(Graph g) {
        SampledGraph graph = (SampledGraph) g;
        int iteration = SnowballStatistics.getInstance().lastIteration(graph.getVertices());
        double product = 0;
        double sum = 0;
        double squareSum = 0;
        double M_hat = 0;
        for (SampledEdge e : graph.getEdges()) {
            SampledVertex v_i = e.getVertices().getFirst();
            SampledVertex v_j = e.getVertices().getSecond();
            if (v_i.isSampled() && v_j.isSampled()) {
                double p_i = piEstimator.probability(v_i, iteration - 1);
                double p_j = piEstimator.probability(v_j, iteration - 1);
                if (p_i > 0 && p_j > 0) {
                    double p = (p_i + p_j) - (p_i * p_j);
                    int k_i = v_i.getEdges().size();
                    int k_j = v_j.getEdges().size();
                    sum += 0.5 * (k_i + k_j) / p;
                    squareSum += 0.5 * (Math.pow(k_i, 2) + Math.pow(k_j, 2)) / p;
                    product += k_i * k_j / p;
                    M_hat += 1 / p;
                }
            }
        }
        double norm = 1 / M_hat;
        return ((norm * product) - Math.pow(norm * sum, 2)) / ((norm * squareSum) - Math.pow(norm * sum, 2));
    }
}
