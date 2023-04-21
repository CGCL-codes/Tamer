package net.sf.sqlking.spi.client;

import java.lang.reflect.Method;
import java.util.Arrays;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.sqlking.api.client.BeanChanges;
import net.sf.sqlking.api.client.ChangeTracker;
import net.sf.sqlking.spi.client.EntityInfo.PropertyInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;

public class ChangeTrackerInterceptor implements MethodInterceptor {

    private static final Log log = LogFactory.getLog(ChangeTrackerInterceptor.class);

    private Class<?> entityClass;

    private EntityInfo entityInfo;

    private BeanChanges beanChanges;

    public ChangeTrackerInterceptor(Class<?> entityClass) {
        this.entityClass = entityClass;
        if (!ClassUtils.getAllInterfacesAsSet(entityClass).contains(ChangeTracker.class)) {
            beanChanges = new BeanChanges();
        }
        entityInfo = EntityUtils.getEntityInfo(entityClass);
    }

    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        String m = method.getName();
        if ("skGetProperty".equals(m)) {
            PropertyInfo pi = entityInfo.getProperty2info().get(args[0]);
            return ReflectUtils.invokeGetter(pi, proxy);
        }
        if ("skSetProperty".equals(m)) {
            PropertyInfo pi = entityInfo.getProperty2info().get(args[0]);
            return ReflectUtils.invokeSetter(pi, proxy, args[1]);
        }
        if ("setChanges".equals(m) && localBeanChanges()) {
            beanChanges = ((BeanChanges) args[0]);
            return null;
        }
        if ("getChanges".equals(m) && localBeanChanges()) {
            return beanChanges;
        }
        if (Arrays.asList(entityClass.getDeclaredMethods()).contains(method)) {
            String property = StringUtils.uncapitalize(method.getName().substring(3));
            BeanChanges bc = changes(proxy);
            if (m.startsWith("set")) {
                bc.getChangedProperties().add(property);
            } else if (m.startsWith("get")) {
                if (!bc.getLoadedProperties().contains(property)) {
                    log.debug("Property " + property + " of class " + entityClass.getName() + " not loaded by '" + bc.getSelectStatement() + "'");
                }
            }
            return methodProxy.invokeSuper(proxy, args);
        }
        return methodProxy.invokeSuper(proxy, args);
    }

    private boolean localBeanChanges() {
        return beanChanges != null;
    }

    private BeanChanges changes(Object proxy) {
        if (localBeanChanges()) {
            return beanChanges;
        }
        ChangeTracker ct = (ChangeTracker) proxy;
        if (ct.getChanges() == null) {
            BeanChanges bc = new BeanChanges();
            ct.setChanges(bc);
        }
        return ct.getChanges();
    }
}
