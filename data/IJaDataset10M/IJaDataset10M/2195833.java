package net.jtools.ant.classloader;

import java.io.*;
import java.net.MalformedURLException;
import junit.framework.TestCase;
import net.jtools.ant.classloader.SimpleURLUtils;
import org.apache.tools.ant.taskdefs.condition.Os;

/**
 * Tests for org.apache.tools.ant.util.URLUtils.
 *
 */
public class URLUtilsTest extends TestCase {

    private File removeThis;

    private String current;

    private String root;

    public static final boolean isUnix = Os.isFamily("unix");

    public static boolean isWindows = Os.isFamily("windows");

    public URLUtilsTest(String name) {
        super(name);
    }

    public void setUp() {
        current = new File(".").getAbsolutePath();
        root = new File(File.separator).getAbsolutePath().toUpperCase();
    }

    public void tearDown() {
        if (removeThis != null && removeThis.exists()) {
            removeThis.delete();
        }
    }

    public void testCreateURL(String test, String expected, String fileOrURL) throws MalformedURLException {
        assertEquals(test, expected, SimpleURLUtils.getURLUtils().createURL(fileOrURL).toString());
    }

    public void testNormalize(String test, String expected, String fileOrURL) throws MalformedURLException {
        assertEquals(test, expected, SimpleURLUtils.getURLUtils().normalize(fileOrURL));
    }

    public void testResolve(String test, String expected, String fileOrURL, File dir) throws MalformedURLException {
        assertEquals(test, expected, SimpleURLUtils.getURLUtils().resolve(fileOrURL, dir));
    }

    public void testIsURL() {
        SimpleURLUtils utils = SimpleURLUtils.getURLUtils();
        assertEquals(true, utils.isURL("http://ant.apache.org"));
        assertEquals(true, utils.isURL("file:/a/b"));
        assertEquals(true, utils.isURL("file://a/b"));
        assertEquals(true, utils.isURL("file:///a/b"));
        assertEquals(true, utils.isURL("file:////a/b"));
        assertEquals(true, utils.isURL("file:/C:/a/b"));
        assertEquals(true, utils.isURL("file://C:/a/b"));
        assertEquals(true, utils.isURL("file:///C:/a/b"));
        assertEquals(false, utils.isURL("C:/a/b"));
        assertEquals(false, utils.isURL("/a/b"));
        assertEquals(false, utils.isURL("a/b"));
        assertEquals(false, utils.isURL("//a/b"));
        assertEquals(false, utils.isURL("///a/b"));
        assertEquals(false, utils.isURL("."));
    }

    public void testIsFileOrFileURL() {
        SimpleURLUtils utils = SimpleURLUtils.getURLUtils();
        assertEquals(false, utils.isFileOrFileURL("http://ant.apache.org"));
        assertEquals(true, utils.isFileOrFileURL("file:/a/b"));
        assertEquals(true, utils.isFileOrFileURL("file://a/b"));
        assertEquals(true, utils.isFileOrFileURL("file:///a/b"));
        assertEquals(true, utils.isFileOrFileURL("file:////a/b"));
        assertEquals(true, utils.isFileOrFileURL("file:/C:/a/b"));
        assertEquals(true, utils.isFileOrFileURL("file://C:/a/b"));
        assertEquals(true, utils.isFileOrFileURL("file:///C:/a/b"));
        assertEquals(true, utils.isFileOrFileURL("C:/a/b"));
        assertEquals(true, utils.isFileOrFileURL("/a/b"));
        assertEquals(true, utils.isFileOrFileURL("a/b"));
        assertEquals(true, utils.isFileOrFileURL("//a/b"));
        assertEquals(true, utils.isFileOrFileURL("///a/b"));
        assertEquals(true, utils.isFileOrFileURL("."));
    }

    public void testIsAbsolute() throws MalformedURLException {
        SimpleURLUtils utils = SimpleURLUtils.getURLUtils();
        assertEquals("2", true, utils.isAbsolute("file:/a/b"));
        assertEquals("3", true, utils.isAbsolute("file://a/b"));
        assertEquals("4", true, utils.isAbsolute("file:///a/b"));
        assertEquals("5", true, utils.isAbsolute("file:////a/b"));
        assertEquals("6", true, utils.isAbsolute("file:/C:/a/b"));
        assertEquals("7", true, utils.isAbsolute("file://C:/a/b"));
        assertEquals("8", true, utils.isAbsolute("file:///C:/a/b"));
        assertEquals("9", true, utils.isAbsolute("C:/a/b"));
        assertEquals("10", File.separatorChar == '/', utils.isAbsolute("/a/b"));
        assertEquals("11", false, utils.isAbsolute("a/b"));
        assertEquals("12", true, utils.isAbsolute("//a/b"));
        assertEquals("13", true, utils.isAbsolute("///a/b"));
        assertEquals("14", false, utils.isAbsolute("."));
    }

    public void testCreateURL() throws MalformedURLException {
        SimpleURLUtils utils = SimpleURLUtils.getURLUtils();
        String rootUrl = utils.createURL(root).toString();
        String currentUrl = utils.createURL(current).toString();
        testCreateURL("http", "http://ant.apache.org", "http://ant.apache.org");
        testCreateURL("file:1", "file:/a/b", "file:/a/b");
        testCreateURL("file:2", "file://a/b", "file://a/b");
        testCreateURL("file:3", "file:/a/b", "file:///a/b");
        testCreateURL("file:4", "file://a/b", "file:////a/b");
        testCreateURL("file:win", "file:C:/a/b", "file:C:/a/b");
        testCreateURL("file:win1", "file:/C:/a/b", "file:/C:/a/b");
        testCreateURL("file:win2", "file://C:/a/b", "file://C:/a/b");
        testCreateURL("file:win3", "file:/C:/a/b", "file:///C:/a/b");
        testCreateURL("current", currentUrl, ".");
        testCreateURL("rel", currentUrl + "a/b", "a/b");
        testCreateURL("unix", rootUrl + "a/b", "/a/b");
        if (isWindows) {
            testCreateURL("win", "file:/C:/a/b", "C:/a/b");
            testCreateURL("unc", "file://a/b", "//a/b");
        }
    }

    private String convertSlashes(String x) {
        return x.replace('/', File.separatorChar);
    }

    public void testNormalize() throws MalformedURLException {
        testNormalize("http", "http://ant.apache.org", "http://ant.apache.org");
        testNormalize("file:1", "file:/a/b", "file:/a/b");
        testNormalize("file:2", "file://a/b", "file://a/b");
        testNormalize("file:3", "file:/a/b", "file:///a/b");
        testNormalize("file:4", "file://a/b", "file:////a/b");
        testNormalize("file:win", "file:C:/a/b", "file:C:/a/b");
        testNormalize("file:win1", "file:/C:/a/b", "file:/C:/a/b");
        testNormalize("file:win2", "file://C:/a/b", "file://C:/a/b");
        testNormalize("file:win3", "file:/C:/a/b", "file:///C:/a/b");
        testNormalize("file: with dot dot 1", "file:/C:/a", "file:/C:/a/b/..");
        testNormalize("file: with dot dot 2", "file:/C:/a/", "file:/C:/a/b/../");
        testNormalize("file: with dot 1", "file:/C:/a", "file:/C:/a/.");
        testNormalize("file: with dot 2", "file:/C:/a/", "file:/C:/a/./");
        testNormalize("file: with dot dot 3", "file:/C:/", "file:/C:/a/..");
        testNormalize("file: with dot dot 4", "file:/C:/", "file:/C:/a/../");
        testNormalize("file: with dot 3", "file:/C:/a", "file:/C:/a/.");
        testNormalize("file: with dot 4", "file:/C:/a/", "file:/C:/a/./");
        if (isWindows) {
            testNormalize("file with dot", "C:\\a", "C:/a/.");
            testNormalize("file with dot dot", "C:\\", "C:/a/..");
            testNormalize("win", convertSlashes("C:/a/b"), "C:/a/b");
            testNormalize("unc", convertSlashes("//a/b"), "//a/b");
        }
        if (isUnix) {
            testNormalize("unix", convertSlashes("/a/b"), "/a/b");
        }
    }

    public void testResolve() throws MalformedURLException {
        File dir = new File("C:/a");
        String dirUrl = SimpleURLUtils.getURLUtils().createURL(dir.getAbsolutePath()).toString();
        testResolve("http", "http://ant.apache.org", "http://ant.apache.org", dir);
        testResolve("file:1", "file:/a/b", "file:/a/b", dir);
        testResolve("file:2", "file://a/b", "file://a/b", dir);
        testResolve("file:3", "file:/a/b", "file:///a/b", dir);
        testResolve("file:4", "file://a/b", "file:////a/b", dir);
        testResolve("file:win1", "file:/C:/a/b", "file:/C:/a/b", dir);
        testResolve("file:win2", "file://C:/a/b", "file://C:/a/b", dir);
        testResolve("file:win3", "file:/C:/a/b", "file:///C:/a/b", dir);
        testResolve("rel 1", dirUrl + "/a", "a", dir);
        if (isWindows) {
            testResolve("unc", "file://a/b", "//a/b", dir);
        }
    }
}
