package net.stickycode.coercion;

@SuppressWarnings("serial")
public class CollectionCoercionDoesNotHaveAnAppriateMappingException extends RuntimeException {

    public CollectionCoercionDoesNotHaveAnAppriateMappingException(Class<?> type) {
        super("A mapping to a concrete collection implementation for " + type.getName() + " was not found");
    }
}
