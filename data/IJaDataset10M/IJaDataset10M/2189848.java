package com.frameworkset.orm.platform;

import com.frameworkset.orm.engine.model.Domain;
import com.frameworkset.orm.engine.model.SchemaType;

/**
 * Interface for RDBMS platform specific behaviour.
 *
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @version $Id: Platform.java,v 1.8 2004/02/22 06:27:19 jmcnally Exp $
 */
public interface Platform {

    /** constant for native id method */
    static final String IDENTITY = "identity";

    /** constant for native id method */
    static final String SEQUENCE = "sequence";

    /**
     * Returns the native IdMethod (sequence|identity)
     *
     * @return the native IdMethod
     */
    String getNativeIdMethod();

    /**
     * Returns the max column length supported by the db.
     *
     * @return the max column length
     */
    int getMaxColumnNameLength();

    /**
     * Returns the db specific domain for a jdbcType.
     *
     * @param jdbcType the jdbcType name
     * @return the db specific domain
     */
    Domain getDomainForSchemaType(SchemaType jdbcType);

    /**
     * Returns the db specific domain for a jdbcType.
     *
     * @param jdbcType the jdbcType name
     * @return the db specific domain
     */
    Domain getDomainForSchemaType(int jdbcType);

    /**
     * @return The RDBMS-specific SQL fragment for <code>NULL</code>
     * or <code>NOT NULL</code>.
     */
    String getNullString(boolean notNull);

    /**
     * @return The RDBMS-specific SQL fragment for autoincrement.
     */
    String getAutoIncrement();

    /**
     * Returns if the RDBMS-specific SQL type has a size attribute.
     * 
     * @param sqlType the SQL type
     * @return true if the type has a size attribute
     */
    boolean hasSize(String sqlType);

    /**
     * Returns if the RDBMS-specific SQL type has a size attribute.
     * 
     * @param sqlType the SQL type
     * @return true if the type has a size attribute
     */
    boolean hasSize(int sqlType);

    /**
     * Returns if the RDBMS-specific SQL type has a scale attribute.
     * 
     * @param sqlType the SQL type
     * @return true if the type has a scale attribute
     */
    boolean hasScale(String sqlType);

    /**
     * Returns if the RDBMS-specific SQL type has a scale attribute.
     * 
     * @param sqlType the SQL type
     * @return true if the type has a scale attribute
     */
    boolean hasScale(int sqlType);

    String getDBTYPE();

    public SchemaType getSchemaTypeFromSqlType(int sqltype);
}
