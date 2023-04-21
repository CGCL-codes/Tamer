package moxie;

abstract class ObjectInterception<T> extends Interception {

    private final Class[] constructorArgTypes;

    private final Object[] constructorArgs;

    protected T proxy;

    private ProxyFactory<T> proxyFactory;

    protected ObjectInterception(Class<T> clazz, String name, MoxieFlags flags, InstantiationStackTrace instantiationStackTrace, Class[] constructorArgTypes, Object[] constructorArgs) {
        super(clazz, name, flags, instantiationStackTrace);
        this.constructorArgTypes = constructorArgTypes;
        this.constructorArgs = constructorArgs;
    }

    T getProxy() {
        if (proxy == null) {
            proxy = getProxyFactory().createProxy(this, constructorArgTypes, constructorArgs);
        }
        return proxy;
    }

    ProxyFactory<T> getProxyFactory() {
        if (proxyFactory == null) {
            proxyFactory = ProxyFactory.create(clazz);
        }
        return proxyFactory;
    }

    Class[] getConstructorArgTypes() {
        return constructorArgTypes;
    }

    Object[] getConstructorArgs() {
        return constructorArgs;
    }

    ObjectExpectationImpl<T> expect() {
        return new ObjectExpectationImpl<T>(this);
    }

    ObjectCheckImpl<T> check() {
        return new ObjectCheckImpl<T>(this, invocations);
    }
}
