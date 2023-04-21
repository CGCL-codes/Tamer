package org.gudy.azureus2.core3.util.protocol.magnet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import org.gudy.azureus2.core3.util.Debug;
import com.aelitis.net.magneturi.MagnetURIHandler;

/**
 * @author parg
 *
 */
public class MagnetConnection extends HttpURLConnection {

    private Socket socket;

    private static final String NL = "\r\n";

    private String status = "";

    protected MagnetConnection(URL _url) {
        super(_url);
    }

    public void connect() throws IOException {
        socket = new Socket("127.0.0.1", MagnetURIHandler.getSingleton().getPort());
        String get = "GET " + "/download/" + getURL().toString().substring(7) + " HTTP/1.0" + NL + NL;
        socket.getOutputStream().write(get.getBytes());
        socket.getOutputStream().flush();
    }

    public InputStream getInputStream() throws IOException {
        InputStream is = socket.getInputStream();
        String line = "";
        byte[] buffer = new byte[1];
        byte[] line_bytes = new byte[2048];
        int line_bytes_pos = 0;
        while (true) {
            int len = is.read(buffer);
            if (len == -1) {
                break;
            }
            line += (char) buffer[0];
            line_bytes[line_bytes_pos++] = buffer[0];
            if (line.endsWith(NL)) {
                line = line.trim();
                if (line.length() == 0) {
                    break;
                }
                if (line.startsWith("X-Report:")) {
                    line = new String(line_bytes, 0, line_bytes_pos, "UTF-8");
                    line = line.substring(9);
                    line = line.trim();
                    status = Character.toUpperCase(line.charAt(0)) + line.substring(1);
                }
                line = "";
                line_bytes_pos = 0;
            }
        }
        return (is);
    }

    public int getResponseCode() {
        return (HTTP_OK);
    }

    public String getResponseMessage() {
        return (status);
    }

    public boolean usingProxy() {
        return (false);
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (Throwable e) {
            Debug.printStackTrace(e);
        }
    }
}
