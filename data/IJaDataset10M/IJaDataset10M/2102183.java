package com.identisense.data.model.config;

import java.io.Serializable;

public class DatabaseConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private DatabaseType databaseType;

    private String adminUser;

    private String adminPassword;

    private String host;

    private int port;

    private String databaseName;

    private String cameoUser;

    private String cameoPassword;

    public DatabaseConfig() {
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getCameoUser() {
        return cameoUser;
    }

    public void setCameoUser(String cameoUser) {
        this.cameoUser = cameoUser;
    }

    public String getCameoPassword() {
        return cameoPassword;
    }

    public void setCameoPassword(String cameoPassword) {
        this.cameoPassword = cameoPassword;
    }

    public String getJdbcUrl() {
        String url = "";
        if (DatabaseType.DERBY.equals(getDatabaseType())) {
            url = "jdbc:derby://" + getHost() + ":" + getPort() + "/" + getDatabaseName() + ";create=true";
        } else if (DatabaseType.ORACLE.equals(getDatabaseType())) {
            url = "jdbc:oracle:thin:@" + getHost() + ":" + getPort() + ":" + getDatabaseName();
        } else if (DatabaseType.MYSQL.equals(getDatabaseType())) {
            url = "jdbc:mysql://" + getHost() + "/" + getDatabaseName();
        } else {
            throw new IllegalStateException("Unknown database type.");
        }
        return url;
    }

    public String getDriver() {
        return getDatabaseType().getDriverName();
    }

    public String getCreateDbFile() {
        return getDatabaseType().getCreateDbFile();
    }

    public String getCreateUserFile() {
        return getDatabaseType().getCreateUserFile();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{").append("type: ").append(getDatabaseType()).append(",");
        builder.append(" name: ").append(getDatabaseName()).append(",");
        builder.append(" host: ").append(getHost()).append(",");
        builder.append(" port: ").append(getPort()).append(",");
        builder.append(" admin user: ").append(getAdminUser()).append(",");
        builder.append(" cameo user: ").append(getCameoUser()).append(" }");
        return builder.toString();
    }
}
