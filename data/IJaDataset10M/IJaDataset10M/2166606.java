package net.persister.configuration;

/**
 * Configuration 파일에서 사용되는 속성(properties)의 정적 정보.
 *
 * @author Park, chanwook
 *
 */
public class ConfigurationProperties {

    public enum EntityMappedType {

        annotation, xml
    }

    public static final String DRIVERCLASS = "driverClass";

    public static final String URL = "url";

    public static final String DATASOURCE = "datasource";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String MEDIATOR = "mediator";

    public static final String AUTOCOMMIT = "autocommit";

    public static final String ACCESSTYPE = "accessType";

    public static final String SCHEMA_GENERATE = "schemaGenerate";

    public static String[] getDBProperties() {
        return new String[] { DRIVERCLASS, URL, DATASOURCE, USERNAME, PASSWORD, MEDIATOR };
    }

    public static String[] getEntityProperties() {
        return new String[] { ACCESSTYPE };
    }

    public static String[] getTxProperties() {
        return new String[] { AUTOCOMMIT };
    }

    public static String[] getToolsProperties() {
        return new String[] { SCHEMA_GENERATE };
    }
}
