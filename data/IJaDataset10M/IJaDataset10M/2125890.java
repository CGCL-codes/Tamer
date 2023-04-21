package org.openremote.controller.deployer;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.openremote.controller.ControllerConfiguration;
import org.openremote.controller.statuscache.StatusCache;
import org.openremote.controller.model.sensor.Sensor;
import org.openremote.controller.model.Command;
import org.openremote.controller.exception.InitializationException;
import org.openremote.controller.exception.ControllerDefinitionNotFoundException;
import org.openremote.controller.exception.XMLParsingException;
import org.openremote.controller.utils.PathUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jdom.input.SAXBuilder;

/**
 * Controller's object model builder for the current 2.0 version of the implementation. <p>
 *
 * Certain build tasks are delegated to related sub-components, for example sensor and command
 * model builds.
 *
 * @see org.openremote.controller.model.xml.Version20SensorBuilder
 * @see DeviceProtocolBuilder
 *
 * @author <a href="mailto:juha@openremote.org">Juha Lindfors</a>
 */
public class Version20ModelBuilder extends AbstractModelBuilder {

    /**
   * File name of the controller definition XML document. The file is located in
   * the 'resource path' directory that can be found from the controller configuration object.
   *
   * @see ControllerConfiguration#getResourcePath()
   */
    public static final String CONTROLLER_XML = "controller.xml";

    /**
   * We are using JAXP API to access the XML parser -- this property name is used to configure
   * XML schema validation (value is defined in {@link #W3C_XML_SCHEMA}). <p>
   *
   * Should it happen that JAXP is not used to access the XML parser (currently done implicitly
   * by the JDOM library), a different property would likely be needed to enable schema validation.
   */
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /**
   * The value for {@link #JAXP_SCHEMA_LANGUAGE} property -- using W3C XML schema as validation
   * language.
   */
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    /**
   * We are using JAXP API to access the XML parser (via use of JDOM library) -- this property
   * name is used to configure XML schema location. It only applies if {@link #JAXP_SCHEMA_LANGUAGE}
   * has been defined first. <p>
   *
   * NOTE: Configuring schema source explicitly in code will override schema definitions given in
   * the XML document.
   */
    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    /**
   * TODO : see ORCJAVA-210
   *   - relative path seems potentially problematic, could use path from config or 'resource.path'
   *   - rename to remove M7 suffix
   */
    private static final String CONTROLLER_XSD_PATH = "/controller-2.0-M7.xsd";

    /**
   * Config property element's "type" attribute in controller.xml file, i.e.
   *
   * <pre>
   * {@code
   *       <config>
   *         <property name = "config.property.name" value = "property-value"/>
   *         ...
   *       </config>
   * }</pre>
   */
    private static final String XML_CONFIG_PROPERTY_NAME_ATTR = "name";

    /**
   * Config property element's "value" attribute in controller.xml file, i.e.
   *
   * <pre>
   * {@code
   *       <config>
   *         <property name = "config.property.name" value = "property-value"/>
   *         ...
   *       </config>
   * }</pre>
   */
    private static final String XML_CONFIG_PROPERTY_VALUE_ATTR = "value";

    /**
   * Identifiers for specific segments in the XML schema this model builder implementation parses.
   */
    protected enum XMLSegment {

        /**
     * Enum for {@code<sensors>} section in the XML document instance.
     */
        SENSORS("sensors"), /**
     * Enum for {@code<config>} section in the XML document instance.
     */
        CONFIG("config"), /**
     * Enum for {@code<commands>} section in the XML document instance.
     */
        COMMANDS("commands");

        /**
     * Actual element name in the XML document instance.
     */
        private String elementName;

        /**
     * @param elementName   actual element name in the XML document instance
     */
        private XMLSegment(String elementName) {
            this.elementName = elementName;
        }

        /**
     * Runs an XPath query on the given model builder's associated XML definition to retrieve
     * the element nodes indicated by this enum's element name.
     *
     * @param   doc   reference to the in-memory XML document model that contains this controller's
     *                definition
     *
     * @return  Returns the XML element and sub-elements indicated by this enum element name.
     *
     * @throws  InitializationException
     *              if the model builder's XML definition file cannot be read or accessed otherwise;
     *              if there's an error in attempting to retrieve the XML nodes indicated by
     *              this enum's element name
     */
        protected Element query(Document doc) throws InitializationException {
            return Version20ModelBuilder.queryElementFromXML(doc, elementName);
        }
    }

    /**
   * Checks if the controller.xml file exists in the configured location.
   *
   * @param   config    controller's user configuration
   *
   * @return  true if file exists; false if file does not exists or access was denied by
   *          security manager
   */
    public static boolean checkControllerDefinitionExists(ControllerConfiguration config) {
        final File file = getControllerDefinitionFile(config);
        try {
            return AccessController.doPrivilegedWithCombiner(new PrivilegedAction<Boolean>() {

                @Override
                public Boolean run() {
                    return file.exists();
                }
            });
        } catch (SecurityException e) {
            log.error("Security manager prevented read access to file ''{0}'' : {1}", e, file.getAbsoluteFile(), e.getMessage());
            return false;
        }
    }

    /**
   * Utility method to return a Java I/O File instance representing the artifact with
   * controller runtime object model (version 2.0) definition.
   *
   * @param   config    controller's user configuration
   *
   * @return  file representing an object model definition for a controller
   */
    public static File getControllerDefinitionFile(ControllerConfiguration config) {
        try {
            URI uri = new URI(config.getResourcePath());
            return new File(uri.resolve(CONTROLLER_XML));
        } catch (Throwable t) {
            String xmlPath = PathUtil.addSlashSuffix(config.getResourcePath()) + CONTROLLER_XML;
            return new File(xmlPath);
        }
    }

    /**
   * Reference to status cache instance that manages the sensors. The sensor instances created
   * by this builder will be registered with this cache.
   */
    private StatusCache deviceStateCache;

    /**
   * User defined controller configuration variables.
   *
   * TODO : see ORCJAVA-183, ORCJAVA-193, ORCJAVA-170
   */
    private ControllerConfiguration config;

    /**
   * Indicates whether the controller.xml for this schema implementation has been found.
   *
   * @see #hasControllerDefinitionChanged()
   */
    private boolean controllerDefinitionIsPresent;

    /**
   * Last known timestamp of controller.xml file.
   */
    private long lastTimeStamp = 0L;

    /**
   * This model builder delegates parsing of {@code <sensors>} segment in XML schema to this
   * sub-builder.
   */
    private SensorBuilder sensorBuilder;

    /**
   * This model builder delegates parsing of {@code <commands>} segment in XML schema to this
   * sub-builder.
   */
    private DeviceProtocolBuilder deviceProtocolBuilder;

    /**
   * Constructs a builder for version 2.0 of controller schema.
   *
   * @param cache
   *            Reference to a device state cache associated with this object model builder.
   *            This implementation initializes the cache with components that are deployed
   *            as part of this model creation.
   *
   * @param config
   *            User controller configuration for this controller.
   *
   * @param sensorBuilder
   *            Reference to a delegate object that handles the details of parsing the
   *            {@code <sensors>} segment of the controller definition schema.
   *
   * @param deviceProtocolBuilder
   *            Reference to a delegate object that handles the details of parsing the
   *            {@code <commands>} segment of the controller definition schema.
   *
   * @throws InitializationException
   *            if the XML document containing this controller's definition cannot be read
   *            for any reason
   */
    public Version20ModelBuilder(StatusCache cache, ControllerConfiguration config, SensorBuilder<Version20ModelBuilder> sensorBuilder, DeviceProtocolBuilder deviceProtocolBuilder) throws InitializationException {
        if (cache == null) {
            throw new IllegalArgumentException("Must include a reference to device state cache.");
        }
        this.deviceStateCache = cache;
        if (sensorBuilder == null) {
            throw new IllegalArgumentException("Must include a reference to sensor builder.");
        }
        this.sensorBuilder = sensorBuilder;
        sensorBuilder.setModelBuilder(this);
        if (deviceProtocolBuilder == null) {
            throw new IllegalArgumentException("Must include a reference to a device protocol builder.");
        }
        this.deviceProtocolBuilder = deviceProtocolBuilder;
        this.config = config;
        controllerDefinitionIsPresent = checkControllerDefinitionExists(config);
        if (controllerDefinitionIsPresent) {
            lastTimeStamp = getControllerXMLTimeStamp();
        }
    }

    /**
   * Returns a map of configuration properties defined in controller's definition within
   * {@code <config>} section. These properties should be used to override the system default
   * properties.
   *
   * @return  map of name,value strings representing configuration properties
   */
    public Map<String, String> getConfigurationProperties() {
        Element element;
        try {
            element = XMLSegment.CONFIG.query(controllerXMLDefinition);
            if (element == null) {
                log.info("No configuration overrides in this deployment -- using defaults.");
                return new HashMap<String, String>(0);
            }
        } catch (InitializationException e) {
            log.error("Error in applying configuration properties -- using defaults : {0}", e, e.getMessage());
            return new HashMap<String, String>(0);
        }
        Map<String, String> propertyMap = new HashMap<String, String>();
        List<Element> properties = getChildElements(element);
        for (Element property : properties) {
            String name = property.getAttributeValue(XML_CONFIG_PROPERTY_NAME_ATTR);
            String value = property.getAttributeValue(XML_CONFIG_PROPERTY_VALUE_ATTR);
            propertyMap.put(name, value);
        }
        return propertyMap;
    }

    /**
   * Returns a reference to a device state cache associated with this model builder. This is
   * intended for delegate builders which need to associate model objects (e.g. sensors)
   * with the cache.
   * 
   * @return    state cache reference
   */
    public StatusCache getDeviceStateCache() {
        return deviceStateCache;
    }

    /**
   * Attempts to determine whether the controller.xml 'last modified' timestamp has changed,
   * or if the file has been removed altogether, or if the file was not present earlier but
   * has been added since last check. <p>
   *
   * All the above cases yield an indication that the controller's model definition has changed
   * which can in turn result in reloading the model by the deployer (see
   * {@link org.openremote.controller.service.Deployer.ControllerDefinitionWatch} for more
   * details).
   *
   * @return  true if controller.xml has been changed, removed or added since last check,
   *          false otherwise
   */
    @Override
    public boolean hasControllerDefinitionChanged() {
        if (controllerDefinitionIsPresent) {
            if (!checkControllerDefinitionExists(config)) {
                controllerDefinitionIsPresent = false;
                return true;
            }
            long lastModified = getControllerXMLTimeStamp();
            if (lastModified > lastTimeStamp) {
                lastTimeStamp = lastModified;
                return true;
            }
        } else {
            if (checkControllerDefinitionExists(config)) {
                controllerDefinitionIsPresent = true;
                return true;
            }
        }
        return false;
    }

    /**
   * Sequence of actions to build object model based on the current 2.0 schema.
   */
    @Override
    protected void build() {
        buildSensorModel();
        buildCommandModel();
    }

    /**
   * Returns an XML document instance built from {@link #CONTROLLER_XML}. The XML parser is
   * located with JAXP API (via JDOM library). By default the document instance is validated
   * with XML schema.
   *
   * @return a built document for controller.xml
   */
    @Override
    protected Document readControllerXMLDocument() throws InitializationException {
        SAXBuilder builder = new SAXBuilder();
        String xsdPath = CONTROLLER_XSD_PATH;
        File controllerXMLFile = getControllerDefinitionFile(config);
        if (!checkControllerDefinitionExists(config)) {
            try {
                throw new ControllerDefinitionNotFoundException("Controller.xml not found -- make sure it's in " + controllerXMLFile.getAbsoluteFile());
            } catch (SecurityException e) {
                throw new InitializationException("Cannot access controller.xml : {0}", e, e.getMessage());
            }
        }
        try {
            URL xsdResource = Version20ModelBuilder.class.getResource(xsdPath);
            if (xsdResource == null) {
                log.error("Cannot find XSD schema ''{0}''. Disabling validation...", xsdPath);
            } else {
                xsdPath = xsdResource.getPath();
                builder.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
                builder.setProperty(JAXP_SCHEMA_SOURCE, new File(xsdPath));
                builder.setValidation(true);
            }
            return builder.build(controllerXMLFile);
        } catch (Throwable t) {
            throw new XMLParsingException("Unable to parse controller definition from " + "''{0}'' (accessing schema from ''{1}'') : {2}", t, controllerXMLFile.getAbsoluteFile(), xsdPath, t.getMessage());
        }
    }

    /**
   * Build concrete sensor Java instances from the XML declaration. <p>
   *
   * NOTE: this implementation will register and start the sensors at build time automatically.
   */
    protected void buildSensorModel() {
        Set<Sensor> sensors = buildSensorObjectModelFromXML();
        for (Sensor sensor : sensors) {
            deviceStateCache.registerSensor(sensor);
            sensor.start();
        }
    }

    /**
   * Builds a concrete Java instances which represent commands from the controller.xml definition.
   * <p>
   *
   * Note that this command model is 'anemic' -- a complete model should merge with the command
   * model defined in org.openremote.controller.command package. This is currently left undone
   * to avoid breaking the existing protocol integration API. See ORCJAVA-209 for more details. <p>
   *
   * NOTE: The event context used in event processing chain of incoming events to status cache is
   * initialized as part of this method implementation with the parsed command object model.
   */
    protected void buildCommandModel() {
        Set<Command> commands = buildCommandObjectModelFromXML();
        deviceStateCache.initializeEventContext(commands);
    }

    /**
   * Parse command definitions from controller.xml and create the corresponding Java objects. <p>
   *
   * See to-do notes about the issues with the object model in {@link #buildCommandModel()}.
   *
   * @return  list of command instances that were succesfully built from the controller.xml
   *          document instance
   */
    protected Set<Command> buildCommandObjectModelFromXML() {
        Element commandsElement = null;
        try {
            commandsElement = XMLSegment.COMMANDS.query(controllerXMLDefinition);
            if (commandsElement == null) {
                log.warn("No commands found.");
                return new HashSet<Command>(0);
            }
        } catch (InitializationException e) {
            log.error("Error loading command definitions from {0} : {1}", e, CONTROLLER_XML, e.getMessage());
        }
        Iterator<Element> commandElementIterator = getChildElements(commandsElement).iterator();
        Set<Command> commandModels = new HashSet<Command>();
        while (commandElementIterator.hasNext()) {
            Element commandElement = commandElementIterator.next();
            try {
                Command cmd = deviceProtocolBuilder.build(commandElement);
                log.debug("Created object model for {0}.", cmd);
                commandModels.add(cmd);
            } catch (Throwable t) {
                log.error("Creating command failed. Error: {0} \n XML Element : {1}", t, t.getMessage(), new XMLOutputter().outputString(commandElement));
            }
        }
        return commandModels;
    }

    /**
   * Parse sensor definitions from controller.xml and create the corresponding Java objects. <p>
   *
   * @return  list of sensor instances that were succesfully built from the controller.xml
   *          document instance
   */
    protected Set<Sensor> buildSensorObjectModelFromXML() {
        Element sensorsElement = null;
        try {
            sensorsElement = XMLSegment.SENSORS.query(controllerXMLDefinition);
            if (sensorsElement == null) {
                log.info("No sensors found.");
                return new HashSet<Sensor>(0);
            }
        } catch (InitializationException e) {
            log.error("Error loading sensor definitions from {0} : {1}", e, CONTROLLER_XML, e.getMessage());
        }
        Iterator<Element> sensorElementIterator = getChildElements(sensorsElement).iterator();
        Set<Sensor> sensorModels = new HashSet<Sensor>();
        while (sensorElementIterator.hasNext()) {
            Element sensorElement = sensorElementIterator.next();
            try {
                Sensor sensor = sensorBuilder.build(sensorElement);
                log.debug("Created object model for sensor ''{0}'' (ID = ''{1}'').", sensor.getName(), sensor.getSensorID());
                sensorModels.add(sensor);
            } catch (Throwable t) {
                log.error("Creating sensor failed. Error : {0} \n XML Element : {1}", t, t.getMessage(), new XMLOutputter().outputString(sensorElement));
            }
        }
        return sensorModels;
    }

    /**
   * Returns the timestamp of controller.xml file of this controller object model.
   *
   * @return  last modified timestamp, or zero if the timestamp cannot be accessed
   */
    private long getControllerXMLTimeStamp() {
        final File controllerXML = getControllerDefinitionFile(config);
        try {
            return AccessController.doPrivilegedWithCombiner(new PrivilegedAction<Long>() {

                @Override
                public Long run() {
                    return controllerXML.lastModified();
                }
            });
        } catch (SecurityException e) {
            log.error("Security manager prevented access to timestamp of file ''{0}'' ({1}). " + "Automatic detection of controller.xml file modifications are disabled.", e, controllerXML, e.getMessage());
            return 0L;
        }
    }
}
