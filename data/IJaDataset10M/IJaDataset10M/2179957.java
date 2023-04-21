package net.taylor.cache;

import java.io.Serializable;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jboss.seam.cache.CacheProvider;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/**
 * <code>CachedInvocationInterceptor</code> intercepts method invocations and caches the
 * return value of the invocation based on arguments.
 * <p>
 * The cached value will be returned if there is a cache hit and method will not
 * be invoked.
 * <p>
 * Please note that the intercepted method has to return a serializable object.
 * And also the arguments must be primitive java types, or custom data types
 * that provide a toString() method which constructs the string using all the
 * field values.
 * 
 * @author jgilbert01
 * @author czhao
 * @since Dec 12, 2005
 * 
 */
public class CachedInvocationInterceptor {

    private static Log log = Logging.getLog(CachedInvocationInterceptor.class.getName());

    @AroundInvoke
    public Object invoke(InvocationContext ctx) throws Exception {
        CacheProvider<?> cacheProvider = CacheProvider.instance();
        if (cacheProvider == null) {
            log.warn("CacheProvider not found for: {0}", ctx);
            return ctx.proceed();
        }
        String targetName = ctx.getTarget().getClass().getName();
        String methodName = ctx.getMethod().getName();
        Object[] arguments = ctx.getParameters();
        String cacheKey = getCacheKey(targetName, methodName, arguments);
        Object result = cacheProvider.get(cacheKey);
        if (result == null) {
            log.debug("Cache miss for {0}. Invoking the target method.", cacheKey);
            result = ctx.proceed();
            if (result instanceof Serializable) {
                log.debug("Caching the method invocation result for {0}.", cacheKey);
                cacheProvider.put(cacheKey, (Serializable) result);
            } else {
                log.warn("The method {0} of {1} class doesn't return a serializable object," + " CacheInterceptor can't cache the result.", methodName, targetName);
            }
        } else {
            log.debug("Cache hit for {0}. Returning cached result.", cacheKey);
        }
        return result;
    }

    /**
	 * Creates the cache key in the format of: targetClassName.methodName.
	 * argument0.argument1...
	 * 
	 * @param targetName
	 *            Target class name.
	 * @param methodName
	 *            Method name of the invocation
	 * @param arguments
	 *            Method invocation arguments.
	 * @return
	 */
    private String getCacheKey(String targetName, String methodName, Object[] arguments) {
        StringBuffer sb = new StringBuffer();
        sb.append(targetName).append(".").append(methodName);
        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
                sb.append(".").append(ToStringBuilder.reflectionToString(arguments[i], ToStringStyle.SHORT_PREFIX_STYLE));
            }
        }
        return sb.toString();
    }
}
