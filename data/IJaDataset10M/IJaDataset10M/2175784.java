package org.pwsafe;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.pwsafe.lib.AllLibTests;
import org.pwsafe.lib.crypto.AllCryptoTests;
import org.pwsafe.lib.datastore.AllDataStoreTests;
import org.pwsafe.lib.file.AllFileTests;
import org.pwsafe.util.AllUtilTests;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for org.pwsafe");
        suite.addTest(AllLibTests.suite());
        suite.addTest(AllCryptoTests.suite());
        suite.addTest(AllFileTests.suite());
        suite.addTest(AllDataStoreTests.suite());
        suite.addTest(AllUtilTests.suite());
        return suite;
    }
}
