package de.freeradical.jpackrat2.network;

import java.io.IOException;
import java.io.Reader;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.net.SocketFactory;
import org.apache.commons.net.nntp.NNTPClient;

public abstract class AbstractNntpServerConnection implements UsenetConnection {

    protected String hostname;

    protected int port;

    protected String password;

    protected String username;

    protected NNTPClient client = null;

    AbstractNntpServerConnection(String hostname, int port, String username, String password) {
        this.port = port;
        this.hostname = hostname;
        this.username = username;
        this.password = password;
    }

    @Override
    public Reader getArticleBody(String articleId) throws IOException {
        return client.retrieveArticleBody(articleId);
    }

    public abstract SocketFactory getSocketFactory();

    @Override
    public void connect(final NetworkConnectionManager m) throws SocketException, IOException {
        if (client == null) {
            client = new NNTPClient();
            SocketFactory sf = getSocketFactory();
            client.setSocketFactory(sf);
            client.setDefaultTimeout(5000);
            client.connect(hostname, port);
            client.authenticate(username, password);
        }
    }

    @Override
    public void disconnect(final NetworkConnectionManager m) throws IOException {
        if (client != null) {
            client.logout();
            client.disconnect();
            client = null;
        }
    }

    public static List<AbstractNntpServerConnection> getConnection(String serverAddress) {
        List<AbstractNntpServerConnection> result = new ArrayList<AbstractNntpServerConnection>();
        Pattern p = Pattern.compile("(\\w*):(\\d*)//(.*):(.*)@(.*):(\\d*)");
        Matcher m = p.matcher(serverAddress);
        m.find();
        String protocol = m.group(1);
        int threads = Integer.parseInt(m.group(2));
        String username = m.group(3);
        String password = m.group(4);
        String hostname = m.group(5);
        String port = m.group(6);
        if (hostname.isEmpty()) return result;
        if (protocol.equals("nntp")) {
            for (int i = 0; i < threads; i++) {
                result.add(new UnsecureNntpServerConnection(hostname, Integer.parseInt(port), username, password));
            }
        } else if (protocol.equals("nntps")) {
            for (int i = 0; i < threads; i++) {
                result.add(new SecureNntpServerConnection(hostname, Integer.parseInt(port), username, password));
            }
        }
        return result;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
