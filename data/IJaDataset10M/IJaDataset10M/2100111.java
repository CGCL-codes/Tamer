package org.wings.io;

import java.io.OutputStream;
import java.io.IOException;

/**
 * An OutputStream, that writes to an Device.
 *
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision: 1759 $
 */
public final class DeviceOutputStream extends OutputStream {

    final Device sink;

    public DeviceOutputStream(Device d) {
        sink = d;
    }

    public void close() {
    }

    public void flush() throws IOException {
        sink.flush();
    }

    public void write(byte b[], int off, int len) throws IOException {
        sink.write(b, off, len);
    }

    public void write(byte b[]) throws IOException {
        sink.write(b);
    }

    public void write(int b) throws IOException {
        sink.write(b);
    }
}
