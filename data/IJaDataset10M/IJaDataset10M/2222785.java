package org.chires.test.flow;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.chires.model.history.flow.FlowEntryStats;
import org.chires.model.history.flow.FlowStats;
import org.chires.test.TestUtility;
import javax.management.j2ee.statistics.Statistic;

/**
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), Sep 8, 2004
 * @version $Revision: 1.2 $($Author: ghinkl $ / $Date: 2004/11/25 04:55:58 $)
 */
public class FlowTest extends TestCase {

    private static TargetClass testInstance = new TargetClass();

    public FlowTest(String s) {
        super(s);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Tests");
        suite.addTest(new FlowTest("testIntroducedCall"));
        suite.addTest(new FlowTest("testBeanCreated"));
        return suite;
    }

    public void testIntroducedCall() throws InterruptedException {
        testInstance.firstMethod();
        inspectBean();
    }

    public void testBeanCreated() {
    }

    public void inspectBean() {
        FlowStats stats = (FlowStats) TestUtility.retrieveBeanValue("FlowModel", "stats");
        System.out.println("The stats appear to be: " + stats.toString());
        FlowEntryStats[] entries = stats.getFlowEntryStats();
        for (int i = 0; i < entries.length; i++) {
            FlowEntryStats entry = entries[i];
            printFlow(entry, 0);
        }
    }

    private void printFlow(FlowEntryStats entryStats, int depth) {
        System.out.println(space(depth) + "* " + entryStats.getClassName() + "::" + entryStats.getMethodName());
        Statistic[] stat = entryStats.getStatistics();
        for (int i = 0; i < stat.length; i++) {
            Statistic statistic = stat[i];
            System.out.println(space(depth + 2) + statistic);
        }
        FlowEntryStats[] childStacks = entryStats.getFlowEntryStats();
        if (childStacks != null) {
            for (int i = 0; i < childStacks.length; i++) {
                FlowEntryStats childStack = childStacks[i];
                printFlow(childStack, depth + 1);
            }
        }
    }

    private String space(int size) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < size; i++) {
            buf.append("\t");
        }
        return buf.toString();
    }
}
