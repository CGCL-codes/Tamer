package com.horstmann.violet.framework.injection.bean;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Manioc is a very very (VERY!) light and basic injection framework. <br/>
 * IT ONLY CONTAINS ONE JAVA SOURCE FILE!!!. Just copy it into your project. <br/>
 * <br/>
 * Source code version : 0.0.1-SNAPSHOT <br/>
 * <br/>
 * Release note : <br/>
 * Version 0.0.1 (07-21-2011) : <br/>
 * + contains a BeanFactory and a BeanInjector<br/>
 * + can manage beans annotated with \@ManagedBean<br/>
 * + supports injection on fields with \@Inject<br/>
 * + supports bean construction on specific constructor or factory method with \@Construct
 * <br/>
 * + automatically injects beans on constructor and factory method without
 * specifying any annotation<br/>
 * + supports \@PostConstruct and \@PreDestroy<br/>
 * + supports multiple injection contexts with hierarchy with
 * \@ManagedBean(context)<br/>
 * + supports bean scope (singleton and prototype) with \@ManagedBean(scope)<br/>
 * + supports injection by annotation on existing beans<br/>
 * + supports manual registration of pre-construct beans on the bean factory
 * with BeanFactory.register()<br/>
 * 
 * Version 0.0.2 (10-07-2011) : <br/>
 * + bug fix on manual bean registration with interface type <br/>
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class ManiocFramework {

    /**
     * This injector is made to perform dependency injection on objects. This
     * class was inspired from the excellent Apache Wicket framework.
     * 
     * @author Alexandre de Pellegrin
     * 
     */
    public static class BeanInjector {

        /**
         * Singleton instance
         */
        private static BeanInjector instance;

        /**
         * Singleton constructor
         */
        private BeanInjector() {
        }

        /**
         * @return the only object instance
         */
        public static BeanInjector getInjector() {
            if (BeanInjector.instance == null) {
                BeanInjector.instance = new BeanInjector();
            }
            return BeanInjector.instance;
        }

        /**
         * Injects beans on fields annotated with \@InjectedBean <br/>
         * For managed beans, you can specified the application context you want <br>
         * directly in the \@ManagedBean annotation.
         * 
         * @param o
         */
        public void inject(Object o) {
            Class<? extends Object> implementationType = o.getClass();
            ManagedBean annotation = implementationType.getAnnotation(ManagedBean.class);
            if (annotation != null) {
                Class<? extends DefaultApplicationContext> context = annotation.applicationContext();
                this.inject(o, context);
                return;
            }
            if (annotation == null) {
                this.inject(o, DefaultApplicationContext.class);
            }
        }

        /**
         * Injects beans in fields annotated with \@InjectedBean. <br/>
         * Beans are taken from the specified application context. </br>
         * 
         * @param o
         * @param context
         */
        public void inject(Object o, Class<? extends DefaultApplicationContext> context) {
            List<Class<?>> classAndSuperClasses = getClassAndSuperClasses(o);
            for (Class<?> aClass : classAndSuperClasses) {
                for (Field aField : aClass.getDeclaredFields()) {
                    InjectedBean propertyAnnotation = aField.getAnnotation(InjectedBean.class);
                    if (propertyAnnotation != null) {
                        aField.setAccessible(true);
                        if (!isFieldNUll(aField, o)) {
                            continue;
                        }
                        Class<?> beanType = aField.getType();
                        boolean isInterface = beanType.isInterface();
                        if (isInterface) {
                            boolean isImplementationFound = false;
                            ImplementedBy defaultImplementationAnnotation = beanType.getAnnotation(ImplementedBy.class);
                            if (defaultImplementationAnnotation != null) {
                                beanType = defaultImplementationAnnotation.value();
                                isImplementationFound = true;
                            }
                            if (!Object.class.equals(propertyAnnotation.implementation())) {
                                beanType = propertyAnnotation.implementation();
                                isImplementationFound = true;
                            }
                            if (BeanFactory.getFactory(context).getBean(beanType) != null) {
                                isImplementationFound = true;
                            }
                            if (!isImplementationFound) {
                                throw new ManiocException("Unable to find which implementation to use for a bean of type " + aField.getType().getName() + " . Application context is " + context.getSimpleName());
                            }
                        }
                        Object beanToInject = BeanFactory.getFactory(context).getBean(beanType);
                        if (beanToInject == null) {
                            if (isInterface) {
                                throw new ManiocException("Unable to inject a bean which implements " + aField.getType().getName() + " . Implementation excepted is " + beanType.getName() + ". Application context is " + context.getSimpleName());
                            }
                            if (!isInterface) {
                                throw new ManiocException("Unable to inject a bean of type " + beanType.getName() + " . No such bean found. Application context is " + context.getSimpleName());
                            }
                        }
                        try {
                            aField.set(o, beanToInject);
                        } catch (IllegalArgumentException e) {
                            throw new ManiocException("Error while setting field value of bean managed by the BeanFactory Application context is " + context.getSimpleName(), e);
                        } catch (IllegalAccessException e) {
                            throw new ManiocException("Error while setting field value of bean managed by the BeanFactory Application context is " + context.getSimpleName(), e);
                        }
                    }
                }
            }
        }

        /**
         * Takes an objet and returns its class and all its inherited classes
         * 
         * @param o
         * @return
         */
        private List<Class<?>> getClassAndSuperClasses(Object o) {
            List<Class<?>> result = new ArrayList<Class<?>>();
            List<Class<?>> fifo = new ArrayList<Class<?>>();
            fifo.add(o.getClass());
            while (!fifo.isEmpty()) {
                Class<?> aClass = fifo.remove(0);
                ;
                Class<?> aSuperClass = aClass.getSuperclass();
                if (aSuperClass != null) {
                    fifo.add(aSuperClass);
                }
                result.add(aClass);
            }
            return result;
        }

        /**
         * Checks if a field is empty (null value)
         * 
         * @param aField
         * @param o
         * @return true if null
         */
        private boolean isFieldNUll(Field aField, Object o) {
            try {
                Object currentValue = aField.get(o);
                if (currentValue == null) {
                    return true;
                }
            } catch (IllegalArgumentException e1) {
                throw new ManiocException("Error while getting field value of bean managed by the BeanFactory. Field = " + aField.getName(), e1);
            } catch (IllegalAccessException e1) {
                throw new ManiocException("Error while getting field value of bean managed by the BeanFactory. Field = " + aField.getName(), e1);
            }
            return false;
        }
    }

    public static class BeanFactory {

        private Map<Class<?>, Object> singletonsMap = new HashMap<Class<?>, Object>();

        private Class<? extends DefaultApplicationContext> context;

        private static Map<Class<? extends DefaultApplicationContext>, BeanFactory> factoryList = new HashMap<Class<? extends DefaultApplicationContext>, BeanFactory>();

        private static Map<Object, Class<? extends DefaultApplicationContext>> managedBeansOnEveryContexts = new HashMap<Object, Class<? extends DefaultApplicationContext>>();

        /**
         * Listen to JVM shutdown to free resources on all managed beans
         */
        static {
            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    Set<Object> beanSet = BeanFactory.managedBeansOnEveryContexts.keySet();
                    for (Object aBean : beanSet) {
                        Class<? extends DefaultApplicationContext> beanContext = BeanFactory.managedBeansOnEveryContexts.get(aBean);
                        BeanFactory.preDestroy(aBean, beanContext);
                    }
                }
            });
        }

        /**
         * Singleton constructor
         */
        private BeanFactory() {
        }

        /**
         * @return the default factory
         */
        public static BeanFactory getFactory() {
            return BeanFactory.getFactory(DefaultApplicationContext.class);
        }

        /**
         * @param context
         * @return factory from the specified scope
         */
        public static BeanFactory getFactory(Class<? extends DefaultApplicationContext> context) {
            if (!BeanFactory.factoryList.containsKey(context)) {
                BeanFactory newInstance = new BeanFactory();
                newInstance.context = context;
                BeanFactory.factoryList.put(context, newInstance);
            }
            return BeanFactory.factoryList.get(context);
        }

        /**
         * Checks if the current factory has already worked whith a bean of the
         * specified type
         * 
         * @param <T>
         * @param classType
         * @return true if found
         */
        public <T> boolean contains(Class<T> classType) {
            Set<Object> beanSet = BeanFactory.managedBeansOnEveryContexts.keySet();
            for (Object aBean : beanSet) {
                Class<? extends DefaultApplicationContext> beanContext = BeanFactory.managedBeansOnEveryContexts.get(aBean);
                if (!beanContext.equals(this.context)) {
                    continue;
                }
                if (classType.isInstance(aBean)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Returns the bean of the specified type
         * 
         * @param <T>
         * @param classType
         * @return
         */
        public <T> T getBean(Class<T> classType) {
            boolean isRegisteredManually = isRegisteredManually(classType);
            checkVisibilityFromCurrentContext(classType);
            T bean = getAlreadyExistingBean(classType);
            if (isRegisteredManually) {
                if (bean == null) {
                    throw new ManiocException("Unable to find a bean of type " + classType.getName() + " which is annotated with @" + ManagedBean.class.getSimpleName() + "(registeredManually=true). Application context is " + this.context.getSimpleName());
                }
            }
            if (bean != null) {
                return bean;
            }
            if (!classType.isInterface()) {
                return createBean(classType);
            }
            return null;
        }

        /**
         * Returns the creation context of the given bean or null if no bean
         * found
         * 
         * @param bean
         * @return the context class if bean found
         */
        public static Class<? extends DefaultApplicationContext> getBeanContext(Object bean) {
            Class<? extends DefaultApplicationContext> beanContext = BeanFactory.managedBeansOnEveryContexts.get(bean);
            return beanContext;
        }

        /**
         * Checks if this kind of class is reachable from the context of the
         * factory
         * 
         * @param <T>
         * @param classType
         */
        private <T> void checkVisibilityFromCurrentContext(Class<T> classType) {
            ManagedBean annotation = classType.getAnnotation(ManagedBean.class);
            if (annotation == null) {
                return;
            }
            Class<? extends DefaultApplicationContext> reachableContext = annotation.applicationContext();
            boolean isClassContextIsChildOfCurrent = reachableContext.isAssignableFrom(this.context);
            if (!isClassContextIsChildOfCurrent) {
                throw new ManiocException("Cannot get a bean type " + classType.getName() + " from context " + this.context.getSimpleName() + " because it has been created in " + reachableContext.getSimpleName() + " which is not a child of " + this.context.getSimpleName());
            }
        }

        /**
         * Checks if the factory should contain only one instance of this class. <br/>
         * There's two cases : \@ManagerBean.scope() is set to 'singleton' or
         * \@ManagedBean.registerManually() is set to true
         * 
         * @param <T>
         * @param classType
         * @return true if singleton
         */
        private <T> boolean isSingleton(Class<T> classType) {
            ManagedBean annotation = classType.getAnnotation(ManagedBean.class);
            if (annotation == null) {
                return false;
            }
            BeanScope beanScope = annotation.scope();
            if (beanScope.equals(BeanScope.SINGLETON)) {
                return true;
            }
            if (annotation.registeredManually()) {
                return true;
            }
            return false;
        }

        /**
         * Checks if this kind of bean should be or not created by the factory
         * 
         * @param <T>
         * @param classType
         * @return true if the class is annotated with
         *         \@ManagedBean(registeredManually=true)
         */
        private <T> boolean isRegisteredManually(Class<T> classType) {
            ManagedBean annotation = classType.getAnnotation(ManagedBean.class);
            if (annotation == null) {
                return false;
            }
            return annotation.registeredManually();
        }

        /**
         * Looks for an existing bean inside all the contexts hierarchy
         * 
         * @param <T>
         * @param classType
         * @return
         */
        private <T> T getAlreadyExistingBean(Class<T> classType) {
            if (singletonsMap.containsKey(classType)) {
                Object object = singletonsMap.get(classType);
                Class<? extends Object> implementationType = object.getClass();
                ManagedBean annotation = implementationType.getAnnotation(ManagedBean.class);
                if (annotation == null) {
                    throw new ManiocException("Error on " + implementationType.getName() + " . All the class instances you want to inject must have the annotation @" + ManagedBean.class.getSimpleName() + " declared. Application context is " + this.context.getSimpleName());
                }
                return (T) object;
            }
            BeanFactory parentBeanFactory = getParentBeanFactory();
            if (parentBeanFactory != null) {
                return parentBeanFactory.getAlreadyExistingBean(classType);
            }
            return null;
        }

        /**
         * @return the parent bean factory or null if this context has no parent
         */
        private BeanFactory getParentBeanFactory() {
            Class<? extends DefaultApplicationContext> parentContext = getParentContext();
            if (parentContext == null) {
                return null;
            }
            return BeanFactory.factoryList.get(parentContext);
        }

        /**
         * Looks for the parent context of this context if it exists
         * 
         * @param childContext
         * @return the parent context or null if there's no parent
         */
        private Class<? extends DefaultApplicationContext> getParentContext() {
            Class<?> aSuperClass = this.context.getSuperclass();
            if (aSuperClass != null && aSuperClass.isAssignableFrom(DefaultApplicationContext.class)) {
                return (Class<? extends DefaultApplicationContext>) aSuperClass;
            }
            return null;
        }

        /**
         * Determines all the parent contexts that the given context can see
         * 
         * @param childContext
         * @return a list of contexts from the child to the oldest parent
         */
        private List<Class<? extends DefaultApplicationContext>> getContextHierarchy(Class<? extends DefaultApplicationContext> childContext) {
            List<Class<? extends DefaultApplicationContext>> result = new ArrayList<Class<? extends DefaultApplicationContext>>();
            List<Class<? extends DefaultApplicationContext>> fifo = new ArrayList<Class<? extends DefaultApplicationContext>>();
            fifo.add(childContext);
            while (!fifo.isEmpty()) {
                Class<? extends DefaultApplicationContext> aClass = fifo.remove(0);
                Class<?> aSuperClass = aClass.getSuperclass();
                if (aSuperClass != null && aSuperClass.isAssignableFrom(DefaultApplicationContext.class)) {
                    fifo.add((Class<? extends DefaultApplicationContext>) aSuperClass);
                }
                result.add(aClass);
            }
            return result;
        }

        /**
         * Register the given bean in the current BeanFactory. If this bean's
         * fields are annotated with @Inject, they would be injected too.<br/>
         * 
         * @param classType
         *            (prefer using the interface here)
         * @param aBean
         */
        public <T> void register(Class<T> classType, T bean) {
            boolean isInterface = classType.isInterface();
            if (!isInterface) {
                checkIfBeanIsManageable(classType);
                boolean isSingleton = isSingleton(classType);
                if (isSingleton) {
                    if (singletonsMap.containsKey(classType)) {
                        throw new ManiocException("Duplicate beans of type " + classType.getName() + " in the context " + this.context.getSimpleName());
                    }
                    singletonsMap.put(classType, bean);
                }
            }
            if (isInterface) {
                checkIfBeanIsManageable(bean.getClass());
                boolean isSingleton = isSingleton(bean.getClass());
                if (isSingleton) {
                    if (singletonsMap.containsKey(classType)) {
                        throw new ManiocException("Duplicate beans of type " + classType.getName() + " in the context " + this.context.getSimpleName());
                    }
                    if (singletonsMap.containsKey(bean.getClass())) {
                        throw new ManiocException("Duplicate beans of type " + bean.getClass().getName() + " in the context " + this.context.getSimpleName());
                    }
                    singletonsMap.put(classType, bean);
                    singletonsMap.put(bean.getClass(), bean);
                }
            }
            BeanInjector beanInjector = BeanInjector.getInjector();
            try {
                beanInjector.inject(bean);
            } catch (ManiocException re) {
                singletonsMap.remove(classType);
                throw new ManiocException("Error while registering a bean of type " + classType.getName() + " . Application context is " + this.context.getSimpleName(), re);
            }
            BeanFactory.managedBeansOnEveryContexts.put(bean, this.context);
        }

        /**
         * Creates a new class instance (on the desired context if specified)
         * 
         * @param <T>
         * @param classType
         * @return the newly created bean
         */
        private <T> T createBean(Class<T> classType) {
            checkIfBeanIsManageable(classType);
            boolean isRegisteredManually = isRegisteredManually(classType);
            if (isRegisteredManually) {
                throw new ManiocException("Cannot create a bean of type " + classType.getName() + " because it has the annotation @" + ManagedBean.class.getSimpleName() + ".registeredManually() set to true. Current context is " + this.context.getSimpleName());
            }
            ManagedBean annotation = classType.getAnnotation(ManagedBean.class);
            Class<? extends DefaultApplicationContext> classTypeContext = annotation.applicationContext();
            BeanFactory classTypeFactory = BeanFactory.getFactory(classTypeContext);
            T newInstance = classTypeFactory.createBeanFromAnnotatedConstructor(classType);
            if (newInstance == null) {
                newInstance = classTypeFactory.createBeanFromAnnotatedFactoryMethod(classType);
            }
            if (newInstance == null) {
                newInstance = classTypeFactory.createBeanFromDefaultConstructor(classType);
            }
            if (newInstance != null) {
                classTypeFactory.register(classType, newInstance);
                classTypeFactory.postConstruct(classType, newInstance);
                return newInstance;
            }
            throw new ManiocException("BeanFactory unexpected error. Failed to create bean of type " + classType.getName());
        }

        /**
         * @param <T>
         * @param classType
         * @return newly created bean
         */
        private <T> T createBeanFromDefaultConstructor(Class<T> classType) {
            Constructor<T> defaultConstructor = getDefaultConstructor(classType);
            if (defaultConstructor != null) {
                try {
                    T newInstance = classType.newInstance();
                    return newInstance;
                } catch (Exception e) {
                    throw new ManiocException("BeanFactory failed to create bean of type " + classType.getName() + " from its default constructor.  Application context is " + this.context.getSimpleName(), e);
                }
            }
            return null;
        }

        /**
         * @param <T>
         * @param classType
         * @return newly created bean
         */
        private <T> T createBeanFromAnnotatedFactoryMethod(Class<T> classType) {
            Method annotatedFactoryMethod = getAnnotatedFactoryMethod(classType);
            if (annotatedFactoryMethod != null) {
                Class<?>[] parameterTypes = annotatedFactoryMethod.getParameterTypes();
                Object[] parameterBeans = BeanFactory.getBeans(parameterTypes, this.context);
                try {
                    T newInstance = (T) annotatedFactoryMethod.invoke(null, parameterBeans);
                    return newInstance;
                } catch (Exception e) {
                    throw new ManiocException("BeanFactory failed to create bean of type " + classType.getName() + " from its method " + annotatedFactoryMethod.getName() + ". Application context is " + this.context.getSimpleName(), e);
                }
            }
            return null;
        }

        /**
         * @param <T>
         * @param classType
         * @return a nealy created bean
         */
        private <T> T createBeanFromAnnotatedConstructor(Class<T> classType) {
            Constructor<T> annotatedConstructor = getAnnotatedConstructor(classType);
            if (annotatedConstructor != null) {
                Class<?>[] parameterTypes = annotatedConstructor.getParameterTypes();
                Object[] parameterBeans = BeanFactory.getBeans(parameterTypes, this.context);
                try {
                    T newInstance = annotatedConstructor.newInstance(parameterBeans);
                    return newInstance;
                } catch (Exception e) {
                    throw new ManiocException("BeanFactory failed to create bean of type " + classType.getName() + " from its annotated constructor .  Application context is " + this.context.getSimpleName(), e);
                }
            }
            return null;
        }

        /**
         * Calls all methods annotated with \@PostConstruct. Injects parameters
         * bean when it's possible (or inject a null value).<br/>
         * This is used to initialize beans after construction.<br/>
         * 
         * @param <T>
         * @param beanType
         * @param bean
         *            which need to be initialized by calling some post
         *            construction methods
         */
        private <T> void postConstruct(Class<T> beanType, Object bean) {
            Method[] methods = beanType.getMethods();
            for (Method aMethod : methods) {
                PostConstruct annotation = aMethod.getAnnotation(PostConstruct.class);
                if (annotation == null) {
                    continue;
                }
                Class<?>[] parameterTypes = aMethod.getParameterTypes();
                Object[] parameterBeans = BeanFactory.getBeans(parameterTypes, this.context);
                try {
                    aMethod.invoke(bean, parameterBeans);
                } catch (Exception e) {
                    throw new ManiocException("BeanFactory failed to invoke the post construction method '" + aMethod.getName() + "' on the bean of type  " + beanType.getName() + ". Application context is " + this.context.getSimpleName(), e);
                }
            }
        }

        /**
         * Calls all methods annotated with \@PreDetroy. Injects parameters bean
         * when it's possible (or inject a null value).<br/>
         * This is used to free resources on beans on JVM shutdown.<br/>
         * 
         * @param bean
         * @param context
         */
        private static void preDestroy(Object bean, Class<? extends DefaultApplicationContext> context) {
            Class<? extends Object> beanType = bean.getClass();
            Method[] methods = beanType.getMethods();
            for (Method aMethod : methods) {
                PreDestroy annotation = aMethod.getAnnotation(PreDestroy.class);
                if (annotation == null) {
                    continue;
                }
                Class<?>[] parameterTypes = aMethod.getParameterTypes();
                Object[] parameterBeans = BeanFactory.getBeans(parameterTypes, context);
                try {
                    aMethod.invoke(bean, parameterBeans);
                } catch (Exception e) {
                    throw new ManiocException("BeanFactory failed to invoke the pre detroy method '" + aMethod.getName() + "' on the bean of type  " + beanType.getName() + ". Application context is " + context.getName(), e);
                }
            }
        }

        /**
         * Takes an array of classes + a context and return an array containing
         * beans from this context. <br/>
         * Useful to get beans to inject in a constructor or a method. <br/>
         * 
         * @param classTypes
         * @param context
         * @return an array of beans. Could contain null value if no bean is
         *         found.
         */
        private static Object[] getBeans(Class<?>[] classTypes, Class<? extends DefaultApplicationContext> context) {
            BeanFactory contextFactory = factoryList.get(context);
            Object[] beans = new Object[classTypes.length];
            for (int i = 0; i < classTypes.length; i++) {
                Class<?> aClassType = classTypes[i];
                Object beanToInject = contextFactory.getBean(aClassType);
                beans[i] = beanToInject;
            }
            return beans;
        }

        /**
         * Verifies if this class is annotated with \@ManagedBean <br/>
         * which means that this factory can handle it
         * 
         * @param <T>
         * @param classType
         * @throws ManiocException
         *             if the class is not annotated with \@ManagedBean
         */
        private <T> void checkIfBeanIsManageable(Class<T> classType) throws ManiocException {
            ManagedBean annotation = classType.getAnnotation(ManagedBean.class);
            if (annotation == null) {
                throw new ManiocException("Error on " + classType + " . All the class instances you want to inject must have the annotation @" + ManagedBean.class.getSimpleName() + " declared. Application context is " + this.context.getSimpleName());
            }
        }

        /**
         * @param <T>
         * @param classType
         * @return the constructor annotated with \@Constructor or null if no
         *         one is found
         */
        private <T> Constructor<T> getAnnotatedConstructor(Class<T> classType) {
            Constructor<T>[] constructors = (Constructor<T>[]) classType.getConstructors();
            List<Constructor<T>> result = new ArrayList<Constructor<T>>();
            for (Constructor<T> aConstructor : constructors) {
                int modifiers = aConstructor.getModifiers();
                if ((modifiers & Modifier.PUBLIC) != 0) {
                    continue;
                }
                Construct annotation = aConstructor.getAnnotation(Construct.class);
                if (annotation == null) {
                    continue;
                }
                result.add(aConstructor);
            }
            if (result.size() == 1) {
                return result.get(0);
            }
            if (result.size() > 1) {
                throw new ManiocException("BeanFactory cannot determine which constructor to use on " + classType.getName() + " because there's more than one constructor annotated with @" + Construct.class.getSimpleName());
            }
            return null;
        }

        /**
         * @param <T>
         * @param classType
         * @return the method annotated with \@Constructor or null if no one is
         *         found
         */
        private <T> Method getAnnotatedFactoryMethod(Class<T> classType) {
            Method[] methods = classType.getMethods();
            List<Method> result = new ArrayList<Method>();
            for (Method aMethod : methods) {
                Class<?> returnType = aMethod.getReturnType();
                if (!returnType.equals(classType)) {
                    continue;
                }
                int modifiers = aMethod.getModifiers();
                if ((modifiers & Modifier.PUBLIC & Modifier.STATIC) != 0) {
                    continue;
                }
                Construct annotation = aMethod.getAnnotation(Construct.class);
                if (annotation == null) {
                    continue;
                }
                result.add(aMethod);
            }
            if (result.size() == 1) {
                return result.get(0);
            }
            if (result.size() > 1) {
                throw new ManiocException("BeanFactory cannot determine which method to use to construct a bean on " + classType.getName() + " because there's more than one method annotated with @" + Construct.class.getSimpleName());
            }
            return null;
        }

        /**
         * @param classType
         * @return the constructor with no parameter or null if no one if found
         */
        private <T> Constructor<T> getDefaultConstructor(Class<T> classType) {
            Constructor<T>[] constructors = (Constructor<T>[]) classType.getConstructors();
            for (Constructor<T> aConstructor : constructors) {
                Class<?>[] parameterTypes = aConstructor.getParameterTypes();
                if (parameterTypes.length == 0) {
                    return aConstructor;
                }
            }
            return null;
        }
    }

    /**
     * Allows to isolate beans on different scopes. All scopes need to
     * inheritate from this interface. A child scope can see beans from its
     * parents.
     * 
     * @author Alexandre de Pellegrin
     * 
     */
    public static class DefaultApplicationContext {
    }

    /**
     * Annotation used to mark methods to call to instance a class
     * 
     * @author Alexandre de Pellegrin
     * 
     */
    @Target({ METHOD, CONSTRUCTOR })
    @Retention(value = RetentionPolicy.RUNTIME)
    public @interface Construct {
    }

    /**
     * Annotation used to inject beans managed by ManiocFactory
     * 
     * @author Alexandre de Pellegrin
     * 
     */
    @Target({ FIELD })
    @Retention(value = RetentionPolicy.RUNTIME)
    public @interface InjectedBean {

        /**
         * @return the desired implementation. Optional. If not precised, it
         *         takes the field's type
         */
        public Class<?> implementation() default Object.class;
    }

    /**
     * Annotation used to specify a default implementation on an interface
     * 
     * @author Alexandre de Pellegrin
     * 
     */
    @Target({ TYPE })
    @Retention(value = RetentionPolicy.RUNTIME)
    public @interface ImplementedBy {

        /**
         * @return the desired implementation. Optional. If not precised, it
         *         takes the field's type
         */
        public Class<?> value();
    }

    /**
     * Annotation used to auto create beans with the BeanFactory
     * 
     * @author Alexandre de Pellegrin
     * 
     */
    @Target({ TYPE })
    @Retention(value = RetentionPolicy.RUNTIME)
    public @interface ManagedBean {

        /**
         * @return true if the bean instance is automatically created by the
         *         BeanFactory or registered manually
         */
        public boolean registeredManually() default false;

        /**
         * @return the context of the bean (which is linked to its visibility
         *         with other contexts)
         */
        public Class<? extends DefaultApplicationContext> applicationContext() default DefaultApplicationContext.class;

        /**
         * @return the scope of the bean : one instance per context (singleton)
         *         or construction of a new instance on each request to the bean
         *         factory <br/>
         */
        public BeanScope scope() default BeanScope.SINGLETON;
    }

    /**
     * Scope of a bean. Indicates if a bean is a singleton on its context or <br/>
     * if a new instance should be constructed on each request to the factory. <br/>
     * 
     * @author Alexandre de Pellegrin
     * 
     */
    public enum BeanScope {

        SINGLETON, PROTOTYPE
    }

    /**
     * Annotation used to mark methods to call on JVM shutdown
     * 
     * @author Alexandre de Pellegrin
     * 
     */
    @Target({ METHOD })
    @Retention(value = RetentionPolicy.RUNTIME)
    public @interface PostConstruct {
    }

    /**
     * Annotation used to mark methods to call on JVM shutdown
     * 
     * @author Alexandre de Pellegrin
     * 
     */
    @Target({ METHOD })
    @Retention(value = RetentionPolicy.RUNTIME)
    public @interface PreDestroy {
    }

    /**
     * Just an unchecked exception you can trap if you want
     * 
     * @author Alexandre de Pellegrin
     * 
     */
    public static class ManiocException extends RuntimeException {

        public ManiocException(String msg, Throwable t) {
            super(msg, t);
        }

        public ManiocException(String msg) {
            super(msg);
        }
    }
}
