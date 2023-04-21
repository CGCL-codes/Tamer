package de.objectcode.openk.accessor.openbravo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class InterceptorHttpServletResponse implements HttpServletResponse {

    private HttpServletResponse delegate;

    private InterceptorPrintWriter icpw;

    private InterceptorOutputStream icos;

    public InterceptorHttpServletResponse(HttpServletResponse response) {
        super();
        this.delegate = response;
        try {
            icos = new InterceptorOutputStream(delegate.getOutputStream());
            icpw = new InterceptorPrintWriter(icos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StringBuilder getBuffy() {
        return icpw.getBuffy();
    }

    public ByteArrayOutputStream getBaos() {
        return icos.getBaos();
    }

    public byte[] getStreamAsByteArray() {
        return icos.getBaos().toByteArray();
    }

    public byte[] getPwAsByteArray() {
        return icpw.getBuffy().toString().getBytes();
    }

    public void addCookie(Cookie arg0) {
        delegate.addCookie(arg0);
    }

    public void addDateHeader(String arg0, long arg1) {
        delegate.addDateHeader(arg0, arg1);
    }

    public void addHeader(String arg0, String arg1) {
        delegate.addHeader(arg0, arg1);
    }

    public void addIntHeader(String arg0, int arg1) {
        delegate.addIntHeader(arg0, arg1);
    }

    public boolean containsHeader(String arg0) {
        return delegate.containsHeader(arg0);
    }

    @SuppressWarnings("deprecation")
    public String encodeRedirectUrl(String arg0) {
        return delegate.encodeRedirectUrl(arg0);
    }

    public String encodeRedirectURL(String arg0) {
        return delegate.encodeRedirectURL(arg0);
    }

    @SuppressWarnings("deprecation")
    public String encodeUrl(String arg0) {
        return delegate.encodeUrl(arg0);
    }

    public String encodeURL(String arg0) {
        return delegate.encodeURL(arg0);
    }

    public void flushBuffer() throws IOException {
        delegate.flushBuffer();
    }

    public int getBufferSize() {
        return delegate.getBufferSize();
    }

    public String getCharacterEncoding() {
        return delegate.getCharacterEncoding();
    }

    public String getContentType() {
        return delegate.getContentType();
    }

    public Locale getLocale() {
        return delegate.getLocale();
    }

    public InterceptorOutputStream getOutputStream() throws IOException {
        return icos;
    }

    public PrintWriter getWriter() throws IOException {
        return icpw;
    }

    public boolean isCommitted() {
        return delegate.isCommitted();
    }

    public void reset() {
        delegate.reset();
    }

    public void resetBuffer() {
        delegate.resetBuffer();
    }

    public void sendError(int arg0, String arg1) throws IOException {
        delegate.sendError(arg0, arg1);
    }

    public void sendError(int arg0) throws IOException {
        delegate.sendError(arg0);
    }

    public void sendRedirect(String arg0) throws IOException {
        delegate.sendRedirect(arg0);
    }

    public void setBufferSize(int arg0) {
        delegate.setBufferSize(arg0);
    }

    public void setCharacterEncoding(String arg0) {
        delegate.setCharacterEncoding(arg0);
    }

    public void setContentLength(int arg0) {
        delegate.setContentLength(arg0);
    }

    public void setContentType(String arg0) {
        delegate.setContentType(arg0);
    }

    public void setDateHeader(String arg0, long arg1) {
        delegate.setDateHeader(arg0, arg1);
    }

    public void setHeader(String arg0, String arg1) {
        delegate.setHeader(arg0, arg1);
    }

    public void setIntHeader(String arg0, int arg1) {
        delegate.setIntHeader(arg0, arg1);
    }

    public void setLocale(Locale arg0) {
        delegate.setLocale(arg0);
    }

    @SuppressWarnings("deprecation")
    public void setStatus(int arg0, String arg1) {
        delegate.setStatus(arg0, arg1);
    }

    public void setStatus(int arg0) {
        delegate.setStatus(arg0);
    }
}
