package net.woodstock.rockapi.jsp.wrappers;

import java.io.InputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class BufferedServletResponseWrapper extends HttpServletResponseWrapper {

    private ServletOutputStreamWrapper outputStream;

    public BufferedServletResponseWrapper(HttpServletResponse response) {
        super(response);
        this.outputStream = new ServletOutputStreamWrapper();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return this.outputStream;
    }

    public ServletOutputStreamWrapper getOutputStreamWrapper() {
        return this.outputStream;
    }

    public InputStream getInputStream() {
        return this.outputStream.getInputStream();
    }
}
