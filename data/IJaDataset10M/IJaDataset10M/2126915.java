package eu.soa4all.execution.adapter.parser.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Class MappingParser used to start a new parsing process
 */
public class MappingParser {

    static Logger log = Logger.getLogger(MappingParser.class);

    private ArrayList<InterfaceRule> interfaceRules;

    private ArrayList<ProtocolRule> protocolRules;

    private String requestorNameSpace;

    private String serviceNameSpace;

    private String startEndpoint;

    private String finalEndpoint;

    private static MappingParser instance;

    private static final String interfaceRuleString = "InterfaceRule";

    private static final String protocolRuleString = "ProtocolRule";

    /**
	 * Default constructor
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
    private MappingParser() {
    }

    /**
	 * NEW METHOD
	 * Constructor with mappingFile in input
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
    public MappingParser(File mappingFile) {
        try {
            log.trace("Analyzing the mapping file");
            interfaceRules = new ArrayList<InterfaceRule>();
            protocolRules = new ArrayList<ProtocolRule>();
            SAXBuilder saxBuilder = new SAXBuilder();
            File f = mappingFile;
            Document doc = saxBuilder.build(f);
            parse(doc);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (interfaceRules == null && protocolRules == null) log.warn("No mapping script found or no needed for adaption");
        MappingParser.instance = this;
    }

    /**
	 * Constructor with mappingScriptPath in input
	 * @throws IOException 
	 * @throws JDOMException 
	 */
    public MappingParser(String mappingScriptPath) {
        interfaceRules = new ArrayList<InterfaceRule>();
        protocolRules = new ArrayList<ProtocolRule>();
        SAXBuilder saxBuilder = new SAXBuilder();
        File f = new File(mappingScriptPath);
        Document doc;
        try {
            doc = saxBuilder.build(f);
            parse(doc);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (interfaceRules == null && protocolRules == null) log.warn("No mapping script found or no needed for adaption");
        MappingParser.instance = this;
    }

    private void parse(Document doc) {
        Element root = doc.getRootElement();
        ArrayList<Element> interfaceChildren = new ArrayList<Element>();
        ArrayList<Element> protocolChildren = new ArrayList<Element>();
        Iterator rootIterator = root.getChildren().iterator();
        while (rootIterator.hasNext()) {
            Element elem = (Element) rootIterator.next();
            if (elem.getName().equalsIgnoreCase("RequestorNameSpace")) requestorNameSpace = elem.getTextTrim(); else if (elem.getName().equalsIgnoreCase("ServiceNameSpace")) serviceNameSpace = elem.getTextTrim(); else if (elem.getName().equalsIgnoreCase("StartEndpoint")) startEndpoint = elem.getTextTrim(); else if (elem.getName().equalsIgnoreCase("FinalEndpoint")) finalEndpoint = elem.getTextTrim(); else if (elem.getName().equalsIgnoreCase(interfaceRuleString)) interfaceChildren.add(elem); else if (elem.getName().equalsIgnoreCase(protocolRuleString)) protocolChildren.add(elem);
        }
        Iterator ifaceIterator = interfaceChildren.iterator();
        Iterator pcolIterator = protocolChildren.iterator();
        while (ifaceIterator.hasNext()) {
            interfaceRules.add(new InterfaceRule((Element) ifaceIterator.next()));
        }
        while (pcolIterator.hasNext()) {
            protocolRules.add(new ProtocolRule((Element) pcolIterator.next()));
        }
    }

    /**
	 * Get interface rules list
	 * @return  interfaceRules HashSet containing all interface rules
	 */
    public ArrayList<InterfaceRule> getInterfaceRules() {
        return interfaceRules;
    }

    /**
	 * Get protocol rules list
	 * @return  protocolRules HashSet containing all protocol rules
	 */
    public ArrayList<ProtocolRule> getProtocolRules() {
        return protocolRules;
    }

    /**
	 * Get requestor namepsace
	 * @return  requestorNameSpace requestor namespace string
	 */
    public String getRequestorNameSpace() {
        return requestorNameSpace;
    }

    /**
	 * Get service namespace
	 * @return  serviceNameSpace service namespace string
	 */
    public String getServiceNameSpace() {
        return serviceNameSpace;
    }

    /**
	 * Get final service endpoint
	 * @return  finalEndpoint final service endpoint
	 */
    public String getFinalEndpoint() {
        return finalEndpoint;
    }

    /**
	 * Get start service endpoint
	 * @return  startEndpoint start service endpoint
	 */
    public String getStartEndpoint() {
        return startEndpoint;
    }

    public static MappingParser getinstance() {
        if (MappingParser.instance == null) {
            instance = new MappingParser();
        }
        return instance;
    }
}
