package syntelos.net.http;

import alto.sys.Reference;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

/**
 * <p> This connection is a socket based structure, not a socket pool
 * user.  To avoid the vagaries of a secret black box socket pool for
 * persistent HTTP connections, this class provides a public method to
 * change the URL for this connection.  </p>
 * 
 * <h3>Usage</h3>
 * 
 * <p> 
 * <pre>
 * (set/get request)|(get output stream; write request entity body)
 * (connect)
 * (get response)|(get input stream; read response entity body)
 * </pre>
 * 
 * The client connects, writing the request message, then the
 * client user code may write a request entity body, then the
 * client reads, and eventually disconnects.  </p>
 * 
 * <h3>SHM</h3>
 * 
 * <p> Using the SHM, don't forget to lock. </p>
 * 
 * @author jdp
 * @since 1.5
 */
public final class Connection extends java.net.HttpURLConnection implements alto.net.Connection {

    public static final boolean SameProtocol(URL a, URL b) {
        return (a.getProtocol().equalsIgnoreCase(b.getProtocol()));
    }

    public static final boolean SameHost(URL a, URL b) {
        return (a.getHost().equalsIgnoreCase(b.getHost()));
    }

    public static final boolean SamePort(URL a, URL b) {
        int ap = a.getPort();
        int bp = b.getPort();
        if (ap == bp) return true; else if (SameProtocol(a, b)) {
            if (-1 == ap) {
                if (80 == bp && "http".equalsIgnoreCase(a.getProtocol())) return true; else if (443 == bp && "https".equalsIgnoreCase(a.getProtocol())) return true;
            } else if (-1 == bp) {
                if (80 == ap && "http".equalsIgnoreCase(a.getProtocol())) return true; else if (443 == ap && "https".equalsIgnoreCase(a.getProtocol())) return true;
            }
        }
        return false;
    }

    public static final String[] Terminal(String s) {
        if (null == s) return null; else {
            int idx = s.indexOf(';');
            if (-1 < idx) {
                if (0 == idx) return new String[] { "", s.substring(1) }; else return new String[] { s.substring(0, idx), s.substring(idx + 1) };
            } else return null;
        }
    }

    private final InetAddress host;

    private final boolean secure;

    private final int port;

    private Reference reference;

    private String terminal;

    private alto.lang.Socket socket;

    private Request request;

    private Response response;

    public Connection(URL url) {
        super(url);
        try {
            this.secure = ("https".equals(url.getProtocol()));
            java.lang.String host = url.getHost();
            this.host = InetAddress.getByName(host);
            int port = url.getPort();
            if (0 < port) this.port = port; else if (this.secure) this.port = 443; else this.port = 80;
            this.request = new Request(this);
        } catch (java.net.UnknownHostException exc) {
            throw new java.lang.IllegalStateException(url.getHost(), exc);
        }
    }

    /**
     * Intended to permit HTTP client protocol over alternative
     * sockets including UDP, MDP and SHM.
     */
    public Connection(URL url, alto.lang.Socket socket) {
        super(url);
        this.socket = socket;
        this.host = null;
        this.port = -1;
        this.request = new Request(this);
        this.secure = false;
    }

    public Reference getReference() {
        return this.reference;
    }

    public void setReference(Reference r) {
        this.reference = r;
    }

    public boolean isSecure() {
        return (this.secure);
    }

    /**
     * Get or create socket.
     */
    public alto.lang.Socket getSocket() throws java.io.IOException {
        if (0 < this.port) {
            if (null == this.socket) this.socket = new Socket(this); else if (this.socket.isNotConnected()) {
                if (this.socket.isNotClosed()) this.socket.close();
                this.socket = new Socket(this);
            } else if (this.socket.isClosed()) {
                this.socket = new Socket(this);
            }
        }
        return this.socket;
    }

    public URL getRequestUrl() {
        return this.url;
    }

    /**
     * Change URL before {@link #connect()}.
     */
    public void setRequestUrl(URL url) {
        this.ensureInit();
        if (SameHost(this.url, url)) {
            if (SamePort(this.url, url)) {
                this.url = url;
            } else throw new IllegalArgumentException("Port changed.");
        } else throw new IllegalArgumentException("Host changed.");
    }

    public java.net.InetAddress getHostAddress() {
        return this.host;
    }

    public java.lang.String getHost() {
        return this.url.getHost();
    }

    public java.lang.String getHostHeader() {
        String header = this.url.getHost();
        int port = this.url.getPort();
        if (0 < port) return header + ':' + port; else return header;
    }

    public java.lang.String getPath() {
        String re = this.url.getPath();
        if (null != re && 0 < re.length()) {
            String[] p = Terminal(re);
            if (null != p) {
                String a = p[0];
                String b = p[1];
                if (0 < b.length()) this.terminal = b;
                if (0 < a.length()) return a; else return "/";
            } else return re;
        } else return "/";
    }

    public boolean hasQuery() {
        String re = this.url.getQuery();
        return (null != re && 0 < re.length());
    }

    public java.lang.String getQuery() {
        String re = this.url.getQuery();
        if (null != re && 0 < re.length()) {
            String[] p = Terminal(re);
            if (null != p) {
                String a = p[0];
                String b = p[1];
                if (0 < b.length()) this.terminal = b;
                if (0 < a.length()) return a; else return null;
            } else return re;
        } else return null;
    }

    public java.lang.String getQuerySuffix() {
        String re = this.getQuery();
        if (null != re) return '?' + re; else return null;
    }

    public java.lang.String getQuerySuffixPath() {
        String re = this.getQuery();
        if (null != re) return '?' + re; else return "";
    }

    public boolean hasFragment() {
        String re = this.url.getRef();
        return (null != re && 0 < re.length());
    }

    public java.lang.String getFragment() {
        String re = this.url.getRef();
        if (null != re && 0 < re.length()) {
            String[] p = Terminal(re);
            if (null != p) {
                String a = p[0];
                String b = p[1];
                if (0 < b.length()) this.terminal = b;
                if (0 < a.length()) return a; else return null;
            } else return re;
        } else return null;
    }

    public java.lang.String getFragmentSuffixPath() {
        java.lang.String re = this.getFragment();
        if (null != re) return '#' + re; else return "";
    }

    public boolean hasTerminal() {
        return (null != this.terminal);
    }

    public String getTerminal() {
        return this.terminal;
    }

    public String getTerminalSuffixPath() {
        String re = this.getTerminal();
        if (null != re) return ';' + re; else return "";
    }

    public int getHostPort() {
        return this.port;
    }

    public int getPort() {
        return this.url.getPort();
    }

    @Override
    public boolean usingProxy() {
        return false;
    }

    /**
     * Initiate or reinitiate request, response sequence.  Reuse
     * persistent HTTP connections as appropriate to the network
     * protocol.
     * 
     * Write HTTP Request Message (request line and headers),
     * permitting subsequent output (writing) to request entity body.
     */
    public void connect() throws java.io.IOException {
        if (null != this.request) {
            if (null == this.response) {
                this.connected = false;
                alto.lang.Socket socket = this.getSocket();
                socket.connect();
                this.request.connect(socket);
                this.connected = true;
            } else {
                alto.lang.Socket socket = this.getSocket().connect();
                try {
                    this.request.connect(socket);
                    this.connected = true;
                } catch (java.net.SocketException disconnected) {
                    socket = this.socket;
                    try {
                        this.socket = null;
                        socket.close();
                    } catch (java.lang.Throwable ignore) {
                    }
                    this.response = null;
                    this.connect();
                }
            }
        } else {
            this.connected = false;
            throw new java.lang.IllegalStateException("disconnected");
        }
    }

    public void release() {
    }

    /**
     * The INIT state exists before the first connect, and after get
     * status or get input stream before a secondary connect.
     */
    protected Request ensureInit() {
        if (this.connected) {
            if (null == this.response) throw new IllegalStateException("Already connected");
        }
        return this.getRequest();
    }

    /**
     * 
     */
    public Request getRequest() {
        if (null == this.request) {
            this.connected = false;
            this.request = new Request(this);
        } else if (null != this.response) {
            try {
                this.response.disconnect();
            } catch (java.io.IOException exc) {
            }
            this.response = null;
            this.connected = false;
            this.request = new Request(this, this.request);
        }
        return this.request;
    }

    /**
     * 
     */
    public Response getResponse() throws java.io.IOException {
        if (!this.connected) this.connect();
        if (null == this.response) {
            this.response = new Response(this.request);
        }
        return this.response;
    }

    /**
     * Destroy, require new connection instance object for a
     * subsequent call to connect.
     */
    public void disconnect() {
        this.connected = false;
        alto.lang.Socket socket = this.socket;
        if (null != socket) {
            try {
                socket.close();
            } catch (java.io.IOException ignore) {
            }
        }
        Response response = this.response;
        if (null != response) {
            try {
                response.disconnect();
            } catch (java.io.IOException ignore) {
            } finally {
                this.request = null;
                this.response = null;
                this.socket = null;
            }
        } else {
            Request request = this.request;
            if (null != request) {
                try {
                    request.disconnect();
                } catch (java.io.IOException ignore) {
                } finally {
                    this.request = null;
                    this.response = null;
                    this.socket = null;
                }
            }
        }
    }

    public void write(alto.lang.HttpMessage container) throws java.io.IOException {
        if (this.connected) throw new IllegalStateException("Already connected"); else {
            Request request = this.getRequest();
            container.copyTo(request);
        }
    }

    public java.io.OutputStream getOutputStream() throws java.io.IOException {
        return this.getRequest().openOutputStream();
    }

    public java.io.InputStream getInputStream() throws java.io.IOException {
        return this.getResponse().openInputStream();
    }

    public int getResponseCode() throws java.io.IOException {
        return this.getResponse().getStatusCode();
    }

    public String getResponseMessage() throws java.io.IOException {
        return this.getResponse().getStatusMessage();
    }

    public String readResponseBodyLine() throws java.io.IOException {
        return this.getResponse().readLine();
    }

    public alto.lang.Value readResponseBodyLineValue() throws java.io.IOException {
        return this.getResponse().readLineValue();
    }

    public void setRequestProperty(java.lang.String key, java.lang.String value) {
        this.ensureInit().setHeader(key, value);
    }

    public void addRequestProperty(java.lang.String key, java.lang.String value) {
        this.ensureInit().setHeader(key, value);
    }

    public java.lang.String getRequestProperty(java.lang.String key) {
        return this.ensureInit().getHeaderString(key);
    }

    public int getContentLength() {
        return this.getHeaderFieldInt("Content-Length", -1);
    }

    public String getContentType() {
        return this.getHeaderField("Content-Type");
    }

    public String getContentEncoding() {
        return this.getHeaderField("Content-Encoding");
    }

    public long getExpiration() {
        return this.getHeaderFieldDate("Expires", 0);
    }

    public long getDate() {
        return this.getHeaderFieldDate("Date", 0);
    }

    public long getLastModified() {
        return this.getHeaderFieldDate("Last-Modified", 0);
    }

    public java.lang.String getHeaderField(java.lang.String name) {
        if (null == this.response) throw new IllegalStateException("Not connected"); else return this.response.getHeaderString(name);
    }

    public java.lang.String getHeaderFieldKey(int idx) {
        if (null == this.response) throw new IllegalStateException("Not connected"); else return this.response.getHeaderName(idx);
    }

    public java.lang.String getHeaderField(int idx) {
        if (null == this.response) throw new IllegalStateException("Not connected"); else return this.response.getHeaderValue(idx);
    }

    public void setRequestMethod(java.lang.String method) throws ProtocolException {
        this.ensureInit();
        this.method = method.toUpperCase();
        this.request.setMethod(this.method);
    }

    public final boolean lockReadEnterTry() {
        alto.lang.Socket socket = this.socket;
        if (null != socket) return socket.lockReadEnterTry(); else return true;
    }

    public final void lockReadEnter() {
        alto.lang.Socket socket = this.socket;
        if (null != socket) socket.lockReadEnter();
        return;
    }

    public final void lockReadExit() {
        alto.lang.Socket socket = this.socket;
        if (null != socket) socket.lockReadExit();
        return;
    }

    public final boolean lockWriteEnterTry() {
        alto.lang.Socket socket = this.socket;
        if (null != socket) return socket.lockWriteEnterTry(); else return true;
    }

    public final boolean lockWriteEnterTry(byte cur, byte nex) {
        alto.lang.Socket socket = this.socket;
        if (null != socket) socket.lockWriteEnterTry(cur, nex);
        return true;
    }

    public final void lockWriteEnter() {
        alto.lang.Socket socket = this.socket;
        if (null != socket) socket.lockWriteEnter();
        return;
    }

    public final boolean lockWriteEnter(byte cur, byte nex) {
        alto.lang.Socket socket = this.socket;
        if (null != socket) return socket.lockWriteEnter(cur, nex); else return true;
    }

    public final void lockWriteExit() {
        alto.lang.Socket socket = this.socket;
        if (null != socket) socket.lockWriteExit();
        return;
    }

    public final boolean isShm() {
        return (null != this.socket && this.socket.isShm());
    }

    public final boolean isNotShm() {
        return (!this.isShm());
    }

    public final boolean isOpen() {
        return (null != this.request);
    }

    public final boolean isNotOpen() {
        return (!this.isOpen());
    }
}
