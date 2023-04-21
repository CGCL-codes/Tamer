package com.webkreator.qlue.pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.exception.ParseErrorException;
import com.webkreator.canoe.HtmlEncoder;
import com.webkreator.qlue.AccessForbiddenException;
import com.webkreator.qlue.Page;
import com.webkreator.qlue.PersistentPageNotFoundException;
import com.webkreator.qlue.ValidationException;
import com.webkreator.qlue.util.WebUtil;
import com.webkreator.qlue.view.View;

/**
 * Handle an application exception.
 */
public class handleThrowable extends Page {

    @Override
    public View service() throws Exception {
        Throwable t = (Throwable) context.request.getAttribute("javax.servlet.error.exception");
        if (t == null) {
            throw new Exception("handleThrowable: direct access not allowed");
        }
        if (t instanceof ValidationException) {
            return _handleValidationException((ValidationException) t);
        } else if (t instanceof PersistentPageNotFoundException) {
            return _handlePersistentPageNotFoundException((PersistentPageNotFoundException) t);
        } else if (t instanceof ParseErrorException) {
            return _handleVelocityParseError((ParseErrorException) t);
        } else if (t instanceof AccessForbiddenException) {
            return _handleAccessForbiddenException((AccessForbiddenException) t);
        }
        return _handleThrowable(t);
    }

    private View _handleAccessForbiddenException(AccessForbiddenException t) throws IOException {
        context.response.setContentType("text/html");
        context.response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        PrintWriter out = context.response.getWriter();
        out.println("<html>");
        out.println("<head><title>Forbidden</title></head>");
        out.println("<body><h1>Forbidden</h1>");
        if (isDevelopmentMode()) {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            out.println("<pre>");
            out.println(HtmlEncoder.encodeForHTMLPreserveWhitespace(sw.toString()));
            out.println("</pre>");
        }
        WebUtil.writePagePaddingforInternetExplorer(out);
        out.println("</body></html>");
        return null;
    }

    private View _handleVelocityParseError(ParseErrorException t) throws Exception {
        if (isDevelopmentMode() == false) {
            return _handleThrowable(t);
        }
        context.response.setContentType("text/html");
        PrintWriter out = context.response.getWriter();
        out.println("<html>");
        out.println("<head><title>Template Parse Error</title></head>");
        out.println("<body><h1>Template Parse Error</h1>");
        out.println("<pre>");
        out.println(HtmlEncoder.encodeForHTMLPreserveWhitespace(t.getMessage()));
        out.println("</pre>");
        WebUtil.writePagePaddingforInternetExplorer(out);
        out.println("</body></html>");
        return null;
    }

    private View _handlePersistentPageNotFoundException(PersistentPageNotFoundException t) throws Exception {
        context.response.setContentType("text/html");
        PrintWriter out = context.response.getWriter();
        out.println("<html>");
        out.println("<head><title>Activity Not Found</title></head>");
        out.println("<body><h1>Activity Not Found</h1>");
        WebUtil.writePagePaddingforInternetExplorer(out);
        out.println("</body></html>");
        return null;
    }

    public View _handleValidationException(ValidationException ve) throws Exception {
        context.response.setContentType("text/html");
        PrintWriter out = context.response.getWriter();
        out.println("<html>");
        out.println("<head><title>Parameter Validation Failed</title></head>");
        out.println("<body><h1>Parameter Validation Failed</h1>");
        WebUtil.writePagePaddingforInternetExplorer(out);
        out.println("</body></html>");
        return null;
    }

    public View _handleThrowable(Throwable t) throws Exception {
        context.response.setContentType("text/html");
        PrintWriter out = context.response.getWriter();
        out.println("<html>");
        out.println("<head><title>Internal Server Error</title></head>");
        out.println("<body><h1>Internal Server Error</h1>");
        if (isDevelopmentMode()) {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            out.println("<pre>");
            out.println(HtmlEncoder.encodeForHTMLPreserveWhitespace(sw.toString()));
            out.println("</pre>");
        }
        WebUtil.writePagePaddingforInternetExplorer(out);
        out.println("</body></html>");
        return null;
    }
}
