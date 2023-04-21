package com.google.code.nanorm.internal.introspect.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.code.nanorm.exceptions.IntrospectionException;
import com.google.code.nanorm.internal.QueryDelegate;
import com.google.code.nanorm.internal.config.InternalConfiguration;
import com.google.code.nanorm.internal.config.StatementConfig;
import com.google.code.nanorm.internal.introspect.AbstractIntrospectionFactory;
import com.google.code.nanorm.internal.introspect.Getter;
import com.google.code.nanorm.internal.introspect.IntrospectUtils;
import com.google.code.nanorm.internal.introspect.Setter;

/**
 * ASM based
 * {@link com.google.code.nanorm.internal.introspect.IntrospectionFactory}
 * implementation. Uses runtime code generation to create getters and setters.
 * 
 * TODO: Debugging.
 * 
 * @author Ivan Dubrov
 * @version 1.0 19.06.2008
 */
public class ASMIntrospectionFactory extends AbstractIntrospectionFactory {

    private final ASMClassLoader classLoader;

    private final Map<AccessorKey, Object> accessors;

    private final AtomicInteger counter = new AtomicInteger(0);

    private final Object lock = new Object();

    /**
     * Constructor.
     * 
     * @param parentLoader parent classloader for classloader which will load
     * generated clasess
     */
    public ASMIntrospectionFactory(final ClassLoader parentLoader) {
        classLoader = AccessController.doPrivileged(new PrivilegedAction<ASMClassLoader>() {

            public ASMClassLoader run() {
                return new ASMClassLoader(parentLoader);
            }
        });
        accessors = new ConcurrentHashMap<AccessorKey, Object>();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isAbstractClassesSupported() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public Getter buildGetter(final Class<?> beanClass, final String path) {
        return (Getter) buildAccessorImpl(beanClass, null, path, false);
    }

    /**
     * Create regular getter or parameter getter instance.
     * 
     * @param beanClass bean class; should be null if types is not null
     * @param types parameter types; should be null if beanClass is not null
     * @param path property path
     * @param isSetter if accessor is setter
     * @return
     */
    private Object buildAccessorImpl(final Class<?> beanClass, final Type[] types, final String path, final boolean isSetter) {
        AccessorKey key = types == null ? new AccessorKey(beanClass, path, isSetter) : new AccessorKey(types, path, isSetter);
        Object instance = accessors.get(key);
        if (instance == null) {
            String name = "com/google/code/nanorm/generated/Accessor" + counter.incrementAndGet();
            AccessorBuilder builder = new AccessorBuilder(name, isSetter);
            Type[] finalType = new Type[1];
            byte[] code = types == null ? IntrospectUtils.visitPath(path, beanClass, builder, finalType) : IntrospectUtils.visitPath(path, types, builder, finalType);
            synchronized (lock) {
                instance = accessors.get(key);
                if (instance == null) {
                    Class<?> clazz = defineClass(name.replace('/', '.'), code);
                    try {
                        Constructor<?> ct = clazz.getConstructor(java.lang.reflect.Type.class);
                        instance = ct.newInstance(finalType[0]);
                    } catch (Exception e) {
                        throw new IntrospectionException("Failed to create getter/setter instance!", e);
                    }
                    accessors.put(key, instance);
                }
            }
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    public Getter buildParameterGetter(final java.lang.reflect.Type[] types, final String path) {
        return (Getter) buildAccessorImpl(null, types, path, false);
    }

    /**
     * {@inheritDoc}
     */
    public Setter buildSetter(final Class<?> beanClass, final String path) {
        return (Setter) buildAccessorImpl(beanClass, null, path, true);
    }

    /**
     * {@inheritDoc}
     */
    public Setter buildParameterSetter(final Type[] types, final String path) {
        return (Setter) buildAccessorImpl(null, types, path, true);
    }

    /**
     * {@inheritDoc}
     */
    public <T> T createMapper(Class<T> mapper, InternalConfiguration config, QueryDelegate delegate) {
        List<MethodConfig> methods = new ArrayList<MethodConfig>();
        List<StatementConfig> configs = new ArrayList<StatementConfig>();
        for (java.lang.reflect.Method m : mapper.getMethods()) {
            if (Modifier.isAbstract(m.getModifiers())) {
                StatementConfig stConfig = config.getStatementConfig(mapper, m);
                MethodConfig cfg = new MethodConfig(m, methods.size());
                methods.add(cfg);
                configs.add(stConfig);
            }
        }
        String name = "com/google/code/nanorm/generated/Mapper" + counter.incrementAndGet();
        byte[] code = MapperBuilder.buildMapper(name, mapper, methods.toArray(new MethodConfig[methods.size()]));
        Class<?> clazz = defineClass(name.replace('/', '.'), code);
        Object instance;
        try {
            Constructor<?> ctor = clazz.getConstructor(QueryDelegate.class, StatementConfig[].class);
            instance = ctor.newInstance(delegate, configs.toArray(new StatementConfig[configs.size()]));
        } catch (Exception e) {
            throw new IntrospectionException("Failed to create mapper instance!", e);
        }
        return mapper.cast(instance);
    }

    /**
     * Define class in the classloader.
     * 
     * @param name
     * @param b
     * @return
     */
    private Class<?> defineClass(final String name, final byte[] b) {
        return AccessController.doPrivileged(new PrivilegedAction<Class<?>>() {

            public Class<?> run() {
                return classLoader.defineClassInternal(name, b);
            }
        });
    }

    /**
     * Classloader used for accessors.
     * 
     * @author Ivan Dubrov
     * @version 1.0 21.06.2008
     */
    private static class ASMClassLoader extends ClassLoader {

        /**
         * Constructor.
         * 
         * @param parent parent classloader
         */
        ASMClassLoader(ClassLoader parent) {
            super(parent);
        }

        /**
         * Define class in the classloader.
         * 
         * @param name class name
         * @param b code
         * @return {@link Class} instance.
         */
        public Class<?> defineClassInternal(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }
}
