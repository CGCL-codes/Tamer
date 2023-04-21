package org.spruice;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.support.GenericApplicationContext;
import org.spruice.proxy.BeanCallback;
import org.spruice.proxy.ProxyCreator;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

public class BeanConfig<T> implements LifecycleConfig<T>, ContextEntry {

    private final String name;

    private final BeanDefinitionBuilder builder;

    private String startMethod;

    private ProxyCreator objFactory;

    private Class<?> proxyClzz;

    private String stopMethod;

    private boolean isPrimary;

    public BeanConfig(String name, BeanDefinitionBuilder builder, ProxyCreator fact, Class<T> clzz) {
        this.name = name;
        this.builder = builder;
        this.objFactory = fact;
        this.proxyClzz = clzz;
    }

    public BeanConfig<T> destroyWith(String destroyMethod) {
        builder.setDestroyMethodName(destroyMethod);
        return this;
    }

    public BeanConfig<T> initWith(String initMethod) {
        builder.setInitMethodName(initMethod);
        return this;
    }

    public String getName() {
        return name;
    }

    public BeanDefinitionBuilder getBuilder() {
        return builder;
    }

    public BeanConfig<T> startWith(String startMethod) {
        this.startMethod = startMethod;
        return this;
    }

    public T initWith() {
        MethodInterceptor advice = new MethodInterceptor() {

            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                validateArgs(args);
                initWith(method.getName());
                return null;
            }
        };
        return createProxy(advice);
    }

    private void validateArgs(Object[] args) {
        if (args == null || args.length == 0) return;
        throw new IllegalArgumentException("Arguments cannot be passed to bean lifecycle methods.");
    }

    public T destroyWith() {
        MethodInterceptor advice = new MethodInterceptor() {

            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                validateArgs(args);
                destroyWith(method.getName());
                return null;
            }
        };
        return createProxy(advice);
    }

    public T startWith() {
        MethodInterceptor advice = new MethodInterceptor() {

            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                validateArgs(args);
                startWith(method.getName());
                return null;
            }
        };
        return createProxy(advice);
    }

    T createProxy(final MethodInterceptor advice) {
        return (T) objFactory.imposterise(new BeanCallback(name, advice), proxyClzz);
    }

    public BeanConfig<T> constructWith(Object s) {
        String id = resolveBeanNameFromObject(s);
        if (id != null) {
            return constructWithRef(id);
        }
        builder.addConstructorArgValue(s);
        return this;
    }

    public BeanConfig<T> constructWith(int index, Object bean) {
        String id = resolveBeanNameFromObject(bean);
        if (id != null) {
            return constructWithRef(index, id);
        }
        builder.getBeanDefinition().getConstructorArgumentValues().addIndexedArgumentValue(index, bean);
        return this;
    }

    public BeanConfig<T> constructWithRef(String beanId) {
        builder.addConstructorArgReference(beanId);
        return this;
    }

    public BeanConfig<T> constructWithRef(int index, String beanId) {
        return constructWith(index, new RuntimeBeanReference(beanId));
    }

    /**
     * Given a proxy or config object, the method will attemp to resolve the id specified for the bean.
     *
     * @param bean
     * @return the id or null if it can't be resolved.
     */
    static String resolveBeanNameFromObject(Object bean) {
        if (bean instanceof Factory) {
            return getBeanName((Factory) bean);
        } else if (bean instanceof NamedBean) {
            return ((NamedBean) bean).getName();
        }
        return null;
    }

    public BeanConfig<T> set(String propertyName, Object value) {
        builder.addPropertyValue(propertyName, value);
        return this;
    }

    public BeanConfig<T> config(Properties props) {
        Map<String, String> filtered = InstanceConfig.filterByName(props, getName());
        for (Map.Entry<String, String> entry : filtered.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public T set() {
        MethodInterceptor advice = new MethodInterceptor() {

            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                String propName = parseProperty(method.getName());
                Object value = args[0];
                String id = resolveBeanNameFromObject(value);
                if (id != null) {
                    setRef(propName, id);
                } else {
                    set(propName, value);
                }
                return null;
            }
        };
        return createProxy(advice);
    }

    public static String parseProperty(String name) {
        String property = name.substring(3);
        return CamelCaseNamer.format(property);
    }

    public static String getBeanName(Factory f) {
        Callback[] callbacks = f.getCallbacks();
        for (Callback callback : callbacks) {
            if (callback instanceof BeanCallback) {
                return ((BeanCallback) callback).getName();
            }
        }
        return null;
    }

    public BeanConfig<T> setRef(String propertyName, String beanName) {
        builder.addPropertyReference(propertyName, beanName);
        return this;
    }

    public String getStartMethod() {
        return startMethod;
    }

    public String getStopMethod() {
        return stopMethod;
    }

    public T stopWith() {
        MethodInterceptor advice = new MethodInterceptor() {

            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                stopMethod = method.getName();
                return null;
            }
        };
        return createProxy(advice);
    }

    public <N> BeanConfig<N> configureWith(Class<N> targetInterface) {
        proxyClzz = targetInterface;
        return (BeanConfig<N>) this;
    }

    public T asRef() {
        MethodInterceptor advice = new MethodInterceptor() {

            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return null;
            }
        };
        return createProxy(advice);
    }

    public void addTo(GenericApplicationContext context) {
        AbstractBeanDefinition def = builder.getBeanDefinition();
        def.setPrimary(isPrimary);
        String beanName = getName();
        context.registerBeanDefinition(beanName, def);
        if (getStartMethod() != null || getStopMethod() != null) {
            BeanDefinitionBuilder startBuilder = BeanDefinitionBuilder.genericBeanDefinition(ContextLifecycle.class);
            startBuilder.addDependsOn(beanName);
            startBuilder.addPropertyReference("target", getName());
            if (getStartMethod() != null) startBuilder.addPropertyValue("startMethod", getStartMethod());
            if (getStopMethod() != null) startBuilder.addPropertyValue("stopMethod", getStopMethod());
            context.registerBeanDefinition(beanName + "LifecycleMethods", startBuilder.getBeanDefinition());
        }
    }

    public BeanConfig<T> setPrimary(boolean b) {
        isPrimary = b;
        return this;
    }
}
