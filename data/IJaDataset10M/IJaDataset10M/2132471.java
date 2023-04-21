package de.fzi.harmonia.commons.fitnesscalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import de.fzi.harmonia.commons.basematchers.Aggregator;
import de.fzi.harmonia.commons.basematchers.BaseMatcher;
import de.fzi.harmonia.commons.basematchers.InfeasibleBaseMatcherException;
import de.fzi.harmonia.commons.basematchers.MissingWeightException;
import de.fzi.harmonia.commons.basematchers.helper.AnnotationPropertyHelper;
import de.fzi.kadmos.api.Alignment;
import de.fzi.kadmos.api.Correspondence;

/**
 * This class computes evaluations of correspondences by using a number of base matchers and an
 * aggregation function. The base matchers are supposed to be implementation classes of
 * {@link BaseMatcher}. The aggregator is supposed to be an implementation class of
 * {@link Aggregator}. The particular entities (<code>OWLEntity</code>) to be
 * evaluated are only made known at the time of asking for the evaluation. However, the
 * correspondence evaluator object knows about the ontologies to be matched, which are needed at
 * creation time of the correspondence evaluator object.
 * 
 * @author bock
 *
 */
public class CorrespondenceEvaluator {

    private static Log logger = LogFactory.getLog(CorrespondenceEvaluator.class);

    protected Alignment alignment;

    protected Properties params;

    protected Map<String, Double> distances;

    protected Map<String, Double> weights;

    protected Set<BaseMatcher> baseMatchers;

    protected Aggregator aggregator;

    protected boolean threading = false;

    /**
	 * Creates a new correspondence evaluator.
	 * 
	 * @param alignment Alignment to be used for gaining context information about the
	 *                  correspondence to be evaluated.
	 * @param params Parameters passed to the alignment process. The general parameters can be used to pass
	 * 				 parameters to base matchers.
	 */
    public CorrespondenceEvaluator(Alignment alignment, Properties params) {
        this.alignment = alignment;
        this.params = params;
        baseMatchers = new HashSet<BaseMatcher>();
        distances = new HashMap<String, Double>();
        weights = new LinkedHashMap<String, Double>();
        readParams();
    }

    /**
	 * Adds a base matcher class to the particle evaluator.
	 * The base matcher class must be of type <code>BaseMatcher</code>.
	 * Note that if the class is not found or could not be loaded, nothing happens
	 * but an error log.
	 * 
	 * @param className The class name of the base matcher class to be loaded.
	 * @throws ClassNotFoundException if the base matcher class cannot be found.
	 * @throws IllegalAccessException if the class constructor cannot be accessed.
	 * @throws InstantiationException if the base matcher class cannot be instantiated.
	 */
    public void addBaseMatcher(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        BaseMatcher baseMatcher = (BaseMatcher) Class.forName(className).newInstance();
        baseMatcher.init(alignment);
        baseMatcher.setParameters(params);
        baseMatchers.add(baseMatcher);
        distances.put(className, -1.d);
        weights.put(className, 1.d);
        logger.info("Base matcher class " + className + " loaded.");
    }

    /**
	 * (Re)sets the weight of a base matcher and normalises all weights. 
	 * If the base matcher is not registered, nothing will happen but a log message.
	 * 
	 * @param baseMatcher The name of the base matcher. (Method name without the "Matcher" suffix.)
	 * @param weight The new weight to be assinged to this base matcher.
	 * @throws IllegalArgumentException if weight is < 0 or > 1.
	 */
    public void setWeight(String baseMatcher, double weight) throws IllegalArgumentException {
        if (weight < 0. || weight > 1.) throw new IllegalArgumentException("Invalid weight. Weight must be 0 <= weight <= 1.");
        if (distances.containsKey(baseMatcher)) weights.put(baseMatcher, weight); else logger.warn("Cannot (re)set weight for base matcher " + baseMatcher + " since base matcher is not registered.");
    }

    /**
	 * Adds an aggregator class to the particle evaluator.
	 * The aggregator class must be of type <code>Aggregator</code>.
	 * Note that if the class is not found or could not be loaded, nothing happens
	 * but an error log.
	 * 
	 * @param className The class name of the aggregator class to be loaded.
	 * @throws ClassNotFoundException if the aggregator class cannot be found
	 * @throws IllegalAccessException if the class constructor cannot be accessed
	 * @throws InstantiationException if the aggregator class cannot be instantiated
	 */
    public void addAggregator(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        aggregator = (Aggregator) Class.forName(className).newInstance();
        logger.info("Aggregator class " + className + " loaded.");
    }

    /**
	 * Returns the aggregated distance a correspondence (<code>Correspondence</code>).
	 * Aggregation is done by the given aggregator using base distances and weights.
	 * 
	 * @param correspondence Correspondence to be evaluated
	 * @return The aggregated correspondence evaluation of the given correspondence. 
	 * @throws MissingAggregatorException if no aggregator class is loaded.
	 * @throws NoBaseMatcherException if no base matcher is loaded.
	 */
    public double getEvaluation(Correspondence<? extends OWLEntity> correspondence) throws MissingAggregatorException, NoBaseMatcherException {
        return getEvaluation(correspondence.getEntity1(), correspondence.getEntity2());
    }

    /**
	 * Returns the aggregated distance of two entities (<code>OWLEntity</code>).
	 * Aggregation is done by the given aggregator using base distances and weights.
	 * 
	 * @param ent1 First OWLEntity from a correspondence to be evaluated
	 * @param ent2 Second OWLEntity from a correspondence to be evaluated
	 * @return The aggregated correspondence evaluation of the given correspondence. 
	 * @throws MissingAggregatorException if no aggregator class is loaded.
	 * @throws NoBaseMatcherException if no base matcher is loaded.
	 */
    public <T extends OWLEntity> double getEvaluation(T ent1, T ent2) throws MissingAggregatorException, NoBaseMatcherException {
        logger.trace("getEvaluation starting.");
        double result = 1.d;
        distances.clear();
        if (aggregator == null) throw new IllegalStateException("CorrespondenceEvaluator not properly initialised.", new MissingAggregatorException("No aggregator registered."));
        computeBaseDistances(ent1, ent2);
        try {
            result = (Double) aggregator.getAggregation(distances, weights);
            if (logger.isDebugEnabled()) printBaseDistances(ent1, ent2, weights, result);
        } catch (MissingWeightException e) {
            logger.error(e.getMessage() + " Setting missing weight to 0.");
            weights.put(e.getMissingWeight(), 0.d);
            try {
                result = (Double) aggregator.getAggregation(distances, weights);
                if (logger.isDebugEnabled()) printBaseDistances(ent1, ent2, weights, result);
            } catch (MissingWeightException e1) {
                logger.error(e.getMessage() + " Second try failed. All hope is lost. Returning 1.0.");
            }
        }
        logger.trace("getEvaluation finised successfully.");
        return result;
    }

    /**
	 * Triggers the computation of all base matchers for the given correspondence.
	 * If a base matcher is not applicable, i.e. it throws an {@link InfeasibleBaseMatcherException}, 
	 * this base distance is not added to the base distances map.
	 * 
	 * @param ent1 First OWLEntity from a correspondence to be evaluated
	 * @param ent2 Second OWLEntity from a correspondence to be evaluated
	 * @throws NoBaseMatcherException if no base matcher is loaded.
	 */
    protected <T extends OWLEntity> void computeBaseDistances(T ent1, T ent2) throws NoBaseMatcherException {
        if (baseMatchers.isEmpty()) throw new IllegalStateException("CorrespondenceEvaluator not properly initialised.", new NoBaseMatcherException("No base matcher registered."));
        if (threading) {
            BaseMatcherRunner baseMatcherThread[] = new BaseMatcherRunner[baseMatchers.size()];
            int i = 0;
            for (BaseMatcher baseMatcher : baseMatchers) {
                baseMatcherThread[i] = new BaseMatcherRunner(baseMatcher, ent1, ent2);
                baseMatcherThread[i++].start();
            }
            for (int j = 0; j < baseMatchers.size(); j++) try {
                baseMatcherThread[j].join();
                double distance;
                try {
                    distance = baseMatcherThread[j].getDistance();
                    distances.put(baseMatcherThread[j].getName(), distance);
                } catch (InfeasibleBaseMatcherException e) {
                }
            } catch (InterruptedException e) {
                logger.error("Base matcher thread for " + baseMatcherThread[j].getName() + " died.");
            }
        } else for (BaseMatcher baseMatcher : baseMatchers) {
            double distance;
            try {
                distance = baseMatcher.getDistance(ent1, ent2);
                distances.put(baseMatcher.getClass().getName(), distance);
            } catch (InfeasibleBaseMatcherException e) {
            }
        }
    }

    /**
	 * Computes the standard deviation of base distances from the previous evaluation.
	 * @return Standard deviation of base distances from previous evaluation.
	 */
    public double getStdDev() {
        double mean = 0.d;
        int nbDist = 0;
        for (String dist : distances.keySet()) {
            mean += distances.get(dist);
            nbDist++;
        }
        mean = mean / nbDist;
        double s = 0.d;
        for (String dist : distances.keySet()) {
            double d = distances.get(dist) - mean;
            s += d * d;
        }
        double stdDev = s / nbDist;
        return stdDev;
    }

    /**
	 * For debugging purposes, this method prints all base distances for a given correspondence
	 * together with their weights and aggregated evaluation.
	 * 
	 * @param ent1 First OWLEntity from a correspondence to be analysed.
	 * @param ent2 Second OWLEntity from a correspondence to be analysed.
	 * @param normalisedWeights 
	 * @param aggregation The aggregated evaluation value.
	 */
    protected <T extends OWLEntity> void printBaseDistances(T ent1, T ent2, Map<String, Double> normalisedWeights, double aggregation) {
        OWLDataFactory dataFactory = alignment.getOntology1().getOWLOntologyManager().getOWLDataFactory();
        OWLAnnotationProperty labelProperty = dataFactory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        String name1 = null;
        String name2 = null;
        if (ent1 != null && ent1 instanceof OWLEntity) name1 = ent1.getIRI().getFragment();
        if (name1 == null) name1 = AnnotationPropertyHelper.getAnnotationLiteralValue(labelProperty, ent1, alignment.getOntology1());
        if (name1 == null) name1 = "??";
        if (ent2 != null && ent2 instanceof OWLEntity) name2 = ent2.getIRI().getFragment();
        if (name2 == null) name2 = AnnotationPropertyHelper.getAnnotationLiteralValue(labelProperty, ent2, alignment.getOntology2());
        if (name2 == null) name2 = "??";
        ArrayList<String> baseMatcherName = new ArrayList<String>();
        ArrayList<Double> distancesL = new ArrayList<Double>();
        ArrayList<Double> weights = new ArrayList<Double>();
        for (Map.Entry<String, Double> distance : distances.entrySet()) {
            baseMatcherName.add(distance.getKey());
            distancesL.add(distance.getValue());
            weights.add(normalisedWeights.get(baseMatcherName));
        }
        printThreadSafeBaseDistances(name1, name2, aggregation, baseMatcherName, distancesL, weights);
    }

    private static synchronized void printThreadSafeBaseDistances(String name1, String name2, double aggregation, ArrayList<String> baseMatcherName, ArrayList<Double> distancesL, ArrayList<Double> weights) {
        logger.debug("=====================================================================");
        logger.debug(name1);
        logger.debug(name2);
        logger.debug("---------------------------------------------------------------------");
        logger.debug("Aggregation: " + aggregation);
        logger.debug("---------------------------------------------------------------------");
        for (int i = 0; i < baseMatcherName.size(); i++) {
            logger.debug(baseMatcherName.get(i) + "(" + weights.get(i) + ") \t" + distancesL.get(i));
        }
        logger.debug("=====================================================================");
    }

    /**
	 * Clears this correspondence evaluator, i.e. all base matchers are removed
	 */
    public void clear() {
        distances.clear();
        weights.clear();
        baseMatchers.clear();
        aggregator = null;
    }

    /**
	 * Parses relevant parameters from the global parameters.
	 */
    protected void readParams() {
        if (params.getProperty("multithreadedBaseMatcherEval") != null) threading = Boolean.parseBoolean((String) params.getProperty("multithreadedBaseMatcherEval")); else {
            logger.warn("Parameter multithreadedBaseMatcherEval not given. Using default (false).");
            threading = false;
        }
        logger.info("Multithreading " + (threading == true ? "enabled" : "disabled") + " for base matcher execution.");
    }

    /**
	 * Thread wrapper for a base matcher. By using this class, any base matcher can be run in a
	 * separate thread. The base matcher and the correspondence to be evaluated is given via
	 * the constructor. Result and reference to the base matcher can be accessed via getter methods.
	 * 
	 * Note that the name of the thread is equal to the class name of the base matcher
	 * it is wrapped around.
	 * 
	 * @author bock
	 */
    private class BaseMatcherRunner extends Thread {

        private BaseMatcher matcher;

        private OWLEntity entity1;

        private OWLEntity entity2;

        private double distance;

        private boolean feasible = true;

        private InfeasibleBaseMatcherException exception;

        /**
		 * Creates a new base matcher runner thread. The name of the thread is equal to the
		 * class name of the base matcher that is given via parameter.
		 * 
		 * @param baseMatcher The base matcher this thread is running.
		 * @param ent1 Entity1 from a correspondence that is to be evaluated.
		 * @param ent2 Entity2 from a correspondence that is to be evaluated.
		 */
        BaseMatcherRunner(BaseMatcher baseMatcher, OWLEntity ent1, OWLEntity ent2) {
            super(baseMatcher.getClass().getName());
            matcher = baseMatcher;
            entity1 = ent1;
            entity2 = ent2;
            distance = -1.d;
        }

        public void run() {
            try {
                distance = matcher.getDistance(entity1, entity2);
            } catch (InfeasibleBaseMatcherException e) {
                feasible = false;
                exception = e;
            }
        }

        /**
		 * Gets the base distance that is computed by the base matcher.
		 * 
		 * @return The distance computed by the base matcher or -1 if the 
		 * 		   base matcher has not yet finished.
		 * @throws InfeasibleBaseMatcherException If the base matcher was not
		 *                                        applicable to the given correspondence.
		 */
        public double getDistance() throws InfeasibleBaseMatcherException {
            if (!feasible) throw exception;
            return distance;
        }
    }
}
