package org.jaffa.rules.realm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.log4j.Logger;
import org.jaffa.rules.JaffaRulesFrameworkException;
import org.jaffa.rules.commons.AbstractLoader;
import org.jaffa.rules.meta.RuleMetaData;
import org.w3c.dom.Element;

/** This class is used to import realms.
 */
public class RealmRepository extends AbstractLoader {

    private static Logger log = Logger.getLogger(RealmRepository.class);

    private static final String ELEMENT_REALM = "realm";

    private static final String ELEMENT_CLASS = "class";

    private static final String ELEMENT_INHERITANCE_RULE_FILTER = "inheritance-rule-filter";

    private static final String ATTR_NAME = "name";

    private static final String ATTR_REGEX = "regex";

    private static final String ATTR_INCLUDES = "includes";

    private static final String ATTR_EXCLUDES = "excludes";

    private static RealmRepository c_instance = new RealmRepository();

    private List<Realm> m_realms = new LinkedList<Realm>();

    private Map<String, List<Realm>> m_realmsBySource = new HashMap<String, List<Realm>>();

    private Map<String, String> m_realmByClassName = new HashMap<String, String>();

    private Map<String, String> m_cacheOfRealmByClassName = new WeakHashMap<String, String>();

    private Map<String, List<String>> m_cacheOfInheritanceRulesToIncludeByRealmName = new WeakHashMap<String, List<String>>();

    private Map<String, List<String>> m_cacheOfInheritanceRulesToExcludeByRealmName = new WeakHashMap<String, List<String>>();

    /** Creates an instance. */
    private RealmRepository() {
    }

    /** Returns the Singleton instance.
     * @return the Singleton instance.
     */
    public static RealmRepository instance() {
        return c_instance;
    }

    /** Imports realms.
     * @param metadataElement the metadata element we are processing within a source.
     * @param source the name of the source file.
     * @throws JaffaRulesFrameworkException if any internal error occurs.
     */
    public void load(Element metadataElement, String source) throws JaffaRulesFrameworkException {
        if (log.isDebugEnabled()) log.debug("Loading realms from " + source);
        Element[] realmElements = getChildren(metadataElement);
        for (Element realmElement : realmElements) {
            if (ELEMENT_REALM.equals(realmElement.getNodeName())) {
                Realm realm = new Realm();
                realm.setName(realmElement.hasAttribute(ATTR_NAME) ? realmElement.getAttribute(ATTR_NAME) : null);
                realm.setSource(source);
                realm.setLine(getLine(realmElement));
                Element[] realmChildren = getChildren(realmElement);
                for (Element realmChild : realmChildren) {
                    if (ELEMENT_CLASS.equals(realmChild.getNodeName())) realm.addClassRegex(realmChild.hasAttribute(ATTR_REGEX) ? realmChild.getAttribute(ATTR_REGEX) : null); else if (ELEMENT_INHERITANCE_RULE_FILTER.equals(realmChild.getNodeName())) {
                        if (realmChild.hasAttribute(ATTR_INCLUDES)) realm.addInheritanceRulesToInclude(Arrays.asList(realmChild.getAttribute(ATTR_INCLUDES).split(",")));
                        if (realmChild.hasAttribute(ATTR_EXCLUDES)) realm.addInheritanceRulesToExclude(Arrays.asList(realmChild.getAttribute(ATTR_EXCLUDES).split(",")));
                    } else {
                        log.warn("Unknown element found while importing class regex inside a realm element: " + realmChild.getNodeName());
                    }
                }
                if (log.isDebugEnabled()) log.debug("Loaded realm: " + realm);
                synchronized (this) {
                    m_realms.add(realm);
                    List<Realm> realms = m_realmsBySource.get(realm.getSource());
                    if (realms == null) {
                        realms = new LinkedList<Realm>();
                        m_realmsBySource.put(realm.getSource(), realms);
                    }
                    realms.add(realm);
                }
            } else {
                log.warn("Unknown element found while importing Realm: " + realmElement.getNodeName());
            }
        }
    }

    /** Unloads the XML that was imported from the input.
     * @param uri the URI of the source file.
     * @throws JaffaRulesFrameworkException if any internal error occurs.
     */
    public synchronized void unload(String uri) throws JaffaRulesFrameworkException {
        if (log.isDebugEnabled()) log.debug("Clearing cache");
        m_cacheOfRealmByClassName.clear();
        m_cacheOfInheritanceRulesToIncludeByRealmName.clear();
        m_cacheOfInheritanceRulesToExcludeByRealmName.clear();
        List<Realm> realms = m_realmsBySource.remove(uri);
        if (realms != null) {
            for (Realm realm : realms) {
                if (log.isDebugEnabled()) log.debug("Unloading " + realm);
                m_realms.remove(realm);
            }
        }
    }

    /** Clears the Repository.
     * @throws JaffaRulesFrameworkException if any internal error occurs.
     */
    public synchronized void clear() throws JaffaRulesFrameworkException {
        if (log.isDebugEnabled()) log.debug("Clearing...");
        m_realms.clear();
        m_realmsBySource.clear();
        m_realmByClassName.clear();
        m_cacheOfRealmByClassName.clear();
        m_cacheOfInheritanceRulesToIncludeByRealmName.clear();
        m_cacheOfInheritanceRulesToExcludeByRealmName.clear();
    }

    /** Adds a className/executionRealm mapping.
     * @param className the class name.
     * @param executionRealm the execution realm.
     */
    public void addMapping(String className, String executionRealm) {
        synchronized (m_realmByClassName) {
            m_realmByClassName.put(className, executionRealm);
        }
    }

    /** Removes the mapping for input className.
     * @param className the class name.
     */
    public void removeMapping(String className) {
        synchronized (m_realmByClassName) {
            m_realmByClassName.remove(className);
        }
    }

    /** Determines the execution-realm for the input class by looking up the cache or evaluating the classRegexes in the repository.
     * A null will be returned if no matching definition is found for the input class or its parent class or its interfaces
     * NOTE: This method should be invoked only after all the metadata has been loaded.
     * @param className The input class.
     * @return The the execution-realm for the input class.
     */
    public String find(String className) {
        if (!m_cacheOfRealmByClassName.containsKey(className)) {
            synchronized (m_cacheOfRealmByClassName) {
                if (!m_cacheOfRealmByClassName.containsKey(className)) {
                    String realm = m_realmByClassName.containsKey(className) ? m_realmByClassName.get(className) : findRecursively(className);
                    m_cacheOfRealmByClassName.put(className, realm);
                    return realm;
                }
            }
        }
        return m_cacheOfRealmByClassName.get(className);
    }

    /** Returns a list of inheritanceRulesToInclude as defined for the input realm.
     * A null will be returned if such a filter has not been defined for the realm.
     * NOTE: This method should be invoked only after all the metadata has been loaded.
     * @param realmName The input realm.
     * @return a list of inheritanceRulesToInclude as defined for the input realm.
     */
    public List<String> getInheritanceRulesToInclude(String realmName) {
        if (!m_cacheOfInheritanceRulesToIncludeByRealmName.containsKey(realmName)) {
            synchronized (m_cacheOfInheritanceRulesToIncludeByRealmName) {
                if (!m_cacheOfInheritanceRulesToIncludeByRealmName.containsKey(realmName)) {
                    List<String> rules = null;
                    for (Realm realm : m_realms) {
                        if (realmName.equals(realm.getName()) && realm.getInheritanceRulesToInclude() != null) {
                            if (rules == null) rules = new LinkedList<String>();
                            rules.addAll(realm.getInheritanceRulesToInclude());
                        }
                    }
                    m_cacheOfInheritanceRulesToIncludeByRealmName.put(realmName, rules);
                    if (log.isDebugEnabled()) log.debug("InheritanceRulesToInclude for realm '" + realmName + "' are " + rules);
                    return rules;
                }
            }
        }
        return m_cacheOfInheritanceRulesToIncludeByRealmName.get(realmName);
    }

    /** Returns a list of inheritanceRulesToExclude as defined for the input realm.
     * A null will be returned if such a filter has not been defined for the realm.
     * NOTE: This method should be invoked only after all the metadata has been loaded.
     * @param realmName The input realm.
     * @return a list of inheritanceRulesToExclude as defined for the input realm.
     */
    public List<String> getInheritanceRulesToExclude(String realmName) {
        if (!m_cacheOfInheritanceRulesToExcludeByRealmName.containsKey(realmName)) {
            synchronized (m_cacheOfInheritanceRulesToExcludeByRealmName) {
                if (!m_cacheOfInheritanceRulesToExcludeByRealmName.containsKey(realmName)) {
                    List<String> rules = null;
                    for (Realm realm : m_realms) {
                        if (realmName.equals(realm.getName()) && realm.getInheritanceRulesToExclude() != null) {
                            if (rules == null) rules = new LinkedList<String>();
                            rules.addAll(realm.getInheritanceRulesToExclude());
                        }
                    }
                    m_cacheOfInheritanceRulesToExcludeByRealmName.put(realmName, rules);
                    if (log.isDebugEnabled()) log.debug("InheritanceRulesToExclude for realm '" + realmName + "' are " + rules);
                    return rules;
                }
            }
        }
        return m_cacheOfInheritanceRulesToExcludeByRealmName.get(realmName);
    }

    /** Returns true if the realm for the targetClassName can inherit the given rule.
     * A true will be returned if no realm is defined for the targetClassName.
     * @param targetClassName the class which is inheriting the rule.
     * @param rule the rule to be inherited.
     * @return true if the realm for the targetClassName can inherit the given rule.
     */
    public boolean isInheritable(String targetClassName, RuleMetaData rule) {
        boolean inheritable = true;
        if (!targetClassName.equals(rule.getClassMetaData().getName())) {
            String targetRealmName = find(targetClassName);
            if (targetRealmName != null) {
                List<String> inheritanceRulesToInclude = getInheritanceRulesToInclude(targetRealmName);
                List<String> inheritanceRulesToExclude = getInheritanceRulesToExclude(targetRealmName);
                inheritable = isInheritable(targetClassName, targetRealmName, inheritanceRulesToInclude, inheritanceRulesToExclude, rule);
            }
        }
        return inheritable;
    }

    /** Iterates through the input rules, returning only those that can be inherited by the realm for the targetClassName.
     * All rules will be returned if no realm is defined for the targetClassName.
     * @param targetClassName the class which is inheriting the rules.
     * @param rules the rules to be inherited.
     * @return inheritable rules.
     */
    public List<RuleMetaData> getInheritableRules(String targetClassName, List<RuleMetaData> rules) {
        if (log.isDebugEnabled()) log.debug("Checking inheritableRules for class '" + targetClassName + "' from " + rules);
        List<RuleMetaData> output = rules;
        if (rules != null && rules.size() > 0) {
            String targetRealmName = find(targetClassName);
            if (targetRealmName != null) {
                output = null;
                List<String> inheritanceRulesToInclude = getInheritanceRulesToInclude(targetRealmName);
                List<String> inheritanceRulesToExclude = getInheritanceRulesToExclude(targetRealmName);
                for (RuleMetaData rule : rules) {
                    if (isInheritable(targetClassName, targetRealmName, inheritanceRulesToInclude, inheritanceRulesToExclude, rule)) {
                        if (output == null) output = new LinkedList<RuleMetaData>();
                        output.add(rule);
                    }
                }
            }
        }
        if (log.isDebugEnabled()) log.debug("InheritableRules for class '" + targetClassName + "' are " + output);
        return output;
    }

    /** Returns a List of Realm instances defined in the input source file.
     * NOTE: This method should be invoked only after all the metadata has been loaded.
     * @param source the name of the source file.
     * @return a List of Realm instances defined in the input source file.
     */
    public List<Realm> getRealmsBySource(String source) {
        return m_realmsBySource.get(source);
    }

    /** Determines the execution-realm for the input class by evaluating the classRegexes in the repository.
     * A null will be returned if no matching definition is found for the input class or its parent class or its interfaces
     * @param className The input class.
     * @return The the execution-realm for the input class.
     */
    private String findRecursively(String className) {
        if (log.isDebugEnabled()) log.debug("Finding realm for " + className);
        for (Realm realm : m_realms) {
            if (realm.getClassRegexes() != null) {
                for (String classRegex : realm.getClassRegexes()) {
                    if (className.matches(classRegex)) {
                        String output = realm.getName();
                        if (log.isDebugEnabled()) log.debug("Matched " + classRegex + ", realm=" + output);
                        return output;
                    }
                }
            }
        }
        String output = null;
        try {
            Class clazz = Class.forName(className);
            if (clazz.getSuperclass() != null) output = findRecursively(clazz.getSuperclass().getName());
            if (output == null && clazz.getInterfaces() != null) {
                for (int i = 0; i < clazz.getInterfaces().length; i++) {
                    output = findRecursively(clazz.getInterfaces()[i].getName());
                    if (output != null) break;
                }
            }
        } catch (ClassNotFoundException e) {
            if (log.isDebugEnabled()) log.debug("Assuming virtual class for " + className);
        }
        return output;
    }

    /** Returns true if the realm for the targetClassName can inherit the given rule.
     * A true will be returned if no realm is defined for the targetClassName.
     * @param targetClassName the class which is inheriting the rule.
     * @param rule the rule to be inherited.
     * @return true if the realm for the targetClassName can inherit the given rule.
     */
    private boolean isInheritable(String targetClassName, String targetRealmName, List<String> inheritanceRulesToInclude, List<String> inheritanceRulesToExclude, RuleMetaData rule) {
        if (!targetClassName.equals(rule.getClassMetaData().getName())) {
            String ruleName = rule.getName();
            return (inheritanceRulesToInclude == null || inheritanceRulesToInclude.contains(ruleName)) && (inheritanceRulesToExclude == null || !inheritanceRulesToExclude.contains(ruleName));
        }
        return true;
    }
}
