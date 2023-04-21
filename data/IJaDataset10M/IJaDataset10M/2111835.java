package uk.ac.shef.wit.simmetrics;

import uk.ac.shef.wit.simmetrics.arbitrators.InterfaceMetricArbitrator;
import uk.ac.shef.wit.simmetrics.arbitrators.MeanMetricArbitrator;
import uk.ac.shef.wit.simmetrics.similaritymetrics.*;
import uk.ac.shef.wit.simmetrics.metrichandlers.MetricHandler;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 
 * Description: uk.ac.shef.wit.simmetrics.TestArbitrators implements a test
 * class for uk.ac.shef.wit.simmetrics.arbitrators. Date: 29-Apr-2004 Time:
 * 12:45:12
 * 
 * @author Sam Chapman <a href="http://www.dcs.shef.ac.uk/~sam/">Website</a>, <a
 *         href="mailto:sam@dcs.shef.ac.uk">Email</a>.
 * @version 1.1
 */
public final class TestArbitrators {

    /** string to perform tests with. */
    private static final String string1 = "Sam J Chapman";

    /** string to perform tests with. */
    private static final String string2 = "Samuel Chapman";

    /** string to perform tests with. */
    private static final String string3 = "S Chapman";

    /** string to perform tests with. */
    private static final String string4 = "Samuel John Chapman";

    /** string to perform tests with. */
    private static final String string5 = "John Smith";

    /** string to perform tests with. */
    private static final String string6 = "Richard Smith";

    /** string to perform tests with. */
    private static final String string7 = "aaaa mnop zzzz";

    /** string to perform tests with. */
    private static final String string8 = "bbbb mnop yyyy";

    /** string to perform tests with. */
    private static final String string9 = "aa mnop zzzzzz";

    /** string to perform tests with. */
    private static final String string10 = "a";

    /** string to perform tests with. */
    private static final String string11 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    /** string to perform tests with. */
    private static final String string12 = "aaaaa bcdefgh mmmmmmmm stuvwx zzzzzz";

    /** string to perform tests with. */
    private static final String string13 = "jjjjj bcdefgh qqqqqqqq stuvwx yyyyyy";

    /** string to perform tests with. */
    private static final String string14 = "aaaaa bcdefgh stuvwx zzzzzz";

    /** string to perform tests with. */
    private static final String string15 = "aaaaa aaaaa aaaaa zzzzzz";

    /** string to perform tests with. */
    private static final String string16 = "aaaaa aaaaa";

    /**
	 * test cases to perform.
	 */
    private static final String[][] testCases = { { string1, string2 }, { string1, string3 }, { string2, string3 }, { string1, string1 }, { string4, string5 }, { string5, string6 }, { string5, string1 }, { string1, string6 }, { string1, string4 }, { string2, string4 }, { string7, string8 }, { string7, string9 }, { string8, string9 }, { string10, string10 }, { string11, string11 }, { string10, string11 }, { string12, string13 }, { string12, string14 }, { string14, string15 }, { string16, string16 } };

    /**
	 * tests the metrics.
	 * 
	 * @param args
	 *            std arguments vector
	 */
    public static void main(final String[] args) {
        ArrayList<String> metricStrings = MetricHandler.GetMetricsAvailable();
        final ArrayList<InterfaceStringMetric> testMetricArrayList = new ArrayList<InterfaceStringMetric>();
        for (String metricString : metricStrings) {
            testMetricArrayList.add(MetricHandler.createMetric(metricString));
        }
        final InterfaceMetricArbitrator arbitrator = new MeanMetricArbitrator();
        arbitrator.setArbitrationMetrics(testMetricArrayList);
        testMethod(arbitrator);
    }

    /**
	 * perform test on given array of metrics.
	 * 
	 * @param arbitrator
	 *            the InterfaceMetricArbitrator to test
	 */
    private static void testMethod(final InterfaceMetricArbitrator arbitrator) {
        System.out.println("Performing Arbitrartion with: " + arbitrator.getShortDescriptionString());
        System.out.println("Using the Following Test Cases:");
        for (int i = 0; i < testCases.length; i++) {
            System.out.println("t" + (i + 1) + " \"" + testCases[i][0] + "\" vs \"" + testCases[i][1] + "\"");
        }
        System.out.println();
        final DecimalFormat df = new DecimalFormat("0.00");
        for (String[] testCase : testCases) {
            final float result = arbitrator.getArbitrationScore(testCase[0], testCase[1]);
            System.out.println(df.format(result) + " for \"" + testCase[0] + "\" vs \"" + testCase[1] + "\"");
        }
    }
}
