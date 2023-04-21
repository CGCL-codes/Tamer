package jmetal.metaheuristics.paes;

import jmetal.base.*;
import jmetal.base.operator.mutation.*;
import jmetal.problems.*;
import jmetal.problems.DTLZ.*;
import jmetal.problems.ZDT.*;
import jmetal.problems.WFG.*;
import jmetal.problems.ZZJ07.*;
import jmetal.problems.LZ07.*;
import jmetal.util.JMException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import jmetal.qualityIndicator.QualityIndicator;

public class PAES_main2 {

    public static Logger logger_;

    public static FileHandler fileHandler_;

    /**
   * @param args Command line arguments.
   * @throws JMException 
   * @throws IOException 
   * @throws SecurityException 
   * Usage: three options
   *      - jmetal.metaheuristics.paes.PAES_main
   *      - jmetal.metaheuristics.paes.PAES_main problemName
   *      - jmetal.metaheuristics.paes.PAES_main problemName ParetoFrontFile
   */
    public static void main(String[] args) throws JMException, SecurityException, IOException {
        Problem problem;
        Algorithm algorithm;
        QualityIndicator indicators;
        logger_ = Configuration.logger_;
        fileHandler_ = new FileHandler("PAES_main2.log");
        logger_.addHandler(fileHandler_);
        Properties settings;
        String problemName;
        String paretoFrontFile;
        settings = new Properties();
        indicators = null;
        if (args.length == 1) {
            Object[] params = { "Real" };
            problem = (new ProblemFactory()).getProblem(args[0], params);
        } else if (args.length == 2) {
            problemName = args[0];
            paretoFrontFile = args[1];
            Object[] params = { "Real" };
            problem = (new ProblemFactory()).getProblem(problemName, params);
            settings.setProperty("PARETO_FRONT_FILE", paretoFrontFile);
            indicators = new QualityIndicator(problem, paretoFrontFile);
        } else {
            problem = new Kursawe(3, "Real");
        }
        algorithm = new PAES_Settings(problem).configure(settings);
        long initTime = System.currentTimeMillis();
        SolutionSet population = algorithm.execute();
        long estimatedTime = System.currentTimeMillis() - initTime;
        logger_.info("Total execution time: " + estimatedTime + "ms");
        logger_.info("Objectives values have been writen to file FUN");
        population.printObjectivesToFile("FUN");
        logger_.info("Variables values have been writen to file VAR");
        population.printVariablesToFile("VAR");
        if (indicators != null) {
            logger_.info("Quality indicators");
            logger_.info("Hypervolume: " + indicators.getHypervolume(population));
            logger_.info("GD         : " + indicators.getGD(population));
            logger_.info("IGD        : " + indicators.getIGD(population));
            logger_.info("Spread     : " + indicators.getSpread(population));
        }
    }
}
