package pipe4j.core;

import java.io.InputStream;
import java.io.OutputStream;
import pipe4j.pipe.SimpleStreamPipe;

class ReadClosingPipe extends SimpleStreamPipe {

    @Override
    public void run(InputStream is, OutputStream os) throws Exception {
        byte[] buffer = new byte[8];
        int n = is.read(buffer);
        os.write(buffer, 0, n);
        is.close();
    }
}
