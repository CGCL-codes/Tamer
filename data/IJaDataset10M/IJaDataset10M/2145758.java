package org.marcont2.rulegenerator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.marcont2.rulegenerator.model.Namespace;

/**
 * Class representing translation rules in the model
 * @author Piotr Piotrowski
 */
public class TranslationRules implements Serializable {

    /**
     * List of all rules
     */
    private List<Rule> rules = new ArrayList<Rule>();

    /**
     * Map with all rules for quick reference.
     */
    private Map<String, Rule> rulesMap = new HashMap<String, Rule>();

    /**
     * Namespaces
     */
    private List<Namespace> namespaces = new ArrayList<Namespace>();

    /** Creates a new instance of TranslationRules */
    public TranslationRules() {
    }

    /**
     * Clears the object
     */
    public void clear() {
        rules.clear();
        rulesMap.clear();
        namespaces.clear();
    }

    /**
     * Adds provided rule to this object.
     * @param rule rule to add
     */
    public void addRule(Rule rule) {
        rulesMap.put(rule.getName(), rule);
        rules.add(rule);
    }

    /**
     * Changes rule. Does not check if the new name is valid
     * @param oldRule rule equal to the one from the model we want to change
     * @param newRule rule containing new values for the rule
     */
    public void changeRule(Rule oldRule, Rule newRule) {
        String oldName = oldRule.getName();
        String newName = newRule.getName();
        Rule tmp = rulesMap.remove(oldName);
        tmp.setName(newName);
        rulesMap.put(newName, tmp);
        tmp.setTerminate(newRule.isTerminate());
    }

    /**
     * Removes rule from translation rules
     * @param ruleName name of the rule to be removed
     * @return if the operation was successful
     */
    public boolean removeRule(String ruleName) {
        Rule tmp = rulesMap.get(ruleName);
        if (tmp.canBeRemoved()) {
            rulesMap.remove(ruleName);
            rules.remove(tmp);
            tmp.removeCalls();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a rule with the given name exists
     * @param rule name of the rule
     * @return true if the rule exists
     */
    public boolean containsRule(String rule) {
        return rulesMap.containsKey(rule);
    }

    /**
     * Returns rule object with the given name
     * @param rule name of the rule to return
     * @return rule object with the given name
     */
    public Rule getRule(String rule) {
        return rulesMap.get(rule);
    }

    /**
     * Adds namespace to the translation rules
     * @param namespace namespace to be added
     */
    public void addNamespace(Namespace namespace) {
        namespaces.add(namespace);
    }

    /**
     * Removes namespace from the translation rules
     * @param namespace namespace to be removed
     */
    public void removeNamespace(Namespace namespace) {
        namespaces.remove(namespace);
    }

    /**
     * Changes namespace.
     * Searches in namespaces for an object equal to oldNamespace and changes it.
     * @param oldNamespace namespace to change
     * @param newNamespace new value for the namespace
     */
    public void changeNamespace(Namespace oldNamespace, Namespace newNamespace) {
        int index = namespaces.indexOf(oldNamespace);
        Namespace tmp = namespaces.get(index);
        tmp.setName(newNamespace.getName());
        tmp.setUri(newNamespace.getUri());
    }

    /**
     * Returns the short name of the namespace whose URI is provided.
     * @return short name of the namespace or null if it is unknown
     * @param uri URI of the namespace
     */
    public String getNamespace(String uri) {
        if (uri == null) {
            return null;
        }
        for (Namespace n : namespaces) {
            if (uri.equals(n.getUri())) {
                return n.getName();
            }
        }
        return null;
    }

    /**
     * Returns a list of rules
     * @return list of rules
     */
    public List<Rule> getRules() {
        return rules;
    }

    /**
     * Returns collection of namespaces
     * @return collection of namespaces
     */
    public List<Namespace> getNamespaces() {
        return namespaces;
    }
}
