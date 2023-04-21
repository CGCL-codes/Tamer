package com.valtech.bootcamp.carRental.web.cocoon;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Locale;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * This allows the wrapping of a request to Cocoon from other applications.
 *   It is similar to HttpServletReqImpl used in EngineWrapper, but is generic 
 *   enough to work anywhere, not just with ProducerFromFile.
 * It can be used to push files through to Cocoon, or Strings (generated from
 *   some other application.
 *
 * @author <a href="mailto:stefano@apache.org">Stefano Mazzocchi</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2000/11/16 12:41:40 $
 */
public class CocoonServletResponse implements HttpServletResponse {

    private PrintWriter out;

    public CocoonServletResponse(PrintWriter out) {
        this.out = out;
    }

    public PrintWriter getWriter() throws IOException {
        return this.out;
    }

    public void setContentLength(int len) {
    }

    public void setContentType(String type) {
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    public String getCharacterEncoding() {
        return null;
    }

    public void addCookie(Cookie cookie) {
    }

    public boolean containsHeader(String name) {
        return false;
    }

    public void setStatus(int sc) {
    }

    public void setHeader(String name, String value) {
    }

    public void setIntHeader(String name, int value) {
    }

    public void setDateHeader(String name, long date) {
    }

    public void sendError(int sc, String msg) throws IOException {
    }

    public void sendError(int sc) throws IOException {
    }

    public void sendRedirect(String location) throws IOException {
    }

    public String encodeURL(String url) {
        return url;
    }

    public String encodeRedirectURL(String url) {
        return url;
    }

    public void setBufferSize(int size) {
    }

    public int getBufferSize() {
        return 0;
    }

    public void flushBuffer() {
    }

    public boolean isCommitted() {
        return false;
    }

    public void reset() {
    }

    public void setLocale(Locale locale) {
    }

    public Locale getLocale() {
        return null;
    }

    public void addDateHeader(String name, long date) {
    }

    public void addHeader(String name, String value) {
    }

    public void addIntHeader(String name, int value) {
    }

    /** @deprecated */
    public void setStatus(int sc, String sm) {
    }

    /** @deprecated */
    public String encodeUrl(String url) {
        return url;
    }

    /** @deprecated */
    public String encodeRedirectUrl(String url) {
        return url;
    }
}
