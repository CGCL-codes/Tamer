package org.knopflerfish.bundle.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class ResponseWrapper implements Response {

    private final HttpServletResponse response;

    private ServletOutputStream sos = null;

    private PrintWriter pw = null;

    public ResponseWrapper(final HttpServletResponse response) {
        this.response = response;
    }

    public OutputStream getRawOutputStream() {
        if (response instanceof Response) {
            return ((Response) response).getRawOutputStream();
        }
        return null;
    }

    public void flushBuffer() throws IOException {
        response.flushBuffer();
    }

    public int getBufferSize() {
        return response.getBufferSize();
    }

    public void resetBuffer() {
        response.resetBuffer();
    }

    public String getContentType() {
        return response.getContentType();
    }

    public void setCharacterEncoding(String enc) {
        response.setCharacterEncoding(enc);
    }

    public String getCharacterEncoding() {
        return response.getCharacterEncoding();
    }

    public Locale getLocale() {
        return response.getLocale();
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (pw != null) throw new IllegalStateException("getWriter() already called");
        if (sos == null) {
            final OutputStream os = getRawOutputStream();
            if (os == null) sos = response.getOutputStream(); else sos = new ServletOutputStreamImpl(os);
        }
        return sos;
    }

    public PrintWriter getWriter() throws IOException {
        if (sos != null) throw new IllegalStateException("getOutputStream() already called");
        if (pw == null) {
            final OutputStream os = getRawOutputStream();
            if (os == null) pw = response.getWriter(); else pw = new PrintWriter(new OutputStreamWriter(os, getCharacterEncoding()));
        }
        return pw;
    }

    public boolean isCommitted() {
        return response.isCommitted();
    }

    public void reset() {
        response.reset();
    }

    public void setBufferSize(int size) {
        response.setBufferSize(size);
    }

    public void setContentLength(int length) {
        response.setContentLength(length);
    }

    public void setContentType(String contentType) {
        response.setContentType(contentType);
    }

    public void setLocale(Locale locale) {
        response.setLocale(locale);
    }

    public void addCookie(Cookie cookie) {
        response.addCookie(cookie);
    }

    public void addDateHeader(String name, long value) {
        response.addDateHeader(name, value);
    }

    public void addHeader(String name, String value) {
        response.addHeader(name, value);
    }

    public void addIntHeader(String name, int value) {
        response.addIntHeader(name, value);
    }

    public boolean containsHeader(String name) {
        return response.containsHeader(name);
    }

    public String encodeRedirectURL(String url) {
        return response.encodeRedirectURL(url);
    }

    public String encodeRedirectUrl(String url) {
        return response.encodeRedirectUrl(url);
    }

    public String encodeURL(String url) {
        return response.encodeURL(url);
    }

    public String encodeUrl(String url) {
        return response.encodeUrl(url);
    }

    public void sendError(int code) throws IOException {
        response.sendError(code);
    }

    public void sendError(int code, String message) throws IOException {
        response.sendError(code, message);
    }

    public void sendRedirect(String uri) throws IOException {
        response.sendRedirect(uri);
    }

    public void setDateHeader(String name, long value) {
        response.setDateHeader(name, value);
    }

    public void setHeader(String name, String value) {
        response.setHeader(name, value);
    }

    public void setIntHeader(String name, int value) {
        response.setIntHeader(name, value);
    }

    public void setStatus(int code) {
        response.setStatus(code);
    }

    public void setStatus(int code, String message) {
        response.setStatus(code, message);
    }
}
