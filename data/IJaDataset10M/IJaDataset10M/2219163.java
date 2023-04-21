package org.t2framework.lucy.tx.interceptor;

import java.lang.reflect.Method;
import org.t2framework.commons.aop.spi.Invocation;
import org.t2framework.lucy.tx.Worker;

/**
 * 
 * {@.en }
 * 
 * <br />
 * 
 * {@.ja }
 * 
 * @author shot
 * 
 */
public interface WorkerAdapterFactory {

    Worker<Object> adapt(Invocation<Method> invocation);
}
