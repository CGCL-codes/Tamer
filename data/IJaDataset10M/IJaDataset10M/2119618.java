package avoware.intchat.client.api;

import avoware.intchat.client.api.concurrent.ProgressMonitor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Andrew Orlov
 */
public class Pipe {

    /**
     * buffer size in bytes (to send/receive files over network)
     */
    public static final int BUFFER_SIZE = 8192;

    protected void pipe(Cancelable source, InputStream in, OutputStream out, ProgressMonitor monitor) throws IOException {
        if (in == null || out == null) return;
        try {
            int l;
            byte[] buffer = new byte[BUFFER_SIZE];
            long bytesRead = 0;
            while (!source.isCancelled() && (l = in.read(buffer)) != -1) {
                in.available();
                out.write(buffer, 0, l);
                bytesRead += l;
                monitor.setProgress(bytesRead);
            }
            out.flush();
            if (monitor != null && monitor.getMaximum() > 0 && monitor.getProgress() < monitor.getMaximum() && !source.isCancelled()) {
                throw new IOException("incomplete data: got " + monitor.getProgress() + " of " + monitor.getMaximum());
            }
        } finally {
            in.close();
            out.close();
        }
    }
}
