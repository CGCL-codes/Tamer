package org.tigr.antware.shared.testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.tigr.antware.shared.util.Logger;

/**
 * The class <code>TIGRTestCase</code> extends the {@link TestCase} class to provide the
 * functionality to invoke a test from the command line specifying which of the
 * TestRunners is used.
 * 
 * All classes that extend this class must call the
 * {@link #runTestsFromCommandLine(String, String[])} method in the main method
 * passing the command line parameters <code>args</code> and the fully qualified
 * class name.
 * 
 * <p>
 * Usage: java <class name> [test=<testname>] [truncate=true|false (default
 * true)] [delete=true|false (default true)] [log=<0|1|2|3|4|5|6|7>]
 * [logfile=<log filename>]
 */
public class TIGRTestCase extends TestCase {

    /**
	 * The variable <code>singleTestName</code> holds the single test name if one is
	 * specified.
	 *  
	 */
    private static String singleTestName;

    /**
	 * The variable <code>logLevel</code> holds the log level. The default is 0, or log
	 * level of SEVERE.
	 */
    private static int logLevel = -1;

    /**
	 * The variable <code>logFile</code> holds the log file name if one is specified
	 * with logfile parameter.
	 */
    private static String logFile;

    /**
     * The variable <code>mode</code> holds the mode, text, swing, ui under which this
     * test should be run.
     *
     */
    private static String mode;

    /**
	 * The variable <code>truncate</code> holds the value for the truncate table flag.
	 * If set to true and then after a test is run the data is truncated from tables
	 * otherwise it is not truncated  
	 */
    private static boolean truncate = true;

    /**
	 * The variable <code>deleteFiles</code> holds the flag that indicates if any files
	 * created during testing are deleted. If this flag is <code>true</code>, the
	 * default, files are deleted, otherwise files are left  
	 */
    private static boolean deleteFiles = true;

    /**
	 * The variable <code>testedClass</code> holds the name of the tested class. Child
	 * classes set this variable to take advantage of the suite method which allows for
	 * single test cases to be executed.
	 */
    public static Class testedClass = TIGRTestCase.class;

    /**
	 * Creates a new <code>TIGRTestCase</code> instance.
	 * 
	 * @param name a <code>String</code> value
	 */
    public TIGRTestCase(String name) {
        super(name);
    }

    /**
	 * The <code>isSingleTestSpecified</code> method checks to see if the
	 * singleTestName field was set when this test was invoked, if so the method returns
	 * true, otherwise it returns false
	 * 
	 * @return a <code>boolean</code> value of true if a single test was specified
	 */
    public static boolean isSingleTestSpecified() {
        boolean single = false;
        if (singleTestName != null) {
            single = true;
        }
        return single;
    }

    /**
	 * The <code>isTruncate</code> method returns the truncate flag value set by
	 * parsing arguments.
	 * 
	 * @return a <code>boolean</code> value of true if the truncate flag was specified
	 * as true, false otherwise
	 */
    public static boolean isTruncate() {
        return truncate;
    }

    /**
	 * The <code>isDeleteFiles</code> method returns the delete files flag value set by
	 * parsing arguments.
	 * 
	 * @return a <code>boolean</code> value of true if the files are to be deleted,
	 * default, or false if files are to be left intact
	 */
    public static boolean isDeleteFiles() {
        return deleteFiles;
    }

    /**
	 * The <code>buildTestSuiteForSpecificTest</code> method is a utility method used
	 * to build a test suite for the specified class that contains the single test
	 * specified as a command line parameter. This method should be invoked in the
	 * subclass <code>suite</code> method that returns a test suite.
	 * 
	 * @param testClass a <code>Class</code> value for the class being tested
	 * @return a <code>TestSuite</code> containing the single test specified on the
	 * command line
	 */
    public static TestSuite buildTestSuiteForSpecificTest(Class testClass) {
        TestSuite suite = new TestSuite();
        try {
            Class[] paramTypes = { String.class };
            Constructor constructor = testClass.getDeclaredConstructor(paramTypes);
            Object[] initArgs = { singleTestName };
            suite.addTest((TIGRTestCase) constructor.newInstance(initArgs));
            System.out.println("Testing '" + singleTestName + "' test case");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return suite;
    }

    /**
	 * The <code>getSingleTestName</code> method returns the parsed single test name if
	 * any.
	 * 
	 * @return a <code>String</code> value for the single test specified on command
	 * line
	 */
    public static String getSingleTestName() {
        return singleTestName;
    }

    /**
	 * The <code>suite</code> method return the test suite by adding the tests using
	 * reflection. This relies on the variable <code>testedClass</code> to be set by
	 * the child classes
	 * 
	 * @return a <code>Test</code> value containing the list of tests
     * @throws Throwable 
	 */
    public static Test suite() throws Throwable {
        TestSuite suite = null;
        if (TIGRTestCase.isSingleTestSpecified()) {
            suite = TIGRTestCase.buildTestSuiteForSpecificTest(testedClass);
        } else {
            suite = new TestSuite(testedClass);
        }
        return suite;
    }

    /**
	 * The <code>getLogLevel</code> method returns the log level parsed if one is
	 * specified. The default log level is 0
	 * 
	 * @return an <code>int</code> value for the log level
	 */
    public static int getLogLevel() {
        return logLevel;
    }

    /**
	 * The <code>getLogFile</code> method returns the log file parsed if one is
	 * specified.
	 * 
	 * @return an <code>String</code> value representing the log file name if one is
	 * specified, returns null otherwise
	 */
    public static String getLogFile() {
        return logFile;
    }

    /**
	 * The <code>getKey</code> method returns the key part a key value pair argument.
	 * If a key does not exists the method returns null.
	 * 
	 * @param argument a <code>String</code> value representing the argument
	 * @return a <code>String</code> value representing the key, null if this argument
	 * is not a key value pair
	 */
    private static String getKey(String argument) {
        int sepIndex = argument.indexOf("=");
        if (sepIndex > 0) {
            return argument.substring(0, sepIndex);
        }
        return null;
    }

    /**
	 * The <code>getValue</code> method returns the value part of a key value pair
	 * argument. if value does not exists the method returns null.
	 * 
	 * @param argument a <code>String</code> value representing the argument
	 * @return a <code>String</code> value representing the value, null if this
	 * argument is not a key value pair
	 */
    private static String getValue(String argument) {
        int sepIndex = argument.indexOf("=");
        if (sepIndex > 0) {
            return argument.substring(sepIndex + 1);
        }
        return null;
    }

    /**
	 * The <code>deleteFile</code> method deletes the specified file if the <code>deleteFiles</code>
	 * flag is set to true
	 * 
	 * @param fileName a <code>String</code> specifying the file name to delete
	 * @throws FileNotFoundException if the specified file cannot be found
	 */
    public static void deleteFile(String fileName) throws FileNotFoundException {
        if (deleteFiles) {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * The <code>deleteFile</code> method deletes the specified file if the <code>deleteFiles</code>
     * flag is set to true or if the forceDelete flag is specified as true
     * 
     * @param fileName a <code>String</code> specifying the file name to delete
     * @param forceDelete a <code>boolean</code> of true to force deletion
     * @throws FileNotFoundException if the specified file cannot be found
     */
    public static void deleteFile(String fileName, boolean forceDelete) throws FileNotFoundException {
        if (deleteFiles || forceDelete) {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
	 * The <code>parseArguments</code> method parses the command line arguments and
	 * sets the values for the static variables used in the test case.
	 * 
	 * @param args a <code>String[]</code> value with command line arguments
	 */
    public static void parseArguments(String className, String[] args) {
        String usage = "Usage: java " + className + "[test=<testname>] [log=<0|1|2|3|4|5|6|7>]  [logfile=<log filename>]" + " [truncate=true|false (default true)] [delete=true|false (default true)]";
        if ((args.length > 0) && (args[0].equalsIgnoreCase("--help"))) {
            System.out.println("Usage: java " + className);
            System.exit(0);
        }
        if (args.length == 0) {
            System.out.println("Invalid invocation of this class");
            System.out.println(usage);
            System.exit(1);
        } else {
            TIGRTestCase.singleTestName = null;
            for (int i = 0; i < args.length; i++) {
                String key = getKey(args[i]);
                if (key == null) {
                    System.out.println("Invalid invocation of this class. The argument '" + args[i] + "' is not a valid key of a key=value pair");
                    System.out.println(usage);
                    System.exit(1);
                }
                String value = getValue(args[i]);
                if (value == null) {
                    System.out.println("Invalid invocation of this class. The argument '" + args[i] + "' is not a valid value of a key=value pair");
                    System.out.println(usage);
                    System.exit(1);
                }
                if (key.equalsIgnoreCase("log")) {
                    TIGRTestCase.logLevel = Integer.parseInt(value);
                } else if (key.equalsIgnoreCase("test")) {
                    TIGRTestCase.singleTestName = value;
                } else if (key.equalsIgnoreCase("truncate")) {
                    TIGRTestCase.truncate = value.equalsIgnoreCase("true") ? true : false;
                } else if (key.equalsIgnoreCase("delete")) {
                    TIGRTestCase.deleteFiles = value.equalsIgnoreCase("true") ? true : false;
                } else if (key.equalsIgnoreCase("logfile")) {
                    TIGRTestCase.logFile = (String) value;
                }
            }
            if (logLevel > 7) {
                System.out.println("Invalid log level specified -- " + logLevel);
                System.out.println(usage);
                System.exit(1);
            }
            if (logLevel >= 0) {
                Logger.initializeLogging(logFile, logLevel);
            } else {
                Logger.initializeLogging();
            }
        }
    }

    /**
	 * The <code>runTestsFromCommandLine</code> method examines the command line
	 * parameters and invokes the appropriate test runner.
	 * 
	 * @param className a <code>String</code> value
	 * @param args a <code>String[]</code> value
	 */
    public static void runTestsFromCommandLine(String className, String[] args) {
        String[] testCaseName = { className };
        junit.textui.TestRunner.main(testCaseName);
        System.exit(0);
    }
}
