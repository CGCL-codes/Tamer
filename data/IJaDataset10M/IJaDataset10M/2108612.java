package com.frameworkset.orm.platform;

import com.frameworkset.orm.adapter.DBFactory;
import com.frameworkset.orm.engine.model.Domain;
import com.frameworkset.orm.engine.model.SchemaType;

/**
 * Oracle Platform implementation.
 *
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @version $Id: PlatformOracleImpl.java,v 1.8 2005/05/22 13:57:18 tfischer Exp $
 */
public class PlatformOracleImpl extends PlatformDefaultImpl {

    /**
     * Default constructor.
     */
    public PlatformOracleImpl() {
        super();
        initialize();
    }

    public String getDBTYPE() {
        return DBFactory.DBOracle;
    }

    /**
     * Initializes db specific domain mapping.
     */
    private void initialize() {
        setSchemaDomainMapping(new Domain(SchemaType.TINYINT, "NUMBER", "3", "0"));
        setSchemaDomainMapping(new Domain(SchemaType.SMALLINT, "NUMBER", "5", "0"));
        setSchemaDomainMapping(new Domain(SchemaType.INTEGER, "NUMBER", "10", "0"));
        setSchemaDomainMapping(new Domain(SchemaType.BOOLEANINT, "NUMBER", "1", "0"));
        setSchemaDomainMapping(new Domain(SchemaType.BIGINT, "NUMBER", "20", "0"));
        setSchemaDomainMapping(new Domain(SchemaType.REAL, "NUMBER"));
        setSchemaDomainMapping(new Domain(SchemaType.DOUBLE, "FLOAT"));
        setSchemaDomainMapping(new Domain(SchemaType.DECIMAL, "NUMBER"));
        setSchemaDomainMapping(new Domain(SchemaType.NUMERIC, "NUMBER"));
        setSchemaDomainMapping(new Domain(SchemaType.VARCHAR, "VARCHAR2"));
        setSchemaDomainMapping(new Domain(SchemaType.LONGVARCHAR, "VARCHAR2", "2000"));
        setSchemaDomainMapping(new Domain(SchemaType.TIME, "DATE"));
        setSchemaDomainMapping(new Domain(SchemaType.TIMESTAMP, "TIMESTAMP"));
        setSchemaDomainMapping(new Domain(SchemaType.BINARY, "LONG RAW"));
        setSchemaDomainMapping(new Domain(SchemaType.VARBINARY, "BLOB"));
        setSchemaDomainMapping(new Domain(SchemaType.LONGVARBINARY, "LONG RAW"));
    }

    /**
     * @see Platform#getMaxColumnNameLength()
     */
    public int getMaxColumnNameLength() {
        return 30;
    }

    /**
     * @see Platform#getNativeIdMethod()
     */
    public String getNativeIdMethod() {
        return Platform.SEQUENCE;
    }

    /**
     * @see Platform#getAutoIncrement()
     */
    public String getAutoIncrement() {
        return "";
    }
}
