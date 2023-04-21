package n2hell.xmlrpc.protocol.scgi;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import n2hell.config.ScgiConfig;
import n2hell.config.SshConfig;
import n2hell.config.UserInfoConfig;
import n2hell.xmlrpc.SSHTransport;
import n2hell.xmlrpc.SocketTransport;
import n2hell.xmlrpc.Transport;

public class Handler extends URLStreamHandler {

    private Map<String, SshConfig> configs = new HashMap<String, SshConfig>();

    @Override
    protected void parseURL(URL url, String arg1, int arg2, int arg3) {
        super.parseURL(url, arg1, arg2, arg3);
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        Transport transport;
        ScgiConfig scgiConfig = new ScgiConfig();
        scgiConfig.setHost(url.getHost());
        scgiConfig.setPort(url.getPort());
        String query = url.getQuery();
        if (query != null) {
            SshConfig sshConfig = configs.get(query);
            if (sshConfig == null) {
                Properties params = Utils.parseQuery(query);
                sshConfig = new SshConfig();
                sshConfig.setCompression(Integer.parseInt(params.getProperty("compression", "0")));
                sshConfig.setEnabled(true);
                sshConfig.setHost(params.getProperty("host"));
                sshConfig.setPassphrase(params.getProperty("passphrase"));
                sshConfig.setPort(Integer.parseInt(params.getProperty("port", "22")));
                sshConfig.setPrivateKeyFile(params.getProperty("privateKeyFile"));
                sshConfig.setUsePrivateKey(Boolean.parseBoolean(params.getProperty("usePrivateKey", "false")));
                UserInfoConfig ui = new UserInfoConfig(params.getProperty("user"), params.getProperty("password"));
                sshConfig.setUser(ui);
                configs.put(query, sshConfig);
            }
            transport = new SSHTransport(sshConfig, scgiConfig);
        } else transport = new SocketTransport(scgiConfig);
        return new ScgiConnection(url, transport);
    }
}
