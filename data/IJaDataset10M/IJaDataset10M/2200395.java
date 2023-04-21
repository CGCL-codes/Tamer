package ArianneViewer;

import junit.framework.*;

/**
 * <p>Title: Guide Viewer</p>
 *
 * <p>Description: Visualizzatore per pagine create con Arianne Editor</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Itaco S.r.l.</p>
 *
 * @author Andrea Annibali
 * @version 1.0
 */
public class TestSuiteGraphEngine extends TestCase {

    public TestSuiteGraphEngine(String s) {
        super(s);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(ArianneViewer.TestEvaluator.class);
        suite.addTestSuite(ArianneViewer.TestParserUtils.class);
        suite.addTestSuite(ArianneViewer.TestExecutor.class);
        return suite;
    }
}
