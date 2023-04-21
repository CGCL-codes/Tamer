package jumble.fast;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Class used for testing.
 * 
 * @author Tin Pavlinic
 * @version $Revision 1.0 $
 */
public class MismatchingSuiteT extends TestCase {

    public void test1() {
    }

    public void test2() {
    }

    public void test3() {
    }

    public void test4() {
    }

    public static Test suite() {
        TestSuite suiteOrig = new TestSuite(MismatchingSuiteT.class);
        TestSuite suiteRet = new TestSuite();
        suiteRet.addTest(suiteOrig.testAt(0));
        suiteRet.addTest(suiteOrig.testAt(2));
        return suiteRet;
    }
}
