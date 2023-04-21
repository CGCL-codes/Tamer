package net.sf.jimo.common.ldap.expr;

/**
 * 
 * Id $Id: LdapLessThanOrEqual.java 650 2007-09-25 18:48:02Z logicfish $
 * Type LdapLessThanOrEqual
 * @version $Rev$
 * @author logicfish
 * @since 0.2
 *
 */
public class LdapLessThanOrEqual extends LdapSimpleUnary implements LdapSimpleExpression, LdapUnaryExpression {

    public Expressions.Operator getOperator() {
        return Expressions.Operator.OPER_LESS_THAN_OR_EQUAL;
    }

    public String toFilterString() {
        return "(" + getProperty() + "<=" + getOperand() + ")";
    }
}
