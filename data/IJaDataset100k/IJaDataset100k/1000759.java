package net.sf.myra.cantminer.alpha;

import static net.sf.myra.cantminer.alpha.Alpha.START_VERTEX;
import java.util.logging.Logger;
import net.sf.myra.cantminer.alpha.step.NominalStep;
import net.sf.myra.cantminer.alpha.step.Step;
import net.sf.myra.cantminer.pittsburgh.CandidateRuleList;
import net.sf.myra.datamining.Rule;
import net.sf.myra.datamining.data.Term;
import net.sf.myra.framework.Configuration;
import net.sf.myra.framework.Edge;
import net.sf.myra.framework.Graph;
import net.sf.myra.framework.Pheromone;
import net.sf.myra.framework.Vertex;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision$ $Date::                    #$
 */
public class PheromoneUpdater {

    /**
	 * The property key under which the evaporation factor is stored.
	 */
    public static final String EVAPORATION_FACTOR = "net.sf.myra.cantminer.maxmin.evaporation";

    /**
	 * The property key under which the p_best probability is stored.
	 */
    public static final String P_BEST = "net.sf.myra.cantminer.maxmin.pbest";

    /**
	 * The initial pheromone value (this is an arbitrary 'big' value).
	 */
    public static final double INITIAL = 10.0;

    /**
	 * The pheromone evaporation factor (default 0.95);
	 */
    private double factor;

    /**
	 * The p_best probability (default 0.05) to estimate the tMin value.
	 */
    private double pBest;

    /**
	 * The maximum pheromone value.
	 */
    private double tMax;

    /**
	 * The minimum pheromone value.
	 */
    private double tMin;

    /**
	 * The quality of the current best solution.
	 */
    private double current = 0.0;

    /**
	 * The logger instance.
	 */
    private Logger logger = Logger.getLogger(PheromoneUpdater.class.getName());

    public void initialize(Graph<Step> graph) {
        graph.apply(new Graph.EdgeInitializationTransform(INITIAL));
        tMax = INITIAL;
        tMin = INITIAL;
        factor = Configuration.getDoubleProperty(EVAPORATION_FACTOR, 0.90);
        pBest = Configuration.getDoubleProperty(P_BEST, 0.05);
        for (Vertex<Step> vertex : graph.vertices()) {
            if (vertex.getInfo() instanceof NominalStep) {
                NominalStep step = (NominalStep) vertex.getInfo();
                double[] pheromone = step.getPheromone(0);
                for (int i = 0; i < pheromone.length; i++) {
                    pheromone[i] = INITIAL;
                }
                step.setPheromone(pheromone, 0);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void update(CandidateRuleList candidate, Graph<Step> graph) {
        if (candidate.getQuality() > current) {
            current = candidate.getQuality();
            double n = graph.vertices().length - 1;
            double average = n / 2.0;
            double pDec = Math.pow(pBest, 1 / (double) n);
            tMax = (1 / (1 - factor)) * (current / 5.0);
            tMin = (tMax * (1 - pDec)) / ((average - 1) * pDec);
            if (tMin > tMax) {
                tMin = tMax;
            }
        }
        for (int i = 0; i < candidate.size(); i++) {
            for (Vertex<Step> vertex : graph.vertices()) {
                for (Edge<Step> edge : graph.getEdges(vertex)) {
                    edge.setPheromone(edge.getPheromone(i), i);
                    if (edge.getTo().getInfo() instanceof NominalStep) {
                        NominalStep step = (NominalStep) edge.getTo().getInfo();
                        step.setPheromone(step.getPheromone(i), i);
                    }
                }
            }
        }
        graph.apply(new Graph.EdgeEvaporateTransform(factor));
        for (Vertex<Step> vertex : graph.vertices()) {
            if (vertex.getInfo() instanceof NominalStep) {
                NominalStep step = (NominalStep) vertex.getInfo();
                for (int i = 0; i < step.levels(); i++) {
                    double[] pheromone = step.getPheromone(i);
                    for (int j = 0; j < pheromone.length; j++) {
                        pheromone[j] = pheromone[j] * factor;
                        if (pheromone[j] > tMax) {
                            pheromone[j] = tMax;
                        } else if (pheromone[j] < tMin) {
                            pheromone[j] = tMin;
                        }
                    }
                    step.setPheromone(pheromone, i);
                }
            }
        }
        double delta = candidate.getQuality() / 5.0;
        int level = 0;
        for (Rule rule : candidate.getRules()) {
            if (!rule.isEmpty()) {
                Vertex<Step> current = graph.get(START_VERTEX);
                for (Vertex<?> vertex : rule.getVertices()) {
                    Vertex<Step> to = graph.get(vertex.getLabel());
                    Edge<Step> edge = graph.getEdge(current, to);
                    Pheromone p = edge.getPheromone(level);
                    p.setValue(p.getValue() + delta);
                    if (to.getInfo() instanceof NominalStep) {
                        NominalStep step = (NominalStep) to.getInfo();
                        double[] pheromone = step.getPheromone(level);
                        Term term = ((Vertex<Term>) vertex).getInfo();
                        int index = (int) term.getValues()[0];
                        pheromone[index] = pheromone[index] + delta;
                        if (pheromone[index] > tMax) {
                            pheromone[index] = tMax;
                        }
                        step.setPheromone(pheromone, level);
                    }
                    current = to;
                }
            }
            level++;
        }
        graph.apply(new Graph.EdgeRangeTransform(tMin, tMax));
    }

    @SuppressWarnings("unchecked")
    public boolean hasConverged(Graph<Step> graph, CandidateRuleList best) {
        int level = 0;
        for (Rule rule : best.getRules()) {
            if (!rule.isEmpty()) {
                Vertex<Step> current = graph.get(START_VERTEX);
                for (Vertex<?> vertex : rule.getVertices()) {
                    int maxCount = 0;
                    int minCount = 0;
                    for (Edge<Step> edge : graph.getEdges(current)) {
                        double value = edge.getPheromone(level).getValue();
                        if (precision(value) == precision(tMin)) {
                            minCount++;
                        } else if (precision(value) == precision(tMax)) {
                            maxCount++;
                        }
                    }
                    int size = graph.getEdges(current).size();
                    if ((minCount != (size - 1)) || (maxCount != 1)) {
                        return false;
                    } else if (vertex.getInfo() instanceof NominalStep) {
                        double[] pheromone = ((NominalStep) vertex.getInfo()).getPheromone(level);
                        minCount = 0;
                        maxCount = 0;
                        for (double value : pheromone) {
                            if (precision(value) == precision(tMin)) {
                                minCount++;
                            } else if (precision(value) == precision(tMax)) {
                                maxCount++;
                            }
                        }
                        if ((minCount != (pheromone.length - 1)) || (maxCount != 1)) {
                            logger.fine("Edge has converged but nominal attribute" + vertex.getLabel() + " did not converge: [min=" + minCount + ", max=" + maxCount + "]");
                            return false;
                        }
                    }
                    current = (Vertex<Step>) vertex;
                }
            }
            level++;
        }
        return true;
    }

    /**
	 * Returns the double value with up to four decimal digits.
	 * 
	 * @param value the value to be converted.
	 * 
	 * @return the double value with up to four decimal digits.
	 */
    private final double precision(double value) {
        int v = (int) (value * 100);
        return v / 100.0;
    }
}
