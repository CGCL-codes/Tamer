package org.freebxml.omar.client.ui.web.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.freebxml.omar.client.ui.web.client.ServiceException;

public class ServiceUtil {

    public static ServiceException toServiceException(Throwable ex) {
        StringWriter s = new StringWriter();
        PrintWriter out = new PrintWriter(s, false);
        ex.printStackTrace(out);
        out.flush();
        return new ServiceException(ex.getMessage(), s.toString());
    }
}
