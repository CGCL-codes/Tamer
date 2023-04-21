package org.deri.iris.facts;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.storage.IRelation;
import org.deri.iris.storage.simple.SimpleRelationFactory;

public class FiniteUniverseFactsTest extends TestCase {

    public void testGetGroundTermsFromRuleHead() {
        IFacts facts = new Facts(new SimpleRelationFactory());
        List<IRule> rules = new ArrayList<IRule>();
        List<ILiteral> head = new ArrayList<ILiteral>();
        List<ILiteral> body = new ArrayList<ILiteral>();
        List<ITerm> terms = new ArrayList<ITerm>();
        ITerm constructedArgument = CONCRETE.createInteger(8);
        terms.add(CONCRETE.createInteger(7));
        terms.add(TERM.createString("r"));
        terms.add(TERM.createConstruct("f", constructedArgument));
        head.add(BASIC.createLiteral(true, BASIC.createPredicate("p", 3), BASIC.createTuple(terms)));
        terms.add(constructedArgument);
        rules.add(BASIC.createRule(head, body));
        FiniteUniverseFacts fuf = new FiniteUniverseFacts(facts, rules);
        checkContains(terms, fuf.get(FiniteUniverseFacts.UNIVERSE));
    }

    public void testGetGroundTermsFromRuleBody() {
        IFacts facts = new Facts(new SimpleRelationFactory());
        List<IRule> rules = new ArrayList<IRule>();
        List<ILiteral> head = new ArrayList<ILiteral>();
        List<ILiteral> body = new ArrayList<ILiteral>();
        List<ITerm> terms = new ArrayList<ITerm>();
        ITerm constructedArgument = CONCRETE.createInteger(8);
        terms.add(CONCRETE.createInteger(7));
        terms.add(TERM.createString("r"));
        terms.add(TERM.createConstruct("f", constructedArgument));
        body.add(BASIC.createLiteral(true, BASIC.createPredicate("p", 3), BASIC.createTuple(terms)));
        head.add(BASIC.createLiteral(true, BASIC.createPredicate("q", 0), BASIC.createTuple()));
        terms.add(constructedArgument);
        rules.add(BASIC.createRule(head, body));
        FiniteUniverseFacts fuf = new FiniteUniverseFacts(facts, rules);
        checkContains(terms, fuf.get(FiniteUniverseFacts.UNIVERSE));
    }

    public void testGetGroundTermsFromFacts() {
        IFacts facts = new Facts(new SimpleRelationFactory());
        IRelation relation = facts.get(BASIC.createPredicate("q", 1));
        List<ITerm> testTerms = new ArrayList<ITerm>();
        ITerm constructedArgument = CONCRETE.createInteger(8);
        ITerm term = CONCRETE.createInteger(7);
        relation.add(BASIC.createTuple(term));
        testTerms.add(term);
        term = TERM.createString("r");
        relation.add(BASIC.createTuple(term));
        testTerms.add(term);
        term = TERM.createConstruct("f", constructedArgument);
        relation.add(BASIC.createTuple(term));
        testTerms.add(term);
        testTerms.add(constructedArgument);
        FiniteUniverseFacts fuf = new FiniteUniverseFacts(facts, new ArrayList<IRule>());
        checkContains(testTerms, fuf.get(FiniteUniverseFacts.UNIVERSE));
    }

    private void checkContains(List<ITerm> groundTerms, IRelation universe) {
        for (ITerm term : groundTerms) assertTrue(universe.contains(BASIC.createTuple(term)));
    }
}
