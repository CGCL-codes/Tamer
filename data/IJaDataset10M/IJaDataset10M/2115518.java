package org.archive.crawler.datamodel.settings;

/** Test the CrawlerSettings object
 *
 * @author John Erik Halse
 */
public class CrawlerSettingsTest extends SettingsFrameworkTestCase {

    /**
     * Constructor for CrawlerSettingsTest.
     * @param arg0
     */
    public CrawlerSettingsTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testAddComplexType() {
        CrawlerModule mod = new CrawlerModule("name");
        DataContainer data = getGlobalSettings().addComplexType(mod);
        assertNotNull(data);
    }

    public final void testGetModule() {
        CrawlerModule mod = new CrawlerModule("name");
        DataContainer data = getGlobalSettings().addComplexType(mod);
        assertSame(mod, getGlobalSettings().getModule("name"));
    }
}
