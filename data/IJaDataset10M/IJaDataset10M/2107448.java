package com.meterware.httpunit;

import junit.framework.Test;
import junit.framework.TestSuite;
import java.net.URL;

/**
 * Verifies handling of URLs with odd features.
 * @author <a href="mailto:ddkilzer@users.sourceforge.net">David D. Kilzer</a>
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class NormalizeURLTest extends HttpUnitTest {

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(NormalizeURLTest.class);
    }

    public NormalizeURLTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testHostnameNoSlash() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name");
        assertEquals("URL", "http://host.name", request.getURL().toExternalForm());
    }

    public void testHostnamePortNoSlash() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name:80");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name:80");
    }

    public void testUsernameHostnameNoSlash() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://username@host.name");
        assertEquals("URL", request.getURL().toExternalForm(), "http://username@host.name");
    }

    public void testUsernamePasswordHostnameNoSlash() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://username:password@host.name");
        assertEquals("URL", request.getURL().toExternalForm(), "http://username:password@host.name");
    }

    public void testUsernameHostnamePortNoSlash() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://username@host.name:80");
        assertEquals("URL", request.getURL().toExternalForm(), "http://username@host.name:80");
    }

    public void testUsernamePasswordHostnamePortNoSlash() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://username:password@host.name:80");
        assertEquals("URL", request.getURL().toExternalForm(), "http://username:password@host.name:80");
    }

    public void testHostnameSlash() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/");
    }

    public void testHostnamePortSlash() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name:80/");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name:80/");
    }

    public void testUsernameHostnameSlash() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://username@host.name/");
        assertEquals("URL", request.getURL().toExternalForm(), "http://username@host.name/");
    }

    public void testUsernamePasswordHostnameSlash() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://username:password@host.name/");
        assertEquals("URL", request.getURL().toExternalForm(), "http://username:password@host.name/");
    }

    public void testUsernameHostnamePortSlash() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://username@host.name:80/");
        assertEquals("URL", request.getURL().toExternalForm(), "http://username@host.name:80/");
    }

    public void testUsernamePasswordHostnamePortSlash() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://username:password@host.name:80/");
        assertEquals("URL", request.getURL().toExternalForm(), "http://username:password@host.name:80/");
    }

    public void testHostnameFile() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/file.html");
    }

    public void testHostnameDirectoryFile() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/directory/file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory/file.html");
    }

    public void testHostnameDirectory1Directory2File() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/directory1/directory2/file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory1/directory2/file.html");
    }

    public void testHostnameDirectory() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/directory/");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory/");
    }

    public void testHostnameDirectory1Directory2() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/directory1/directory2/");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory1/directory2/");
    }

    public void testTortureHostnameDotFile() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/./file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/file.html");
    }

    public void testTortureHostnameDotDirectoryFile() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/./directory/file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory/file.html");
    }

    public void testTortureHostnameDotDirectoryDotFile() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/./directory/./file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory/file.html");
    }

    public void testTortureHostnameDotDirectoryDotDotFile() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/./directory/../file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/file.html");
    }

    public void testTortureHostnameDotDirectory1Directory2File() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/./directory1/directory2/file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory1/directory2/file.html");
    }

    public void testTortureHostnameDotDirectory1DotDirectory2File() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/./directory1/./directory2/file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory1/directory2/file.html");
    }

    public void testTortureHostnameDotDirectory1DotDirectory2DotFile() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/./directory1/./directory2/./file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory1/directory2/file.html");
    }

    public void testTortureHostnameDirectory1Directory2File() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/directory1/directory2/file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory1/directory2/file.html");
    }

    public void testTortureHostnameDirectory1DotDotDirectory2File() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/directory1/../directory2/file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory2/file.html");
    }

    public void testTortureHostnameDirectory1DotDotDirectory2DotDotFile() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/directory1/../directory2/../file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/file.html");
    }

    public void testTortureHostnameDirectory1Directory2DotDotDotDotFile() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/directory1/directory2/../../file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/file.html");
    }

    public void testRelativePathDotDotFile() throws Exception {
        WebRequest request = new GetMethodWebRequest(new URL("http://host.name/directory1/file.html"), "../directory2/file.html");
        assertEquals("URL", "http://host.name/directory2/file.html", request.getURL().toExternalForm());
    }

    public void testHostnameSlash1File() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name//file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/file.html");
    }

    public void testHostnameSlash2File() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name///file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/file.html");
    }

    public void testHostnameSlash3File() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name////file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/file.html");
    }

    public void testHostnameSlash1DirectoryFile() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/directory//file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory/file.html");
    }

    public void testHostnameSlash2DirectoryFile() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/directory///file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory/file.html");
    }

    public void testHostnameSlash3DirectoryFile() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name/directory////file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory/file.html");
    }

    public void testHostnameSlash1Directory1Directory2File() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name//directory1//directory2//file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory1/directory2/file.html");
    }

    public void testHostnameSlash2Directory1Directory2File() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name///directory1///directory2///file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory1/directory2/file.html");
    }

    public void testHostnameSlash3Directory1Directory2File() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name////directory1////directory2////file.html");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory1/directory2/file.html");
    }

    public void testHostnameSlash1Directory() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name//directory//");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory/");
    }

    public void testHostnameSlash2Directory() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name///directory///");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory/");
    }

    public void testHostnameSlash3Directory() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name////directory////");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory/");
    }

    public void testHostnameSlash1Directory1Directory2() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name//directory1//directory2//");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory1/directory2/");
    }

    public void testHostnameSlash2Directory1Directory2() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name///directory1///directory2///");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory1/directory2/");
    }

    public void testHostnameSlash3Directory1Directory2() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host.name////directory1////directory2////");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host.name/directory1/directory2/");
    }

    public void testPathElementLeadingDot() throws Exception {
        WebRequest request = new GetMethodWebRequest("http://host/context/.src/page");
        assertEquals("URL", request.getURL().toExternalForm(), "http://host/context/.src/page");
    }

    public void testUrlAsParameter() throws Exception {
        String desiredUrl = "http://localhost:3333/composite/addobserver?url=http://localhost:8081/";
        WebRequest request = new GetMethodWebRequest(desiredUrl);
        assertEquals("URL", desiredUrl, request.getURL().toExternalForm());
    }

    public void testSlashesInParameter() throws Exception {
        String desiredUrl = "http://localhost:8888/bug2295681/TestServlet?abc=abc&aaa=%%%&bbb=---%2d%2F%*%aa&ccc=yahoo@yahoo.com&ddd=aaa/../../&eee=/.";
        WebRequest request = new GetMethodWebRequest(desiredUrl);
        assertEquals("URL", desiredUrl, request.getURL().toExternalForm());
    }
}
