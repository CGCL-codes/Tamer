package net.jadoth.sqlengine.dbms.oracle11g;

import net.jadoth.meta.License;
import net.jadoth.sqlengine.dbms.DbmsAdaptor;
import net.jadoth.sqlengine.dbms.DbmsConnectionProvider;
import net.jadoth.sqlengine.dbms.SQLExceptionParser;
import net.jadoth.sqlengine.internal.DatabaseGateway;
import net.jadoth.sqlengine.internal.tables.SqlTableIdentifier;
import net.jadoth.sqlengine.license.SqlEngineLicense;
import net.jadoth.util.chars.VarChar;

/**
 * The Class Oracle11gDbms.
 */
@License(name = SqlEngineLicense.LICENSE_NAME, licenseClass = SqlEngineLicense.class, declaringClass = Oracle11gDbms.class)
public class Oracle11gDbms extends DbmsAdaptor.Implementation<Oracle11gDbms, Oracle11gDMLAssembler, Oracle11gDDLMapper, Oracle11gRetrospectionAccessor, Oracle11gSyntax> {

    /** The Constant MAX_VARCHAR_LENGTH. */
    protected static final int MAX_VARCHAR_LENGTH = 2000;

    protected static final char IDENTIFIER_DELIMITER = '"';

    /**
	 * Single connection.
	 *
	 * @param host the host
	 * @param port the port
	 * @param user the user
	 * @param password the password
	 * @param database the database
	 * @return the connection provider
	 */
    public static DbmsConnectionProvider<Oracle11gDbms> singleConnection(final String host, final int port, final String user, final String password, final String database) {
        return new DbmsConnectionProvider.Implementation<Oracle11gDbms>(new Oracle11gConnectionInformation(host, port, user, password, database, new Oracle11gDbms()));
    }

    /**
	 * Instantiates a new oracle11g dbms.
	 */
    public Oracle11gDbms() {
        this(new Oracle11gSQLExceptionParser());
    }

    /**
	 * Instantiates a new oracle11g dbms.
	 *
	 * @param sqlExceptionParser the sql exception parser
	 */
    public Oracle11gDbms(final SQLExceptionParser sqlExceptionParser) {
        super(sqlExceptionParser, false);
        this.setRetrospectionAccessor(new Oracle11gRetrospectionAccessor(this));
        this.setDMLAssembler(new Oracle11gDMLAssembler(this));
    }

    /**
	 * @param host
	 * @param port
	 * @param user
	 * @param password
	 * @param catalog
	 * @return
	 * @see net.jadoth.sqlengine.dbmsadaptor.DbmsAdaptor#createConnectionInformation(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
	 */
    @Override
    public Oracle11gConnectionInformation createConnectionInformation(final String host, final int port, final String user, final String password, final String catalog) {
        return new Oracle11gConnectionInformation(host, port, user, password, catalog, this);
    }

    /**
	 * Oracle does not support any means of calculating table columns selectivity as far as it is known.<br>
	 * So this method does nothing and returns {@code null}
	 *
	 * @param table the table
	 * @return the object
	 */
    @Override
    public Object updateSelectivity(final SqlTableIdentifier.Implementation table) {
        return null;
    }

    /**
	 * @param dbc
	 * @see net.jadoth.sqlengine.dbmsadaptor.DbmsAdaptor#initialize(net.jadoth.sqlengine.internal.DatabaseGateway)
	 */
    @Override
    public void initialize(final DatabaseGateway<Oracle11gDbms> dbc) {
    }

    /**
	 * @return
	 * @see net.jadoth.sqlengine.dbmsadaptor.DbmsAdaptor.Implementation#supportsMERGE()
	 */
    @Override
    public boolean supportsMERGE() {
        return false;
    }

    @Override
    public boolean supportsOFFSET_ROWS() {
        return false;
    }

    /**
	 * @param bytes
	 * @param sb
	 * @return
	 * @see net.jadoth.sqlengine.dbmsadaptor.DbmsAdaptor#assembleTransformBytes(byte[], java.lang.VarChar)
	 */
    @Override
    public VarChar assembleTransformBytes(final byte[] bytes, final VarChar sb) {
        throw new RuntimeException("Oracle assembleTransformBytes() not implemented yet");
    }

    /**
	 * @param fullQualifiedTableName
	 * @return
	 * @see net.jadoth.sqlengine.dbmsadaptor.DbmsAdaptor#rebuildAllIndices(java.lang.String)
	 */
    @Override
    public Object rebuildAllIndices(final String fullQualifiedTableName) {
        throw new RuntimeException("Oracle rebuildIndices() not implemented yet");
    }

    /**
	 * @return
	 * @see net.jadoth.sqlengine.dbmsadaptor.DbmsAdaptor#getMaxVARCHARlength()
	 */
    @Override
    public int getMaxVARCHARlength() {
        return MAX_VARCHAR_LENGTH;
    }

    @Override
    public char getIdentifierDelimiter() {
        return IDENTIFIER_DELIMITER;
    }

    @Override
    public boolean mustBeDelimited(final String identifier) {
        return false;
    }
}
