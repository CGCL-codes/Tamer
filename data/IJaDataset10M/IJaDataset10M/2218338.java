package jbnc.measures;

import BayesianNetworks.BayesNet;
import BayesianNetworks.ProbabilityFunction;
import jbnc.dataset.AttributeSpecs;
import jbnc.dataset.AttributeType;
import jbnc.dataset.DatasetInt;
import jbnc.util.BNTools;

/**
 *  Return value of the Heckerman-Geiger-Chickering measure for a Bayesian
 *  network structure (also called Baysian Dirihlet metric). <p>
 *
 *  D. Heckerman, D. Geiger and D. M. Chickering, "Learning Bayesian Networks:
 *  The Combination of Knowledge and Statistical Data", <i>Machine Learning</i>
 *  , vol.20, pp.197-243, 1995. eq.(28).
 *
 * @author     Jarek Sacha
 * @since      June 1, 1999
 */
public class QualityMeasureHGC extends QualityMeasure {

    /**  Constructor for the QualityMeasureHGC object */
    public QualityMeasureHGC() {
        super();
    }

    /**
   *  Constructor for the QualityMeasureHGC object
   *
   * @param  dataset  Description of Parameter
   */
    public QualityMeasureHGC(DatasetInt dataset) {
        super(dataset);
    }

    /**
   *  Gets the Name attribute of the QualityMeasureHGC object
   *
   * @return    The Name value
   */
    public String getName() {
        return "Heckerman-Geiger-Chickering";
    }

    /**
   *  Description of the Method
   *
   * @param  net            Description of Parameter
   * @return                Description of the Returned Value
   * @exception  Exception  Description of Exception
   */
    public final double evaluate(BayesNet net) throws Exception {
        AttributeSpecs[] names = dataset.names;
        for (int n = 0; n < names.length; ++n) {
            if (names[n].getType() != AttributeType.DISCRETE) {
                throw new Exception("All attributes in the data set have to be discrete.");
            }
        }
        int nbVars = names.length;
        int nbCases = dataset.cases.size();
        int[] varSize = new int[nbVars];
        for (int i = 0; i < nbVars; ++i) {
            varSize[i] = names[i].getStates().length;
        }
        if (nbVars != net.number_variables()) {
            throw new Exception("Number of variables in the data set and in the network do no agree (" + nbVars + "!=" + net.number_variables() + ").");
        }
        double alpha_ijk = alphaK;
        if (!usePriors || alphaK < BNTools.beta_ijk) {
            alpha_ijk = BNTools.beta_ijk;
        }
        double qm = 0;
        double log_gamma_alpha_ijk = BNTools.gammaLn(alpha_ijk);
        ProbabilityFunction[] funcs = net.get_probability_functions();
        for (int funcNb = 0; funcNb < funcs.length; ++funcNb) {
            if (funcs[funcNb] == null) {
                continue;
            }
            int[] varIndx = funcs[funcNb].get_indexes();
            double[] vals = funcs[funcNb].get_values();
            int[] vCount = new int[vals.length];
            int[] varCycle = new int[varIndx.length];
            varCycle[varCycle.length - 1] = 1;
            for (int i = varCycle.length - 2; i >= 0; --i) {
                varCycle[i] = varCycle[i + 1] * varSize[varIndx[i + 1]];
            }
            int q_i = varCycle[0];
            int r_i = vals.length / q_i;
            int[] count = new int[q_i];
            for (int caseNb = 0; caseNb < nbCases; ++caseNb) {
                int[] thisCase = (int[]) dataset.cases.get(caseNb);
                int index = 0;
                for (int varNb = 0; varNb < varIndx.length; ++varNb) {
                    index += varCycle[varNb] * thisCase[varIndx[varNb]];
                }
                ++vCount[index];
                ++count[index % q_i];
            }
            double alpha_ij = alpha_ijk * r_i;
            double log_gamma_alpha_ij = BNTools.gammaLn(alpha_ij);
            for (int j = 0; j < count.length; ++j) {
                qm += log_gamma_alpha_ij - BNTools.gammaLn(alpha_ij + count[j]);
            }
            for (int i = 0; i < vals.length; ++i) {
                qm += BNTools.gammaLn(alpha_ijk + vCount[i]) - log_gamma_alpha_ijk;
            }
        }
        return qm;
    }
}
