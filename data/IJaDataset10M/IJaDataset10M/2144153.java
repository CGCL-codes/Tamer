package com.modelmetrics.cloudconverter.dirtdb;

public class DatabaseCredentialsBuilder {

    public static DatabaseCredentials getDerbySample() {
        return DatabaseCredentialsBuilder.getSetInfo("derby", "jdbc:derby:./src/sampledbs/derby/sample1", "sa", "", "Select * from mytable");
    }

    public static DatabaseCredentials getDerbySampleComplex() {
        return DatabaseCredentialsBuilder.getSetInfo("derby", "jdbc:derby:./src/sampledbs/derby/sample2", "sa", "", "Select * from mytable");
    }

    public static DatabaseCredentials getSetInfo(String type, String name, String username, String password, String sql) {
        DatabaseCredentials dbCreds = new DatabaseCredentials();
        dbCreds.setDatabaseName(name);
        dbCreds.setDatabaseType(type);
        dbCreds.setUsername(username);
        dbCreds.setPassword(password);
        dbCreds.setSql(sql);
        return dbCreds;
    }
}
