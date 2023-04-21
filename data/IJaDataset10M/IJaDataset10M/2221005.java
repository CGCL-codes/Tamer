package org.deri.iris.rdb.evaluation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.evaluation.stratifiedbottomup.IRuleEvaluator;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rdb.facts.IRdbFacts;
import org.deri.iris.rdb.facts.RdbFacts;
import org.deri.iris.rdb.rules.compiler.IRdbCompiledRule;
import org.deri.iris.rdb.storage.IRdbRelation;
import org.deri.iris.rdb.storage.RdbDisjoinedRelation;
import org.deri.iris.rules.compiler.ICompiledRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An impementation of a semi-naive evaluation Datalog programs based on a
 * relational database system. This implementation creates a new relation in the
 * database for each "delta" relation, which allows to evaluate the program
 * using SQL without recursive statements. However, this has the disadvantage of
 * needing to duplicate the tuples of a delta relation, such that these tuples
 * are present in, both, in the delta relation and in the relation of for the
 * predicate. To reduce storage consumption, a delta relation is dropped as soon
 * as it isnot needed anymore, that is after run i + 1 where i is the run, in
 * which the delta relation was created.
 */
public class RdbSemiNaiveEvaluator implements IRuleEvaluator {

    private static final Logger logger = LoggerFactory.getLogger(RdbSemiNaiveEvaluator.class);

    private Connection connection;

    public RdbSemiNaiveEvaluator(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void evaluateRules(List<ICompiledRule> rules, IFacts facts, Configuration configuration) throws EvaluationException {
        IRdbFacts rdbFacts;
        if (!(facts instanceof IRdbFacts)) {
            rdbFacts = new RdbFacts(connection);
            rdbFacts.addAll(facts);
        } else {
            rdbFacts = (IRdbFacts) facts;
        }
        List<IRdbCompiledRule> rdbRules = new ArrayList<IRdbCompiledRule>();
        for (ICompiledRule rule : rules) {
            if (!(rule instanceof IRdbCompiledRule)) {
                throw new IllegalArgumentException("Compiled rules must be instances of IRdbCompiledRule");
            }
            rdbRules.add((IRdbCompiledRule) rule);
        }
        int run = 1;
        IRdbFacts deltas = new RdbFacts(connection, run + "-");
        for (IRdbCompiledRule rule : rdbRules) {
            IRdbRelation delta = rule.evaluate();
            if (delta != null) {
                IPredicate predicate = rule.headPredicate();
                deltas.get(predicate).addAll(delta);
            }
        }
        rdbFacts.addAll(deltas);
        boolean newTuples;
        do {
            run++;
            newTuples = false;
            IRdbFacts previousDeltas = deltas;
            deltas = new RdbFacts(connection, run + "-");
            for (final IRdbCompiledRule rule : rdbRules) {
                IPredicate predicate = rule.headPredicate();
                IRdbRelation programFacts = rdbFacts.get(predicate);
                IRdbRelation delta = rule.evaluateIteratively(previousDeltas);
                if (delta != null) {
                    delta = removeDeducedTuples(programFacts, delta);
                }
                if (delta != null) {
                    IRdbRelation newDelta = deltas.get(predicate);
                    boolean someTuplesWereAdded = newDelta.addAll(delta);
                    if (someTuplesWereAdded) {
                        newTuples = true;
                        programFacts.addAll(newDelta);
                    }
                }
            }
            previousDeltas.dropAll();
        } while (newTuples);
        deltas.dropAll();
    }

    private IRdbRelation removeDeducedTuples(IRdbRelation programFacts, IRdbRelation delta) throws EvaluationException {
        if (delta.getArity() == 0 && delta.size() == 0) {
            return delta;
        }
        if (programFacts.getArity() != delta.getArity()) {
            logger.error("Arity of {} ({}) and {} ({}) does not match", new Object[] { programFacts.getTableName(), String.valueOf(programFacts.getArity()), delta.getTableName(), String.valueOf(delta.getArity()) });
            throw new EvaluationException("Arities of program and delta facts do not match");
        }
        List<List<Integer>> indices = new ArrayList<List<Integer>>();
        for (int i = 0; i < programFacts.getArity(); i++) {
            List<Integer> joinIndices = new ArrayList<Integer>();
            joinIndices.add(i);
            joinIndices.add(programFacts.getArity() + i);
            indices.add(joinIndices);
        }
        try {
            return new RdbDisjoinedRelation(connection, delta, programFacts, indices);
        } catch (SQLException e) {
            logger.error("Could not remove already deduced tuples", e);
            throw new EvaluationException(e.getLocalizedMessage());
        }
    }
}
