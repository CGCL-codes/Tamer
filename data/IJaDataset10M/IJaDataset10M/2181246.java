package jbnc.util;

import BayesianNetworks.BayesNet;
import BayesianNetworks.ProbabilityFunction;
import java.util.Vector;

/**
 *  Description of the Class
 *
 * @author     Jarek Sacha
 * @since      June 1, 1999
 */
public final class FrequencyTable {

    protected int nbCases = 0;

    protected FrequencyNode[] frequencyNodes;

    /**
   *  Constructor for the FrequencyTable object
   *
   * @param  net  Description of Parameter
   */
    public FrequencyTable(BayesNet net) {
        ProbabilityFunction[] funcs = net.get_probability_functions();
        frequencyNodes = new FrequencyNode[funcs.length];
        for (int i = 0; i < frequencyNodes.length; ++i) {
            frequencyNodes[i] = new FrequencyNode(funcs[i]);
        }
    }

    /**
   *  Sets the Cases attribute of the FrequencyTable object
   *
   * @param  cases  The new Cases value
   */
    public void setCases(Vector cases) {
        removeAllCases();
        int nbCases = cases.size();
        for (int c = 0; c < nbCases; ++c) {
            int[] thisCase = (int[]) cases.get(c);
            for (int i = 0; i < frequencyNodes.length; ++i) {
                frequencyNodes[i].addCase(thisCase);
            }
        }
        this.nbCases = nbCases;
    }

    /**
   *  Gets the N attribute of the FrequencyTable object
   *
   * @param  i  Description of Parameter
   * @param  j  Description of Parameter
   * @param  k  Description of Parameter
   * @return    The N value
   */
    public int getN(int i, int j, int k) {
        return frequencyNodes[i].nJK[j][k];
    }

    /**
   *  Gets the N attribute of the FrequencyTable object
   *
   * @param  i  Description of Parameter
   * @param  j  Description of Parameter
   * @return    The N value
   */
    public int getN(int i, int j) {
        return frequencyNodes[i].nJ[j];
    }

    /**
   *  Gets the NbCases attribute of the FrequencyTable object
   *
   * @return    The NbCases value
   */
    public int getNbCases() {
        return nbCases;
    }

    /**
   *  Adds a feature to the Case attribute of the FrequencyTable object
   *
   * @param  aCase  The feature to be added to the Case attribute
   */
    public void addCase(int[] aCase) {
        for (int i = 0; i < frequencyNodes.length; ++i) {
            frequencyNodes[i].addCase(aCase);
        }
        ++nbCases;
    }

    /**
   *  Description of the Method
   *
   * @param  aCase          Description of Parameter
   * @exception  Exception  Description of Exception
   */
    public void removeCase(int[] aCase) throws Exception {
        if (nbCases < 1) {
            throw new Exception("No cases to remove from.");
        }
        for (int i = 0; i < frequencyNodes.length; ++i) {
            frequencyNodes[i].removeCase(aCase);
        }
        --nbCases;
    }

    /**  Description of the Method */
    public void removeAllCases() {
        nbCases = 0;
        if (frequencyNodes != null) {
            for (int i = 0; i < frequencyNodes.length; ++i) {
                frequencyNodes[i].removeAllCases();
            }
        }
    }

    /**
   *  Description of the Method
   *
   * @param  net            Description of Parameter
   * @param  usePriors      Description of Parameter
   * @param  alphaK         Description of Parameter
   * @exception  Exception  Description of Exception
   */
    public void learnNetParam(BayesNet net, boolean usePriors, double alphaK) throws Exception {
        if (usePriors && (alphaK <= 0)) {
            throw new Exception("When using Dirihlet priors alphaK must be greater than zero.");
        }
        if (frequencyNodes.length != net.number_variables()) {
            throw new Exception("Number of variables in the data set and in the network do no agree (" + frequencyNodes.length + "!=" + net.number_variables() + ").");
        }
        ProbabilityFunction[] funcs = net.get_probability_functions();
        for (int funcNb = 0; funcNb < funcs.length; ++funcNb) {
            if (funcs[funcNb] == null) {
                continue;
            }
            double[] vals = funcs[funcNb].get_values();
            frequencyNodes[funcNb].learnNodeParam(vals, usePriors, alphaK);
        }
    }
}
