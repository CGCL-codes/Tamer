package fitnesse;

import junit.framework.TestCase;

public class SuiteExporterArgumentsTest extends TestCase {

    private SuiteExporterArguments args;

    public void setUp() throws Exception {
        args = new SuiteExporterArguments();
    }

    public void testArgumentsDefaults() throws Exception {
        assertEquals(80, args.getPort());
        assertEquals(".", args.getRootPath());
        assertEquals("FitNesseRoot", args.getRootDirectory());
        assertEquals(null, args.getLogDirectory());
        assertEquals(false, args.isOmittingUpdates());
        assertEquals(14, args.getDaysTillVersionsExpire());
        assertEquals(null, args.getUserpass());
    }

    public void testSuiteWikiPagePath() throws Exception {
        String pagePath = "IntegrationTests";
        args.setSuiteWikiPagePath(pagePath);
        assertEquals(pagePath, args.getSuiteWikiPagePath());
    }

    public void testOutputDir() throws Exception {
        String outputDir = "testDir";
        args.setOutputDir(outputDir);
        assertEquals(outputDir, args.getOutputDir());
    }
}
