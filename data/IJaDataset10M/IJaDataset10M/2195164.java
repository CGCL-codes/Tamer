package org.gamio.conf;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Agemo Cui <agemocui@gamio.org>
 * @version $Rev: 19 $ $Date: 2008-09-26 19:00:58 -0400 (Fri, 26 Sep 2008) $
 */
public final class JmsProps extends MsgQueueProps {

    private String initContextFactory = null;

    private String providerURL = null;

    private String connectionFactory = null;

    private String destQueue = null;

    private String userName = null;

    private String password = null;

    private long intervalToRetry = 10000L;

    private long timeToLive = 60000L;

    private Map<String, String> jndiMap = new LinkedHashMap<String, String>();

    @Override
    public String getConnectionFactory() {
        return connectionFactory;
    }

    @Override
    public void setConnectionFactory(String connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public String getDestQueue() {
        return destQueue;
    }

    @Override
    public void setDestQueue(String destQueue) {
        this.destQueue = destQueue;
    }

    @Override
    public String getInitContextFactory() {
        return initContextFactory;
    }

    @Override
    public void setInitContextFactory(String initContextFactory) {
        this.initContextFactory = initContextFactory;
    }

    @Override
    public String getProviderURL() {
        return providerURL;
    }

    @Override
    public void setProviderURL(String providerURL) {
        this.providerURL = providerURL;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public long getIntervalToRetry() {
        return intervalToRetry;
    }

    @Override
    public void setIntervalToRetry(long intervalToRetry) {
        this.intervalToRetry = intervalToRetry;
    }

    @Override
    public boolean isJms() {
        return true;
    }

    @Override
    public void addJndiProperty(String name, String value) {
        jndiMap.put(name, value);
    }

    @Override
    public Collection<Entry<String, String>> getJndiProperties() {
        return jndiMap.entrySet();
    }

    @Override
    public long getTimeToLive() {
        return timeToLive;
    }

    @Override
    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }
}
