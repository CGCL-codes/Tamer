package net.sf.csutils.core;

import org.junit.BeforeClass;

public class AbstractJaxMasTestCase extends net.sf.csutils.core.tests.AbstractJaxMasTestCase {

    @BeforeClass
    public static void setUpClass() throws Exception {
        initProperties();
        initDb();
    }
}
