package org.eralyautumn.common.interceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.eralyautumn.common.GlobalBase;
import org.eralyautumn.common.annotations.NoCache;
import org.eralyautumn.common.persistence.MapManager;

/**
 * Service拦截器
 * 
 * @author <a href="mailto:fmlou@163.com">HongzeZhang</a>
 * 
 * @version 1.0
 * 
 * @since 2010-8-11
 */
public class CacheInterceptor extends GlobalBase implements MethodInterceptor {

    @Autowired
    private MapManager mapManager;

    public Object invoke(MethodInvocation mi) throws Throwable {
        Class<?> clazz = mi.getThis().getClass();
        Method method = mi.getMethod();
        if (method.getAnnotation(NoCache.class) != null) return mi.proceed();
        Object[] arguments = mi.getArguments();
        logger.debug("------CacheInterceptor-class=" + clazz.getSimpleName() + ",method=" + method.getName() + ",parmas=" + Arrays.toString(arguments));
        String key = this.getCacheKey(clazz, method, arguments);
        Object returnObj = mapManager.load(key);
        if (returnObj == null) {
            returnObj = mi.proceed();
            mapManager.save(key, returnObj);
        } else {
            logger.debug("Find local cache : " + returnObj);
        }
        return returnObj;
    }

    public String getCacheKey(Class<?> clazz, Method method, Object[] arguments) {
        return clazz.getName() + ":" + method.getName() + ":" + Arrays.toString(method.getParameterTypes()) + Arrays.toString(arguments);
    }
}
