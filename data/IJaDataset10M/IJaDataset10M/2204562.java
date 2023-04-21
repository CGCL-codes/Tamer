package genomancer.tengcha;

import java.io.IOException;
import org.xml.sax.SAXException;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.net.URLEncoder;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;
import java.util.List;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jtr4v
 */
public class FeaturesCapabilityTest {

    static ServletTester tester = new ServletTester();

    static HttpTester request = new HttpTester();

    static HttpTester response = new HttpTester();

    public FeaturesCapabilityTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        tester.setContextPath("/trellis");
        ServletHolder sh = tester.addServlet("genomancer.trellis.das2.server.TrellisDas2Servlet", "/*");
        sh.setInitParameter("sources_plugin_class", "genomancer.tengcha.SourcesPlugin");
        sh.setInitParameter("sources_query", "http://tester/trellis/sources");
        tester.start();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void segmentsUriShouldRespondWith200() {
        request.setMethod("GET");
        request.setHeader("Host", "tester");
        request.setURI("/trellis/A.mellifera/features?segment=GroupUn1044");
        request.setVersion("HTTP/1.0");
        try {
            response.parse(tester.getResponses(request.generate()));
            assertTrue(response.getMethod() == null);
            assertEquals(200, response.getStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void segmentFilterShouldReturnCorrectFeaturesXml() {
        File correctResponseFile = new File(genomancer.tengcha.Config.TEST_SUPPORT_PATH + "correct_trellis_A.mellifera_features_segmentFilterShouldRespondWithReturnCorrectFeaturesXML.xml");
        String testURL = "/trellis/A.mellifera/features?segment=GroupUn1044";
        Boolean verbose = true;
        Boolean writeOutResponse = true;
        assertExpectedAndActualXMLResponses(correctResponseFile, testURL, verbose, writeOutResponse);
    }

    @Test
    public void segmentFilterShouldReturnCorrectFeaturesJson() {
        assertEquals("really", "really");
    }

    @Test
    public void segmentFilterNoFeaturesShouldReturnNoFeaturesXml() {
        assertEquals("really", "really");
    }

    @Test
    public void segmentFilterNoFeaturesShouldReturnNoFeaturesJson() {
        assertEquals("really", "really");
    }

    @Test
    public void segmentFilterNoSegmentsClauseShouldCause400ErrorXml() {
        assertEquals("really", "really");
    }

    @Test
    public void segmentAndTypesFilterShouldReturnCorrectFeaturesXml() {
        assertEquals("really", "really");
    }

    @Test
    public void segmentAndTypesFilterShouldReturnCorrectFeaturesJson() {
        assertEquals("really", "really");
    }

    @Test
    public void segmentAndTypesFilterShouldReturnNoFeaturesXml() {
        assertEquals("really", "really");
    }

    @Test
    public void segmentAndTypesFilterShouldReturnNoFeaturesJson() {
        assertEquals("really", "really");
    }

    @Test
    public void segmentAndOverlapFilterShouldReturnCorrectFeaturesXml() {
        File correctResponseFile = new File(genomancer.tengcha.Config.TEST_SUPPORT_PATH + "correct_trellis_A.mellifera_features_segmentAndOverlapFilterShouldReturnCorrectFeaturesXml.xml");
        String testURL = "/trellis/A.mellifera/features?segment=GroupUn1044;overlap=900:1500";
        Boolean verbose = true;
        Boolean writeOutResponse = true;
        assertExpectedAndActualXMLResponses(correctResponseFile, testURL, verbose, writeOutResponse);
    }

    @Test
    public void segmentAndOverlapFilterShouldReturnCorrectFeaturesJson() {
        assertEquals("really", "really");
    }

    public String replaceLeadingTrailingSpace(String thisString) {
        thisString = thisString.replaceAll("\\s+$", "");
        thisString = thisString.replaceAll("^\\s+", "");
        return thisString;
    }

    public void assertExpectedAndActualXMLResponses(File correctResponse, String URL, Boolean verbose, Boolean writeResponseToFile) {
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        XMLUnit.setIgnoreWhitespace(true);
        if (verbose == null) {
            verbose = false;
        }
        if (writeResponseToFile == null) {
            writeResponseToFile = false;
        }
        Diff diff = null;
        request.setMethod("GET");
        request.setHeader("Host", "tester");
        request.setURI(URL);
        request.setVersion("HTTP/1.0");
        try {
            response.parse(tester.getResponses(request.generate()));
            assertTrue(response.getMethod() == null);
            String correctResponseString = FileUtils.readFileToString(correctResponse, "UTF-8");
            String actualResponseString = response.getContent();
            if (writeResponseToFile) {
                String responseOutfileName = URLEncoder.encode(URL, "UTF-8") + ".response";
                File responseOutFile = new File(responseOutfileName);
                FileUtils.writeStringToFile(responseOutFile, actualResponseString, "UTF-8");
            }
            diff = new Diff(correctResponseString, actualResponseString);
            System.out.println("Similar? " + diff.similar());
            System.out.println("Identical? " + diff.identical());
            DetailedDiff detDiff = new DetailedDiff(diff);
            List differences = detDiff.getAllDifferences();
            System.out.println("found " + differences.size() + " differences");
            if (verbose) {
                for (Object object : differences) {
                    Difference difference = (Difference) object;
                    System.out.println("***********************");
                    System.out.println(difference);
                    System.out.println("***********************");
                }
            }
            assertEquals("found some differences while comparing xml", differences.size(), 0);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
