package com.xmultra.util;

/**
 * PropertiesConfig is a singleton class that knows how to access Xmultra's Properties
 * XML files.
 *
 * @author      Shannon Brown
 * @version     $Revision: #1 $
 * @since       1.3.1
 */
abstract class PropertiesConfig {

    static final String VERSION = "@version $Revision: #1 $";

    static final String PROPERTIES_CFG_DTD_FILE = "defs/properties.dtd";

    static final String ROOT = "Properties";

    static final String PROPERTY = "Property";

    static final String DESCRIPTION = "Description";

    static final String NAME = "Name";

    static final String VALUE = "Value";

    static final String EMPTY_PROPERTIES_DOC_STRING = "<Properties>" + "</Properties>";
}
