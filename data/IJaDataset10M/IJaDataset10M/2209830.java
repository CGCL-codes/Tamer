package br.com.bit.ideias.reflection.criteria.expression;

import java.lang.reflect.Member;

/**
 * @author Leonardo Campos
 * @date 31/07/2009
 * 
 *       For a DisjunctionExpression to be evaluated as true, any of its
 *       subexpressions should evaluate to true
 */
public class DisjunctionExpression extends ConjunctionExpression {

    @Override
    public boolean accept(final Member member) {
        for (final Expression expression : expressions) {
            if (expression.accept(member)) return true;
        }
        return false;
    }
}
