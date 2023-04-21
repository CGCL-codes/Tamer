package net.paoding.rest.view.velocity;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.velocity.tools.view.ToolboxRuleSet;

/**
 * <p>The set of Digester rules required to parse a toolbox
 * configuration file (<code>toolbox.xml</code>) for the
 * ServletToolboxManager class.</p>
 *
 * @since VelocityTools 1.1
 * @author Nathan Bubna
 * @version $Id: ServletToolboxRuleSet.java 479724 2006-11-27 18:49:37Z nbubna $
 */
public class TheToolboxRuleSet extends ToolboxRuleSet {

    /**
     * Overrides {@link ToolboxRuleSet} to add create-session rule.
     *
     * <p>These rules assume that an instance of
     * <code>org.apache.velocity.tools.view.ServletToolboxManager</code> is
     * pushed onto the evaluation stack before parsing begins.</p>
     *
     * @param digester Digester instance to which the new Rule instances
     *  should be added.
     */
    public void addRuleInstances(Digester digester) {
        digester.addRule("toolbox/create-session", new CreateSessionRule());
        digester.addRule("toolbox/xhtml", new XhtmlRule());
        super.addRuleInstances(digester);
    }

    /**
     * Overrides {@link ToolboxRuleSet} to add rule for scope element.
     */
    protected void addToolRules(Digester digester) {
        super.addToolRules(digester);
        digester.addBeanPropertySetter("toolbox/tool/scope", "scope");
        digester.addBeanPropertySetter("toolbox/tool/request-path", "requestPath");
    }

    /**
     * Overrides {@link ToolboxRuleSet} to use ServletToolInfo class.
     */
    protected Class getToolInfoClass() {
        return TheToolInfo.class;
    }

    /**
     * Abstract rule for configuring boolean options on the parent
     * object/element of the matching element.
     */
    protected abstract class BooleanConfigRule extends Rule {

        public void body(String ns, String name, String text) throws Exception {
            Object parent = digester.peek();
            if ("yes".equalsIgnoreCase(text)) {
                setBoolean(parent, Boolean.TRUE);
            } else {
                setBoolean(parent, Boolean.valueOf(text));
            }
        }

        /**
         * Takes the parent object and boolean value in order to
         * call the appropriate method on the parent for the
         * implementing rule.
         *
         * @param parent the parent object/element in the digester's stack
         * @param value the boolean value contained in the current element
         */
        public abstract void setBoolean(Object parent, Boolean value) throws Exception;
    }

    /**
     * Rule that sets <code>setCreateSession()</code> for the top object
     * on the stack, which must be a
     * <code>org.apache.velocity.tools.ServletToolboxManager</code>.
     */
    protected final class CreateSessionRule extends BooleanConfigRule {

        public void setBoolean(Object obj, Boolean b) throws Exception {
            ((TheToolboxManager) obj).setCreateSession(b.booleanValue());
        }
    }

    /**
     * Rule that sets <code>setXhtml()</code> for the top object
     * on the stack, which must be a
     * <code>org.apache.velocity.tools.ServletToolboxManager</code>.
     */
    protected final class XhtmlRule extends BooleanConfigRule {

        public void setBoolean(Object obj, Boolean b) throws Exception {
            ((TheToolboxManager) obj).setXhtml(b);
        }
    }
}
