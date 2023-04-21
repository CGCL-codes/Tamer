package br.net.woodstock.rockframework.net.ldap.filter;

class LDAPRestrictionContains extends LDAPRestriction {

    private static final long serialVersionUID = 4125869948384443018L;

    public LDAPRestrictionContains(final String propertyName) {
        super();
        this.setPropertyName(propertyName);
    }

    @Override
    public String getRestriction() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(this.getPropertyName());
        builder.append("=*)");
        return builder.toString();
    }
}
