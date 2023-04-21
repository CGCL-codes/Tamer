package org.t2framework.lucy.inject;

import org.t2framework.lucy.Lucy;
import org.t2framework.lucy.exception.CyclicReferenceRuntimeException;

/**
 * 
 * <#if locale="en">
 * <p>
 * {@link Injector} injects component using {@link Lucy} to the instance.
 * </p>
 * <#else>
 * <p>
 * {@link Injector}はコンポーネントへのインジェクトを実現します.
 * </p>
 * </#if>
 * 
 * @author shot
 * 
 */
public interface Injector {

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Inject component from {@link Lucy} to the instance representing T.
	 * </p>
	 * <#else>
	 * <p>
	 * コンポーネントTに対してインジェクトを行います.
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param t
	 * @param lucy
	 * @return
	 * @throws CyclicReferenceRuntimeException
	 */
    <T> T inject(T t, Lucy lucy) throws CyclicReferenceRuntimeException;
}
