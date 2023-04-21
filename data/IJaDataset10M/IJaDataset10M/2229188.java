package jmetal.metaheuristics.gde3;

import jmetal.core.*;
import jmetal.operators.crossover.*;
import jmetal.operators.selection.*;
import jmetal.problems.*;
import jmetal.problems.DTLZ.*;
import jmetal.problems.ZDT.*;
import jmetal.problems.WFG.*;
import jmetal.problems.LZ09.*;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Class for configuring and running the GDE3 algorithm
 */
public class GDE3_main {

    public static Logger logger_;

    public static FileHandler fileHandler_;

    /**
   * @param args Command line arguments.
   * @throws JMException 
   * @throws IOException 
   * @throws SecurityException 
   * Usage: three choices
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main problemName
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main problemName paretoFrontFile
   */
    public static void main(String[] args) throws JMException, SecurityException, IOException, ClassNotFoundException {
        Problem problem;
        Algorithm algorithm;
        Operator selection;
        Operator crossover;
        HashMap parameters;
        QualityIndicator indicators;
        logger_ = Configuration.logger_;
        fileHandler_ = new FileHandler("GDE3_main.log");
        logger_.addHandler(fileHandler_);
        indicators = null;
        if (args.length == 1) {
            Object[] params = { "Real" };
            problem = (new ProblemFactory()).getProblem(args[0], params);
        } else if (args.length == 2) {
            Object[] params = { "Real" };
            problem = (new ProblemFactory()).getProblem(args[0], params);
            indicators = new QualityIndicator(problem, args[1]);
        } else {
            problem = new Kursawe("Real", 3);
        }
        algorithm = new GDE3(problem);
        algorithm.setInputParameter("populationSize", 100);
        algorithm.setInputParameter("maxIterations", 250);
        parameters = new HashMap();
        parameters.put("CR", 0.5);
        parameters.put("F", 0.5);
        crossover = CrossoverFactory.getCrossoverOperator("DifferentialEvolutionCrossover", parameters);
        parameters = null;
        selection = SelectionFactory.getSelectionOperator("DifferentialEvolutionSelection", parameters);
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("selection", selection);
        long initTime = System.currentTimeMillis();
        SolutionSet population = algorithm.execute();
        long estimatedTime = System.currentTimeMillis() - initTime;
        logger_.info("Total execution time: " + estimatedTime + "ms");
        logger_.info("Variables values have been writen to file VAR");
        population.printVariablesToFile("VAR");
        logger_.info("Objectives values have been writen to file FUN");
        population.printObjectivesToFile("FUN");
        if (indicators != null) {
            logger_.info("Quality indicators");
            logger_.info("Hypervolume: " + indicators.getHypervolume(population));
            logger_.info("GD         : " + indicators.getGD(population));
            logger_.info("IGD        : " + indicators.getIGD(population));
            logger_.info("Spread     : " + indicators.getSpread(population));
            logger_.info("Epsilon    : " + indicators.getEpsilon(population));
        }
    }
}
