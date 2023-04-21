package jmetal.experiments.settings;

import jmetal.metaheuristics.mocell.*;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.ProblemFactory;
import java.util.HashMap;
import java.util.Properties;
import jmetal.core.*;
import jmetal.experiments.Settings;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.Configuration.*;

/**
 * Settings class of algorithm MOCell
 */
public class MOCell_Settings extends Settings {

    public int populationSize_;

    public int maxEvaluations_;

    public int archiveSize_;

    public int feedback_;

    public double mutationProbability_;

    public double crossoverProbability_;

    public double crossoverDistributionIndex_;

    public double mutationDistributionIndex_;

    /**
	 * Constructor
	 */
    public MOCell_Settings(String problemName) {
        super(problemName);
        Object[] problemParams = { "Real" };
        try {
            problem_ = (new ProblemFactory()).getProblem(problemName_, problemParams);
        } catch (JMException e) {
            e.printStackTrace();
        }
        populationSize_ = 100;
        maxEvaluations_ = 25000;
        archiveSize_ = 100;
        feedback_ = 20;
        mutationProbability_ = 1.0 / problem_.getNumberOfVariables();
        crossoverProbability_ = 0.9;
        crossoverDistributionIndex_ = 20.0;
        mutationDistributionIndex_ = 20.0;
    }

    /**
	 * Configure the MOCell algorithm with default parameter settings
	 * @return an algorithm object
	 * @throws jmetal.util.JMException
	 */
    public Algorithm configure() throws JMException {
        Algorithm algorithm;
        Crossover crossover;
        Mutation mutation;
        Operator selection;
        QualityIndicator indicators;
        HashMap parameters;
        Object[] problemParams = { "Real" };
        problem_ = (new ProblemFactory()).getProblem(problemName_, problemParams);
        algorithm = new MOCell(problem_);
        algorithm.setInputParameter("populationSize", populationSize_);
        algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
        algorithm.setInputParameter("archiveSize", archiveSize_);
        algorithm.setInputParameter("feedBack", feedback_);
        parameters = new HashMap();
        parameters.put("probability", crossoverProbability_);
        parameters.put("distributionIndex", crossoverDistributionIndex_);
        crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);
        parameters = new HashMap();
        parameters.put("probability", mutationProbability_);
        parameters.put("distributionIndex", mutationDistributionIndex_);
        mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);
        parameters = null;
        selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("selection", selection);
        if ((paretoFrontFile_ != null) && (!paretoFrontFile_.equals(""))) {
            indicators = new QualityIndicator(problem_, paretoFrontFile_);
            algorithm.setInputParameter("indicators", indicators);
        }
        return algorithm;
    }
}
