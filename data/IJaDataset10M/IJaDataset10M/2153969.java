package org.deri.iris.rdb.evaluation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.IEvaluationStrategy;
import org.deri.iris.evaluation.stratifiedbottomup.EvaluationUtilities;
import org.deri.iris.evaluation.stratifiedbottomup.IRuleEvaluator;
import org.deri.iris.evaluation.stratifiedbottomup.IRuleEvaluatorFactory;
import org.deri.iris.factory.Factory;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rdb.facts.IRdbFacts;
import org.deri.iris.rdb.facts.RdbFacts;
import org.deri.iris.rdb.rules.compiler.IRdbCompiledRule;
import org.deri.iris.rdb.rules.compiler.RdbRuleCompiler;
import org.deri.iris.rdb.rules.optimization.EmptyRuleBodyOptimizer;
import org.deri.iris.rdb.storage.FixedSizeRelation;
import org.deri.iris.rdb.storage.IRdbRelation;
import org.deri.iris.rdb.storage.RdbProjectedRelation;
import org.deri.iris.rules.IRuleHeadEqualityPreProcessor;
import org.deri.iris.rules.RuleHeadEqualityRewriter;
import org.deri.iris.rules.compiler.ICompiledRule;
import org.deri.iris.storage.IRelation;
import org.deri.iris.utils.UniqueList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A strategy that uses bottom up evaluation on a stratified rule set. This
 * evaluation strategy uses the {@link RuleHeadEqualityRewriter} technique for
 * rules with rule head equality.
 */
public class RdbStratifiedBottomUpEvaluationStrategy implements IEvaluationStrategy {

    private static final Logger logger = LoggerFactory.getLogger(RdbStratifiedBottomUpEvaluationStrategy.class);

    private final Connection connection;

    protected final Configuration configuration;

    protected final IRdbFacts facts;

    protected final IRuleEvaluatorFactory ruleEvaluatorFactory;

    public RdbStratifiedBottomUpEvaluationStrategy(Connection connection, IFacts facts, List<IRule> rules, IRuleEvaluatorFactory ruleEvaluatorFactory, Configuration configuration) throws EvaluationException {
        this.connection = connection;
        this.ruleEvaluatorFactory = ruleEvaluatorFactory;
        this.configuration = configuration;
        if (!(facts instanceof IRdbFacts)) {
            this.facts = new RdbFacts(connection);
            this.facts.addAll(facts);
            facts = this.facts;
        } else {
            this.facts = (IRdbFacts) facts;
        }
        IRuleHeadEqualityPreProcessor rewriter = new RuleHeadEqualityRewriter();
        rules = rewriter.process(rules, facts);
        configuration.ruleOptimisers.add(new EmptyRuleBodyOptimizer());
        EvaluationUtilities utils = new EvaluationUtilities(configuration);
        List<IRule> safeRules = utils.applyRuleSafetyProcessor(rules);
        List<List<IRule>> stratifiedRules = utils.stratify(safeRules);
        RdbRuleCompiler compiler = new RdbRuleCompiler(connection, facts);
        int stratumNumber = 0;
        for (List<IRule> stratum : stratifiedRules) {
            List<IRule> reorderedRules = utils.reOrderRules(stratum);
            List<IRule> optimisedRules = utils.applyRuleOptimisers(reorderedRules);
            List<ICompiledRule> compiledRules = new ArrayList<ICompiledRule>();
            for (IRule rule : optimisedRules) {
                try {
                    compiledRules.add(compiler.compile(rule));
                } catch (SQLException e) {
                    throw new EvaluationException(e.getLocalizedMessage());
                }
            }
            IRuleEvaluator evaluator = ruleEvaluatorFactory.createEvaluator();
            evaluator.evaluateRules(compiledRules, facts, configuration);
            stratumNumber++;
        }
    }

    @Override
    public IRelation evaluateQuery(IQuery query, List<IVariable> outputVariables) throws EvaluationException {
        if (query == null) {
            throw new IllegalArgumentException("Query must not be null.");
        }
        if (outputVariables == null) {
            throw new IllegalArgumentException("OutputVariables must not be null.");
        }
        RdbRuleCompiler compiler = new RdbRuleCompiler(connection, facts);
        IRdbCompiledRule compiledQuery;
        try {
            compiledQuery = compiler.compile(query);
        } catch (SQLException e) {
            throw new EvaluationException(e.getLocalizedMessage());
        }
        IRdbRelation result = compiledQuery.evaluate();
        ITuple outputTuple = compiledQuery.getOutputTuple();
        UniqueList<IVariable> variables = new UniqueList<IVariable>();
        variables.addAll(outputTuple.getAllVariables());
        ITuple queryTuple = Factory.BASIC.createTuple(new ArrayList<ITerm>(variables));
        outputVariables.clear();
        outputVariables.addAll(variables);
        try {
            if (outputVariables.isEmpty()) {
                return new FixedSizeRelation(result.size());
            }
            return new RdbProjectedRelation(connection, result, queryTuple, outputTuple);
        } catch (SQLException e) {
            logger.error("Failed to create a projected relation for the query " + query, e);
            throw new EvaluationException("Could not create a selection on the output relation");
        }
    }
}
