package org.one.stone.soup.authentication.server;

public class Login {

    private String domain;

    private String subdomain;

    private User user;

    private boolean loggedIn = false;

    private String password;

    private String pebble;

    private long logginTime;

    private long lastAccessTime;

    private long timeoutTime;

    private long clientTime;

    private long serverTimeOffset;

    private String result = "Not Processed";

    public Login() {
    }

    public Login(String domain, String userName, String password) {
        this.domain = domain;
        this.user = new User();
        user.setName(userName);
        setClientTime(System.currentTimeMillis());
        this.password = password;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setResult(boolean passed, String result) {
        this.loggedIn = passed;
        this.result = result;
    }

    public String getPebble() {
        return pebble;
    }

    public void setPebble(String pebble) {
        this.pebble = pebble;
    }

    public long getLogginTime() {
        return logginTime;
    }

    public void setLogginTime(long logginTime) {
        this.logginTime = logginTime;
    }

    public long getTimeoutTime() {
        return timeoutTime;
    }

    public void setTimeoutTime(long timeoutTime) {
        this.timeoutTime = timeoutTime;
    }

    public long timeLeft() {
        return (lastAccessTime + timeoutTime) - (serverTimeOffset + System.currentTimeMillis());
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public long getClientTime() {
        return clientTime;
    }

    public void setClientTime(long clientTime) {
        this.clientTime = clientTime;
    }

    public long getServerTimeOffset() {
        return serverTimeOffset;
    }

    public void setServerTimeOffset(long serverTimeOffset) {
        this.serverTimeOffset = serverTimeOffset;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
