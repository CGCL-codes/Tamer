package weka.classifiers.rules;

import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests DecisionTable. Run from the command line with:<p>
 * java weka.classifiers.rules.DecisionTableTest
 *
 * @author <a href="mailto:eibe@cs.waikato.ac.nz">Eibe Frank</a>
 * @version $Revision: 1.2 $
 */
public class DecisionTableTest extends AbstractClassifierTest {

    public DecisionTableTest(String name) {
        super(name);
    }

    /** Creates a default DecisionTable */
    public Classifier getClassifier() {
        return new DecisionTable();
    }

    public static Test suite() {
        return new TestSuite(DecisionTableTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
