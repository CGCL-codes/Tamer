package com.safi.asterisk.handler.connection;

import java.util.Properties;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.manager.SafiManagerConnection;
import com.safi.asterisk.handler.SafiFastAgiServerFactory;
import com.safi.asterisk.handler.SafletEngineException;
import com.safi.asterisk.handler.dispatch.SafletDispatch;
import com.safi.db.manager.DBManagerException;
import com.safi.db.server.config.AsteriskServer;
import com.safi.db.server.config.SafiServer;
import com.safi.server.SafiServerListener;

public interface AsteriskConnectionManager {

    public abstract boolean addListener(SafiServerListener listener);

    public abstract boolean removeListener(SafiServerListener listener);

    public abstract String getServerIpAddress();

    public abstract void beginProcessing() throws SafletEngineException;

    public abstract void stopProcessing();

    public abstract Object setLoopbackLock(String uuid, Object lock);

    public abstract Object getLoopbackCall(String uuid);

    public abstract void handle(final AgiRequest request, final AgiChannel channel);

    public abstract void handle(String saflet, String astIp, Properties properties, ExceptionHandler exceptionHandler);

    public abstract SafletDispatch getDispatcher();

    public abstract void setDispatcher(SafletDispatch dispatcher);

    public abstract SafiManagerConnection getManagerConnection(Integer key);

    public abstract void setSafiServerConfig(SafiServer safiServer);

    public abstract void safiServerChanged(boolean startup);

    public abstract void asteriskServerAdded(AsteriskServer astServer);

    public abstract void asteriskServerRemoved(Integer id);

    public abstract void asteriskServerModified(Integer id);

    public abstract void setUseBlocking(boolean blocking);

    public abstract void setTesting(boolean b);

    public abstract void setAgiServerFactory(SafiFastAgiServerFactory factory);

    public abstract void initManagerConnections() throws SafletEngineException;

    public abstract void initSafiServer();

    public abstract void initDBResources() throws DBManagerException;

    public abstract void setManagementPort(int managementPort);

    public abstract void notifyServerStarted();

    void notifyServerStopped(String msg);

    SafiServer getSafiServerConfig();

    public abstract int getNumAgiRequests();

    public abstract int getNumCustomInitiations();

    public abstract void setUsePing(boolean usePing);

    public abstract boolean isUsePing();

    long getManagerRetryPeriod();

    void setManagerRetryPeriod(long managerRetryPeriod);
}
