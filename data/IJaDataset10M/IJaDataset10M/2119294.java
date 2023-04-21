package lackrp.hadoop.io.compress.lzma;

import java.io.IOException;
import java.io.OutputStream;
import lackrp.io.compress.LzmaOutputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;

public class LzmaCompressionOutputStream extends CompressionOutputStream {

    public LzmaCompressionOutputStream(OutputStream out) throws InterruptedException {
        super(new LzmaOutputStream(out));
    }

    @Override
    public void finish() throws IOException {
    }

    @Override
    public void resetState() throws IOException {
        throw new UnsupportedOperationException("LzmaCompressionOutputStream.resetState()");
    }

    @Override
    public void write(byte[] bs, int off, int len) throws IOException {
        out.write(bs, off, len);
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }
}
