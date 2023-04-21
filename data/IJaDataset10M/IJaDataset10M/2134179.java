package test.org.mandarax.testsupport;

import org.apache.log4j.BasicConfigurator;

/**
 * Utility class to build test runner. For a detailed description
 * how it works, read the comment for run(Class,String[])
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 1.2
 */
public class TestRunner {

    public static String AWT = "awt";

    public static String TEXT = "text";

    public static String RELOAD = "reload";

    /**
	 * Check whether the parameter list contains a certain string.
	 * @return the result of the check
	 * @param pars the parameter list
	 * @param par the string
	 */
    private static boolean check(String[] pars, String par) {
        for (int i = 0; i < pars.length; i++) {
            if (pars[i].equalsIgnoreCase(par)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Run a test suite. suite is a class that has a static suite() method returning a test suite.
	 * The parameters determine how the test should be performed.
	 * By default, the swing test runner (junit.swingui.TestRunner) is used.
	 * If one parameter equals "awt", we use the awt based test runner, if "text" is specified, we use
	 * the text based swing runner. By default, classes are not reloaded.
	 * @param suite a test suite
	 * @param pars java.lang.String[]
	 */
    public static void run(Class suite, String[] pars) {
        run(suite, pars, true);
    }

    /**
	  * Run a test suite. suite is a class that has a static suite() method returning a test suite.
	  * The parameters determine how the test should be performed.
	  * By default, the swing test runner (junit.swingui.TestRunner) is used.
	  * If one parameter equals "awt", we use the awt based test runner, if "text" is specified, we use
	  * the text based swing runner. By default, classes are not reloaded.
	  * @param suite a test suite
	  * @param pars java.lang.String[]
	  * @param initLog whether to initialize log4j
	  */
    public static void run(Class suite, String[] pars, boolean initLog) {
        if (initLog) BasicConfigurator.configure();
        boolean reload = check(pars, RELOAD);
        String[] args;
        if (reload || check(pars, TEXT)) {
            args = new String[] { suite.getName() };
        } else {
            args = new String[] { suite.getName(), "-noloading" };
        }
        if (check(pars, AWT)) {
            junit.awtui.TestRunner.main(args);
        } else {
            if (check(pars, TEXT)) {
                junit.textui.TestRunner.main(args);
            } else {
                junit.swingui.TestRunner.main(args);
            }
        }
    }
}
