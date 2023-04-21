package com.spextreme.nunit.xml.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.StringReader;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.junit.Test;
import com.spextreme.nunit.model.TestResults;

/**
 * Tests the test result processor object.
 */
public class TestResultsProcessorTest {

    /**
	 * Test method for
	 * {@link com.spextreme.nunit.xml.internal.TestResultsProcessor#parseElement(org.jdom.Element)}.
	 */
    @Test
    public void testParseElement() throws Exception {
        final SAXBuilder builder = new SAXBuilder();
        final Document document = builder.build(new StringReader(getXMLDocumentForTest()));
        final TestResultsProcessor processor = new TestResultsProcessor();
        final TestResults results = (TestResults) processor.parseElement(document.getRootElement());
        assertNotNull(results);
        assertEquals("2010-01-18", results.getDate());
        assertEquals(1, results.getTestSuites().size());
        assertTrue(true);
        assertFalse(false);
    }

    /**
	 * Test method for
	 * {@link com.spextreme.nunit.xml.internal.TestResultsProcessor#parseElement(org.jdom.Element)}.
	 */
    @Test
    public void testParseInt() {
        assertEquals(0, TestResultsProcessor.parseInt("abc"));
        assertEquals(1, TestResultsProcessor.parseInt("1"));
    }

    private String getXMLDocumentForTest() {
        final StringBuilder string = new StringBuilder();
        string.append("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>");
        string.append("<test-results name=\"PlayoffLadderCreator_2.4.0.0.dll\" total=\"19\" errors=\"0\" failures=\"1\" not-run=\"0\" inconclusive=\"0\" ignored=\"0\" skipped=\"0\" invalid=\"0\" date=\"2010-01-18\" time=\"09:49:57\">");
        string.append("<environment nunit-version=\"2.5.2.9222\" clr-version=\"2.0.50727.3603\" os-version=\"Microsoft Windows NT 5.1.2600 Service Pack 3\" platform=\"Win32NT\" cwd=\"C:\\SPextreme\\repository\\PlayoffLadderCreator\\trunk\\bin\" machine-name=\"MTNGT7287\" user=\"spaulin\" user-domain=\"ACCT04\" />");
        string.append("<culture-info current-culture=\"en-US\" current-uiculture=\"en-US\" />");
        string.append("<test-suite name=\"PlayoffLadderCreator_2.4.0.0.dll\" executed=\"True\" success=\"False\" time=\"0.094\" asserts=\"0\">");
        string.append("<results>");
        string.append("<test-suite name=\"PlayoffLadderCreator\" executed=\"True\" success=\"False\" time=\"0.078\" asserts=\"0\">");
        string.append("<results>");
        string.append("<test-suite name=\"tests\" executed=\"True\" success=\"False\" time=\"0.078\" asserts=\"0\">");
        string.append("<results>");
        string.append("<test-suite name=\"GameTest\" executed=\"True\" success=\"True\" time=\"0.016\" asserts=\"0\">");
        string.append("<results>");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.GameTest.TestCompareToObject\" executed=\"True\" success=\"True\" time=\"0.016\" asserts=\"4\" />");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.GameTest.TestCompareToObjectInvalid\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"0\" />");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.GameTest.TestToString\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"2\" />");
        string.append("</results>");
        string.append("</test-suite>");
        string.append("<test-suite name=\"LadderNodeTest\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"0\">");
        string.append("<results>");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.LadderNodeTest.Test\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"1\" />");
        string.append("</results>");
        string.append("</test-suite>");
        string.append("<test-suite name=\"TeamsCollectionTest\" executed=\"True\" success=\"False\" time=\"0.031\" asserts=\"0\">");
        string.append("<results>");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamsCollectionTest.TestAddNull\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"0\" />");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamsCollectionTest.TestAddObject\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"0\" />");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamsCollectionTest.TestAddRange\" executed=\"True\" success=\"False\" time=\"0.016\" asserts=\"3\">");
        string.append("<failure>");
        string.append("<message><![CDATA[  Expected: 1");
        string.append("But was:  0");
        string.append("]]></message>");
        string.append("<stack-trace><![CDATA[at PlayoffLadderCreator.tests.TeamsCollectionTest.TestAddRange() in c:\\SPextreme\\repository\\PlayoffLadderCreator\\trunk\\tests\\TeamsCollectionTest.cs:line 46");
        string.append("]]></stack-trace>");
        string.append("</failure>");
        string.append("</test-case>");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamsCollectionTest.TestAddRemove\" executed=\"True\" success=\"True\" time=\"0.016\" asserts=\"18\" />");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamsCollectionTest.TestContainsInvalid\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"0\" />");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamsCollectionTest.TestContainsNull\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"0\" />");
        string.append("</results>");
        string.append("</test-suite>");
        string.append("<test-suite name=\"TeamTest\" executed=\"True\" success=\"True\" time=\"0.016\" asserts=\"0\">");
        string.append("<results>");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamTest.TestCompareToObject\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"0\" />");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamTest.TestCompareToSeed\" executed=\"True\" success=\"True\" time=\"0.016\" asserts=\"3\" />");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamTest.TestCompareToTeam\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"3\" />");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamTest.TestConstructName\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"3\" />");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamTest.TestConstructSeed\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"3\" />");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamTest.TestConstructSeedName\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"3\" />");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamTest.TestConstructSeedNameBye\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"3\" />");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamTest.TestProperties\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"6\" />");
        string.append("<test-case name=\"PlayoffLadderCreator.tests.TeamTest.TestToString\" executed=\"True\" success=\"True\" time=\"0.000\" asserts=\"2\" />");
        string.append("</results>");
        string.append("</test-suite>");
        string.append("</results>");
        string.append("</test-suite>");
        string.append("</results>");
        string.append("</test-suite>");
        string.append("</results>");
        string.append("</test-suite>");
        string.append("</test-results>");
        return string.toString();
    }
}
