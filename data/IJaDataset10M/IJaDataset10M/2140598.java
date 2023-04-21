package org.apache.catalina.startup;

import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.RuleSetBase;

/**
 * <p><strong>RuleSet</strong> for processing the JNDI Enterprise Naming
 * Context resource declaration elements.</p>
 *
 * @author Craig R. McClanahan
 * @author Remy Maucherat
 * @version $Revision: 570102 $ $Date: 2007-08-27 15:20:38 +0200 (Mon, 27 Aug 2007) $
 */
public class NamingRuleSet extends RuleSetBase {

    /**
     * The matching pattern prefix to use for recognizing our elements.
     */
    protected String prefix = null;

    /**
     * Construct an instance of this <code>RuleSet</code> with the default
     * matching pattern prefix.
     */
    public NamingRuleSet() {
        this("");
    }

    /**
     * Construct an instance of this <code>RuleSet</code> with the specified
     * matching pattern prefix.
     *
     * @param prefix Prefix for matching pattern rules (including the
     *  trailing slash character)
     */
    public NamingRuleSet(String prefix) {
        super();
        this.namespaceURI = null;
        this.prefix = prefix;
    }

    /**
     * <p>Add the set of Rule instances defined in this RuleSet to the
     * specified <code>Digester</code> instance, associating them with
     * our namespace URI (if any).  This method should only be called
     * by a Digester instance.</p>
     *
     * @param digester Digester instance to which the new Rule instances
     *  should be added.
     */
    public void addRuleInstances(Digester digester) {
        digester.addObjectCreate(prefix + "Ejb", "org.apache.catalina.deploy.ContextEjb");
        digester.addRule(prefix + "Ejb", new SetAllPropertiesRule());
        digester.addRule(prefix + "Ejb", new SetNextNamingRule("addEjb", "org.apache.catalina.deploy.ContextEjb"));
        digester.addObjectCreate(prefix + "Environment", "org.apache.catalina.deploy.ContextEnvironment");
        digester.addSetProperties(prefix + "Environment");
        digester.addRule(prefix + "Environment", new SetNextNamingRule("addEnvironment", "org.apache.catalina.deploy.ContextEnvironment"));
        digester.addObjectCreate(prefix + "LocalEjb", "org.apache.catalina.deploy.ContextLocalEjb");
        digester.addRule(prefix + "LocalEjb", new SetAllPropertiesRule());
        digester.addRule(prefix + "LocalEjb", new SetNextNamingRule("addLocalEjb", "org.apache.catalina.deploy.ContextLocalEjb"));
        digester.addObjectCreate(prefix + "Resource", "org.apache.catalina.deploy.ContextResource");
        digester.addRule(prefix + "Resource", new SetAllPropertiesRule());
        digester.addRule(prefix + "Resource", new SetNextNamingRule("addResource", "org.apache.catalina.deploy.ContextResource"));
        digester.addObjectCreate(prefix + "ResourceEnvRef", "org.apache.catalina.deploy.ContextResourceEnvRef");
        digester.addRule(prefix + "ResourceEnvRef", new SetAllPropertiesRule());
        digester.addRule(prefix + "ResourceEnvRef", new SetNextNamingRule("addResourceEnvRef", "org.apache.catalina.deploy.ContextResourceEnvRef"));
        digester.addObjectCreate(prefix + "ServiceRef", "org.apache.catalina.deploy.ContextService");
        digester.addRule(prefix + "ServiceRef", new SetAllPropertiesRule());
        digester.addRule(prefix + "ServiceRef", new SetNextNamingRule("addService", "org.apache.catalina.deploy.ContextService"));
        digester.addObjectCreate(prefix + "Transaction", "org.apache.catalina.deploy.ContextTransaction");
        digester.addRule(prefix + "Transaction", new SetAllPropertiesRule());
        digester.addRule(prefix + "Transaction", new SetNextNamingRule("setTransaction", "org.apache.catalina.deploy.ContextTransaction"));
    }
}
