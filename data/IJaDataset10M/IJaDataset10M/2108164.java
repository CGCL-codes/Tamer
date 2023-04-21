package sf.net.algotrade.comm;

/**
 * HTTP server that responds to commands.
 * 
 * <p>Configurable by parameters.
 *
 */
public class HttpHostEndPoint {

    public static enum Protocol {

        http, https
    }

    ;

    /** IP or DNS address */
    private String hostAddress;

    /** IP port */
    private String port;

    private Protocol protocol;

    /** Connection timeout in ms */
    int connectionTimeoutMs;

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
}
