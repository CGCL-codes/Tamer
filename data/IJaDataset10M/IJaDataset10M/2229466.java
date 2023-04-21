package net.sf.jctc.common.ldap.expr;

/**
 * 
 * Id $Id: LdapGreaterThanOrEqual.java 39 2008-02-23 06:08:10Z logicfish $
 * Type LdapGreaterThanOrEqual
 * @version $Rev$
 * @author logicfish
 * @since 0.2
 *
 */
public class LdapGreaterThanOrEqual extends LdapSimpleUnary implements LdapSimpleExpression, LdapUnaryExpression {

    public Expressions.Operator getOperator() {
        return Expressions.Operator.OPER_GREATER_THAN_OR_EQUAL;
    }

    public String toFilterString() {
        return "(" + getProperty() + ">=" + getOperand() + ")";
    }
}
