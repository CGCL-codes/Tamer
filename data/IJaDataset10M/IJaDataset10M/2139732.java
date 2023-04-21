package com.att.echarts.test;

import com.att.echarts.StdoutMonitor;
import com.att.echarts.RawEventMonitor;
import com.att.echarts.Machine;
import com.att.echarts.MachineMonitor;
import com.att.echarts.MachineConstructor;
import java.util.Date;

/**
   Quick and dirty test execution and logging program. This program
   simply runs specified ECharts test programs, optionally logging
   their output to files. Takes one optional command line arg "-l dir"
   specifying directory path for creating 'raw' event log files for
   ECharts programs run by this program. If this option is not
   present, then any program output is directed to standard
   output. Subsequent command line args specify the name of ECharts
   test files (including the ".ech" extension). We assume the test
   files are in a package "test".

   The "-l" option is used to capture test program output for
   subsequent regression testing. See the RegressionTest class for
   more details.

   Also see the runTest and regressionTest scripts in the
   ECHARTS_HOME/runtime/java/test directory.
 */
public class RunTest {

    public static final void main(String[] argv) {
        int argIndex = 0;
        boolean loggingEnabled = false;
        String loggingDir = null;
        if (argv[argIndex].equals("-l")) {
            loggingEnabled = true;
            argIndex++;
            loggingDir = argv[argIndex];
            argIndex++;
        }
        final long startTime = System.currentTimeMillis();
        System.out.println("Test run started: " + new Date(startTime));
        if (loggingEnabled) {
            Machine.setSystemMonitoring(true);
            Machine.setSystemDebugging(true);
            Machine.setDefaultMonitoring(true);
            Machine.setDefaultDebugging(true);
            System.out.println("Log output directory: " + loggingDir);
        }
        System.out.print("\n");
        String fileName = null;
        String testName = null;
        String logFilePath = null;
        String machineName = null;
        int numTested = 0;
        int numCompleted = 0;
        int numAbnormal = 0;
        for (int i = argIndex; i < argv.length; i++) {
            try {
                fileName = argv[i];
                testName = fileName.substring(0, fileName.length() - 4);
                machineName = "test." + testName;
                if (loggingEnabled) {
                    logFilePath = loggingDir + System.getProperty("file.separator") + testName + "_Log.raw";
                    Machine.setDefaultMonitor(new RawEventMonitor(logFilePath, Machine.getDefaultMonitorFilter()));
                } else {
                    Machine.setDefaultMonitor(new StdoutMonitor(Machine.getDefaultMonitorFilter()));
                }
                System.out.println("*** Running " + testName);
                Machine machine = MachineConstructor.newInstance(machineName, null, null);
                numTested++;
                machine.run();
                System.out.println("*** Completed");
                numCompleted++;
            } catch (Exception e) {
                System.out.println("*** Terminated abnormally");
                e.printStackTrace();
                numAbnormal++;
            }
        }
        final long endTime = System.currentTimeMillis();
        System.out.print("\n");
        System.out.println("Test run ended: " + new Date(endTime));
        System.out.println("Elapsed time: " + (endTime - startTime) + " msec");
        System.out.println("Number of tests: " + numTested);
        System.out.println("Completed: " + numCompleted);
        System.out.println("Abnormally terminated: " + numAbnormal);
    }
}
