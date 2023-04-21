package weka.classifiers.bnc;

import BayesianNetworks.BayesNet;
import BayesianNetworks.ProbabilityFunction;
import BayesianNetworks.ProbabilityVariable;
import junit.framework.TestCase;
import weka.core.SerializedObject;

/**
 * @author Jarek Sacha
 * @version $Revision: 144 $
 */
public class SerializedObjectTest extends TestCase {

    public SerializedObjectTest(String test) {
        super(test);
    }

    public void testBayesNet() throws Exception {
        BayesNet bayesNet = new BayesNet();
        SerializedObject so = new SerializedObject(bayesNet);
        BayesNet newBayesNet = (BayesNet) so.getObject();
    }

    public void testProbabilityFunction() throws Exception {
        ProbabilityFunction probabilityFunction = new ProbabilityFunction();
        SerializedObject so = new SerializedObject(probabilityFunction);
        ProbabilityFunction newprobabilityFunction = (ProbabilityFunction) so.getObject();
    }

    public void testProbabilityVariable() throws Exception {
        ProbabilityVariable probabilityFunction = new ProbabilityVariable();
        SerializedObject so = new SerializedObject(probabilityFunction);
        ProbabilityVariable newprobabilityFunction = (ProbabilityVariable) so.getObject();
    }

    /**
     * The fixture set up called before every test method
     */
    protected void setUp() throws Exception {
    }

    /**
     * The fixture clean up called after every test method
     */
    protected void tearDown() throws Exception {
    }
}
