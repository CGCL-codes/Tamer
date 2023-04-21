package net.sf.ij_plugins.filters;

import ij.ImagePlus;
import ij.process.FloatProcessor;
import junit.framework.TestCase;
import net.sf.ij_plugins.io.IOUtils;

/**
 * @author Jarek Sacha
 * @version $Revision: 1.1 $
 */
public class RunningFilterTest extends TestCase {

    public RunningFilterTest(String test) {
        super(test);
    }

    /**
     * The fixture set up called before every test method.
     */
    protected void setUp() throws Exception {
    }

    /**
     * The fixture clean up called after every test method.
     */
    protected void tearDown() throws Exception {
    }

    public void testFilter() throws Exception {
        ImagePlus imp = IOUtils.openImage("test/data/blobs_noise.png");
        final FloatProcessor fp = (FloatProcessor) imp.getProcessor().convertToFloat();
        final RunningFilter filter = new RunningFilter(new RunningMedianRBTOperator(), 29, 29);
        filter.run(fp);
    }
}
