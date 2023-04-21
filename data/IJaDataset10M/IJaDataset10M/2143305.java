package org.apache.tapestry5.ioc.internal;

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.def.*;
import org.apache.tapestry5.ioc.internal.services.JustInTimeObjectCreator;
import org.apache.tapestry5.ioc.internal.util.*;
import org.apache.tapestry5.ioc.services.*;
import org.slf4j.Logger;
import java.io.ObjectStreamException;
import java.io.Serializable;
import static java.lang.String.format;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

public class ModuleImpl implements Module {

    private final InternalRegistry registry;

    private final ServiceActivityTracker tracker;

    private final ModuleDef2 moduleDef;

    private final ClassFactory classFactory;

    private final Logger logger;

    /**
     * Lazily instantiated.  Access is guarded by BARRIER.
     */
    private Object moduleInstance;

    private boolean insideConstructor;

    /**
     * Keyed on fully qualified service id; values are instantiated services (proxies). Guarded by BARRIER.
     */
    private final Map<String, Object> services = CollectionFactory.newCaseInsensitiveMap();

    private final Map<String, ServiceDef2> serviceDefs = CollectionFactory.newCaseInsensitiveMap();

    /**
     * The barrier is shared by all modules, which means that creation of *any* service for any module is single
     * threaded.
     */
    private static final ConcurrentBarrier BARRIER = new ConcurrentBarrier();

    public ModuleImpl(InternalRegistry registry, ServiceActivityTracker tracker, ModuleDef moduleDef, ClassFactory classFactory, Logger logger) {
        this.registry = registry;
        this.tracker = tracker;
        this.moduleDef = InternalUtils.toModuleDef2(moduleDef);
        this.classFactory = classFactory;
        this.logger = logger;
        for (String id : moduleDef.getServiceIds()) {
            ServiceDef sd = moduleDef.getServiceDef(id);
            ServiceDef2 sd2 = InternalUtils.toServiceDef2(sd);
            serviceDefs.put(id, sd2);
        }
    }

    public <T> T getService(String serviceId, Class<T> serviceInterface) {
        Defense.notBlank(serviceId, "serviceId");
        Defense.notNull(serviceInterface, "serviceInterface");
        ServiceDef2 def = getServiceDef(serviceId);
        assert def != null;
        Object service = findOrCreate(def, null);
        try {
            return serviceInterface.cast(service);
        } catch (ClassCastException ex) {
            throw new RuntimeException(IOCMessages.serviceWrongInterface(serviceId, def.getServiceInterface(), serviceInterface));
        }
    }

    public Set<DecoratorDef> findMatchingDecoratorDefs(ServiceDef serviceDef) {
        Set<DecoratorDef> result = CollectionFactory.newSet();
        for (DecoratorDef def : moduleDef.getDecoratorDefs()) {
            if (def.matches(serviceDef)) result.add(def);
        }
        return result;
    }

    public Set<AdvisorDef> findMatchingServiceAdvisors(ServiceDef serviceDef) {
        Set<AdvisorDef> result = CollectionFactory.newSet();
        for (AdvisorDef def : moduleDef.getAdvisorDefs()) {
            if (def.matches(serviceDef)) result.add(def);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Collection<String> findServiceIdsForInterface(Class serviceInterface) {
        Defense.notNull(serviceInterface, "serviceInterface");
        Collection<String> result = CollectionFactory.newList();
        for (ServiceDef2 def : serviceDefs.values()) {
            if (serviceInterface.isAssignableFrom(def.getServiceInterface())) result.add(def.getServiceId());
        }
        return result;
    }

    /**
     * Locates the service proxy for a particular service (from the service definition).
     *
     * @param def              defines the service
     * @param eagerLoadProxies collection into which proxies for eager loaded services are added (or null)
     * @return the service proxy
     */
    private Object findOrCreate(final ServiceDef2 def, final Collection<EagerLoadServiceProxy> eagerLoadProxies) {
        final String key = def.getServiceId();
        final Invokable create = new Invokable() {

            public Object invoke() {
                Object result = services.get(key);
                if (result == null) {
                    result = create(def, eagerLoadProxies);
                    services.put(key, result);
                }
                return result;
            }
        };
        Invokable find = new Invokable() {

            public Object invoke() {
                Object result = services.get(key);
                if (result == null) result = BARRIER.withWrite(create);
                return result;
            }
        };
        return BARRIER.withRead(find);
    }

    public void collectEagerLoadServices(final Collection<EagerLoadServiceProxy> proxies) {
        Runnable work = new Runnable() {

            public void run() {
                for (ServiceDef2 def : serviceDefs.values()) {
                    if (def.isEagerLoad()) findOrCreate(def, proxies);
                }
            }
        };
        registry.run("Eager loading services", work);
    }

    /**
     * Creates the service and updates the cache of created services.
     *
     * @param eagerLoadProxies a list into which any eager loaded proxies should be added
     */
    private Object create(final ServiceDef2 def, final Collection<EagerLoadServiceProxy> eagerLoadProxies) {
        final String serviceId = def.getServiceId();
        final Logger logger = registry.getServiceLogger(serviceId);
        String description = IOCMessages.creatingService(serviceId);
        if (logger.isDebugEnabled()) logger.debug(description);
        final Module module = this;
        Invokable operation = new Invokable() {

            public Object invoke() {
                try {
                    ServiceBuilderResources resources = new ServiceResourcesImpl(registry, module, def, classFactory, logger);
                    ObjectCreator creator = def.createServiceCreator(resources);
                    Class serviceInterface = def.getServiceInterface();
                    if (!serviceInterface.isInterface()) return creator.createObject();
                    creator = new OperationTrackingObjectCreator(registry, "Invoking " + creator.toString(), creator);
                    creator = new LifecycleWrappedServiceCreator(registry, def.getServiceScope(), resources, creator);
                    if (!def.isPreventDecoration()) {
                        creator = new AdvisorStackBuilder(def, creator, getAspectDecorator(), registry);
                        creator = new InterceptorStackBuilder(def, creator, registry);
                    }
                    creator = new RecursiveServiceCreationCheckWrapper(def, creator, logger);
                    creator = new OperationTrackingObjectCreator(registry, "Realizing service " + serviceId, creator);
                    JustInTimeObjectCreator delegate = new JustInTimeObjectCreator(tracker, creator, serviceId);
                    Object proxy = createProxy(resources, delegate);
                    registry.addRegistryShutdownListener(delegate);
                    if (def.isEagerLoad() && eagerLoadProxies != null) eagerLoadProxies.add(delegate);
                    tracker.setStatus(serviceId, Status.VIRTUAL);
                    return proxy;
                } catch (Exception ex) {
                    throw new RuntimeException(IOCMessages.errorBuildingService(serviceId, def, ex), ex);
                }
            }
        };
        return registry.invoke(description, operation);
    }

    private AspectDecorator getAspectDecorator() {
        return registry.invoke("Obtaining AspectDecorator service", new Invokable<AspectDecorator>() {

            public AspectDecorator invoke() {
                return registry.getService(AspectDecorator.class);
            }
        });
    }

    private final Runnable instantiateModule = new Runnable() {

        public void run() {
            moduleInstance = registry.invoke("Constructing module class " + moduleDef.getBuilderClass().getName(), new Invokable() {

                public Object invoke() {
                    return instantiateModuleInstance();
                }
            });
        }
    };

    private final Invokable provideModuleInstance = new Invokable<Object>() {

        public Object invoke() {
            if (moduleInstance == null) BARRIER.withWrite(instantiateModule);
            return moduleInstance;
        }
    };

    public Object getModuleBuilder() {
        return BARRIER.withRead(provideModuleInstance);
    }

    private Object instantiateModuleInstance() {
        Class moduleClass = moduleDef.getBuilderClass();
        Constructor[] constructors = moduleClass.getConstructors();
        if (constructors.length == 0) throw new RuntimeException(IOCMessages.noPublicConstructors(moduleClass));
        if (constructors.length > 1) {
            Comparator<Constructor> comparator = new Comparator<Constructor>() {

                public int compare(Constructor c1, Constructor c2) {
                    return c2.getParameterTypes().length - c1.getParameterTypes().length;
                }
            };
            Arrays.sort(constructors, comparator);
            logger.warn(IOCMessages.tooManyPublicConstructors(moduleClass, constructors[0]));
        }
        Constructor constructor = constructors[0];
        if (insideConstructor) throw new RuntimeException(IOCMessages.recursiveModuleConstructor(moduleClass, constructor));
        ObjectLocator locator = new ObjectLocatorImpl(registry, this);
        Map<Class, Object> resourcesMap = CollectionFactory.newMap();
        resourcesMap.put(Logger.class, logger);
        resourcesMap.put(ObjectLocator.class, locator);
        resourcesMap.put(OperationTracker.class, registry);
        InjectionResources resources = new MapInjectionResources(resourcesMap);
        Throwable fail = null;
        try {
            insideConstructor = true;
            Object[] parameterValues = InternalUtils.calculateParameters(locator, resources, constructor.getParameterTypes(), constructor.getGenericParameterTypes(), constructor.getParameterAnnotations(), registry);
            Object result = constructor.newInstance(parameterValues);
            InternalUtils.injectIntoFields(result, locator, resources, registry);
            return result;
        } catch (InvocationTargetException ex) {
            fail = ex.getTargetException();
        } catch (Exception ex) {
            fail = ex;
        } finally {
            insideConstructor = false;
        }
        throw new RuntimeException(IOCMessages.instantiateBuilderError(moduleClass, fail), fail);
    }

    private Object createProxy(ServiceResources resources, ObjectCreator creator) {
        String serviceId = resources.getServiceId();
        Class serviceInterface = resources.getServiceInterface();
        String toString = format("<Proxy for %s(%s)>", serviceId, serviceInterface.getName());
        return createProxyInstance(creator, serviceId, serviceInterface, toString);
    }

    private Object createProxyInstance(ObjectCreator creator, String serviceId, Class serviceInterface, String description) {
        ServiceProxyToken token = SerializationSupport.createToken(serviceId);
        ClassFab classFab = registry.newClass(serviceInterface);
        classFab.addField("creator", Modifier.PRIVATE | Modifier.FINAL, ObjectCreator.class);
        classFab.addField("token", Modifier.PRIVATE | Modifier.FINAL, ServiceProxyToken.class);
        classFab.addConstructor(new Class[] { ObjectCreator.class, ServiceProxyToken.class }, null, "{ creator = $1; token = $2; }");
        classFab.addInterface(Serializable.class);
        MethodSignature writeReplaceSig = new MethodSignature(Object.class, "writeReplace", null, new Class[] { ObjectStreamException.class });
        classFab.addMethod(Modifier.PRIVATE, writeReplaceSig, "return token;");
        String body = format("return (%s) creator.createObject();", serviceInterface.getName());
        MethodSignature sig = new MethodSignature(serviceInterface, "delegate", null, null);
        classFab.addMethod(Modifier.PRIVATE, sig, body);
        classFab.proxyMethodsToDelegate(serviceInterface, "delegate()", description);
        Class proxyClass = classFab.createClass();
        try {
            return proxyClass.getConstructors()[0].newInstance(creator, token);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public Set<ContributionDef> getContributorDefsForService(String serviceId) {
        Set<ContributionDef> result = CollectionFactory.newSet();
        for (ContributionDef def : moduleDef.getContributionDefs()) {
            if (def.getServiceId().equals(serviceId)) result.add(def);
        }
        return result;
    }

    public ServiceDef2 getServiceDef(String serviceId) {
        return serviceDefs.get(serviceId);
    }

    public String getLoggerName() {
        return moduleDef.getLoggerName();
    }

    @Override
    public String toString() {
        return String.format("ModuleImpl[%s]", moduleDef.getLoggerName());
    }
}
