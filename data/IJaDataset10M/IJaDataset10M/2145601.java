package org.genxdm.xs.exceptions;

@SuppressWarnings("serial")
public final class WildcardIntersectionNotExpressibleException extends WildcardIntersectionException {

    public WildcardIntersectionNotExpressibleException() {
        super(PART_INTERSECTION_NOT_EXPRESSIBLE);
    }

    public WildcardIntersectionNotExpressibleException(final SchemaException cause) {
        super(PART_INTERSECTION_NOT_EXPRESSIBLE, cause);
    }

    @Override
    public String getMessage() {
        return "If the two are negations of different namespace names, then the intersection is not expressible.";
    }
}
