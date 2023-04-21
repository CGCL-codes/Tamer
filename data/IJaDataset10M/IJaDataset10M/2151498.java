package org.deri.iris.evaluation.common;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;
import static org.deri.iris.MiscHelper.createLiteral;
import static org.deri.iris.MiscHelper.createVarList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.evaluation.common.IAdornedPredicate;
import org.deri.iris.api.evaluation.common.IAdornedProgram;
import org.deri.iris.api.evaluation.common.IAdornedRule;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.compiler.Parser;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedRule;
import org.deri.iris.evaluation.magic.SIPImpl;
import org.deri.iris.factory.Factory;
import org.deri.iris.MiscHelper;

/**
 * <p>
 * Tests the adornments.
 * </p>
 * <p>
 * $Id: AdornmentsTest.java,v 1.5 2007-04-16 14:54:07 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.5 $
 */
public class AdornmentsTest extends TestCase {

    private IAdornedProgram p0;

    private IAdornedProgram p1;

    public static final Comparator<IRule> RC = new RuleComparator();

    public static Test suite() {
        return new TestSuite(AdornmentsTest.class, AdornmentsTest.class.getSimpleName());
    }

    public void setUp() {
        Set<IRule> rules = new HashSet<IRule>();
        IHead head = BASIC.createHead(createLiteral("sg", "X", "Y"));
        IBody body = BASIC.createBody(createLiteral("flat", "X", "Y"));
        rules.add(BASIC.createRule(head, body));
        head = BASIC.createHead(createLiteral("sg", "X", "Y"));
        List<ILiteral> bodyLiterals = new ArrayList<ILiteral>();
        bodyLiterals.add(createLiteral("up", "X", "Z1"));
        bodyLiterals.add(createLiteral("sg", "Z1", "Z2"));
        bodyLiterals.add(createLiteral("flat", "Z2", "Z3"));
        bodyLiterals.add(createLiteral("sg", "Z3", "Z4"));
        bodyLiterals.add(createLiteral("down", "Z4", "Y"));
        body = BASIC.createBody(bodyLiterals);
        rules.add(BASIC.createRule(head, body));
        IQuery query0 = BASIC.createQuery(BASIC.createLiteral(true, BASIC.createPredicate("sg", 2), BASIC.createTuple(TERM.createString("john"), TERM.createVariable("Y"))));
        p0 = new AdornedProgram(rules, query0);
        rules = new HashSet<IRule>();
        head = BASIC.createHead(createLiteral("rsg", "X", "Y"));
        body = BASIC.createBody(createLiteral("flat", "X", "Y"));
        rules.add(BASIC.createRule(head, body));
        head = BASIC.createHead(createLiteral("rsg", "X", "Y"));
        bodyLiterals = new ArrayList<ILiteral>();
        bodyLiterals.add(createLiteral("up", "X", "X1"));
        bodyLiterals.add(createLiteral("rsg", "Y1", "X1"));
        bodyLiterals.add(createLiteral("down", "Y1", "Y"));
        body = BASIC.createBody(bodyLiterals);
        rules.add(BASIC.createRule(head, body));
        IQuery query1 = BASIC.createQuery(BASIC.createLiteral(true, BASIC.createPredicate("rsg", 2), BASIC.createTuple(TERM.createString("a"), TERM.createVariable("Y"))));
        p1 = new AdornedProgram(rules, query1);
    }

    /**
	 * Tests whether all adorned predicates are available.
	 */
    public void testAdornedPredicatesP0() {
        final Set<IAdornedPredicate> preds = new HashSet<IAdornedPredicate>(1);
        preds.add(new AdornedPredicate("sg", new Adornment[] { Adornment.BOUND, Adornment.FREE }));
        assertEquals("There are not all predicates created", preds, p0.getAdornedPredicates());
    }

    /**
	 * Tests whether all adorned predicates are available.
	 */
    public void testAdornedPredicatesP1() {
        final Set<IAdornedPredicate> preds = new HashSet<IAdornedPredicate>(1);
        preds.add(new AdornedPredicate("rsg", new Adornment[] { Adornment.BOUND, Adornment.FREE }));
        preds.add(new AdornedPredicate("rsg", new Adornment[] { Adornment.FREE, Adornment.BOUND }));
        assertEquals("There are not all predicates created", preds, p1.getAdornedPredicates());
    }

    /**
	 * <p>
	 * Tests whether all adorned rules are computed
	 * </p>
	 * <p>
	 * <b>ATTENTION: at the moment only the &quot;core&quot; rules are compared,
	 * because while the computation of the adorned rules different queries are
	 * created to get the corresponding sip. So the sip of the adorned rules
	 * isn't compared</b>
	 * </p>
	 */
    public void testAdornedRulesP0() {
        final IAdornedPredicate pred = new AdornedPredicate("sg", new Adornment[] { Adornment.BOUND, Adornment.FREE });
        Set<IAdornedRule> rules = new HashSet<IAdornedRule>();
        IHead head = BASIC.createHead(BASIC.createLiteral(true, pred, BASIC.createTuple(new ArrayList<ITerm>(createVarList("X", "Y")))));
        IBody body = BASIC.createBody(createLiteral("flat", "X", "Y"));
        IRule r = BASIC.createRule(head, body);
        rules.add(new AdornedRule(r, new SIPImpl(unadornRule(r), p0.getQuery())));
        head = BASIC.createHead(BASIC.createLiteral(true, pred, BASIC.createTuple(new ArrayList<ITerm>(createVarList("X", "Y")))));
        final List<ILiteral> bodyLiterals = new ArrayList<ILiteral>();
        bodyLiterals.add(createLiteral("up", "X", "Z1"));
        bodyLiterals.add(BASIC.createLiteral(true, pred, BASIC.createTuple(new ArrayList<ITerm>(createVarList("Z1", "Z2")))));
        bodyLiterals.add(createLiteral("flat", "Z2", "Z3"));
        bodyLiterals.add(BASIC.createLiteral(true, pred, BASIC.createTuple(new ArrayList<ITerm>(createVarList("Z3", "Z4")))));
        bodyLiterals.add(createLiteral("down", "Z4", "Y"));
        body = BASIC.createBody(bodyLiterals);
        r = BASIC.createRule(head, body);
        rules.add(new AdornedRule(r, new SIPImpl(unadornRule(r), p0.getQuery())));
        final List<IAdornedRule> l0 = new ArrayList<IAdornedRule>(rules);
        final List<IAdornedRule> l1 = new ArrayList<IAdornedRule>(p0.getAdornedRules());
        assertEquals("The amount of rules must be equal", l0.size(), l1.size());
        Collections.sort(l0, RC);
        Collections.sort(l1, RC);
        for (Iterator<IAdornedRule> i0 = l0.iterator(), i1 = l1.iterator(); i0.hasNext(); ) {
            final IAdornedRule r0 = i0.next();
            final IAdornedRule r1 = i1.next();
            assertEquals("The rules\n" + r0 + "\nand\n" + r1 + "\ndon't match.", 0, RC.compare(r0, r1));
        }
    }

    /**
	 * <p>
	 * Tests whether all adorned rules are computed
	 * </p>
	 * <p>
	 * <b>ATTENTION: at the moment only the &quot;core&quot; rules are compared,
	 * because while the computation of the adorned rules different queries are
	 * created to get the corresponding sip. So the sip of the adorned rules
	 * isn't compared</b>
	 * </p>
	 */
    public void testAdornedRulesP1() {
        final IAdornedPredicate pred_bf = new AdornedPredicate("rsg", new Adornment[] { Adornment.BOUND, Adornment.FREE });
        final IAdornedPredicate pred_fb = new AdornedPredicate("rsg", new Adornment[] { Adornment.FREE, Adornment.BOUND });
        Set<IAdornedRule> rules = new HashSet<IAdornedRule>();
        IHead head = BASIC.createHead(BASIC.createLiteral(true, pred_bf, BASIC.createTuple(new ArrayList<ITerm>(createVarList("X", "Y")))));
        IBody body = BASIC.createBody(createLiteral("flat", "X", "Y"));
        IRule r = BASIC.createRule(head, body);
        rules.add(new AdornedRule(r, new SIPImpl(unadornRule(r), p1.getQuery())));
        head = BASIC.createHead(BASIC.createLiteral(true, pred_fb, BASIC.createTuple(new ArrayList<ITerm>(createVarList("X", "Y")))));
        body = BASIC.createBody(createLiteral("flat", "X", "Y"));
        r = BASIC.createRule(head, body);
        rules.add(new AdornedRule(r, new SIPImpl(unadornRule(r), p1.getQuery())));
        head = BASIC.createHead(BASIC.createLiteral(true, pred_bf, BASIC.createTuple(new ArrayList<ITerm>(createVarList("X", "Y")))));
        List<ILiteral> bodyLiterals = new ArrayList<ILiteral>();
        bodyLiterals.add(createLiteral("up", "X", "X1"));
        bodyLiterals.add(BASIC.createLiteral(true, pred_fb, BASIC.createTuple(new ArrayList<ITerm>(createVarList("Y1", "X1")))));
        bodyLiterals.add(createLiteral("down", "Y1", "Y"));
        body = BASIC.createBody(bodyLiterals);
        r = BASIC.createRule(head, body);
        rules.add(new AdornedRule(r, new SIPImpl(unadornRule(r), p1.getQuery())));
        head = BASIC.createHead(BASIC.createLiteral(true, pred_fb, BASIC.createTuple(new ArrayList<ITerm>(createVarList("X", "Y")))));
        bodyLiterals = new ArrayList<ILiteral>();
        bodyLiterals.add(createLiteral("up", "X", "X1"));
        bodyLiterals.add(BASIC.createLiteral(true, pred_fb, BASIC.createTuple(new ArrayList<ITerm>(createVarList("Y1", "X1")))));
        bodyLiterals.add(createLiteral("down", "Y1", "Y"));
        body = BASIC.createBody(bodyLiterals);
        r = BASIC.createRule(head, body);
        rules.add(new AdornedRule(r, new SIPImpl(unadornRule(r), p1.getQuery())));
        final List<IAdornedRule> l0 = new ArrayList<IAdornedRule>(rules);
        final List<IAdornedRule> l1 = new ArrayList<IAdornedRule>(p1.getAdornedRules());
        assertEquals("The amount of rules must be equal", l0.size(), l1.size());
        Collections.sort(l0, RC);
        Collections.sort(l1, RC);
        for (Iterator<IAdornedRule> i0 = l0.iterator(), i1 = l1.iterator(); i0.hasNext(); ) {
            final IAdornedRule r0 = i0.next();
            final IAdornedRule r1 = i1.next();
            assertEquals("The rules\n" + r0 + "\nand\n" + r1 + "\ndon't match.", 0, RC.compare(r0, r1));
        }
    }

    /**
	 * Creates an unadorned version of an adorned rule.
	 * 
	 * @param r
	 *            the adorned rule
	 * @return the unadorned rule
	 * @throws NullPointerException
	 *             if the rule is {@code null}
	 */
    public static IRule unadornRule(final IRule r) {
        if (r == null) {
            throw new NullPointerException("The rule must not be null");
        }
        boolean changed = false;
        List<ILiteral> lits = new ArrayList<ILiteral>(r.getHeadLiterals());
        int i = 0;
        for (final ILiteral l : lits) {
            if (l.getPredicate() instanceof IAdornedPredicate) {
                lits.set(i, BASIC.createLiteral(l.isPositive(), ((IAdornedPredicate) l.getPredicate()).getUnadornedPredicate(), l.getTuple()));
                changed = true;
            }
            i++;
        }
        final IHead h = BASIC.createHead(lits);
        lits = new ArrayList<ILiteral>(r.getBodyLiterals());
        i = 0;
        for (final ILiteral l : lits) {
            if (l.getPredicate() instanceof IAdornedPredicate) {
                lits.set(i, BASIC.createLiteral(l.isPositive(), ((IAdornedPredicate) l.getPredicate()).getUnadornedPredicate(), l.getTuple()));
                changed = true;
            }
            i++;
        }
        final IBody b = BASIC.createBody(lits);
        return changed ? BASIC.createRule(h, b) : r;
    }

    /**
	 * <p>
	 * Tests whether all adorned rules are computed when the query got no
	 * arguments bound.
	 * </p>
	 * <p>
	 * <b>ATTENTION: at the moment only the &quot;core&quot; rules are compared,
	 * because while the computation of the adorned rules different queries are
	 * created to get the corresponding sip. So the sip of the adorned rules
	 * isn't compared</b>
	 * </p>
	 */
    public void testFreeQuery() {
        final IRule r = BASIC.createRule(BASIC.createHead(createLiteral("w", "Y")), BASIC.createBody(createLiteral("k", "X", "Y"), createLiteral("l", "X")));
        final IQuery q = BASIC.createQuery(createLiteral("w", "X"));
        final AdornedProgram a = new AdornedProgram(Collections.singleton(r), q);
        assertTrue("There must not any adorned rules be created", a.getAdornedRules().isEmpty());
    }

    /**
	 * <p>
	 * Tests whether all adorned rules are computed when the query got no
	 * arguments bound.
	 * </p>
	 * <p>
	 * <b>ATTENTION: at the moment only the &quot;core&quot; rules are compared,
	 * because while the computation of the adorned rules different queries are
	 * created to get the corresponding sip. So the sip of the adorned rules
	 * isn't compared</b>
	 * </p>
	 */
    public void testFreeQuery1() {
        final IAdornedPredicate pred = new AdornedPredicate("w", new Adornment[] { Adornment.BOUND, Adornment.FREE });
        final IRule r = BASIC.createRule(BASIC.createHead(createLiteral("w", "X", "Y")), BASIC.createBody(createLiteral("k", "X", "B"), createLiteral("l", "B", "C"), createLiteral("w", "C", "Y")));
        final IQuery q = BASIC.createQuery(createLiteral("w", "X", "Y"));
        Set<IAdornedRule> rules = new HashSet<IAdornedRule>();
        IHead head = BASIC.createHead(BASIC.createLiteral(true, pred, BASIC.createTuple(new ArrayList<ITerm>(createVarList("X", "Y")))));
        IBody body = BASIC.createBody(createLiteral("k", "X", "B"), createLiteral("l", "B", "C"), BASIC.createLiteral(true, pred, BASIC.createTuple(new ArrayList<ITerm>(createVarList("C", "Y")))));
        IRule rule = BASIC.createRule(head, body);
        rules.add(new AdornedRule(rule, new SIPImpl(unadornRule(rule), q)));
        head = BASIC.createHead(createLiteral("w", "X", "Y"));
        body = BASIC.createBody(createLiteral("k", "X", "B"), createLiteral("l", "B", "C"), BASIC.createLiteral(true, pred, BASIC.createTuple(new ArrayList<ITerm>(createVarList("C", "Y")))));
        rule = BASIC.createRule(head, body);
        rules.add(new AdornedRule(rule, new SIPImpl(unadornRule(rule), q)));
        final AdornedProgram a = new AdornedProgram(Collections.singleton(r), q);
        final List<IAdornedRule> l0 = new ArrayList<IAdornedRule>(rules);
        final List<IAdornedRule> l1 = new ArrayList<IAdornedRule>(a.getAdornedRules());
        assertEquals("The amount of rules must be equal", l0.size(), l1.size());
        Collections.sort(l0, RC);
        Collections.sort(l1, RC);
        for (Iterator<IAdornedRule> i0 = l0.iterator(), i1 = l1.iterator(); i0.hasNext(); ) {
            final IAdornedRule r0 = i0.next();
            final IAdornedRule r1 = i1.next();
            assertEquals("The rules\n" + r0 + "\nand\n" + r1 + "\ndon't match.", 0, RC.compare(r0, r1));
        }
    }

    /**
	 * <p>
	 * Tests whether all adorned rules are computed when the query got no
	 * arguments bound.
	 * </p>
	 * <p>
	 * <b>ATTENTION: at the moment only the &quot;core&quot; rules are compared,
	 * because while the computation of the adorned rules different queries are
	 * created to get the corresponding sip. So the sip of the adorned rules
	 * isn't compared</b>
	 * </p>
	 */
    public void testFreeQuery2() {
        final IRule r = BASIC.createRule(BASIC.createHead(createLiteral("w", "X", "Y")), BASIC.createBody(createLiteral("k", "X", "B"), createLiteral("l", "B", "C"), createLiteral("w", "D", "Y")));
        final IQuery q = BASIC.createQuery(createLiteral("w", "X", "Y"));
        final AdornedProgram a = new AdornedProgram(Collections.singleton(r), q);
        assertTrue("There must not any adorned rules be created", a.getAdornedRules().isEmpty());
    }

    public void testConstructedAdornment() {
        final ILiteral constr = BASIC.createLiteral(true, BASIC.createPredicate("w", 3), BASIC.createTuple(TERM.createConstruct("const", TERM.createVariable("X"), TERM.createVariable("A"), TERM.createVariable("B")), TERM.createVariable("C"), TERM.createVariable("Y")));
        final IRule r = BASIC.createRule(BASIC.createHead(createLiteral("w", "X", "Y", "Z")), BASIC.createBody(createLiteral("k", "A", "B", "Y"), constr));
        final IQuery q = BASIC.createQuery(BASIC.createLiteral(true, BASIC.createPredicate("w", 3), BASIC.createTuple(TERM.createString("asdf"), TERM.createString("jklö"), TERM.createVariable("Z"))));
        final AdornedProgram a = new AdornedProgram(Collections.singleton(r), q);
        final IAdornedPredicate bbf = new AdornedPredicate("w", new Adornment[] { Adornment.BOUND, Adornment.BOUND, Adornment.FREE });
        final IAdornedPredicate bfb = new AdornedPredicate("w", new Adornment[] { Adornment.BOUND, Adornment.FREE, Adornment.BOUND });
        final Set<IAdornedRule> rules = new HashSet<IAdornedRule>();
        IHead head = BASIC.createHead(BASIC.createLiteral(true, bbf, BASIC.createTuple(new ArrayList<ITerm>(createVarList("X", "Y", "Z")))));
        IBody body = BASIC.createBody(createLiteral("k", "X", "B", "Y"), BASIC.createLiteral(true, bfb, BASIC.createTuple(TERM.createConstruct("const", TERM.createVariable("X"), TERM.createVariable("A"), TERM.createVariable("B")), TERM.createVariable("C"), TERM.createVariable("Y"))));
        IRule rule = BASIC.createRule(head, body);
        rules.add(new AdornedRule(rule, new SIPImpl(unadornRule(rule), q)));
        head = BASIC.createHead(BASIC.createLiteral(true, bfb, BASIC.createTuple(new ArrayList<ITerm>(createVarList("X", "Y", "Z")))));
        body = BASIC.createBody(createLiteral("k", "X", "B", "Y"), BASIC.createLiteral(true, bfb, BASIC.createTuple(TERM.createConstruct("const", TERM.createVariable("X"), TERM.createVariable("A"), TERM.createVariable("B")), TERM.createVariable("C"), TERM.createVariable("Y"))));
        rule = BASIC.createRule(head, body);
        rules.add(new AdornedRule(rule, new SIPImpl(unadornRule(rule), q)));
        final List<IAdornedRule> l0 = new ArrayList<IAdornedRule>(rules);
        final List<IAdornedRule> l1 = new ArrayList<IAdornedRule>(a.getAdornedRules());
        assertEquals("The amount of rules must be equal", l0.size(), l1.size());
        Collections.sort(l0, RC);
        Collections.sort(l1, RC);
        for (Iterator<IAdornedRule> i0 = l0.iterator(), i1 = l1.iterator(); i0.hasNext(); ) {
            final IAdornedRule r0 = i0.next();
            final IAdornedRule r1 = i1.next();
            assertEquals("The rules\n" + r0 + "\nand\n" + r1 + "\ndon't match.", 0, RC.compare(r0, r1));
        }
    }

    /**
	 * Creates an adorned literal.
	 * 
	 * @param symbol
	 *            the predicate symbot to use for the literal
	 * @param ad
	 *            the adornments
	 * @param t
	 *            the terms for the literal
	 * @return the constructed magic literal
	 * @throws NullPointerException
	 *             if the symbol is {@code null}
	 * @throws IllegalArgumentException
	 *             if the symbol is 0 sighns long
	 * @throws NullPointerException
	 *             if the adornments is or contains {@code null}
	 * @throws NullPointerException
	 *             if the terms is or contains {@code null}
	 */
    private static ILiteral createAdornedLiteral(final String symbol, final Adornment[] ad, final ITerm[] t) {
        if (symbol == null) {
            throw new NullPointerException("The symbol must not be null");
        }
        if (symbol.length() == 0) {
            throw new IllegalArgumentException("The symbol must be longer than 0 characters");
        }
        if ((ad == null) || Arrays.asList(ad).contains(null)) {
            throw new NullPointerException("The adornments must not be, or contain null");
        }
        if ((t == null) || Arrays.asList(t).contains(null)) {
            throw new NullPointerException("The terms must not be, or contain null");
        }
        return BASIC.createLiteral(true, new AdornedProgram.AdornedPredicate(symbol, t.length, ad), BASIC.createTuple(t));
    }

    /**
	 * Tests that constants in literals in the body will be marked as bound.
	 */
    public void testConstantsInBody() {
        final String program = "a(?X, ?Y) :- b(?X, ?Z), c('a', ?Z, ?Y). \n" + "c(?X, ?Y, ?Z) :- x(?X, ?Y, ?Z). \n" + "?-a('john', ?Y).";
        final IProgram p = Factory.PROGRAM.createProgram();
        Parser.parse(program, p);
        final AdornedProgram ap = new AdornedProgram(p.getRules(), p.getQueries().iterator().next());
        final ITerm X = TERM.createVariable("X");
        final ITerm Y = TERM.createVariable("Y");
        final ITerm Z = TERM.createVariable("Z");
        final ITerm[] XYZ = new ITerm[] { X, Y, Z };
        final Adornment[] bbf = new Adornment[] { Adornment.BOUND, Adornment.BOUND, Adornment.FREE };
        final Adornment[] bf = new Adornment[] { Adornment.BOUND, Adornment.FREE };
        final ILiteral b = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("b", 2), BASIC.createTuple(X, Z)));
        final ILiteral x = BASIC.createLiteral(true, BASIC.createAtom(BASIC.createPredicate("x", 3), BASIC.createTuple(XYZ)));
        final Set<IRule> ref = new HashSet<IRule>();
        ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("a", bf, new ITerm[] { X, Y })), BASIC.createBody(b, createAdornedLiteral("c", bbf, new ITerm[] { TERM.createString("a"), Z, Y }))));
        ref.add(BASIC.createRule(BASIC.createHead(createAdornedLiteral("c", bbf, XYZ)), BASIC.createBody(x)));
        assertTrue("The rules must be '" + MiscHelper.join("\n", ref) + "', but were '" + MiscHelper.join("\n", ap.getAdornedRules()) + "'", MiscHelper.compare(ap.getAdornedRules(), ref, RC));
    }

    /**
	 * <p>
	 * Compares two rules according to their predicate symbols.
	 * </p>
	 * <p>
	 * $Id: AdornmentsTest.java,v 1.5 2007-04-16 14:54:07 poettler_ric Exp $
	 * </p>
	 * 
	 * @author Richard Pöttler (richard dot poettler at deri dot org)
	 * @version $Revision: 1.5 $
	 */
    private static class RuleComparator implements Comparator<IRule> {

        public int compare(IRule o1, IRule o2) {
            if ((o1 == null) || (o2 == null)) {
                throw new NullPointerException("The rules must not be null");
            }
            int res = 0;
            if ((res = o1.getHeadLenght() - o2.getHeadLenght()) != 0) {
                return res;
            }
            for (final Iterator<ILiteral> i1 = o1.getHeadLiterals().iterator(), i2 = o2.getHeadLiterals().iterator(); i1.hasNext(); ) {
                if ((res = compareLiteral(i1.next(), i2.next())) != 0) {
                    return res;
                }
            }
            if ((res = o1.getBodyLenght() - o2.getBodyLenght()) != 0) {
                return res;
            }
            for (final Iterator<ILiteral> i1 = o1.getBodyLiterals().iterator(), i2 = o2.getBodyLiterals().iterator(); i1.hasNext(); ) {
                if ((res = compareLiteral(i1.next(), i2.next())) != 0) {
                    return res;
                }
            }
            return 0;
        }

        private static int compareLiteral(final ILiteral l1, final ILiteral l2) {
            if ((l1 == null) || (l2 == null)) {
                throw new NullPointerException("The literals must not be null");
            }
            int res = 0;
            final IPredicate p1 = l1.getPredicate();
            final IPredicate p2 = l2.getPredicate();
            if ((res = p1.getPredicateSymbol().compareTo(p2.getPredicateSymbol())) != 0) {
                return res;
            }
            if ((res = p1.getArity() - p2.getArity()) != 0) {
                return res;
            }
            if ((p1 instanceof IAdornedPredicate) && !(p2 instanceof IAdornedPredicate)) {
                return 1;
            } else if (!(p1 instanceof IAdornedPredicate) && (p2 instanceof IAdornedPredicate)) {
                return -1;
            } else if ((p1 instanceof IAdornedPredicate) && (p2 instanceof IAdornedPredicate)) {
                final Adornment[] a1 = ((IAdornedPredicate) p1).getAdornment();
                final Adornment[] a2 = ((IAdornedPredicate) p2).getAdornment();
                for (int i = 0; i < a1.length; i++) {
                    if ((a1[i] != a2[i]) && (a1[i] == Adornment.BOUND)) {
                        return -1;
                    } else if ((a1[i] != a2[i]) && (a1[i] == Adornment.FREE)) {
                        return 1;
                    }
                }
            }
            return 0;
        }
    }
}
