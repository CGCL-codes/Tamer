package kg.apc.jmeter;

import java.io.File;
import java.io.IOException;
import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.util.JMeterUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author undera
 */
public class PluginsCMDWorkerTest {

    private PluginsCMDWorker instance;

    private final String basedir;

    public PluginsCMDWorkerTest() {
        String file = this.getClass().getResource("short.jtl").getPath();
        basedir = file.substring(0, file.lastIndexOf("/"));
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        JMeterUtils.setJMeterHome("");
        instance = new PluginsCMDWorker();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addExportMode method, of class CMDWorker.
     */
    @Test
    public void testAddExportMode() {
        System.out.println("addExportMode");
        int mode = 0;
        instance.addExportMode(mode);
    }

    /**
     * Test of setInputFile method, of class CMDWorker.
     */
    @Test
    public void testSetInputFile() {
        System.out.println("setInputFile");
        String string = "";
        instance.setInputFile(string);
    }

    /**
     * Test of setOutputCSVFile method, of class CMDWorker.
     */
    @Test
    public void testSetOutputCSVFile() {
        System.out.println("setOutputCSVFile");
        String string = "";
        instance.setOutputCSVFile(string);
    }

    /**
     * Test of setOutputPNGFile method, of class CMDWorker.
     */
    @Test
    public void testSetOutputPNGFile() {
        System.out.println("setOutputPNGFile");
        String string = "";
        instance.setOutputPNGFile(string);
    }

    /**
     * Test of setPluginType method, of class CMDWorker.
     */
    @Test
    public void testSetPluginType() {
        System.out.println("setPluginType");
        String string = "";
        instance.setPluginType(string);
    }

    /**
     * Test of doJob method, of class CMDWorker.
     */
    @Test
    public void testDoJob() throws IOException {
        System.out.println("doJob");
        JMeterUtils.setProperty("saveservice_properties", basedir + "/saveservice.properties");
        instance.setInputFile(basedir + "/short.jtl");
        File pngfile = File.createTempFile("test", ".png");
        instance.setOutputPNGFile(pngfile.getAbsolutePath());
        File csvfile = File.createTempFile("test", ".csv");
        instance.setOutputCSVFile(csvfile.getAbsolutePath());
        instance.setPluginType("ResponseTimesDistribution");
        instance.addExportMode(PluginsCMDWorker.EXPORT_PNG);
        instance.addExportMode(PluginsCMDWorker.EXPORT_CSV);
        int result = instance.doJob();
        int expResult = 0;
        assertEquals(expResult, result);
        System.out.println(pngfile.length());
        assertTrue(73 == csvfile.length() || 77 == csvfile.length());
        assertTrue(16000 < pngfile.length());
    }

    /**
     * Test of doJob method, of class CMDWorker.
     */
    @Test
    public void testDoJob_png() throws IOException {
        System.out.println("doJob");
        instance.setInputFile(basedir + "/short.jtl");
        instance.setOutputPNGFile(File.createTempFile("test", ".png").getAbsolutePath());
        instance.setPluginType("ResponseTimesDistribution");
        instance.addExportMode(PluginsCMDWorker.EXPORT_PNG);
        int result = instance.doJob();
        int expResult = 0;
        assertEquals(expResult, result);
    }

    /**
     * Test of doJob method, of class CMDWorker.
     */
    @Test
    public void testDoJob_csv() throws IOException {
        System.out.println("doJob");
        instance.setInputFile(basedir + "/short.jtl");
        instance.setOutputCSVFile(File.createTempFile("test", ".csv").getAbsolutePath());
        instance.setPluginType("ResponseTimesDistribution");
        instance.addExportMode(PluginsCMDWorker.EXPORT_CSV);
        int result = instance.doJob();
        int expResult = 0;
        assertEquals(expResult, result);
    }

    @Test
    public void testDoJob_csv_filtered() throws IOException {
        System.out.println("doJob fil1");
        File csvfile = File.createTempFile("test", ".csv");
        instance.setInputFile(basedir + "/short.jtl");
        instance.setOutputCSVFile(csvfile.getAbsolutePath());
        instance.setPluginType("ResponseTimesDistribution");
        instance.addExportMode(PluginsCMDWorker.EXPORT_CSV);
        instance.setIncludeLabels("test");
        int result = instance.doJob();
        int expResult = 0;
        assertEquals(expResult, result);
        assertEquals(21, csvfile.length());
    }

    /**
     * Test of setGraphWidth method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetGraphWidth() {
        System.out.println("setGraphWidth");
        int i = 0;
        instance.setGraphWidth(i);
    }

    /**
     * Test of setGraphHeight method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetGraphHeight() {
        System.out.println("setGraphHeight");
        int i = 0;
        instance.setGraphHeight(i);
    }

    /**
     * Test of setAggregate method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetAggregate() {
        System.out.println("setAggregate");
        boolean b = false;
        instance.setAggregate(0);
    }

    /**
     * Test of setZeroing method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetZeroing() {
        System.out.println("setZeroing");
        int logicValue = 0;
        instance.setZeroing(logicValue);
    }

    /**
     * Test of setPreventOutliers method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetPreventOutliers() {
        System.out.println("setPreventOutliers");
        int logicValue = 0;
        instance.setPreventOutliers(logicValue);
    }

    /**
     * Test of setRowsLimit method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetRowsLimit() {
        System.out.println("setRowsLimit");
        int parseInt = 0;
        instance.setRowsLimit(parseInt);
    }

    /**
     * Test of setForceY method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetForceY() {
        System.out.println("setForceY");
        int parseInt = 0;
        instance.setForceY(parseInt);
    }

    /**
     * Test of setHideLowCounts method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetHideLowCounts() {
        System.out.println("setHideLowCounts");
        int parseInt = 0;
        instance.setHideLowCounts(parseInt);
    }

    /**
     * Test of setGranulation method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetGranulation() {
        System.out.println("setGranulation");
        int parseInt = 0;
        instance.setGranulation(parseInt);
    }

    /**
     * Test of setRelativeTimes method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetRelativeTimes() {
        System.out.println("setRelativeTimes");
        int logicValue = 0;
        instance.setRelativeTimes(logicValue);
    }

    /**
     * Test of setGradient method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetGradient() {
        System.out.println("setGradient");
        int logicValue = 0;
        instance.setGradient(logicValue);
    }

    /**
     * Test of setIncludeLabels method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetIncludeLabels() {
        System.out.println("setIncludeLabels");
        String string = "";
        instance.setIncludeLabels(string);
    }

    /**
     * Test of setExcludeLabels method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetExcludeLabels() {
        System.out.println("setExcludeLabels");
        String string = "";
        instance.setExcludeLabels(string);
    }

    /**
     * Test of setAutoScaleRows method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetAutoScaleRows() {
        System.out.println("setAutoScaleRows");
        int logicValue = 0;
        instance.setAutoScaleRows(logicValue);
    }

    /**
     * Test of setLineWeight method, of class PluginsCMDWorker.
     */
    @Test
    public void testSetLineWeight() {
        System.out.println("setLineWeight");
        float parseInt = 0.0F;
        instance.setLineWeight(parseInt);
    }
}
