package ch.exm.storm.query.relational;

import ch.exm.storm.conditions.arguments.Argument;
import ch.exm.storm.conditions.arguments.EvaluatedArgument;
import ch.exm.storm.query.Query;

public interface RelationalQuery extends Query {

    /**
	 * Evaluates an Argument binding it to this query
	 * @param argument
	 * @return the result of the argument evaluation
	 */
    EvaluatedArgument evaluateArgument(Argument argument);
}
