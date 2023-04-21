package jbnc.measures;

import BayesianNetworks.BayesNet;
import jbnc.dataset.DatasetInt;

/**
 *  Measure the quality of the bayesian network on the dataset using local
 *  criterion measure. <p>
 *
 *  &nbsp;&nbsp;&nbsp; <i>q</i> = LC(<i>B</i> ,<i>D</i> ) = sum<SUB><i>l</i>
 *  =1...n</SUB> log <i>p</i> (<i>c<SUB>l</SUB> </i> | <i><b>a</b> <SUB>l</SUB>
 *  </i>, <i>D<SUB>l</SUB> </i> , <i>B</i> ) <br>
 *
 *
 * @author     Jarek Sacha
 * @since      June 1, 1999
 */
public class QualityMeasureLC extends QualityMeasure {

    /**  Constructor for the QualityMeasureLC object */
    public QualityMeasureLC() {
        super();
    }

    /**
   *  Constructor for the QualityMeasureLC object
   *
   * @param  dataset  Description of Parameter
   */
    public QualityMeasureLC(DatasetInt dataset) {
        super(dataset);
    }

    /**
   *  Gets the Name attribute of the QualityMeasureLC object
   *
   * @return    The Name value
   */
    public String getName() {
        return "Local criterion";
    }

    /**
   *  Description of the Method
   *
   * @param  net            Description of Parameter
   * @return                Description of the Returned Value
   * @exception  Exception  Description of Exception
   */
    public final double evaluate(BayesNet net) throws Exception {
        double q = 0;
        jbnc.util.FrequencyTable freqTable = new jbnc.util.FrequencyTable(net);
        jbnc.graphs.BNCInference inference = new jbnc.graphs.BNCInference(net);
        int nbCases = dataset.cases.size();
        int nbVars = dataset.names.length;
        int nbAttrib = nbVars - 1;
        int[] thisCase = (int[]) dataset.cases.get(0);
        for (int i = 1; i < nbCases; ++i) {
            freqTable.addCase(thisCase);
            freqTable.learnNetParam(net, usePriors, alphaK);
            thisCase = (int[]) dataset.cases.get(i);
            double[] classProb = inference.getCondClassProb(thisCase);
            int trueClassIndex = thisCase[nbAttrib];
            double lq = Math.log(classProb[trueClassIndex]);
            q += lq;
        }
        return q;
    }
}
