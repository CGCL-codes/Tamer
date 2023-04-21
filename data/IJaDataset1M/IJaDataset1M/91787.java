package fr.graphit.web.api.reply.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import com.google.sitebricks.client.transport.Text;

public class SVG extends Text {

    public String contentType() {
        return "image/svg+xml";
    }

    public <T> T in(InputStream in, Class<T> type) throws IOException {
        return type.cast(IOUtils.toString(in));
    }

    public <T> void out(OutputStream out, Class<T> type, T data) {
        try {
            IOUtils.write(data.toString(), out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
