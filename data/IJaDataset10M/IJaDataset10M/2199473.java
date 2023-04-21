package jbnc.inducers;

import BayesianNetworks.BayesNet;
import jbnc.dataset.AttributeType;
import jbnc.dataset.DatasetInt;
import jbnc.measures.QualityMeasure;

/**
 *  Abstract class for Bayesian network inducers.
 *
 * @author     Jarek Sacha
 * @since      June 1, 1999
 */
public abstract class BayesianInducer {

    protected static final double DEFAULT_APLHA_IJK = 1.0;

    /**  Bayesian network representing the classifier. */
    protected BayesNet net = null;

    protected NetLayout layout = new NetLayout();

    protected boolean debugMode = false;

    /**
   *  Some Bayesian network inducers may use a quality measure to select the
   *  most optimal networ architecture for a given training data set.
   */
    protected QualityMeasure qualityMeasure = null;

    /**
   *  Sets the debug mode flag.
   *
   * @param  mode  The new DebugMode value
   */
    public void setDebugMode(boolean mode) {
        debugMode = mode;
    }

    /**
   *  Sets new quality measure.
   *
   * @param  qualityMeasure  The new QualityMeasure value
   */
    public void setQualityMeasure(QualityMeasure qualityMeasure) {
        this.qualityMeasure = qualityMeasure;
    }

    /**
   *  Gets Bayesian network representing the classifier.
   *
   * @return                The Network value
   * @exception  Exception  Description of Exception
   */
    public BayesNet getNetwork() throws Exception {
        return net;
    }

    /**
   *  Gets the debug mode flag.
   *
   * @return    The DebugMode value
   */
    public boolean getDebugMode() {
        return debugMode;
    }

    /**
   *  Gets the current quality measure.
   *
   * @return    The QualityMeasure value
   */
    public QualityMeasure getQualityMeasure() {
        return this.qualityMeasure;
    }

    /**  Reset the inducer. Get ready for learning of a new network. */
    public void reset() {
        clean();
    }

    public void train(jbnc.util.FrequencyCalc fc) throws Exception {
        train(fc, false, 0);
    }

    public abstract void train(jbnc.util.FrequencyCalc fc, boolean usePriors, double alpha_ijk) throws Exception;

    /**  Clean member variables. */
    protected void clean() {
        net = null;
    }

    /**
   *  Check validity of the dataset.
   *
   * @param  dataset        Description of Parameter
   * @exception  Exception  Description of Exception
   */
    protected void verifyDataset(DatasetInt dataset) throws Exception {
        if (dataset == null || dataset.names.length < 1) {
            throw new Exception("Training data set has to have at least the class variable.");
        }
        for (int n = 0; n < dataset.names.length; ++n) {
            if (dataset.names[n].getType() != AttributeType.DISCRETE) {
                throw new Exception("All attributes in the data set have to be discrete.");
            }
        }
    }

    /**
   *  Network layout.
   *
   * @author     Jarek Sacha
   * @since      June 1, 1999
   */
    protected class NetLayout {

        int xAttrib = 70;

        int xClass = 240;

        int yOffset = 40;

        int yStep = 50;
    }
}
