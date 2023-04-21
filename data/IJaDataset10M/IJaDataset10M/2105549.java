package net.sf.jimo.common.ldap.expr;

/**
 * 
 * Id $Id: LdapSimpleExpression.java 650 2007-09-25 18:48:02Z logicfish $
 * Type LdapSimpleExpression
 * @version $Rev$
 * @author logicfish
 * @since 0.2
 *
 */
public interface LdapSimpleExpression extends LdapExpression {

    Expressions.Operator getOperator();

    String getProperty();

    void setProperty(String property);
}
