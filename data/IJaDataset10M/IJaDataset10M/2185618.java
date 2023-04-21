package net.stickycode.coercion;

public class StringCoercion extends AbstractNoDefaultCoercion<String> {

    @Override
    public String coerce(CoercionTarget target, String value) {
        return value;
    }

    @Override
    public boolean isApplicableTo(CoercionTarget target) {
        return String.class.isAssignableFrom(target.getType());
    }
}
