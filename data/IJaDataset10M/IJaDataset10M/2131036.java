package test.darq;

import junit.framework.TestSuite;
import test.darq.optimizer.OptimizerAllTests;
import com.hp.hpl.jena.query.expr.E_Function;
import com.hp.hpl.jena.query.expr.NodeValue;

/**
 * All the DARQ tests 
 * @author		ARQTestSuite by Andy Seaborne , Modifed for DARQ by Bastian Quilitz
 * @version 	$Id$
 */
public class DARQTestSuite extends TestSuite {

    static final String testDirDARQ = "testing/DARQ";

    public static TestSuite suite() {
        return new DARQTestSuite();
    }

    private DARQTestSuite() {
        super("ARQ");
        NodeValue.VerboseWarnings = false;
        E_Function.WarnOnUnknownFunction = false;
        addTest(OptimizerAllTests.suite());
        addTest(QueryTestSuiteFactory.make(testDirDARQ + "/manifest-darq.ttl"));
    }
}
