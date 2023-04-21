package playground.johannes.socialnetworks.snowball2.sim.deprecated;

import gnu.trove.TObjectDoubleHashMap;
import playground.johannes.sna.graph.Vertex;
import playground.johannes.sna.snowball.SampledGraph;
import playground.johannes.sna.snowball.SampledVertex;
import playground.johannes.sna.snowball.analysis.PiEstimator;
import playground.johannes.socialnetworks.snowball2.sim.SampleStats;

/**
 * @author illenberger
 *
 */
public class Estimator10 implements PiEstimator {

    private final int N;

    private SampleStats stats;

    private TObjectDoubleHashMap<SampledVertex> probas;

    private double c;

    public Estimator10(int N) {
        this.N = N;
    }

    public void update(SampledGraph graph) {
        stats = new SampleStats(graph);
        probas = new TObjectDoubleHashMap<SampledVertex>();
        double p_sum = 0;
        for (Vertex vertex : graph.getVertices()) {
            SampledVertex v = (SampledVertex) vertex;
            if (v.isSampled()) {
                double p = getProbabilityIntern(v);
                probas.put(v, p);
                p_sum += 1 / p;
            }
        }
        c = N / p_sum;
    }

    public double getProbabilityIntern(SampledVertex vertex) {
        int it = stats.getMaxIteration();
        if (it == 0) return stats.getNumSampled(0) / (double) N; else {
            int n = stats.getAccumulatedNumSampled(it - 1);
            double p_k = 1 - Math.pow(1 - n / (double) N, vertex.getNeighbours().size());
            double p = 1;
            return p * p_k;
        }
    }

    @Override
    public double probability(SampledVertex vertex) {
        return probas.get(vertex);
    }

    public double getPrevProbability(SampledVertex vertex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double probability(SampledVertex vertex, int iteration) {
        return 0;
    }
}
