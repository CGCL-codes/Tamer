package test.core.box;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.ibex.js.Fountain;
import org.ibex.js.JSTestUtil;
import test.core.CoreTestSuite;

/**
 * @author mike
 */
public class TestBox extends CoreTestSuite {

    public TestBox(Class klass) {
        super(klass);
    }

    public TestBox() {
        this(TestBox.class);
    }

    public Fountain.Multiple getResourceDirs() {
        Fountain.Multiple r = super.getResourceDirs();
        r.addOverrideStream(JSTestUtil.getResourceFountain(TestBox.class, ".t"));
        return r;
    }

    public static Test suite() {
        TestSuite suite = (TestSuite) CoreTestSuite.suite(new TestBox());
        return suite;
    }

    public static void main(String[] args) throws Throwable {
        CoreTestSuite cts = new CoreTestSuite(TestBox.class);
        TestCase t = cts.createTestCase(cts.getResourceDirs(), "pis_method.t");
        t.runBare();
    }
}
