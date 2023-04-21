package gnu.hylafax.pool;

public class ClientPoolConfiguration {

    private String adminPassword = null;

    private long blockingTimeout = 0;

    private String host = null;

    private long maxIdleTime = 10 * 60 * 1000;

    private long maxNoopTime = 10000;

    private int maxPoolSize = 5;

    private int minPoolSize = 1;

    private String password = null;

    private boolean pooling = true;

    private int port = -1;

    private long retryInterval = 100;

    private String timeZone = null;

    private String userName = null;

    /**
     * @return the admin password.
     */
    public String getAdminPassword() {
        return adminPassword;
    }

    /**
     * @return the blocking timeout in milliseconds.
     */
    public long getBlockingTimeout() {
        return blockingTimeout;
    }

    /**
     * @return the host string
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the maximum time in milliseconds for a client to be idle before
     *         reopening a connection.
     */
    public long getMaxIdleTime() {
        return maxIdleTime;
    }

    /**
     * @return the maximum time in millisecondes for a client to be idle between
     *         noop commands.
     */
    public long getMaxNoopTime() {
        return maxNoopTime;
    }

    /**
     * @return the maximum number of clients in the client pool.
     */
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
     * @return the minimum number of clients in the client pool.
     */
    public int getMinPoolSize() {
        return minPoolSize;
    }

    /**
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the port number to be used.
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the number of milliseconds to wait before checking the stack for
     *         a returned client.
     */
    public long getRetryInterval() {
        return retryInterval;
    }

    /**
     * @return the time zone string.
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * @return the user name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return true if pooling is on.
     */
    public boolean isPooling() {
        return pooling;
    }

    /**
     * @param adminPassword the administrator password to set.
     */
    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    /**
     * The number of milliseconds to wait for a client before throwing an
     * exception.
     * @param blockingTimeout
     */
    public void setBlockingTimeout(long blockingTimeout) {
        this.blockingTimeout = blockingTimeout;
    }

    /**
     * @param host the host string.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @param maxIdleTime the number of milliseconds for idle clients to remain
     *        open before reopening.
     */
    public void setMaxIdleTime(long maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    /**
     * @param maxNoopTime the number of milliseconds for idle clients to wait
     *        between noop commands.
     */
    public void setMaxNoopTime(long maxNoopTime) {
        this.maxNoopTime = maxNoopTime;
    }

    /**
     * @param maxPoolSize maximum number of connections to maintain in the pool.
     */
    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    /**
     * @param minPoolSize minimum number of connections to maintain in the pool.
     */
    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    /**
     * @param password the password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param pooling set to true if client pooling is desired.
     */
    public void setPooling(boolean pooling) {
        this.pooling = pooling;
    }

    /**
     * @param port the port number to set.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @param retryInterval the number of milliseconds to wait before rechecking
     *        the pool for returned clients.
     */
    public void setRetryInterval(long retryInterval) {
        this.retryInterval = retryInterval;
    }

    /**
     * @param timeZone the timezone string to set.
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * @param userName the user name to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
