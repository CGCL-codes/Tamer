package uk.ac.ebi.intact.sanity.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.ac.ebi.intact.commons.util.ClassUtils;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.util.*;

/**
 * Declared rules manager.
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id:RuleManager.java 9493 2007-08-17 14:02:49Z baranda $
 */
public class DeclaredRuleManager {

    private static final Log log = LogFactory.getLog(DeclaredRuleManager.class);

    public static final String RULES_XML_PATH = "META-INF/sanity-rules.xml";

    private static ThreadLocal<DeclaredRuleManager> instance = new ThreadLocal<DeclaredRuleManager>();

    public static DeclaredRuleManager getInstance() {
        DeclaredRuleManager declaredRuleManager = instance.get();
        if (declaredRuleManager == null) {
            declaredRuleManager = new DeclaredRuleManager();
            instance.set(declaredRuleManager);
        }
        return declaredRuleManager;
    }

    public static void close() {
        instance.set(null);
    }

    private List<DeclaredRule> availableDeclaredRules = new ArrayList<DeclaredRule>();

    private DeclaredRuleManager() {
        try {
            loadDeclaredRulesFromClassPath();
        } catch (IOException e) {
            throw new SanityRuleException("Problem getting declared rules from classpath", e);
        }
    }

    public Set<String> getAvailableGroups() {
        Set<String> groups = new HashSet<String>();
        for (DeclaredRule rule : availableDeclaredRules) {
            groups.addAll(rule.getGroups().getGroups());
        }
        return groups;
    }

    public List<DeclaredRule> getDeclaredRulesForTarget(Class targetClass) {
        final Set<String> availableGroups = getAvailableGroups();
        return getDeclaredRulesForTarget(targetClass, availableGroups.toArray(new String[availableGroups.size()]));
    }

    public List<DeclaredRule> getDeclaredRulesForTarget(Class targetClass, String... groups) {
        List<DeclaredRule> rules = new ArrayList<DeclaredRule>();
        for (DeclaredRule rule : availableDeclaredRules) {
            Class ruleTargetClass;
            try {
                ruleTargetClass = Class.forName(rule.getTargetClass());
            } catch (ClassNotFoundException e) {
                throw new SanityRuleException("Found declared rule with a target class not found in the classpath: " + rule.getTargetClass());
            }
            if (ruleTargetClass.isAssignableFrom(targetClass) && isDeclaredRuleInGroup(rule, groups)) {
                rules.add(rule);
            }
        }
        return rules;
    }

    public List<DeclaredRule> getDeclaredRulesForGroup(String... groups) {
        List<DeclaredRule> rules = new ArrayList<DeclaredRule>();
        for (DeclaredRule rule : availableDeclaredRules) {
            if (isDeclaredRuleInGroup(rule, groups)) {
                rules.add(rule);
            }
        }
        return rules;
    }

    public Set<String> getAvailableTargetClasses() {
        Set<String> targets = new HashSet<String>();
        for (DeclaredRule rule : availableDeclaredRules) {
            targets.add(rule.getTargetClass());
        }
        return targets;
    }

    public static DeclaredRules readRulesXml(InputStream is) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(DeclaredRules.class.getPackage().getName());
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (DeclaredRules) unmarshaller.unmarshal(is);
    }

    public static void writeRulesXml(DeclaredRules rules, Writer writer) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("declared-rules");
        root.setAttribute("xmlns", "http://uk.ac.ebi.intact/sanity/rules");
        document.appendChild(root);
        for (DeclaredRule declaredRule : rules.getDeclaredRules()) {
            Element decRuleNode = document.createElement("declared-rule");
            root.appendChild(decRuleNode);
            Element ruleNameNode = document.createElement("rule-name");
            decRuleNode.appendChild(ruleNameNode);
            ruleNameNode.appendChild(document.createTextNode(declaredRule.getRuleName()));
            Element ruleClassNode = document.createElement("rule-class");
            decRuleNode.appendChild(ruleClassNode);
            ruleClassNode.appendChild(document.createTextNode(declaredRule.getRuleClass()));
            Element targetClassNode = document.createElement("target-class");
            decRuleNode.appendChild(targetClassNode);
            targetClassNode.appendChild(document.createTextNode(declaredRule.getTargetClass()));
            Element groupsNode = document.createElement("groups");
            decRuleNode.appendChild(groupsNode);
            if (declaredRule.getGroups() != null) {
                for (String groupName : declaredRule.getGroups().getGroups()) {
                    Element groupNode = document.createElement("group");
                    groupsNode.appendChild(groupNode);
                    groupNode.appendChild(document.createTextNode(groupName));
                }
            }
        }
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        StreamResult result = new StreamResult(writer);
        DOMSource source = new DOMSource(document);
        trans.transform(source, result);
    }

    public List<DeclaredRule> getAvailableDeclaredRules() {
        return availableDeclaredRules;
    }

    public void addDeclaredRule(DeclaredRule declaredRule) {
        boolean exists = false;
        for (DeclaredRule availableRule : availableDeclaredRules) {
            if (availableRule.getRuleName().equals(declaredRule.getRuleName())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            availableDeclaredRules.add(declaredRule);
        }
    }

    public void addAllDeclaredRules(Collection<DeclaredRule> declaredRules) {
        for (DeclaredRule declaredRule : declaredRules) {
            addDeclaredRule(declaredRule);
        }
    }

    protected void loadDeclaredRulesFromClassPath() throws IOException {
        Collection<URL> resources = ClassUtils.searchResourcesInClasspath(RULES_XML_PATH);
        if (log.isDebugEnabled()) log.debug("Found " + resources.size() + " sanity-rules.xml files in the classpath");
        if (log.isDebugEnabled() && resources.size() > 1) {
            int i = 1;
            for (URL resource : resources) {
                log.debug(i + ") " + resource.getFile());
                i++;
            }
        }
        for (URL resource : resources) {
            try {
                DeclaredRules dr = readDeclaredRules(resource.openStream());
                addAllDeclaredRules(dr.getDeclaredRules());
                if (log.isDebugEnabled()) log.debug("Loaded " + dr.getDeclaredRules().size() + " declared rules from: " + resource);
            } catch (Throwable t) {
                throw new SanityRuleException("Problem reading declared rules from resource: " + resource, t);
            }
        }
    }

    protected static DeclaredRules readDeclaredRules(InputStream is) {
        if (is == null) {
            throw new IllegalStateException("No sanity rules file found: " + RULES_XML_PATH);
        }
        DeclaredRules rules = null;
        try {
            rules = readRulesXml(is);
        } catch (JAXBException e) {
            throw new SanityRuleException(e);
        }
        return rules;
    }

    protected static boolean isDeclaredRuleInGroup(DeclaredRule rule, String... groupNames) {
        for (String groupName : rule.getGroups().getGroups()) {
            if (Arrays.binarySearch(groupNames, groupName) > -1) {
                return true;
            }
        }
        return false;
    }
}
