package net.sf.xspecs.oomatron;

import org.jmock.core.Constraint;

public interface Role {

    void attachMonitor(InvocationMonitor monitor);

    void assertHasMethodMatching(Constraint methodMatcher);

    void collectConstrainedMethods(Constraint methodConstraint, Collector accumulator);
}
