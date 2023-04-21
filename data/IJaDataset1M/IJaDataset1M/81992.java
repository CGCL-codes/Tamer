package net.sf.jimo.common.ldap.expr;

import java.util.List;

/**
 * 
 * Id $Id: LdapConditionalExpression.java 650 2007-09-25 18:48:02Z logicfish $
 * Type LdapConditionalExpression
 * @version $Rev$
 * @author logicfish
 * @since 0.2
 *
 */
public interface LdapConditionalExpression extends LdapExpression {

    public Expressions.Condition getCondtion();

    public List<? super LdapExpression> getChildren();
}
