package fi.mmmtike.tiira.invoker.http.client;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor;
import fi.mmmtike.tiira.logging.TiiraLogger;

/**
 * Tiira implementation of http executor. This uses CommonsHttpInvokerRequestExecutor
 * that supports keepalive messages. 
 * <p>
 * Default max connections per host is by default 10. Call setDefaultMaxConnectionsPerHost method
 * or init environment variable TIIRA_DEFAULT_MAX_CONNECTIONS to change this value.
 * 
 * @author Tomi Tuomainen 
 *
 */
public class TiiraHttpInvokerRequestExecutor extends CommonsHttpInvokerRequestExecutor {

    private static TiiraLogger logger = new TiiraLogger(TiiraHttpInvokerRequestExecutor.class);

    public static final String TIIRA_DEFAULT_MAX_CONNECTIONS = "TIIRA_DEFAULT_MAX_CONNECTIONS";

    public static final String TIIRA_SO_TIMEOUT = "TIIRA_SO_TIMEOUT";

    public static final int DEFAULT_MAX_CONNECTIONS_VALUE_IF_NOT_SET = 10;

    public static final int DEFAULT_SO_TIMEOUT_IF_NOT_SET = 1000 * 60 * 5;

    private int defaultMaxConnectionsPerHost;

    private int soTimeout;

    public TiiraHttpInvokerRequestExecutor() {
        initDefaults();
        initConnections();
    }

    private void initDefaults() {
        this.defaultMaxConnectionsPerHost = fetchValue(TIIRA_DEFAULT_MAX_CONNECTIONS, DEFAULT_MAX_CONNECTIONS_VALUE_IF_NOT_SET);
        this.soTimeout = fetchValue(TIIRA_SO_TIMEOUT, DEFAULT_SO_TIMEOUT_IF_NOT_SET);
    }

    private int fetchValue(String property, int defaultValue) {
        String stringValue = System.getProperty(property);
        if (stringValue == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(stringValue);
            } catch (NumberFormatException ex) {
                logger.error("Invalid parameter " + property + "=" + stringValue + ". Using " + defaultValue);
                return defaultValue;
            }
        }
    }

    private void initConnections() {
        MultiThreadedHttpConnectionManager connManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = connManager.getParams();
        params.setDefaultMaxConnectionsPerHost(defaultMaxConnectionsPerHost);
        params.setSoTimeout(soTimeout);
        connManager.setParams(params);
        this.getHttpClient().setHttpConnectionManager(connManager);
    }

    public int getDefaultMaxConnectionsPerHost() {
        return defaultMaxConnectionsPerHost;
    }

    public void setDefaultMaxConnectionsPerHost(int defaultMaxConnectionsPerHost) {
        this.defaultMaxConnectionsPerHost = defaultMaxConnectionsPerHost;
    }
}
