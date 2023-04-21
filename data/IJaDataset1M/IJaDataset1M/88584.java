package plugins;

import java.sql.ResultSet;

/**
 * stellt die Verbindungsfunktionen zur Datenbank her  Interface to database
 */
public interface IJDBCAdapter {

    /**
     * Führt einen SQL select - befehl aus  executes an select query
     *
     * @param Sql TODO Missing Constructuor Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public ResultSet executeQuery(String Sql);

    /**
     * Executes an SQL INSERT, UPDATE, CREATE or DELETE statement. In addition, SQL statements that
     * return nothing, such as SQL DDL statements, can be executed.  PluginTables should be named
     * this way : PluginName_TableName to avaoid problems with standard tables...
     *
     * @param Sql TODO Missing Constructuor Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public int executeUpdate(String Sql);

    /**
     * Return all tablenames from current database
     * @return String[]
     */
    public Object[] getAllTableNames();
}
