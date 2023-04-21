package br.net.woodstock.rockframework.net.ldap.restriction;

import br.net.woodstock.rockframework.net.ldap.LDAPRestriction;

class LDAPRestrictionNotContains extends LDAPRestriction {

    private static final long serialVersionUID = 7524499520806772784L;

    public LDAPRestrictionNotContains(final String propertyName) {
        super();
        this.setPropertyName(propertyName);
    }

    @Override
    public String getRestriction() {
        StringBuilder builder = new StringBuilder();
        builder.append("(!(");
        builder.append(this.getPropertyName());
        builder.append("=*))");
        return builder.toString();
    }
}
