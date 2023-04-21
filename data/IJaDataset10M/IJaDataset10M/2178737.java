package de.schwarzrot.app.config;

import java.io.File;
import java.util.Map;

/**
 * interface to hold all properties necessary to configure a database connection
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * 
 */
public interface DBConfig {

    /**
     * returns the type of a data storage. Currently only mysql databases
     * supported
     * 
     * @return - the type of the data storage
     */
    public String getDbType();

    /**
     * returns the classname of the JDBC driver
     * 
     * @return - the classname of the JDBC driver
     */
    public String getDrvClassName();

    /**
     * returns the path of the jar-file that contains a jdbc-driver
     * 
     * @return - the path of the jar-file that contains a jdbc-driver
     */
    public File getDrvLibPath();

    /**
     * returns the url used by the JDBC driver to access a data storage
     * 
     * @return - the url used by the JDBC driver to access a database
     */
    public String getDrvUrl();

    /**
     * returns the name of a data storage
     * 
     * @return - the name of a data storage, i.e. name of a database
     */
    public String getDsDB();

    /**
     * returns the hostname of the data storage server
     * 
     * @return - the hostname of the data storage server
     */
    public String getDsHost();

    /**
     * returns the password used to access a data storage
     * 
     * @return - the password used to access the data storage
     */
    public String getDsPassword();

    /**
     * SRJRCFrames supports database schema usage even on databases, that don't
     * support schemata. This support is seamless, so there is no difference for
     * the user/developer in using a database that supports schemata or a
     * database whithout that support.
     * 
     * @return - the default schema to use
     */
    public String getDsSchema();

    /**
     * returns the username used to access a data storage
     * 
     * @return - the user name used to access the data storage
     */
    public String getDsUser();

    public Map<String, Boolean> getSupportedDBDrivers();

    /**
     * sets the type of the database (currently only mysql supported)
     * 
     * @param dbType
     *            - the type of the data storage
     */
    public void setDbType(String dbType);

    /**
     * sets the classname of the JDBC-driver
     * 
     * @param drvClassName
     *            - the classname of the driver
     */
    public void setDrvClassName(String drvClassName);

    /**
     * sets the url to access the data storage
     * 
     * @param url
     *            - url used to access the data storage
     */
    public void setDrvUrl(String url);

    /**
     * sets the name of the data storage, i.e. the name of a database
     * 
     * @param dsDB
     *            - name of the data storage
     */
    public void setDsDB(String dsDB);

    /**
     * sets the hostname of the server, that hosts the data storage
     * 
     * @param dsHost
     *            - hostname of data storage server
     */
    public void setDsHost(String dsHost);

    /**
     * sets the password used to access the data storage
     * 
     * @param dsPassword
     *            - password for data storage
     */
    public void setDsPassword(String dsPassword);

    /**
     * sets the default schema used by applications. SRJRCFrames supports
     * database schema usage even on databases, that don't support schemata.
     * This support is seamless, so there is no difference for the
     * user/developer in using a database that supports schemata or a database
     * whithout that support.
     * 
     * @param dsSchema
     *            - the default schema name
     */
    public void setDsSchema(String dsSchema);

    /**
     * sets the user used to access the data storage
     * 
     * @param dsUser
     *            - user for data storage
     */
    public void setDsUser(String dsUser);
}
