package org.jumpmind.symmetric.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jumpmind.symmetric.transport.handler.PushResourceHandler;

public class PushServlet extends AbstractTransportResourceServlet<PushResourceHandler> {

    private static final long serialVersionUID = 1L;

    private static final Log logger = LogFactory.getLog(PushServlet.class);

    @Override
    public boolean isContainerCompatible() {
        return true;
    }

    @Override
    protected void handleHead(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    }

    @Override
    protected void handlePut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nodeId = getParameter(req, WebConstants.NODE_ID);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Push request received from %s", nodeId));
        }
        InputStream inputStream = createInputStream(req);
        OutputStream outputStream = createOutputStream(resp);
        getTransportResourceHandler().push(inputStream, outputStream);
        outputStream.flush();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Done with Push request from %s", nodeId));
        }
    }

    @Override
    protected Log getLogger() {
        return logger;
    }
}
