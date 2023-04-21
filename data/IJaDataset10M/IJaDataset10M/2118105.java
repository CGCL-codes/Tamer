package br.net.woodstock.rockframework.web.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletInputStream;
import br.net.woodstock.rockframework.utils.IOUtils;

public class CachedServletInputStream extends ServletInputStream {

    private byte[] bytes;

    private InputStream inputStream;

    public CachedServletInputStream(final ServletInputStream inputStream) throws IOException {
        super();
        this.bytes = IOUtils.toByteArray(inputStream);
        this.inputStream = new ByteArrayInputStream(this.bytes);
    }

    public CachedServletInputStream(final byte[] bytes) {
        super();
        this.bytes = bytes;
        this.inputStream = new ByteArrayInputStream(this.bytes);
    }

    @Override
    public int available() throws IOException {
        return this.inputStream.available();
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }

    @Override
    public int read() throws IOException {
        return this.inputStream.read();
    }

    @Override
    public synchronized void reset() throws IOException {
        this.inputStream.reset();
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public byte[] getBytes() {
        return this.bytes;
    }
}
