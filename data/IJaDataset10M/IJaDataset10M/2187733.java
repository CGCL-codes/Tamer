package com.jiexplorer.rcp.db;

import java.util.Properties;

public interface IMyConnectionProfile {

    public String getProviderName();

    public void setPorviderName(String name);

    public String getConnectionUrl();

    public void setDriverClassName(String provider);

    public String getDriverClassName();

    public void setHost(String host);

    public String getHost();

    public void setPort(String port);

    public String getPort();

    public void setDatabase(String database);

    public String getDatabase();

    public void setUserId(String userid);

    public String getUserId();

    public void setUserPasswd(String passwd);

    public String getUserPasswd();

    public Properties getSchemaProperties();
}
