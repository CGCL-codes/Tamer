package com.calipso.reportgenerator.reportcalculator.expression;

import java.io.Serializable;

/**
 * Representa una expresi n binaria que determina si el valor resultante de evaluar una expresi n el menor o igual
 * que el valor resultante de evaluar el valor de la otra
 */
public class LessOrEqualTo extends BinaryExp implements Serializable {

    /**
   * Crea una expresi n asignando las sub expresiones
   * @param expression
   * @param expression1
   */
    public LessOrEqualTo(Expression expression, Expression expression1) {
        super(expression, expression1);
    }

    /**
   * Resuelve la representaci n en texto
   * @return
   */
    protected String basicAsString() {
        return getLeft().basicAsString() + " <= " + getRight().basicAsString();
    }

    /**
   * Resoluci n del pattern visitor
   * @param visitor
   * @return
   */
    public Object visitedBy(ExpressionVisitor visitor) {
        return visitor.processLessOrEqualTo(this);
    }
}
