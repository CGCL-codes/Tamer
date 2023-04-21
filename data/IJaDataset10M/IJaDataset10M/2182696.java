package branches.new_server.src;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class SecureHttpClient {

    private OutputStreamWriter toServer;

    private InputStream fromServer;

    private String fileName;

    HttpURLConnection conn;

    public SecureHttpClient() {
    }

    private void connect(URL url) throws IOException {
        String protocol = url.getProtocol();
        if (!protocol.equals("http")) throw new IllegalArgumentException("URL must use 'http:' protocol");
        int port = url.getPort();
        if (port == -1) port = 80;
        fileName = url.getFile();
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        toServer = new OutputStreamWriter(conn.getOutputStream());
        fromServer = conn.getInputStream();
    }

    public ByteArrayOutputStream httpPost(URL HttpURL, String postMsg) throws IOException {
        connect(HttpURL);
        toServer.write(postMsg);
        toServer.flush();
        return pull_buffer();
    }

    public void closeConnection() throws IOException {
        fromServer.close();
        toServer.close();
    }

    protected ByteArrayOutputStream pull_buffer() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fromServer.read(buffer, 0, buffer.length)) != -1) output.write(buffer, 0, bytesRead);
        closeConnection();
        return output;
    }
}
