package org.nodevision.portal.wrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class NVHttpServletResponse implements HttpServletResponse {

    private HttpServletResponse response;

    private NVServletOutputStream bufferedServletOut = new NVServletOutputStream();

    private PrintWriter printWriter;

    private ServletOutputStream outputStream;

    public NVHttpServletResponse(HttpServletResponse origResponse) {
        response = origResponse;
    }

    public void addCookie(Cookie cookie) {
        response.addCookie(cookie);
    }

    public void addDateHeader(String header, long number) {
        response.addDateHeader(header, number);
    }

    public void addHeader(String header, String string) {
        response.addHeader(header, string);
    }

    public void addIntHeader(String header, int integer) {
        response.addIntHeader(header, integer);
    }

    public boolean containsHeader(String header) {
        return response.containsHeader(header);
    }

    public String encodeRedirectUrl(String url) {
        return response.encodeRedirectUrl(url);
    }

    public String encodeRedirectURL(String url) {
        return response.encodeRedirectURL(url);
    }

    public String encodeUrl(String url) {
        return response.encodeUrl(url);
    }

    public String encodeURL(String url) {
        return response.encodeURL(url);
    }

    public void flushBuffer() throws IOException {
        if (null != outputStream) {
            outputStream.flush();
        } else if (null != printWriter) {
            printWriter.flush();
        }
    }

    public int getBufferSize() {
        return getBuffer().length;
    }

    public String getCharacterEncoding() {
        return response.getCharacterEncoding();
    }

    public String getContentType() {
        return response.getContentType();
    }

    public Locale getLocale() {
        return response.getLocale();
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (null != printWriter) {
            throw new IllegalStateException("The Servlet API forbids calling getOutputStream( ) after" + " getWriter( ) has been called");
        }
        if (null == outputStream) {
            outputStream = bufferedServletOut;
        }
        return outputStream;
    }

    public PrintWriter getWriter() throws IOException {
        if (null != outputStream) {
            throw new IllegalStateException("The Servlet API forbids calling getWriter( ) after" + " getOutputStream( ) has been called");
        }
        if (null == printWriter) {
            printWriter = new PrintWriter(bufferedServletOut);
        }
        return printWriter;
    }

    public boolean isCommitted() {
        return response.isCommitted();
    }

    public void reset() {
        bufferedServletOut = new NVServletOutputStream();
        printWriter = new PrintWriter(bufferedServletOut);
        response.reset();
    }

    public void resetBuffer() {
        try {
            bufferedServletOut.flush();
            printWriter.flush();
        } catch (Exception e) {
        }
        bufferedServletOut = new NVServletOutputStream();
        printWriter = new PrintWriter(bufferedServletOut);
        response.resetBuffer();
    }

    public void sendError(int status, String message) throws IOException {
        response.sendError(status, message);
    }

    public void sendError(int status) throws IOException {
        response.sendError(status);
    }

    public void sendRedirect(String url) throws IOException {
        response.sendRedirect(url);
    }

    public void setBufferSize(int size) {
        response.setBufferSize(size);
    }

    public void setCharacterEncoding(String encoding) {
        response.setCharacterEncoding(encoding);
    }

    public void setContentLength(int length) {
        response.setContentLength(length);
    }

    public void setContentType(String contentType) {
        response.setContentType(contentType);
    }

    public void setDateHeader(String header, long date) {
        response.setDateHeader(header, date);
    }

    public void setHeader(String header, String string) {
        response.setHeader(header, string);
    }

    public void setIntHeader(String header, int integer) {
        response.setIntHeader(header, integer);
    }

    public void setLocale(Locale locale) {
        response.setLocale(locale);
    }

    public void setStatus(int status, String message) {
        response.setStatus(status, message);
    }

    public void setStatus(int status) {
        response.setStatus(status);
    }

    public byte[] getBuffer() {
        return bufferedServletOut.getBuffer();
    }
}
