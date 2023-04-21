package org.impalaframework.service.filter.ldap;

/**
 * Implements = operator in RFC 1960 matching expression.
 * 
 * @author Phil Zoio
 */
class EqualsNode extends ItemNode {

    EqualsNode(String key, String value) {
        super(key, value);
    }

    @Override
    public String toString() {
        return wrapBrackets(getKey() + "=" + getEncodedValue());
    }

    @Override
    protected boolean matchString(String external) {
        return getValue().equals(external);
    }

    @Override
    protected boolean matchBoolean(Boolean external) {
        return TypeHelper.equalsBoolean(getValue(), external);
    }

    @Override
    protected boolean matchByte(Byte external) {
        return TypeHelper.equalsByte(getValue(), external);
    }

    @Override
    protected boolean matchCharacter(Character external) {
        return TypeHelper.equalsCharacter(getValue(), external);
    }

    @Override
    protected boolean matchDouble(Double external) {
        return TypeHelper.equalsDouble(getValue(), external);
    }

    @Override
    protected boolean matchFloat(Float external) {
        return TypeHelper.equalsFloat(getValue(), external);
    }

    @Override
    protected boolean matchInteger(Integer external) {
        return TypeHelper.equalsInteger(getValue(), external);
    }

    @Override
    protected boolean matchLong(Long external) {
        return TypeHelper.equalsLong(getValue(), external);
    }

    @Override
    protected boolean matchShort(Short external) {
        return TypeHelper.equalsShort(getValue(), external);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean matchComparable(Comparable internal, Comparable external) {
        return (internal.compareTo(external) == 0);
    }
}
