package net.stickycode.mockwire;

import java.lang.reflect.Method;
import net.stickycode.exception.PermanentException;

@SuppressWarnings("serial")
public class VoidMethodsCannotBeUsedAsFactoriesForCodeUnderTestException extends PermanentException {

    public VoidMethodsCannotBeUsedAsFactoriesForCodeUnderTestException(String message, Object... parameters) {
        super(message, parameters);
    }

    public VoidMethodsCannotBeUsedAsFactoriesForCodeUnderTestException(Method method) {
        super("Method {} on test {} is void so you can't bless it as a bean factory method", method.getName(), method.getDeclaringClass().getSimpleName());
    }
}
