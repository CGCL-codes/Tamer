package org.fb4j.connect;

/**
 * @author Mino Togna
 * 
 */
public class Account {

    private String emailHash, url;

    private long user;

    public Account(String emailHash) {
        super();
        this.emailHash = emailHash;
    }

    public String getEmailHash() {
        return emailHash;
    }

    public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }
}
