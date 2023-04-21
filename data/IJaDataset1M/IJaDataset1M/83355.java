package n2hell.xmlrpc.protocol.scgi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;
import n2hell.utils.FileUtils;
import n2hell.xmlrpc.SCGI;
import n2hell.xmlrpc.Transport;

public class ScgiConnection extends HttpURLConnection {

    private final Transport transport;

    private ByteArrayOutputStream outStream;

    public ScgiConnection(URL url) {
        super(url);
        transport = null;
    }

    public ScgiConnection(URL url, Transport transport) {
        super(url);
        this.transport = transport;
    }

    @Override
    public void connect() throws IOException {
        transport.connect();
    }

    @Override
    public void disconnect() {
        if (transport != null) try {
            transport.close();
        } catch (IOException e) {
        }
    }

    @Override
    public boolean usingProxy() {
        return false;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (!transport.isConnected()) connect();
        final byte[] buffer = new byte[2048];
        BufferedInputStream input = new BufferedInputStream(transport.getInputStream());
        String line = Utils.readLine(input, buffer);
        StringTokenizer tokens = new StringTokenizer(line);
        if (!tokens.hasMoreTokens()) {
            throw new IOException("Unexpected Response from Server: " + line);
        }
        tokens.nextToken();
        String statusCode = tokens.nextToken();
        String statusMsg = tokens.nextToken("\n\r");
        if (!"200".equals(statusCode)) {
            throw new IOException("Unexpected Response from Server: " + statusMsg);
        }
        int contentLength = -1;
        for (; ; ) {
            line = Utils.readLine(input, buffer);
            if (line == null || "".equals(line)) {
                break;
            }
            line = line.toLowerCase();
            if (line.startsWith("content-length:")) {
                contentLength = Integer.parseInt(line.substring("content-length:".length()).trim());
            }
        }
        String res = FileUtils.read(input, "UTF-8");
        InputStream result = FileUtils.getInputStream(res, "UTF-8");
        return result;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (!transport.isConnected()) connect();
        outStream = new ByteArrayOutputStream() {

            @Override
            public void flush() throws IOException {
                super.flush();
                if (outStream.size() > 0) {
                    String res = new String(outStream.toByteArray());
                    reset();
                    String message = SCGI.make(null, res);
                    OutputStream out = new BufferedOutputStream(transport.getOutputStream());
                    out.write(message.getBytes("UTF-8"));
                    out.flush();
                    transport.shutdownOutput();
                }
            }

            @Override
            public void close() throws IOException {
                flush();
                super.close();
            }
        };
        return outStream;
    }
}
