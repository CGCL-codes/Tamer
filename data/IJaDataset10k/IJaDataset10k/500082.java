package uk.org.ogsadai.resource.dataresource.jdbc;

/**
 * Interface for classes that provide access to jdbc settings for a JDBC resource.
 * 
 * @author The OGSA-DAI Project Team
 *
 */
public interface JDBCSettingsProvider {

    /**
     * Gets the JDBCSettings instance for the resource.
     * 
     * @return a mapper.
     */
    JDBCSettings getJDBCSettings();
}
