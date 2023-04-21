package com.triplea.rolap.plugins.acl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.triplea.rolap.plugins.IACL;
import com.triplea.rolap.plugins.IAuthentication;
import com.triplea.rolap.plugins.IServer;
import com.triplea.rolap.plugins.JPlugin;
import com.triplea.rolap.plugins.PluginProvider;
import com.triplea.rolap.plugins.Tuple;

public class Default implements IACL {

    private final String _configFile = "plugins/PluginACLDefault.xml";

    private static Logger _logger = Logger.getLogger(Default.class.getName());

    public static final String XML_RULES_NODE = "rules";

    public static final String XML_DATABASE_NODE = "database";

    public static final String XML_CUBE_NODE = "cube";

    public static final String XML_READRULES_NODE = "readRules";

    public static final String XML_WRITERULES_NODE = "writeRules";

    public static final String XML_NAME_ATTRIBUTE = "name";

    private static final String HASH_READRULES = "readRules";

    private static final String HASH_WRITERULES = "writeRules";

    private Hashtable<String, Hashtable<String, Hashtable<String, ArrayList<Rule>>>> _databaseRules = null;

    public Default() {
        this._databaseRules = new Hashtable<String, Hashtable<String, Hashtable<String, ArrayList<Rule>>>>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(this._configFile);
            NodeList rulesNodes = doc.getElementsByTagName(XML_RULES_NODE);
            if (0 == rulesNodes.getLength()) {
                String message = "Can't find <rules> node at " + doc.getDocumentURI();
                _logger.error(message);
                throw new Exception(message);
            }
            NodeList nodes = rulesNodes.item(0).getChildNodes();
            for (int firstLevel = 0; firstLevel < nodes.getLength(); firstLevel++) {
                Node node = nodes.item(firstLevel);
                if (Node.ELEMENT_NODE != node.getNodeType()) continue;
                if (node.getNodeName().equalsIgnoreCase(XML_DATABASE_NODE)) {
                    String databaseName = ((Element) node).getAttribute(Default.XML_NAME_ATTRIBUTE);
                    Hashtable<String, Hashtable<String, ArrayList<Rule>>> cubeRules = new Hashtable<String, Hashtable<String, ArrayList<Rule>>>();
                    NodeList subNodes = node.getChildNodes();
                    for (int secondLevel = 0; secondLevel < subNodes.getLength(); secondLevel++) {
                        Node subNode = subNodes.item(secondLevel);
                        if (Node.ELEMENT_NODE != subNode.getNodeType()) continue;
                        if (subNode.getNodeName().equalsIgnoreCase(XML_CUBE_NODE)) {
                            String cubeName = ((Element) subNode).getAttribute(Default.XML_NAME_ATTRIBUTE);
                            Hashtable<String, ArrayList<Rule>> rules = this._parseCubeSectionOfConfigFile(subNode);
                            cubeRules.put(cubeName, rules);
                        }
                    }
                    this._databaseRules.put(databaseName, cubeRules);
                }
            }
        } catch (Exception ex) {
            _logger.error("Error parsing config file", ex);
        }
    }

    private Hashtable<String, ArrayList<Rule>> _parseCubeSectionOfConfigFile(Node node) {
        Hashtable<String, ArrayList<Rule>> rules = new Hashtable<String, ArrayList<Rule>>();
        ArrayList<Rule> readRules = new ArrayList<Rule>();
        ArrayList<Rule> writeRules = new ArrayList<Rule>();
        NodeList subNodes = node.getChildNodes();
        for (int level = 0; level < subNodes.getLength(); level++) {
            Node subNode = subNodes.item(level);
            if (Node.ELEMENT_NODE != subNode.getNodeType()) continue;
            if (subNode.getNodeName().equalsIgnoreCase(XML_READRULES_NODE)) {
                readRules = this._parseRulesSectionOfConfigFile(subNode);
            } else if (subNode.getNodeName().equalsIgnoreCase(XML_WRITERULES_NODE)) {
                writeRules = this._parseRulesSectionOfConfigFile(subNode);
            }
        }
        rules.put(HASH_READRULES, readRules);
        rules.put(HASH_WRITERULES, writeRules);
        return rules;
    }

    private ArrayList<Rule> _parseRulesSectionOfConfigFile(Node node) {
        ArrayList<Rule> rules = new ArrayList<Rule>();
        NodeList subNodes = node.getChildNodes();
        for (int level = 0; level < subNodes.getLength(); level++) {
            Node subNode = subNodes.item(level);
            if (Node.ELEMENT_NODE != subNode.getNodeType()) continue;
            if (subNode.getNodeName().equalsIgnoreCase(Rule.XML_RULE_NODE)) {
                Rule rule = this._parseRuleSectionOfConfigFile(subNode);
                if (null != rule) {
                    rules.add(rule);
                }
            }
        }
        return rules;
    }

    private Rule _parseRuleSectionOfConfigFile(Node ruleNode) {
        String type = ((Element) ruleNode).getAttribute(Rule.XML_TYPE_ATTRIBUTE);
        Rule rule = null;
        if (type.equalsIgnoreCase(Rule.XML_ALLOW_ATTRVALUE)) {
            rule = Rule.createAllowRule();
        } else if (type.equalsIgnoreCase(Rule.XML_DENY_ATTRVALUE)) {
            rule = Rule.createDenyRule();
        } else {
            _logger.warn("Incorrect rule.type: " + type);
            return null;
        }
        rule.setDescription(((Element) ruleNode).getAttribute(Rule.XML_DESCRIPTION_ATTRIBUTE));
        String ruleName = ((Element) ruleNode).getAttribute(Rule.XML_NAME_ATTRIBUTE);
        if (ruleName.equals("")) {
            ruleName = "unknown";
        }
        rule.setName(ruleName);
        ArrayList<String> users = new ArrayList<String>();
        ArrayList<String> groups = new ArrayList<String>();
        ArrayList<RuleDimension> dimensions = new ArrayList<RuleDimension>();
        NodeList nodes = ruleNode.getChildNodes();
        for (int firstLevel = 0; firstLevel < nodes.getLength(); firstLevel++) {
            Node node = nodes.item(firstLevel);
            if (Node.ELEMENT_NODE != node.getNodeType()) continue;
            if (node.getNodeName().equalsIgnoreCase(Rule.XML_OBJECTS_NODE)) {
                NodeList subNodes = node.getChildNodes();
                for (int secondLevel = 0; secondLevel < subNodes.getLength(); secondLevel++) {
                    Node subNode = subNodes.item(secondLevel);
                    if (Node.ELEMENT_NODE != subNode.getNodeType()) continue;
                    if (subNode.getNodeName().equalsIgnoreCase(Rule.XML_USER_NODE)) {
                        String userName = ((Element) subNode).getAttribute(Rule.XML_NAME_ATTRIBUTE);
                        users.add(userName);
                    } else if (subNode.getNodeName().equalsIgnoreCase(Rule.XML_GROUP_NODE)) {
                        String groupName = ((Element) subNode).getAttribute(Rule.XML_NAME_ATTRIBUTE);
                        groups.add(groupName);
                    }
                }
            } else if (node.getNodeName().equalsIgnoreCase(Rule.XML_SUBJECT_NODE)) {
                NodeList subNodes = node.getChildNodes();
                for (int secondLevel = 0; secondLevel < subNodes.getLength(); secondLevel++) {
                    Node subNode = subNodes.item(secondLevel);
                    if (Node.ELEMENT_NODE != subNode.getNodeType()) continue;
                    if (subNode.getNodeName().equalsIgnoreCase(RuleDimension.XML_DIMENSION_NODE)) {
                        RuleDimension dimension = new RuleDimension(((Element) subNode).getAttribute(Rule.XML_NAME_ATTRIBUTE));
                        ArrayList<String> elements = new ArrayList<String>();
                        NodeList elemNodes = subNode.getChildNodes();
                        for (int thirdLevel = 0; thirdLevel < elemNodes.getLength(); thirdLevel++) {
                            Node elemNode = elemNodes.item(thirdLevel);
                            if (Node.ELEMENT_NODE != elemNode.getNodeType()) continue;
                            if (elemNode.getNodeName().equalsIgnoreCase(RuleDimension.XML_ELEMENT_NODE)) {
                                String elementName = ((Element) elemNode).getAttribute(Rule.XML_NAME_ATTRIBUTE);
                                elements.add(elementName);
                            }
                        }
                        dimension.setElements(elements);
                        dimensions.add(dimension);
                    }
                }
            }
        }
        rule.setUsers(users);
        rule.setGroups(groups);
        rule.setDimensions(dimensions);
        return rule;
    }

    public boolean checkRead(String username, Tuple tuple) {
        String database = tuple.getDatabaseName();
        String cube = tuple.getCubename();
        IAuthentication auth = PluginProvider.getInstance().getAuthenticationInterface();
        if (this._databaseRules.containsKey(database)) {
            Hashtable<String, Hashtable<String, ArrayList<Rule>>> dbRules = this._databaseRules.get(database);
            if (dbRules.containsKey(cube)) {
                Hashtable<String, ArrayList<Rule>> cubeRules = dbRules.get(cube);
                ArrayList<Rule> readRules = cubeRules.get(HASH_READRULES);
                if (readRules.size() > 0) {
                    for (int i = 0; i < readRules.size(); i++) {
                        Rule rule = readRules.get(i);
                        if (rule.isAllowType()) {
                            ArrayList<RuleDimension> ruleDimensions = rule.getDimensions();
                            for (int dimIndex = 0; dimIndex < ruleDimensions.size(); dimIndex++) {
                                String dimName = ruleDimensions.get(dimIndex).getName();
                                int tupleDimIndex = tuple.getDimension().indexOf(dimName);
                                if (-1 == tupleDimIndex) {
                                    _logger.debug("Cell path doesn't contain obligitary dimension: " + dimName);
                                    return false;
                                }
                                if (!ruleDimensions.get(dimIndex).affectWholeDimension()) {
                                    String tupleDimElement = tuple.getPath()[tupleDimIndex];
                                    ArrayList<String> dimElements = ruleDimensions.get(dimIndex).getElements();
                                    if (false == dimElements.contains(tupleDimElement)) {
                                        _logger.debug(dimName + "(" + tupleDimElement + ") isn't allowed for this path");
                                        return false;
                                    }
                                }
                            }
                            boolean isAccessible = false;
                            if (rule.getUsers().contains(username)) {
                                isAccessible = true;
                            } else {
                                ArrayList<String> groups = rule.getGroups();
                                for (int index = 0; index < groups.size(); index++) {
                                    if (auth.belongsToGroup(username, groups.get(index))) {
                                        isAccessible = true;
                                        break;
                                    }
                                }
                            }
                            if (false == isAccessible) {
                                _logger.debug(username + " isn't allowed to read this path");
                                return false;
                            }
                        }
                        if (rule.isDenyType()) {
                            boolean isDenied = true;
                            ArrayList<RuleDimension> ruleDimensions = rule.getDimensions();
                            for (int dimIndex = 0; dimIndex < ruleDimensions.size(); dimIndex++) {
                                String dimName = ruleDimensions.get(dimIndex).getName();
                                int tupleDimIndex = tuple.getDimension().indexOf(dimName);
                                if (-1 == tupleDimIndex) {
                                    isDenied = false;
                                    break;
                                }
                                if (!ruleDimensions.get(dimIndex).affectWholeDimension()) {
                                    String tupleDimElement = tuple.getPath()[tupleDimIndex];
                                    ArrayList<String> dimElements = ruleDimensions.get(dimIndex).getElements();
                                    if (false == dimElements.contains(tupleDimElement)) {
                                        isDenied = false;
                                        break;
                                    }
                                }
                            }
                            if (isDenied) {
                                isDenied = false;
                                if (rule.getUsers().contains(username)) {
                                    isDenied = true;
                                } else {
                                    ArrayList<String> userGroups = auth.userGroups(username);
                                    for (int index = 0; index < userGroups.size(); index++) {
                                        if (rule.getGroups().contains(userGroups.get(index))) {
                                            isDenied = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (isDenied) {
                                _logger.debug("Denied to read");
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean checkWrite(String username, Tuple tuple) {
        String database = tuple.getDatabaseName();
        String cube = tuple.getCubename();
        IAuthentication auth = PluginProvider.getInstance().getAuthenticationInterface();
        if (this._databaseRules.containsKey(database)) {
            Hashtable<String, Hashtable<String, ArrayList<Rule>>> dbRules = this._databaseRules.get(database);
            if (dbRules.containsKey(cube)) {
                Hashtable<String, ArrayList<Rule>> cubeRules = dbRules.get(cube);
                ArrayList<Rule> writeRules = cubeRules.get(HASH_WRITERULES);
                for (int i = 0; i < writeRules.size(); i++) {
                    Rule rule = writeRules.get(i);
                    if (rule.isAllowType()) {
                        ArrayList<RuleDimension> ruleDimensions = rule.getDimensions();
                        for (int dimIndex = 0; dimIndex < ruleDimensions.size(); dimIndex++) {
                            String dimName = ruleDimensions.get(dimIndex).getName();
                            int tupleDimIndex = tuple.getDimension().indexOf(dimName);
                            if (-1 == tupleDimIndex) {
                                _logger.debug("Cell path doesn't contain obligitary dimension: " + dimName);
                                return false;
                            }
                            if (!ruleDimensions.get(dimIndex).affectWholeDimension()) {
                                String tupleDimElement = tuple.getPath()[tupleDimIndex];
                                ArrayList<String> dimElements = ruleDimensions.get(dimIndex).getElements();
                                if (false == dimElements.contains(tupleDimElement)) {
                                    _logger.debug(dimName + "(" + tupleDimElement + ") isn't allowed for this path");
                                    return false;
                                }
                            }
                        }
                        boolean isAccessible = false;
                        if (rule.getUsers().contains(username)) {
                            isAccessible = true;
                        } else {
                            ArrayList<String> groups = rule.getGroups();
                            for (int index = 0; index < groups.size(); index++) {
                                if (auth.belongsToGroup(username, groups.get(index))) {
                                    isAccessible = true;
                                    break;
                                }
                            }
                        }
                        if (false == isAccessible) {
                            _logger.debug(username + " isn't allowed to write this path");
                            return false;
                        }
                    }
                    if (rule.isDenyType()) {
                        boolean isDenied = true;
                        ArrayList<RuleDimension> ruleDimensions = rule.getDimensions();
                        for (int dimIndex = 0; dimIndex < ruleDimensions.size(); dimIndex++) {
                            String dimName = ruleDimensions.get(dimIndex).getName();
                            int tupleDimIndex = tuple.getDimension().indexOf(dimName);
                            if (-1 == tupleDimIndex) {
                                isDenied = false;
                                break;
                            }
                            if (!ruleDimensions.get(dimIndex).affectWholeDimension()) {
                                String tupleDimElement = tuple.getPath()[tupleDimIndex];
                                ArrayList<String> dimElements = ruleDimensions.get(dimIndex).getElements();
                                if (false == dimElements.contains(tupleDimElement)) {
                                    isDenied = false;
                                    break;
                                }
                            }
                        }
                        if (isDenied) {
                            isDenied = false;
                            if (rule.getUsers().contains(username)) {
                                isDenied = true;
                            } else {
                                ArrayList<String> userGroups = auth.userGroups(username);
                                for (int index = 0; index < userGroups.size(); index++) {
                                    if (rule.getGroups().contains(userGroups.get(index))) {
                                        isDenied = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (isDenied) {
                            _logger.debug("Denied to write");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public ArrayList<Rule> getReadRules(String databaseName, String cubeName) {
        ArrayList<Rule> rules = new ArrayList<Rule>();
        if (this._databaseRules.containsKey(databaseName)) {
            Hashtable<String, Hashtable<String, ArrayList<Rule>>> dbRules = this._databaseRules.get(databaseName);
            if (dbRules.containsKey(cubeName)) {
                Hashtable<String, ArrayList<Rule>> cubeRules = dbRules.get(cubeName);
                rules = cubeRules.get(HASH_READRULES);
            }
        }
        return rules;
    }

    public ArrayList<Rule> getWriteRules(String databaseName, String cubeName) {
        ArrayList<Rule> rules = new ArrayList<Rule>();
        if (this._databaseRules.containsKey(databaseName)) {
            Hashtable<String, Hashtable<String, ArrayList<Rule>>> dbRules = this._databaseRules.get(databaseName);
            if (dbRules.containsKey(cubeName)) {
                Hashtable<String, ArrayList<Rule>> cubeRules = dbRules.get(cubeName);
                rules = cubeRules.get(HASH_WRITERULES);
            }
        }
        return rules;
    }

    public void addReadRule(String databaseName, String cubeName, Rule newRule) {
        if (!this._databaseRules.containsKey(databaseName)) {
            Hashtable<String, Hashtable<String, ArrayList<Rule>>> cubeRules = new Hashtable<String, Hashtable<String, ArrayList<Rule>>>();
            Hashtable<String, ArrayList<Rule>> rules = new Hashtable<String, ArrayList<Rule>>();
            ArrayList<Rule> readRules = new ArrayList<Rule>();
            ArrayList<Rule> writeRules = new ArrayList<Rule>();
            rules.put(HASH_READRULES, readRules);
            rules.put(HASH_WRITERULES, writeRules);
            cubeRules.put(cubeName, rules);
            this._databaseRules.put(databaseName, cubeRules);
        }
        Hashtable<String, Hashtable<String, ArrayList<Rule>>> cubeRules = this._databaseRules.get(databaseName);
        if (!cubeRules.containsKey(cubeName)) {
            Hashtable<String, ArrayList<Rule>> rules = new Hashtable<String, ArrayList<Rule>>();
            ArrayList<Rule> readRules = new ArrayList<Rule>();
            ArrayList<Rule> writeRules = new ArrayList<Rule>();
            rules.put(HASH_READRULES, readRules);
            rules.put(HASH_WRITERULES, writeRules);
            cubeRules.put(cubeName, rules);
        }
        cubeRules.get(cubeName).get(HASH_READRULES).add(newRule);
    }

    public void addWriteRule(String databaseName, String cubeName, Rule newRule) {
        if (!this._databaseRules.containsKey(databaseName)) {
            Hashtable<String, Hashtable<String, ArrayList<Rule>>> cubeRules = new Hashtable<String, Hashtable<String, ArrayList<Rule>>>();
            Hashtable<String, ArrayList<Rule>> rules = new Hashtable<String, ArrayList<Rule>>();
            ArrayList<Rule> readRules = new ArrayList<Rule>();
            ArrayList<Rule> writeRules = new ArrayList<Rule>();
            rules.put(HASH_READRULES, readRules);
            rules.put(HASH_WRITERULES, writeRules);
            cubeRules.put(cubeName, rules);
            this._databaseRules.put(databaseName, cubeRules);
        }
        Hashtable<String, Hashtable<String, ArrayList<Rule>>> cubeRules = this._databaseRules.get(databaseName);
        if (!cubeRules.containsKey(cubeName)) {
            Hashtable<String, ArrayList<Rule>> rules = new Hashtable<String, ArrayList<Rule>>();
            ArrayList<Rule> readRules = new ArrayList<Rule>();
            ArrayList<Rule> writeRules = new ArrayList<Rule>();
            rules.put(HASH_READRULES, readRules);
            rules.put(HASH_WRITERULES, writeRules);
            cubeRules.put(cubeName, rules);
        }
        cubeRules.get(cubeName).get(HASH_WRITERULES).add(newRule);
    }

    public void deleteReadRule(String databaseName, String cubeName, Rule rule) {
        if (this._databaseRules.containsKey(databaseName)) {
            Hashtable<String, Hashtable<String, ArrayList<Rule>>> cubeRules = this._databaseRules.get(databaseName);
            if (cubeRules.containsKey(cubeName)) {
                ArrayList<Rule> rules = cubeRules.get(cubeName).get(HASH_READRULES);
                rules.remove(rule);
            }
        }
    }

    public void deleteWriteRule(String databaseName, String cubeName, Rule rule) {
        if (this._databaseRules.containsKey(databaseName)) {
            Hashtable<String, Hashtable<String, ArrayList<Rule>>> cubeRules = this._databaseRules.get(databaseName);
            if (cubeRules.containsKey(cubeName)) {
                ArrayList<Rule> rules = cubeRules.get(cubeName).get(HASH_WRITERULES);
                rules.remove(rule);
            }
        }
    }

    public JPlugin getConfigurationComponent(IServer serverContext) {
        JPluginACLDefault pluginComponent = new JPluginACLDefault(this, serverContext);
        return pluginComponent;
    }

    public void saveParameters() {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rulesElement = doc.createElement(Default.XML_RULES_NODE);
            for (Enumeration<String> dbKeys = this._databaseRules.keys(); dbKeys.hasMoreElements(); ) {
                String dbName = dbKeys.nextElement();
                Element dbElement = doc.createElement(Default.XML_DATABASE_NODE);
                dbElement.setAttribute(Default.XML_NAME_ATTRIBUTE, dbName);
                Hashtable<String, Hashtable<String, ArrayList<Rule>>> cubes = this._databaseRules.get(dbName);
                for (Enumeration<String> cubeKeys = cubes.keys(); cubeKeys.hasMoreElements(); ) {
                    String cubeName = cubeKeys.nextElement();
                    Element cubeElement = doc.createElement(Default.XML_CUBE_NODE);
                    cubeElement.setAttribute(Default.XML_NAME_ATTRIBUTE, cubeName);
                    Element readRulesElement = doc.createElement(Default.XML_READRULES_NODE);
                    ArrayList<Rule> readRules = cubes.get(cubeName).get(HASH_READRULES);
                    for (int i = 0; i < readRules.size(); i++) {
                        readRulesElement.appendChild(readRules.get(i).createDOMElement(doc));
                    }
                    cubeElement.appendChild(readRulesElement);
                    Element writeRulesElement = doc.createElement(Default.XML_WRITERULES_NODE);
                    ArrayList<Rule> writeRules = cubes.get(cubeName).get(HASH_WRITERULES);
                    for (int i = 0; i < writeRules.size(); i++) {
                        writeRulesElement.appendChild(writeRules.get(i).createDOMElement(doc));
                    }
                    cubeElement.appendChild(writeRulesElement);
                    dbElement.appendChild(cubeElement);
                }
                rulesElement.appendChild(dbElement);
            }
            doc.appendChild(rulesElement);
            OutputFormat format = new OutputFormat(doc);
            format.setIndenting(true);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(this._configFile));
            XMLSerializer out = new XMLSerializer(fileOutputStream, format);
            out.serialize(doc);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception ex) {
            _logger.error("Error writing config file", ex);
        }
    }
}
