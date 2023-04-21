package net.jetrix.config;

import net.jetrix.DataSourceManager;

/**
 * Configuration for a pool of connections to a database.
 * 
 * @author Emmanuel Bourg
 * @version $Revision: 797 $, $Date: 2009-02-18 09:03:17 -0500 (Wed, 18 Feb 2009) $
 * @since 0.3
 */
public class DataSourceConfig {

    private String name = DataSourceManager.DEFAULT_DATASOURCE;

    private String url;

    private String driver;

    private String username;

    private String password;

    private int minIdle = DataSourceManager.DEFAULT_MIN_IDLE;

    private int maxActive = DataSourceManager.DEFAULT_MAX_ACTIVE;

    /**
     * Tells if this datasource is the default datasource.
     */
    public boolean isDefault() {
        return DataSourceManager.DEFAULT_DATASOURCE.equals(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }
}
