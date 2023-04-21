package org.deri.iris.evaluation;

import java.util.List;
import org.deri.iris.EvaluationException;
import org.deri.iris.ProgramNotStratifiedException;
import org.deri.iris.RuleUnsafeException;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.storage.IRelation;

/**
 * Interface for all evaluators that accept queries and return a result set as a relation.
 */
public interface IEvaluator {

    /**
	 * Evaluate a query.
	 * @param query The query to evaluate.
	 * @return The relation of results.
	 * @throws ProgramNotStratifiedException If the program (knowledge-base)can not be stratified
	 * @throws RuleUnsafeException If the program (knowledge-base) contains an unsafe rule.
	 * @throws EvaluationException If the avaluation fails for any other reason.
	 */
    IRelation evaluateQuery(IQuery query) throws ProgramNotStratifiedException, RuleUnsafeException, EvaluationException;

    /**
	 * Evaluate a query and optionally return the variable bindings.
	 * @param query The query to evaluate.
	 * @param outputVariables If this is not null, it will be filled with the variable bindings
	 * of the result relation, i.e. there will be one variable instance for each term
	 * (in one row) of the results set
	 * @return The relation of results.
	 * @throws ProgramNotStratifiedException If the program (knowledge-base)can not be stratified
	 * @throws RuleUnsafeException If the program (knowledge-base) contains an unsafe rule.
	 * @throws EvaluationException If the avaluation fails for any other reason.
	 */
    IRelation evaluateQuery(IQuery query, List<IVariable> outputVariables) throws ProgramNotStratifiedException, RuleUnsafeException, EvaluationException;
}
