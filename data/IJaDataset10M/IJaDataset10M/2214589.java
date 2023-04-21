package org.xaware.common;

/**
 * Interface for the advisor factory classes.
 * 
 * @author venkatarama.satish
 * 
 */
public interface IAdvisorFactory {

    /** String constant for advisor.properties file name. */
    static final String ADVISOR_PROPERTIES_FILE = "advisor.properties";

    /** String constant for Connector type prefix */
    static final String CONNECTOR_TYPE_PREFIX = "Connector_";

    static final String PLUGIN_PREFIX = "Plugin_";

    /** String constant for bundle property name */
    static final String ADVISOR_BUNDLE_PROPERTY_NAME = "advisorBundle";

    /** String constant for advisor class property name */
    static final String ADVISOR_CLASS_PROPERTY_NAME = "advisorClass";

    /** String constant for the component name of plug-in */
    public static final String COMPONENT_DESIGNER = PLUGIN_PREFIX + "Designer";

    /** String constant for the component name of plug-in */
    public static final String COMPONENT_HELP = PLUGIN_PREFIX + "Help";

    /** String constant for the component name of plug-in */
    public static final String COMPONENT_SERVER = PLUGIN_PREFIX + "Server";

    /** String constant for the component name of plug-in */
    public static final String COMPONENT_SCHEMA_NAVIGATOR = PLUGIN_PREFIX + "Schema_Navigator";

    /** String constant for the component name of plug-in */
    public static final String COMPONENT_DATA_TOOLS = PLUGIN_PREFIX + "Data_Tools";

    /** String constant for the component name of plug-in */
    public static final String COMPONENT_SALES_FORCE = PLUGIN_PREFIX + "Sales_Force";

    /** String constant for the component name suffix - BizComponent Wizard */
    public static final String COMPONENT_SUFFIX_BIZCOMPONENT_WIZARD = " BizComponent Wizard";

    /** String constant for the component name suffix - BizComponent Editor */
    public static final String COMPONENT_SUFFIX_BIZCOMPONENT_EDITOR = " BizComponent Editor";

    /** String constant for the component name suffix - BizDriver Editor */
    public static final String COMPONENT_SUFFIX_BIZDRIVER_EDITOR = " BizDriver Editor";

    /** String constant for the component name suffix - Service Editor */
    public static final String COMPONENT_SUFFIX_SERVICE_EDITOR = " Service Editor";

    /** String constant for the component name suffix - BizComponent Execution */
    public static final String COMPONENT_SUFFIX_BIZCOMPONENT_EXECUTION = " BizComponent Execution";

    /** String constant for the component name - Streaming */
    public static final String COMPONENT_STREAMING = "Streaming";

    /** String constant for the component name - Distributed Transaction Management */
    public static final String COMPONENT_DISTRIBUTED_TRANSACTION = "Distributed Transaction Management";

    /** String constant for the component name - Security Authorization */
    public static final String COMPONENT_SECURITY_AUTHORIZATION = "Security Authorization";
}
