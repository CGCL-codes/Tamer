package org.middleheaven.math.expression;

import java.util.LinkedList;
import java.util.List;

public class Expression {

    List<Term> terms = new LinkedList<Term>();

    public Expression() {
        super();
    }

    public Expression(List<Term> terms) {
        this.terms.addAll(terms);
    }

    public void addTerm(Term exp) {
        terms.add(exp);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Term exp : terms) {
            builder.append(exp.toString());
            builder.append(";");
        }
        return builder.toString();
    }

    public Number evaluate() {
        return new ExpressionEvaluator().evaluate(this);
    }
}
