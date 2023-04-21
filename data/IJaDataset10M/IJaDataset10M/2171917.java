package lackrp.io.compress;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Semaphore;
import lackrp.io.BytePipe;

public class LzmaCompressThread extends LzmaThreadBase {

    private InputStream input;

    private OutputStream output;

    public LzmaCompressThread(BytePipe pipe, OutputStream out, Semaphore lock) throws InterruptedException {
        super(pipe, lock);
        this.input = pipe.getInputStream();
        this.output = out;
    }

    public void work() throws Exception {
        Lzma.compress(input, output);
    }
}
