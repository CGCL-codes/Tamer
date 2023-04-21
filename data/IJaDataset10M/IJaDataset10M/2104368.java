package org.digitalcure.lunarcp.bundlesinfocreator.config;

/**
 * Interface that contains the names of the XML elements and attributes of the
 * configuration file format.
 * @author Stefan Diener
 * @version 1.0
 * @since LunaRCP 0.5, 06.06.2011
 * @lastChange $Date$ by $Author$
 */
public interface IXmlNames {

    /** The outer element (root element). */
    String ELEM_BUNDLESINFOCREATOR_CONFIG = "bundlesinfocreator_config";

    /** Element for the path of the Eclipse installation. */
    String ELEM_PATH_OF_ECLIPSE_INSTALL = "path_of_eclipse_install";

    /** Element for a path to be examined. */
    String ELEM_PATH_TO_BE_EXAMINED = "path_to_be_examined";

    /**  Element for the environment settings. */
    String ELEM_ENVIRONMENT = "environment";

    /** Element for the destination file name. */
    String ELEM_DESTINATION_FILE = "destination_file";

    /** Element for the default start level. */
    String ELEM_DEFAULT_START_LEVEL = "default_start_level";

    /** Element for a single start level. */
    String ELEM_START_LEVEL = "start_level";

    /** Element for an auto-start plug-in. */
    String ELEM_AUTO_START_PLUGIN = "auto_start_plugin";

    /** Attribute for a name. */
    String ATTR_NAME = "name";

    /** Attribute for an ID. */
    String ATTR_ID = "id";

    /** Attribute for a numeric value. */
    String ATTR_VALUE = "value";
}
