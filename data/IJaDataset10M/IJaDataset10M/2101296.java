package org.tmapiutils.query.tolog.predicates;

import java.util.List;
import org.tmapiutils.query.tolog.TologProcessingException;
import org.tmapiutils.query.tolog.parser.Variable;
import org.tmapiutils.query.tolog.utils.VariableSet;
import org.tmapi.core.Locator;

/**
 *
 * @author Kal Ahmed (kal[at]techquila.com)
 * @author Lars Heuer (heuer[at]semagia.com)
 */
public class BaseLocatorPredicate extends PredicateBase {

    public static final String PREDICATE_NAME = "base-locator";

    private static final ParameterInfo[] PARAM_INFO = new ParameterInfo[] { new ParameterInfo(true) };

    public ParameterInfo[] getParamInfo() {
        return PARAM_INFO;
    }

    public String getPredicateName() {
        return PREDICATE_NAME;
    }

    public VariableSet matches(List params) throws TologProcessingException {
        Object p = params.get(0);
        VariableSet ret = new VariableSet();
        initialiseResultsSet(ret);
        if (p instanceof Variable) {
            addResultsRow(ret, new Object[] { getTopicMap().getBaseLocator() });
        } else if (p instanceof Locator) {
            if (p.equals(getTopicMap().getBaseLocator())) {
                addResultsRow(ret, new Object[] { p });
            }
        }
        return ret;
    }
}
