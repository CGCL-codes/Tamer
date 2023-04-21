package org.jdu.dao;

/**
 * Definizione dei nomi delle properties usate dall'applicativo
 * @author epelli
 *
 */
public interface PropertiesNames {

    public static final String XML_DB_MAPPING = "xml.db.mapping";

    public static final String DB_CLASS = "db.class";

    public static final String DB_JDBC_DRIVER = "db.${dbname}.driver";

    public static final String DB_JDBC_USER = "db.${dbname}.user";

    public static final String DB_JDBC_PASSWORD = "db.${dbname}.pwd";

    public static final String DB_JDBC_URL = "db.${dbname}.url";

    public static final String DB_JNDI_NAME = "${dbname}.ds";
}
