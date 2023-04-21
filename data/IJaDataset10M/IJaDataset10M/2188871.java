package mockit.internal.expectations.invocation;

import java.lang.reflect.*;
import mockit.Invocation;

final class DelegateInvocation extends Invocation {

    private final InvocationArguments invocationArguments;

    boolean proceedIntoConstructor;

    DelegateInvocation(Object invokedInstance, Object[] invokedArguments, ExpectedInvocation expectedInvocation, InvocationConstraints constraints) {
        super(invokedInstance, invokedArguments, constraints.invocationCount, constraints.minInvocations, constraints.maxInvocations);
        invocationArguments = expectedInvocation.arguments;
    }

    @Override
    protected Method getRealMethod() {
        if (invocationArguments.isForConstructor()) {
            proceedIntoConstructor = true;
            return null;
        }
        return invocationArguments.getRealMethod().method;
    }
}
