package sun.net.www.protocol.mailto;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.io.*;
import sun.net.www.*;
import sun.net.smtp.SmtpClient;

/** open an nntp input stream given a URL */
public class Handler extends URLStreamHandler {

    public synchronized URLConnection openConnection(URL u) {
        return new MailToURLConnection(u);
    }

    /**
     * This method is called to parse the string spec into URL u for a
     * mailto protocol.
     *
     * @param   u the URL to receive the result of parsing the spec
     * @param   spec the URL string to parse
     * @param   start the character position to start parsing at.  This is
     *          just past the ':'.
     * @param   limit the character position to stop parsing at.
     */
    public void parseURL(URL u, String spec, int start, int limit) {
        String protocol = u.getProtocol();
        String host = "";
        int port = u.getPort();
        String file = "";
        if (start < limit) {
            file = spec.substring(start, limit);
        }
        boolean nogood = false;
        if (file == null || file.equals("")) nogood = true; else {
            boolean allwhites = true;
            for (int i = 0; i < file.length(); i++) if (!Character.isWhitespace(file.charAt(i))) allwhites = false;
            if (allwhites) nogood = true;
        }
        if (nogood) throw new RuntimeException("No email address");
        setURL(u, protocol, host, port, file, null);
    }
}
