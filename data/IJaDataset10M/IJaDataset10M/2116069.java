package com.frameworkset.orm.platform;

import com.frameworkset.orm.engine.model.Domain;
import com.frameworkset.orm.engine.model.SchemaType;

/**
 * Postgresql Platform implementation.
 *
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @version $Id: PlatformPostgresqlImpl.java,v 1.6 2004/02/22 06:27:19 jmcnally Exp $
 */
public class PlatformPostgresqlImpl extends PlatformDefaultImpl {

    /**
     * Default constructor.
     */
    public PlatformPostgresqlImpl() {
        super();
        initialize();
    }

    /**
     * Initializes db specific domain mapping.
     */
    private void initialize() {
        setSchemaDomainMapping(new Domain(SchemaType.BIT, "BOOLEAN"));
        setSchemaDomainMapping(new Domain(SchemaType.TINYINT, "INT2"));
        setSchemaDomainMapping(new Domain(SchemaType.SMALLINT, "INT2"));
        setSchemaDomainMapping(new Domain(SchemaType.BIGINT, "INT8"));
        setSchemaDomainMapping(new Domain(SchemaType.REAL, "FLOAT"));
        setSchemaDomainMapping(new Domain(SchemaType.BOOLEANCHAR, "CHAR"));
        setSchemaDomainMapping(new Domain(SchemaType.BOOLEANINT, "INT2"));
        setSchemaDomainMapping(new Domain(SchemaType.DOUBLE, "DOUBLE PRECISION"));
        setSchemaDomainMapping(new Domain(SchemaType.LONGVARCHAR, "TEXT"));
        setSchemaDomainMapping(new Domain(SchemaType.BINARY, "BYTEA"));
        setSchemaDomainMapping(new Domain(SchemaType.VARBINARY, "BYTEA"));
        setSchemaDomainMapping(new Domain(SchemaType.LONGVARBINARY, "BYTEA"));
        setSchemaDomainMapping(new Domain(SchemaType.CLOB, "TEXT"));
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
