package immf;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.entity.mime.content.AbstractContentBody;

public class ByteArrayBody extends AbstractContentBody {

    private byte[] data;

    private String filename;

    public ByteArrayBody(byte[] data, String mimeType, String filename) {
        super(mimeType);
        this.data = data;
        this.filename = filename;
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        out.write(this.data);
        out.flush();
    }

    public String getFilename() {
        return this.filename;
    }

    public String getCharset() {
        return null;
    }

    public long getContentLength() {
        return this.data.length;
    }

    public String getTransferEncoding() {
        return "binary";
    }
}
