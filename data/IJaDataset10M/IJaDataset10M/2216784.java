package edu.smu.cse8377.netsim;

import java.util.*;
import edu.smu.cse8377.inf.files.FileReader;
import edu.smu.cse8377.inf.log.LogIt;
import edu.smu.cse8377.inf.thread.WorkUnit;
import edu.smu.cse8377.netsim.enums.SizeEnum;
import edu.smu.cse8377.netsim.enums.TimeEnum;

/**
 * This class encapsulates a Throttle Scenario, as defined in a single file
 * which will be applied to bytes being sent and received via the ThrottleSim
 * Module of the larger NetSim endpoint "B".  It will function in such a way
 * as to maintain an ever-changing value of how many bytes to transfer in a
 * given timeperiod.  If the time period total prescribed by the scenario
 * is less than the total execution time, then the scenario will cycle back
 * to the beginning and use those values over again, forever, until the system
 * is terminated. 
 *
 * @author mdtrl
 *
 */
public class ThrottleScenario extends WorkUnit {

    /**
     * ClassName for Logging
     */
    private static final String CLASSNAME = ThrottleScenario.class.getName();

    /**
     * FileReader to read in the scenario file
     */
    private FileReader throttleScenario;

    /**
     * Bytes read in from the scenario file
     */
    private byte[] allBytes;

    /**
     * All throttle values read in from the scenario file
     */
    private ArrayList<Long> sizeValues;

    /**
     * Which index of the sizeValues array to use for current throttle value
     */
    private int arrayIdx;

    /**
     * Time period to use to change throttle values
     */
    private TimeEnum time;

    /**
     * Size to use to measure throttle value)
     */
    private SizeEnum size;

    /**
     * Amount to multiply by throttle value 
     * to get the actual amount to throttle
     */
    private double sizeFraction;

    /**
     * Simulator reference
     */
    private ThrottleSim simulator;

    /**
     * Time in millis to wait until changing throttle value
     */
    private long waitTimeMillis;

    /**
     * Constructor for ThrottleScenario
     *
     * @param name String - task name
     * @param path String - path to throttle scenario file
     * @param sim ThrottleSim - simulator
     * @throws Exception if a problem occurs
     */
    public ThrottleScenario(String name, String path, ThrottleSim sim) throws Exception {
        super(name);
        String mN = CLASSNAME + ThrottleScenario.class.getSimpleName() + "() -> ";
        sizeValues = new ArrayList<Long>();
        LogIt.getInstance().logNormal(mN + "Initializing throttle scenario file...");
        throttleScenario = new FileReader(path);
        allBytes = throttleScenario.readAllBytes();
        throttleScenario.close();
        parseBytes();
        simulator = sim;
    }

    /**
     * Parse the throttle scenario
     *
     * @throws Exception if a problem occurs
     */
    private void parseBytes() throws Exception {
        final String mN = CLASSNAME + ".parseBytes() -> ";
        LogIt.getInstance().logNormal(mN + "Parsing Throttle Scenario...");
        String allLines = new String(allBytes);
        StringTokenizer chopper = new StringTokenizer(allLines, "\n");
        LogIt.getInstance().logDetail(mN + "Determining Time Enum to use...");
        String timeEnum = chopper.nextToken();
        time = TimeEnum.valueOf(timeEnum.toUpperCase());
        LogIt.getInstance().logDetail(mN + "TimeEnum is '" + time + "'");
        if (!chopper.hasMoreTokens()) {
            throw new Exception("Failed to parse ThrottleScenario file - " + "Failed to parse sizeEnum value from 2nd line of file!");
        }
        LogIt.getInstance().logDetail(mN + "Determining Size Enum to use...");
        String sizeEnum = chopper.nextToken();
        size = SizeEnum.valueOf(sizeEnum.toUpperCase());
        LogIt.getInstance().logDetail(mN + "SizeEnum is '" + size + "'");
        if (!chopper.hasMoreTokens()) {
            throw new Exception("Failed to parse ThrottleScenario file - " + "No throttling information found on lines past timeEnum " + "and sizeEnum values!");
        }
        LogIt.getInstance().logDetail(mN + "Entering line-by-line " + "parsing loop...");
        while (chopper.hasMoreTokens()) {
            String line = chopper.nextToken();
            try {
                long sizeNum = Long.parseLong(line);
                sizeValues.add(sizeNum);
            } catch (NumberFormatException nfe) {
                LogIt.getInstance().logWarn(mN + "Line '" + line + "' " + "FAILED conversion to a primitive Long!");
                LogIt.getInstance().logExceptionAsWarn(nfe);
            }
        }
        LogIt.getInstance().logDetail(mN + "Done parsing line-by-line.  " + "Checking to ensure non-empty sizeValues list...");
        if (sizeValues.isEmpty()) {
            throw new Exception("Failed to parse ThrottleScenario file - " + "No Size Values were parsed; This probably indicates " + "that the values had NumberFormatExceptions during " + "parsing!");
        }
        LogIt.getInstance().logNormal(mN + "Done parsing Throttle Scenario.");
    }

    /**
     * Getter for waitTimeMillis
     *
     * @return long
     */
    public long getWaitTimeMillis() {
        return waitTimeMillis;
    }

    /** 
     * MANDATORY OVERRIDE:
     * Entry point to execution logic for ThrottleScenario thread.
     *
     * @throws Exception
     * @see edu.smu.cse8377.inf.thread.WorkUnit#doWork()
     */
    @Override
    public void doWork() throws Exception {
        final String mN = CLASSNAME + ".doWork() -> ";
        LogIt.getInstance().logNormal(mN + "Execution is beginning.  " + "Determining wait-value from TimeEnum...");
        waitTimeMillis = 0;
        switch(time) {
            case MINUTES:
                waitTimeMillis = 1000 * 60;
                break;
            case SECONDS:
                waitTimeMillis = 1000;
                break;
            case DECISECONDS:
                waitTimeMillis = 100;
                break;
            case CENTISECONDS:
                waitTimeMillis = 10;
                break;
            case MILLISECONDS:
                waitTimeMillis = 1;
                break;
            default:
                throw new Exception("Unsupported TimeEnum value '" + time + "'!");
        }
        LogIt.getInstance().logNormal(mN + "WaitTimeMillis=" + waitTimeMillis);
        LogIt.getInstance().logNormal(mN + "Determining sizeFraction from " + "SizeEnum...");
        sizeFraction = 0;
        switch(size) {
            case BITS:
                sizeFraction = (1 / 8);
                break;
            case BYTES:
                sizeFraction = 1;
                break;
            case KILOBYTES:
                sizeFraction = 1024;
                break;
            case MEGABYTES:
                sizeFraction = 1024 * 1024;
                break;
            case GIGABYTES:
                sizeFraction = 1024 * 1024 * 1024;
                break;
            default:
                throw new Exception("Unsupported SizeEnum value '" + size + "'!");
        }
        LogIt.getInstance().logNormal(mN + "SizeFraction=" + sizeFraction);
        LogIt.getInstance().logNormal(mN + "Beginning Throttling Loop...");
        while (true) {
            long value = sizeValues.get(arrayIdx);
            long scaledValue = (long) Math.ceil(sizeFraction * value);
            LogIt.getInstance().logNormal(mN + "Value=" + value + "; Scaled=" + scaledValue + "; Pushing scaled val to ThrottleSim module now...");
            simulator.setBytesToTransfer(scaledValue);
            int nextIdx = (++arrayIdx) % sizeValues.size();
            LogIt.getInstance().logDetail(mN + "This Idx=" + arrayIdx + "; Next Idx=" + nextIdx);
            arrayIdx = nextIdx;
            LogIt.getInstance().logDetail(mN + "Sleeping for " + waitTimeMillis + " ms...");
            Object o = new Object();
            synchronized (o) {
                try {
                    o.wait(waitTimeMillis);
                } catch (Throwable t) {
                    LogIt.getInstance().logWarn(mN + "Exception while sleeping!");
                    LogIt.getInstance().logExceptionAsWarn(t);
                }
            }
        }
    }

    /** 
     * MANDATORY OVERRIDE:
     * Cleanup logic when ThrottleScenario thread is terminating.
     *
     * @see edu.smu.cse8377.inf.thread.WorkUnit#doCleanup()
     */
    @Override
    public void doCleanup() {
        final String mN = CLASSNAME + ".doCleanup() -> ";
        LogIt.logNormalCleanup(mN + "No cleanup to do.  Done!");
    }
}
