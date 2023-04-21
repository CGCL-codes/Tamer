package jhomenet.server.dao.xml;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import jhomenet.commons.ParsingException;
import jhomenet.commons.cfg.*;
import jhomenet.commons.hw.HardwareException;
import jhomenet.commons.hw.HomenetHardware;
import jhomenet.commons.hw.RegisteredHardware;
import jhomenet.commons.hw.mngt.HardwareFactory;
import jhomenet.commons.hw.sensor.Sensor;
import jhomenet.commons.hw.sensor.ValueSensor;
import jhomenet.server.dao.HardwareDao;

/**
 * TODO: Class description.
 * <p>
 * Id: $Id: $
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class HardwareDaoXml implements HardwareDao {

    /**
	 * Define the root logger.
	 */
    private static Logger logger = Logger.getLogger(HardwareDaoXml.class.getName());

    /**
	 * Known file elements.
	 */
    private static enum HardwareNodes {

        /**
		 * Hardware persistence element
		 */
        HARDWARE_PERSISTENCE("hardware-persistence"), /**
		 * Hardware element
		 */
        HARDWARE("hardware"), /**
		 * Hardware communication channel
		 */
        CHANNEL("channel"), /**
		 * Preferred data unit
		 */
        DATA_UNIT("preferred-unit");

        /**
		 * Element name
		 */
        private String nodeName;

        /**
		 * Constructor.
		 * 
		 * @param nodeName
		 */
        private HardwareNodes(String nodeName) {
            this.nodeName = nodeName;
        }

        /**
		 * Get the node name.
		 * 
		 * @return The node name
		 */
        String getNodeName() {
            return nodeName;
        }

        /**
		 * @see java.lang.Object#toString()
		 */
        @Override
        public String toString() {
            return getNodeName();
        }
    }

    /**
	 * Known hardware attributes.
	 */
    private static enum HardwareAttributes {

        /**
		 * Class name
		 */
        CLASSNAME("classname", "classname"), /**
		 * Hardware physical address
		 */
        HARADWARE_ADDRESS("hardware-address", "hardwareAddr"), /**
		 * Hardware setup description
		 */
        SETUP_DESC("setup-description", "hardwareSetupDescription"), /**
		 * Hardware polling interval
		 */
        POLLING_INTERVAL("polling-interval", "pollingInterval"), /**
		 * General ID
		 */
        ID("id", ""), /**
		 * Channel description.
		 */
        CHANNEL_DESCRIPTION("description", "channelDescription"), /**
		 * Preferred data unit
		 */
        PREFERRED_UNIT("value", "preferredDataUnit");

        /**
		 * Attribute name
		 */
        private final String attr;

        /**
		 * The name of the variable
		 */
        private final String variableName;

        /**
		 * Constructor.
		 * 
		 * @param attr
		 * @param variableName
		 */
        private HardwareAttributes(String attr, String variableName) {
            this.attr = attr;
            this.variableName = variableName;
        }

        /**
		 * Get the attribute name.
		 * 
		 * @return The attribute
		 */
        String getAttr() {
            return attr;
        }

        /**
		 * @see java.lang.Object#toString()
		 */
        @Override
        public String toString() {
            return getAttr();
        }

        /**
		 * @return Returns the variableName.
		 */
        final String getVariableName() {
            return variableName;
        }
    }

    private static final String defaultFilename = "hardwarepersistence.xml";

    private String filename = defaultFilename;

    /**
	 * The persistence folder.
	 */
    private final String foldername = "persistence" + Environment.SEPARATOR + "hardware" + Environment.SEPARATOR;

    /**
	 * 
	 */
    public HardwareDaoXml() {
        super();
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
	 * @see jhomenet.server.dao.GenericDao#clear()
	 */
    public void clear() {
    }

    /**
	 * @see jhomenet.server.dao.GenericDao#findAll()
	 */
    public List<RegisteredHardware> findAll() {
        logger.debug("Loading hardware persistence from XML file...");
        List<RegisteredHardware> hardwareList = new ArrayList<RegisteredHardware>();
        try {
            buildHardwareList(hardwareList);
        } catch (ParsingException pe) {
            logger.error("Error while loading hardware from persistence layer: " + pe.getMessage());
        }
        return hardwareList;
    }

    /**
	 * @see jhomenet.server.dao.GenericDao#findByExample(java.lang.Object, java.lang.String[])
	 */
    public List<RegisteredHardware> findByExample(RegisteredHardware exampleInstance, String... excludeProperty) {
        return null;
    }

    /**
	 * @see jhomenet.server.dao.GenericDao#findById(java.io.Serializable, boolean)
	 */
    public RegisteredHardware findById(Long id, boolean lock) {
        return null;
    }

    /**
	 * @see jhomenet.server.dao.GenericDao#flush()
	 */
    public void flush() {
    }

    /**
	 * @see jhomenet.server.dao.GenericDao#makePersistent(java.lang.Object)
	 */
    public RegisteredHardware makePersistent(RegisteredHardware entity) {
        return null;
    }

    /**
	 * @see jhomenet.server.dao.GenericDao#makeTransient(java.lang.Object)
	 */
    public void makeTransient(RegisteredHardware entity) {
    }

    /**
	 * Parse the server settings configuration file.
	 * 
	 * @return A reference to the Dom4j document object
	 * @throws ParsingException
	 */
    private Document parseFile() throws ParsingException {
        logger.debug("Parsing hardware persistence XML file: " + foldername + filename);
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(foldername + filename));
            return document;
        } catch (DocumentException de) {
            logger.error("Error while parsing hardware persistence XML file: " + de.getMessage());
            throw new ParsingException(de);
        }
    }

    /**
	 * Build the registered hardware map.
	 * 
	 * @param rootNode
	 * @param hardwareList
	 */
    private void buildHardwareList(List<RegisteredHardware> hardwareList) throws ParsingException {
        logger.debug("Building hardware persistence list");
        Element rootNode = parseFile().getRootElement();
        RegisteredHardware registeredHardware = null;
        Element element = null;
        String nodeName = null;
        for (Iterator i = rootNode.elementIterator(); i.hasNext(); ) {
            element = (Element) i.next();
            nodeName = element.getName();
            if (nodeName.equalsIgnoreCase(HardwareNodes.HARDWARE.getNodeName())) {
                registeredHardware = buildRegisteredHardware(element);
                logger.debug("Hardware [" + registeredHardware.getHardwareAddr() + "] successfully retrieved");
                if (registeredHardware != null) hardwareList.add(registeredHardware);
                registeredHardware = null;
            }
        }
    }

    /**
	 * Build the registered hardware object.
	 * 
	 * @param hardwareNode The XML hardware node
	 * @return A reference to the built registered hardware object
	 * @throws ParsingException
	 */
    private RegisteredHardware buildRegisteredHardware(Element hardwareNode) throws ParsingException {
        String hardwareClassname = hardwareNode.valueOf("@" + HardwareAttributes.CLASSNAME.getAttr());
        String hardwareAddr = hardwareNode.valueOf("@" + HardwareAttributes.HARADWARE_ADDRESS.getAttr());
        String setupDesc = hardwareNode.valueOf("@" + HardwareAttributes.SETUP_DESC.getAttr());
        String pollingInterval = hardwareNode.valueOf("@" + HardwareAttributes.POLLING_INTERVAL.getAttr());
        try {
            RegisteredHardware hardware = HardwareFactory.newHardware(hardwareClassname, hardwareAddr, setupDesc);
            if (hardware instanceof Sensor) this.setPollingInterval((Sensor) hardware, pollingInterval);
            for (Iterator i = hardwareNode.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                String nodeName = element.getName();
                if (hardware instanceof HomenetHardware) {
                    if (nodeName.equalsIgnoreCase(HardwareNodes.CHANNEL.getNodeName())) {
                        String channelId = element.valueOf("@" + HardwareAttributes.ID.getAttr());
                        String channelDesc = element.valueOf("@" + HardwareAttributes.CHANNEL_DESCRIPTION.getAttr());
                        setChannelDescription((HomenetHardware) hardware, Integer.parseInt(channelId), channelDesc);
                    } else if (nodeName.equalsIgnoreCase(HardwareNodes.DATA_UNIT.getNodeName())) {
                        String unitValue = element.valueOf("@" + HardwareAttributes.PREFERRED_UNIT.getAttr());
                        setPreferredUnit((ValueSensor) hardware, unitValue);
                    }
                }
            }
            return hardware;
        } catch (HardwareException he) {
            logger.error("Hardware exception while creating new hardware object: " + he.getMessage(), he);
            throw new ParsingException(he);
        }
    }

    /**
	 * Set the sensor polling interval.
	 *
	 * @param sensor
	 * @param pollingInterval
	 * @throws ParsingException
	 */
    private void setPollingInterval(Sensor sensor, String pollingInterval) throws ParsingException {
        setVariable(sensor, Sensor.class, HardwareAttributes.POLLING_INTERVAL.getVariableName(), new Object[] { pollingInterval }, new Class[] { String.class });
    }

    /**
	 * Set the sensor's preferred unit.
	 * 
	 * @param sensor
	 * @param preferredUnit
	 * @throws ParsingException
	 */
    private void setPreferredUnit(ValueSensor sensor, String preferredUnit) throws ParsingException {
        setVariable(sensor, ValueSensor.class, HardwareAttributes.PREFERRED_UNIT.getVariableName(), new Object[] { preferredUnit }, new Class[] { String.class });
    }

    /**
	 * Set the hardware channel description.
	 *
	 * @param hardware
	 * @param channel
	 * @param description
	 * @throws ParsingException
	 */
    private void setChannelDescription(HomenetHardware hardware, Integer channel, String description) throws ParsingException {
        setVariable(hardware, HomenetHardware.class, HardwareAttributes.CHANNEL_DESCRIPTION.getVariableName(), new Object[] { channel, description }, new Class[] { Integer.class, String.class });
    }

    /**
	 * Set the variable on the hardware object using reflection.
	 * 
	 * @param hardware
	 * @param hardwareClass
	 * @param variableName
	 * @param arguments
	 * @param parameterTypes
	 * @throws ParsingException
	 */
    private void setVariable(Object hardware, Class hardwareClass, String variableName, Object[] arguments, Class[] parameterTypes) throws ParsingException {
        Method method;
        try {
            method = hardwareClass.getDeclaredMethod("set" + toUpperCase(variableName), parameterTypes);
            method.setAccessible(true);
            method.invoke(hardware, arguments);
        } catch (NoSuchMethodException nsme) {
            throw new ParsingException(nsme);
        } catch (InvocationTargetException ite) {
            throw new ParsingException(ite);
        } catch (IllegalAccessException iae) {
            throw new ParsingException(iae);
        }
    }

    private static String toUpperCase(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (i == 0) {
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
