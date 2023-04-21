package ntlmproxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Properties;
import org.apache.commons.httpclient.ConnectMethod;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.ProxyClient;
import org.apache.commons.httpclient.ProxyClient.ConnectResponse;
import org.apache.commons.httpclient.auth.AuthScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Forwarder extends Thread {

    final Logger log = LoggerFactory.getLogger(Forwarder.class);

    ServerSocket ssocket;

    protected String remoteHost;

    protected int remotePort;

    Properties props;

    public Forwarder(Properties props, int lport, String remoteHost, int remotePort) throws IOException {
        ssocket = new ServerSocket(lport);
        this.remotePort = remotePort;
        this.remoteHost = remoteHost;
        this.props = props;
    }

    class Handler extends Thread {

        Socket localSocket;

        ByteBuffer buffer = ByteBuffer.allocate(8192);

        public Handler(Socket localSocket) {
            this.localSocket = localSocket;
        }

        public void run() {
            ProxyClient client = new ProxyClient();
            client.getHostConfiguration().setHost(remoteHost, remotePort);
            client.getHostConfiguration().setProxy(props.getProperty(Main.PROXY_DELEGATE_HOST_NAME), Integer.parseInt(props.getProperty(Main.PROXY_DELEGATE_HOST_PORT)));
            try {
                client.getState().setProxyCredentials(new AuthScope(AuthScope.ANY), new NTCredentials(props.getProperty(Main.PROXY_DELEGATE_USERNAME), props.getProperty(Main.PROXY_DELEGATE_PASSWORD), InetAddress.getLocalHost().getHostName(), props.getProperty(Main.PROXY_DELEGATE_DOMAIN)));
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }
            Socket remoteSocket = null;
            try {
                ConnectResponse response = client.connect();
                remoteSocket = response.getSocket();
                if (remoteSocket == null) {
                    ConnectMethod method = response.getConnectMethod();
                    throw new IOException("Socket not created: " + method.getStatusLine());
                }
                new Thread(new Piper(localSocket.getInputStream(), remoteSocket.getOutputStream())).start();
                new Piper(remoteSocket.getInputStream(), localSocket.getOutputStream()).run();
            } catch (Exception e) {
                log.debug(e.getMessage(), e);
            } finally {
                if (remoteSocket != null) try {
                    remoteSocket.close();
                } catch (Exception fe) {
                    log.debug(fe.getMessage(), fe);
                }
                if (localSocket != null) try {
                    localSocket.close();
                } catch (Exception fe) {
                    log.debug(fe.getMessage(), fe);
                }
            }
        }
    }

    public void run() {
        while (true) {
            try {
                Socket s = ssocket.accept();
                new Handler(s).start();
            } catch (IOException e) {
                log.debug(e.getMessage(), e);
                break;
            }
        }
    }

    public void close() throws IOException {
        ssocket.close();
    }
}
