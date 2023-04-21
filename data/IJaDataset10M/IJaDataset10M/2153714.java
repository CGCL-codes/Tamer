package jdbm.helper;

import junit.framework.*;

/**
 *  Package test suite.
 *
 * @author <a href="mailto:boisvert@intalio.com">Alex Boisvert</a>
 * @version $Id: Test.java,v 1.7 2006/05/15 14:42:13 thompsonbry Exp $
 */
public class Test extends TestCase {

    public static junit.framework.Test suite() {
        TestSuite retval = new TestSuite();
        retval.addTestSuite(ConversionTest.class);
        retval.addTestSuite(TestByteSerializer.class);
        retval.addTestSuite(TestShortSerializer.class);
        retval.addTestSuite(TestCharacterSerializer.class);
        retval.addTest(TestSerializers.suite());
        retval.addTest(jdbm.helper.compression.Test.suite());
        retval.addTest(new TestSuite(TestMRU.class));
        retval.addTest(new TestSuite(TestMRUNativeLong.class));
        retval.addTest(new TestSuite(TestSoftCache.class));
        return retval;
    }
}
