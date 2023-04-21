package com.msli.core.util;

import com.msli.core.util.Disposable.Ignore;

/**
 * Prescribes the "singleton" object pattern, where only a single instance of a
 * concrete type is allowed to exist in a given JVM.
 * See NullObject.Null for an example of how a singleton should be implemented.
 * <p>
 * As a singleton, the contract for equality requires the use of identity
 * equality, with {@code Object.equals()} returning "this". By convention,
 * {@code Object.hashCode()} should return {@code System.identityHashCode(this)}.
 * <p>
 * The contract for Cloneable does not absolutely require that "x != x.clone()",
 * and the contract for Dupable specifically allows "x = x.dupe()" for
 * singletons. As such, factory methods such as {@code Object.clone()} and
 * {@code Dupable.dupe()} should return "this". For similar reasons, if a
 * concrete subclass is Serializable it should implement {@code readResolve()}
 * such that it returns the single static instance (which, in this case, is NOT
 * "this").
 * <p>
 * For a singleton object to serve as a singleton object it need not implement
 * this interface (although it doesn't hurt).
 * @author jonb
 * @see Object#clone()
 * @see Serializable
 * @see Dupable
 * @see NullObject.Null
 */
public interface Singleton {

    /**
	 * A "normal" singleton, whose lifetime is supposed to be that of the JVM.
	 * As such, disposal is ignored.
	 * @author jonb
	 */
    public static interface Normal extends Ignore {
    }

    /**
	 * A special singleton, which can be disposed. Intended for use a singleton
	 * global service that is shutdown just prior to the JVM shutting down.
	 * By supporting disposal, the service can verify the shutdown state
	 * and release any native system resources.
	 * @author jonb
	 */
    public static interface Disposable {
    }
}
