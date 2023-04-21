package org.xaware.testing.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import com.meterware.httpunit.WebResponse;
import com.meterware.pseudoserver.WebResource;
import org.xaware.server.connector.servlet.SoapServletTestHelper;
import org.xaware.server.connector.servlet.XASoapStyle;
import org.xaware.shared.util.XAwareException;
import org.xaware.testing.util.GenericEchoServlet;

/**
 * Provide linkage to the XAMSSoapServlet, prepare received message and send it to the XAMSSoapServlet, take the
 * returned results from the Soap Servlet and pass it back to the server.
 * 
 * @author hcurtis
 * 
 */
public class XAwarePseudoRpcServlet extends GenericEchoServlet {

    protected SoapServletTestHelper testHelper = null;

    /**
     * Constructor for other RPC styles
     */
    public XAwarePseudoRpcServlet(final Log logger) {
        super(logger);
    }

    /**
     * Constructor
     */
    public XAwarePseudoRpcServlet(final Log logger, final Class tc, final String dataFolder) throws XAwareException {
        super(logger);
        testHelper = new SoapServletTestHelper(tc, dataFolder, XASoapStyle.RPC_ENCODED);
    }

    @Override
    public WebResource getPostResponse() throws IOException {
        msgBody = new String(getBody());
        log.debug("Request Message:" + msgBody);
        fetchHeaders();
        final ByteArrayInputStream msg = new ByteArrayInputStream(this.getBody());
        final Iterator hIter = headerMap.keySet().iterator();
        while (hIter.hasNext()) {
            final String name = (String) hIter.next();
            if (CONTENT_LENGTH.equalsIgnoreCase(name)) {
                testHelper.addHeader(CONTENT_LENGTH, String.valueOf(msgBody.length()));
            } else {
                final String value = headerMap.get(name);
                testHelper.addHeader(name, value);
            }
        }
        final WebResponse servletResponse = testHelper.runRequest(msg);
        final WebResource resource = new WebResource(servletResponse.getText(), servletResponse.getContentType());
        resource.addHeader("connection: close");
        setHeader(CONTENT_LENGTH, String.valueOf(servletResponse.getText().length()));
        pushHeaders(resource);
        return resource;
    }
}
