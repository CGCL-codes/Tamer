package jeco.lib.problems.fsm.ge;

import Individuals.Phenotype;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import jeco.lib.problems.fsm.problemFSMs.OracleProblemFunction;

/**
 * Processes several inputs for the FSM
 *
 * @author J. M. Colmenar
 */
public class FitnessV2SeveralInputs extends FitnessV2 {

    public static ArrayList<String> Xs = new ArrayList<String>();

    public static ArrayList<String> realOutput = new ArrayList<String>();

    public static OracleProblemFunction oracle = null;

    public static int inputLength = 0;

    public static double errorWeight = 1.0;

    public static double statesWeight = 0.01;

    public static boolean noErrorsMeans0Fitness = true;

    public static int lowerLimitForReducingPhen = 6;

    /** Above this value, all individuals are penalized with worst value */
    public static int upperPhenLimit = 50;

    /**
     * Adds a pair input/output only if was not already added
     * 
     * @param solPair
     * @return true if the pair was not already added
     */
    static boolean addPair(String[] solPair) {
        boolean ret = true;
        if (Xs.contains(solPair[0])) {
            ret = false;
        } else {
            Xs.add(solPair[0]);
            realOutput.add(solPair[1]);
        }
        return ret;
    }

    static void logProcessedInputs(Logger logger, FiniteStateMachine fsm) {
        logger.log(Level.INFO, "Evaluated inputs list (input;FSM output;Real output;# Diff.):");
        String fsmOutput = "";
        for (int i = 0; i < Xs.size(); i++) {
            int diffs = 0;
            fsmOutput = fsm.getOutputString(Xs.get(i));
            for (int j = 0; j < fsmOutput.length(); j++) {
                if (!fsmOutput.substring(j, j + 1).equals(realOutput.get(i).substring(j, j + 1))) diffs++;
            }
            logger.log(Level.INFO, Xs.get(i) + ";" + fsmOutput + ";" + realOutput.get(i) + ";" + diffs);
        }
    }

    public void reducePhenotype(Phenotype phenotype) {
        String origFsmAsString = phenotype.getStringNoSpace();
        HashSet<String> states = new HashSet<String>();
        states.add("F0");
        states.add("F1");
        int currentPos = 0;
        String currState = origFsmAsString.substring(currentPos, currentPos + 2);
        if (states.contains(currState)) {
            currentPos = 2;
        } else {
            int pendingChildren = 2;
            currentPos += 2;
            states.add(currState);
            while ((pendingChildren > 0) && (currentPos < origFsmAsString.length() - 1)) {
                pendingChildren -= 1;
                currState = origFsmAsString.substring(currentPos, currentPos + 2);
                if (!states.contains(currState)) {
                    pendingChildren += 2;
                    states.add(currState);
                }
                currentPos += 2;
            }
        }
        int removed = 0;
        for (int i = origFsmAsString.length() - 1; i >= currentPos; i--) {
            phenotype.remove(i);
            removed++;
        }
    }

    @Override
    public double evaluate(Phenotype phenotype) {
        double fsmMetricValue = Double.POSITIVE_INFINITY;
        if (((phenotype.getStringNoSpace().length() % 2) == 0) && (phenotype.getStringNoSpace().length() < upperPhenLimit)) {
            if (phenotype.getStringNoSpace().length() > lowerLimitForReducingPhen) reducePhenotype(phenotype);
            SimulatorV2SeveralInputs simulator = new SimulatorV2SeveralInputs(phenotype.getStringNoSpace(), Xs, realOutput, oracle, inputLength);
            SimulatorV2SeveralInputs.currentMetricFunction = SimulatorV2SeveralInputs.NUMBER_ERRORS_METRIC;
            fsmMetricValue = simulator.simulate();
        }
        double fitness = (fsmMetricValue * errorWeight + phenotype.getStringNoSpace().length() * statesWeight);
        if (noErrorsMeans0Fitness && (fsmMetricValue == 0.0)) return 0.0; else return fitness;
    }
}
