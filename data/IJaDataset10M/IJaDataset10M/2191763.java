package br.usp.pcs.lahpc.ogsadai.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.activities.delivery.DeliverToRequestStatus;
import uk.org.ogsadai.client.toolkit.activities.transform.TupleToWebRowSetCharArrays;
import uk.org.ogsadai.client.toolkit.exception.ClientException;
import uk.org.ogsadai.client.toolkit.exception.ClientToolkitException;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;
import uk.org.ogsadai.client.toolkit.exception.RequestException;
import uk.org.ogsadai.client.toolkit.exception.ResourceUnknownException;
import uk.org.ogsadai.client.toolkit.exception.ServerCommsException;
import uk.org.ogsadai.client.toolkit.exception.ServerException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;
import uk.org.ogsadai.converters.webrowset.WebRowSetResultSetParseException;
import br.usp.pcs.lahpc.ogsadai.activities.metadata.client.DatabaseMetaDataMethod;
import br.usp.pcs.lahpc.ogsadai.activities.utils.MethodInformation;

/**
 * Holds informations about the capabilities of the underlying DBMS.
 * This implementation have as prerequisite the a custom OGSA-DAI Activity.
 * This Activity executes a method in a DatabaseMetaData object of a given
 * relational resource.<p>
 * 
 * Some discussion should be taken regarding this class, since we should
 * be enabling the driver to deal with more than one resource in a near
 * future.
 * 
 * @author Mathias Brito
 * @see java.sql.DatabaseMetaData
 */
public class DAIDatabaseMetaData implements java.sql.DatabaseMetaData {

    private static final int JDBC_MAJOR_VERSION = 2;

    private static final int JDBC_MINOR_VERSION = 1;

    /**
	 * Holds the connection that creates the instance of this class.
	 */
    DAIConnection connection;

    /**
	 * Constructor.
	 * @param conn Connection that instantiates this object.
	 */
    public DAIDatabaseMetaData(DAIConnection conn) {
        this.connection = conn;
    }

    /**
	 * Executes, in the server, the given method described in a MethodInformation object.
	 * 
	 * @param methodToCall Informations about the method to be invoked.
	 * @return Returns a Object containing the result of the call.
	 * @throws SQLException
	 */
    private Object executeMethodInServer(MethodInformation methodToCall) throws SQLException {
        Object result = null;
        DataRequestExecutionResource drer = connection.getResourceInfo().getDrer();
        DatabaseMetaDataMethod method = new DatabaseMetaDataMethod();
        method.addInput(methodToCall.getXML());
        method.setResourceID(connection.getResourceId());
        TupleToWebRowSetCharArrays tupleToWebRowSet = new TupleToWebRowSetCharArrays();
        tupleToWebRowSet.connectDataInput(method.getDataOutput());
        DeliverToRequestStatus delivery = new DeliverToRequestStatus();
        delivery.connectInput(tupleToWebRowSet.getResultOutput());
        PipelineWorkflow workflow = new PipelineWorkflow();
        workflow.add(method);
        workflow.add(tupleToWebRowSet);
        workflow.add(delivery);
        try {
            drer.execute(workflow, RequestExecutionType.SYNCHRONOUS);
            tupleToWebRowSet.getResultOutput().setDeliverToRequestStatusActivity(delivery);
            if (tupleToWebRowSet.hasNextResult()) {
                result = tupleToWebRowSet.nextResultAsResultSet();
            }
        } catch (ServerCommsException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientToolkitException e) {
            e.printStackTrace();
        } catch (DataStreamErrorException e) {
            e.printStackTrace();
        } catch (UnexpectedDataValueException e) {
            e.printStackTrace();
        } catch (DataSourceUsageException e) {
            e.printStackTrace();
        } catch (ResourceUnknownException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (RequestException e) {
            e.printStackTrace();
        } catch (WebRowSetResultSetParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
	 * Asks the object to execute the given method in the server and 
	 * returns the result as a boolean vaule.
	 * 
	 * @param method Informations about the method to invoke.
	 * @return returns the result of the invocation as a boolean.
	 * @throws SQLException
	 */
    private boolean getBooleanReturnValue(MethodInformation method) throws SQLException {
        ResultSet rs = (ResultSet) executeMethodInServer(method);
        if (!rs.next()) {
            return false;
        } else {
            return Boolean.valueOf((rs.getString(method.getMethodName()))).booleanValue();
        }
    }

    /**
	 * Asks the object to execute the given method in the server and 
	 * returns the result as a int vaule.
	 * 
	 * @param method Informations about the method to invoke.
	 * @return returns the result of the invocation as an int.
	 * @throws SQLException
	 */
    private int getIntReturnValue(MethodInformation method) throws SQLException {
        ResultSet rs = (ResultSet) executeMethodInServer(method);
        if (!rs.next()) {
            return 0;
        } else {
            return Integer.parseInt(rs.getString(method.getMethodName()));
        }
    }

    /**
	 * Asks the object to execute the given method in the server and 
	 * returns the result as a String vaule.
	 * 
	 * @param method Informations about the method to invoke.
	 * @return returns the result of the invocation as a String.
	 * @throws SQLException
	 */
    private String getStringReturnValue(MethodInformation method) throws SQLException {
        ResultSet rs = (ResultSet) executeMethodInServer(method);
        if (rs == null) {
            throw new SQLException("Error executing operation.");
        }
        if (!rs.next()) {
            return null;
        } else {
            return rs.getString(method.getMethodName());
        }
    }

    /**
	 * @see java.sql.DatabaseMetaData#allProceduresAreCallable()
	 */
    public boolean allProceduresAreCallable() throws SQLException {
        MethodInformation method = new MethodInformation("allProceduresAreCallable");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#allTablesAreSelectable()
	 */
    public boolean allTablesAreSelectable() throws SQLException {
        MethodInformation method = new MethodInformation("allTablesAreSelectable");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#dataDefinitionCausesTransactionCommit()
	 */
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        MethodInformation method = new MethodInformation("dataDefinitionCausesTransactionCommit");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#dataDefinitionIgnoredInTransactions()
	 */
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        MethodInformation method = new MethodInformation("dataDefinitionIgnoredInTransactions");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#deletesAreDetected(int)
	 */
    public boolean deletesAreDetected(int type) throws SQLException {
        MethodInformation method = new MethodInformation("deletesAreDetected");
        method.addParameter(new Integer(type), Integer.class);
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#doesMaxRowSizeIncludeBlobs()
	 */
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        MethodInformation method = new MethodInformation("doesMaxRowSizeIncludeBlobs");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getAttributes(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
    public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
        MethodInformation method = new MethodInformation("getAttributes");
        method.addParameter(catalog, String.class);
        method.addParameter(schemaPattern, String.class);
        method.addParameter(typeNamePattern, String.class);
        method.addParameter(attributeNamePattern, String.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getBestRowIdentifier(java.lang.String, java.lang.String, java.lang.String, int, boolean)
	 */
    public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
        MethodInformation method = new MethodInformation("getBestRowIdentifier");
        method.addParameter(catalog, String.class);
        method.addParameter(schema, String.class);
        method.addParameter(table, String.class);
        method.addParameter(Integer.toString(scope), int.class);
        method.addParameter(Boolean.toString(nullable), boolean.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getCatalogSeparator()
	 */
    public String getCatalogSeparator() throws SQLException {
        MethodInformation method = new MethodInformation("getCatalogSeparator");
        return getStringReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getCatalogTerm()
	 */
    public String getCatalogTerm() throws SQLException {
        MethodInformation method = new MethodInformation("getCatalogTerm");
        return getStringReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getCatalogs()
	 */
    public ResultSet getCatalogs() throws SQLException {
        MethodInformation method = new MethodInformation("getCatalogs");
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getColumnPrivileges(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
        MethodInformation method = new MethodInformation("getColumnPrivileges");
        method.addParameter(catalog, String.class);
        method.addParameter(schema, String.class);
        method.addParameter(table, String.class);
        method.addParameter(columnNamePattern, String.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        MethodInformation method = new MethodInformation("getColumns");
        method.addParameter(catalog, String.class);
        method.addParameter(schemaPattern, String.class);
        method.addParameter(tableNamePattern, String.class);
        method.addParameter(columnNamePattern, String.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getConnection()
	 */
    public Connection getConnection() throws SQLException {
        return connection;
    }

    /**
	 * @see java.sql.DatabaseMetaData#getCrossReference(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
    public ResultSet getCrossReference(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
        MethodInformation method = new MethodInformation("getCrossReference");
        method.addParameter(primaryCatalog, String.class);
        method.addParameter(primarySchema, String.class);
        method.addParameter(primaryTable, String.class);
        method.addParameter(foreignCatalog, String.class);
        method.addParameter(foreignSchema, String.class);
        method.addParameter(foreignTable, String.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getDatabaseMajorVersion()
	 */
    public int getDatabaseMajorVersion() throws SQLException {
        MethodInformation method = new MethodInformation("getDatabaseMajorVersion");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getDatabaseMinorVersion()
	 */
    public int getDatabaseMinorVersion() throws SQLException {
        MethodInformation method = new MethodInformation("getDatabaseMinorVersion");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getDatabaseProductName()
	 */
    public String getDatabaseProductName() throws SQLException {
        MethodInformation method = new MethodInformation("getDatabaseProductName");
        return getStringReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getDatabaseProductVersion()
	 */
    public String getDatabaseProductVersion() throws SQLException {
        MethodInformation method = new MethodInformation("getDatabaseProductVersion");
        return getStringReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getDefaultTransactionIsolation()
	 */
    public int getDefaultTransactionIsolation() throws SQLException {
        MethodInformation method = new MethodInformation("getDefaultTransactionIsolation");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getDriverMajorVersion()
	 */
    public int getDriverMajorVersion() {
        return Driver.DRIVER_MAJOR_VERSION;
    }

    /**
	 * @see java.sql.DatabaseMetaData#getDriverMinorVersion()
	 */
    public int getDriverMinorVersion() {
        return Driver.DRIVER_MINOR_VERSION;
    }

    /**
	 * @see java.sql.DatabaseMetaData#getDriverName()
	 */
    public String getDriverName() throws SQLException {
        return Driver.DRIVER_NAME;
    }

    /**
	 * @see java.sql.DatabaseMetaData#getDriverVersion()
	 */
    public String getDriverVersion() throws SQLException {
        return Driver.DRIVER_VERSION;
    }

    /**
	 * @see java.sql.DatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String, java.lang.String)
	 */
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        MethodInformation method = new MethodInformation("getExportedKeys");
        method.addParameter(catalog, String.class);
        method.addParameter(schema, String.class);
        method.addParameter(table, String.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getExtraNameCharacters()
	 */
    public String getExtraNameCharacters() throws SQLException {
        MethodInformation method = new MethodInformation("getExtraNameCharacters");
        return getStringReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getIdentifierQuoteString()
	 */
    public String getIdentifierQuoteString() throws SQLException {
        MethodInformation method = new MethodInformation("getIdentifierQuoteString");
        return getStringReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getImportedKeys(java.lang.String, java.lang.String, java.lang.String)
	 */
    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        MethodInformation method = new MethodInformation("getImportedKeys");
        method.addParameter(catalog, String.class);
        method.addParameter(schema, String.class);
        method.addParameter(table, String.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getIndexInfo(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)
	 */
    public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
        MethodInformation method = new MethodInformation("getIndexInfo");
        method.addParameter(catalog, String.class);
        method.addParameter(schema, String.class);
        method.addParameter(table, String.class);
        method.addParameter(Boolean.toString(unique), boolean.class);
        method.addParameter(Boolean.toString(approximate), boolean.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getJDBCMajorVersion()
	 */
    public int getJDBCMajorVersion() throws SQLException {
        return JDBC_MAJOR_VERSION;
    }

    /**
	 * @see java.sql.DatabaseMetaData#getJDBCMinorVersion()
	 */
    public int getJDBCMinorVersion() throws SQLException {
        return JDBC_MINOR_VERSION;
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxBinaryLiteralLength()
	 */
    public int getMaxBinaryLiteralLength() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxBinaryLiteralLength");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxCatalogNameLength()
	 */
    public int getMaxCatalogNameLength() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxCatalogNameLength");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxCharLiteralLength()
	 */
    public int getMaxCharLiteralLength() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxCharLiteralLength");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxColumnNameLength()
	 */
    public int getMaxColumnNameLength() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxColumnNameLength");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInGroupBy()
	 */
    public int getMaxColumnsInGroupBy() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxColumnsInGroupBy");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInIndex()
	 */
    public int getMaxColumnsInIndex() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxColumnsInIndex");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInOrderBy()
	 */
    public int getMaxColumnsInOrderBy() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxColumnsInOrderBy");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInSelect()
	 */
    public int getMaxColumnsInSelect() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxColumnsInSelect");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxColumnsInTable()
	 */
    public int getMaxColumnsInTable() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxColumnsInTable");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxConnections()
	 */
    public int getMaxConnections() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxConnections");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxCursorNameLength()
	 */
    public int getMaxCursorNameLength() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxCursorNameLength");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxIndexLength()
	 */
    public int getMaxIndexLength() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxIndexLength");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxProcedureNameLength()
	 */
    public int getMaxProcedureNameLength() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxProcedureNameLength");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxRowSize()
	 */
    public int getMaxRowSize() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxRowSize");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxSchemaNameLength()
	 */
    public int getMaxSchemaNameLength() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxSchemaNameLength");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxStatementLength()
	 */
    public int getMaxStatementLength() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxStatementLength");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxStatements()
	 */
    public int getMaxStatements() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxStatements");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxTableNameLength()
	 */
    public int getMaxTableNameLength() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxTableNameLength");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxTablesInSelect()
	 */
    public int getMaxTablesInSelect() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxTablesInSelect");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getMaxUserNameLength()
	 */
    public int getMaxUserNameLength() throws SQLException {
        MethodInformation method = new MethodInformation("getMaxUserNameLength");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getNumericFunctions()
	 */
    public String getNumericFunctions() throws SQLException {
        MethodInformation method = new MethodInformation("getNumericFunctions");
        return getStringReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getPrimaryKeys(java.lang.String, java.lang.String, java.lang.String)
	 */
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        MethodInformation method = new MethodInformation("getPrimaryKeys");
        method.addParameter(catalog, String.class);
        method.addParameter(schema, String.class);
        method.addParameter(table, String.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        MethodInformation method = new MethodInformation("getProcedureColumns");
        method.addParameter(catalog, String.class);
        method.addParameter(schemaPattern, String.class);
        method.addParameter(procedureNamePattern, String.class);
        method.addParameter(columnNamePattern, String.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getProcedureTerm()
	 */
    public String getProcedureTerm() throws SQLException {
        MethodInformation method = new MethodInformation("getProcedureTerm");
        return getStringReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getProcedures(java.lang.String, java.lang.String, java.lang.String)
	 */
    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
        MethodInformation method = new MethodInformation("getProcedureColumns");
        method.addParameter(catalog, String.class);
        method.addParameter(schemaPattern, String.class);
        method.addParameter(procedureNamePattern, String.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getResultSetHoldability()
	 */
    public int getResultSetHoldability() throws SQLException {
        MethodInformation method = new MethodInformation("getResultSetHoldability");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getSQLKeywords()
	 */
    public String getSQLKeywords() throws SQLException {
        MethodInformation method = new MethodInformation("getSQLKeywords");
        return getStringReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getSQLStateType()
	 */
    public int getSQLStateType() throws SQLException {
        MethodInformation method = new MethodInformation("getSQLStateType");
        return getIntReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getSchemaTerm()
	 */
    public String getSchemaTerm() throws SQLException {
        MethodInformation method = new MethodInformation("getSchemaTerm");
        return getStringReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getSchemas()
	 */
    public ResultSet getSchemas() throws SQLException {
        MethodInformation method = new MethodInformation("getSchemas");
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getSearchStringEscape()
	 */
    public String getSearchStringEscape() throws SQLException {
        MethodInformation method = new MethodInformation("getSearchStringEscape");
        return getStringReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getStringFunctions()
	 */
    public String getStringFunctions() throws SQLException {
        MethodInformation method = new MethodInformation("getStringFunctions");
        return getStringReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getSuperTables(java.lang.String, java.lang.String, java.lang.String)
	 */
    public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        MethodInformation method = new MethodInformation("getSuperTables");
        method.addParameter(catalog, String.class);
        method.addParameter(schemaPattern, String.class);
        method.addParameter(tableNamePattern, String.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getSuperTypes(java.lang.String, java.lang.String, java.lang.String)
	 */
    public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
        MethodInformation method = new MethodInformation("getSuperTypes");
        method.addParameter(catalog, String.class);
        method.addParameter(schemaPattern, String.class);
        method.addParameter(typeNamePattern, String.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getSystemFunctions()
	 */
    public String getSystemFunctions() throws SQLException {
        MethodInformation method = new MethodInformation("getSystemFunctions");
        return getStringReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getTablePrivileges(java.lang.String, java.lang.String, java.lang.String)
	 */
    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        MethodInformation method = new MethodInformation("getTablePrivileges");
        method.addParameter(catalog, String.class);
        method.addParameter(schemaPattern, String.class);
        method.addParameter(tableNamePattern, String.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getTableTypes()
	 */
    public ResultSet getTableTypes() throws SQLException {
        MethodInformation method = new MethodInformation("getTableTypes");
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
	 */
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        MethodInformation method = new MethodInformation("getTables");
        method.addParameter(catalog, String.class);
        method.addParameter(schemaPattern, String.class);
        method.addParameter(tableNamePattern, String.class);
        method.addParameter(types, String[].class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getTimeDateFunctions()
	 */
    public String getTimeDateFunctions() throws SQLException {
        MethodInformation method = new MethodInformation("getTimeDateFunctions");
        return getStringReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getTypeInfo()
	 */
    public ResultSet getTypeInfo() throws SQLException {
        MethodInformation method = new MethodInformation("getTypeInfo");
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getUDTs(java.lang.String, java.lang.String, java.lang.String, int[])
	 */
    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
        MethodInformation method = new MethodInformation("getUDTs");
        method.addParameter(catalog, String.class);
        method.addParameter(schemaPattern, String.class);
        method.addParameter(typeNamePattern, String.class);
        method.addParameter(types, int[].class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#getURL()
	 */
    public String getURL() throws SQLException {
        return connection.getServerBaseUrl();
    }

    /**
	 * @see java.sql.DatabaseMetaData#getUserName()
	 */
    public String getUserName() throws SQLException {
        return connection.getResourceInfo().getCatalogName();
    }

    /**
	 * @see java.sql.DatabaseMetaData#getVersionColumns(java.lang.String, java.lang.String, java.lang.String)
	 */
    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
        MethodInformation method = new MethodInformation("getTablePrivileges");
        method.addParameter(catalog, String.class);
        method.addParameter(schema, String.class);
        method.addParameter(table, String.class);
        return (ResultSet) executeMethodInServer(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#insertsAreDetected(int)
	 */
    public boolean insertsAreDetected(int type) throws SQLException {
        MethodInformation method = new MethodInformation("insertsAreDetected");
        method.addParameter(new Integer(type), Integer.class);
        ResultSet rs = (ResultSet) executeMethodInServer(method);
        if (!rs.next()) {
            return false;
        }
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#isCatalogAtStart()
	 */
    public boolean isCatalogAtStart() throws SQLException {
        MethodInformation method = new MethodInformation("isCatalogAtStart");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#isReadOnly()
	 */
    public boolean isReadOnly() throws SQLException {
        MethodInformation method = new MethodInformation("isReadOnly");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#locatorsUpdateCopy()
	 */
    public boolean locatorsUpdateCopy() throws SQLException {
        MethodInformation method = new MethodInformation("locatorsUpdateCopy");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#nullPlusNonNullIsNull()
	 */
    public boolean nullPlusNonNullIsNull() throws SQLException {
        MethodInformation method = new MethodInformation("nullPlusNonNullIsNull");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#nullsAreSortedAtEnd()
	 */
    public boolean nullsAreSortedAtEnd() throws SQLException {
        MethodInformation method = new MethodInformation("nullsAreSortedAtEnd");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#nullsAreSortedAtStart()
	 */
    public boolean nullsAreSortedAtStart() throws SQLException {
        MethodInformation method = new MethodInformation("nullsAreSortedAtStart");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#nullsAreSortedHigh()
	 */
    public boolean nullsAreSortedHigh() throws SQLException {
        MethodInformation method = new MethodInformation("nullsAreSortedHigh");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#nullsAreSortedLow()
	 */
    public boolean nullsAreSortedLow() throws SQLException {
        MethodInformation method = new MethodInformation("nullsAreSortedLow");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#othersDeletesAreVisible(int)
	 */
    public boolean othersDeletesAreVisible(int type) throws SQLException {
        MethodInformation method = new MethodInformation("othersDeletesAreVisible");
        method.addParameter(new Integer(type), Integer.class);
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#othersInsertsAreVisible(int)
	 */
    public boolean othersInsertsAreVisible(int type) throws SQLException {
        MethodInformation method = new MethodInformation("othersInsertsAreVisible");
        method.addParameter(new Integer(type), Integer.class);
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#othersUpdatesAreVisible(int)
	 */
    public boolean othersUpdatesAreVisible(int type) throws SQLException {
        MethodInformation method = new MethodInformation("othersUpdatesAreVisible");
        method.addParameter(new Integer(type), Integer.class);
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#ownDeletesAreVisible(int)
	 */
    public boolean ownDeletesAreVisible(int type) throws SQLException {
        MethodInformation method = new MethodInformation("ownDeletesAreVisible");
        method.addParameter(new Integer(type), Integer.class);
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#ownInsertsAreVisible(int)
	 */
    public boolean ownInsertsAreVisible(int type) throws SQLException {
        MethodInformation method = new MethodInformation("ownInsertsAreVisible");
        method.addParameter(new Integer(type), Integer.class);
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#ownUpdatesAreVisible(int)
	 */
    public boolean ownUpdatesAreVisible(int type) throws SQLException {
        MethodInformation method = new MethodInformation("ownUpdatesAreVisible");
        method.addParameter(new Integer(type), Integer.class);
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#storesLowerCaseIdentifiers()
	 */
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        MethodInformation method = new MethodInformation("storesLowerCaseIdentifiers");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#storesLowerCaseQuotedIdentifiers()
	 */
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        MethodInformation method = new MethodInformation("storesLowerCaseQuotedIdentifiers");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#storesMixedCaseIdentifiers()
	 */
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        MethodInformation method = new MethodInformation("storesMixedCaseIdentifiers");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#storesMixedCaseQuotedIdentifiers()
	 */
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        MethodInformation method = new MethodInformation("storesMixedCaseQuotedIdentifiers");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#storesUpperCaseIdentifiers()
	 */
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        MethodInformation method = new MethodInformation("storesUpperCaseIdentifiers");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#storesUpperCaseQuotedIdentifiers()
	 */
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        MethodInformation method = new MethodInformation("storesUpperCaseQuotedIdentifiers");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsANSI92EntryLevelSQL()
	 */
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        MethodInformation method = new MethodInformation("supportsANSI92EntryLevelSQL");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsANSI92FullSQL()
	 */
    public boolean supportsANSI92FullSQL() throws SQLException {
        MethodInformation method = new MethodInformation("supportsANSI92FullSQL");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsANSI92IntermediateSQL()
	 */
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        MethodInformation method = new MethodInformation("supportsANSI92IntermediateSQL");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsAlterTableWithAddColumn()
	 */
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        MethodInformation method = new MethodInformation("supportsAlterTableWithAddColumn");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsAlterTableWithDropColumn()
	 */
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        MethodInformation method = new MethodInformation("supportsAlterTableWithDropColumn");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsBatchUpdates()
	 */
    public boolean supportsBatchUpdates() throws SQLException {
        MethodInformation method = new MethodInformation("supportsBatchUpdates");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInDataManipulation()
	 */
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        MethodInformation method = new MethodInformation("supportsCatalogsInDataManipulation");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInIndexDefinitions()
	 */
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        MethodInformation method = new MethodInformation("supportsCatalogsInIndexDefinitions");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInPrivilegeDefinitions()
	 */
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        MethodInformation method = new MethodInformation("supportsCatalogsInPrivilegeDefinitions");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInProcedureCalls()
	 */
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        MethodInformation method = new MethodInformation("supportsCatalogsInProcedureCalls");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsCatalogsInTableDefinitions()
	 */
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        MethodInformation method = new MethodInformation("supportsCatalogsInTableDefinitions");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsColumnAliasing()
	 */
    public boolean supportsColumnAliasing() throws SQLException {
        MethodInformation method = new MethodInformation("supportsColumnAliasing");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsConvert()
	 */
    public boolean supportsConvert() throws SQLException {
        MethodInformation method = new MethodInformation("supportsConvert");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsConvert(int, int)
	 */
    public boolean supportsConvert(int fromType, int toType) throws SQLException {
        MethodInformation method = new MethodInformation("othersUpdatesAreVisible");
        method.addParameter(new Integer(fromType), Integer.class);
        method.addParameter(new Integer(toType), Integer.class);
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsCoreSQLGrammar()
	 */
    public boolean supportsCoreSQLGrammar() throws SQLException {
        MethodInformation method = new MethodInformation("supportsCoreSQLGrammar");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsCorrelatedSubqueries()
	 */
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        MethodInformation method = new MethodInformation("supportsCorrelatedSubqueries");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsDataDefinitionAndDataManipulationTransactions()
	 */
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        MethodInformation method = new MethodInformation("supportsDataDefinitionAndDataManipulationTransactions");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsDataManipulationTransactionsOnly()
	 */
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        MethodInformation method = new MethodInformation("supportsDataManipulationTransactionsOnly");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsDifferentTableCorrelationNames()
	 */
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        MethodInformation method = new MethodInformation("supportsDifferentTableCorrelationNames");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsExpressionsInOrderBy()
	 */
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        MethodInformation method = new MethodInformation("supportsExpressionsInOrderBy");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsExtendedSQLGrammar()
	 */
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        MethodInformation method = new MethodInformation("supportsExtendedSQLGrammar");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsFullOuterJoins()
	 */
    public boolean supportsFullOuterJoins() throws SQLException {
        MethodInformation method = new MethodInformation("supportsFullOuterJoins");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsGetGeneratedKeys()
	 */
    public boolean supportsGetGeneratedKeys() throws SQLException {
        MethodInformation method = new MethodInformation("supportsGetGeneratedKeys");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsGroupBy()
	 */
    public boolean supportsGroupBy() throws SQLException {
        MethodInformation method = new MethodInformation("supportsGroupBy");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsGroupByBeyondSelect()
	 */
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        MethodInformation method = new MethodInformation("supportsGroupByBeyondSelect");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsGroupByUnrelated()
	 */
    public boolean supportsGroupByUnrelated() throws SQLException {
        MethodInformation method = new MethodInformation("supportsGroupByUnrelated");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsIntegrityEnhancementFacility()
	 */
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        MethodInformation method = new MethodInformation("supportsIntegrityEnhancementFacility");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsLikeEscapeClause()
	 */
    public boolean supportsLikeEscapeClause() throws SQLException {
        MethodInformation method = new MethodInformation("supportsLikeEscapeClause");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsLimitedOuterJoins()
	 */
    public boolean supportsLimitedOuterJoins() throws SQLException {
        MethodInformation method = new MethodInformation("supportsLimitedOuterJoins");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsMinimumSQLGrammar()
	 */
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        MethodInformation method = new MethodInformation("supportsMinimumSQLGrammar");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsMixedCaseIdentifiers()
	 */
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        MethodInformation method = new MethodInformation("supportsMixedCaseIdentifiers");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsMixedCaseQuotedIdentifiers()
	 */
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        MethodInformation method = new MethodInformation("supportsMixedCaseQuotedIdentifiers");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsMultipleOpenResults()
	 */
    public boolean supportsMultipleOpenResults() throws SQLException {
        MethodInformation method = new MethodInformation("supportsMultipleOpenResults");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsMultipleResultSets()
	 */
    public boolean supportsMultipleResultSets() throws SQLException {
        MethodInformation method = new MethodInformation("supportsMultipleResultSets");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsMultipleTransactions()
	 */
    public boolean supportsMultipleTransactions() throws SQLException {
        MethodInformation method = new MethodInformation("supportsMultipleTransactions");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsNamedParameters()
	 */
    public boolean supportsNamedParameters() throws SQLException {
        MethodInformation method = new MethodInformation("supportsNamedParameters");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsNonNullableColumns()
	 */
    public boolean supportsNonNullableColumns() throws SQLException {
        MethodInformation method = new MethodInformation("supportsNonNullableColumns");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsOpenCursorsAcrossCommit()
	 */
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        MethodInformation method = new MethodInformation("supportsOpenCursorsAcrossCommit");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsOpenCursorsAcrossRollback()
	 */
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        MethodInformation method = new MethodInformation("supportsOpenCursorsAcrossRollback");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsOpenStatementsAcrossCommit()
	 */
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        MethodInformation method = new MethodInformation("supportsOpenStatementsAcrossCommit");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsOpenStatementsAcrossRollback()
	 */
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        MethodInformation method = new MethodInformation("supportsOpenStatementsAcrossRollback");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsOrderByUnrelated()
	 */
    public boolean supportsOrderByUnrelated() throws SQLException {
        MethodInformation method = new MethodInformation("supportsOrderByUnrelated");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsOuterJoins()
	 */
    public boolean supportsOuterJoins() throws SQLException {
        MethodInformation method = new MethodInformation("supportsOuterJoins");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsPositionedDelete()
	 */
    public boolean supportsPositionedDelete() throws SQLException {
        MethodInformation method = new MethodInformation("supportsPositionedDelete");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsPositionedUpdate()
	 */
    public boolean supportsPositionedUpdate() throws SQLException {
        MethodInformation method = new MethodInformation("supportsPositionedUpdate");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsResultSetConcurrency(int, int)
	 */
    public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
        MethodInformation method = new MethodInformation("supportsResultSetConcurrency");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsResultSetHoldability(int)
	 */
    public boolean supportsResultSetHoldability(int holdability) throws SQLException {
        MethodInformation method = new MethodInformation("supportsResultSetHoldability");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsResultSetType(int)
	 */
    public boolean supportsResultSetType(int type) throws SQLException {
        MethodInformation method = new MethodInformation("supportsResultSetType");
        method.addParameter(new Integer(type), Integer.class);
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsSavepoints()
	 */
    public boolean supportsSavepoints() throws SQLException {
        MethodInformation method = new MethodInformation("supportsSavepoints");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsSchemasInDataManipulation()
	 */
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        MethodInformation method = new MethodInformation("supportsSchemasInDataManipulation");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsSchemasInIndexDefinitions()
	 */
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        MethodInformation method = new MethodInformation("supportsSchemasInIndexDefinitions");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsSchemasInPrivilegeDefinitions()
	 */
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        MethodInformation method = new MethodInformation("supportsSchemasInPrivilegeDefinitions");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsSchemasInProcedureCalls()
	 */
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        MethodInformation method = new MethodInformation("supportsSchemasInProcedureCalls");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsSchemasInTableDefinitions()
	 */
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        MethodInformation method = new MethodInformation("supportsSchemasInTableDefinitions");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsSelectForUpdate()
	 */
    public boolean supportsSelectForUpdate() throws SQLException {
        MethodInformation method = new MethodInformation("supportsSelectForUpdate");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsStatementPooling()
	 */
    public boolean supportsStatementPooling() throws SQLException {
        MethodInformation method = new MethodInformation("supportsStatementPooling");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsStoredProcedures()
	 */
    public boolean supportsStoredProcedures() throws SQLException {
        MethodInformation method = new MethodInformation("supportsStoredProcedures");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsSubqueriesInComparisons()
	 */
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        MethodInformation method = new MethodInformation("supportsSubqueriesInComparisons");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsSubqueriesInExists()
	 */
    public boolean supportsSubqueriesInExists() throws SQLException {
        MethodInformation method = new MethodInformation("supportsSubqueriesInExists");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsSubqueriesInIns()
	 */
    public boolean supportsSubqueriesInIns() throws SQLException {
        MethodInformation method = new MethodInformation("supportsSubqueriesInIns");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsSubqueriesInQuantifieds()
	 */
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        MethodInformation method = new MethodInformation("supportsSubqueriesInQuantifieds");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsTableCorrelationNames()
	 */
    public boolean supportsTableCorrelationNames() throws SQLException {
        MethodInformation method = new MethodInformation("supportsTableCorrelationNames");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsTransactionIsolationLevel(int)
	 */
    public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
        MethodInformation method = new MethodInformation("supportsTransactionIsolationLevel");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsTransactions()
	 */
    public boolean supportsTransactions() throws SQLException {
        MethodInformation method = new MethodInformation("supportsTransactions");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsUnion()
	 */
    public boolean supportsUnion() throws SQLException {
        MethodInformation method = new MethodInformation("supportsUnion");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#supportsUnionAll()
	 */
    public boolean supportsUnionAll() throws SQLException {
        MethodInformation method = new MethodInformation("supportsUnionAll");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#updatesAreDetected(int)
	 */
    public boolean updatesAreDetected(int type) throws SQLException {
        MethodInformation method = new MethodInformation("updatesAreDetected");
        method.addParameter(new Integer(type), Integer.class);
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#usesLocalFilePerTable()
	 */
    public boolean usesLocalFilePerTable() throws SQLException {
        MethodInformation method = new MethodInformation("usesLocalFilePerTable");
        return getBooleanReturnValue(method);
    }

    /**
	 * @see java.sql.DatabaseMetaData#usesLocalFiles()
	 */
    public boolean usesLocalFiles() throws SQLException {
        MethodInformation method = new MethodInformation("usesLocalFiles");
        return getBooleanReturnValue(method);
    }
}
