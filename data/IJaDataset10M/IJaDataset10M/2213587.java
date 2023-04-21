package net.woodstock.rockapi.net.ldap.filter;

class LDAPRestrictionLike extends LDAPRestriction {

    private static final long serialVersionUID = 1359372433669807722L;

    public LDAPRestrictionLike(String propertyName, Object value) {
        super();
        this.setPropertyName(propertyName);
        this.setValue(value);
    }

    @Override
    public String getRestriction() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(this.getPropertyName());
        builder.append("=*");
        builder.append(this.getValue().toString());
        builder.append("*)");
        return builder.toString();
    }
}
