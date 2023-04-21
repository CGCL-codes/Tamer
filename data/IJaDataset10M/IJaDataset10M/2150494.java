package org.jredis.client.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

public class RedisConnectionPoolConfiguration implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3052180612039586565L;

    public static RedisConnectionPoolConfiguration readDefaultConfiguration() {
        if (DEFAULT_CONFIG == null) {
            DEFAULT_CONFIG = new RedisConnectionPoolConfiguration();
            InputStream is = RedisConnectionPoolConfiguration.class.getResourceAsStream("/RedisConnectionPoolConfiguration.properties");
            DEFAULT_CONFIG.readConfiguration(is);
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return DEFAULT_CONFIG;
    }

    private static RedisConnectionPoolConfiguration DEFAULT_CONFIG = null;

    private String host = "localhost";

    private int port = 6379;

    private int minIdle = 0;

    private int maxIdle = 5;

    private int maxActive = 10;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    private void readConfiguration(InputStream is) {
        Properties p = new Properties();
        try {
            p.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Error loading Redis Pool Configuration: " + e.getLocalizedMessage(), e);
        }
        String value = p.getProperty("host");
        if (value != null && value.trim().length() > 0) this.setHost(value.trim());
        value = p.getProperty("port");
        if (value != null && value.trim().length() > 0) this.setPort(Integer.parseInt(value.trim()));
        value = p.getProperty("maxActive");
        if (value != null && value.trim().length() > 0) this.setMaxActive(Integer.parseInt(value.trim()));
        value = p.getProperty("maxIdle");
        if (value != null && value.trim().length() > 0) this.setMaxIdle(Integer.parseInt(value.trim()));
        value = p.getProperty("minIdle");
        if (value != null && value.trim().length() > 0) this.setMinIdle(Integer.parseInt(value.trim()));
    }
}
