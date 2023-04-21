package edu.mit.lcs.haystack.adenine.query;

import edu.mit.lcs.haystack.adenine.AdenineConstants;
import edu.mit.lcs.haystack.adenine.AdenineException;
import edu.mit.lcs.haystack.adenine.interpreter.DynamicEnvironment;
import edu.mit.lcs.haystack.adenine.interpreter.Interpreter;
import edu.mit.lcs.haystack.adenine.interpreter.Message;
import edu.mit.lcs.haystack.rdf.RDFNode;
import edu.mit.lcs.haystack.rdf.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @version 	1.0
 * @author		Dennis Quan
 */
public class AdenineConditionHandler implements IConditionHandler {

    public AdenineConditionHandler() {
    }

    /**
	 * @see edu.mit.lcs.haystack.adenine.query.IConditionHandler#resolveCondition(DynamicEnvironment,IQueryEngine, Condition, Resource[], RDFNode[][])
	 */
    public Set resolveCondition(DynamicEnvironment denv, IQueryEngine engine, Condition condition, Resource[] existentials, RDFNode[][] hints) throws AdenineException {
        Interpreter interpreter = new Interpreter(denv.getInstructionSource());
        HashMap namedParams = new HashMap();
        namedParams.put(AdenineConstants.existentials, Arrays.asList(existentials));
        namedParams.put(AdenineConstants.currentResults, hints);
        Message message = new Message(condition.getParameters(), namedParams);
        Set set = (Set) interpreter.callMethod(condition.getFunction(), message, denv).getPrimaryValue();
        HashSet set2 = new HashSet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Object o = i.next();
            if (o instanceof Collection) {
                Collection c = (Collection) o;
                RDFNode[] datum = new RDFNode[c.size()];
                c.toArray(datum);
                set2.add(datum);
            } else {
                set2.add(o);
            }
        }
        return set2;
    }
}
