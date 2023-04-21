package com.ar4j.spring;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.BeanMetadataAttribute;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.transaction.PlatformTransactionManager;
import com.ar4j.ActiveRecord;
import com.ar4j.ActiveRecordContext;
import com.ar4j.ActiveRecordFactory;
import com.ar4j.sql.INamedQuerySource;
import com.ar4j.util.ObjectCache;

/**
 * A factory that is able to produce new blank active record instances that are read only and have
 * default settings in their context.
 */
public class ReadOnlyActiveRecordFactory {

    private static final String INSTANCE_CACHE_NAME = ReadOnlyActiveRecordFactory.class.getName() + ".instanceCache";

    private static final Set<String> DEFAULT_DISALLOWED_METHODS = new HashSet<String>(Arrays.asList(new String[] { "setProperty", "isNewRecord", "reload", "save" }));

    private static final ObjectCache<CacheKey, ActiveRecord<?>> INSTANCE_CACHE = new ObjectCache<CacheKey, ActiveRecord<?>>(INSTANCE_CACHE_NAME, 512, 0);

    /**
   * Returns a cached or a newly constructed read only instance for the given base class, qualifier, and schema name values. 
   *  
   */
    @SuppressWarnings("unchecked")
    public static <E extends ActiveRecord<E>> E getReadOnlyInstance(ConfigurableListableBeanFactory beanFactory, Class<E> baseClass, String springQualifier, String schemaName) {
        CacheKey key = new CacheKey(baseClass, springQualifier, schemaName);
        E out = (E) INSTANCE_CACHE.get(key);
        if (out == null) {
            DataSource dataSource = getQualifiedBean(beanFactory, true, DataSource.class, key.getQualifier());
            PlatformTransactionManager txManager = getQualifiedBean(beanFactory, false, PlatformTransactionManager.class, key.getQualifier());
            INamedQuerySource namedQuerySource = getQualifiedBean(beanFactory, false, INamedQuerySource.class, key.getQualifier());
            ActiveRecordContext context = new ActiveRecordContext(dataSource, key.getSchemaName(), namedQuerySource, txManager);
            E delegate = ActiveRecordFactory.getFactory().getActiveRecord((Class<E>) key.getBaseClass(), context);
            out = getReadOnlyActiveRecordProxy(delegate);
            INSTANCE_CACHE.put(key, out);
        }
        return out;
    }

    /**
   * Retrieves a qualified bean from the context, if the qualifier is not the default one.
   * If a single value (qualified or not is available) it is returned. For unqualified (or
   * default qualified) retrievals the primary autowire candidate is returned.
   */
    @SuppressWarnings("unchecked")
    private static <E> E getQualifiedBean(ConfigurableListableBeanFactory beanFactory, boolean required, Class<E> beanClass, String qualifier) {
        E out = null;
        if (qualifier == null) {
            Map<String, E> beans = beanFactory.getBeansOfType(beanClass);
            E primary = null;
            for (Map.Entry<String, E> entry : beans.entrySet()) {
                BeanDefinition definition = beanFactory.getBeanDefinition(entry.getKey());
                if (beans.size() == 1 || definition.isPrimary()) {
                    primary = entry.getValue();
                }
            }
            if (beans.size() >= 1 && primary == null) {
                throw new IllegalArgumentException("Found more than one default qualified bean of type: " + beanClass + ", got: " + beans);
            } else {
                out = primary;
            }
        } else {
            String[] beanNames = beanFactory.getBeanNamesForType(beanClass);
            E unqualifiedCandidate = null;
            for (String beanName : beanNames) {
                AbstractBeanDefinition definition = (AbstractBeanDefinition) beanFactory.getBeanDefinition(beanName);
                if (beanNames.length == 1 || definition.isPrimary()) {
                    unqualifiedCandidate = (E) beanFactory.getBean(beanName);
                }
                for (AutowireCandidateQualifier autowireQualifier : definition.getQualifiers()) {
                    BeanMetadataAttribute attr = autowireQualifier.getMetadataAttribute("value");
                    if (qualifier.equals(attr.getValue())) {
                        if (out != null) {
                            throw new IllegalArgumentException("More than one qualified bean found of type: " + beanClass + ", qualifier: " + qualifier);
                        } else {
                            out = (E) beanFactory.getBean(beanName);
                        }
                    }
                }
            }
            if (out == null && unqualifiedCandidate != null) {
                out = unqualifiedCandidate;
            }
        }
        if (out == null && required) {
            throw new IllegalArgumentException("Could not find qualified bean of type: " + beanClass + ", qualifier: " + qualifier);
        }
        return out;
    }

    /**
   * Scans the base class for the name of property setter methods and
   * creates a filtering proxy disallowing access to those methods as well as
   * the default disallowed methods on a blank instance of the base class
   */
    @SuppressWarnings("unchecked")
    private static <E extends ActiveRecord<E>> E getReadOnlyActiveRecordProxy(E delegate) {
        try {
            Set<String> disallowed = new HashSet<String>(DEFAULT_DISALLOWED_METHODS);
            for (String property : delegate.getPropertyNames()) {
                PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(delegate, property);
                Method writeMethod = pd.getWriteMethod();
                if (writeMethod != null) {
                    disallowed.add(writeMethod.getName());
                }
            }
            ActiveRecordFilteringMethodInterceptor interceptor = new ActiveRecordFilteringMethodInterceptor(delegate, disallowed);
            return (E) Enhancer.create(delegate.getBaseClass(), interceptor);
        } catch (Exception e) {
            throw new RuntimeException("Could not create a read only proxy for: " + delegate.getBaseClass(), e);
        }
    }

    /**
   * Key used in the read only instance cache
   */
    private static class CacheKey {

        private Class<?> baseClass;

        private String qualifier;

        private String schemaName;

        public CacheKey(Class<?> baseClass, String qualifier, String schemaName) {
            this.baseClass = baseClass;
            this.qualifier = qualifier;
            this.schemaName = schemaName;
        }

        public Class<?> getBaseClass() {
            return baseClass;
        }

        public String getQualifier() {
            return qualifier;
        }

        public String getSchemaName() {
            return schemaName;
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

    /**
   * A method intercepter that is able to filter specific methods and execute all others on the underlying object.
   */
    private static class ActiveRecordFilteringMethodInterceptor implements MethodInterceptor {

        private ActiveRecord<?> record;

        private Set<String> disallowedMethods;

        public ActiveRecordFilteringMethodInterceptor(ActiveRecord<?> record, Set<String> disallowedMethods) {
            this.record = record;
            this.disallowedMethods = disallowedMethods;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            String name = method.getName();
            if (disallowedMethods.contains(name)) {
                throw new IllegalAccessException("Access to " + name + " of " + obj.getClass().getName() + " is not allowed.");
            }
            return proxy.invoke(record, args);
        }
    }
}
