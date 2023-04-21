package org.conann.util;

@SuppressWarnings({ "ThrowableInstanceNeverThrown" })
public abstract class DevelopmentUtil {

    public static UnderDevelopmentException unsupportedOperation(String message) {
        return new UnderDevelopmentException(message);
    }

    public static UnderDevelopmentException unsupportedOperation() {
        return new UnderDevelopmentException("Yeah, well...");
    }
}
