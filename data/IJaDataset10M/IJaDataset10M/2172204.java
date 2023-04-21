package net.sf.jimo.common.ldap.expr;

/**
 * 
 * Id $Id: LdapEqualExpression.java 650 2007-09-25 18:48:02Z logicfish $
 * Type LdapEqualExpression
 * @version $Rev$
 * @author logicfish
 * @since 0.2
 *
 */
public class LdapEqualExpression extends LdapSimpleUnary implements LdapSimpleExpression, LdapUnaryExpression {

    public Expressions.Operator getOperator() {
        return Expressions.Operator.OPER_EQUAL;
    }

    public String toFilterString() {
        return "(" + getProperty() + "=" + getOperand() + ")";
    }
}
