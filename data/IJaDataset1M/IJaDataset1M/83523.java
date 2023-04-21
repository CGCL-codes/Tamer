package jmetal.experiments.settings;

import jmetal.metaheuristics.moead.*;
import java.util.Properties;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.experiments.Settings;
import jmetal.problems.ProblemFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

/**
 *
 * @author Antonio
 */
public class pMOEAD_Settings extends Settings {

    double CR_ = 0.1;

    double F_ = 0.5;

    int populationSize_ = 300;

    int maxEvaluations_ = 150000;

    double mutationProbability_ = 1.0 / problem_.getNumberOfVariables();

    double distributionIndexForMutation_ = 20;

    String paretoFrontFile_ = "";

    /**
   * Constructor
   */
    public pMOEAD_Settings(Problem problem) {
        super(problem);
    }

    /**
   * Configure the algorith with the especified parameter settings
   * @return an algorithm object
   * @throws jmetal.util.JMException
   */
    public Algorithm configure() throws JMException {
        Algorithm algorithm;
        Operator crossover;
        Operator mutation;
        QualityIndicator indicators;
        algorithm = new pMOEAD(problem_, 4);
        algorithm.setInputParameter("populationSize", populationSize_);
        algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
        crossover = CrossoverFactory.getCrossoverOperator("DifferentialEvolutionCrossover");
        crossover.setParameter("CR", CR_);
        crossover.setParameter("F", F_);
        mutation = MutationFactory.getMutationOperator("PolynomialMutation");
        mutation.setParameter("probability", mutationProbability_);
        mutation.setParameter("distributionIndex", distributionIndexForMutation_);
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        if (!paretoFrontFile_.equals("")) {
            indicators = new QualityIndicator(problem_, paretoFrontFile_);
            algorithm.setInputParameter("indicators", indicators);
        }
        return algorithm;
    }

    /**
   * Configure an algorithm with user-defined parameter settings
   * @param settings
   * @return An algorithm
   * @throws jmetal.util.JMException
   */
    public Algorithm configure(Properties settings) throws JMException {
        if (settings != null) {
            CR_ = Double.parseDouble(settings.getProperty("CR", "" + CR_));
            F_ = Double.parseDouble(settings.getProperty("F", "" + F_));
            populationSize_ = Integer.parseInt(settings.getProperty("POPULATION_SIZE", "" + populationSize_));
            maxEvaluations_ = Integer.parseInt(settings.getProperty("MAX_EVAlUATIONS", "" + maxEvaluations_));
            mutationProbability_ = Double.parseDouble(settings.getProperty("MUTATION_PROBABILITY", "" + mutationProbability_));
            distributionIndexForMutation_ = Double.parseDouble(settings.getProperty("DISTRIBUTION_INDEX_FOR_MUTATION", "" + distributionIndexForMutation_));
            paretoFrontFile_ = settings.getProperty("PARETO_FRONT_FILE", "");
        }
        return configure();
    }
}
