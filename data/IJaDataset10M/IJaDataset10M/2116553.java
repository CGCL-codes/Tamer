package org.deft.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.deft.helper.DeftLogger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Status;

public class TransformerRuleSet {

    private enum SelectState {

        ALL, NONE
    }

    private List<TransformerRule> includeRules;

    private List<TransformerRule> excludeRules;

    private IOutlineNodeFactory outlineNodeFactory;

    private String pluginID;

    private SelectState selectState;

    private TransformerRule defaultRule;

    private HashMap<String, TransformerRule> cachedRules;

    public TransformerRuleSet(IConfigurationElement transformer) {
        try {
            this.outlineNodeFactory = (IOutlineNodeFactory) transformer.createExecutableExtension("class");
        } catch (CoreException e) {
            DeftLogger.log(Status.ERROR, "Could not instantiate IElementTransformer.", e, TransformerRuleSet.class);
        }
        this.includeRules = new ArrayList<TransformerRule>();
        this.excludeRules = new ArrayList<TransformerRule>();
        for (IConfigurationElement element : transformer.getChildren()) {
            if (element.getName().toLowerCase().equals("include")) {
                for (IConfigurationElement rule : element.getChildren()) {
                    includeRules.add(new TransformerRule(rule));
                }
            } else if (element.getName().toLowerCase().equals("exclude")) {
                for (IConfigurationElement rule : element.getChildren()) {
                    excludeRules.add(new TransformerRule(rule));
                }
            } else if (element.getName().toLowerCase().equals("defaultelement")) {
                defaultRule = new TransformerRule(element);
            }
        }
        this.pluginID = transformer.getContributor().getName();
        String select = transformer.getAttribute("select");
        if (select.equals("none")) this.selectState = SelectState.NONE; else this.selectState = SelectState.ALL;
        this.cachedRules = new HashMap<String, TransformerRule>();
    }

    /**
	 * Returns a rule that matches the given element name or null if there is no
	 * matching rule. If there are more than one matching rules, the first one
	 * that was found will be returned.
	 * 
	 * @param elementName
	 * @return
	 */
    public TransformerRule getRule(String elementName) {
        if (cachedRules.containsKey(elementName)) {
            return cachedRules.get(elementName);
        } else {
            if (selectState == SelectState.NONE) {
                for (TransformerRule rule : includeRules) {
                    if (elementName.matches(rule.getElementName())) {
                        cachedRules.put(elementName, rule);
                        return rule;
                    }
                }
                return null;
            } else if (selectState == SelectState.ALL) {
                for (TransformerRule rule : includeRules) {
                    if (elementName.matches(rule.getElementName())) {
                        cachedRules.put(elementName, rule);
                        return rule;
                    }
                }
                for (TransformerRule rule : excludeRules) {
                    if (elementName.matches(rule.getElementName())) return null;
                }
                cachedRules.put(elementName, defaultRule);
                return defaultRule;
            }
            return null;
        }
    }

    public TransformerRule getDefaultRule() {
        return defaultRule;
    }

    /**
	 * Returns true if there is a matching rule in the rule set or false if not.
	 * 
	 * @param elementName
	 * @return
	 */
    public boolean containsRule(String elementName) {
        return getRule(elementName) != null;
    }

    /**
	 * Returns the id of the plugin that uses the transformer extension point.
	 * 
	 * @return
	 */
    public String getPluginID() {
        return pluginID;
    }

    /**
	 * Retunrs the transformer that is connected to the sset of rules.
	 * 
	 * @return
	 */
    public IOutlineNodeFactory getOutlineNodeFactory() {
        return outlineNodeFactory;
    }
}
