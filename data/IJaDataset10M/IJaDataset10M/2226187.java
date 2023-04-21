package ao.dd.shell.impl.transfer.stream;

import ao.util.async.Throttle;
import java.io.*;

/**
 * User: aostrovsky
 * Date: 24-Jul-2009
 * Time: 9:08:10 AM
 */
public class ThrottledOutputStream extends OutputStream {

    private final OutputStream deleget;

    private final Throttle throttle;

    public ThrottledOutputStream(String filename, long bytesPerSecond) throws FileNotFoundException {
        this(new FileOutputStream(filename), bytesPerSecond);
    }

    public ThrottledOutputStream(File file, long bytesPerSecond) throws FileNotFoundException {
        this(new FileOutputStream(file), bytesPerSecond);
    }

    public ThrottledOutputStream(OutputStream out, long bytesPerSecond) {
        deleget = (out instanceof BufferedOutputStream) ? out : new BufferedOutputStream(out);
        throttle = new Throttle(bytesPerSecond);
    }

    public void throttle(long bytesPerSecond) {
        throttle.limit(bytesPerSecond);
    }

    @Override
    public void write(int b) throws IOException {
        throttle.process();
        deleget.write(b);
    }

    @Override
    public void close() throws IOException {
        deleget.close();
    }
}
