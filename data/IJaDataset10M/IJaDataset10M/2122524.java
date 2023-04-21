package hu.sqooler.jdbc.databases;

import hu.sqooler.exception.SqoolerSQLException;
import java.sql.*;
import java.util.*;

public interface IDatabase {

    public static final int MYSQL = 0;

    public static final int ORACLE = 1;

    public static final int POSTGRESQL = 2;

    public static final int HSQL_DB = 3;

    public static final int SQL_SERVER = 4;

    public static final int SYBASE = 5;

    public static final int GENERIC_DB = 1000;

    /**
    * Retrieves the column names for table names, sorted.
    * @param dbmd Database metadata
    * @param catalog
    * @param tables Table names
    * @return the column names for the tables
    */
    public String[] getTableColumnNames(DatabaseMetaData dbmd, String catalog, String schema, String table) throws SQLException, SqoolerSQLException;

    /**
    * Retrieves the properties of columns of a table.
    * @param dbmd
    * @param catalog
    * @param schema
    * @param table
    * @return
    * @throws SQLException
    */
    public SortedMap getSortedTableColumnsProperties(DatabaseMetaData dbmd, String catalog, String schema, String table) throws SQLException, SqoolerSQLException;

    /**
    * Retrieves the properties of columns of a table.
    * @param dbmd
    * @param catalog
    * @param schema
    * @param table
    * @return
    * @throws SQLException
    */
    public ResultSet getTableColumnsProperties(DatabaseMetaData dbmd, String catalog, String schema, String table) throws SQLException, SqoolerSQLException;

    /**
    * Retrieves database schemas, sorted.
    * @param dbmd
    * @return schemas
    * @throws SQLException
    */
    public String[] getSchemas(DatabaseMetaData dbmd) throws SQLException;

    /**
    * Retrieves table names, sorted.
    * @param dbmd database metadata
    * @param catalog catalog name
    * @param schema schema name
    * @param type what type of table (TABLE, SYNONYM, etc.)
    * @return table names
    * @throws SQLException
    */
    public String[] getTables(DatabaseMetaData dbmd, String catalog, String schema, String[] type) throws SQLException;

    /**
    * Gets the column names for table names, sorted.
    * @param dbmd
    * @param catalog
    * @param schema
    * @param table Table name
    * @return the column names for the tables
    */
    public String[] getPrimaryKeys(DatabaseMetaData dbmd, String catalog, String schema, String table) throws SQLException, SqoolerSQLException;

    /**
    * 
    * @param dbmd
    * @param catalog
    * @param schemaPattern
    * @param procedureNamePattern
    * @return
    * @throws SQLException
    * @throws SqoolerSQLException
    */
    public SortedMap getProcedures(DatabaseMetaData dbmd, String catalog, String schemaPattern, String procedureCatPattern, String procedureNamePattern) throws SQLException, SqoolerSQLException;

    /**
    * 
    * @param dbmd
    * @param catalog
    * @param schema
    * @param procedureNamePattern
    * @return
    * @throws SQLException
    * @throws SqoolerSQLException
    */
    public String[] getPackages(DatabaseMetaData dbmd, String catalog, String schema, String procedureNamePattern) throws SQLException, SqoolerSQLException;

    /**
    * 
    * @param dbmd
    * @param catalog
    * @param schemaPattern
    * @param procedureNamePattern
    * @param columnNamePattern
    * @return
    * @throws SQLException
    * @throws SqoolerSQLException
    */
    public Map getProcedureColumns(DatabaseMetaData dbmd, String catalog, String schemaPattern, String procedureCat, String procedureNamePattern, String columnNamePattern) throws SQLException, SqoolerSQLException;

    /**
    * 
    * @param dbmd
    * @param primaryCatalog
    * @param primarySchema
    * @param primaryTable
    * @param foreignCatalog
    * @param foreignSchema
    * @param foreignTable
    * @return
    * @throws SQLException
    */
    public List getCrossReference(DatabaseMetaData dbmd, String primaryCatalog, String primarySchema, String primaryTable) throws SQLException;

    /**
    * 
    * @return
    */
    public int getDatabaseType();
}
