package se.kth.cid.server.applet.conzilla;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * @author enok
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LCPConnector {

    HttpURLConnection http;

    URL url;

    String server;

    String s;

    String method;

    String URI;

    public LCPConnector() {
        this("http://localhost:8095/mywar/HelloServlet");
    }

    public LCPConnector(String server) {
        this.server = server;
    }

    public void setRequestMethod(String method) {
        this.method = method;
    }

    public void setURI(String s) {
        URI = s;
    }

    public String getString() {
        return s;
    }

    public void sendAndRecieveRequest() {
        try {
            url = new URL(server);
            http = (HttpURLConnection) url.openConnection();
            InputStream is;
            OutputStream os;
            String LCPRequest = method + " " + URI + " LCP/0.1\r\n\r\n";
            int curr = 0;
            byte[] b = new byte[1000];
            if (URI == null) {
                http.setRequestMethod("GET");
            } else {
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                os = http.getOutputStream();
                os.write(LCPRequest.getBytes());
            }
            http.connect();
            System.out.println("K?r...");
            is = http.getInputStream();
            curr = is.read(b);
            if (curr > 0) {
                s = new String(b);
                b = new byte[1000];
                while (is.read(b) > 0) {
                    s += new String(b);
                    b = new byte[1000];
                }
            }
        } catch (MalformedURLException mue) {
            System.out.println("MalformedURLException");
        } catch (IOException ioe) {
            System.out.println("IOException" + ioe.getMessage());
        }
    }
}
