package org.deri.iris.rules.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.utils.UniqueList;

/**
 * A collection of utility methods for term/tuple matching and variable substitution.
 */
public class TermMatcher {

    /**
	 * Extract the variables in the same order that they are discovered during term matching.
	 * @param subGoalTuple The tuple as it appears in the sub-goal
	 * @param unique true, if only unique variables are required
	 * @return The list of variables occurring in subGoalTuple
	 */
    public static List<IVariable> getVariables(ITuple subGoalTuple, boolean unique) {
        List<IVariable> variables;
        if (unique) variables = new UniqueList<IVariable>(); else variables = new ArrayList<IVariable>();
        for (ITerm term : subGoalTuple) {
            if (term instanceof IVariable) variables.add((IVariable) term); else if (term instanceof IConstructedTerm) getVariables((IConstructedTerm) term, variables);
        }
        return variables;
    }

    /**
	 * Recursive helper for constructed terms.
	 * @param constructed The constructed term to find the variables for.
	 * @param variables The map to put the variables as they are found.
	 */
    private static void getVariables(IConstructedTerm constructed, List<IVariable> variables) {
        for (ITerm term : constructed.getParameters()) {
            if (term instanceof IVariable) variables.add((IVariable) term); else if (term instanceof IConstructedTerm) getVariables((IConstructedTerm) term, variables);
        }
    }

    /**
	 * Match a tuple to view criteria.
	 * If a match occurs, return a tuple with values for each distinct variable in the vire criteria.
	 * @param viewCriteria The tuple from a sub-goal instance.
	 * @param relation The tuple from an EDB relation.
	 * @return The tuple of values for the view's variables or null if a match did not occur.
	 */
    public static ITuple matchTuple(ITuple viewCriteria, ITuple relation) {
        Map<IVariable, ITerm> variableMap = new HashMap<IVariable, ITerm>();
        List<ITerm> terms = new ArrayList<ITerm>();
        for (int i = 0; i < viewCriteria.size(); ++i) {
            ITerm bodyTerm = viewCriteria.get(i);
            ITerm relationTerm = relation.get(i);
            if (!matchTermOfTuple(bodyTerm, relationTerm, variableMap, terms)) return null;
        }
        return Factory.BASIC.createTuple(terms);
    }

    /**
	 * Helper for matching terms of a tuple.
	 * @param viewTerm The term from the rule's sub-goal tuple. 
	 * @param relationTerm The term from the relation's tuple.
	 * @param variableMap The current map of variable-constant bindings.
	 * @param terms The bound variable values in the order in which they are found.
	 * @return true, if these terms match and are consistent with previous macthed term pairs.
	 */
    private static boolean matchTermOfTuple(ITerm viewTerm, ITerm relationTerm, Map<IVariable, ITerm> variableMap, List<ITerm> terms) {
        if (viewTerm instanceof IVariable) {
            IVariable variable = (IVariable) viewTerm;
            ITerm mappedGroundTerm = variableMap.get(variable);
            if (mappedGroundTerm == null) {
                variableMap.put(variable, relationTerm);
                terms.add(relationTerm);
                return true;
            } else if (mappedGroundTerm.equals(relationTerm)) {
                return true;
            } else {
                return false;
            }
        } else if (viewTerm instanceof IConstructedTerm) {
            IConstructedTerm bodyConstructedTerm = (IConstructedTerm) viewTerm;
            if (relationTerm instanceof IConstructedTerm) {
                IConstructedTerm relationConstructedTerm = (IConstructedTerm) relationTerm;
                if (!bodyConstructedTerm.getFunctionSymbol().equals(relationConstructedTerm.getFunctionSymbol())) return false;
                List<ITerm> bodyTerms = bodyConstructedTerm.getParameters();
                List<ITerm> relationTerms = relationConstructedTerm.getParameters();
                if (bodyTerms.size() != relationTerms.size()) return false;
                for (int i = 0; i < bodyTerms.size(); ++i) {
                    if (!matchTermOfTuple(bodyTerms.get(i), relationTerms.get(i), variableMap, terms)) return false;
                }
                return true;
            } else return false;
        } else {
            return viewTerm.equals(relationTerm);
        }
    }

    /**
	 * Attempts to match the rule-body predicate tuple (sub-goal expression) with a tuple
	 * from the associated relation.
	 * @param body The sub-goal tuple
	 * @param relation The relation tuple
	 * @return
	 */
    public static Map<IVariable, ITerm> match(ITuple body, ITuple relation) {
        assert body.size() == relation.size();
        Map<IVariable, ITerm> variableMap = new HashMap<IVariable, ITerm>();
        for (int i = 0; i < body.size(); ++i) {
            ITerm bodyTerm = body.get(i);
            ITerm relationTerm = relation.get(i);
            if (!match(bodyTerm, relationTerm, variableMap)) return null;
        }
        return variableMap;
    }

    /**
	 * Indicates if bodyTerm (containing >= 0 variables) matches groundTerm and is
	 * also consistent with previous matched terms, i.e. variables map to the same ground terms. 
	 * @param bodyTerm The term from the sub-goal predicate
	 * @param relationTerm The term from the relation tuple
	 * @param variableMap The map of variable to ground term map
	 * @return true if the match succeeds, false otherwise
	 */
    public static boolean match(ITerm bodyTerm, ITerm relationTerm, Map<IVariable, ITerm> variableMap) {
        if (bodyTerm instanceof IVariable) {
            IVariable variable = (IVariable) bodyTerm;
            ITerm mappedGroundTerm = variableMap.get(variable);
            if (mappedGroundTerm == null) {
                variableMap.put(variable, relationTerm);
                return true;
            } else if (mappedGroundTerm.equals(relationTerm)) {
                return true;
            } else {
                return false;
            }
        } else if (bodyTerm instanceof IConstructedTerm) {
            IConstructedTerm bodyConstructedTerm = (IConstructedTerm) bodyTerm;
            if (relationTerm instanceof IConstructedTerm) {
                IConstructedTerm relationConstructedTerm = (IConstructedTerm) relationTerm;
                if (!bodyConstructedTerm.getFunctionSymbol().equals(relationConstructedTerm.getFunctionSymbol())) return false;
                List<ITerm> bodyTerms = bodyConstructedTerm.getParameters();
                List<ITerm> relationTerms = relationConstructedTerm.getParameters();
                if (bodyTerms.size() != relationTerms.size()) return false;
                for (int i = 0; i < bodyTerms.size(); ++i) {
                    if (!match(bodyTerms.get(i), relationTerms.get(i), variableMap)) return false;
                }
                return true;
            } else return false;
        } else {
            return bodyTerm.equals(relationTerm);
        }
    }

    /**
	 * Substitute the variable bindings in to a tuple to ground it.
	 * @param tuple The tuple containing variables to ground.
	 * @param variableMap The variable bindings to use.
	 * @return The grounded tuple.
	 */
    public static ITuple substituteVariablesInToTuple(ITuple tuple, Map<IVariable, ITerm> variableMap) {
        List<ITerm> substitutedTerms = new ArrayList<ITerm>();
        for (ITerm headTerm : tuple) {
            ITerm substitutedTerm = substituteVariablesInToTerm(headTerm, variableMap);
            substitutedTerms.add(substitutedTerm);
        }
        return Factory.BASIC.createTuple(substitutedTerms);
    }

    /**
	 * Substitute the variable bindings in to a term to ground it.
	 * @param term The term to ground.
	 * @param variableMap The variable bindings to use.
	 * @return The grounded term.
	 */
    private static ITerm substituteVariablesInToTerm(ITerm term, Map<IVariable, ITerm> variableMap) {
        if (term.isGround()) return term;
        if (term instanceof IVariable) {
            IVariable variable = (IVariable) term;
            ITerm groundTerm = variableMap.get(variable);
            assert groundTerm != null;
            return groundTerm;
        }
        assert term instanceof IConstructedTerm;
        IConstructedTerm constructedTerm = (IConstructedTerm) term;
        List<ITerm> substitutedChildTerms = new ArrayList<ITerm>();
        for (ITerm childTerm : constructedTerm.getParameters()) substitutedChildTerms.add(substituteVariablesInToTerm(childTerm, variableMap));
        return Factory.TERM.createConstruct(constructedTerm.getFunctionSymbol(), substitutedChildTerms);
    }

    /**
	 * Silly helper to create a mutable integer that can be passed by reference.
	 */
    private static class MutableInteger {

        int mValue;
    }

    /**
	 * Substitute variable values in to a tuple to ground it
	 * using a list of terms with indices instead of a variable-term map.
	 * @param tuple The tuple containing variables to ground.
	 * @param variableValues The variable values to be substituted.
	 * @param indices The indices in to variableValues for each occurence of a variable
	 * in the tuple IN THE ORDER IN WHICH THEY ARE FOUND.
	 * @return The grounded tuple.
	 */
    public static ITuple substituteVariablesInToTuple(ITuple tuple, List<ITerm> variableValues, int[] indices) {
        List<ITerm> substitutedTerms = new ArrayList<ITerm>();
        MutableInteger bindingIndex = new MutableInteger();
        for (ITerm headTerm : tuple) {
            ITerm substitutedTerm = substituteVariablesInToTupleTerm(headTerm, variableValues, indices, bindingIndex);
            substitutedTerms.add(substitutedTerm);
        }
        return Factory.BASIC.createTuple(substitutedTerms);
    }

    /**
	 * Helper.
	 * @param term The term to substitute in to.
	 * @param variableValues The variable value list.
	 * @param indices The indices in to variableValues for each occurence of a variable.
	 * @param bindingIndex Indicates the next index to use.
	 * @return The grounded term.
	 */
    private static ITerm substituteVariablesInToTupleTerm(ITerm term, List<ITerm> variableValues, int[] indices, MutableInteger bindingIndex) {
        if (term.isGround()) return term;
        if (term instanceof IVariable) {
            ITerm groundTerm = variableValues.get(indices[bindingIndex.mValue++]);
            assert groundTerm != null;
            return groundTerm;
        }
        assert term instanceof IConstructedTerm;
        IConstructedTerm constructedTerm = (IConstructedTerm) term;
        List<ITerm> substitutedChildTerms = new ArrayList<ITerm>();
        for (ITerm childTerm : constructedTerm.getParameters()) substitutedChildTerms.add(substituteVariablesInToTupleTerm(childTerm, variableValues, indices, bindingIndex));
        return Factory.TERM.createConstruct(constructedTerm.getFunctionSymbol(), substitutedChildTerms);
    }
}
