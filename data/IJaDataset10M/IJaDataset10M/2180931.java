package architecture.common.adaptor.connector.ftp;

import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import architecture.common.adaptor.Connector;

public class AbstractFtpConnector implements Connector {

    protected Log log = LogFactory.getLog(getClass());

    private FTPClient ftp = new FTPClient();

    private String hostname;

    private int port = 0;

    private String username;

    private String password;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    protected Object pull() {
        try {
            int reply;
            if (port > 0) ftp.connect(hostname, port); else ftp.connect(hostname);
            log.debug("Connected to " + hostname + " on " + (port > 0 ? port : ftp.getDefaultPort()));
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                log.error("FTP server refused connection.");
            }
            if (!ftp.login(username, password)) {
                ftp.logout();
            }
            log.debug("Remote system is " + ftp.getSystemType());
        } catch (Exception e) {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                }
                log.error("FTP server refused connection.");
            }
        }
        return null;
    }
}
