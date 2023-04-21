package pipe4j.pipe.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import pipe4j.pipe.SimpleStreamPipe;

/**
 * Writes data read from previous pipe into next, while replicating data to the
 * given {@link OutputStream}
 * 
 * @author bbennett
 */
public class TeePipe extends SimpleStreamPipe {

    private final OutputStream teeStream;

    public TeePipe(OutputStream teeStream) {
        super();
        this.teeStream = teeStream;
    }

    public OutputStream getTeeStream() {
        return teeStream;
    }

    @Override
    protected void run(InputStream inputStream, OutputStream outputStream) throws Exception {
        transfer(inputStream, getTeeOutputStream(outputStream));
        this.teeStream.flush();
        this.teeStream.close();
    }

    private OutputStream getTeeOutputStream(OutputStream out) throws IOException {
        return new TeeOutputStream(out, getTeeStream());
    }
}
