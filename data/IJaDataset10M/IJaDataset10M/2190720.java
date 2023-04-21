package net.siop.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.siop.Bridge;
import net.siop.Transport;
import net.siop.transport.Text;

public class Pipe {

    public static final int CLIENT_PARTNER_ID = 1;

    public static final int SERVER_PARTNER_ID = 2;

    private Pipe() {
        super();
    }

    public static Bridge standardServer() {
        return standardServer(System.in, System.out);
    }

    public static Bridge standardServer(InputStream input, OutputStream output) {
        return standardServer(input, output, new Object());
    }

    public static Bridge standardServer(InputStream input, OutputStream output, Object context) {
        Transport transport = new Text(input, output, SERVER_PARTNER_ID, CLIENT_PARTNER_ID);
        return new Bridge(transport, context);
    }

    public static Bridge connect(String command) throws IOException {
        return connect(command, new Object());
    }

    public static Bridge connect(String command, Object context) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        Transport transport = new Text(process.getInputStream(), process.getOutputStream(), CLIENT_PARTNER_ID, SERVER_PARTNER_ID);
        return new Bridge(transport, context);
    }

    public static void main(String[] args) {
        try {
            Object context;
            if (args.length > 0) {
                Class klass = Class.forName(args[0]);
                context = klass.newInstance();
            } else {
                context = new Object();
            }
            standardServer(System.in, System.out, context).serve();
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }
}
