package org.drools.jsr94.rules;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.StatelessRuleSession;
import org.drools.FactException;
import org.drools.WorkingMemory;
import org.drools.jsr94.rules.admin.RuleExecutionSetImpl;
import org.drools.jsr94.rules.admin.RuleExecutionSetRepository;

/**
 * The Drools implementation of the <code>StatelessRuleSession</code>
 * interface which is a representation of a stateless rules engine session. A
 * stateless rules engine session exposes a stateless rule execution API to an
 * underlying rules engine.
 * 
 * @see StatelessRuleSession
 * 
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public class StatelessRuleSessionImpl extends AbstractRuleSessionImpl implements StatelessRuleSession {

    /**
	 * Gets the <code>RuleExecutionSet</code> for this URI and associates it
	 * with a RuleBase.
	 * 
	 * @param bindUri
	 *            the URI the <code>RuleExecutionSet</code> has been bound to
	 * @param properties
	 *            additional properties used to create the
	 *            <code>RuleSession</code> implementation.
	 * 
	 * @throws RuleExecutionSetNotFoundException
	 *             if there is no rule set under the given URI
	 */
    StatelessRuleSessionImpl(String bindUri, Map properties, RuleExecutionSetRepository repository) throws RuleExecutionSetNotFoundException {
        super(repository);
        this.setProperties(properties);
        RuleExecutionSetImpl ruleSet = (RuleExecutionSetImpl) repository.getRuleExecutionSet(bindUri);
        if (ruleSet == null) {
            throw new RuleExecutionSetNotFoundException("RuleExecutionSet unbound: " + bindUri);
        }
        this.setRuleExecutionSet(ruleSet);
    }

    /**
	 * Executes the rules in the bound rule execution set using the supplied
	 * list of objects. A <code>List</code> is returned containing the objects
	 * created by (or passed into the rule session) the executed rules that pass
	 * the filter test of the default <code>RuleExecutionSet</code>
	 * <code>ObjectFilter</code>
	 * (if present). <p/> The returned list may not neccessarily include all
	 * objects passed, and may include <code>Object</code>s created by
	 * side-effects. The execution of a <code>RuleExecutionSet</code> can add,
	 * remove and update objects. Therefore the returned object list is
	 * dependent on the rules that are part of the executed
	 * <code>RuleExecutionSet</code> as well as Drools specific rule engine
	 * behavior.
	 * 
	 * @param objects
	 *            the objects used to execute rules.
	 * 
	 * @return a <code>List</code> containing the objects as a result of
	 *         executing the rules.
	 * 
	 * @throws InvalidRuleSessionException
	 *             on illegal rule session state.
	 */
    public List executeRules(List objects) throws InvalidRuleSessionException {
        return this.executeRules(objects, this.getRuleExecutionSet().getObjectFilter());
    }

    /**
	 * Executes the rules in the bound rule execution set using the supplied
	 * list of objects. A <code>List</code> is returned containing the objects
	 * created by (or passed into the rule engine) the executed rules and
	 * filtered with the supplied object filter. <p/> The returned list may not
	 * neccessarily include all objects passed, and may include
	 * <code>Object</code>s created by side-effects. The execution of a
	 * <code>RuleExecutionSet</code> can add, remove and update objects.
	 * Therefore the returned object list is dependent on the rules that are
	 * part of the executed <code>RuleExecutionSet</code> as well as Drools
	 * specific rule engine behavior.
	 * 
	 * @param objects
	 *            the objects used to execute rules.
	 * @param filter
	 *            the object filter.
	 * 
	 * @return a <code>List</code> containing the objects as a result of
	 *         executing rules, after passing through the supplied object
	 *         filter.
	 * 
	 * @throws InvalidRuleSessionException
	 *             on illegal rule session state.
	 */
    public List executeRules(List objects, ObjectFilter filter) throws InvalidRuleSessionException {
        WorkingMemory workingMemory = this.newWorkingMemory();
        try {
            for (Iterator objectIter = objects.iterator(); objectIter.hasNext(); ) {
                workingMemory.assertObject(objectIter.next());
            }
            workingMemory.fireAllRules();
        } catch (FactException e) {
            throw new InvalidRuleSessionException(e.getMessage(), e);
        }
        List results = workingMemory.getObjects();
        this.applyFilter(results, filter);
        return results;
    }
}
