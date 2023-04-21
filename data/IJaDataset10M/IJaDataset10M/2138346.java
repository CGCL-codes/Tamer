package jbnc.graphs;

import BayesianNetworks.BayesNet;
import BayesianNetworks.DiscreteVariable;
import BayesianNetworks.ProbabilityFunction;
import BayesianNetworks.ProbabilityVariable;
import java.io.Serializable;

/**
 * Efficient calculation of the joint probability distribution and and conditional probability of the class variable
 * given values of all attributes. Assumes that all nodes are instantiated.
 *
 * @author Jarek Sacha
 * @since June 1, 1999
 */
public class BNCInference implements Serializable {

    /** */
    protected int nbVars = 0;

    /** */
    protected int nbClasses = 0;

    /**
     * Probability function values for each of the network nodes.
     */
    protected double[][] probFuncs = null;

    /**
     * Variables for each of the probability function.
     */
    protected int[][] variables;

    /**
     * Used to calculate index within the probFunction.
     */
    protected int[][] radix;

    /** */
    protected double[] condClassProb = null;

    /**
     * @param net Description of Parameter
     * @throws Exception Description of Exception
     */
    public BNCInference(BayesNet net) throws Exception {
        ProbabilityFunction[] pFuncs = net.get_probability_functions();
        ProbabilityVariable[] pVars = net.get_probability_variables();
        if (pFuncs.length != pVars.length) {
            throw new Exception("Number of functions does not " + "match number of variables in the network.");
        }
        nbVars = pVars.length;
        int[] varSize = new int[nbVars];
        for (int i = 0; i < nbVars; ++i) {
            varSize[i] = pVars[i].number_values();
        }
        nbClasses = varSize[nbVars - 1];
        condClassProb = new double[nbClasses];
        probFuncs = new double[nbVars][];
        variables = new int[nbVars][];
        radix = new int[nbVars][];
        for (int i = 0; i < nbVars; ++i) {
            DiscreteVariable[] vars = pFuncs[i].get_variables();
            int thisIndex = vars[0].get_index();
            probFuncs[thisIndex] = pFuncs[i].get_values();
            variables[thisIndex] = pFuncs[i].get_indexes();
            int[] varCycle = new int[variables[thisIndex].length];
            varCycle[varCycle.length - 1] = 1;
            for (int j = varCycle.length - 2; j >= 0; --j) {
                varCycle[j] = varCycle[j + 1] * varSize[variables[thisIndex][j + 1]];
            }
            radix[thisIndex] = varCycle;
        }
    }

    /**
     * Return the value of the joint probability for the instantiation of the variables given by parameter 'vars'.
     *
     * @param aCase Values for each of the varaibles in the network. The last variable is class variable.
     * @return The JointProb value
     * @throws Exception when size of array 'aCase' is different then expected.
     */
    public double getJointProb(int[] aCase) throws Exception {
        if (nbVars != aCase.length) {
            throw new Exception("Incorrect number of variables in the case. " + "Expecting " + nbVars + ", got " + aCase.length);
        }
        double prob = 1;
        for (int i = 0; i < nbVars; ++i) {
            int probIndex = 0;
            for (int j = 0; j < variables[i].length; ++j) {
                int r = radix[i][j];
                int v = variables[i][j];
                int c = aCase[v];
                probIndex += r * c;
            }
            prob *= probFuncs[i][probIndex];
        }
        return prob;
    }

    /**
     * Return values of the class node conditional probability given instantiation of attribute variables.
     *
     * @param aCase Values for each of the varaibles in the network. The last variable is class variable. Its value is
     *              ignored.
     * @return The CondClassProb value
     * @throws Exception when size of array 'aCase' is different then expected.
     */
    public double[] getCondClassProb(int[] aCase) throws Exception {
        if (nbVars != aCase.length) {
            throw new Exception("Incorrect number of variables in the case. " + "Expecting " + nbVars + ", got " + aCase.length);
        }
        int origClassIndex = aCase[nbVars - 1];
        double pA = 0;
        for (int c = 0; c < nbClasses; ++c) {
            aCase[nbVars - 1] = c;
            condClassProb[c] = getJointProb(aCase);
            pA += condClassProb[c];
        }
        for (int c = 0; c < nbClasses; ++c) {
            condClassProb[c] /= pA;
        }
        aCase[nbVars - 1] = origClassIndex;
        return condClassProb;
    }
}
