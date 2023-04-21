package org.chiba.adapter.upload;

import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * Class by Pierre-Alexandre Losson -- http://www.telio.be/blog
 * @author Original : plosson on 05-janv.-2006 10:46:33 - Last modified  by $Author: joernt $ on $Date: 2006/01/12 17:15:36 $
 * @version $id: $
 */
public class MonitoredOutputStream extends OutputStream {

    private OutputStream target;

    private OutputStreamListener listener;

    public MonitoredOutputStream(OutputStream target, OutputStreamListener listener) {
        this.target = target;
        this.listener = listener;
        this.listener.start();
    }

    public void write(byte b[], int off, int len) throws IOException {
        target.write(b, off, len);
        listener.bytesRead(len - off);
    }

    public void write(byte b[]) throws IOException {
        target.write(b);
        listener.bytesRead(b.length);
    }

    public void write(int b) throws IOException {
        target.write(b);
        listener.bytesRead(1);
    }

    public void close() throws IOException {
        target.close();
        listener.done();
    }

    public void flush() throws IOException {
        target.flush();
    }
}
