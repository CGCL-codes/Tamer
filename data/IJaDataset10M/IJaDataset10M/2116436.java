package net.sf.copernicus.server.m2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.sf.copernicus.server.m2.IndicationManager.ItemType;
import net.sf.copernicus.server.m2.discovery.Agent;
import net.sf.copernicus.server.m2.http.HttpRequest;
import net.sf.copernicus.server.m2.http.HttpResponse;
import net.sf.copernicus.server.m2.wbem.WbemComposer;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

interface TimerAdapterListener {

    public void onTimer();
}

class TimerAdapter extends TimerTask {

    private TimerAdapterListener listener;

    public TimerAdapter(TimerAdapterListener listener) {
        this.listener = listener;
    }

    public void run() {
        listener.onTimer();
    }
}

class CimQualifierDefinition {

    public String name;

    public String value;

    public CimQualifierDefinition(String name) {
        this.name = name;
    }

    public CimQualifierDefinition(String name, String value) {
        this(name);
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CimQualifierDefinition other = (CimQualifierDefinition) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }
}

class CimPropertyDefinition {

    public String name;

    public ArrayList<CimQualifierDefinition> qualifiers = new ArrayList<CimQualifierDefinition>();

    public CimPropertyDefinition(String name) {
        this.name = name;
    }

    public boolean hasQualifier(CimQualifierDefinition qualifier) {
        for (CimQualifierDefinition qual : qualifiers) {
            if (qual.equals(qualifier)) return true;
        }
        return false;
    }
}

class CimClassDefinition {

    public String name;

    public ArrayList<CimPropertyDefinition> properties = new ArrayList<CimPropertyDefinition>();

    public CimClassDefinition(String name) {
        this.name = name;
    }

    public CimPropertyDefinition findPropertyWithQualifier(CimQualifierDefinition qualifier) {
        for (CimPropertyDefinition prop : properties) {
            if (prop.hasQualifier(qualifier)) return prop;
        }
        return null;
    }
}

class CimXmlClassParserHandler extends DefaultHandler {

    private static final String[] interestingQualifiers = { "Propagated" };

    private CimClassDefinition cimClassdef;

    private CimPropertyDefinition curCimProperty;

    private CimQualifierDefinition curCimQualifier;

    private boolean qualifierValueFlag = false;

    public CimClassDefinition getClassdef() {
        return cimClassdef;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        if (name.equalsIgnoreCase("CLASS")) {
            for (int i = 0; i < attributes.getLength(); i++) {
                if (attributes.getLocalName(i).equalsIgnoreCase("NAME")) {
                    cimClassdef = new CimClassDefinition(attributes.getValue(i));
                    break;
                }
            }
        } else if (name.equalsIgnoreCase("PROPERTY") && cimClassdef != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                if (attributes.getLocalName(i).equalsIgnoreCase("NAME")) {
                    curCimProperty = new CimPropertyDefinition(attributes.getValue(i));
                    cimClassdef.properties.add(curCimProperty);
                    break;
                }
            }
        } else if (name.equalsIgnoreCase("QUALIFIER") && curCimProperty != null) {
            String qname = attributes.getValue("", "NAME");
            if (qname != null) {
                if (!isInterestingQualifier(qname)) {
                    curCimQualifier = null;
                    return;
                }
                curCimQualifier = new CimQualifierDefinition(qname);
                curCimProperty.qualifiers.add(curCimQualifier);
            }
        } else if (name.equalsIgnoreCase("VALUE") && curCimQualifier != null) {
            qualifierValueFlag = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (qualifierValueFlag) {
            curCimQualifier.value = new String(ch, start, length);
            qualifierValueFlag = false;
            curCimQualifier = null;
        }
    }

    private static boolean isInterestingQualifier(String name) {
        for (String s : interestingQualifiers) {
            if (s.equalsIgnoreCase(name)) return true;
        }
        return false;
    }
}

public class RequestLogic extends DefaultHandler implements TimerAdapterListener {

    private enum RequestType {

        GET_CLASS, ENUM_INSTANCES, GET_INSTANCE, INVOKE_METHOD, ASSOCIATOR_NAMES, ENUM_INSTANCE_NAMES, MODIFY_INSTANCE, CREATE_INSTANCE, DELETE_INSTANCE, INDICATION_CREATE_FILTER, INDICATION_DELETE_FILTER, INDICATION_CREATE_HANDLER, INDICATION_DELETE_HANDLER, INDICATION_CREATE_SUBSCRIPTION, INDICATION_DELETE_SUBSCRIPTION;

        public static boolean isIndicationSubscription(RequestType type) {
            return type == INDICATION_CREATE_SUBSCRIPTION || type == INDICATION_DELETE_SUBSCRIPTION;
        }

        public static boolean isDeleteIndicationItem(RequestType type) {
            return type == INDICATION_DELETE_FILTER || type == INDICATION_DELETE_HANDLER || type == INDICATION_DELETE_SUBSCRIPTION;
        }
    }

    private enum StrategyType {

        ONE_AGENT, ALL_AGENTS
    }

    private static Logger log = Logger.getLogger(RequestLogic.class);

    private static SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

    private static DocumentBuilderFactory domParserFactory = DocumentBuilderFactory.newInstance();

    private static TransformerFactory transformerFactory = TransformerFactory.newInstance();

    private RequestLogicHandler handler;

    private SAXParser saxParser;

    private DocumentBuilder domParser;

    private RequestType type;

    private StrategyType strategy;

    private String targetHost;

    private boolean targetHostValueFlag = false;

    private boolean systemNameFlag = false;

    private String classname;

    private ArrayList<String> parseErrors = new ArrayList<String>();

    private Document compositeResponse;

    private HashSet<String> expectedAgents = new HashSet<String>();

    private HashSet<String> repliedAgents = new HashSet<String>();

    private WbemComposer wbemComposer = new WbemComposer();

    private Timer timeoutTimer = new Timer(true);

    private boolean awaitingFlag = false;

    private int numPeriodPassed = 0;

    private static final int TIMER_PERIOD_MSEC = 10000;

    private static ConcurrentHashMap<String, CimClassDefinition> cimClasses = new ConcurrentHashMap<String, CimClassDefinition>();

    private Object cimGetClassEvent = new Object();

    private volatile boolean cimGetClassRequestFlag = false;

    private CimClassDefinition newCimClassdef;

    private String systemNamePropName;

    private HttpResponse errorResponse;

    private String indicationItemName;

    private static IndicationManager indicationManager = new IndicationManager();

    public RequestLogic(RequestLogicHandler handler) throws Exception {
        this.handler = handler;
        saxParser = saxParserFactory.newSAXParser();
        domParser = domParserFactory.newDocumentBuilder();
        timeoutTimer.schedule(new TimerAdapter(this), TIMER_PERIOD_MSEC, TIMER_PERIOD_MSEC);
    }

    public synchronized void requestFromClient(HttpRequest req) throws SAXException, IOException {
        parseErrors.clear();
        type = null;
        strategy = null;
        targetHost = null;
        targetHostValueFlag = false;
        systemNameFlag = false;
        classname = null;
        compositeResponse = null;
        repliedAgents.clear();
        newCimClassdef = null;
        cimGetClassRequestFlag = false;
        systemNamePropName = null;
        errorResponse = null;
        indicationItemName = null;
        log.debug("requestFromClient: parsing");
        saxParser.parse(new ByteArrayInputStream(req.getBody().getBytes()), this, null);
        if (parseErrors.size() > 0) {
            log.error("requestFromClient: " + new Integer(parseErrors.size() + " errors"));
            handler.replyErrorToClient(parseErrors.toString());
            return;
        }
        log.debug("requestFromClient: request type = " + type);
        if (type != null) {
            if (type == RequestType.ENUM_INSTANCES || type == RequestType.ENUM_INSTANCE_NAMES) {
                strategy = StrategyType.ALL_AGENTS;
                getExpectedAgentsList();
                handler.sendToAllAgents(req.toString());
            } else if (type == RequestType.INDICATION_CREATE_FILTER || type == RequestType.INDICATION_CREATE_HANDLER) {
                indicationItemName = getIndicationItemNameFromInstance(req.getBody());
                if (indicationItemName == null) {
                    handler.replyErrorToClient("Couldn't find Name property inside the indication item.");
                    return;
                }
                strategy = StrategyType.ALL_AGENTS;
                getExpectedAgentsList();
                handler.sendToAllAgents(req.toString());
            } else if (RequestType.isIndicationSubscription(type)) {
                String filterName = null;
                if (type == RequestType.INDICATION_CREATE_SUBSCRIPTION) filterName = getReferenceNameFromIndicationSubscriptionInstance(req.getBody(), "Filter"); else if (type == RequestType.INDICATION_DELETE_SUBSCRIPTION) filterName = getReferenceNameFromIndicationSubscriptionPath(req.getBody(), "Filter");
                if (filterName == null) {
                    handler.replyErrorToClient("Couldn't find Name keybinding for CIM_IndicationFilter inside CIM_IndicationSubscription instance.");
                    return;
                }
                String handlerName = null;
                if (type == RequestType.INDICATION_CREATE_SUBSCRIPTION) handlerName = getReferenceNameFromIndicationSubscriptionInstance(req.getBody(), "Handler"); else if (type == RequestType.INDICATION_DELETE_SUBSCRIPTION) handlerName = getReferenceNameFromIndicationSubscriptionPath(req.getBody(), "Handler");
                if (handlerName == null) {
                    handler.replyErrorToClient("Couldn't find Name keybinding for CIM_ListenerDestinationCIMXML inside CIM_IndicationSubscription instance.");
                    return;
                }
                ConcurrentHashMap<String, String> filterHosts = indicationManager.getHosts(ItemType.FILTER, filterName);
                ConcurrentHashMap<String, String> handlerHosts = indicationManager.getHosts(ItemType.HANDLER, handlerName);
                strategy = StrategyType.ALL_AGENTS;
                expectedAgents.clear();
                for (String host : filterHosts.keySet()) {
                    if (!handlerHosts.containsKey(host)) {
                        log.warn("Filter host " + host + " not found in handler hosts map.");
                        continue;
                    }
                    expectedAgents.add(host);
                    if (type == RequestType.INDICATION_CREATE_SUBSCRIPTION) {
                        handler.sendToAgent(wbemComposer.createIndicationSubscriptionRequest(host, filterHosts.get(host), handlerHosts.get(host), req), host);
                    } else if (type == RequestType.INDICATION_DELETE_SUBSCRIPTION) {
                        handler.sendToAgent(wbemComposer.deleteIndicationSubscriptionRequest(host, filterHosts.get(host), handlerHosts.get(host), req), host);
                    }
                }
                if (type == RequestType.INDICATION_DELETE_SUBSCRIPTION) handler.sendToClient(wbemComposer.deleteIndicationItemResponse());
            } else if (type == RequestType.INDICATION_DELETE_FILTER || type == RequestType.INDICATION_DELETE_HANDLER) {
                indicationItemName = getIndciationItemNameFromPath(req.getBody());
                if (indicationItemName == null) {
                    handler.replyErrorToClient("Couldn't find Name property inside the indication item object path.");
                    return;
                }
                strategy = StrategyType.ALL_AGENTS;
                ConcurrentHashMap<String, String> hosts = indicationManager.getHosts(toIndicationItemType(type), indicationItemName);
                for (String host : hosts.keySet()) {
                    handler.sendToAgent(wbemComposer.deleteInstanceRequest(hosts.get(host), host, req), host);
                }
                indicationManager.deleteHosts(toIndicationItemType(type), indicationItemName);
                handler.sendToClient(wbemComposer.deleteIndicationItemResponse());
            } else if (type == RequestType.GET_CLASS) {
                strategy = StrategyType.ONE_AGENT;
                handler.sendToAgent(req.toString(), handler.getPrimaryAgent());
            } else {
                strategy = StrategyType.ONE_AGENT;
                String host = (targetHost != null ? targetHost : handler.getPrimaryAgent());
                handler.sendToAgent(req.toString(), host);
            }
            awaitingFlag = true;
        } else {
            handler.replyErrorToClient("Unable to define request type.");
        }
    }

    public void responseFromAgent(HttpResponse resp, String host) {
        try {
            log.debug("responsseFromAgent: host = " + host + "; result = " + resp.getResult());
            if (cimGetClassRequestFlag) {
                log.debug("responseFromAgent: response to getClass request");
                cimGetClassRequestFlag = false;
                if (resp.getResult().equalsIgnoreCase("200 OK")) {
                    newCimClassdef = parseToCimClassdef(resp.getBody());
                }
                synchronized (cimGetClassEvent) {
                    cimGetClassEvent.notify();
                }
                return;
            }
            if (!resp.getResult().equalsIgnoreCase("200 OK") && !RequestType.isDeleteIndicationItem(type)) {
                handler.sendToClient(resp.toString());
                return;
            }
            synchronized (this) {
                if (strategy == StrategyType.ONE_AGENT) {
                    awaitingFlag = false;
                    handler.sendToClient(resp.toString());
                } else if (strategy == StrategyType.ALL_AGENTS) {
                    if (RequestType.isDeleteIndicationItem(type)) {
                        log.debug("Response to " + type + " request, ignoring");
                        repliedAgents.add(host);
                        return;
                    }
                    Document document = domParser.parse(new ByteArrayInputStream(resp.getBody().getBytes()));
                    Node retval = document.getElementsByTagName("IRETURNVALUE").item(0);
                    if (retval != null) {
                        if (type == RequestType.INDICATION_CREATE_FILTER || type == RequestType.INDICATION_CREATE_HANDLER) {
                            log.debug("Response to " + type + " request, recording INSTANCENAME");
                            NodeList instancenames = document.getElementsByTagName("INSTANCENAME");
                            if (instancenames.getLength() == 1) {
                                indicationManager.addHost(toIndicationItemType(type), indicationItemName, host, toString(instancenames.item(0), false));
                            } else {
                                log.error("No INSTANCENAME element in response to " + type + " request, Agent " + host);
                            }
                        }
                    }
                    if (compositeResponse != null) {
                        if (retval != null) {
                            errorResponse = null;
                            NodeList instances = document.getElementsByTagName("VALUE.NAMEDINSTANCE");
                            Node target = compositeResponse.getElementsByTagName("IRETURNVALUE").item(0);
                            if (target != null) {
                                for (int i = 0; i < instances.getLength(); i++) {
                                    Node node = compositeResponse.importNode(instances.item(i), true);
                                    target.appendChild(node);
                                }
                            } else compositeResponse = document;
                        } else {
                            log.error("IRETURNVALUE element not found in the Agent " + host + " response.");
                            errorResponse = resp;
                        }
                    } else {
                        if (retval == null) {
                            log.error("IRETURNVALUE element not found in the Agent " + host + " response.");
                            errorResponse = resp;
                        } else {
                            compositeResponse = document;
                        }
                    }
                    repliedAgents.add(host);
                    if (repliedAgents.containsAll(expectedAgents)) {
                        awaitingFlag = false;
                        numPeriodPassed = 0;
                        if (compositeResponse != null) {
                            handler.sendToClient(new HttpResponse(toString(compositeResponse, true)).toString());
                        } else {
                            if (errorResponse != null) handler.sendToClient(errorResponse.toString()); else handler.sendToClient(resp.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            handler.replyErrorToClient(e.getMessage());
        }
    }

    private String getIndicationItemNameFromInstance(String xml) throws SAXException, IOException {
        Document doc = domParser.parse(new ByteArrayInputStream(xml.getBytes()));
        NodeList properties = doc.getElementsByTagName("PROPERTY");
        for (int i = 0; i < properties.getLength(); i++) {
            Node property = properties.item(i);
            Node nameAttribute = property.getAttributes().getNamedItem("NAME");
            if (nameAttribute != null && nameAttribute.getTextContent().equalsIgnoreCase("Name")) {
                NodeList propertyChildren = property.getChildNodes();
                for (int j = 0; j < propertyChildren.getLength(); j++) {
                    Node child = propertyChildren.item(j);
                    if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equalsIgnoreCase("VALUE")) {
                        return child.getTextContent();
                    }
                }
            }
        }
        return null;
    }

    private String getIndciationItemNameFromPath(String xml) throws SAXException, IOException {
        Document doc = domParser.parse(new ByteArrayInputStream(xml.getBytes()));
        NodeList keybindings = doc.getElementsByTagName("KEYBINDING");
        for (int i = 0; i < keybindings.getLength(); i++) {
            Node keybinding = keybindings.item(i);
            Node nameAttribute = keybinding.getAttributes().getNamedItem("NAME");
            if (nameAttribute != null && nameAttribute.getTextContent().equalsIgnoreCase("Name")) {
                NodeList keybindingChildren = keybinding.getChildNodes();
                for (int j = 0; j < keybindingChildren.getLength(); j++) {
                    Node child = keybindingChildren.item(j);
                    if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equalsIgnoreCase("KEYVALUE")) {
                        return child.getTextContent();
                    }
                }
            }
        }
        return null;
    }

    private String getReferenceNameFromIndicationSubscriptionInstance(String xml, String propertyName) throws SAXException, IOException {
        Document doc = domParser.parse(new ByteArrayInputStream(xml.getBytes()));
        NodeList proprefs = doc.getElementsByTagName("PROPERTY.REFERENCE");
        for (int i = 0; i < proprefs.getLength(); i++) {
            Node propref = proprefs.item(i);
            Node nameAttribute = propref.getAttributes().getNamedItem("NAME");
            if (nameAttribute != null && nameAttribute.getTextContent().equalsIgnoreCase(propertyName)) {
                Node instancename = findNode(propref, "INSTANCENAME");
                if (instancename != null) {
                    NodeList instancenameChildren = instancename.getChildNodes();
                    for (int j = 0; j < instancenameChildren.getLength(); j++) {
                        Node instancenameChild = instancenameChildren.item(j);
                        if (instancenameChild.getNodeType() == Node.ELEMENT_NODE && instancenameChild.getNodeName().equalsIgnoreCase("KEYBINDING")) {
                            Node keybindingName = instancenameChild.getAttributes().getNamedItem("NAME");
                            if (keybindingName != null && keybindingName.getTextContent().equalsIgnoreCase("Name")) {
                                NodeList keybindingChildren = instancenameChild.getChildNodes();
                                for (int k = 0; k < keybindingChildren.getLength(); k++) {
                                    Node childNode = keybindingChildren.item(k);
                                    if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equalsIgnoreCase("KEYVALUE")) {
                                        return childNode.getTextContent();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private String getReferenceNameFromIndicationSubscriptionPath(String xml, String keybindingName) throws SAXException, IOException {
        Document doc = domParser.parse(new ByteArrayInputStream(xml.getBytes()));
        NodeList instancenames = doc.getElementsByTagName("INSTANCENAME");
        for (int i = 0; i < instancenames.getLength(); i++) {
            Node instancename = instancenames.item(i);
            Node classnameAttribute = instancename.getAttributes().getNamedItem("CLASSNAME");
            if (classnameAttribute != null && classnameAttribute.getTextContent().equalsIgnoreCase("CIM_IndicationSubscription")) {
                NodeList subscriptionKeybindings = instancename.getChildNodes();
                for (int j = 0; j < subscriptionKeybindings.getLength(); j++) {
                    Node subscriptionKeybinding = subscriptionKeybindings.item(j);
                    Node nameAttribute = subscriptionKeybinding.getAttributes().getNamedItem("NAME");
                    if (nameAttribute != null && nameAttribute.getTextContent().equalsIgnoreCase(keybindingName)) {
                        Node itemInstancename = findNode(subscriptionKeybinding, "INSTANCENAME");
                        NodeList itemKeybindings = itemInstancename.getChildNodes();
                        for (int k = 0; k < itemKeybindings.getLength(); k++) {
                            Node itemKeybinding = itemKeybindings.item(k);
                            nameAttribute = itemKeybinding.getAttributes().getNamedItem("NAME");
                            if (nameAttribute != null && nameAttribute.getTextContent().equalsIgnoreCase("Name")) {
                                Node keyvalue = findNode(itemKeybinding, "KEYVALUE");
                                if (keyvalue != null) return keyvalue.getTextContent();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private Node findNode(Node parent, String childName) {
        if (parent.getNodeName().equalsIgnoreCase(childName)) return parent;
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = findNode(children.item(i), childName);
            if (child != null) return child;
        }
        return null;
    }

    private CimClassDefinition parseToCimClassdef(String xml) throws SAXException, IOException, ParserConfigurationException {
        SAXParser parser = saxParserFactory.newSAXParser();
        CimXmlClassParserHandler handler = new CimXmlClassParserHandler();
        parser.parse(new ByteArrayInputStream(xml.getBytes()), handler);
        return handler.getClassdef();
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        parseErrors.add("Line " + e.getLineNumber() + ", col " + e.getColumnNumber() + ": " + e.getMessage());
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        error(e);
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        targetHostValueFlag = false;
        try {
            if (name.equalsIgnoreCase("IMETHODCALL")) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getLocalName(i).equalsIgnoreCase("NAME")) {
                        type = toRequestType(attributes.getValue(i));
                        break;
                    }
                }
            } else if (name.equalsIgnoreCase("METHODCALL")) {
                type = RequestType.INVOKE_METHOD;
            } else if (type == RequestType.GET_INSTANCE || type == RequestType.CREATE_INSTANCE || type == RequestType.MODIFY_INSTANCE || type == RequestType.DELETE_INSTANCE || type == RequestType.INVOKE_METHOD || type == RequestType.ASSOCIATOR_NAMES) {
                if (name.equalsIgnoreCase("INSTANCENAME") || name.equalsIgnoreCase("INSTANCE")) {
                    for (int i = 0; i < attributes.getLength(); i++) {
                        if (attributes.getLocalName(i).equalsIgnoreCase("CLASSNAME")) {
                            classname = attributes.getValue(i);
                            break;
                        }
                    }
                    if (classname == null) {
                        parseErrors.add("No classname found for the instance.");
                        return;
                    }
                    if (classname.equals("CIM_IndicationFilter")) {
                        if (type == RequestType.CREATE_INSTANCE) type = RequestType.INDICATION_CREATE_FILTER; else if (type == RequestType.DELETE_INSTANCE) type = RequestType.INDICATION_DELETE_FILTER;
                        return;
                    }
                    if (classname.equals("CIM_IndicationSubscription")) {
                        if (type == RequestType.CREATE_INSTANCE) type = RequestType.INDICATION_CREATE_SUBSCRIPTION; else if (type == RequestType.DELETE_INSTANCE) type = RequestType.INDICATION_DELETE_SUBSCRIPTION;
                        return;
                    }
                    if (classname.equals("CIM_ListenerDestinationCIMXML")) {
                        if (type == RequestType.CREATE_INSTANCE) type = RequestType.INDICATION_CREATE_HANDLER; else if (type == RequestType.DELETE_INSTANCE) type = RequestType.INDICATION_DELETE_HANDLER;
                        return;
                    }
                    if (classname.equals("CIM_System") || classname.equals("CIM_ComputerSystem") || classname.equals("PG_ComputerSystem")) {
                        systemNamePropName = "Name";
                    } else {
                        systemNamePropName = defineSystemNamePropName();
                    }
                    if (systemNamePropName == null) {
                        log.warn("Cannot find property with qualifier Propogated(\"CIM_System.Name\"). Unable to define the target computer system.");
                        return;
                    }
                } else if (systemNamePropName != null && name.equalsIgnoreCase("KEYBINDING")) {
                    for (int i = 0; i < attributes.getLength(); i++) {
                        if (attributes.getLocalName(i).equalsIgnoreCase("NAME")) {
                            String keyvalue = attributes.getValue(i);
                            if (keyvalue.equalsIgnoreCase(systemNamePropName)) {
                                systemNameFlag = true;
                                return;
                            }
                        }
                    }
                } else if (systemNameFlag && name.equalsIgnoreCase("KEYVALUE")) {
                    systemNameFlag = false;
                    targetHostValueFlag = true;
                }
            }
        } catch (InterruptedException e) {
            return;
        }
    }

    private String defineSystemNamePropName() throws InterruptedException {
        CimClassDefinition cimClassdef = cimClasses.get(classname);
        if (cimClassdef == null) {
            cimGetClassRequestFlag = true;
            handler.sendToAgent(wbemComposer.getClassRequest(classname, handler.getPrimaryAgent()), handler.getPrimaryAgent());
            synchronized (cimGetClassEvent) {
                cimGetClassEvent.wait(TIMER_PERIOD_MSEC);
            }
            if (newCimClassdef != null) {
                cimClassdef = newCimClassdef;
                cimClasses.put(classname, newCimClassdef);
                newCimClassdef = null;
            } else {
                return null;
            }
        }
        CimPropertyDefinition systemNameProp = cimClassdef.findPropertyWithQualifier(new CimQualifierDefinition("Propagated", "CIM_System.Name"));
        return (systemNameProp != null ? systemNameProp.name : null);
    }

    @Override
    public void characters(char[] c, int start, int length) throws SAXException {
        if (targetHostValueFlag) {
            if (targetHost == null) {
                targetHost = new String(c, start, length);
            } else {
                targetHost += new String(c, start, length);
            }
        }
    }

    private RequestType toRequestType(String stype) {
        if (stype.equalsIgnoreCase("GetClass")) return RequestType.GET_CLASS; else if (stype.equalsIgnoreCase("GetInstance")) return RequestType.GET_INSTANCE; else if (stype.equalsIgnoreCase("CreateInstance")) return RequestType.CREATE_INSTANCE; else if (stype.equalsIgnoreCase("ModifyInstance")) return RequestType.MODIFY_INSTANCE; else if (stype.equalsIgnoreCase("DeleteInstance")) return RequestType.DELETE_INSTANCE; else if (stype.equalsIgnoreCase("EnumerateInstances")) return RequestType.ENUM_INSTANCES; else if (stype.equalsIgnoreCase("AssociatorNames")) return RequestType.ASSOCIATOR_NAMES; else if (stype.equalsIgnoreCase("EnumerateInstanceNames")) {
            return RequestType.ENUM_INSTANCE_NAMES;
        } else return null;
    }

    private ItemType toIndicationItemType(RequestType type) {
        if (type == RequestType.INDICATION_CREATE_HANDLER || type == RequestType.INDICATION_DELETE_HANDLER) return ItemType.HANDLER;
        if (type == RequestType.INDICATION_CREATE_FILTER || type == RequestType.INDICATION_DELETE_FILTER) return ItemType.FILTER;
        assert (false);
        return null;
    }

    private String toString(Node node, boolean xmlHeader) throws TransformerException {
        Source source = new DOMSource(node);
        StringWriter stringWriter = new StringWriter();
        Result result = new StreamResult(stringWriter);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, (xmlHeader ? "no" : "yes"));
        transformer.transform(source, result);
        return stringWriter.getBuffer().toString();
    }

    private void getExpectedAgentsList() {
        Iterator<Agent> agents = handler.getAgentList();
        expectedAgents.clear();
        while (agents.hasNext()) {
            expectedAgents.add(agents.next().hostname);
        }
    }

    @Override
    public synchronized void onTimer() {
        if (!awaitingFlag) return;
        if (++numPeriodPassed >= 2) {
            log.debug("Timer expired");
            awaitingFlag = false;
            numPeriodPassed = 0;
            if (strategy == StrategyType.ALL_AGENTS) {
                if (strategy == StrategyType.ALL_AGENTS && compositeResponse != null) {
                    log.debug("Sending the current response to Client");
                    try {
                        handler.sendToClient(new HttpResponse(toString(compositeResponse, true)).toString());
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                    log.debug("Kicking all non-replied Agents");
                    for (String agent : expectedAgents) {
                        if (!repliedAgents.contains(agent)) {
                            handler.kickAgent(agent);
                        }
                    }
                    repliedAgents.clear();
                } else {
                    handler.replyErrorToClient("None of the Agents replied in time.");
                }
            } else if (strategy == StrategyType.ONE_AGENT) {
                String host = (targetHost != null ? targetHost : handler.getPrimaryAgent());
                handler.replyErrorToClient("Agent " + host + " didn't reply in time.");
                handler.kickAgent(host);
            }
        }
    }
}
