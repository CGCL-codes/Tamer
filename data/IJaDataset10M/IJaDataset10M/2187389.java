package de.fzi.herakles.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * read the configuration information of center reasoner factory and distribute reasoner factory
 * from the XML file. 
 * @author Xu
 *
 */
@Deprecated
public class Configuration {

    private final String defaultCenterFactoryCfgFile = "centerFactory.xml";

    private final String defaultDistributeFactoryCfgFile = "distributeFactory.xml";

    private final String defaultHeraklesCfgFile = "herakles.xml";

    private String centerReasonerFactoryHost;

    private String centerReasonerFactoryName;

    private HashMap<String, Map<Integer, String>> distributeReasonerFactoryHost;

    private HashMap<String, Map<Integer, String>> distributeReasonerFactoryName;

    private Properties property;

    public Configuration() {
        centerReasonerFactoryHost = "";
        centerReasonerFactoryName = "";
        distributeReasonerFactoryHost = new HashMap<String, Map<Integer, String>>();
        distributeReasonerFactoryName = new HashMap<String, Map<Integer, String>>();
    }

    /**
	 * load the herakles configuration by using default file name
	 */
    public void loadHeraklesConfiguration() {
        loadHeraklesConfiguration(defaultHeraklesCfgFile);
    }

    /**
	 * load the herakles confiuration by using special file name
	 * @param filename the name of the configuration file
	 */
    public void loadHeraklesConfiguration(String filename) {
        property = new Properties();
        File file = new File(filename);
        if (file.exists()) {
            if (!file.canRead()) {
                System.out.println("The Configuration file can not be read!");
            } else {
                try {
                    InputStream in = new FileInputStream(file);
                    property.loadFromXML(in);
                } catch (Exception e) {
                    System.out.println("The format of the Configuration file is not correct!");
                    property = new Properties();
                }
            }
        } else {
            System.out.println("The Configuration file " + file.getAbsolutePath() + " can not be found!");
        }
    }

    /**
	 * get the loaded herakles configuration 
	 * @return herakles property
	 */
    public Properties getHeraklesConfiguration() {
        return property;
    }

    /**
	 * load the configuration file of center reasoner factory
	 * @param filename the name of the center reasoner factory configuration file
	 */
    public void loadCenterReasonerFactoryConfiguration(String filename) {
        InputStream in;
        try {
            in = new FileInputStream(filename);
            loadCenter(in);
        } catch (FileNotFoundException e) {
            System.out.println("The confilguration file centerFactory.xml can not be found!");
            e.printStackTrace();
        } catch (XMLStreamException e) {
            System.out.println("The format of the confilguration file " + "centerFactory.xml is not correct!");
            e.printStackTrace();
        }
    }

    /**
	 * load the center configuration xml file
	 * @param in input stream of the center configuartion file
	 * @throws FactoryConfigurationError XML input factory error
	 * @throws XMLStreamException XML stream error
	 */
    private void loadCenter(InputStream in) throws FactoryConfigurationError, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader parser = factory.createXMLEventReader(in);
        while (parser.hasNext()) {
            XMLEvent event = parser.nextEvent();
            switch(event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    StartElement element = event.asStartElement();
                    if (element.getName().toString().equalsIgnoreCase("Factory")) {
                        while (parser.hasNext()) {
                            XMLEvent event2 = parser.nextEvent();
                            if (event2.getEventType() == XMLStreamConstants.START_ELEMENT) {
                                StartElement element2 = event2.asStartElement();
                                if (element2.getName().toString().equalsIgnoreCase("host")) {
                                    event2 = parser.nextEvent();
                                    if (!event2.isCharacters()) {
                                        System.out.println("incorrcet configuration file format!");
                                        return;
                                    }
                                    Characters characters = event2.asCharacters();
                                    if (characters.isWhiteSpace()) {
                                        System.out.println("incorrcet configuration file format!");
                                        return;
                                    }
                                    centerReasonerFactoryHost = characters.getData().trim();
                                }
                                if (element2.getName().toString().equalsIgnoreCase("name")) {
                                    event2 = parser.nextEvent();
                                    if (!event2.isCharacters()) {
                                        System.out.println("incorrcet configuration file format!");
                                        return;
                                    }
                                    Characters characters = event2.asCharacters();
                                    if (characters.isWhiteSpace()) {
                                        System.out.println("incorrcet configuration file format!");
                                        return;
                                    }
                                    centerReasonerFactoryName = characters.getData().trim();
                                }
                            } else if (event2.getEventType() == XMLStreamConstants.END_ELEMENT) {
                                EndElement element3 = event2.asEndElement();
                                if (element3.getName().toString().equalsIgnoreCase("Factory")) {
                                    break;
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
	 * load the configuration file of center reasoner factory
	 */
    public void loadCenterReasonerFactoryConfiguration() {
        loadCenterReasonerFactoryConfiguration(defaultCenterFactoryCfgFile);
    }

    /**
	 * get the center reasoner Factory host
	 * @return the host of the center reasoner factory
	 */
    public String getCenterReasonerFactoryHost() {
        return centerReasonerFactoryHost;
    }

    /**
	 * get the center reasoner Factory name
	 * @return the RMI name of the center reasoner factory
	 */
    public String getCenterReasonerFactoryName() {
        return centerReasonerFactoryName;
    }

    /**
	 * load the configuration file of distribute reasoner factory
	 * @param filename the name of the distribute reasoner factory configuration file
	 */
    public void loadDistributeReasonerFactoryConfiguration(String filename) {
        InputStream in;
        try {
            in = new FileInputStream(filename);
            loadDistribute(in);
        } catch (FileNotFoundException e) {
            System.out.println("The confilguration file distributeFactory.xml can not be found!");
            e.printStackTrace();
        } catch (XMLStreamException e) {
            System.out.println("The format of the confilguration file " + "distributeFactory.xml is not correct!");
            e.printStackTrace();
        }
    }

    /**
	 * load the distribute configuration xml file
	 * @param in input stream of distribute configuration file
	 * @throws FactoryConfigurationError XML factory error
	 * @throws XMLStreamException XML stream error
	 */
    private void loadDistribute(InputStream in) throws FactoryConfigurationError, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader parser = factory.createXMLEventReader(in);
        while (parser.hasNext()) {
            XMLEvent event = parser.nextEvent();
            switch(event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    StartElement element = event.asStartElement();
                    if (element.getName().toString().equalsIgnoreCase("Factory")) {
                        String type = "UNKNOWN";
                        String host = "";
                        String name = "";
                        int index = 0;
                        while (parser.hasNext()) {
                            XMLEvent event2 = parser.nextEvent();
                            if (event2.getEventType() == XMLStreamConstants.START_ELEMENT) {
                                StartElement element2 = event2.asStartElement();
                                if (element2.getName().toString().equalsIgnoreCase("host")) {
                                    event2 = parser.nextEvent();
                                    if (!event2.isCharacters()) {
                                        System.out.println("incorrcet configuration file format!");
                                        return;
                                    }
                                    Characters characters = event2.asCharacters();
                                    if (characters.isWhiteSpace()) {
                                        System.out.println("incorrcet configuration file format!");
                                        return;
                                    }
                                    host = characters.getData().trim();
                                }
                                if (element2.getName().toString().equalsIgnoreCase("name")) {
                                    event2 = parser.nextEvent();
                                    if (!event2.isCharacters()) {
                                        System.out.println("incorrcet configuration file format!");
                                        return;
                                    }
                                    Characters characters = event2.asCharacters();
                                    if (characters.isWhiteSpace()) {
                                        System.out.println("incorrcet configuration file format!");
                                        return;
                                    }
                                    name = characters.getData().trim();
                                }
                                if (element2.getName().toString().equalsIgnoreCase("type")) {
                                    event2 = parser.nextEvent();
                                    if (!event2.isCharacters()) {
                                        System.out.println("incorrcet configuration file format!");
                                        return;
                                    }
                                    Characters characters = event2.asCharacters();
                                    if (characters.isWhiteSpace()) {
                                        System.out.println("incorrcet configuration file format!");
                                        return;
                                    }
                                    type = characters.getData().toUpperCase();
                                }
                            } else if (event2.getEventType() == XMLStreamConstants.END_ELEMENT) {
                                EndElement element3 = event2.asEndElement();
                                if (element3.getName().toString().equalsIgnoreCase("Factory")) {
                                    if (!distributeReasonerFactoryHost.containsKey(type)) {
                                        distributeReasonerFactoryHost.put(type, new HashMap<Integer, String>());
                                        distributeReasonerFactoryName.put(type, new HashMap<Integer, String>());
                                    }
                                    Map<Integer, String> hosts = distributeReasonerFactoryHost.get(type);
                                    Map<Integer, String> names = distributeReasonerFactoryName.get(type);
                                    index = hosts.size() + 1;
                                    hosts.put(index, host);
                                    names.put(index, name);
                                    break;
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
	 * load the configuration file of distribute reasoner factory
	 */
    public void loadDistributeReasonerFactoryConfiguration() {
        loadDistributeReasonerFactoryConfiguration(defaultDistributeFactoryCfgFile);
    }

    /**
	 * get the host of the distribute reasoner factory
	 * @param type reasoner type
	 * @return the RMI host of distribute reasoner factory of the input type
	 */
    public Map<Integer, String> getDistributeReasonerFactoryHost(String type) {
        Map<Integer, String> results = new HashMap<Integer, String>();
        if (distributeReasonerFactoryHost.containsKey(type)) {
            results.putAll(distributeReasonerFactoryHost.get(type));
        }
        return results;
    }

    /**
	 * get the RMI name of the distribute reasoner factory
	 * @param type reasoner type
	 * @return the RMI name of distribute reasoner factory of the input type
	 */
    public Map<Integer, String> getDistributeReasonerFactoryName(String type) {
        Map<Integer, String> results = new HashMap<Integer, String>();
        if (distributeReasonerFactoryName.containsKey(type)) {
            results.putAll(distributeReasonerFactoryName.get(type));
        }
        return results;
    }

    /**
	 * get the index of the distribute reasoner factory
	 * 
	 * @param type reasoner type
	 * @return the indexs of the distribute reasoner factories
	 */
    public Set<Integer> getDistributeReasonerFactoryIndexs(String type) {
        HashSet<Integer> indexs = new HashSet<Integer>();
        if (distributeReasonerFactoryName.containsKey(type)) {
            indexs.addAll(distributeReasonerFactoryName.get(type).keySet());
        }
        return indexs;
    }

    /**
	 * get all the types of the reasoner
	 * @return the types of all distribute reasoners
	 */
    public Set<String> getDistributeReasonerTypes() {
        HashSet<String> types = new HashSet<String>();
        types.addAll(distributeReasonerFactoryHost.keySet());
        return types;
    }
}
