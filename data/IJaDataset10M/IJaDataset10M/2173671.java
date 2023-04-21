package weka.classifiers.meta;

import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests Vote. Run from the command line with:<p/>
 * java weka.classifiers.meta.VoteTest
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 1.2 $
 */
public class VoteTest extends AbstractClassifierTest {

    public VoteTest(String name) {
        super(name);
    }

    /** Creates a default Vote */
    public Classifier getClassifier() {
        return new Vote();
    }

    public static Test suite() {
        return new TestSuite(VoteTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
