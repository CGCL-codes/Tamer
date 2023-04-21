package com.calipso.reportgenerator.reportcalculator.expression;

import java.io.Serializable;

/**
 * Representa una expresi n binaria con operador OR
 */
public class OrExp extends BinaryExp implements Serializable {

    public OrExp(Expression expression, Expression expression1) {
        super(expression, expression1);
    }

    /**
   * Resuelva las particularidades de la representaci n en texto con el operador OR
   * @return
   */
    protected String asStringUnderOr() {
        return basicAsString();
    }

    /**
   * Resuelve la representaci n en texto
   * @return
   */
    protected String basicAsString() {
        return getLeft().asStringUnderOr() + " OR " + getRight().asStringUnderOr();
    }

    /**
   * Resoluci n del pattern visitor
   * @param visitor
   * @return
   */
    public Object visitedBy(ExpressionVisitor visitor) {
        return visitor.processOr(this);
    }
}
