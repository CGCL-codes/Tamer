package jmetal.metaheuristics.singleObjective.particleSwarmOptimization;

import java.io.IOException;
import jmetal.core.*;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.problems.*;
import jmetal.problems.DTLZ.*;
import jmetal.problems.ZDT.*;
import jmetal.problems.singleObjective.Griewank;
import jmetal.problems.singleObjective.Sphere;
import jmetal.problems.WFG.*;
import jmetal.problems.LZ09.*;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import jmetal.qualityIndicator.QualityIndicator;

/**
 * Class for configuring and running a single-objective PSO algorithm
 */
public class PSO_main {

    public static Logger logger_;

    public static FileHandler fileHandler_;

    /**
   * @param args Command line arguments. The first (optional) argument specifies 
   *             the problem to solve.
   * @throws JMException 
   * @throws IOException 
   * @throws SecurityException 
   * Usage: three options
   *      - jmetal.metaheuristics.mocell.MOCell_main
   *      - jmetal.metaheuristics.mocell.MOCell_main problemName
   *      - jmetal.metaheuristics.mocell.MOCell_main problemName ParetoFrontFile
   */
    public static void main(String[] args) throws JMException, IOException, ClassNotFoundException {
        Problem problem;
        Algorithm algorithm;
        Mutation mutation;
        QualityIndicator indicators;
        HashMap parameters;
        logger_ = Configuration.logger_;
        fileHandler_ = new FileHandler("PSO_main.log");
        logger_.addHandler(fileHandler_);
        problem = new Sphere("Real", 20);
        algorithm = new PSO(problem);
        algorithm.setInputParameter("swarmSize", 50);
        algorithm.setInputParameter("maxIterations", 5000);
        parameters = new HashMap();
        parameters.put("probability", 1.0 / problem.getNumberOfVariables());
        parameters.put("distributionIndex", 20.0);
        mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);
        algorithm.addOperator("mutation", mutation);
        long initTime = System.currentTimeMillis();
        SolutionSet population = algorithm.execute();
        long estimatedTime = System.currentTimeMillis() - initTime;
        logger_.info("Total execution time: " + estimatedTime + "ms");
        logger_.info("Objectives values have been writen to file FUN");
        population.printObjectivesToFile("FUN");
        logger_.info("Variables values have been writen to file VAR");
        population.printVariablesToFile("VAR");
    }
}
