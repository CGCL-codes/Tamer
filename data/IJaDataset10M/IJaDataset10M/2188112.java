package org.jgentleframework.context.aop;

import org.jgentleframework.context.aop.support.Matching;

/**
 * Core JGentle pointcut abstraction.
 * 
 * @author LE QUOC CHUNG - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Aug 14, 2008
 * @see PointcutOfParameterFilter
 * @see PointcutOfClassFilter
 * @see PointcutOfMethodFilter
 * @see PointcutOfConstructorFilter
 * @see PointcutOfFieldFilter
 * @see PointcutOfAll
 * @see ClassFilter
 * @see MethodFilter
 * @see FieldFilter
 * @see ConstructorFilter
 * @see ParameterFilter
 */
public interface Pointcut<T extends Matching> {

    /**
	 * Canonical {@link Pointcut} instance that always matches.
	 */
    Pointcut<? extends Matching> TRUE = TruePointcut.singleton();

    /**
	 * Returns the {@link Filter} of this {@link Pointcut}.
	 */
    Filter<T> getFilter();
}
