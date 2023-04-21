package org.codecover.componenttest.metrics;

import java.util.List;
import org.codecover.metrics.coverage.BranchCoverage;
import org.codecover.metrics.coverage.CoverageResult;
import org.codecover.metrics.coverage.LoopCoverage;
import org.codecover.metrics.coverage.StatementCoverage;
import org.codecover.metrics.coverage.TermCoverage;
import org.codecover.model.MASTBuilder;
import org.codecover.model.TestCase;
import org.codecover.model.TestSession;
import org.codecover.model.TestSessionContainer;
import org.codecover.model.exceptions.FileLoadException;
import org.codecover.model.mast.HierarchyLevel;
import org.codecover.model.utils.Logger;
import org.codecover.model.utils.SimpleLogger;

/**
 * 
 * @author Tilmann Scheller
 * @version 1.0 ($Id: CM0008.java 71 2010-04-14 18:28:46Z schmidberger $)
 */
public class CM0008 extends junit.framework.TestCase {

    public void testCM0008() throws FileLoadException {
        String containerLocation = "../../qa/testdata/containers/metrics/metrics-condition-full.xml";
        Logger logger = new SimpleLogger();
        MASTBuilder builder = new MASTBuilder(logger);
        TestSessionContainer tsc = null;
        TestSession ts = null;
        List<TestCase> testCases = null;
        CoverageResult result = null;
        HierarchyLevel code = null;
        tsc = TestSessionContainer.load(org.codecover.model.extensions.PluginManager.create(), logger, builder, containerLocation);
        code = tsc.getCode();
        assertNotNull(code);
        code = code.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0);
        assertEquals("TestClass1", code.getName());
        ts = tsc.getTestSessionWithName("ts1");
        assertNotNull(ts);
        testCases = ts.getTestCases();
        assertNotNull(testCases);
        StatementCoverage statementCoverage = StatementCoverage.getInstance();
        BranchCoverage branchCoverage = BranchCoverage.getInstance();
        TermCoverage termCoverage = TermCoverage.getInstance();
        LoopCoverage loopCoverage = LoopCoverage.getInstance();
        for (HierarchyLevel curr : code.getChildren()) {
            result = statementCoverage.getCoverage(testCases, curr);
            assertEquals(0, result.getCoveredItems());
            result = branchCoverage.getCoverage(testCases, curr);
            assertEquals(0, result.getCoveredItems());
            result = termCoverage.getCoverage(testCases, curr);
            if (curr.getName().equals("ifbranch")) {
                assertEquals(1, result.getCoveredItems());
            } else if (curr.getName().equals("complexIfAnd") || curr.getName().equals("complexIfOr")) {
                assertEquals(2, result.getCoveredItems());
            } else {
                assertEquals(0, result.getCoveredItems());
            }
            result = loopCoverage.getCoverage(testCases, curr);
            assertEquals(0, result.getCoveredItems());
        }
    }
}
