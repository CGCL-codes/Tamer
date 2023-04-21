package hu.dobrosi.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectionConnector implements Connector {

    protected String url;

    protected InputStream in;

    protected OutputStream out;

    public void close() throws IOException {
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
    }

    public InputStream getInputStream() throws IOException {
        return null;
    }

    public OutputStream getOutputStream() throws IOException {
        return null;
    }

    public void open(String url) throws IOException {
        this.url = url;
    }

    public void sleep(long millis) throws IOException, InterruptedException {
    }

    public void reopen() throws IOException {
    }
}
