package dbsync4j.core.concrete;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import schemacrawler.crawl.SchemaCrawler;
import schemacrawler.crawl.SchemaCrawlerOptions;
import schemacrawler.crawl.SchemaInfoLevel;
import schemacrawler.utility.datasource.PropertiesDataSource;
import schemacrawler.utility.datasource.PropertiesDataSourceException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import dbsync4j.core.behavior.DatabaseObject;
import dbsync4j.core.behavior.Schema;
import dbsync4j.core.behavior.Server;
import dbsync4j.core.exception.DataSourceException;
import dbsync4j.core.exception.DataSourceMissingParameters;
import dbsync4j.core.loader.behavior.DatabaseLoader;
import dbsync4j.core.loader.crawler.CrawlerSchemaLoader;
import dbsync4j.core.types.DatabaseType;

/**
 * Abstrai o servidor de banco de dados.
 * 
 * @author Rafael
 *
 */
public class ConcreteServer implements Server, DatabaseObject {

    private DataSource dataSource;

    private String url;

    private String name;

    private String username;

    private String password;

    private DatabaseType databaseType;

    private boolean connected;

    private Schema schema;

    public ConcreteServer() {
    }

    private void makeDataSource() throws DataSourceException, DataSourceMissingParameters {
        validateParameters();
        try {
            final String datasourceName = "dbsync4j";
            final Properties connectionProperties = new Properties();
            connectionProperties.setProperty(datasourceName + ".driver", databaseType.getDriver());
            connectionProperties.setProperty(datasourceName + ".url", url);
            connectionProperties.setProperty(datasourceName + ".user", username);
            connectionProperties.setProperty(datasourceName + ".password", password);
            dataSource = new PropertiesDataSource(connectionProperties, datasourceName);
        } catch (PropertiesDataSourceException e) {
            e.printStackTrace();
            throw new DataSourceException("Error creating dbsync4j datasource to connect server " + name);
        }
    }

    private void validateParameters() throws DataSourceMissingParameters {
        if (username == null || password == null) throw new DataSourceMissingParameters("Username or password is null!");
        if (url == null) throw new DataSourceMissingParameters("Url is not defined!");
        if (databaseType == null) throw new DataSourceMissingParameters("You must set the database type!");
        if (databaseType == null) throw new DataSourceMissingParameters("You must set the name of the server!");
    }

    public void connect() throws DataSourceException, DataSourceMissingParameters {
        connected = false;
        makeDataSource();
        connected = true;
    }

    public void disconnect() throws SQLException {
        dataSource.getConnection().close();
        connected = false;
        dataSource = null;
    }

    @Override
    public Schema getSchema() {
        DatabaseLoader<Schema> schemaLoader = new CrawlerSchemaLoader();
        schemaLoader.setDependantObjectOwner(this);
        final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        options.setAlphabeticalSortForTableColumns(true);
        ConcreteSchema schema = (ConcreteSchema) schemaLoader.loadDatabaseObject(SchemaCrawler.getSchema(dataSource, SchemaInfoLevel.maximum(), options));
        this.schema = schema;
        return schema;
    }

    @Override
    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    @Override
    public void setPassword(String pass) {
        this.password = pass;
    }

    @Override
    public void setServerUrl(String url) {
        this.url = url;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setName(String servername) {
        this.name = servername;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public Connection getCurrentConnection() throws SQLException {
        return (dataSource != null) ? dataSource.getConnection() : null;
    }

    @Override
    public String getCatalogName() {
        return schema.getCatalogName();
    }

    @Override
    public String getFullName() {
        return name + " = " + url;
    }

    @Override
    public String getSchemaName() {
        return schema.getSchemaName();
    }

    @Override
    public String getRemarks() {
        throw new NotImplementedException();
    }

    /**
	 * @return the url
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * @return the username
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * @return the password
	 */
    public String getPassword() {
        return password;
    }
}
