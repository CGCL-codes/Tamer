package ntlmproxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.httpclient.ConnectMethod;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.ProxyClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.ProxyClient.ConnectResponse;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.httpclient.cookie.CookiePolicy;

public class HttpForwarder extends Thread {

    final Logger log = LoggerFactory.getLogger(HttpForwarder.class);

    ServerSocket ssocket;

    Properties props;

    ExecutorService threadPool = Executors.newCachedThreadPool();

    HttpClient delegateClient;

    HttpClient noDelegateClient;

    public HttpForwarder(Properties props, int lport) throws IOException {
        ssocket = new ServerSocket(lport);
        this.props = props;
        MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
        manager.getParams().setDefaultMaxConnectionsPerHost(20);
        delegateClient = new HttpClient(manager);
        delegateClient.getHostConfiguration().setProxy(props.getProperty(Main.PROXY_DELEGATE_HOST_NAME), Integer.parseInt(props.getProperty(Main.PROXY_DELEGATE_HOST_PORT)));
        delegateClient.getState().setProxyCredentials(new AuthScope(AuthScope.ANY), new NTCredentials(props.getProperty(Main.PROXY_DELEGATE_USERNAME), props.getProperty(Main.PROXY_DELEGATE_PASSWORD), InetAddress.getLocalHost().getHostName(), props.getProperty(Main.PROXY_DELEGATE_DOMAIN)));
        noDelegateClient = new HttpClient(manager);
    }

    static List stripHeadersIn = Arrays.asList(new String[] { "Content-Type", "Content-Length", "Proxy-Connection" });

    static List stripHeadersOut = Arrays.asList(new String[] { "Proxy-Authentication", "Proxy-Authorization" });

    class Handler implements Runnable {

        Socket localSocket;

        ByteBuffer buffer = ByteBuffer.allocate(8192);

        public Handler(Socket localSocket) {
            this.localSocket = localSocket;
        }

        public void run() {
            try {
                HttpParser parser = new HttpParser(localSocket.getInputStream());
                HttpMethod method = null;
                try {
                    while (!parser.parse()) ;
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                    parser.close();
                    return;
                }
                HttpClient client = (Main.noForwardPattern != null && Main.noForwardPattern.matcher(parser.getUri()).find()) ? noDelegateClient : delegateClient;
                if (parser.getMethod().equals("GET")) method = new GetMethod(); else if (parser.getMethod().equals("POST")) method = new PostMethod(); else if (parser.getMethod().equals("HEAD")) method = new HeadMethod(); else if (parser.getMethod().equals("CONNECT")) {
                    doConnect(parser, localSocket.getOutputStream());
                    return;
                } else throw new Exception("Unknown method: " + parser.getMethod());
                if (method instanceof EntityEnclosingMethod) {
                    EntityEnclosingMethod method2 = (EntityEnclosingMethod) method;
                    method2.setRequestEntity(new StreamingRequestEntity(parser));
                }
                method.setURI(new URI(parser.getUri(), true));
                method.setFollowRedirects(false);
                method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
                for (int i = 0; i < parser.getHeaders().length; i++) {
                    Header h = parser.getHeaders()[i];
                    log.debug(h.getName());
                    if (stripHeadersIn.contains(h.getName())) continue;
                    method.addRequestHeader(h);
                }
                client.executeMethod(method);
                localSocket.shutdownInput();
                OutputStream os = localSocket.getOutputStream();
                os.write(method.getStatusLine().toString().getBytes());
                os.write("\r\n".getBytes());
                log.debug(method.getStatusLine().toString());
                Header[] headers = method.getResponseHeaders();
                for (int i = 0; i < headers.length; i++) {
                    if (stripHeadersOut.contains(headers[i])) continue;
                    os.write(headers[i].toExternalForm().getBytes());
                    log.debug(headers[i].toExternalForm());
                }
                InputStream is = method.getResponseBodyAsStream();
                if (is != null) {
                    os.write("\r\n".getBytes());
                    new Piper(is, os).run();
                }
                method.releaseConnection();
                localSocket.close();
            } catch (Exception e) {
                log.debug(e.getMessage(), e);
            }
        }
    }

    public void run() {
        while (true) {
            try {
                Socket s = ssocket.accept();
                threadPool.execute(new Handler(s));
            } catch (IOException e) {
                log.debug(e.getMessage(), e);
                break;
            }
        }
    }

    public void close() throws IOException {
        ssocket.close();
    }

    void doConnect(HttpParser parser, OutputStream os) {
        String[] uri = parser.getUri().split(":");
        ProxyClient client = new ProxyClient();
        client.getHostConfiguration().setHost(uri[0], Integer.parseInt(uri[1]));
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
            os.write(response.getConnectMethod().getStatusLine().toString().getBytes());
            os.write("\r\n\r\n".getBytes());
            threadPool.execute(new Piper(parser, remoteSocket.getOutputStream()));
            new Piper(remoteSocket.getInputStream(), os).run();
            parser.close();
            os.close();
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        } finally {
            if (remoteSocket != null) try {
                remoteSocket.close();
            } catch (Exception fe) {
                log.debug(fe.getMessage(), fe);
            }
            try {
                os.close();
            } catch (IOException e) {
            }
            try {
                parser.close();
            } catch (IOException e) {
            }
        }
    }
}
