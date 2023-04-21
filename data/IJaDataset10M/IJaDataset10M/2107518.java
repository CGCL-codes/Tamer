package pipe4j.pipe.archive;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import pipe4j.pipe.SimpleStreamPipe;

/**
 * Applies gzip to stream.
 * 
 * @author bbennett
 */
public class GZipPipe extends SimpleStreamPipe {

    @Override
    protected void run(InputStream inputStream, OutputStream outputStream) throws Exception {
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        transfer(inputStream, gzipOutputStream);
        gzipOutputStream.finish();
    }
}
