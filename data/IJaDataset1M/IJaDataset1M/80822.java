package org.springframework.aop.target;

import java.io.Serializable;
import org.springframework.aop.TargetSource;
import org.springframework.util.Assert;

/**
 * Implementation of the TargetSource interface that holds a local object.
 * This is the default implementation of TargetSource used by the AOP framework.
 * There is no need to create objects of this class in application code.
 *
 * <p>This class is serializable. However, the actual serializability of a
 * SingletonTargetSource will depend on whether the target is serializable.
 *
 * @author Rod Johnson
 * @see org.springframework.aop.framework.AdvisedSupport#setTarget
 */
public final class SingletonTargetSource implements TargetSource, Serializable {

    /** Target cached and invoked using reflection */
    private final Object target;

    /**
	 * Create a new SingletonTargetSource for the given target.
	 * @param target the target object
	 */
    public SingletonTargetSource(Object target) {
        Assert.notNull(target, "target is required");
        this.target = target;
    }

    public Class getTargetClass() {
        return this.target.getClass();
    }

    public Object getTarget() {
        return this.target;
    }

    public void releaseTarget(Object target) {
    }

    public boolean isStatic() {
        return true;
    }

    /**
	 * SingletonTargetSource uses the hash code of the target object.
	 */
    public int hashCode() {
        return this.target.hashCode();
    }

    /**
	 * Two invoker interceptors are equal if they have the same target or if the
	 * targets or the targets are equal.
	 */
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SingletonTargetSource)) {
            return false;
        }
        SingletonTargetSource otherTargetSource = (SingletonTargetSource) other;
        return this.target.equals(otherTargetSource.target);
    }

    public String toString() {
        return "SingletonTargetSource for target: " + this.target;
    }
}
