package org.deri.iris.api.evaluation.algebra;

import java.util.Map;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.storage.IMixedDatatypeRelation;

/**
 * <p>
 * Represents an evaluator of a relational algebra expression.
 * This interface is supposed to be implemented whenever an 
 * evaluator for relational algebra expressions is needed, 
 * regardless whether the evaluation is conducted accessing
 * the DB data or in-memory data only.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date Dec 18, 2006
 * 
 */
public interface IExpressionEvaluator {

    /**
	 * Evaluates the relational algebra expression.
	 * 
	 * @param c
	 * 			A relational algebra expression to be evaluated/executed.
	 * @param program
	 * 			The entire program including idb and edb relations 
	 *          required for the evaluation of c.
	 * @return	Implicit tuples derived after the evaluation of c.
	 */
    public IMixedDatatypeRelation evaluate(IComponent c, IProgram program);

    /**
	 * Evaluates the relational algebra expression incrementally.
	 * 
	 * @param c
	 * 			A relational algebra expression to be evaluated/executed.
	 * @param p
	 * 			The entire program including idb and edb relations 
	 *          required for the evaluation of c.
	 * @param aq
	 * 			IDB literals and corresponding relations with tuples 
	 * 			derived during the previous evaluation round.
	 * @return	Implicit tuples derived after the evaluation of c.
	 */
    public IMixedDatatypeRelation evaluateIncrementally(IComponent c, IProgram p, Map<IPredicate, IMixedDatatypeRelation> aq);
}
