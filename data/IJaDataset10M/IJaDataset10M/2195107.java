package org.echarts.test;

import java.util.Date;
import org.echarts.Machine;
import org.echarts.TransitionMachine;
import org.echarts.MachineConstructor;
import org.echarts.monitor.PrettyPrintFormatter;
import org.echarts.monitor.PrintStreamMonitor;
import org.echarts.monitor.RawMonitor;

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
            Machine.setDebugging(true);
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
                    Machine.setDefaultMachineMonitor(new RawMonitor(logFilePath, Machine.getDefaultMachineMonitorFilter()));
                } else {
                    Machine.setDefaultMachineMonitor(new PrintStreamMonitor(Machine.getDefaultMachineMonitorFilter(), System.out, new PrettyPrintFormatter()));
                }
                System.out.println("*** Running " + testName);
                TransitionMachine machine = (TransitionMachine) MachineConstructor.newInstance(machineName, null);
                numTested++;
                machine.run();
                System.out.println("*** Completed");
                numCompleted++;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("*** Terminated abnormally");
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
