package net.sf.ictalive.wow;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;
import opera.OM.Atom;
import opera.OM.Conjunction;
import opera.OM.Disjunction;
import opera.OM.Norm;
import opera.OM.PartialStateDescription;
import net.sf.ictalive.monitoring.rules.drools.schema.AndConditionalElement;
import net.sf.ictalive.monitoring.rules.drools.schema.FieldBinding;
import net.sf.ictalive.monitoring.rules.drools.schema.FieldConstraint;
import net.sf.ictalive.monitoring.rules.drools.schema.Lhs;
import net.sf.ictalive.monitoring.rules.drools.schema.LiteralRestriction;
import net.sf.ictalive.monitoring.rules.drools.schema.ObjectFactory;
import net.sf.ictalive.monitoring.rules.drools.schema.OrConditionalElement;
import net.sf.ictalive.monitoring.rules.drools.schema.Pattern;
import net.sf.ictalive.monitoring.rules.drools.schema.Rule;
import net.sf.ictalive.monitoring.rules.drools.schema.VariableRestriction;

public class NormParser {

    public Collection<Rule> parse(Collection<Norm> ns) {
        Vector<Rule> vr;
        vr = new Vector<Rule>();
        for (Norm norm : ns) {
            vr.addAll(parse(norm));
        }
        return vr;
    }

    public Collection<Rule> parse(Norm n) {
        Vector<Rule> vr;
        Rule r;
        vr = new Vector<Rule>();
        r = parseCondition(n.getActivationCondition(), n.getNormID(), "activation");
        vr.add(r);
        r = parseCondition(n.getMaintenanceCondition(), n.getNormID(), "maintenance");
        vr.add(r);
        r = parseCondition(n.getExpirationCondition(), n.getNormID(), "expiration");
        vr.add(r);
        r = parseCondition(n.getDeadline(), n.getNormID(), "deadline");
        vr.add(r);
        return vr;
    }

    private Rule parseCondition(PartialStateDescription condition, String normID, String type) {
        Rule r;
        Lhs lhs;
        Object res;
        ObjectFactory of;
        Set<String> vars;
        String rhs, ruleName, key;
        Iterator<String> it;
        ruleName = normID + "_" + type;
        of = new ObjectFactory();
        r = new Rule();
        r.setName(ruleName);
        vars = new TreeSet<String>();
        lhs = new Lhs();
        res = parseFormula(condition, vars);
        if (res != null) {
            if (res instanceof AndConditionalElement) {
                lhs.getAbstractConditionalElementOrNotOrExists().add(of.createAndConditionalElement((AndConditionalElement) res));
            } else if (res instanceof OrConditionalElement) {
                lhs.getAbstractConditionalElementOrNotOrExists().add(of.createOrConditionalElement((OrConditionalElement) res));
            } else {
                lhs.getAbstractConditionalElementOrNotOrExists().add(res);
            }
        }
        r.setLhs(lhs);
        rhs = "\t\tjava.util.Set<Value> substitution = new java.util.TreeSet<Value>();\n\t\tValue newValue;\n";
        it = vars.iterator();
        while (it.hasNext()) {
            key = it.next();
            rhs = rhs + "\t\tnewValue = new Value(\"" + key + "\", " + key + ");";
            rhs = rhs + "\t\tsubstitution.add(newValue);\n";
        }
        rhs = rhs + "\t\tinsertLogical(new Holds" + new String(type.charAt(0) + "").toUpperCase() + type.substring(1);
        rhs = rhs + "(\"" + normID + "\", substitution));\n";
        r.setRhs(rhs);
        return r;
    }

    private Object parseFormula(PartialStateDescription condition, Set<String> context) {
        Object res, tmp;
        Atom a;
        Pattern p;
        Conjunction c;
        AndConditionalElement and;
        OrConditionalElement or;
        Disjunction d;
        StringTokenizer pred;
        FieldBinding fb;
        FieldConstraint fc;
        int i;
        LiteralRestriction lr;
        String token;
        VariableRestriction vr;
        if (condition == null) {
            res = null;
        } else if (condition instanceof Atom) {
            a = (Atom) condition;
            p = new Pattern();
            p.setIdentifier("id" + new Random().nextInt(10000));
            p.setObjectType("Proposition");
            pred = new StringTokenizer(a.getProposition(), "(,)");
            fc = new FieldConstraint();
            fc.setFieldName("predicate");
            lr = new LiteralRestriction();
            lr.setEvaluator("==");
            lr.setValue(pred.nextToken());
            fc.getLiteralRestrictionOrVariableRestrictionOrReturnValueRestriction().add(lr);
            p.getFieldBindingOrFieldConstraintOrFrom().add(fc);
            i = 0;
            while (pred.hasMoreTokens()) {
                token = pred.nextToken();
                if (context.contains(token)) {
                    fc = new FieldConstraint();
                    fc.setFieldName("p" + i);
                    vr = new VariableRestriction();
                    vr.setEvaluator("==");
                    vr.setIdentifier(token);
                    fc.getLiteralRestrictionOrVariableRestrictionOrReturnValueRestriction().add(vr);
                    p.getFieldBindingOrFieldConstraintOrFrom().add(fc);
                } else {
                    fb = new FieldBinding();
                    fb.setFieldName("p" + i);
                    fb.setIdentifier(token);
                    p.getFieldBindingOrFieldConstraintOrFrom().add(fb);
                    context.add(token);
                }
                i++;
            }
            res = p;
        } else if (condition instanceof Conjunction) {
            c = (Conjunction) condition;
            and = new AndConditionalElement();
            tmp = parseFormula(c.getLeftStateFormula(), context);
            if (tmp != null) {
                if (tmp instanceof AndConditionalElement) {
                    or = new OrConditionalElement();
                    or.getAndConditionalElement().add((AndConditionalElement) tmp);
                    and.getOrConditionalElement().add(or);
                } else if (tmp instanceof OrConditionalElement) {
                    and.getOrConditionalElement().add((OrConditionalElement) tmp);
                } else {
                    and.getNotOrExistsOrEval().add(tmp);
                }
            }
            tmp = parseFormula(c.getRightStateFormula(), context);
            if (tmp != null) {
                if (tmp instanceof AndConditionalElement) {
                    or = new OrConditionalElement();
                    or.getAndConditionalElement().add((AndConditionalElement) tmp);
                    and.getOrConditionalElement().add(or);
                } else if (tmp instanceof OrConditionalElement) {
                    and.getOrConditionalElement().add((OrConditionalElement) tmp);
                } else {
                    and.getNotOrExistsOrEval().add(tmp);
                }
            }
            res = and;
        } else if (condition instanceof Disjunction) {
            d = (Disjunction) condition;
            or = new OrConditionalElement();
            tmp = parseFormula(d.getLeftStateFormula(), context);
            if (tmp != null) {
                if (tmp instanceof OrConditionalElement) {
                    and = new AndConditionalElement();
                    and.getOrConditionalElement().add((OrConditionalElement) tmp);
                    or.getAndConditionalElement().add(and);
                } else if (tmp instanceof AndConditionalElement) {
                    or.getAndConditionalElement().add((AndConditionalElement) tmp);
                } else {
                    or.getNotOrExistsOrEval().add(tmp);
                }
            }
            tmp = parseFormula(d.getRightStateFormula(), context);
            if (tmp != null) {
                if (tmp instanceof OrConditionalElement) {
                    and = new AndConditionalElement();
                    and.getOrConditionalElement().add((OrConditionalElement) tmp);
                    or.getAndConditionalElement().add(and);
                } else if (tmp instanceof AndConditionalElement) {
                    or.getAndConditionalElement().add((AndConditionalElement) tmp);
                } else {
                    or.getNotOrExistsOrEval().add(tmp);
                }
            }
            res = or;
        } else {
            p = new Pattern();
            p.setIdentifier(condition.getClass().getName() + new Random().nextInt(10000));
            p.setObjectType("Object");
            res = p;
        }
        return res;
    }
}
