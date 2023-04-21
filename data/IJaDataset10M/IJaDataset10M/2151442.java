package picoevo.tutorials.es.cmaesdemo;

import picoevo.core.evolution.ParameterSet;
import picoevo.es.cmaes.WrapperForCMAES;
import picoevo.toolbox.Misc;

/**
 * @author nicolas
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class CMAESexample {

    public static void main(String[] args) {
        ParameterSet params = new ParameterSet("CMA-ES ParameterSet");
        params.setProperty("EvaluationOperator", new SphereFunctionEvaluationOperator());
        params.setProperty("dimension", "20");
        params.setProperty("initialSearchRegionLower", "-1.0");
        params.setProperty("initialSearchRegionUpper", "0.0");
        params.setProperty("stopFunctionValue", "1e-3");
        params.setProperty("stopTolFun", "1e-12");
        params.setProperty("stopTolFunHist", "1e-12");
        params.setProperty("stopTolX", "0.0");
        params.setProperty("stopTolXFactor", "1e-9");
        params.setProperty("stopTolUpXFactor", "1000");
        params.setProperty("numberOfRuns", "12");
        params.setProperty("maximumTimeFractionForEigendecomposition", "0.5");
        String logfilenameprefix = "log/logfile_cmaes_testing_" + Misc.getCurrentTimeAsCompactString();
        params.displayInformation(logfilenameprefix + ".param");
        params.setProperty("outputFileNamesPrefix", logfilenameprefix + "_");
        WrapperForCMAES myEvolvableWorld = new WrapperForCMAES("EvolvingWithCMA-ES", params);
        myEvolvableWorld.performInitialisation();
        myEvolvableWorld.evolve();
        double bestfitnessvalue = myEvolvableWorld.bestSolution.getFunctionValue();
        double[] bestgenome = myEvolvableWorld.bestSolution.getX();
    }
}
