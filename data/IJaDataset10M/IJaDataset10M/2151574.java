package ws.prova;

import ws.prova.reference.IProvaKnowledgeBase;
import ws.prova.reference.RBSLARobinsonsUnificationAlgorithm;
import ws.prova.util.DefaultLogicFactorySupport;
import org.mandarax.kernel.*;
import org.mandarax.reference.TmpClause;
import org.mandarax.reference.Resolution;
import org.mandarax.util.ClauseIterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Updater {

    reagent reagent;

    private IProvaKnowledgeBase kb;

    static final SimplePredicate state = HelperPredicates.state();

    static final SimplePredicate derive = HelperPredicates.derive();

    final UnificationAlgorithm unificationAlgorithm = new RBSLARobinsonsUnificationAlgorithm();

    public Updater(reagent reagent, IProvaKnowledgeBase kb) {
        this.reagent = reagent;
        this.kb = kb;
    }

    /**
     * Dynamically assert a new fact.
	 * Both meta/spec state predicates and regular derive predicates are supported.
	 *
	 * @param r Parameters including only one: ComplexTerm to be asserted
	 * @param reagent
	 * @return Always <code>true</code>
	 */
    boolean prova_assert(Object[] r, reagent reagent) {
        ComplexTerm ct = (ComplexTerm) r[0];
        String key = (String) ((ConstantTerm) ct.getTerms()[1]).getObject();
        if (key.equals("meta") || key.equals("spec")) {
            kb.add(DefaultLogicFactorySupport.fact(reagent.state, ct));
        } else {
            kb.add(DefaultLogicFactorySupport.fact(kb.poly(key, reagent.derive), key, ct));
        }
        return true;
    }

    /**
	 * Dynamically assert a new fact before all other facts.
	 * Both meta/spec state predicates and regular derive predicates are supported.
	 *
	 * @param r Parameters including only one: ComplexTerm to be asserted
	 * @param reagent
	 * @return Always <code>true</code>
	 */
    boolean prova_asserta(Object[] r, reagent reagent) {
        ComplexTerm ct = (ComplexTerm) r[0];
        String key = (String) ((ConstantTerm) ct.getTerms()[1]).getObject();
        if (key.equals("meta") || key.equals("spec")) {
            kb.add(DefaultLogicFactorySupport.fact(reagent.state, ct));
            kb.moveToTop(DefaultLogicFactorySupport.fact(reagent.state, ct));
        } else {
            kb.add(DefaultLogicFactorySupport.fact(kb.poly(key, reagent.derive), key, ct));
            kb.moveToTop(DefaultLogicFactorySupport.fact(kb.poly(key, reagent.derive), key, ct));
        }
        return true;
    }

    /**
	 * Dynamically insert a new fact.
	 * If a fact or a rule with header subsumed by the supplied fact exists in the KB,
	 * the method does nothing and returns <code>true</code>.
	 * Both meta/spec state predicates and regular derive predicates are supported.
	 *
	 * @param r Parameters including only one: ComplexTerm to be asserted
	 * @param reagent
	 * @return Always <code>true</code>
	 */
    boolean prova_update_fact(Object[] r, reagent reagent) throws ClauseSetException {
        if (r.length == 1) {
            retractall(r[0]);
            prova_assert(r, reagent);
            return true;
        } else if (r.length == 2) {
            retractall(r[0]);
            prova_assert(new Object[] { r[1] }, reagent);
        }
        return true;
    }

    /**
	 * Retract one fact or a rule with a header that is subsumed by the pattern of the supplied fact.
	 * This method does not remove any rules.
	 *
	 * @param f Fact a fact holding a pattern of a fact to be removed
	 */
    protected boolean remove_fact(Fact f) throws ClauseSetException {
        List lit = new ArrayList(1);
        lit.add(f);
        TmpClause nextClause = new TmpClause();
        nextClause.negativeLiterals = lit;
        nextClause.positiveLiterals = new ArrayList();
        ClauseIterator e = kb.clauses(nextClause, null);
        while (e.hasNext()) {
            Object o = e.next();
            if (o instanceof Fact) {
                Fact cs = (Fact) o;
                if (Resolution.subsumes(f, cs, unificationAlgorithm)) {
                    kb.remove(cs);
                    return true;
                }
            } else if (o instanceof Rule) {
                Rule r = (Rule) o;
                if (Resolution.subsumes(f, r.getHead(), unificationAlgorithm)) {
                    kb.remove(r);
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Dynamically retract one fact or rule with header subsumed by the specified pattern.
	 * Both meta/spec state predicates and regular derive predicates are supported.
	 * If at least one fact is not found, the method returns Boolean.FALSE.
	 *
	 * @param r Object[] Parameters including only one: ComplexTerm to be retracted
	 * @return true if a fact was retracted
	 */
    boolean retract(Object[] r) throws ClauseSetException {
        ComplexTerm ct = (ComplexTerm) r[0];
        String key = (String) ((ConstantTerm) ct.getTerms()[1]).getObject();
        boolean removed = false;
        if (key.equals("meta") || key.equals("spec")) {
            removed = remove_fact(DefaultLogicFactorySupport.fact(state, ct));
        } else {
            removed = remove_fact(DefaultLogicFactorySupport.fact(kb.poly(key, derive), key, ct));
        }
        return removed;
    }

    /**
	 * Dynamically retract all facts or rules with headers subsumed by the specified pattern.
	 * Both meta/spec state predicates and regular derive predicates are supported.
	 *
	 * @param r The pattern as either
	 *          <br> a <code>ComplexTerm</code> for the facts to be retracted
	 *          <br> or a <code>String</code> representing the rulebase source for which all facts and rules are to be retracted
	 */
    void retractall(Object r) throws ClauseSetException {
        if (r instanceof String) {
            removeall_facts((String) r);
            return;
        }
        ComplexTerm ct = (ComplexTerm) r;
        String key = (String) ((ConstantTerm) ct.getTerms()[1]).getObject();
        if (key.equals("meta") || key.equals("spec")) {
            removeall_facts(DefaultLogicFactorySupport.fact(state, ct));
        } else {
            removeall_facts(DefaultLogicFactorySupport.fact(kb.poly(key, derive), key, ct));
        }
    }

    /**
	 * Retract all facts or rules subsumed by a fact serving as a pattern.
	 *
	 * @param f Fact a fact holding a pattern of facts to be removed
	 */
    public void removeall_facts(Fact f) throws ClauseSetException {
        List lit = new ArrayList(1);
        lit.add(f);
        TmpClause nextClause = new TmpClause();
        nextClause.negativeLiterals = lit;
        nextClause.positiveLiterals = new ArrayList();
        ClauseIterator e = kb.clauses(nextClause, null);
        while (e.hasNext()) {
            Object o = e.next();
            if (o instanceof Fact) {
                Fact cs = (Fact) o;
                if (Resolution.subsumes(f, cs, unificationAlgorithm)) {
                    kb.remove(cs);
                }
            } else if (o instanceof Rule) {
                Rule r = (Rule) o;
                if (Resolution.subsumes(f, r.getHead(), unificationAlgorithm)) {
                    kb.remove(r);
                }
            }
        }
    }

    /**
	 * Retract all facts or rules having a src representing the source of the consulted rulebase.
	 *
	 * @param src The source of the consulted rulebase
	 */
    public void removeall_facts(String src) {
        removeall_facts("src", src);
    }

    /**
	 * Retract all facts or rules having the specified value in the also
	 * specified property.
	 * 
	 * @param name
	 *            Property name
	 * @param value
	 *            Property value
	 * @return True, if (at least) one rule or fact with specied property was
	 *         removed
	 */
    public boolean removeall_facts(String name, String value) {
        boolean successful = false;
        List list = kb.getClauseSets();
        for (Iterator e = list.iterator(); e.hasNext(); ) {
            Object o = e.next();
            if (o instanceof Fact) {
                Fact f = (Fact) o;
                if (f.getProperty(name) != null && f.getProperty(name).equals(value)) {
                    successful |= kb.remove(f);
                }
            } else if (o instanceof Rule) {
                Rule r = (Rule) o;
                if (r.getProperty(name) != null && r.getProperty(name).equals(value)) {
                    successful |= kb.remove(r);
                }
            }
        }
        return successful;
    }

    /**
	 * Dynamically insert a new fact.
	 * If a fact or a rule with header subsumed by the supplied fact exists in the KB,
	 * the method does nothing and returns <code>true</code>.
	 * Both meta/spec state predicates and regular derive predicates are supported.
	 *
	 * @param r Parameters including only one: ComplexTerm to be asserted
	 * @return Always <code>true</code>
	 */
    boolean prova_insert(Object[] r) throws ClauseSetException {
        ComplexTerm ct = (ComplexTerm) r[0];
        String key = (String) ((ConstantTerm) ct.getTerms()[1]).getObject();
        Fact f = null;
        if (key.equals("meta") || key.equals("spec")) {
            f = DefaultLogicFactorySupport.fact(state, ct);
        } else {
            f = DefaultLogicFactorySupport.fact(kb.poly(key, derive), key, ct);
        }
        List lit = new ArrayList(1);
        lit.add(f);
        TmpClause nextClause = new TmpClause();
        nextClause.negativeLiterals = lit;
        nextClause.positiveLiterals = new ArrayList();
        ClauseIterator e = kb.clauses(nextClause, null);
        while (e.hasNext()) {
            Object o = e.next();
            if (o instanceof Fact) {
                Fact cs = (Fact) o;
                if (Resolution.subsumes(f, cs, unificationAlgorithm)) {
                    return true;
                }
            } else if (o instanceof Rule) {
                Rule rule = (Rule) o;
                if (Resolution.subsumes(f, rule.getHead(), unificationAlgorithm)) {
                    return true;
                }
            }
        }
        kb.add(f);
        return true;
    }
}
