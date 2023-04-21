package mobi.ilabs.restroom.tests.roundtripTestHarness;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;

public class FileEntity extends Entity {

    String filename;

    public String getFilename() {
        return filename;
    }

    public FileEntity(final DownloadURLRewriter dur, final String urlString, final String type, final String filename) {
        super(dur, urlString, type);
        this.filename = filename;
    }

    private byte[] content = null;

    @Override
    public byte[] getContentAsBytes() {
        if (content != null) {
            return content;
        }
        assert (getFilename() != null);
        final File f = new File(getFilename());
        final long llength = f.length();
        if (llength > Integer.MAX_VALUE) {
            throw new RuntimeException("File too long to be buffered " + getFilename());
        }
        final int length = (int) f.length();
        final byte[] result = new byte[length];
        int remaining = length;
        try {
            final InputStream is = new FileInputStream(f);
            int nextOffset = 0;
            final int stride = 4096;
            int noOfReadBytes;
            do {
                noOfReadBytes = is.read(result, nextOffset, stride < remaining ? stride : remaining);
                nextOffset += noOfReadBytes;
                remaining -= noOfReadBytes;
            } while (noOfReadBytes > 0);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e.toString());
        } catch (final IOException e) {
            throw new RuntimeException(e.toString());
        }
        content = result;
        assert (content != null);
        return content;
    }

    @Override
    public RequestEntity getRequestEntity() throws UnsupportedEncodingException {
        return new FileRequestEntity(new File(getFilename()), getType());
    }
}
