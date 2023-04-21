package edu.yale.csgp.vitapad.tests;

import java.io.File;
import junit.framework.TestSuite;
import org.python.util.PythonInterpreter;

/**
 * @author ploix Class used to build test suites from python classes. Should be
 *         used by the Jython test case Wrapper only.
 */
class JythonTestSuiteExtractor extends TestSuite {

    /**
     * 
     * @param fileDir
     *            be careful : with slashes, not backslashes !!!
     * @param module
     *            to be scanned for tests
     * @param test
     *            the class from which getting the test methods
     */
    public JythonTestSuiteExtractor(String fileDir, String module, String test) {
        File f = new File(fileDir);
        PythonInterpreter interp = new PythonInterpreter();
        String[] cp = System.getProperty("java.class.path").replace('\\', '/').split(";");
        interp.exec("import sys");
        interp.exec("sys.path.append (\"" + fileDir + "\")");
        for (int i = 0; i < cp.length; i++) {
            String s = "sys.path.append (\"" + cp[i] + "\")";
            interp.exec(s);
        }
        interp.exec("import junit");
        try {
            interp.exec("from " + module + " import " + test);
        } catch (Exception e) {
            System.err.println("Jython : Couldn't extract module " + module + " from test " + test + " in directory " + fileDir + "; now running in " + System.getProperty("user.dir"));
            e.printStackTrace(System.err);
            return;
        }
        interp.exec("suite = junit.framework.TestSuite()");
        interp.exec("[suite.addTest( " + test + "(f) ) for f in dir(" + test + ") if f.startswith(\"test\")]");
        TestSuite suite = (TestSuite) interp.get("suite").__tojava__(TestSuite.class);
        addTest(suite);
    }
}
