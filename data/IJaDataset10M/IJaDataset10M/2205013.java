package org.custommonkey.xmlunit;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Test a DetailedDiff. Extend the test case class for Diff so we can rerun those
 * tests with a DetailedDiff and assert that behaviour has not changed.
 */
public class test_DetailedDiff extends test_Diff {

    private String firstForecast, secondForecast;

    public void testAllDifferencesFirstForecastControl() throws Exception {
        Diff multipleDifferences = new Diff(firstForecast, secondForecast);
        DetailedDiff detailedDiff = new DetailedDiff(multipleDifferences);
        List differences = detailedDiff.getAllDifferences();
        assertExpectedDifferencesFirstForecastControl(differences, detailedDiff);
    }

    private void assertExpectedDifferencesFirstForecastControl(List differences, DetailedDiff detailedDiff) {
        assertEquals("size: " + detailedDiff, 5, differences.size());
        assertEquals("first: " + detailedDiff, DifferenceConstants.ELEMENT_NUM_ATTRIBUTES, differences.get(0));
        assertEquals("second: " + detailedDiff, DifferenceConstants.ATTR_NAME_NOT_FOUND, differences.get(1));
        assertEquals("third: " + detailedDiff, DifferenceConstants.ATTR_VALUE, differences.get(2));
        assertEquals("fourth: " + detailedDiff, DifferenceConstants.ATTR_SEQUENCE, differences.get(3));
        assertEquals("fifth: " + detailedDiff, DifferenceConstants.HAS_CHILD_NODES, differences.get(4));
    }

    public void testAllDifferencesSecondForecastControl() throws Exception {
        Diff multipleDifferences = new Diff(secondForecast, firstForecast);
        DetailedDiff detailedDiff = new DetailedDiff(multipleDifferences);
        List differences = detailedDiff.getAllDifferences();
        assertEquals("size: " + detailedDiff, 4, differences.size());
        assertEquals("first: " + detailedDiff, DifferenceConstants.ELEMENT_NUM_ATTRIBUTES, differences.get(0));
        assertEquals("second: " + detailedDiff, DifferenceConstants.ATTR_VALUE, differences.get(1));
        assertEquals("third: " + detailedDiff, DifferenceConstants.ATTR_SEQUENCE, differences.get(2));
        assertEquals("fourth: " + detailedDiff, DifferenceConstants.HAS_CHILD_NODES, differences.get(3));
    }

    public void testPrototypeIsADetailedDiff() throws Exception {
        Diff multipleDifferences = new Diff(firstForecast, secondForecast);
        DetailedDiff detailedDiff = new DetailedDiff(new DetailedDiff(multipleDifferences));
        List differences = detailedDiff.getAllDifferences();
        assertExpectedDifferencesFirstForecastControl(differences, detailedDiff);
    }

    public void testLargeFiles() throws Exception {
        int i = 0;
        String expr = null;
        File test, control;
        control = new File(test_Constants.BASEDIR + "/tests/etc/controlDetail.xml");
        test = new File(test_Constants.BASEDIR + "/tests/etc/testDetail.xml");
        DetailedDiff differencesWithWhitespace = new DetailedDiff(new Diff(new InputSource(new FileReader(control)), new InputSource(new FileReader(test))));
        assertEquals(1402, differencesWithWhitespace.getAllDifferences().size());
        try {
            XMLUnit.setIgnoreWhitespace(true);
            Diff prototype = new Diff(new FileReader(control), new FileReader(test));
            DetailedDiff detailedDiff = new DetailedDiff(prototype);
            List differences = detailedDiff.getAllDifferences();
            assertEquals(40, differences.size());
            SimpleXpathEngine xpathEngine = new SimpleXpathEngine();
            Document controlDoc = XMLUnit.buildControlDocument(new InputSource(new FileReader(control)));
            Document testDoc = XMLUnit.buildTestDocument(new InputSource(new FileReader(test)));
            Difference aDifference;
            String value;
            for (Iterator iter = differences.iterator(); iter.hasNext(); ) {
                aDifference = (Difference) iter.next();
                if (aDifference.equals(DifferenceConstants.ATTR_VALUE) || aDifference.equals(DifferenceConstants.CDATA_VALUE) || aDifference.equals(DifferenceConstants.COMMENT_VALUE) || aDifference.equals(DifferenceConstants.ELEMENT_TAG_NAME) || aDifference.equals(DifferenceConstants.TEXT_VALUE)) {
                    expr = aDifference.getControlNodeDetail().getXpathLocation();
                    if (expr == null || expr.length() == 0) {
                        System.out.println(aDifference);
                    } else {
                        value = xpathEngine.evaluate(expr, controlDoc);
                        assertEquals(i + " control " + aDifference.toString(), value, aDifference.getControlNodeDetail().getValue());
                    }
                    expr = aDifference.getTestNodeDetail().getXpathLocation();
                    if (expr == null || expr.length() == 0) {
                        System.out.println(aDifference);
                    } else {
                        value = xpathEngine.evaluate(expr, testDoc);
                        assertEquals(i + " test " + aDifference.toString(), value, aDifference.getTestNodeDetail().getValue());
                    }
                }
                ++i;
            }
        } catch (Exception e) {
            System.out.println("eek@" + i + ":" + expr);
            throw e;
        } finally {
            XMLUnit.setIgnoreWhitespace(false);
        }
    }

    protected Diff buildDiff(Document control, Document test) {
        return new DetailedDiff(super.buildDiff(control, test));
    }

    protected Diff buildDiff(String control, String test) throws Exception {
        return new DetailedDiff(super.buildDiff(control, test));
    }

    protected Diff buildDiff(Reader control, Reader test) throws Exception {
        return new DetailedDiff(super.buildDiff(control, test));
    }

    public test_DetailedDiff(String name) {
        super(name);
        firstForecast = "<weather><today icon=\"clouds\" temp=\"17\">" + "<outlook>unsettled</outlook></today></weather>";
        secondForecast = "<weather><today temp=\"20\"/></weather>";
    }
}
