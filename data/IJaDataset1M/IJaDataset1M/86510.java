package org.springframework.rules.reporting;

import java.util.Iterator;
import org.springframework.core.ReflectiveVisitorHelper;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.And;
import org.springframework.rules.constraint.Or;
import org.springframework.rules.constraint.property.CompoundPropertyConstraint;
import org.springframework.rules.constraint.property.ParameterizedPropertyConstraint;
import org.springframework.rules.constraint.property.PropertiesConstraint;
import org.springframework.util.Assert;

/**
 * @author Keith Donald
 */
public class SummingVisitor {

    private ReflectiveVisitorHelper visitorSupport = new ReflectiveVisitorHelper();

    private int sum;

    private Constraint constraint;

    public SummingVisitor(Constraint constraint) {
        Assert.notNull(constraint, "constraint is required");
        this.constraint = constraint;
    }

    public int sum() {
        visitorSupport.invokeVisit(this, constraint);
        return sum;
    }

    void visit(CompoundPropertyConstraint rule) {
        visitorSupport.invokeVisit(this, rule.getPredicate());
    }

    void visit(PropertiesConstraint e) {
        sum++;
    }

    void visit(ParameterizedPropertyConstraint e) {
        sum++;
    }

    void visit(And and) {
        Iterator it = and.iterator();
        while (it.hasNext()) {
            Constraint p = (Constraint) it.next();
            visitorSupport.invokeVisit(this, p);
        }
    }

    void visit(Or or) {
        Iterator it = or.iterator();
        while (it.hasNext()) {
            Constraint p = (Constraint) it.next();
            visitorSupport.invokeVisit(this, p);
        }
    }

    void visit(Constraint constraint) {
        sum++;
    }
}
