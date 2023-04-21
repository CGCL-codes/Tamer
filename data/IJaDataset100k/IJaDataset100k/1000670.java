package weka.classifiers.lazy;

import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests IBk. Run from the command line with:<p>
 * java weka.classifiers.lazy.IBkTest
 *
 * @author <a href="mailto:eibe@cs.waikato.ac.nz">Eibe Frank</a>
 * @version $Revision: 1.2 $
 */
public class IBkTest extends AbstractClassifierTest {

    public IBkTest(String name) {
        super(name);
    }

    /** Creates a default IBk */
    public Classifier getClassifier() {
        return new IBk();
    }

    public static Test suite() {
        return new TestSuite(IBkTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
