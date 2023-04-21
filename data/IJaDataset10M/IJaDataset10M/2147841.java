package org.jazzteam.jpatterns.core;

import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * This class contains the global constants which are usefull for the development of the JPatterns
 * 
 * $Author:: zmicer $<br/>
 * $Rev:: 67 $<br/>
 * * $Date:: 2007-08-28 21:37:07 #$<br/>
 * $Date:: 2007-08-28 21:37:07 #$<br/>
 */
public class JPConstants {

    /**
	 * Logger instance.
	 */
    public static final Logger LOG = Logger.getLogger(JPConstants.class);

    /**
	 * This *compiled* pattern represents the mask for the configuration files which should be used by the JPatterns to
	 * initialize the patterns stuff. <br/>
	 * This patterns excludes the names of the files are used by the framework for the initializing the framework
	 * itself.
	 */
    public static final Pattern PATTERN = Pattern.compile(".*jpatterns.*(?<!^jpatterns_framework)(?<!^jpatterns_framework_custom)\\.xml");

    /**
	 * This interface stores the constants related to the configuration of all the XML configuration files might be used
	 * at the JPatterns
	 */
    public interface XMLConfigFilesConstants {

        /**
		 * This is the file name where the JPatterns configuration would be stored (the idea is when JPattern would have
		 * the patterns implemented, then another functionality would be forced to use these patterns as base - e.g.
		 * initially implementations of JPatterns base interfaces are taken from the properties file, but when "factory"
		 * pattern would be implemented we would be able to use the xml for the storing implementations defined per
		 * interfaces etc.) <br/>
		 * The customer specific configuration file can not be the same name with this provided below. It is reserved
		 * one. When for the XML configuration files for the JPatterns this name would be simply excluded.
		 * (<strong>jpatterns_framework_custom</strong> and <strong>jpatterns_framework</strong>)
		 */
        String DEFAULT_XML_FRAMEWORK_CONFIG_FILE_NAME = "jpatterns_framework.xml";

        /**
		 * The JVM param is used for the obtaining the overriden configuration for the framework default configuration
		 * (is used for the wramework itself)
		 */
        String DEFAULT_XML_FRAMEWORK_CONFIG_FILE_NAME_JVM_PARAM = XMLConfigFilesConstants.DEFAULT_XML_FRAMEWORK_CONFIG_FILE_NAME;

        /**
		 * The key is used for the obtaining the configuration for the default framework xml file.
		 */
        String DEFAULT_XML_FRAMEWORK_CONFIG_FILE_NAME_PROPS_PARAM = "jpatterns.configuration.xml.default.framework.file";

        /**
		 * Please review the Java Docs to the
		 * {@link JPConstants.XMLConfigFilesConstants#DEFAULT_XML_FRAMEWORK_CONFIG_FILE_NAME}. <br/>
		 * The file with such name allows to override/extend the configuration is written and this default file.
		 */
        String CUSTOM_XML_FRAMEWORK_CONFIG_FILE_NAME = "jpatterns_framework_custom.xml";

        /**
		 * The JVM param is used for the obtaining the overriden configuration for the custom framework xml file.
		 */
        String CUSTOM_XML_FRAMEWORK_CONFIG_FILE_NAME_JVM_PARAM = XMLConfigFilesConstants.CUSTOM_XML_FRAMEWORK_CONFIG_FILE_NAME;

        /**
		 * The key is used for the obtaining the overriden configuration for the custom framework xml file.
		 */
        String CUSTOM_XML_FRAMEWORK_CONFIG_FILE_NAME_PROPS_PARAM = "jpatterns.configuration.xml.custom.framework.file";

        /**
		 * The JVM param is used for the obtaining the path to the directory where the overriden XML customer specific
		 * configuration should be seached
		 */
        String CUSTOM_XML_CONSUMER_CONFIG_DIR_NAME_JVM_PARAM = "jpatterns.configuration.xml.custom.consumer.config.dir";

        /**
		 * The JVM param is used for the obtaining the path to the directory where the default XML customer specific
		 * configuration should be seached
		 */
        String DEFAULT_XML_CONSUMER_CONFIG_DIR_NAME_JVM_PARAM = "jpatterns.configuration.xml.default.consumer.config.dir";

        /**
		 * The key for the properties settings defining the directory where to find all the jpatterns configuration.
		 */
        String CUSTOM_XML_CONSUMER_CONFIG_DIR_NAME_PROPS_PARAM = XMLConfigFilesConstants.CUSTOM_XML_CONSUMER_CONFIG_DIR_NAME_JVM_PARAM;

        /**
		 * The key for the properties settings defining the directory where to find default consumer jpatterns
		 * configuration.
		 */
        String DEFAULT_XML_CONSUMER_CONFIG_DIR_NAME_PROPS_PARAM = XMLConfigFilesConstants.DEFAULT_XML_CONSUMER_CONFIG_DIR_NAME_JVM_PARAM;
    }

    /**
	 * This interface stores the constants related to the configuration of all the properties based configuration files
	 */
    public interface PropertiesConfigFilesConstants {

        /**
		 * PROPERTIES_EXTENSION
		 */
        String PROPERTIES_EXTENSION = ".properties";

        /**
		 * DEFAULT_PROPERTIES_BASE_NAME
		 */
        String DEFAULT_PROPERTIES_BASE_NAME = "jpatterns";

        /**
		 * The name of the default jpatterns properties file - these properties are necessary to setup configuration
		 * only for the JPatterns core files - when factory pattern would be implemented the configuration would be
		 * taken from the xml file with the name <code>DEFAULT_XML_CONFIG_FILE_NAME</code>
		 */
        String DEFAULT_PROPERTIES_FILE_NAME = PropertiesConfigFilesConstants.DEFAULT_PROPERTIES_BASE_NAME + PropertiesConfigFilesConstants.PROPERTIES_EXTENSION;

        /**
		 * It is possible to override the name to the default properties file using the JVM parameter
		 */
        String DEFAULT_PROPERTIES_FILE_NAME_JVM_PARAM = PropertiesConfigFilesConstants.DEFAULT_PROPERTIES_FILE_NAME;

        /**
		 * CUSTOM_PROPERTIES_BASE_NAME
		 */
        String CUSTOM_PROPERTIES_BASE_NAME = "jpatterns_custom";

        /**
		 * The name of the file where the custom properties would be stored - it would allow the developers using
		 * JPatterns, to override the default settings would be stored at the Jar file.
		 */
        String CUSTOM_PROPERTIES_FILE_NAME = PropertiesConfigFilesConstants.CUSTOM_PROPERTIES_BASE_NAME + PropertiesConfigFilesConstants.PROPERTIES_EXTENSION;

        /**
		 * It is possible to override the name to the custom properties file using the JVM parameter
		 */
        String CUSTOM_PROPERTIES_FILE_NAME_JVM_PARAM = PropertiesConfigFilesConstants.CUSTOM_PROPERTIES_FILE_NAME;
    }

    /**
	 * Defines the logging categories to be used by the JPatterns core
	 */
    public enum LoggingCategory {

        /**
		 * All the debug messages related to the Factory pattern
		 */
        FACTORY, /**
		 * All the messages related to the obtaining patterns from the JPatterns consumer/framework configuration
		 */
        OBTAIN_PATTERNS
    }

    /**
	 * This enumeration defines the possible categories for the configuration JPatterns engine would use for the obtain
	 * the patterns implementations (for nowe it is just consumer but in the future when some different providers appear
	 * - it could be extended or made even dynamical) <br/>
	 * todo [zmicer]: review the above mentioned. It is logged as the 4 item of the release 1.1 requirements.
	 */
    public enum EngineConfigCategory {

        /**
		 * Framework configuration
		 */
        FRAMEWORK, /**
		 * Consumer configuration
		 */
        CONSUMER
    }

    /**
	 * This interface stores the names of the config elements which are the part of the FrameworkConfig
	 */
    public interface FrameworkConfigEntities {

        /**
		 * Defines the name of the factory woul dbe used for the factory core settings
		 */
        String FACTORY_JPATTERNS_CORE = "JPatternsCore";

        /**
		 * Defines the item with the configuration for the plugins
		 */
        String ITEM_JPATTERNS_PLUGIN = "JPatternsPlugins";
    }

    /**
	 * The default scope name constant.
	 */
    public static final String DEFAULT_SCOPE_NAME = "DEFAULT_SCOPE";

    /**
	 * default priority to be used with the not prioritized JPatternsConfig child elements
	 */
    public static final int DEFAULT_PRIORITY = -1;

    /**
	 * Prioritized priority prefix
	 */
    public static final String PRIORITIZED_PRIOTITY_PREFIX = "PRIORITIZED_PRIORITY_";
}
