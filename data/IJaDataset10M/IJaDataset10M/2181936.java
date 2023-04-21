package org.imv.webdav;

import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.imv.util.ImvWebdavStatus;

/**
 * ImvWebdav exception class.
 *
 * @author <a href="mailto:rushi@mail.com">Rushi Desai</a>
 */
public class ImvWebdavException extends Exception {

    /**
     * HTTP status code associated with the exception.
     */
    protected int statusCode;

    /**
     * Constructor.
     *
     * @param statusCode Exception status code
     */
    public ImvWebdavException(int statusCode) {
        super(ImvWebdavStatus.getStatusText(statusCode));
        this.statusCode = statusCode;
    }

    /**
     * Returns the HTTP/WebDAV status code associated with the exception.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns an HTTP formatted error page.
     *
     * @param resp HTTP servlet response
     */
    public void writeErrorPage(HttpServletResponse resp) {
        try {
            PrintWriter writer = resp.getWriter();
            writer.println("<html><body><b>" + statusCode + ":</b> " + ImvWebdavStatus.getStatusText(statusCode) + "</body></html>");
            writer.close();
        } catch (IOException e) {
        }
    }
}
