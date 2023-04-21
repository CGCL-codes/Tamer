package com.leclercb.taskunifier.api.synchronizer;

import java.util.Properties;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;

public abstract class SynchronizerApi {

    private String apiId;

    private String apiName;

    private String apiWebSite;

    protected String proxyHost;

    protected int proxyPort;

    protected String proxyUsername;

    protected String proxyPassword;

    protected SynchronizerApi(String apiId, String apiName, String apiWebSite) {
        this.setApiId(apiId);
        this.setApiName(apiName);
        this.setApiWebSite(apiWebSite);
        this.setProxyHost(null);
        this.setProxyPort(0);
        this.setProxyUsername(null);
        this.setProxyPassword(null);
    }

    public String getApiId() {
        return this.apiId;
    }

    public void setApiId(String apiId) {
        CheckUtils.isNotNull(apiId);
        this.apiId = apiId;
    }

    public String getApiName() {
        return this.apiName;
    }

    public void setApiName(String apiName) {
        CheckUtils.isNotNull(apiName);
        this.apiName = apiName;
    }

    public String getApiWebSite() {
        return this.apiWebSite;
    }

    public void setApiWebSite(String apiWebSite) {
        CheckUtils.isNotNull(apiWebSite);
        this.apiWebSite = apiWebSite;
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return this.proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUsername() {
        return this.proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return this.proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public String[] getDefaultRepeatValues() {
        return new String[0];
    }

    public boolean isValidRepeatValue(String repeat) {
        return false;
    }

    public void createRepeatTask(Task task) {
    }

    public abstract Connection getConnection(Properties properties) throws SynchronizerException;

    public abstract Synchronizer getSynchronizer(Properties properties, Connection connection) throws SynchronizerException;

    public abstract void resetConnectionParameters(Properties properties);

    public abstract void resetSynchronizerParameters(Properties properties);
}
