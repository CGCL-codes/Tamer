package com.continuent.tungsten.replicator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ThreadInfo;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.management.remote.JMXConnector;
import com.continuent.tungsten.commons.config.TungstenProperties;
import com.continuent.tungsten.commons.exec.ArgvIterator;
import com.continuent.tungsten.commons.jmx.JmxManager;
import com.continuent.tungsten.commons.jmx.ServerRuntimeException;
import com.continuent.tungsten.replicator.conf.MonitorThreadBucket;
import com.continuent.tungsten.replicator.conf.MonitorThreadInfo;
import com.continuent.tungsten.replicator.conf.ReplicatorConf;
import com.continuent.tungsten.replicator.conf.ReplicatorMonitor;
import com.continuent.tungsten.replicator.conf.ReplicatorMonitorMBean;
import com.continuent.tungsten.replicator.consistency.ConsistencyTable;

/**
 * This class defines a ReplicatorManagerCtrl that implements a simple utility
 * to access ReplicatorManager JMX interface. See the printHelp() command for a
 * description of current commands.
 * 
 * @author <a href="mailto:teemu.ollakka@continuent.com">Teemu Ollakka</a>
 * @version 1.0
 */
public class ReplicatorManagerCtrl {

    static InputStreamReader converter = new InputStreamReader(System.in);

    static BufferedReader stdin = new BufferedReader(converter);

    private boolean expectLostConnection = false;

    private ArgvIterator argvIterator;

    ReplicatorManagerCtrl(String[] argv) {
        argvIterator = new ArgvIterator(argv);
    }

    static void printHelp() {
        println("Replicator Manager Control Utility");
        println("Syntax:  [java " + ReplicatorManagerCtrl.class.getName() + " \\");
        println("             [global-options] command [command-options]");
        println("Global Options:");
        println("\t-host name       - Host name of replicator [default: localhost]");
        println("\t-port number     - Port number of replicator [default: 10000]");
        println("Commands:");
        println("\tbackup [-backup agent] [-storage agent] [-limit s]  - Backup database");
        println("\tclear            - Clear one or all dynamic variables");
        println("\tconfigure [file] - Reload replicator properties file");
        println("\tcpu              - Thread based CPU statistics");
        println("\tcpuDetail [-divisor d] [-help]  - Dump detailed cpu statistics");
        println("\tcsv              - Dump statistics in CSV file format");
        println("\tdiag             - Dump diagnostic information into log");
        println("\tflush [-limit s] - Synchronize transaction history log to database");
        println("\theartbeat        - Insert a heartbeat event");
        println("\tkill [-y]        - Kill the replicator process immediately");
        println("\toffline          - Set replicator offline (to OFFLINE)");
        println("\tonline           - Set Replicator online (to SYNCHRONIZING/ONLINE)");
        println("\trealDetail [-divisor d] [-help] - Dump detailed real time statistics");
        println("\tresetMonitors    - Clear monitor counters and reset time interval");
        println("\trestore [-uri uri] [-limit s]  - Restore database");
        println("\tset              - Set a dynamic variable name to value");
        println("\tshow             - Show current dynamic variables");
        println("\tstart            - Start replicator and move to OFFLINE state");
        println("\tstatus           - Print full replicator status information");
        println("\tstop             - Stop replicator gracefully (must be OFFLINE)");
        println("\twait -state s [-limit s] - Wait up to s seconds for replicator state s");
        println("\twait -applied n [-limit s] - Wait up to s seconds for seqno to be applied");
        println("\tcheck <table> [-limit offset,limit] [-method m] - generate consistency check for the given table");
        println("Omitting a command prints summary replicator status");
    }

    /**
     * Main method to run utility.
     * 
     * @param argv optional command string
     */
    public static void main(String argv[]) {
        ReplicatorManagerCtrl ctrl = new ReplicatorManagerCtrl(argv);
        ctrl.go();
    }

    /**
     * Process replicator command.
     */
    public void go() {
        String rmiHost = ReplicatorConf.RMI_DEFAULT_HOST;
        int rmiPort = new Integer(System.getProperty(ReplicatorConf.RMI_PORT, ReplicatorConf.RMI_DEFAULT_PORT)).intValue();
        boolean verbose = false;
        String command = null;
        String curArg = null;
        try {
            while (argvIterator.hasNext()) {
                curArg = argvIterator.next();
                if ("-host".equals(curArg)) rmiHost = argvIterator.next(); else if ("-port".equals(curArg)) rmiPort = Integer.parseInt(argvIterator.next()); else if ("-verbose".equals(curArg)) verbose = true; else if (curArg.startsWith("-")) {
                    fatal("Unrecognized global option: " + curArg, null);
                } else {
                    command = curArg;
                    break;
                }
            }
        } catch (NumberFormatException e) {
            fatal("Bad numeric argument for " + curArg, null);
        } catch (ArrayIndexOutOfBoundsException e) {
            fatal("Missing value for " + curArg, null);
        }
        ReplicatorManagerMBean manager = null;
        ReplicatorMonitorMBean monitor = null;
        try {
            try {
                JMXConnector conn = JmxManager.getRMIConnector(rmiHost, rmiPort, ReplicatorConf.RMI_DEFAULT_SERVICE_NAME);
                manager = (ReplicatorManagerMBean) JmxManager.getMBeanProxy(conn, ReplicatorManager.class, false);
                monitor = (ReplicatorMonitorMBean) JmxManager.getMBeanProxy(conn, ReplicatorMonitor.class, false);
            } catch (ServerRuntimeException e) {
                fatal("Connection failed: " + e, e);
            }
            if (command != null) {
                if (command.equals(Commands.START)) manager.start(); else if (command.equals(Commands.ONLINE)) manager.online(); else if (command.equals(Commands.OFFLINE)) manager.offline(); else if (command.equals(Commands.WAIT)) {
                    String state = null;
                    long seqno = -99;
                    long seconds = 0;
                    while (argvIterator.hasNext()) {
                        curArg = argvIterator.next();
                        try {
                            if ("-state".equals(curArg)) state = argvIterator.next(); else if ("-applied".equals(curArg)) seqno = Long.parseLong(argvIterator.next()); else if ("-limit".equals(curArg)) seconds = Long.parseLong(argvIterator.next()); else fatal("Unrecognized option: " + curArg, null);
                        } catch (Exception e) {
                            fatal("Missing or invalid argument to flag: " + curArg, null);
                        }
                    }
                    boolean succeeded = false;
                    if (state != null && seqno != -99) {
                        fatal("You must specify -state or -applied, not both", null);
                    } else if (state != null) {
                        succeeded = manager.waitForState(state, seconds);
                    } else if (seqno != 99) {
                        succeeded = manager.waitForAppliedSequenceNumber(seqno, seconds);
                    } else {
                        fatal("You must specify what to wait for using -state or -applied", null);
                    }
                    if (!succeeded) {
                        fatal("Wait timed out!", null);
                    }
                } else if (command.equals(Commands.CHECK)) {
                    String schemaName = null;
                    String tableName = null;
                    int rowOffset = ConsistencyTable.ROW_UNSET;
                    int rowLimit = ConsistencyTable.ROW_UNSET;
                    String ccType = "md5";
                    while (argvIterator.hasNext()) {
                        curArg = argvIterator.next();
                        if ("-limit".equals(curArg)) {
                            String[] limits = argvIterator.next().split(",");
                            if (limits.length != 2) {
                                fatal("'-limit' option requires two comma-separated positive integer parameters: offset,range", null);
                            }
                            rowOffset = Integer.parseInt(limits[0]);
                            rowLimit = Integer.parseInt(limits[1]);
                            if (rowOffset < 0 || rowLimit < 0) {
                                fatal("'-limit' option requires non-negative parameters", null);
                            }
                        } else if ("-method".equals(curArg)) {
                            ccType = argvIterator.next();
                        } else if (schemaName == null) {
                            String[] names = curArg.split("\\.");
                            if (names.length > 2 || names.length < 1) {
                                fatal("Schema/table name must be in the form schema[.table]. Found: " + curArg, null);
                            }
                            schemaName = names[0];
                            if (names.length == 2) {
                                tableName = names[1];
                            }
                        } else {
                            fatal("Unrecognized argument: " + curArg, null);
                        }
                    }
                    if (schemaName == null) {
                        fatal("Schema/table name must be supplied" + curArg, null);
                    }
                    if (tableName == null && rowOffset != ConsistencyTable.ROW_UNSET) {
                        println("Only schema name supplied, row limits will be ignored.");
                    }
                    manager.consistencyCheck(ccType, schemaName, tableName, rowOffset, rowLimit);
                } else if (command.equals(Commands.STOP)) {
                    expectLostConnection = true;
                    manager.stop();
                } else if (command.equals(Commands.KILL)) {
                    boolean yes = false;
                    while (argvIterator.hasNext()) {
                        curArg = argvIterator.next();
                        if ("-y".equals(curArg)) yes = true; else fatal("Unrecognized option: " + curArg, null);
                    }
                    if (!yes) {
                        String answer = readline("Do you really want to kill the replicator process? [yes/NO] ");
                        yes = "yes".equals(answer);
                    }
                    if (yes) {
                        println("Sending kill command to replicator");
                        expectLostConnection = true;
                        manager.kill();
                    }
                } else if (command.equals(Commands.HEARTBEAT)) {
                    manager.heartbeat();
                } else if (command.equals(Commands.FLUSH)) {
                    long seconds = 0;
                    while (argvIterator.hasNext()) {
                        curArg = argvIterator.next();
                        try {
                            if ("-limit".equals(curArg)) seconds = Long.parseLong(argvIterator.next()); else fatal("Unrecognized option: " + curArg, null);
                        } catch (Exception e) {
                            fatal("Missing or invalid argument to flag: " + curArg, null);
                        }
                    }
                    long seqno = manager.flush(seconds);
                    println("Master log is synchronized with database at sequence number: " + seqno);
                } else if (command.equals(Commands.CONFIGURE)) {
                    TungstenProperties conf = null;
                    if (argvIterator.hasNext()) {
                        File propsFile = new File(argvIterator.next());
                        if (!propsFile.exists() || !propsFile.canRead()) {
                            fatal("Properties file not found: " + propsFile.getAbsolutePath(), null);
                        }
                        conf = new TungstenProperties();
                        try {
                            conf.load(new FileInputStream(propsFile));
                        } catch (IOException e) {
                            fatal("Unable to read properties file: " + propsFile.getAbsolutePath() + " (" + e.getMessage() + ")", null);
                        }
                    }
                    Map<String, String> confMap;
                    if (conf == null) confMap = null; else confMap = conf.map();
                    manager.configure(confMap);
                } else if (command.equals(Commands.SET)) {
                    String name = null;
                    String value = null;
                    try {
                        while (argvIterator.hasNext()) {
                            curArg = argvIterator.next();
                            if ("-name".equals(curArg)) name = argvIterator.next(); else if ("-value".equals(curArg)) value = argvIterator.next(); else {
                                fatal("Unrecognized option: " + curArg, null);
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        fatal("Missing value for " + curArg, null);
                    }
                    TungstenProperties tp = new TungstenProperties();
                    tp.setString(name, value);
                    manager.updateDynamicProperties(tp.map());
                } else if (command.equals(Commands.SHOW)) {
                    String name = null;
                    try {
                        while (argvIterator.hasNext()) {
                            curArg = argvIterator.next();
                            if ("-name".equals(curArg)) name = argvIterator.next(); else {
                                fatal("Unrecognized option: " + curArg, null);
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        fatal("Missing value for " + curArg, null);
                    }
                    TungstenProperties properties = new TungstenProperties(manager.getDynamicProperties());
                    if (name == null) {
                        for (String key : properties.keyNames()) {
                            println("Name:\t" + key + "\tValue:\t" + properties.getString(key));
                        }
                    } else {
                        String value = properties.getString(name);
                        if (value == null) println("Dynamic property not found: " + name); else println("Name:\t" + name + "\tValue:\t" + properties.getString(name));
                    }
                } else if (command.equals(Commands.CLEAR)) {
                    manager.clearDynamicProperties();
                } else if (command.equals(Commands.BACKUP)) {
                    String backupAgent = null;
                    String storageAgent = null;
                    long seconds = 0;
                    try {
                        while (argvIterator.hasNext()) {
                            curArg = argvIterator.next();
                            if ("-backup".equals(curArg)) backupAgent = argvIterator.next(); else if ("-storage".equals(curArg)) storageAgent = argvIterator.next(); else if ("-limit".equals(curArg)) seconds = Long.parseLong(argvIterator.next()); else {
                                fatal("Unrecognized option: " + curArg, null);
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        fatal("Missing value for " + curArg, null);
                    }
                    String uri = manager.backup(backupAgent, storageAgent, seconds);
                    if (uri == null) println("Backup is pending; check log for status"); else println("Backup completed successfully; URI=" + uri);
                } else if (command.equals(Commands.RESTORE)) {
                    String uri = null;
                    long seconds = 0;
                    try {
                        while (argvIterator.hasNext()) {
                            curArg = argvIterator.next();
                            if ("-uri".equals(curArg)) uri = argvIterator.next(); else if ("-limit".equals(curArg)) seconds = Long.parseLong(argvIterator.next()); else {
                                fatal("Unrecognized option: " + curArg, null);
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        fatal("Missing value for " + curArg, null);
                    }
                    boolean success = manager.restore(uri, seconds);
                    if (success) println("Restore completed successfully"); else println("Restore is pending; check log for status");
                } else if (command.equals(Commands.REAL_DETAIL)) {
                    long startingMaxSeq = manager.getMaxSeqNo();
                    long lastMaxSeq = 0;
                    long currentMaxSeq = 0;
                    boolean oneMoreThenQuit = false;
                    ArrayList<MonitorThreadInfo> cpuTimes;
                    Iterator<?> iter;
                    MonitorThreadInfo mti;
                    int i;
                    int divisorType = Divisor.NONE;
                    long divisor = 1L;
                    String name;
                    long startingRows, endingRows, totalRows;
                    long startingEvents, endingEvents, totalEvents;
                    long startingTime, endingTime, totalTime;
                    try {
                        while (argvIterator.hasNext()) {
                            curArg = argvIterator.next();
                            if ("-divisor".equals(curArg)) {
                                name = argvIterator.next();
                                if ("self".equals(name)) divisorType = Divisor.SELF; else if ("none".equals(name)) divisorType = Divisor.NONE; else if ("rows".equals(name)) divisorType = Divisor.RECORDS; else if ("events".equals(name)) divisorType = Divisor.EVENTS; else fatal("Unrecognized divisor: " + curArg, null);
                            } else if ("-help".equals(curArg)) {
                                displayRealHelp();
                                return;
                            } else {
                                fatal("Unrecognized option: " + curArg, null);
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        fatal("Missing value for " + curArg, null);
                    }
                    cpuTimes = monitor.getCPUTimes();
                    if (cpuTimes == null) {
                        System.out.format("No CPU times registerred yet");
                        return;
                    }
                    startingRows = monitor.getRows();
                    startingEvents = monitor.getEvents();
                    startingTime = System.currentTimeMillis();
                    System.out.format("Dividing values by %s. Time units are micro-seconds\n", getDivisorName(divisorType));
                    System.out.format("%-8s ", "Thread");
                    for (i = 0; i < ReplicatorMonitor.realNames.length; i++) System.out.format("%8s ", ReplicatorMonitor.realNames[i]);
                    System.out.format("%8s\n", "Total");
                    for (int sampleNumber = 1; ; sampleNumber++) {
                        manager.clearMonitoringCounters();
                        Thread.sleep(5000);
                        cpuTimes = monitor.getCPUTimes();
                        iter = cpuTimes.iterator();
                        while (iter.hasNext()) {
                            long totalSeen = 0L;
                            long value;
                            MonitorThreadBucket buckets[];
                            mti = (MonitorThreadInfo) iter.next();
                            System.out.format("%-8s ", mti.getName());
                            buckets = mti.getRealTimes();
                            for (i = 0; i < ReplicatorMonitor.realNames.length; i++) {
                                value = buckets[i].getValue();
                                divisor = getDivisor(divisorType, buckets[i], monitor);
                                totalSeen += value;
                                value /= 1000L;
                                value /= divisor;
                                System.out.format("%8s ", Long.toString(value));
                            }
                            totalSeen /= 1000L;
                            totalSeen /= divisor;
                            System.out.format("%8s", Long.toString(totalSeen));
                            System.out.format("\n");
                        }
                        System.out.format("\n");
                        if (oneMoreThenQuit) break;
                        currentMaxSeq = manager.getMaxSeqNo();
                        if (sampleNumber > 1 && currentMaxSeq > startingMaxSeq && currentMaxSeq == lastMaxSeq) oneMoreThenQuit = true;
                        lastMaxSeq = currentMaxSeq;
                    }
                    endingRows = monitor.getRows();
                    endingEvents = monitor.getEvents();
                    endingTime = System.currentTimeMillis();
                    totalRows = endingRows - startingRows;
                    totalEvents = endingEvents - startingEvents;
                    totalTime = endingTime - startingTime;
                    totalTime /= 1000L;
                    System.out.format("Observed %s rows, over %s events for %s rows/event\n", Long.toString(totalRows), Long.toString(totalEvents), Long.toString((totalEvents != 0) ? totalRows / totalEvents : 0L));
                    System.out.format("Observed %s rows, over %s seconds for %s rows/second\n", Long.toString(totalRows), Long.toString(totalTime), Long.toString((totalTime != 0) ? totalRows / totalTime : 0L));
                    System.out.format("Observed %s events, over %s seconds for %s events/second\n", Long.toString(totalEvents), Long.toString(totalTime), Long.toString((totalTime != 0) ? totalEvents / totalTime : 0L));
                } else if (command.equals(Commands.CPU_DETAIL)) {
                    long startingMaxSeq = manager.getMaxSeqNo();
                    long lastMaxSeq = 0;
                    long currentMaxSeq = 0;
                    boolean oneMoreThenQuit = false;
                    long CPUTime = 0;
                    ArrayList<MonitorThreadInfo> cpuTimes;
                    Iterator<?> iter;
                    Long totalCPUTime;
                    MonitorThreadInfo mti;
                    int largestBucketNumber;
                    int bucketNumber;
                    long largestBucketSize;
                    long displayValue;
                    int i;
                    int divisorType = Divisor.NONE;
                    long divisor = 1L;
                    String name;
                    long startingRows, endingRows, totalRows;
                    long startingEvents, endingEvents, totalEvents;
                    long startingTime, endingTime, totalTime;
                    try {
                        while (argvIterator.hasNext()) {
                            curArg = argvIterator.next();
                            if ("-divisor".equals(curArg)) {
                                name = argvIterator.next();
                                if ("self".equals(name)) divisorType = Divisor.SELF; else if ("none".equals(name)) divisorType = Divisor.NONE; else if ("rows".equals(name)) divisorType = Divisor.RECORDS; else if ("events".equals(name)) divisorType = Divisor.EVENTS; else fatal("Unrecognized divisor: " + curArg, null);
                            } else if ("-help".equals(curArg)) {
                                displayCPUHelp();
                                return;
                            } else {
                                fatal("Unrecognized option: " + curArg, null);
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        fatal("Missing value for " + curArg, null);
                    }
                    cpuTimes = monitor.getCPUTimes();
                    if (cpuTimes == null) {
                        System.out.format("No CPU times registerred yet");
                        return;
                    }
                    startingRows = monitor.getRows();
                    startingEvents = monitor.getEvents();
                    startingTime = System.currentTimeMillis();
                    System.out.format("Dividing values by %s. Time units are micro-seconds.\n", getDivisorName(divisorType));
                    System.out.format("%-8s", "Thread");
                    for (i = 0; i < ReplicatorMonitor.cpuNames.length; i++) System.out.format("%8s ", ReplicatorMonitor.cpuNames[i]);
                    System.out.format("%8s    %8s\n", "Missing", "Total(pct)");
                    for (int sampleNumber = 1; ; sampleNumber++) {
                        manager.clearMonitoringCounters();
                        Thread.sleep(5000);
                        cpuTimes = monitor.getCPUTimes();
                        totalCPUTime = 0L;
                        iter = cpuTimes.iterator();
                        largestBucketNumber = 0;
                        largestBucketSize = 0L;
                        bucketNumber = 0;
                        while (iter.hasNext()) {
                            mti = (MonitorThreadInfo) iter.next();
                            CPUTime = mti.getCPUTime();
                            totalCPUTime += CPUTime;
                            if (CPUTime > largestBucketSize) {
                                largestBucketNumber = bucketNumber;
                                largestBucketSize = CPUTime;
                            }
                            bucketNumber++;
                        }
                        iter = cpuTimes.iterator();
                        bucketNumber = 0;
                        while (iter.hasNext()) {
                            long totalSeen = 0L;
                            long value;
                            long missing;
                            MonitorThreadBucket buckets[];
                            mti = (MonitorThreadInfo) iter.next();
                            CPUTime = mti.getCPUTime();
                            displayValue = CPUTime * 100L / totalCPUTime;
                            if (bucketNumber == largestBucketNumber && displayValue != 100) displayValue++;
                            System.out.format("%-8s ", mti.getName());
                            buckets = mti.getCPUTimes();
                            for (i = 0; i < ReplicatorMonitor.cpuNames.length; i++) {
                                value = buckets[i].getValue();
                                totalSeen += value;
                                divisor = getDivisor(divisorType, buckets[i], monitor);
                                value /= 1000L;
                                value /= divisor;
                                System.out.format("%8s ", Long.toString(value));
                            }
                            missing = CPUTime - totalSeen;
                            missing /= 1000L;
                            missing /= divisor;
                            System.out.format("%8s ", Long.toString(missing));
                            CPUTime /= 1000L;
                            CPUTime /= divisor;
                            System.out.format("%8s(%2s%%) ", Long.toString(CPUTime), Long.toString(displayValue));
                            bucketNumber++;
                            System.out.format("\n");
                        }
                        System.out.format("\n");
                        if (oneMoreThenQuit) break;
                        currentMaxSeq = manager.getMaxSeqNo();
                        if (sampleNumber > 1 && currentMaxSeq > startingMaxSeq && currentMaxSeq == lastMaxSeq) oneMoreThenQuit = true;
                        lastMaxSeq = currentMaxSeq;
                    }
                    endingRows = monitor.getRows();
                    endingEvents = monitor.getEvents();
                    endingTime = System.currentTimeMillis();
                    totalRows = endingRows - startingRows;
                    totalEvents = endingEvents - startingEvents;
                    totalTime = endingTime - startingTime;
                    totalTime /= 1000L;
                    System.out.format("Observed %s rows, over %s events for %s rows/event\n", Long.toString(totalRows), Long.toString(totalEvents), Long.toString((totalEvents != 0) ? totalRows / totalEvents : 0L));
                    System.out.format("Observed %s rows, over %s seconds for %s rows/second\n", Long.toString(totalRows), Long.toString(totalTime), Long.toString((totalTime != 0) ? totalRows / totalTime : 0L));
                    System.out.format("Observed %s events, over %s seconds for %s events/second\n", Long.toString(totalEvents), Long.toString(totalTime), Long.toString((totalTime != 0) ? totalEvents / totalTime : 0L));
                } else if (command.equals(Commands.CPU)) {
                    long startingMaxSeq = manager.getMaxSeqNo();
                    long lastMaxSeq = 0;
                    long currentMaxSeq = 0;
                    boolean oneMoreThenQuit = false;
                    long CPUTime = 0;
                    ArrayList<MonitorThreadInfo> cpuTimes;
                    Iterator<?> iter;
                    Long totalCPUTime;
                    MonitorThreadInfo mti;
                    int largestBucketNumber;
                    int bucketNumber;
                    long largestBucketSize;
                    long displayValue;
                    cpuTimes = monitor.getCPUTimes();
                    if (cpuTimes == null) {
                        System.out.format("No CPU times registerred yet");
                        return;
                    }
                    iter = cpuTimes.iterator();
                    while (iter.hasNext()) {
                        mti = (MonitorThreadInfo) iter.next();
                        System.out.format("%14s ", mti.getName());
                    }
                    System.out.format("\n");
                    for (int sampleNumber = 1; ; sampleNumber++) {
                        manager.clearMonitoringCounters();
                        Thread.sleep(5000);
                        cpuTimes = monitor.getCPUTimes();
                        totalCPUTime = 0L;
                        iter = cpuTimes.iterator();
                        largestBucketNumber = 0;
                        largestBucketSize = 0L;
                        bucketNumber = 0;
                        while (iter.hasNext()) {
                            mti = (MonitorThreadInfo) iter.next();
                            CPUTime = mti.getCPUTime();
                            totalCPUTime += CPUTime;
                            if (CPUTime > largestBucketSize) {
                                largestBucketNumber = bucketNumber;
                                largestBucketSize = CPUTime;
                            }
                            bucketNumber++;
                        }
                        iter = cpuTimes.iterator();
                        bucketNumber = 0;
                        while (iter.hasNext()) {
                            mti = (MonitorThreadInfo) iter.next();
                            CPUTime = mti.getCPUTime();
                            displayValue = CPUTime * 100L / totalCPUTime;
                            if (bucketNumber == largestBucketNumber && displayValue != 100) displayValue++;
                            System.out.format("%10s(%2s%%) ", Long.toString(CPUTime), Long.toString(displayValue));
                            bucketNumber++;
                        }
                        System.out.format("\n");
                        if (oneMoreThenQuit) break;
                        currentMaxSeq = manager.getMaxSeqNo();
                        if (sampleNumber > 1 && currentMaxSeq > startingMaxSeq && currentMaxSeq == lastMaxSeq) oneMoreThenQuit = true;
                        lastMaxSeq = currentMaxSeq;
                    }
                } else if (command.equals(Commands.CSV)) {
                    long startingMaxSeq = manager.getMaxSeqNo();
                    long lastMaxSeq = 0;
                    long currentMaxSeq = 0;
                    boolean oneMoreThenQuit = false;
                    double loadAverage;
                    int loadPercent;
                    int numberOfProcessors;
                    java.lang.management.OperatingSystemMXBean mxbean;
                    java.lang.management.ThreadMXBean tmxbean;
                    long myCPUTime = 0;
                    long[] threadIDs;
                    ThreadInfo[] threadInfos;
                    mxbean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
                    tmxbean = java.lang.management.ManagementFactory.getThreadMXBean();
                    numberOfProcessors = mxbean.getAvailableProcessors();
                    if (numberOfProcessors < 1) numberOfProcessors = 1;
                    System.out.format("Sample,  MaxSeq,           ExtrPerSec,          ApplyPerSec,    ApplyLatency,  UsedCache, Total Cache, LoadPct(p=%d)", numberOfProcessors);
                    for (int sampleNumber = 1; ; sampleNumber++) {
                        manager.clearMonitoringCounters();
                        Thread.sleep(5000);
                        loadAverage = 1;
                        myCPUTime = tmxbean.getCurrentThreadCpuTime();
                        threadIDs = tmxbean.getAllThreadIds();
                        threadInfos = tmxbean.getThreadInfo(threadIDs);
                        loadPercent = (int) (loadAverage / numberOfProcessors * 100);
                        System.out.format("%6d, %7s, %10s, %10s, %15s, %10d, %10d %5d %5s %d\n", sampleNumber, Long.toString(manager.getMaxSeqNo()), Double.toString(monitor.getExtractedPerSec()), Double.toString(monitor.getAppliedPerSec()), Double.toString(monitor.getAppliedLatency()), monitor.getEventsUsedCacheSize(), monitor.getEventsTotalCacheSize(), loadPercent, Long.toString(myCPUTime), threadInfos.length);
                        if (oneMoreThenQuit) break;
                        currentMaxSeq = manager.getMaxSeqNo();
                        if (sampleNumber > 1 && currentMaxSeq > startingMaxSeq && currentMaxSeq == lastMaxSeq) oneMoreThenQuit = true;
                        lastMaxSeq = currentMaxSeq;
                    }
                } else if (command.equals(Commands.STATS)) {
                    long[] valMinMax = manager.getMinMaxSeqNo();
                    println("Name               Value");
                    println("===============================================");
                    println("System ID          " + manager.getSourceId());
                    println("System Version:    " + manager.getVersionInfo());
                    println("System State:      " + manager.getState());
                    println("System Uptime (S): " + manager.getUptimeSeconds() + "s");
                    println("State Uptime (S):  " + manager.getTimeInStateSeconds() + "s");
                    println("Error:             " + manager.getPendingError());
                    println("Error Exception:   " + manager.getPendingExceptionMessage());
                    println("Min Seq No:        " + valMinMax[0]);
                    println("Max Seq No:        " + valMinMax[1]);
                    println("Latest Epoch Nmbr: " + monitor.getLatestEpochNumber());
                    println("Latest Event ID:   " + monitor.getLatestEventId());
                    println("Monitor Intvl (S): " + monitor.getMonitoringIntervalSecs());
                    println("Extr Total:        " + monitor.getExtracted());
                    println("Extr Last Seq No:  " + monitor.getExtractedLastSeqNo());
                    println("Extr/Sec:          " + monitor.getExtractedPerSec());
                    println("Recv Total:        " + monitor.getReceived());
                    println("Recv Last Seq No:  " + monitor.getReceivedLastSeqNo());
                    println("Recv Source TS:    " + monitor.getReceivedLastSourceTStamp());
                    println("Recv Target TS:    " + monitor.getReceivedLastTargetTStamp());
                    println("Recv Latency (S):  " + monitor.getReceivedLatency());
                    println("Recv/Sec:          " + monitor.getReceivedPerSec());
                    println("Apply Total:       " + monitor.getApplied());
                    println("Apply Last Seq No: " + monitor.getAppliedLastSeqNo());
                    println("Apply Source TS:   " + monitor.getAppliedLastSourceTStamp());
                    println("Apply Target TS:   " + monitor.getAppliedLastTargetTStamp());
                    println("Apply Latency (S): " + monitor.getAppliedLatency());
                    println("Apply/Sec:         " + monitor.getAppliedPerSec());
                    if (monitor.getEventsTotalCacheSize() > 0) println("Cache:             " + monitor.getEventsUsedCacheSize() + "/" + monitor.getEventsTotalCacheSize() + "(" + monitor.getUsedCacheRatio() + "%)");
                    println("State: " + manager.getState());
                    if (manager.getPendingError() != null) {
                        println("Error: " + manager.getPendingError());
                        println("Exception Message: " + manager.getPendingExceptionMessage());
                    }
                    println("Seqno Range: " + valMinMax[0] + " -> " + valMinMax[1]);
                    return;
                } else if (command.equals(Commands.RESET)) manager.clearMonitoringCounters(); else if (command.equals(Commands.HELP)) {
                    printHelp();
                } else if (command.equals(Commands.GO_ONLINE)) {
                    println("Warning: goOnline is deprecated; use online instead");
                    manager.online();
                } else if (command.equals(Commands.SHUTDOWN)) {
                    println("Warning: shutdown is deprecated; use offline instead");
                    manager.offline();
                } else if (command.equals(Commands.GO_MASTER)) {
                    println("Warning: goOnline is deprecated; use online instead");
                    manager.online();
                } else if (command.equals(Commands.GO_OFFLINE)) {
                    println("Warning: goOffline is deprecated; use offline instead");
                    manager.offline();
                } else if (command.equals(Commands.DIAG)) {
                    manager.diag();
                    println("Diag executed, check log for output");
                } else {
                    println("Unknown command: '" + command + "'");
                    printHelp();
                }
            }
            println("State: " + manager.getState());
            if (manager.getPendingError() != null) {
                println("Error: " + manager.getPendingError());
                println("Exception Message: " + manager.getPendingExceptionMessage());
            }
            println("Seqno Range: " + manager.getMinSeqNo() + " -> " + manager.getMaxSeqNo());
        } catch (ConnectException e) {
            if (expectLostConnection) println("RMI connection lost!"); else fatal("RMI connection lost!", e);
        } catch (RemoteException e) {
            if (expectLostConnection) println("Replicator appears to be stopped"); else {
                fatal("Fatal RMI communication error: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            println("Operation failed: " + e.getMessage());
            if (verbose) e.printStackTrace();
            if (manager.getPendingError() != null) {
                println("Error: " + manager.getPendingError());
                println("Exception Message: " + manager.getPendingExceptionMessage());
            }
        } catch (Throwable t) {
            fatal("Fatal error: " + t.getMessage(), t);
        }
    }

    private static void displayRealHelp() {
        println("RealHelp");
        println("-divisor d - Option divisor parameter.");
        println(" -divisor none  -> do not divide metric by anything, print raw value");
        println(" -divisor self  -> divide metric by number of times metric itself was taken");
        println(" -divisor event -> divide metric by number of events processed");
        println(" -divisor rows  -> divide metric by number of rows processed");
        println("");
        println("Metrics");
        println(" - TSrvSnd  -> Time spent by master for send operation to complete");
        println(" - TCliRcv  -> Time spent by slave for receive operation to complete");
        println(" - ExtrWait -> Time spent by master waiting for extractor to fill THL");
        println(" - THLWait  -> Time spent by slave waiting for network thread to fill THL");
        println(" - ExtHead  -> Time spent by master extractor reading binlog (header piece)");
        println(" - ExtBody  -> Time spent by master extractor reading binlog (body piece)");
        println(" - InsWait  -> Time spent by applier waiting for insert of client row to complete");
        println(" - UpdWait  -> Time spent by applier waiting for update of client row to complete");
        println(" - DelWait  -> Time spent by applier waiting for delete of client row to complete");
        println(" - ComWait  -> Time spent by applier waiting for commit of client row to complete");
        println(" - Apply    -> Time spent applying single event");
        println(" - Extract  -> Time spent extracting single event");
        println("");
        println("Notes");
        println(" - The \"Apply\" metric contains the times accounted for in \"InsWait\", \"UpdWait\", \"DelWait\",");
        println("   and \"ComWait\".  It is taken from a higher level in the code line.");
    }

    private static void displayCPUHelp() {
        println("CPUHelp");
        println("-divisor d - Option divisor parameter.");
        println(" -divisor none  -> do not divide metric by anything, print raw value");
        println(" -divisor self  -> divide metric by number of times metric itself was taken");
        println(" -divisor event -> divide metric by number of events processed");
        println(" -divisor rows  -> divide metric by number of rows processed");
        println("");
        println("Metrics");
        println(" - MsgSerial -> CPU spent serializing messages to be sent out");
        println(" - MsgDeSer  -> CPU spent deserializing messages read in");
        println(" - DBSerial  -> CPU spent serializing events to be written to database");
        println(" - DBDeSer   -> CPU spent deserializing events read from database");
        println(" - Extract   -> CPU spent extracting and parsing binlog event");
        println(" - InsTHL    -> CPU spent inserting already serialized event into THL");
        println(" - Checksum  -> CPU spent computing checksum (either master or slave)");
        println(" - AplBuSQL  -> CPU spent building textual SQL statement for client SQL");
        println(" - AplPrSQL  -> CPU spent in prepare call for client SQL");
        println(" - AplBiSQL  -> CPU spent binding variables for client SQL");
        println(" - AplExSQL  -> CPU spent executing statement for client SQL");
        println(" - AplGEvt   -> CPU spent getting event from THL to be applied");
        println(" - AplAEvt   -> CPU spent applying event (approx sum(Apl*SQL) from above metrics)");
        println(" - AplMEvt   -> CPU spent mark SQL event as completed");
        println(" - AplBTran  -> CPU spent beginning transactions for client SQL");
        println(" - Missing   -> sum of above CPU times subtracted from known total CPU time");
        println(" - Total     -> Total CPU time thread reportedly has taken");
        println("");
        println("Notes");
        println(" - Total CPU comes from a completely different mechanism than the individual CPU times");
        println("   being reported (\"java.lang.management.ThreadMXBean.getThreadCpuTime()\").");
        println(" - As some of the CPU buckets now overlap, \"missing\" may no longer represent anything of");
        println("   importance.  It is left in, however, to see if there are significant amounts of CPU");
        println("   time left unreported.");
    }

    private static void println(String msg) {
        System.out.println(msg);
    }

    private static void print(String msg) {
        System.out.print(msg);
    }

    private String readline(String prompt) {
        if (prompt != null) print(prompt);
        try {
            return stdin.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    private static void fatal(String msg, Throwable t) {
        System.out.println(msg);
        if (t != null) t.printStackTrace();
        System.exit(1);
    }

    long getDivisor(int divisorType, MonitorThreadBucket bucket, ReplicatorMonitorMBean monitor) {
        long retval = 1L;
        switch(divisorType) {
            case Divisor.NONE:
                retval = 1L;
                break;
            case Divisor.SELF:
                retval = (long) bucket.getCount();
                break;
            case Divisor.RECORDS:
                retval = monitor.getRows();
                break;
            case Divisor.EVENTS:
                retval = monitor.getEvents();
                break;
        }
        if (retval == 0L) retval = 1L;
        return retval;
    }

    String getDivisorName(int divisorType) {
        switch(divisorType) {
            case Divisor.NONE:
                return "1";
            case Divisor.SELF:
                return "# of times specific bucket incremented";
            case Divisor.RECORDS:
                return "# of rows";
            case Divisor.EVENTS:
                return "# of events";
            default:
                return "unknown";
        }
    }

    class Commands {

        public static final String START = "start";

        public static final String STOP = "stop";

        public static final String ONLINE = "online";

        public static final String OFFLINE = "offline";

        public static final String KILL = "kill";

        public static final String FLUSH = "flush";

        public static final String HEARTBEAT = "heartbeat";

        public static final String SHUTDOWN = "shutdown";

        public static final String CONFIGURE = "configure";

        public static final String SET = "set";

        public static final String SHOW = "show";

        public static final String CLEAR = "clear";

        public static final String RESET = "resetMonitors";

        public static final String STATS = "status";

        public static final String CSV = "csv";

        public static final String CPU = "cpu";

        public static final String CPU_DETAIL = "cpuDetail";

        public static final String REAL_DETAIL = "realDetail";

        public static final String PROGRESS = "progress";

        public static final String HELP = "help";

        public static final String WAIT = "wait";

        public static final String CHECK = "check";

        public static final String BACKUP = "backup";

        public static final String RESTORE = "restore";

        public static final String DIAG = "diag";

        public static final String GO_ONLINE = "goOnline";

        public static final String GO_MASTER = "goMaster";

        public static final String GO_OFFLINE = "goOffline";
    }

    class Divisor {

        public static final int INVALID = 0;

        public static final int NONE = 1;

        public static final int SELF = 2;

        public static final int RECORDS = 3;

        public static final int EVENTS = 4;
    }
}
