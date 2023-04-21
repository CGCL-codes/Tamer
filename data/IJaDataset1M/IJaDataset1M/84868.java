package org.gamio.conf;

import java.util.Collection;
import java.util.Map.Entry;

/**
 * @author Agemo Cui <agemocui@gamio.org>
 * @version $Rev: 19 $ $Date: 2008-09-26 19:00:58 -0400 (Fri, 26 Sep 2008) $
 */
public abstract class MsgQueueProps {

    private static final long DEFAULT_INTERVALTORETRY = 10000L;

    private static final long DEFAULT_TIMETOLIVE = 60000L;

    private int internalMessageCacheSize = 0;

    public final int getInternalMessageCacheSize() {
        return internalMessageCacheSize;
    }

    public final void setInternalMessageCacheSize(int internalMessageCacheSize) {
        this.internalMessageCacheSize = internalMessageCacheSize;
    }

    public boolean isJms() {
        return false;
    }

    public int getCapacity() {
        return 0;
    }

    public void setCapacity(int capacity) {
    }

    public String getConnectionFactory() {
        return null;
    }

    public void setConnectionFactory(String connectionFactory) {
    }

    public String getDestQueue() {
        return null;
    }

    public void setDestQueue(String destQueue) {
    }

    public String getInitContextFactory() {
        return null;
    }

    public void setInitContextFactory(String initContextFactory) {
    }

    public String getProviderURL() {
        return null;
    }

    public void setProviderURL(String providerURL) {
    }

    public void setIntervalToRetry(long intervalToRetry) {
    }

    public long getIntervalToRetry() {
        return DEFAULT_INTERVALTORETRY;
    }

    public void setTimeToLive(long timeToLive) {
    }

    public long getTimeToLive() {
        return DEFAULT_TIMETOLIVE;
    }

    public String getPassword() {
        return null;
    }

    public void setPassword(String password) {
    }

    public String getUserName() {
        return null;
    }

    public void setUserName(String userName) {
    }

    public void addJndiProperty(String name, String value) {
    }

    public Collection<Entry<String, String>> getJndiProperties() {
        return null;
    }
}
