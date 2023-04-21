package de.herberlin.server.test;

import java.io.IOException;
import java.net.MalformedURLException;
import org.xml.sax.SAXException;
import com.meterware.httpunit.WebResponse;

/**
 * Tests ssi.page includes ssi-page
 * @author aherbertz
 */
public class Ssi2Test extends SsiTest {

    /**
     * @param name
     */
    public Ssi2Test(String name) {
        super(name);
    }

    /**
     * @see de.herberlin.server.test.SsiTest#getSsiPage()
     */
    protected WebResponse getSsiPage() throws MalformedURLException, IOException, SAXException {
        return getPage("/ssi2.shtml");
    }

    protected boolean isSsi2() {
        return true;
    }

    public void testIncludePosition() throws Exception {
        WebResponse resp = getSsiPage();
        assertTextPresent(resp, "<!-- START INCLUDE ssi.shtml -->\n" + "<hr>\n" + "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n" + "<!-- page=ssi.shtml -->");
        assertTextPresent(resp, "</body>\n" + "</html>\n" + "\n" + "<hr>\n" + "<!-- END INCLUDE ssi.shtml -->");
    }

    public void testCommoneEnvironmentForIncludes() throws MalformedURLException, IOException, SAXException {
        WebResponse resp = getSsiPage();
        assertTextPresent(resp, "TEST=success");
    }
}
