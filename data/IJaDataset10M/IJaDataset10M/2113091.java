package org.jgentleframework.context.aop;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.jgentleframework.context.aop.support.MethodConstructorMatching;
import org.jgentleframework.core.intercept.support.AbstractMatcher;

/**
 * Canonical {@link MethodFilter} instance that matches all {@link Method}.
 * 
 * @author LE QUOC CHUNG - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Aug 16, 2008
 * @see MethodFilter
 */
public class TrueMethodFilter extends AbstractMatcher<MethodConstructorMatching<Method>> implements MethodFilter, Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -429920506221381577L;

    /** The obj instance. */
    private static TrueMethodFilter objInstance = null;

    /**
	 * Enforce Singleton pattern.
	 */
    private TrueMethodFilter() {
    }

    /**
	 * Singleton.
	 * 
	 * @return the true constructor filter
	 */
    public static synchronized TrueMethodFilter singleton() {
        if (TrueMethodFilter.objInstance == null) {
            TrueMethodFilter.objInstance = new TrueMethodFilter();
        }
        return TrueMethodFilter.objInstance;
    }

    @Override
    public boolean isRuntime() {
        return false;
    }

    /**
	 * Required to support serialization. Replaces with canonical instance on
	 * deserialization, protecting Singleton pattern. Alternative to overriding
	 * <code>equals()</code>.
	 * 
	 * @return the object
	 */
    private Object readResolve() {
        return objInstance;
    }

    @Override
    public String toString() {
        return "TrueMethodFilter.TRUE";
    }

    @Override
    public boolean matches(MethodConstructorMatching<Method> matching) {
        return true;
    }
}
