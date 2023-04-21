package weka.classifiers.pmml.consumer;

import weka.core.FastVector;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the pmml NeuralNetwork classifier.
 *
 * @author Mark Hall (mhall{[at]}pentaho{[dot]}com)
 * @version $Revision 1.0 $
 */
public class NeuralNetworkTest extends AbstractPMMLClassifierTest {

    public NeuralNetworkTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        m_modelNames = new FastVector();
        m_dataSetNames = new FastVector();
        m_modelNames.addElement("IRIS_MLP.xml");
        m_modelNames.addElement("HEART_RBF.xml");
        m_modelNames.addElement("ElNino_NN.xml");
        m_dataSetNames.addElement("iris.arff");
        m_dataSetNames.addElement("heart-c.arff");
        m_dataSetNames.addElement("Elnino_small.arff");
    }

    public static Test suite() {
        return new TestSuite(weka.classifiers.pmml.consumer.NeuralNetworkTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
