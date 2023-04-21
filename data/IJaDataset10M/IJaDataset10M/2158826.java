package org.dcm4chee.xds.common.store;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;

public class InputStreamDataSource implements DataSource {

    private InputStream inputStream;

    public InputStreamDataSource(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getContentType() {
        return "application/octet-stream";
    }

    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    public String getName() {
        return "InputStreamDataSource";
    }

    public OutputStream getOutputStream() throws IOException {
        return null;
    }
}
