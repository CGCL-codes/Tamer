package weka.clusterers;

import weka.clusterers.AbstractClustererTest;
import weka.clusterers.Clusterer;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests MakeDensityBasedClusterer. Run from the command line with:<p/>
 * java weka.clusterers.MakeDensityBasedClustererTest
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 1.1 $
 */
public class MakeDensityBasedClustererTest extends AbstractClustererTest {

    public MakeDensityBasedClustererTest(String name) {
        super(name);
    }

    /** Creates a default MakeDensityBasedClusterer */
    public Clusterer getClusterer() {
        return new MakeDensityBasedClusterer();
    }

    public static Test suite() {
        return new TestSuite(MakeDensityBasedClustererTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
