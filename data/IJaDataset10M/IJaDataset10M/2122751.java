package org.jaffa.rules.jbossaop.interceptors;

import java.security.AccessControlException;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jaffa.rules.jbossaop.tools.AopGenerator;
import org.jaffa.rules.meta.RuleMetaData;
import org.jaffa.security.SecurityManager;
import org.jboss.aop.joinpoint.MethodInvocation;

/** This rule is used limit access to a method based on the access to a component.
 * A SecurityException is raised if access is denied.
 */
public class ComponentGuardInterceptor extends AbstractRuleInterceptor {

    private static Logger log = Logger.getLogger(ComponentGuardInterceptor.class);

    /** Creates an instance.
     */
    public ComponentGuardInterceptor() {
        super("component-guard");
    }

    /** Generates a JBossAOP specific pointcut.
     * @param className the class name. Should always be passed.
     * @param propertyName the property name. This input is ignored.
     * @param rule the RuleMetaData for which the pointcut is being generated.
     * @return a JBoss-AOP specific pointcut.
     */
    @Override
    public String[] generatePointcuts(String className, String propertyName, RuleMetaData rule) {
        return new String[] { "execution(* " + className + "->" + rule.getParameter("method") + ")" };
    }

    /** Injects the necessary behavior.
     * @param invocation the Invocation.
     * @param targetClassName The target Class.
     * @param targetObject The target Object.
     * @throws Throwable if any error occurs.
     * @return output from the next element in the invocation stack.
     */
    protected Object invoke(MethodInvocation invocation, String targetClassName, Object targetObject) throws Throwable {
        Map<String, List<RuleMetaData>> ruleMap = getPropertyRuleMap(targetClassName, targetObject);
        if (ruleMap != null) {
            List<RuleMetaData> rules = ruleMap.get(null);
            if (rules != null) {
                for (RuleMetaData rule : rules) {
                    if (AopGenerator.match(invocation.getMethod(), generatePointcuts(targetClassName, null, rule)[0])) invoke(invocation, targetClassName, targetObject, rule);
                }
            }
        }
        return invocation.invokeNext();
    }

    /** Injects the necessary behavior.
     * @param invocation the Invocation.
     * @param targetClassName The target Class.
     * @param targetObject The target Object.
     * @param rule The rule to be applied.
     * @throws Throwable if any error occurs.
     * @return output from the next element in the invocation stack.
     */
    private void invoke(MethodInvocation invocation, String targetClassName, Object targetObject, RuleMetaData rule) throws Throwable {
        if (log.isDebugEnabled()) log.debug("Applying " + rule + " on " + targetObject);
        String name = rule.getParameter("name");
        if (!SecurityManager.checkComponentAccess(name)) {
            String str = "Access to component '" + name + "' is required to be able to invoke '" + rule.getParameter("method") + "' on " + targetClassName;
            log.error(str);
            Throwable t = new AccessControlException(str);
            throw handleException(t, targetObject, rule);
        }
    }
}
