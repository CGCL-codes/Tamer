package org.apache.axis.attachments;

import javax.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PlainTextDataSource implements DataSource {

    public static final String CONTENT_TYPE = "text/plain";

    private final String name;

    private byte[] data;

    private ByteArrayOutputStream os;

    public PlainTextDataSource(String name, String data) {
        this.name = name;
        this.data = data == null ? null : data.getBytes();
        os = new ByteArrayOutputStream();
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return CONTENT_TYPE;
    }

    public InputStream getInputStream() throws IOException {
        if (os.size() != 0) {
            data = os.toByteArray();
        }
        return new ByteArrayInputStream(data == null ? new byte[0] : data);
    }

    public OutputStream getOutputStream() throws IOException {
        if (os.size() != 0) {
            data = os.toByteArray();
        }
        return new ByteArrayOutputStream();
    }
}
