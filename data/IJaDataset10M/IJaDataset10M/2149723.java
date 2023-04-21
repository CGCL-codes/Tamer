package org.vizzini.tool.dependencyanalyzer;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.vizzini.util.FileUtilities;
import org.vizzini.util.GraphvizRunner;
import org.vizzini.util.TestFinder;

/**
 * Provides unit tests for the <code>DependencyAnalyzer</code> class.
 *
 * <p>By default, all test methods (methods names beginning with <code>
 * test</code>) are run. Run individual tests from the command line using the
 * <code>main()</code> method. Specify individual test methods to run using the
 * <code>suite()</code> method.</p>
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @see      TestFinder
 * @since    v0.3
 */
public class DependencyAnalyzerTest extends TestCase {

    /** Line separator. */
    private static final String LINE_SEPARATOR = "\n";

    /** Flag which indicates whether to produce verbose output. */
    private boolean _isVerbose = false;

    /**
     * Construct this object with the given parameter.
     *
     * @param  method  Method to run.
     *
     * @since  v0.3
     */
    public DependencyAnalyzerTest(String method) {
        super(method);
    }

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.3
     */
    public static void main(String[] args) {
        TestFinder.getInstance().run(DependencyAnalyzerTest.class, args);
    }

    /**
     * @return  a suite of tests to run.
     *
     * @since   v0.3
     */
    public static TestSuite suite() {
        TestSuite suite = new TestSuite(DependencyAnalyzerTest.class);
        return suite;
    }

    /**
     * Test the <code>analyze()</code> method.
     *
     * @since  v0.3
     */
    public void testAnalyze() {
        String baseDirectory = getBaseDirectory0();
        String basePackageDirectory = getBasePackageDirectory0();
        boolean isHighDetail = true;
        DependencyAnalyzer analyzer = new DependencyAnalyzer(baseDirectory, basePackageDirectory, isHighDetail);
        assertNotNull(analyzer);
        analyzer.analyze();
        Map<String, Set<String>> packagesToDependencies = analyzer.getPackageToDependencies();
        assertNotNull(packagesToDependencies);
        printPackagesToDependences(packagesToDependencies);
        assertEquals(10, packagesToDependencies.size());
    }

    /**
     * Test the constructor method.
     *
     * @since  v0.3
     */
    public void testConstructorHighDetail() {
        String baseDirectory = getBaseDirectory0();
        String basePackageDirectory = getBasePackageDirectory0();
        boolean isHighDetail = true;
        DependencyAnalyzer analyzer = new DependencyAnalyzer(baseDirectory, basePackageDirectory, isHighDetail);
        assertNotNull(analyzer);
        assertEquals(baseDirectory + "/", analyzer.getBaseDirectory());
        assertEquals(basePackageDirectory, analyzer.getBasePackageDirectory());
        String basePackage = basePackageDirectory.replaceAll("[/]", ".");
        assertEquals(basePackage, analyzer.getBasePackage());
        Set<String> packages = analyzer.getPackages();
        assertNotNull(packages);
        printPackages("packages", packages);
        assertEquals(10, packages.size());
        Map<String, URL> packagesToUrl = analyzer.getPackageToUrl();
        assertNotNull(packagesToUrl);
        printPackagesToUrl(packagesToUrl);
        assertEquals(10, packagesToUrl.size());
        Map<String, Set<String>> packagesToDependencies = analyzer.getPackageToDependencies();
        assertNotNull(packagesToDependencies);
        printPackagesToDependences(packagesToDependencies);
        assertEquals(0, packagesToDependencies.size());
    }

    /**
     * Test the constructor method.
     *
     * @since  v0.3
     */
    public void testConstructorLowDetail() {
        String baseDirectory = getBaseDirectory0();
        String basePackageDirectory = getBasePackageDirectory0();
        boolean isHighDetail = false;
        DependencyAnalyzer analyzer = new DependencyAnalyzer(baseDirectory, basePackageDirectory, isHighDetail);
        assertNotNull(analyzer);
        assertEquals(baseDirectory + "/", analyzer.getBaseDirectory());
        assertEquals(basePackageDirectory, analyzer.getBasePackageDirectory());
        String basePackage = basePackageDirectory.replaceAll("[/]", ".");
        assertEquals(basePackage, analyzer.getBasePackage());
        Set<String> packages = analyzer.getPackages();
        assertNotNull(packages);
        printPackages("packages", packages);
        assertEquals(10, packages.size());
        Map<String, URL> packagesToUrl = analyzer.getPackageToUrl();
        assertNotNull(packagesToUrl);
        printPackagesToUrl(packagesToUrl);
        assertEquals(10, packagesToUrl.size());
        Map<String, Set<String>> packagesToDependencies = analyzer.getPackageToDependencies();
        assertNotNull(packagesToDependencies);
        printPackagesToDependences(packagesToDependencies);
        assertEquals(0, packagesToDependencies.size());
    }

    /**
     * Test the <code>contains()</code> method.
     *
     * @throws  IOException  if there is an I/O problem.
     *
     * @since   v0.3
     */
    public void testContains() throws IOException {
        boolean isHighDetail = false;
        DependencyAnalyzer analyzer = new DependencyAnalyzer(getBaseDirectory0(), getBasePackageDirectory0(), isHighDetail);
        String filepath = getUserDir() + "/test-tool/org/vizzini/tool/dependencyanalyzer/package0/Class0.java";
        FileUtilities fileUtils = new FileUtilities();
        System.out.println("filepath = " + filepath);
        String content = fileUtils.readFile(filepath);
        String pattern = "org.vizzini.tool.dependencyanalyzer.package0";
        assertFalse(analyzer.contains(content, pattern));
        pattern = "org.vizzini.tool.dependencyanalyzer.package1";
        assertFalse(analyzer.contains(content, pattern));
        filepath = getUserDir() + "/test-tool/org/vizzini/tool/dependencyanalyzer/package0/package00/Class00.java";
        System.out.println("filepath = " + filepath);
        content = fileUtils.readFile(filepath);
        pattern = "org.vizzini.tool.dependencyanalyzer.package0.package00";
        assertFalse(analyzer.contains(content, pattern));
        pattern = "org.vizzini.tool.dependencyanalyzer.package0";
        assertTrue(analyzer.contains(content, pattern));
        pattern = "org.vizzini.tool.dependencyanalyzer.package1";
        assertFalse(analyzer.contains(content, pattern));
        filepath = getUserDir() + "/test-tool/org/vizzini/tool/dependencyanalyzer/package0/package00/package000/Class000.java";
        System.out.println("filepath = " + filepath);
        content = fileUtils.readFile(filepath);
        pattern = "org.vizzini.tool.dependencyanalyzer.package0.package00.package000";
        assertFalse(analyzer.contains(content, pattern));
        pattern = "org.vizzini.tool.dependencyanalyzer.package0.package00";
        assertTrue(analyzer.contains(content, pattern));
        pattern = "org.vizzini.tool.dependencyanalyzer.package0";
        assertFalse(analyzer.contains(content, pattern));
        pattern = "org.vizzini.tool.dependencyanalyzer.package1";
        assertFalse(analyzer.contains(content, pattern));
    }

    /**
     * Test the <code>contains()</code> method.
     *
     * @since  v0.3
     */
    public void testContains1() {
        boolean isHighDetail = true;
        DependencyAnalyzer analyzer = new DependencyAnalyzer(getBaseDirectory1(), getBasePackageDirectory1(), isHighDetail);
        String content = "something" + LINE_SEPARATOR + "import org.vizzini.ai.geneticalgorithm.AbstractGeneticAlgorithm;" + LINE_SEPARATOR;
        String pattern = "org.vizzini.ai.geneticalgorithm";
        assertTrue(analyzer.contains(content, pattern));
        pattern = "org.vizzini.ai";
        assertFalse(analyzer.contains(content, pattern));
        content = "something" + LINE_SEPARATOR + "package org.vizzini.ai.geneticalgorithm;" + LINE_SEPARATOR;
        pattern = "import org.vizzini.ai";
        assertFalse(analyzer.contains(content, pattern));
    }

    /**
     * Test the <code>createDotString()</code> method.
     *
     * @throws  IOException  if there is an I/O problem.
     *
     * @since   v0.3
     */
    public void testCreateDotStringHighDetail() throws IOException {
        String baseDirectory = getBaseDirectory0();
        String basePackageDirectory = getBasePackageDirectory0();
        boolean isHighDetail = true;
        DependencyAnalyzer analyzer = new DependencyAnalyzer(baseDirectory, basePackageDirectory, isHighDetail);
        assertNotNull(analyzer);
        analyzer.analyze();
        String dotString = analyzer.createDotString();
        if (_isVerbose) {
            GraphvizRunner runner = new GraphvizRunner();
            String fileRoot = System.getProperty("user.dir");
            runner.run(dotString, fileRoot);
            String outputFilename = fileRoot + ".png";
            Runtime.getRuntime().exec(new String[] { "open", outputFilename });
        }
        String lineSeparator = "\n";
        String expected = "digraph G" + lineSeparator + "{" + lineSeparator + "label = \"org.vizzini.tool.dependencyanalyzer.package0\";" + lineSeparator + "   package0 [label=\"package0\"];" + lineSeparator + "   package0_package00 [label=\"package0.package00\"];" + lineSeparator + "   package0_package00_package000 [label=\"package0.package00.package000\"];" + lineSeparator + "   package0_package00_package001 [label=\"package0.package00.package001\"];" + lineSeparator + "   package0_package00_package002 [label=\"package0.package00.package002\"];" + lineSeparator + "   package0_package01 [label=\"package0.package01\"];" + lineSeparator + "   package0_package01_package010 [label=\"package0.package01.package010\"];" + lineSeparator + "   package0_package01_package011 [label=\"package0.package01.package011\"];" + lineSeparator + "   package0_package02 [label=\"package0.package02\"];" + lineSeparator + "   package0_package02_package020 [label=\"package0.package02.package020\"];" + lineSeparator + "   package0_package00 -> package0;" + lineSeparator + "   package0_package00_package000 -> package0_package00;" + lineSeparator + "   package0_package00_package001 -> package0_package00;" + lineSeparator + "   package0_package00_package002 -> package0;" + lineSeparator + "   package0_package01 -> package0;" + lineSeparator + "   package0_package01_package010 -> package0_package01;" + lineSeparator + "   package0_package01_package011 -> package0_package01;" + lineSeparator + "   package0_package02 -> package0;" + lineSeparator + "   package0_package02_package020 -> package0_package02;" + lineSeparator + "}" + lineSeparator;
        assertEquals(expected, dotString);
    }

    /**
     * Test the <code>createDotString()</code> method.
     *
     * @throws  IOException  if there is an I/O problem.
     *
     * @since   v0.3
     */
    public void testCreateDotStringLowDetail() throws IOException {
        String baseDirectory = getBaseDirectory0();
        String basePackageDirectory = getBasePackageDirectory0();
        boolean isHighDetail = false;
        DependencyAnalyzer analyzer = new DependencyAnalyzer(baseDirectory, basePackageDirectory, isHighDetail);
        assertNotNull(analyzer);
        analyzer.analyze();
        String dotString = analyzer.createDotString();
        if (_isVerbose) {
            GraphvizRunner runner = new GraphvizRunner();
            String fileRoot = System.getProperty("user.dir");
            runner.run(dotString, fileRoot);
            String outputFilename = fileRoot + ".png";
            Runtime.getRuntime().exec(new String[] { "open", outputFilename });
        }
        String lineSeparator = "\n";
        String expected = "digraph G" + lineSeparator + "{" + lineSeparator + "label = \"org.vizzini.tool.dependencyanalyzer.package0\";" + lineSeparator + "   package0 [label=\"package0\"];" + lineSeparator + "   package0_package00 [label=\"package0.package00\"];" + lineSeparator + "   package0_package01 [label=\"package0.package01\"];" + lineSeparator + "   package0_package02 [label=\"package0.package02\"];" + lineSeparator + "   package0_package00 -> package0;" + lineSeparator + "   package0_package01 -> package0;" + lineSeparator + "   package0_package02 -> package0;" + lineSeparator + "}" + lineSeparator;
        assertEquals(expected, dotString);
    }

    /**
     * Test the <code>filter()</code> method.
     *
     * @since  v0.3
     */
    public void testFilter() {
        boolean isHighDetail = false;
        DependencyAnalyzer analyzer = new DependencyAnalyzer(getBaseDirectory0(), getBasePackageDirectory0(), isHighDetail);
        analyzer.analyze();
        Map<String, Set<String>> packagesToDependencies = analyzer.getPackageToDependencies();
        assertNotNull(packagesToDependencies);
        System.out.println("Before:");
        printPackagesToDependences(packagesToDependencies);
        Map<String, Set<String>> map = analyzer.filter(packagesToDependencies);
        System.out.println("After:");
        printPackagesToDependences(map);
        assertEquals(4, map.size());
    }

    /**
     * Test the <code>getDependencySetFor()</code> method.
     *
     * @since  v0.3
     */
    public void testGetDependencySetFor() {
        boolean isHighDetail = false;
        DependencyAnalyzer analyzer = new DependencyAnalyzer(getBaseDirectory1(), getBasePackageDirectory1(), isHighDetail);
        Set<String> set = analyzer.getDependencySetFor("test");
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    /**
     * Test the <code>isPackageDependentOn()</code> method.
     *
     * @since  v0.3
     */
    public void testIsPackageDependentOn() {
        boolean isHighDetail = false;
        DependencyAnalyzer analyzer = new DependencyAnalyzer(getBaseDirectory1(), getBasePackageDirectory1(), isHighDetail);
        assertTrue(analyzer.isPackageDependentOn("org.vizzini.game", "org.vizzini.math"));
        assertFalse(analyzer.isPackageDependentOn("org.vizzini.math", "org.vizzini.game"));
        assertFalse(analyzer.isPackageDependentOn("org.vizzini.ai", "org.vizzini.database"));
        assertFalse(analyzer.isPackageDependentOn("org.vizzini.ai", "org.vizzini.example"));
        assertFalse(analyzer.isPackageDependentOn("org.vizzini.ai", "org.vizzini.game"));
        assertTrue(analyzer.isPackageDependentOn("org.vizzini.ai.neuralnetwork", "org.vizzini.math"));
        assertFalse(analyzer.isPackageDependentOn("org.vizzini.ai.geneticprogramming", "org.vizzini.tool"));
        assertFalse(analyzer.isPackageDependentOn("org.vizzini.ai.geneticprogramming", "org.vizzini.ui"));
        assertTrue(analyzer.isPackageDependentOn("org.vizzini.ai.geneticprogramming", "org.vizzini.util"));
    }

    /**
     * Test the <code>reducePackage()</code> method.
     *
     * @since  v0.3
     */
    public void testReducePackage() {
        boolean isHighDetail = false;
        DependencyAnalyzer analyzer = new DependencyAnalyzer(getBaseDirectory0(), getBasePackageDirectory0(), isHighDetail);
        assertEquals("org.vizzini.game", analyzer.reducePackage("org.vizzini.game", 3));
        assertEquals("org.vizzini.game", analyzer.reducePackage("org.vizzini.game.boardgame", 3));
        assertEquals("org.vizzini.game", analyzer.reducePackage("org.vizzini.game.boardgame.chess", 3));
    }

    /**
     * @return  the base directory.
     *
     * @since   v0.3
     */
    private String getBaseDirectory0() {
        String userDir = getUserDir();
        return userDir + "/test-tool";
    }

    /**
     * @return  the base directory.
     *
     * @since   v0.3
     */
    private String getBaseDirectory1() {
        String userDir = getUserDir();
        return userDir + "/src";
    }

    /**
     * @return  the base package directory.
     *
     * @since   v0.3
     */
    private String getBasePackageDirectory0() {
        return "org/vizzini/tool/dependencyanalyzer/package0";
    }

    /**
     * @return  the base package directory.
     *
     * @since   v0.3
     */
    private String getBasePackageDirectory1() {
        return "org/vizzini";
    }

    /**
     * @return  the user directory.
     *
     * @since   v0.3
     */
    private String getUserDir() {
        String userDir = System.getProperty("user.dir");
        String key = "jmthompson";
        if (userDir.endsWith(key)) {
            userDir += "/Documents/SoftwareDev/JavaProjects/vizzini";
        }
        key = "/config";
        if (userDir.endsWith(key)) {
            int len = userDir.length() - key.length();
            userDir = userDir.substring(0, len);
        }
        return userDir;
    }

    /**
     * Print the given parameter.
     *
     * @param  title     Title.
     * @param  packages  Packages.
     *
     * @since  v0.3
     */
    private void printPackages(String title, Set<String> packages) {
        if (_isVerbose) {
            System.out.println("\n" + title + ":");
            int i = 0;
            Iterator<String> iter = packages.iterator();
            while (iter.hasNext()) {
                String aPackage = iter.next();
                System.out.println(i + " " + aPackage);
                i++;
            }
        }
    }

    /**
     * Print the given parameter.
     *
     * @param  packagesToDependencies  Map of package to dependencies.
     *
     * @since  v0.3
     */
    private void printPackagesToDependences(Map<String, Set<String>> packagesToDependencies) {
        if (_isVerbose) {
            System.out.println("\npackagesToDependencies:");
            int i = 0;
            Iterator<Entry<String, Set<String>>> iter = packagesToDependencies.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, Set<String>> entry = iter.next();
                String aPackage = entry.getKey();
                Set<String> set = entry.getValue();
                System.out.println(i + " " + aPackage + " " + set);
                i++;
            }
        }
    }

    /**
     * Print the given parameter.
     *
     * @param  packagesToUrl  Map of package to URL.
     *
     * @since  v0.3
     */
    private void printPackagesToUrl(Map<String, URL> packagesToUrl) {
        if (_isVerbose) {
            System.out.println("\npackagesToUrl:");
            int i = 0;
            Iterator<Entry<String, URL>> iter = packagesToUrl.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, URL> entry = iter.next();
                String aPackage = entry.getKey();
                URL url = entry.getValue();
                System.out.println(i + " " + aPackage + " " + url);
                i++;
            }
        }
    }
}
