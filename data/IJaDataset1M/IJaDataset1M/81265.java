package hambo.messaging;

import java.io.*;
import javax.activation.*;

/**
 * This is just implementation and should be removed from the interface
 * package.
 */
public class ByteArrayDataSource implements DataSource {

    private byte[] data;

    private String type;

    public ByteArrayDataSource(InputStream is, String type) {
        this.type = type;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) os.write(ch);
            data = os.toByteArray();
        } catch (IOException ioex) {
        }
    }

    public ByteArrayDataSource(byte[] data, String type) {
        this.data = data;
        this.type = type;
    }

    public InputStream getInputStream() throws IOException {
        if (data == null) throw new IOException("no data");
        return new ByteArrayInputStream(data);
    }

    public OutputStream getOutputStream() throws IOException {
        throw new IOException("cannot do this");
    }

    public String getContentType() {
        return type;
    }

    public String getName() {
        return "dummy";
    }
}
