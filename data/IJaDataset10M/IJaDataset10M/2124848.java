package org.deri.iris.evaluation.seminaive;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.evaluation.MiscOps;

/**
 * Algorithm 3.4: Semi-Naive Evaluation of Datalog Equations
 * 
 * INPUT: A collection of rectified datalog rules with EDB predicates r1,...rk
 * and IDB predicates p1,...,pm. Also, a list of relations R1, ..., Rk to serve
 * as values of the EDB predicates.
 * 
 * OUTPUT: The least fixed point solution to the datalog equations obtained from
 * these rules.
 * 
 * <p>
 * $Id$
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 05-sep-2006
 */
public class SeminaiveEvaluation extends GeneralSeminaiveEvaluation {

    public SeminaiveEvaluation(IExpressionEvaluator e, IProgram program) {
        super(e, program);
    }

    /**
	 * Algorithm from Ullman, Principles of Database and Knowledge-base Systems, page 127
	 */
    public boolean evaluate() {
        for (int i = 1, maxStrat = MiscOps.getMaxStratum(mProgram, this.idbMap.keySet()); i <= maxStrat; i++) {
            Map<IPredicate, IMixedDatatypeRelation> dP = new HashMap<IPredicate, IMixedDatatypeRelation>();
            Set<IPredicate> preds = MiscOps.getPredicatesOfStratum(mProgram, this.idbMap.keySet(), i);
            for (final IPredicate pr : preds) {
                IMixedDatatypeRelation dPi = method.evaluate(idbMap.get(pr), mProgram);
                mProgram.addFacts(pr, dPi);
            }
            boolean newTuples = true;
            while (newTuples) {
                newTuples = false;
                for (final IPredicate pr : preds) {
                    IMixedDatatypeRelation dPi = method.evaluateIncrementally(idbMap.get(pr), mProgram, dP);
                    if (dPi.size() > 0) {
                        IMixedDatatypeRelation programFacts = mProgram.getFacts(pr);
                        if (programFacts != null) {
                            if (!programFacts.containsAll(dPi)) {
                                removeDeducedTuples(dPi, programFacts);
                                dP.put(pr, dPi);
                                newTuples = true;
                            }
                        }
                    }
                    if (!newTuples) dP.remove(pr);
                }
                Iterator<Map.Entry<IPredicate, IMixedDatatypeRelation>> iterdP = dP.entrySet().iterator();
                while (iterdP.hasNext()) {
                    Map.Entry<IPredicate, IMixedDatatypeRelation> e = iterdP.next();
                    mProgram.addFacts(e.getKey(), e.getValue());
                }
            }
        }
        return true;
    }

    /**<p>
	 * Removes tuples from the last iteration that have already been deduced 
	 * (in the previous iterations)
	 * </p>
	 * <p>
	 * It seems that use of the removeDeducedTuples method is more efficient than: 
	 * p.removeAll(this.p.getFacts(pr));
	 * However this statement needs to be checked more carefully (once tests for performance are done).
	 * </p>
	 * @param r0	Relation containing tuples derived during the last iteration. 
	 * @param r1	Relation containing tuples derived during the previous iteration .
	 */
    private void removeDeducedTuples(IMixedDatatypeRelation r0, IMixedDatatypeRelation r1) {
        Iterator<ITuple> it = r0.iterator();
        while (it.hasNext()) {
            if (r1.contains(it.next())) it.remove();
        }
    }
}
