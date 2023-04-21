package playground.johannes.socialnetworks.snowball2.sim;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.matsim.core.config.Config;
import playground.johannes.sna.graph.Edge;
import playground.johannes.sna.graph.Graph;
import playground.johannes.sna.graph.Vertex;
import playground.johannes.sna.graph.analysis.FixedSizeRandomPartition;
import playground.johannes.sna.graph.analysis.RandomPartition;
import playground.johannes.sna.graph.io.SparseGraphMLReader;
import playground.johannes.sna.snowball.SampledVertexDecorator;
import playground.johannes.sna.snowball.analysis.PiEstimator;
import playground.johannes.sna.snowball.analysis.SimplePiEstimator;
import playground.johannes.sna.snowball.sim.Sampler;
import playground.johannes.sna.snowball.sim.SamplerListener;
import playground.johannes.studies.snowball.SnowballSim;

/**
 * @author illenberger
 *
 */
public class NEstimTest {

    private static final Logger logger = Logger.getLogger(NEstimTest.class);

    private static int INIT_CAPACITY = 100;

    private static long rndSeed = 0;

    public static void main(String args[]) throws IOException {
        final String MODULE_NAME = "estimatortest";
        Config config = SnowballSim.loadConfig(args[0]);
        SparseGraphMLReader reader = new SparseGraphMLReader();
        Graph graph = reader.readGraph(config.getParam(MODULE_NAME, "graphfile"));
        int nSims = Integer.parseInt(config.findParam(MODULE_NAME, "simulations"));
        int nBurnin = Integer.parseInt(config.findParam(MODULE_NAME, "burnin"));
        int seeds = Integer.parseInt(config.findParam(MODULE_NAME, "seeds"));
        double proba = Double.parseDouble(config.findParam(MODULE_NAME, "responserate"));
        String output = config.getParam(MODULE_NAME, "output");
        logger.info("Running burnin...");
        Level level = Logger.getRootLogger().getLevel();
        Logger.getRootLogger().setLevel(Level.WARN);
        Random random = new Random(rndSeed);
        ProbaObsListener pObsListener = new ProbaObsListener();
        for (int i = 0; i < nBurnin; i++) {
            pObsListener.reset();
            Sampler<Graph, Vertex, Edge> sampler = newSampler(graph, seeds, proba, random);
            sampler.setListener(pObsListener);
            sampler.run(graph);
            if (i % 100 == 0) {
                Logger.getRootLogger().setLevel(level);
                logger.info(String.format("%1$s of %2$s simulations done.", i, nBurnin));
                Logger.getRootLogger().setLevel(Level.WARN);
            }
        }
        Map<Vertex, double[]> vertexProbas = pObsListener.getPObs();
        Logger.getRootLogger().setLevel(level);
        logger.info("Calculating N_obs...");
        Logger.getRootLogger().setLevel(Level.WARN);
        random = new Random(rndSeed);
        NEstimListener nEstimListener = new NEstimListener();
        nEstimListener.vertexProbas = vertexProbas;
        for (int i = 0; i < nSims; i++) {
            nEstimListener.reset(graph.getVertices().size());
            Sampler<Graph, Vertex, Edge> sampler = newSampler(graph, seeds, proba, random);
            sampler.setListener(nEstimListener);
            sampler.run(graph);
            if (i % 100 == 0) {
                Logger.getRootLogger().setLevel(level);
                logger.info(String.format("%1$s of %2$s simulations done.", i, nBurnin));
                Logger.getRootLogger().setLevel(Level.WARN);
            }
        }
        Logger.getRootLogger().setLevel(level);
        logger.info("Analyzing...");
        double[] N_obs = nEstimListener.getNobs();
        BufferedWriter writer = new BufferedWriter(new FileWriter(output + "/N_obs.txt"));
        writer.write("it\tN_obs");
        writer.newLine();
        for (int i = 0; i < N_obs.length; i++) {
            writer.write(String.valueOf(i));
            writer.write("\t");
            writer.write(String.valueOf(N_obs[i]));
            writer.newLine();
        }
        writer.close();
        double[] N_estim = nEstimListener.getNestim();
        writer = new BufferedWriter(new FileWriter(output + "/N_estim.txt"));
        writer.write("it\tN_estim");
        writer.newLine();
        for (int i = 0; i < N_estim.length; i++) {
            writer.write(String.valueOf(i));
            writer.write("\t");
            writer.write(String.valueOf(N_estim[i]));
            writer.newLine();
        }
        writer.close();
        logger.info("Done.");
    }

    private static Sampler<Graph, Vertex, Edge> newSampler(Graph graph, int seeds, double proba, Random random) {
        Sampler<Graph, Vertex, Edge> sampler = new Sampler<Graph, Vertex, Edge>();
        sampler.setSeedGenerator(new FixedSizeRandomPartition<Vertex>(seeds, random.nextLong()));
        sampler.setResponseGenerator(new RandomPartition<Vertex>(proba, random.nextLong()));
        return sampler;
    }

    private static class NEstimListener implements SamplerListener {

        private PiEstimator estimator;

        private int lastIteration;

        private int maxIteration;

        private Map<Vertex, double[]> vertexProbas;

        private int[] nSimualtions = new int[INIT_CAPACITY];

        private double[] N_obs = new double[INIT_CAPACITY];

        private double[] N_estim = new double[INIT_CAPACITY];

        public void reset(int N) {
            lastIteration = 0;
            estimator = new SimplePiEstimator(N);
        }

        @Override
        public boolean afterSampling(Sampler<?, ?, ?> sampler, SampledVertexDecorator<?> vertex) {
            return true;
        }

        @Override
        public boolean beforeSampling(Sampler<?, ?, ?> sampler, SampledVertexDecorator<?> v) {
            if (sampler.getIteration() > lastIteration) {
                int it = sampler.getIteration();
                lastIteration = it;
                nSimualtions[it - 1]++;
                maxIteration = Math.max(maxIteration, it - 1);
                estimator.update(sampler.getSampledGraph());
                double w_obs_sum = 0;
                double w_estim_sum = 0;
                for (SampledVertexDecorator<?> vertex : sampler.getSampledGraph().getVertices()) {
                    if (vertex.isSampled()) {
                        Vertex delegate = vertex.getDelegate();
                        double p_estim = estimator.probability(vertex);
                        double p_obs = 0;
                        double[] probas = vertexProbas.get(delegate);
                        if (probas != null) p_obs = probas[it - 1];
                        if (p_obs > 0) {
                            w_estim_sum += 1 / p_estim;
                            w_obs_sum += 1 / p_obs;
                        }
                    }
                }
                N_obs[it - 1] += w_obs_sum;
                N_estim[it - 1] += w_estim_sum;
            }
            return true;
        }

        @Override
        public void endSampling(Sampler<?, ?, ?> sampler) {
            beforeSampling(sampler, null);
        }

        public double[] getNestim() {
            double[] N_estim_mean = new double[maxIteration + 1];
            for (int i = 0; i <= maxIteration; i++) {
                N_estim_mean[i] = N_estim[i] / (double) nSimualtions[i];
            }
            return N_estim_mean;
        }

        public double[] getNobs() {
            double[] N_obs_mean = new double[maxIteration + 1];
            for (int i = 0; i <= maxIteration; i++) {
                N_obs_mean[i] = N_obs[i] / (double) nSimualtions[i];
            }
            return N_obs_mean;
        }
    }

    private static class ProbaObsListener implements SamplerListener {

        private Map<Vertex, int[]> vertexCounts = new HashMap<Vertex, int[]>();

        private int[] nSimualtions = new int[INIT_CAPACITY];

        private int lastIteration;

        private int maxIteration;

        public void reset() {
            lastIteration = 0;
        }

        @Override
        public boolean afterSampling(Sampler<?, ?, ?> sampler, SampledVertexDecorator<?> vertex) {
            return true;
        }

        @Override
        public boolean beforeSampling(Sampler<?, ?, ?> sampler, SampledVertexDecorator<?> v) {
            if (sampler.getIteration() > lastIteration) {
                int it = sampler.getIteration();
                lastIteration = it;
                maxIteration = Math.max(maxIteration, it - 1);
                nSimualtions[it - 1]++;
                for (SampledVertexDecorator<?> vertex : sampler.getSampledGraph().getVertices()) {
                    if (vertex.isSampled()) {
                        Vertex delegate = vertex.getDelegate();
                        int[] counts = vertexCounts.get(delegate);
                        if (counts == null) {
                            counts = new int[INIT_CAPACITY];
                            vertexCounts.put(delegate, counts);
                        }
                        counts[it - 1]++;
                    }
                }
            }
            return true;
        }

        @Override
        public void endSampling(Sampler<?, ?, ?> sampler) {
            beforeSampling(sampler, null);
        }

        public Map<Vertex, double[]> getPObs() {
            Map<Vertex, double[]> pObs = new HashMap<Vertex, double[]>();
            for (Entry<Vertex, int[]> entry : vertexCounts.entrySet()) {
                double[] N_obs = new double[maxIteration + 1];
                for (int i = 0; i <= maxIteration; i++) {
                    N_obs[i] = entry.getValue()[i] / (double) nSimualtions[i];
                    pObs.put(entry.getKey(), N_obs);
                }
            }
            return pObs;
        }
    }
}
