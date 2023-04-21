package sunlabs.brazil.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.net.Socket;

/**
 * Version of Request for testing purposes.  Arranges for
 * all output to be captured in one place so the ouput of a test
 * may be compared with the expected output.
 */
public class TestRequest extends Request {

    ByteArrayOutputStream log = new ByteArrayOutputStream();

    public TestRequest(Server server, String request) throws IOException {
        super(server, new TestSocket(request));
        getRequest();
        log.reset();
    }

    public Object put(String key, String value) {
        return props.put(key, value);
    }

    public String log() {
        return log.toString();
    }

    public String result() {
        return ((TestSocket) sock).out.toString();
    }

    public void log(int level, Object obj, String message) {
        try {
            if (obj != null) {
                log.write(obj.toString().getBytes());
                log.write(':');
                log.write(' ');
            }
            if (message != null) {
                log.write(message.getBytes());
            }
            log.write('\n');
        } catch (IOException e) {
        }
    }

    private static class TestSocket extends Socket {

        InputStream in;

        ByteArrayOutputStream out;

        public TestSocket(String request) {
            in = new StringBufferInputStream(request);
            out = new ByteArrayOutputStream();
        }

        public void close() throws IOException {
            in.close();
            out.close();
        }

        public InputStream getInputStream() {
            return in;
        }

        public OutputStream getOutputStream() {
            return out;
        }
    }
}
