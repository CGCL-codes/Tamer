package edu.uiuc.ncsa.security.core.configuration;

/**
 * A collection of tags for configuration files. These occur in the XML configuration file and are used
 * by providers to get the associated values.
 * <p>Created by Jeff Gaynor<br>
 * on 3/29/12 at  10:26 AM
 */
public interface ConfigurationTags {

    public static final String FS_PATH = "path";

    public static final String FS_INDEX = "indexPath";

    public static final String FS_DATA = "dataPath";

    public static final String SQL_TABLENAME = "tablename";

    public static final String SQL_PREFIX = "tablePrefix";

    public static final String SQL_SCHEMA = "schema";
}
