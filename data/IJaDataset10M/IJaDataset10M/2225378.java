package org.eoti.io.stream;

import static org.eoti.io.stream.MultiplexControl.ETB;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MultiplexOutputStream extends FilterOutputStream {

    public MultiplexOutputStream(OutputStream out) {
        super(out);
    }

    public void endTransmissionBlock() throws IOException {
        out.write(ETB.getCode());
    }

    public void close() throws IOException {
    }

    public void finish() throws IOException {
        out.close();
    }
}
