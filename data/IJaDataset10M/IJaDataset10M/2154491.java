package gumbo.core.struct;

import gumbo.core.util.NullObject;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Wraps a target Collection as a Set. As such, subclasses must implement
 * equality (equals() and hashCode()), which must satisfy the contract for Set
 * equality. If the target is a Set, can use SetWrapper.Impl instead. For
 * construction convenience, implements Serialization, but serialization will
 * fail unless the wrapper target is also serializable.
 * <P>
 * Derived from gumbo.util.collection.SetWrapper.
 * @author jonb
 * @param <E> Collection element type.
 */
public abstract class SetWrapper<E> extends CollectionWrapper<E> implements Serializable, Set<E> {

    /**
	 * Creates an instance.
	 * @param target Shared exposed wrapper target. Never null.
	 */
    public SetWrapper(Collection<E> target) {
        super(target);
    }

    @Override
    protected Set<E> getWrapperTarget() {
        return (Set<E>) super.getWrapperTarget();
    }

    private static final long serialVersionUID = 1;

    /**
	 * Default serializable concrete implementation, with the wrapper target
	 * being a Set. Equality (equals(), hashCode()) forwards to the wrapper
	 * target.
	 * @param <E> Collection element type.
	 */
    public static class Impl<E> extends SetWrapper<E> {

        /**
		 * Creates an instance.
		 * @param target Shared exposed wrapper target. Never null.
		 */
        public Impl(Set<E> target) {
            super(target);
        }

        @Override
        public final boolean equals(Object obj) {
            if (obj == this) return true;
            return getWrapperTarget().equals(obj);
        }

        @Override
        public final int hashCode() {
            return getWrapperTarget().hashCode();
        }

        private static final long serialVersionUID = 1;
    }

    /**
	 * Concrete serializable unmodifiable wrapper. All exposers are unmodifiable
	 * (but collection element objects may be modifiable). All mutators throw
	 * UnmodifiableStateException. Equality (equals(), hashCode()) forwards to
	 * the wrapper target.
	 * @param <E> Collection element type.
	 */
    public static class Unmod<E> extends CollectionWrapper.Unmod<E> implements Serializable, Set<E> {

        /**
		 * Creates an instance. Since this wrapper is unmodifiable, the target
		 * group can be one with elements that are a subclass of E.
		 * @param target Shared exposed wrapper target. Never null.
		 */
        public Unmod(Set<? extends E> target) {
            super(target);
        }

        @Override
        protected Set<? extends E> getWrapperTarget() {
            return (Set<? extends E>) super.getWrapperTarget();
        }

        @Override
        public final boolean equals(Object obj) {
            if (obj == this) return true;
            return getWrapperTarget().equals(obj);
        }

        @Override
        public final int hashCode() {
            return getWrapperTarget().hashCode();
        }

        private static final long serialVersionUID = 1L;
    }

    /**
	 * Concrete serializable null object wrapper. All exposers are null objects.
	 * All mutators do nothing. Equality (equals(), hashCode()) forwards to the
	 * wrapper target.
	 * @param <E> Collection element type.
	 */
    public static class Null<E> extends CollectionWrapper.Null<E> implements NullObject, Serializable, Set<E> {

        /**
		 * Creates an instance. Since this wrapper is unmodifiable, the target
		 * group can be one with elements that are a subclass of E.
		 * @param target Shared exposed wrapper target. Never null.
		 */
        public Null(Set<? extends E> target) {
            super(target);
        }

        @Override
        protected Set<? extends E> getWrapperTarget() {
            return (Set<? extends E>) super.getWrapperTarget();
        }

        @Override
        public final boolean equals(Object obj) {
            if (obj == this) return true;
            return getWrapperTarget().equals(obj);
        }

        @Override
        public final int hashCode() {
            return getWrapperTarget().hashCode();
        }

        private static final long serialVersionUID = 1L;
    }
}
