package net.sourceforge.gedapi.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.gedapi.test.AbstractTest;
import net.sourceforge.gedapi.util.Environment;
import net.sourceforge.gedapi.util.GLinkURL;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import junit.framework.Test;
import junit.framework.TestSuite;

public class GedapiServletTest extends AbstractTest {

    private static final Logger LOG = Logger.getLogger(GedapiServletTest.class.getName());

    static int testIDCounter = 0;

    final GedapiServlet servlet = new GedapiServlet();

    public GedapiServletTest(String testName) throws ServletException {
        super(testName);
        LOG.finest("Running GedapiServletTest... Please be patient...");
        Mockery context = new Mockery();
        final ServletConfig servletConfig = context.mock(ServletConfig.class);
        context.checking(new Expectations() {

            {
            }
        });
        servlet.init(servletConfig);
        context.assertIsSatisfied();
    }

    public static Test suite() {
        return new TestSuite(GedapiServletTest.class);
    }

    @Override
    public void runAllTests() throws Exception {
        testLoadGedcom();
        testFileForcedNetworkReload();
        testGedcomSearch();
        testGedapiServlet();
    }

    public void testGedcomSearch() throws InterruptedException, UnsupportedEncodingException, IOException {
        List<Runnable> threads = new ArrayList<Runnable>();
        File gedcomFile = new File(Environment.getProperty("CentralSiteRoot") + "/http~~~_3~~~1localhost~~~_38081/gedapi/" + GLinkURL.hashFilename("export.ged"));
        long ifNoneMatch = -1;
        long lastModified = gedcomFile.lastModified();
        String jsonObjectString = "{\"gedcomurl\":\"http://localhost:8081/gedapi/export.ged\",\"glinkURLs\":[\"Matthew Ammon /Jarvis/\",\"http://localhost:8081/gedapi/export.ged/I3\",\"Joshua Seth /Jarvis/\",\"http://localhost:8081/gedapi/export.ged/I4\",\"Norman Chris /Jarvis/\",\"http://localhost:8081/gedapi/export.ged/I1\",\"Elizabeth /Rhoades/\",\"http://localhost:8081/gedapi/export.ged/I2\",\"Elizabeth /Rhoades/\",\"http://localhost:8081/gedapi/export.ged/I2\",\"Nathaniel Aaron /Jarvis/\",\"http://localhost:8081/gedapi/export.ged/I5\"],\"tabIndex\":\"4\"}";
        String eTag = GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonObjectString.length() + "-" + String.valueOf(lastModified) + "\"";
        threads.addAll(startThreads("/gedapi/gedcom/4/" + URLEncoder.encode("Elizabeth Rhoades~~~~~~~~INDI", "UTF-8") + "/" + URLEncoder.encode("http://localhost:8081/gedapi/export.ged/I1", "UTF-8"), jsonObjectString, ifNoneMatch, lastModified, eTag, "true", "GET", null, (oneAtaTime ? 1 : 30)));
        Thread.sleep(300);
        for (Runnable thread : threads) {
            if (thread instanceof Thread) {
                ((Thread) thread).join();
            }
        }
    }

    public void testFileForcedNetworkReload() throws InterruptedException, UnsupportedEncodingException, IOException {
        List<Runnable> threads = new ArrayList<Runnable>();
        File gedcomFile = new File(Environment.getProperty("CentralSiteRoot") + "/http~~~_3~~~1localhost~~~_38081/gedapi/" + GLinkURL.hashFilename("export.ged"));
        long ifNoneMatch = gedcomFile.lastModified() - 1000;
        long lastModified = gedcomFile.lastModified();
        String jsonObjectString = "{\"gedcomurl\":\"http://localhost:8081/gedapi/export.ged\",\"indi\":{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I1\",\"glinks\":[{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob James\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bobby\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Joe\"}],\"id\":\"I1\",\"name\":\"N.J. - I1 (possibly living data)\"},\"marriages\":[{\"children\":[{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I3\",\"glinks\":[],\"id\":\"I3\",\"name\":\"M.J. - I3 (possibly living data)\"},{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I4\",\"glinks\":[],\"id\":\"I4\",\"name\":\"J.J. - I4 (possibly living data)\"},{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I5\",\"glinks\":[{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob James\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bobby\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Joe\"}],\"id\":\"I5\",\"name\":\"N.J. - I5 (possibly living data)\"}],\"marriageDate\":\"19 DEC 1992\",\"spouse\":{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I2\",\"glinks\":[],\"id\":\"I2\",\"name\":\"E.R. - I2 (possibly living data)\"}}],\"parents\":[{\"husband\":{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I6\",\"glinks\":[{\"date\":\"28 JAN 2009\",\"fromRelation\":\"GLR00010.GLM01003\",\"toRelation\":\"GLR00012.GLM01003\",\"toURL\":\"http://localhost:8081/gedapi/export.ged/I2\",\"toURLName\":\"Elizabeth Rhoades\"}],\"id\":\"I6\",\"name\":\"W.J. - I6 (possibly living data)\"},\"siblingIndex\":0,\"siblings\":[{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I1\",\"glinks\":[{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob James\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bobby\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Joe\"}],\"id\":\"I1\",\"name\":\"N.J. - I1 (possibly living data)\"}],\"wife\":{\"birthDate\":\"23 Jan 1942\",\"deathDate\":\"24 Jul 2003\",\"gender\":1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I7\",\"glinks\":[{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob James\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bobby\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Joe\"}],\"id\":\"I7\",\"name\":\"Dorothy Rita Haws\"}}],\"tabIndex\":\"0\"}";
        File forceNetworkReload = new File(Environment.getProperty("CentralSiteRoot") + "/http~~~_3~~~1localhost~~~_38081/gedapi/" + GLinkURL.hashFilename("export.ged.I1." + GLinkURL.JSON_ETAG_VERSION + ".force.network.reload"));
        forceNetworkReload.getParentFile().mkdirs();
        forceNetworkReload.createNewFile();
        LOG.finest("Checking to see if file '" + forceNetworkReload.getAbsolutePath() + "' exists!");
        assertTrue(forceNetworkReload.exists());
        String eTag = GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonObjectString.length() + "-" + String.valueOf(lastModified) + "\"";
        threads.addAll(startThreads("/gedapi/gedcom/0/" + URLEncoder.encode("http://localhost:8081/gedapi/export.ged/I1", "UTF-8"), jsonObjectString, ifNoneMatch, lastModified, eTag, "true", "GET", null, 1));
        Thread.sleep(300);
        for (Runnable thread : threads) {
            if (thread instanceof Thread) {
                ((Thread) thread).join();
            }
        }
        assertFalse(forceNetworkReload.exists());
    }

    public void testLoadGedcom() throws InterruptedException, UnsupportedEncodingException, IOException {
        List<Runnable> threads = new ArrayList<Runnable>();
        File gedcomFile = new File(Environment.getProperty("CentralSiteRoot") + "/http~~~_3~~~1freespace.virgin.net/edwy.harling/millerdes/" + GLinkURL.hashFilename("millerdes.ged"));
        long ifNoneMatch = gedcomFile.lastModified() - 1000;
        long lastModified = gedcomFile.lastModified();
        String jsonObjectString = "{\"gedcomurl\":\"http://freespace.virgin.net/edwy.harling/millerdes/millerdes.ged\",\"indi\":{\"birthDate\":\"1872\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://freespace.virgin.net/edwy.harling/millerdes/millerdes.ged/I61\",\"glinks\":[],\"id\":\"I61\",\"name\":\"Joseph OWEN\"},\"marriages\":[{\"children\":[{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://freespace.virgin.net/edwy.harling/millerdes/millerdes.ged/I894\",\"glinks\":[],\"id\":\"I894\",\"name\":\"John OWEN\"},{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":1,\"glink\":\"http://freespace.virgin.net/edwy.harling/millerdes/millerdes.ged/I895\",\"glinks\":[],\"id\":\"I895\",\"name\":\"Elsie OWEN\"},{\"birthDate\":\"1895\",\"deathDate\":\"\",\"gender\":1,\"glink\":\"http://freespace.virgin.net/edwy.harling/millerdes/millerdes.ged/I893\",\"glinks\":[],\"id\":\"I893\",\"name\":\"Lilly OWEN\"}],\"marriageDate\":\"\",\"spouse\":{\"birthDate\":\"1872\",\"deathDate\":\"\",\"gender\":1,\"glink\":\"http://freespace.virgin.net/edwy.harling/millerdes/millerdes.ged/I889\",\"glinks\":[],\"id\":\"I889\",\"name\":\"Mary Ann MILLER\"}}],\"parents\":[],\"tabIndex\":\"0\"}";
        ;
        String eTag = GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonObjectString.length() + "-" + String.valueOf(lastModified) + "\"";
        threads.addAll(startThreads("/gedapi/gedcom/0/" + URLEncoder.encode("http://freespace.virgin.net/edwy.harling/millerdes/millerdes.ged", "UTF-8"), jsonObjectString, ifNoneMatch, lastModified, eTag, "true", "GET", null, 1));
        Thread.sleep(300);
        for (Runnable thread : threads) {
            if (thread instanceof Thread) {
                ((Thread) thread).join();
            }
        }
    }

    public void testGedapiServlet() throws InterruptedException, UnsupportedEncodingException, IOException {
        List<Runnable> threads = new ArrayList<Runnable>();
        File gedcomFile = new File(Environment.getProperty("CentralSiteRoot") + "/http~~~_3~~~1localhost~~~_38081/gedapi/" + GLinkURL.hashFilename("export.ged"));
        long ifNoneMatch = -1;
        long lastModified = gedcomFile.lastModified();
        String jsonObjectString = "{\"error\":\"[1001] java.lang.RuntimeException: java.lang.RuntimeException: [1005] Connect error at URL: http://localhost:8081/gedapi/export_it.ged : java.net.ConnectException: Connection refused";
        if (File.separatorChar == '\\') {
            jsonObjectString += ": connect\"}";
        } else {
            jsonObjectString += "\"}";
        }
        String eTag = GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonObjectString.length() + "-" + String.valueOf(lastModified) + "\"";
        threads.addAll(startThreads("/gedapi/gedcom/2/" + URLEncoder.encode("http://localhost:8081/gedapi/export_it.ged/I1", "UTF-8"), jsonObjectString, ifNoneMatch, lastModified, eTag, "false", "GET", "connection refused", 1));
        ifNoneMatch = -1;
        lastModified = gedcomFile.lastModified();
        jsonObjectString = "{\"error\":\"[1001] java.lang.RuntimeException: java.lang.RuntimeException: [1005] Connect error at URL: http://zippidydoda:8080/gedapi/export1.ged : java.net.UnknownHostException: zippidydoda\"}";
        eTag = GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonObjectString.length() + "-" + String.valueOf(lastModified) + "\"";
        threads.addAll(startThreads("/gedapi/gedcom/2/" + URLEncoder.encode("http://zippidydoda:8080/gedapi/export1.ged/I1", "UTF-8"), jsonObjectString, ifNoneMatch, lastModified, eTag, "false", "GET", "bad host", 1));
        ifNoneMatch = -1;
        lastModified = gedcomFile.lastModified();
        jsonObjectString = "{\"error\":\"[1000] This servlet does not support the HTTP POST method.\"}";
        eTag = GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonObjectString.length() + "-" + String.valueOf(lastModified) + "\"";
        threads.addAll(startThreads("/gedapi/gedcom/2/" + URLEncoder.encode("http://localhost:8081/gedapi/export.ged/I1", "UTF-8"), jsonObjectString, ifNoneMatch, lastModified, eTag, "false", "POST", "http post not allowed", 1));
        ifNoneMatch = -1;
        File cachedJsonFile = new File(Environment.getProperty("CentralSiteRoot") + "/http~~~_3~~~1localhost~~~_38081/gedapi/" + GLinkURL.hashFilename("export.ged.I1.2." + GLinkURL.JSON_ETAG_VERSION + ".json"));
        lastModified = cachedJsonFile.lastModified();
        jsonObjectString = "{\"gedcomurl\":\"http://localhost:8081/gedapi/export.ged\",\"tabIndex\":\"2\"}";
        eTag = GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonObjectString.length() + "-" + String.valueOf(lastModified) + "\"";
        threads.addAll(startThreads("/gedapi/gedcom/2/" + URLEncoder.encode("http://localhost:8081/gedapi/export.ged/I1", "UTF-8"), jsonObjectString, ifNoneMatch, lastModified, eTag, "false", "GET", null, (oneAtaTime ? 1 : 12)));
        ifNoneMatch = 0;
        lastModified = gedcomFile.lastModified();
        jsonObjectString = "";
        eTag = GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonObjectString.length() + "-" + String.valueOf(ifNoneMatch) + "\"";
        threads.addAll(startThreads("/gedapi/gedcom/2/" + URLEncoder.encode("http://localhost:8081/gedapi/export.ged/I1", "UTF-8"), jsonObjectString, ifNoneMatch, lastModified, eTag, "false", "GET", null, (oneAtaTime ? 1 : 13)));
        ifNoneMatch = 0;
        lastModified = gedcomFile.lastModified();
        jsonObjectString = "{\"gedcomurl\":\"http://localhost:8081/gedapi/export.ged\",\"tabIndex\":\"2\"}";
        eTag = GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonObjectString.length() + "-" + String.valueOf(lastModified) + "\"";
        threads.addAll(startThreads("/gedapi/gedcom/2/" + URLEncoder.encode("http://localhost:8081/gedapi/export.ged/I1", "UTF-8"), jsonObjectString, ifNoneMatch, lastModified, eTag, "true", "GET", null, (oneAtaTime ? 1 : 10)));
        Thread.sleep(1500);
        ifNoneMatch = gedcomFile.lastModified();
        lastModified = gedcomFile.lastModified();
        jsonObjectString = "";
        eTag = GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonObjectString.length() + "-" + String.valueOf(lastModified) + "\"";
        threads.addAll(startThreads("/gedapi/gedcom/0/" + URLEncoder.encode("http://localhost:8081/gedapi/export.ged/I1", "UTF-8"), jsonObjectString, ifNoneMatch, lastModified, eTag, "false", "GET", null, (oneAtaTime ? 1 : 40)));
        ifNoneMatch = gedcomFile.lastModified() - 1000;
        lastModified = gedcomFile.lastModified();
        jsonObjectString = "{\"gedcomurl\":\"http://localhost:8081/gedapi/export.ged\",\"indi\":{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I1\",\"glinks\":[{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob James\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bobby\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Joe\"}],\"id\":\"I1\",\"name\":\"N.J. - I1 (possibly living data)\"},\"marriages\":[{\"children\":[{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I3\",\"glinks\":[],\"id\":\"I3\",\"name\":\"M.J. - I3 (possibly living data)\"},{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I4\",\"glinks\":[],\"id\":\"I4\",\"name\":\"J.J. - I4 (possibly living data)\"},{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I5\",\"glinks\":[{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob James\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bobby\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Joe\"}],\"id\":\"I5\",\"name\":\"N.J. - I5 (possibly living data)\"}],\"marriageDate\":\"19 DEC 1992\",\"spouse\":{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I2\",\"glinks\":[],\"id\":\"I2\",\"name\":\"E.R. - I2 (possibly living data)\"}}],\"parents\":[{\"husband\":{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I6\",\"glinks\":[{\"date\":\"28 JAN 2009\",\"fromRelation\":\"GLR00010.GLM01003\",\"toRelation\":\"GLR00012.GLM01003\",\"toURL\":\"http://localhost:8081/gedapi/export.ged/I2\",\"toURLName\":\"Elizabeth Rhoades\"}],\"id\":\"I6\",\"name\":\"W.J. - I6 (possibly living data)\"},\"siblingIndex\":0,\"siblings\":[{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I1\",\"glinks\":[{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob James\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bobby\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Joe\"}],\"id\":\"I1\",\"name\":\"N.J. - I1 (possibly living data)\"}],\"wife\":{\"birthDate\":\"23 Jan 1942\",\"deathDate\":\"24 Jul 2003\",\"gender\":1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I7\",\"glinks\":[{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob James\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bobby\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Joe\"}],\"id\":\"I7\",\"name\":\"Dorothy Rita Haws\"}}],\"tabIndex\":\"0\"}";
        eTag = GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonObjectString.length() + "-" + String.valueOf(lastModified) + "\"";
        threads.addAll(startThreads("/gedapi/gedcom/0/" + URLEncoder.encode("http://localhost:8081/gedapi/export.ged/I1", "UTF-8"), jsonObjectString, ifNoneMatch, lastModified, eTag, "true", "GET", null, (oneAtaTime ? 1 : 40)));
        ifNoneMatch = gedcomFile.lastModified() - 1000;
        lastModified = gedcomFile.lastModified();
        jsonObjectString = "{\"gedcomurl\":\"http://localhost:8081/gedapi/export.ged\",\"indi\":{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I2\",\"glinks\":[],\"id\":\"I2\",\"name\":\"E.R. - I2 (possibly living data)\"},\"marriages\":[{\"children\":[{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I3\",\"glinks\":[],\"id\":\"I3\",\"name\":\"M.J. - I3 (possibly living data)\"},{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I4\",\"glinks\":[],\"id\":\"I4\",\"name\":\"J.J. - I4 (possibly living data)\"},{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I5\",\"glinks\":[{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob James\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bobby\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Joe\"}],\"id\":\"I5\",\"name\":\"N.J. - I5 (possibly living data)\"}],\"marriageDate\":\"19 DEC 1992\",\"spouse\":{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I1\",\"glinks\":[{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob James\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bobby\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Joe\"}],\"id\":\"I1\",\"name\":\"N.J. - I1 (possibly living data)\"}}],\"parents\":[{\"husband\":{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I52\",\"glinks\":[],\"id\":\"I52\",\"name\":\"R.R. - I52 (possibly living data)\"},\"siblingIndex\":0,\"siblings\":[{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I2\",\"glinks\":[],\"id\":\"I2\",\"name\":\"E.R. - I2 (possibly living data)\"}],\"wife\":{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I53\",\"glinks\":[],\"id\":\"I53\",\"name\":\"D.M. - I53 (possibly living data)\"}}],\"tabIndex\":\"0\"}";
        eTag = GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonObjectString.length() + "-" + String.valueOf(lastModified) + "\"";
        threads.addAll(startThreads("/gedapi/gedcom/0/" + URLEncoder.encode("http://localhost:8081/gedapi/export.ged/I2", "UTF-8"), jsonObjectString, ifNoneMatch, lastModified, eTag, "true", "GET", null, (oneAtaTime ? 1 : 40)));
        ifNoneMatch = gedcomFile.lastModified() - 1000;
        lastModified = gedcomFile.lastModified();
        jsonObjectString = "{\"gedcomurl\":\"http://localhost:8081/gedapi/export.ged\",\"indi\":{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I5\",\"glinks\":[{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob James\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bobby\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Joe\"}],\"id\":\"I5\",\"name\":\"N.J. - I5 (possibly living data)\"},\"marriages\":[],\"parents\":[{\"husband\":{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I1\",\"glinks\":[{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob James\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bobby\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Joe\"}],\"id\":\"I1\",\"name\":\"N.J. - I1 (possibly living data)\"},\"siblingIndex\":2,\"siblings\":[{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I3\",\"glinks\":[],\"id\":\"I3\",\"name\":\"M.J. - I3 (possibly living data)\"},{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I4\",\"glinks\":[],\"id\":\"I4\",\"name\":\"J.J. - I4 (possibly living data)\"},{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":-1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I5\",\"glinks\":[{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bob James\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Bobby\"},{\"date\":\"2008-09-11T00:00:00+0000\",\"fromRelation\":\"GLR00001\",\"toRelation\":\"GLR00001\",\"toURL\":\"http://localhost:8081/gedapi/export1.ged/I1\",\"toURLName\":\"Joe\"}],\"id\":\"I5\",\"name\":\"N.J. - I5 (possibly living data)\"}],\"wife\":{\"birthDate\":\"\",\"deathDate\":\"\",\"gender\":1,\"glink\":\"http://localhost:8081/gedapi/export.ged/I2\",\"glinks\":[],\"id\":\"I2\",\"name\":\"E.R. - I2 (possibly living data)\"}}],\"tabIndex\":\"0\"}";
        eTag = GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonObjectString.length() + "-" + String.valueOf(lastModified) + "\"";
        threads.addAll(startThreads("/gedapi/gedcom/0/" + URLEncoder.encode("http://localhost:8081/gedapi/export.ged/I5", "UTF-8"), jsonObjectString, ifNoneMatch, lastModified, eTag, "true", "GET", null, 1));
        Thread.sleep(1000);
        for (Runnable thread : threads) {
            if (thread instanceof Thread) {
                ((Thread) thread).join();
            }
        }
    }

    public List<Runnable> startThreads(String uri, String jsonResponse, long ifNoneMatch, long lastModified, String eTag, String xNetworkReload, String requestMethod, String errorMessage, int threadCount) throws InterruptedException, IOException {
        int testID = testIDCounter++;
        Runnable[] threads = new Runnable[threadCount];
        for (int i = 0; i < threads.length; ++i) {
            DoGetRunner runner = new DoGetRunner(uri, jsonResponse, ifNoneMatch, lastModified, eTag, xNetworkReload, requestMethod, errorMessage, testID);
            if (oneAtaTime) {
                threads[i] = runner;
            } else {
                threads[i] = new Thread(runner);
            }
        }
        for (Runnable thread : threads) {
            if (thread instanceof Thread) {
                ((Thread) thread).start();
            } else {
                thread.run();
                if (shouldBreak()) {
                    System.exit(1);
                }
            }
        }
        return Arrays.asList(threads);
    }

    class DoGetRunner implements Runnable {

        public String uri;

        public String jsonResponse;

        public long ifNoneMatch;

        public long lastModified;

        public String eTag;

        public String xNetworkReload;

        public String requestMethod;

        public String errorMessage;

        public int testID;

        public DoGetRunner(String uri, String jsonResponse, long ifNoneMatch, long lastModified, String eTag, String xNetworkReload, String requestMethod, String errorMessage, int testID) {
            this.uri = uri;
            this.jsonResponse = jsonResponse;
            this.ifNoneMatch = ifNoneMatch;
            this.lastModified = lastModified;
            this.eTag = eTag;
            this.xNetworkReload = xNetworkReload;
            this.requestMethod = requestMethod;
            this.errorMessage = errorMessage;
            this.testID = testID;
        }

        public void run() {
            try {
                handleDoGet(uri, jsonResponse, ifNoneMatch, lastModified, eTag, xNetworkReload, requestMethod, errorMessage, testID);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void handleDoGet(final String requestURI, final String jsonResponse, final long ifNoneMatch, final long lastModified, final String eTag, final String xNetworkReload, final String requestMethod, final String errorMessage, final int testID) throws IOException, ServletException {
        Mockery context = new Mockery();
        final HttpServletRequest httpRequest = context.mock(HttpServletRequest.class);
        final HttpServletResponse httpResponse = context.mock(HttpServletResponse.class);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintWriter httpResponseWriter = new PrintWriter(out);
        context.checking(new Expectations() {

            {
                one(httpRequest).getMethod();
                will(returnValue(requestMethod));
                if (!("http post not allowed".equals(errorMessage))) {
                    one(httpRequest).getRequestURI();
                    will(returnValue(requestURI));
                }
                if (!("invalid protocol".equals(errorMessage) || "http post not allowed".equals(errorMessage))) {
                    one(httpRequest).getHeader("If-None-Match");
                    if (ifNoneMatch < 0) {
                        will(returnValue(null));
                    } else {
                        will(returnValue(GLinkURL.JSON_ETAG_VERSION + "/\"" + jsonResponse.length() + "-" + String.valueOf(ifNoneMatch) + "\""));
                    }
                    one(httpRequest).getHeader("X-Network-Reload");
                    will(returnValue(xNetworkReload));
                }
                if (errorMessage != null) {
                    one(httpResponse).setContentType("application/json");
                    one(httpResponse).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    one(httpResponse).getWriter();
                    will(returnValue(httpResponseWriter));
                } else if (ifNoneMatch < 0 || "true".equals(xNetworkReload)) {
                    if ("true".equals(xNetworkReload)) {
                        LOG.finest(testID + ") Sending the browser new JSON content!!");
                    } else {
                        LOG.finest(testID + ") Sending the browser cached JSON content!!");
                    }
                    one(httpResponse).setHeader("Cache-Control", "max-age=0");
                    one(httpRequest).getAttribute("X-Downloading-GEDCOM");
                    will(returnValue(null));
                    one(httpResponse).setHeader(with(equal("ETag")), with(any(String.class)));
                    one(httpResponse).setDateHeader("Last-Modified", lastModified);
                    one(httpResponse).setContentType("application/json");
                    one(httpResponse).getWriter();
                    will(returnValue(httpResponseWriter));
                } else {
                    LOG.finest(testID + ") Telling the browser to use cached JSON content!!");
                    one(httpResponse).setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    one(httpResponse).setHeader(with(equal("ETag")), with(any(String.class)));
                }
            }
        });
        servlet.doGet(httpRequest, httpResponse);
        httpResponseWriter.flush();
        httpResponseWriter.close();
        context.assertIsSatisfied();
        String strResponse = out.toString();
        boolean equalResponses = jsonResponse.equals(strResponse);
        if (!equalResponses) {
            LOG.finest(testID + ") EXCPECTED RESPONSE ::: " + jsonResponse);
            LOG.finest(testID + ") BROWSER RESPONSE ::: " + strResponse);
        }
    }

    class HttpResponseMatcher<T> extends BaseMatcher<HttpServletResponse> {

        private HttpServletResponse httpResponse;

        public HttpResponseMatcher(HttpServletResponse httpResponse) {
            this.httpResponse = httpResponse;
        }

        @Override
        public boolean matches(Object item) {
            LOG.finest("Invoking HttpResponseMatcher with parameter: " + item);
            return false;
        }

        @Override
        public void describeTo(Description description) {
        }
    }
}
