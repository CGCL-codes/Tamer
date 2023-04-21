package com.kitten.dao;

public interface IKittenLoginDetailsDao {

    public String getUserName();

    public String getPassword();

    public String getDatabase();

    public String getHost();

    public String getPort();

    public String getDatabaseType();

    public String getConnectionUrl();
}
