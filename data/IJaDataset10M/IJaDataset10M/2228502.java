package com.google.test.jinjector.coverage;

import com.google.test.jinjector.util.FileConnectionUtil;
import com.google.test.jinjector.util.Log;
import java.io.*;
import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

/**
 * Collects information about coverage.
 *
 * <p> This class can be used for method coverage and/or line coverage. The
 * following code would initialize method and line coverage.
 * <pre>
 *   CoverageManager.initMethodCoverage(...);
 *   CoverageManager.initLineCoverage(...);
 *   CoverageManager.enableCoverage();
 * </pre>
 *
 * The code to enableCoverage() is mandatory, look the
 * CoverageManager#coverageEnabled for more information.
 * 
 * @author Michele Sama
 *
 */
public class CoverageManager {

    public static final String FILENAME_COVERAGE = "coverage.txt";

    /**
   * Static reference to the singleton instance.
   */
    private static CoverageManager instance = new CoverageManager();

    /**
   * Information about the methods to get coverage for.
   */
    private MethodInfoContainer methodInfoContainer;

    /**
   * Used for method coverage.
   *
   * <p>Each method is represented by a bit. Mapping from method names to 
   * an index is loaded from a file.
   */
    private Bitfield coveredMethod;

    /**
   * Stores the total execution time of each method. The duration is recorded 
   * in milliseconds.
   */
    private long[] methodExecutionTotalTime;

    /**
   * Stores number of times of method is covered.
   * 
   * <p>To minimize the memory footprint an <code>int</code> has been used 
   * instead of a <code>long</code>. Since the number of times a method has been 
   * executed can only be 0 or a positive integer we use this field as unsigned.
   * Each element of the array is initialized with {@link Integer#MIN_VALUE}
   * and increased up to {@link Integer#MAX_VALUE}. 
   * 
   * @see #initMethodProfiling(String)
   * @see #setMethodProfilingFields(MethodInfoContainer, String)
   * @see #incrementMethodCallCountImplementation(int)
   */
    private int[] numberOfCallsPerMethod;

    /**
   * Used for line coverage. Stores line coverage status for each method.
   *
   * <p>One bitfield is created for each method. The method index is loaded 
   * from a file. The line indexes are relative to the line number of the 
   * method definition.
   */
    private Bitfield[] coveredLines;

    /**
   * Used for line coverage.
   */
    private CoverageDataFile coverageDataFile = new CoverageDataFile(null, null);

    /**
   * Represents an internal state in which the coverage flagging is not
   * active. This can happen before an invocation of {@link #enableCoverage()}
   * or after both {@link #disableCoverage()} and {@link #writeReport(String)}
   * have been invoked sucessfully.
   * 
   * @see #coverageCollectionState
   */
    private static final int STATE_NO_COVERAGE = 0;

    /**
   * Represents an internal state in which the coverage flagging is active. 
   * This can happen after an invocation of {@link #enableCoverage()}
   * and before an invocation of {@link #disableCoverage()}.
   * 
   * @see #coverageCollectionState
   */
    private static final int STATE_COVERAGE_FLAGGING_ENABLED = 1;

    /**
   * Represents an internal state in which the coverage is collectable. 
   * This happens between invocation of {@link #disableCoverage()} and 
   * {@link #writeReport(String)}.
   * 
   * @see #coverageCollectionState
   */
    private static final int STATE_COVERAGE_COLLECTABLE = 2;

    /**
   * Tells the state of coverage. Initially the state will be 
   * {@link #STATE_NO_COVERAGE}, then it will change in
   * {@link #STATE_COVERAGE_FLAGGING_ENABLED}, 
   * {@link #STATE_COVERAGE_COLLECTABLE} and
   * {@link #STATE_NO_COVERAGE} again.
   * 
   * <p>For automated testing purposes the coverage is collected directly from 
   * the code, while in a normal execution the MIDlet is instrumented in order 
   * to collect coverage at the end of its execution. The internal state 
   * prevents to collect the coverage twice or to collect coverage when it was
   * not started.
   * 
   * <p>Coverage should be disabled during the initialization of the 
   * CoverageManager. If during the initialization, an instance instrumented 
   * for coverage is used, it is possible to start an uncontrolled recursion 
   * between the initialization and the instrumented instance. To avoid this no
   * coverage is collected during the initialization.
   */
    private static int coverageCollectionState = STATE_NO_COVERAGE;

    /**
   * This method should be called by the instrumentation code. This method
   * should only be called once.
   *
   * <p>Not thread-safe.
   *
   * @param runId unique number used to identify the coverage run, this is used
   *   to construct the names of the input files which contains this id to make
   *   them unique (required to identify multiple executions).
   * @param lCovOutputFile output file
   */
    public static void initLineCoverage(String runId, String lCovOutputFile) {
        String filename = "/" + CoverageLcovWriter.FILENAME_INSTRUMENTED_LINES + runId;
        Bitfield[] bitfield = Bitfield.getBitfieldsForLineCoverage(filename);
        CoverageDataFile dataFile = new CoverageDataFile(runId, lCovOutputFile);
        instance.setLineCoverageFields(bitfield, dataFile);
    }

    /**
   * Initializes the fields needed for the line coverage.
   *
   * <p>This method is defined as packaged for testing purposes. It should not 
   * be invoked by other classes.
   * 
   * @param coveredLines an array of bitfields to collect line coverage.
   * @param coverageDataFile the data file containing information on the 
   *     output file
   */
    void setLineCoverageFields(Bitfield[] coveredLines, CoverageDataFile coverageDataFile) {
        this.coveredLines = coveredLines;
        this.coverageDataFile = coverageDataFile;
    }

    /**
   * Enables line coverage. If disabled, calls to CoverageManager#setCovered
   * and CoverageManager#setLineCovered will have no effect.
   */
    public static void enableCoverage() {
        coverageCollectionState = STATE_COVERAGE_FLAGGING_ENABLED;
    }

    /**
   * Disables the coverage.
   * 
   * <p>This method is visible and exists for testing purposes only. Clients 
   * should invoke {@link #writeReport(String)} to stop the coverage. However 
   * placing control over the state here and not directly in 
   * {@link #writeReport(String)} improves the testability of this class and of
   * the writer classes by making it possible for a test to start and stop
   * coverage.
   */
    static void disableCoverage() {
        if (coverageCollectionState == STATE_COVERAGE_FLAGGING_ENABLED) {
            coverageCollectionState = STATE_COVERAGE_COLLECTABLE;
        } else {
            throw new IllegalStateException("The coverage manager should have been" + " in state 'flagging'=" + STATE_COVERAGE_FLAGGING_ENABLED + " to be disabled, it was in state " + coverageCollectionState + " instead.");
        }
    }

    /**
   * This method should be called by the instrumentation code. This method
   * should only be called once.
   *
   * <p> Not thread-safe.
   *
   * @param runId unique number used to identify the coverage run, this is used
   * to construct the names of the input files which contains this id to make
   * them unique.
   */
    public static void initMethodCoverage(String runId) {
        MethodInfoContainer methods = new MethodInfoContainer();
        methods.loadFile(runId);
        instance.setMethodCoverageFields(methods, runId);
    }

    /**
   * This method is similar to #initMethodCoverage(String) but it initializes a
   * larger data structure to keep track of the number of method calls.
   *
   * @see #initMethodCoverage(String)
   */
    public static void initMethodProfiling(String runId) {
        MethodInfoContainer methods = new MethodInfoContainer();
        methods.loadFile(runId);
        instance.setMethodProfilingFields(methods, runId);
    }

    /**
   * Initializes the fields needed for the method coverage.
   *
   * <p> Visible for testing.
   */
    void setMethodCoverageFields(MethodInfoContainer methods, String runId) {
        methodInfoContainer = methods;
        coverageDataFile.setRunId(runId);
        coveredMethod = new Bitfield(methods.size());
    }

    /**
   * Initializes the fields needed for the method profiling.
   *
   * <p> Visible for testing.
   * 
   * @see #numberOfCallsPerMethod
   */
    void setMethodProfilingFields(MethodInfoContainer methods, String runId) {
        methodInfoContainer = methods;
        coverageDataFile.setRunId(runId);
        numberOfCallsPerMethod = new int[methods.size()];
        for (int i = 0; i < methods.size(); i++) {
            numberOfCallsPerMethod[i] = Integer.MIN_VALUE;
        }
    }

    /**
   * Initializes the fields needed for the method's execution time profiling.
   *
   * <p> Visible for testing.
   */
    void setMethodExecutionTimeFields(MethodInfoContainer methods, String runId) {
        methodInfoContainer = methods;
        coverageDataFile.setRunId(runId);
        methodExecutionTotalTime = new long[methods.size()];
    }

    /**
   * Replace the default "instrumented" instance with a new one.
   * This method is for test purposes only and allows tests to set an instance 
   * with a fixed size.
   *
   * <p> Visible for testing.
   *
   * @param manager The manager to set as a default one.
   */
    static void setInstance(CoverageManager manager) {
        instance = manager;
        coverageCollectionState = STATE_NO_COVERAGE;
    }

    /**
   * This is the most important method for the method coverage.
   * At instrumentation time at the beginning of each method this function is 
   * called with the the index of the executed method.
   *  
   * <p>The index is used to identify a bit in the bitfield and to flag it as 
   * covered. 
   * 
   * @param index The index to be set.
   */
    public static void setCovered(int index) {
        if (coverageCollectionState == STATE_COVERAGE_FLAGGING_ENABLED) {
            instance.setCoveredImplementation(index);
        }
    }

    /**
   * Increases the flag of the method at the specified index.
   *
   * <p> Method call count stored cannot be greater than Integer.MAX_VALUE.
   * 
   * @param index The index of the method to be set.
   */
    public static void incrementMethodCallCount(int index) {
        if (coverageCollectionState == STATE_COVERAGE_FLAGGING_ENABLED) {
            instance.incrementMethodCallCountImplementation(index);
        }
    }

    /**
   * @see #incrementMethodCallCount(int)
   */
    protected void incrementMethodCallCountImplementation(int index) {
        synchronized (numberOfCallsPerMethod) {
            if (numberOfCallsPerMethod[index] < Integer.MAX_VALUE) {
                numberOfCallsPerMethod[index]++;
            } else {
            }
        }
    }

    /**
   * Increases the execution time of the method at the specified index.
   * 
   * @param index The index of the method to be set.
   * @param time the execution time.
   */
    protected static void incrementMethodExecutionTime(int index, long time) {
        if (coverageCollectionState == STATE_COVERAGE_FLAGGING_ENABLED) {
            instance.incrementMethodTotalTimeImplementation(index, time);
        }
    }

    /**
   * @see #incrementMethodExecutionTime(int, long)
   */
    protected void incrementMethodTotalTimeImplementation(int index, long time) {
        synchronized (methodExecutionTotalTime) {
            if (methodExecutionTotalTime[index] < Integer.MAX_VALUE) {
                methodExecutionTotalTime[index] += time;
            } else {
            }
        }
    }

    /**
   * Flags the method identified by a specific index as covered.
   *
   * @param index The index to be set.
   */
    protected void setCoveredImplementation(int index) {
        synchronized (coveredMethod) {
            coveredMethod.set(index);
        }
    }

    /**
   * Marks a line of a specific method as covered.
   *
   * @param classIndex the index of the method containing the line to be 
   *    covered.
   * @param lineIndex the index of the line to flag as covered.
   */
    public static void setLineCovered(int classIndex, int lineIndex) {
        if (coverageCollectionState == STATE_COVERAGE_FLAGGING_ENABLED) {
            Bitfield field = instance.coveredLines[classIndex];
            synchronized (field) {
                field.set(lineIndex);
            }
        }
    }

    /**
   * Tells if a specific line of a specific method has been covered.
   *
   * @param classIndex the index of the method containing the line to be 
   *    covered.
   * @param lineIndex the index of the line to flag as covered.
   * @return <code>true</code> if that line has been covered.
   */
    public boolean isLineCovered(int classIndex, int lineIndex) {
        if (coveredLines == null) {
            throw new IllegalStateException("GeneralFailure!" + " The Bitfield array is null");
        }
        Bitfield field = coveredLines[classIndex];
        synchronized (field) {
            return field.get(lineIndex);
        }
    }

    /**
   * Returns a writable root folder where the coverage results can be stored.
   * 
   * <p>The actual path will be written to the log. It may depend on the device
   * and whether it has a memory card installed.
   *
   * TODO: This may need tweaking for various physical devices. The method
   *      has been developed to cope with Nokia devices which have restricted
   *      access to some folders or roots e.g. C:\ cannot be written to.
   */
    protected static String getWriteableRoot() {
        String prefix = "file://localhost/";
        Enumeration rootEnum = FileSystemRegistry.listRoots();
        while (rootEnum.hasMoreElements()) {
            String root = (String) rootEnum.nextElement();
            if (root.equalsIgnoreCase("C:/") || root.indexOf("memory") != -1) {
                Log.log(CoverageManager.class.getName(), "Skipping Root: " + root);
                continue;
            } else {
                Log.log(CoverageManager.class.getName(), "Trying Root: " + root);
                String fullPath = prefix + root;
                try {
                    FileConnection fc = (FileConnection) Connector.open(fullPath + "dummy.txt", Connector.READ_WRITE);
                    if (!fc.exists()) {
                        fc.create();
                    }
                    fc.close();
                    Log.log(CoverageManager.class.getName(), "Using: " + fullPath);
                    return fullPath;
                } catch (IOException ioe) {
                    Log.log(CoverageManager.class.getName(), "IOException caught when trying to test if we can write to: " + fullPath);
                }
            }
        }
        String photos = "fileconn.dir.photos";
        String path = System.getProperty(photos);
        if (path != null) {
            return path;
        }
        throw new IllegalStateException("No writable folder found for coverage data.");
    }

    /**
   * Writes the report for this instance.
   * 
   * TODO which will be able to stream
   *     coverage information over the network and which will improve the
   *     testability of this class.
   * 
   * @param path the path to write the report to
   * @throws IllegalStateException if coverage report is 
   *    requested externally and coverage has not been collected.
   */
    public static void writeReport(String path) {
        path = getWriteableRoot();
        if (coverageCollectionState == STATE_NO_COVERAGE) {
            Log.log(CoverageManager.class.getName(), "Coverage was already collected or coverage was not enabled. " + "If no data has been created please check you instrumentation " + "flags. If you are running end2end tests maybe the test suite is " + "collecting coverage before the application exit.");
            return;
        }
        disableCoverage();
        if (instance.coveredMethod != null) {
            CoverageWriter writer = new CoverageSummaryWriter(instance, instance.methodInfoContainer, instance.coverageDataFile.getRunId());
            try {
                writer.writeFullReport(path);
            } catch (IOException ex) {
                throw new RuntimeException("There was an exception while saving " + "lcov method coverage on file " + path + instance.coverageDataFile.lCovOutputFile + ". The exception was: " + ex.getMessage());
            }
        }
        if (instance.coveredLines != null) {
            CoverageWriter writer = new CoverageLcovWriter(instance, instance.coverageDataFile);
            try {
                writer.writeFullReport(path);
            } catch (IOException ex) {
                throw new RuntimeException("There was an exception while saving " + "lcov line coverage on file " + path + instance.coverageDataFile.lCovOutputFile + ". The exception was: " + ex.getMessage());
            }
        }
        coverageCollectionState = STATE_NO_COVERAGE;
    }

    /**
   * Tells if the method at the specified index has been covered.
   *
   * @param index The method's index.
   * @return <code>true</code> if this method has been covered.
   * 
   */
    public boolean isCovered(int index) {
        if (coveredLines == null) {
            throw new IllegalStateException("GeneralFailure!" + " The Bitfield array is null");
        }
        synchronized (coveredMethod) {
            return coveredMethod.get(index);
        }
    }

    /**
   * Returns the number of calls for the given method index.
   *
   * @param index The method's index.
   * @see #numberOfCallsPerMethod
   */
    public int getNumberOfMethodCalls(int index) {
        synchronized (numberOfCallsPerMethod) {
            return numberOfCallsPerMethod[index] - Integer.MIN_VALUE;
        }
    }

    /**
   * Returns the execution time for the given method index.
   *
   * @param index The method's index.
   */
    public long getTotalMethodExecutionTime(int index) {
        synchronized (methodExecutionTotalTime) {
            return methodExecutionTotalTime[index];
        }
    }

    /**
   * Stores information about input/output data files needed by the coverage
   * tools.
   * 
   * TODO: remove this class as it is just a container for two variables. 
   */
    public static class CoverageDataFile {

        private String lCovOutputFile;

        private String runId;

        CoverageDataFile(String runId, String lCovOutputFile) {
            this.runId = runId;
            this.lCovOutputFile = lCovOutputFile;
        }

        public String getRunId() {
            return runId;
        }

        public String getLCovOutputFile() {
            return lCovOutputFile;
        }

        public void setRunId(String value) {
            runId = value;
        }

        public void setLCovOutputFile(String value) {
            lCovOutputFile = value;
        }
    }
}
