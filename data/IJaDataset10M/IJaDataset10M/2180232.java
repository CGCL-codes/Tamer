package org.opennms.core.criteria.restrictions;

public interface Restriction {

    public static enum RestrictionType {

        NULL, NOTNULL, EQ, NE, GT, GE, LT, LE, ALL, ANY, LIKE, ILIKE, IN, NOT, BETWEEN, SQL, IPLIKE
    }

    public abstract void visit(final RestrictionVisitor visitor);

    public RestrictionType getType();
}
