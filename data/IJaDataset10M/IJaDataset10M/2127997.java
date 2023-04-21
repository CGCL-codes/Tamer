package au.edu.archer.metadata.mde.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import org.apache.cactus.FilterTestCase;
import org.apache.cactus.WebResponse;
import au.edu.archer.metadata.mde.repository.RepositoryEventListener;

/**
 * Base class for cactus Filter tests for the MDE filters.
 *
 * @author scrawley@itee.uq.edu.au
 *
 */
public abstract class MDEFilterTest extends FilterTestCase {

    protected Filter filter;

    protected MockFilterChain mockChain;

    /**
     * Setup the servlet context, filter chain and so on ready for a Filter test.
     *
     * @param filter the filter instance to be tested
     * @param filterName the name of the filter (for messages)
     * @throws IOException
     * @throws ServletException
     */
    public void filterSetup(Filter filter, String filterName) throws IOException, ServletException {
        ServletContext context = config.getServletContext();
        ServletContextEvent event = new ServletContextEvent(context);
        RepositoryEventListener listener = new RepositoryEventListener();
        listener.contextInitialized(event);
        this.filter = filter;
        this.filter.init(config);
        this.mockChain = new MockFilterChain(filterName);
    }

    /**
     * Loads a test record
     * @param fileName Name of record wile (relative from WS env var)
     * @return Streamed file
     * @throws IOException
     */
    protected InputStream fetchTestRecord(String fileName) throws IOException {
        String testDir = MDETestHelper.getTestDataDir();
        fileName = testDir + "records/" + fileName;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            int ch = br.read();
            while (ch != -1) {
                sb.append((char) ch);
                ch = br.read();
            }
            byte[] bytes = sb.toString().getBytes();
            return new ByteArrayInputStream(bytes);
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    protected void checkText(String expected, String actual) {
        String[] expectedLines = expected.split("\n");
        String[] actualLines = actual.split("\n");
        int nosLines = Math.min(expectedLines.length, actualLines.length);
        for (int i = 0; i < nosLines; i++) {
            String e = expectedLines[i].trim();
            String a = actualLines[i].trim();
            if (!e.equals(a)) {
                fail("texts differ at line #" + i + "\n" + "expected: '" + e + "'\n" + "actual:   '" + a + "'\n" + "\nFULL TEXT:\n" + actual);
            }
        }
        if (nosLines < expectedLines.length) {
            fail("missing line at #" + nosLines + "\n" + "expected: '" + expectedLines[nosLines].trim() + "'\n");
        }
        if (nosLines < actualLines.length) {
            fail("extra line at #" + nosLines + "\n" + "actual:   '" + actualLines[nosLines].trim() + "'\n");
        }
    }

    protected String getResponseBody(WebResponse webResponse) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(webResponse.getInputStream()));
            StringBuilder sb = new StringBuilder();
            int ch = br.read();
            while (ch != -1) {
                sb.append((char) ch);
                ch = br.read();
            }
            return sb.toString();
        } finally {
            if (br != null) br.close();
        }
    }
}
