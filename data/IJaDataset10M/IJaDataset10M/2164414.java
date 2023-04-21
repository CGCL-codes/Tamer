package net.sourceforge.gedapi.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.gedapi.test.AbstractTest;
import org.jmock.Expectations;
import org.jmock.Mockery;
import junit.framework.Test;
import junit.framework.TestSuite;

public class GEDCOMCentralSiteServletTest extends AbstractTest {

    final GEDCOMCentralSiteServlet servlet = new GEDCOMCentralSiteServlet();

    public GEDCOMCentralSiteServletTest(String testName) throws ServletException {
        super(testName);
        System.out.println("Running GEDCOMCentralSiteServlet... Please be patient...");
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
        return new TestSuite(GEDCOMCentralSiteServletTest.class);
    }

    @Override
    public void runAllTests() throws IOException {
        testGCSBrowse();
        testGCSDownload();
        testGCSUpload();
        testGCSNotResolved();
    }

    public void testGCSBrowse() throws IOException {
        Mockery context = new Mockery();
        final HttpServletRequest httpRequest = context.mock(HttpServletRequest.class);
        final HttpServletResponse httpResponse = context.mock(HttpServletResponse.class);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintWriter httpResponseWriter = new PrintWriter(out);
        try {
            context.checking(new Expectations() {

                {
                    one(httpRequest).getMethod();
                    will(returnValue("POST"));
                    one(httpRequest).getRequestURI();
                    will(returnValue("/GCS/"));
                    one(httpRequest).getServletPath();
                    will(returnValue("/GCS"));
                    one(httpRequest).getMethod();
                    will(returnValue("POST"));
                    one(httpRequest).getContentType();
                    will(returnValue("application/x-www-form-urlencoded"));
                    one(httpRequest).getRequestURL();
                    will(returnValue(new StringBuffer("http://localhost:8081/gedapi/GCS/")));
                    one(httpResponse).setHeader("Cache-Control", "max-age=0");
                    one(httpResponse).setHeader("ETag", "W0006/\"5296-1244677883000\"");
                    one(httpResponse).setDateHeader("Last-Modified", 1244677883000l);
                    one(httpResponse).setContentType("application/json");
                    one(httpResponse).getWriter();
                    will(returnValue(httpResponseWriter));
                }
            });
            servlet.service(httpRequest, httpResponse);
            httpResponseWriter.flush();
            httpResponseWriter.close();
            context.assertIsSatisfied();
            String strResponse = out.toString();
            String jsonResponse = "{\"content\":[{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1freespace.virgin.net\",\"linkContent\":\"http://freespace.virgin.net\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1freespace.virgin.net\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1gedcom.surnames.com\",\"linkContent\":\"http://gedcom.surnames.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1gedcom.surnames.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1glinktest.test.com\",\"linkContent\":\"http://glinktest.test.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1glinktest.test.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1greer-family-tree.com\",\"linkContent\":\"http://greer-family-tree.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1greer-family-tree.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1homepages.rootsweb.ancestry.com\",\"linkContent\":\"http://homepages.rootsweb.ancestry.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1homepages.rootsweb.ancestry.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1jdmcox.com\",\"linkContent\":\"http://jdmcox.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1jdmcox.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1localgedcom~~~_38081\",\"linkContent\":\"http://localgedcom:8081\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1localgedcom~~~_38081\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1localhost_8081\",\"linkContent\":\"http://localhost_8081\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1localhost_8081\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1localhost~~~_38081\",\"linkContent\":\"http://localhost:8081\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1localhost~~~_38081\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1testglink.test.com\",\"linkContent\":\"http://testglink.test.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1testglink.test.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1us.geocities.com\",\"linkContent\":\"http://us.geocities.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1us.geocities.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1vlado.fmf.uni-lj.si\",\"linkContent\":\"http://vlado.fmf.uni-lj.si\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1vlado.fmf.uni-lj.si\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www-personal.umich.edu\",\"linkContent\":\"http://www-personal.umich.edu\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www-personal.umich.edu\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www.angelfire.com\",\"linkContent\":\"http://www.angelfire.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www.angelfire.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www.avac.uklinux.net\",\"linkContent\":\"http://www.avac.uklinux.net\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www.avac.uklinux.net\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www.coryfamsoc.com\",\"linkContent\":\"http://www.coryfamsoc.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www.coryfamsoc.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www.cursiter.com\",\"linkContent\":\"http://www.cursiter.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www.cursiter.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www.daml.org\",\"linkContent\":\"http://www.daml.org\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www.daml.org\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www.discovergenealogy.com\",\"linkContent\":\"http://www.discovergenealogy.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www.discovergenealogy.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www.geocities.com\",\"linkContent\":\"http://www.geocities.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www.geocities.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www.jdweaver.com\",\"linkContent\":\"http://www.jdweaver.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www.jdweaver.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www.moonrakers.org.uk\",\"linkContent\":\"http://www.moonrakers.org.uk\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www.moonrakers.org.uk\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www.mosesclawson.com\",\"linkContent\":\"http://www.mosesclawson.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www.mosesclawson.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www.reardon-family.org\",\"linkContent\":\"http://www.reardon-family.org\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www.reardon-family.org\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www.rootsweb.ancestry.com\",\"linkContent\":\"http://www.rootsweb.ancestry.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www.rootsweb.ancestry.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www.spoonergen.com\",\"linkContent\":\"http://www.spoonergen.com\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www.spoonergen.com\"},{\"isFile\":false,\"jsContent\":\"/http~~~_3~~~1www.ussery.net\",\"linkContent\":\"http://www.ussery.net\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/http~~~_3~~~1www.ussery.net\"},{\"isFile\":false,\"jsContent\":\"/uploads\",\"linkContent\":\"uploads\",\"linkURL\":\"http://localhost:8081/gedapi/GCS/uploads\"}],\"gedcomurl\":\"\",\"tabIndex\":\"gcs\"}";
            boolean equalResponses = jsonResponse.equals(strResponse);
            if (!equalResponses) {
                System.out.println("1) EXCPECTED RESPONSE ::: " + jsonResponse);
                System.out.println("1) BROWSER RESPONSE ::: " + strResponse);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        oneAtaTime();
    }

    public void testGCSDownload() {
    }

    public void testGCSUpload() {
    }

    public void testGCSNotResolved() {
    }
}
