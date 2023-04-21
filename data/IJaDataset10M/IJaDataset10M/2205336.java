package alt.jiapi;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Category;

/**
 * This class holds information needed by the instrumentation process. It has a
 * List of Instrumentations. The Instrumentations define the instrumentation
 * function.
 * <p>
 * 
 * InstrumentationDescriptor holds those Instrumentations which use the same
 * Rules. Rules define which classes is to be instrumented.
 * <p>
 * 
 * @see Instrumentation
 * 
 * @author Mika Riekkinen
 * @author Joni Suominen
 * @version $Revision: 1.17 $ $Date: 2011/08/19 12:47:08 $
 */
public class InstrumentationDescriptor {

    private static Category log = Runtime.getLogCategory(InstrumentationDescriptor.class);

    /**
	 * A list of Instrumentations.
	 */
    private List<Instrumentor> instrumentors = Collections.synchronizedList(new ArrayList<Instrumentor>());

    /**
	 * Inclusion rules in significance order,
	 */
    private Set<Rule> inclusionRules = new TreeSet<Rule>(Collections.reverseOrder());

    /**
	 * Exclusion rules in significance order,
	 */
    private Set<Rule> exclusionRules = new TreeSet<Rule>(Collections.reverseOrder());

    public InstrumentationDescriptor() {
    }

    /**
	 * Adds a new chain.
	 * 
	 * @param i
	 *            Instrumentor that is used in instrumenting
	 * @param patch
	 *            Patch, which is associated with the Instrumentor given
	 */
    public void addInstrumentor(Instrumentor instrumentor) {
        instrumentors.add(instrumentor);
    }

    /**
	 * Gets all the Instrumentations.
	 * 
	 * @return An ordered List of Instrumentations.
	 */
    public List<Instrumentor> getInstrumentors() {
        return instrumentors;
    }

    /**
	 * Adds a rule to this descriptor. Each Instrumentor - Patch pair is joined
	 * with this rule. When instrumenting, if inclusion rule matches a
	 * class/method that is loaded by the ClassLoader, Instrumentors, that are
	 * registered to this descriptor by <b>addBinding(Instrumentor, Patch)</b>
	 * are called with bytecode, that reflects the method. Instrumentor is then
	 * supposed to modify that class somehow.
	 * <p>
	 * Format of the rule is the same, as fully qualified Java
	 * package/class/method names. For example, following are valid rules:
	 * <p>
	 * <ul>
	 * <li>my.package.*
	 * <li>my.package.SomeClass
	 * <li>my.*.SomeClass
	 * <li>my.package.SomeClass.someMethod()V
	 * </ul>
	 * 
	 * @param rule
	 *            A Inclusion rule, that is to be added to this descriptor
	 * @exception JiapiException
	 *                thrown if there's a syntax error in rule
	 */
    public void addInclusionRule(String rule) throws JiapiException {
        inclusionRules.add(new Rule(rule));
    }

    /**
	 * Gets the inclusion rules of this descriptor.
	 * 
	 * @return a Set of inclusion rules
	 */
    public Set<Rule> getInclusionsRules() {
        return inclusionRules;
    }

    /**
	 * Adds an exclusion rule to this descriptor. When choosing whether a
	 * class/method is to be insturmented, if exclusion rule matches, that
	 * class/method will be skipped from instrumentation.
	 * <p>
	 * In general Exclusion rules are more powerful than inclusion rules. That
	 * is, if for some reason two similar rules are assigned to both exclusion
	 * and inclusion rules, exclusion rule will win.
	 * 
	 * @param rule
	 *            A Exclusion rule, that is to be added to this descriptor
	 * @exception JiapiException
	 *                thrown if there's a syntax error in rule
	 */
    public void addExclusionRule(String rule) throws JiapiException {
        exclusionRules.add(new Rule(rule));
    }

    /**
	 * Gets the inclusion rules of this descriptor.
	 * 
	 * @return a Set of inclusion rules
	 */
    public Set<Rule> getExclusionsRules() {
        return exclusionRules;
    }

    /**
	 * Check whether the given className matches any of given rules. Excplicit
	 * exclusion rules win if they are more significant than inclusion rules.
	 * 
	 * @param className
	 *            a class name
	 */
    public boolean match(String className) {
        log.debug("Matching rules to " + className);
        Rule exclusionRule = null;
        Rule inclusionRule = null;
        for (Iterator<Rule> i = inclusionRules.iterator(); i.hasNext(); ) {
            Rule rule = i.next();
            if (rule.match(className)) {
                inclusionRule = rule;
                break;
            }
        }
        if (inclusionRule == null) {
            log.debug("Excluding: " + className + " cause: no match");
            return false;
        }
        for (Iterator<Rule> i = exclusionRules.iterator(); i.hasNext(); ) {
            Rule rule = i.next();
            if (rule.match(className)) {
                exclusionRule = rule;
                break;
            }
        }
        if (exclusionRule == null) {
            log.debug("Including: " + className + " cause: " + inclusionRule + ", no exclusion rule");
            return true;
        }
        if (inclusionRule.isMoreSignificant(exclusionRule)) {
            log.debug("Including: " + className + " cause:\n  include " + inclusionRule + " is more significant than " + exclusionRule);
            return true;
        } else {
            log.debug("Excluding: " + className + " cause:\n  excluding " + exclusionRule + " is more significant than " + inclusionRule);
            return false;
        }
    }

    public String toString() {
        return inclusionRules.toString() + ", " + exclusionRules.toString();
    }
}
