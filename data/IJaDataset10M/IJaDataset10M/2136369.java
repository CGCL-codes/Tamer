package org.clin4j.framework.domain.mock;

import java.io.Serializable;

public class Driver implements Serializable {

    private String driverName;

    private String url;

    private String user;

    private String password;

    public Driver(String driverName, String url, String user, String password) {
        this.driverName = driverName;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String getDriverName() {
        return this.driverName;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }
}
