package org.unicef.doc.ibis.nut.export;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author ngroupp
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ org.unicef.doc.ibis.nut.export.FOPTest.class, org.unicef.doc.ibis.nut.export.ExporterTest.class, org.unicef.doc.ibis.nut.export.HTMLExporterTest.class, org.unicef.doc.ibis.nut.export.NewsMarketChecklistTest.class })
public class ExportSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}
