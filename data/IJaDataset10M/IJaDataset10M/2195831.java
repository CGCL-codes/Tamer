package org.hansel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import junit.framework.AssertionFailedError;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.hansel.probes.ACmpBranchProbe;
import org.hansel.probes.BinaryBranchProbe;
import org.hansel.probes.MethodProbe;
import org.hansel.probes.NullCmpBranchProbe;
import org.hansel.probes.ProbeFilter;
import org.hansel.probes.SelectProbe;
import org.hansel.probes.UnaryBranchProbe;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

/**
 * This class contains a table of currently active Probes.
 * It implements the singleton pattern.
 *
 * @author Niklas Mehner
 */
public class ProbeTable extends TestSuite {

    /** Singleton instance of the ProbeTable. */
    private static ProbeTable table;

    /** List containing currently active probes. */
    private List<Probe> probes;

    private Map<ProbeData, Probe> probeMap;

    private boolean displayStatistics;

    private ProbeFilter probeFilter;

    public ProbeTable(ProbeFilter probeFilter) {
        super("Coverage Test");
        this.probes = new Vector<Probe>();
        this.probeMap = new HashMap<ProbeData, Probe>();
        this.displayStatistics = false;
        this.probeFilter = probeFilter;
    }

    public void setDisplayStatistics(boolean display) {
        this.displayStatistics = display;
    }

    public int addProbe(Probe probe) {
        if (probeMap.containsKey(probe.getProbeData())) {
            throw new IllegalStateException("Duplicate probe.");
        }
        probes.add(probe);
        probeMap.put(probe.getProbeData(), probe);
        if ((probeFilter == null) || !probeFilter.filter(probe.getProbeData())) {
            addTest(probe);
        }
        return probes.size() - 1;
    }

    public Probe getCached(ProbeData pd) {
        return (Probe) probeMap.get(pd);
    }

    public void hit(int index) {
        ((MethodProbe) getProbe(index)).hit();
    }

    /**
     * This is the method inserted by the instrumentation, to
     * record execution of a given probe.
     * @param index Index of the probe.
     */
    public static void hitMethod(int index) {
        getProbeTable().hit(index);
    }

    public void hitB(int condition, int index) {
        ((UnaryBranchProbe) getProbe(index)).hit(condition);
    }

    public static void hitBranch(int condition, int index) {
        getProbeTable().hitB(condition, index);
    }

    public void hit(int n, int m, int index) {
        ((BinaryBranchProbe) getProbe(index)).hit(n, m);
    }

    public static void hitBranch(int n, int m, int index) {
        getProbeTable().hit(n, m, index);
    }

    public void hit(Object obj1, Object obj2, int index) {
        ((ACmpBranchProbe) getProbe(index)).hit(obj1, obj2);
    }

    public static void hitBranch(Object obj1, Object obj2, int index) {
        getProbeTable().hit(obj1, obj2, index);
    }

    public void hit(Object obj, int index) {
        ((NullCmpBranchProbe) getProbe(index)).hit(obj);
    }

    public static void hitBranch(Object obj, int index) {
        getProbeTable().hit(obj, index);
    }

    public void hit(int value, int index) {
        ((SelectProbe) getProbe(index)).hit(value);
    }

    public static void hitSelect(int value, int index) {
        getProbeTable().hit(value, index);
    }

    /**
     * Returns the index for the next probe. The index of a probe has to be
     * unique, because it is used to identify the probe, when a call to
     * ProbeTable.hit(index) is made by the instrumented code.
     * @return Index for the next probe.
     */
    public int getProbeIndex() {
        return probes.size();
    }

    /**
     * Return the probe for a given index.
     * @param index Index of the probe.
     * @return Probe.
     */
    private Probe getProbe(int index) {
        return (Probe) probes.get(index);
    }

    public void run(TestResult result) {
        if ((result.errorCount() == 0) && (result.failureCount() == 0)) {
            super.run(result);
            int covered = getCovered();
            int count = getSize();
            if (displayStatistics && (covered != count)) {
                String msg = "Coverage Test failed: Only " + covered + " of " + count + " probes covered.";
                result.addFailure(this, new AssertionFailedError(msg));
            }
        } else {
            result.startTest(this);
            String msg = "Coverage Failure: No coverage test performed, " + "because test failed.";
            result.addFailure(this, new AssertionFailedError(msg));
            result.endTest(this);
        }
        clear();
    }

    public void addProbeDescriptions(Description coverageDesc) throws ClassNotFoundException {
        for (Probe probe : probes) {
            coverageDesc.addChild(probe.getDescription());
        }
    }

    public void run(RunNotifier result, Description coverageDescription, boolean hasErrors) throws ClassNotFoundException {
        if (!hasErrors) {
            int covered = getCovered();
            int count = getSize();
            for (Probe probe : probes) {
                probe.run(result, coverageDescription);
            }
            if (displayStatistics && (covered != count)) {
                String msg = "Coverage Test failed: Only " + covered + " of " + count + " probes covered.";
                result.fireTestFailure(new Failure(coverageDescription, new AssertionFailedError(msg)));
            }
        } else {
            String msg = "Coverage Failure: No coverage test performed, " + "because test failed.";
            result.fireTestFailure(new Failure(coverageDescription, new AssertionFailedError(msg)));
        }
        clear();
    }

    public static void setProbeTable(ProbeTable pt) {
        table = pt;
    }

    /**
     * Returns the singleton instance of the ProbeTable.
     * @return ProbeTable.
     */
    public static ProbeTable getProbeTable() {
        return table;
    }

    public void clear() {
        probes.clear();
    }

    public int getSize() {
        return probes.size();
    }

    public int getCovered() {
        int covered = 0;
        for (int i = 0; i < probes.size(); i++) {
            Probe probe = (Probe) probes.get(i);
            if (!probe.coverageFailure()) {
                covered++;
            }
        }
        return covered;
    }
}
