package weka.clusterers;

import weka.clusterers.AbstractClustererTest;
import weka.clusterers.Clusterer;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests FilteredClusterer. Run from the command line with:<p/>
 * java weka.clusterers.FilteredClustererTest
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 1.1 $
 */
public class FilteredClustererTest extends AbstractClustererTest {

    public FilteredClustererTest(String name) {
        super(name);
    }

    /** Creates a default FilteredClusterer */
    public Clusterer getClusterer() {
        return new FilteredClusterer();
    }

    public static Test suite() {
        return new TestSuite(FilteredClustererTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
