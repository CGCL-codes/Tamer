package org.demis.dwarf.configuration;

/**
 * @version 1.0
 * @author <a href="mailto:demis27@demis27.net">Stéphane kermabon</a>
 */
public class DatabaseConfigurationImpl implements DatabaseConfiguration {

    private String databaseUrl = null;

    private String databaseUser = null;

    private String databasePassword = null;

    private String databaseSchemaName = null;

    private SupportedDBMS dbms = null;

    private String datasourceUrl = null;

    public DatabaseConfigurationImpl() {
    }

    @Override
    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    @Override
    public String getDatabaseSchemaName() {
        return databaseSchemaName;
    }

    public void setDatabaseSchemaName(String databaseSchemaName) {
        this.databaseSchemaName = databaseSchemaName;
    }

    @Override
    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    @Override
    public String getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    @Override
    public SupportedDBMS getDbms() {
        return dbms;
    }

    public void setDbms(SupportedDBMS dbms) {
        this.dbms = dbms;
    }

    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public void setDatasourceUrl(String datasourceUrl) {
        this.datasourceUrl = datasourceUrl;
    }
}
