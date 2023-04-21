package org.txt2xml.config;

import java.beans.Beans;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.BeanMap;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.txt2xml.core.Processor;

/**
 * Create a Processor from a configuration file.
 * <p>
 * Config file of the form:
 * <pre>
 *     &lt;txt2xml>
 * *****
 *     &lt;/txt2xml>
 * </pre>
 * </p>
 * 
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 */
public class ProcessorFactory {

    private static final Logger LOG = Logger.getLogger(ProcessorFactory.class.getName());

    private static final String ROOT_ELEMENT = "txt2xml";

    private static final String PROCESSOR_ELEMENT = "processor";

    private static final String TYPE_ATTRIBUTE = "type";

    private static final String TYPES_CONFIG_NAME = "/types_config.properties";

    private static Properties typeMap;

    static {
        Properties defaults = new Properties();
        defaults.put("RegexDelimited", "org.txt2xml.core.RegexDelimitedProcessor");
        defaults.put("RegexMatch", "org.txt2xml.core.RegexMatchProcessor");
        defaults.put("RepeatRegexMatch", "org.txt2xml.core.RepeatRegexMatchProcessor");
        defaults.put("StringEnclosedMatch", "org.txt2xml.core.StringEnclosedMatchProcessor");
        defaults.put("Copy", "org.txt2xml.core.CopyProcessor");
        defaults.put("LA", "org.txt2xml.core.LocateArtifactResultProcessor");
        defaults.put("LAT", "org.txt2xml.core.LocateArtifactTrueResultProcessor");
        defaults.put("LAOwner", "org.txt2xml.core.LocateArtifactWithOwnerResultProcessor");
        defaults.put("LATOwner", "org.txt2xml.core.LocateArtifactTrueWithOwnerResultProcessor");
        defaults.put("CharOrders", "org.txt2xml.core.CharacterOrdersProcessor");
        defaults.put("DoubleAgent", "org.txt2xml.core.DoubleAgentProcessor");
        typeMap = new Properties(defaults);
        InputStream typesConfigStream = ProcessorFactory.class.getResourceAsStream(TYPES_CONFIG_NAME);
        if (typesConfigStream != null) {
            try {
                if (LOG.isLoggable(Level.FINER)) LOG.finer("Loading custom typeMap: " + TYPES_CONFIG_NAME);
                typeMap.load(typesConfigStream);
            } catch (IOException e) {
                LOG.warning("Can't load txt2xml types_config.properties: " + e.toString());
            }
        }
    }

    private static final ProcessorFactory instance = new ProcessorFactory();

    private ProcessorFactory() {
    }

    public static ProcessorFactory getInstance() {
        return instance;
    }

    /**
     * <p>
     * Read the configuration and return the Processor
     * defined: this will include sub-Processors or following
     * Processors if defined in the config.
     * </p>
     * 
     * @param configReader Reader for a config definition.
     * @return Processor the Processor defined by the
     * config file at the passed URL.
     */
    public Processor createProcessor(Reader configReader) throws ConfigException {
        try {
            Document document = new SAXBuilder().build(configReader);
            Element rootElement = document.getRootElement();
            if (!ROOT_ELEMENT.equals(rootElement.getName())) {
                throw new ConfigException("Configuration's root element '" + rootElement.getName() + "' should be '" + ROOT_ELEMENT + "'");
            }
            return readProcessorSequenceFrom(rootElement);
        } catch (JDOMException e) {
            throw new ConfigException("Can't parse configuration: " + configReader, e);
        }
    }

    /**
     * <p>
     * Read the configuration and return the Processor
     * defined: this will include sub-Processors or following
     * Processors if defined in the config.
     * </p>
     * 
     * @param configUrl URL of a config file.
     * @return Processor the Processor defined by the
     * config file at the passed URL.
     */
    public Processor createProcessor(URL configUrl) throws ConfigException {
        Reader reader = null;
        try {
            reader = new InputStreamReader(configUrl.openStream());
            return createProcessor(reader);
        } catch (IOException e) {
            throw new ConfigException("Can't open configuration: " + configUrl, e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                throw new ConfigException("Can't read configuration: " + configUrl, e);
            }
        }
    }

    /**
     * <p>
     * Read config contained by the passed parent Element and
     * return the sequence of Processors defined there, which might include
     * sub-Processors and following Processors if defined.
     * </p>
     * <p>
     * Note that non-"Processor" elements are silently ignored.
     * </p>
     * 
     * @param parentElement the Element containing a Processor
     * definition. Only child "Processor" elements are handled.
     * @return Processor the Processor defined within the parent Eement.
     */
    protected Processor readProcessorSequenceFrom(Element parentElement) throws ConfigException {
        Processor resultProcessor = null;
        Processor currentProcessor = null;
        for (Iterator i = parentElement.getChildren(PROCESSOR_ELEMENT).iterator(); i.hasNext(); ) {
            Element processorElement = (Element) i.next();
            Processor nextProcessor = readProcessor(processorElement);
            if (currentProcessor == null) {
                resultProcessor = nextProcessor;
                currentProcessor = nextProcessor;
            } else {
                currentProcessor.setNextProcessor(nextProcessor);
                currentProcessor = nextProcessor;
            }
        }
        return resultProcessor;
    }

    /**
     * <p>
     * Read config for a Processor in the passed "Processor"
     * Element and return the Processor defined there, including
     * sub-Processors.
     * </p>
     * <p>
     * The type of a Processor is read from the processor_types.properties
     * configuration. The following types are pre-defined but can be overriden
     * in a "processor_types.properties" file:
     * <ul>
     * <li>RegexDelimited -> org.txt2xml.core.RegexDelimitedProcessor</li>
     * </ul>
     * Processors are created using java.beans.Beans.instantiate() so
     * serialised Processors can be loaded with the appropriate
     * config. See JDK javadoc.
     * </p>
     * 
     * @param processorElement the top-level Processor Element.
     * @return Processor matching the config.
     */
    protected Processor readProcessor(Element processorElement) throws ConfigException {
        String type = processorElement.getAttributeValue(TYPE_ATTRIBUTE);
        String processorBeanName = typeMap.getProperty(type);
        if (processorBeanName == null) {
            throw new ConfigException("Can't create a Processor of type: " + type);
        }
        Processor processor;
        try {
            processor = (Processor) Beans.instantiate(getClass().getClassLoader(), processorBeanName);
        } catch (Exception e) {
            throw new ConfigException("Can't create a Processor bean named: " + processorBeanName);
        }
        BeanMap beanMap = new BeanMap(processor);
        for (Iterator i = processorElement.getChildren().iterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            if (!PROCESSOR_ELEMENT.equals(element.getName())) {
                beanMap.put(element.getName(), element.getTextTrim());
            }
        }
        Processor subProcessor = readProcessorSequenceFrom(processorElement);
        processor.setSubProcessor(subProcessor);
        return processor;
    }
}
