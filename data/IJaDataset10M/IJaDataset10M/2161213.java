package net.llando;

import java.util.HashMap;
import net.llando.containers.*;
import net.llando.exceptions.*;
import net.llando.requests.*;

public class TypeRegistry {

    HashMap<String, RegisteredType> typeMap;

    HashMap<Class<?>, RegisteredType> classMap = new HashMap<Class<?>, RegisteredType>();

    static TypeRegistry singleton;

    static {
        try {
            TypeRegistry.getInstance().register(Welcome.class);
            TypeRegistry.getInstance().register(PropertyGetter.class);
            TypeRegistry.getInstance().register(PropertySetter.class);
            TypeRegistry.getInstance().register(LazyPropertyStub.class);
            TypeRegistry.getInstance().register(ReleaseNotification.class);
            TypeRegistry.getInstance().register(Proxy.class);
            TypeRegistry.getInstance().register(Date.class);
            TypeRegistry.getInstance().register(ProxyReference.class);
            TypeRegistry.getInstance().register(ResolveRequest.class);
            TypeRegistry.getInstance().register(MutableArray.class);
            TypeRegistry.getInstance().register(MethodCall.class);
            TypeRegistry.getInstance().register(Response.class);
            TypeRegistry.getInstance().register(PeerConnection.class);
            TypeRegistry.getInstance().register(ObjectNotification.class);
            TypeRegistry.getInstance().register(EmptyService.class);
        } catch (IncompatibleClassException e) {
            Logger.getLogger().error("", e);
        }
    }

    public TypeRegistry() {
        typeMap = new HashMap<String, RegisteredType>();
    }

    public Class<?> classForTag(String llandoTag) {
        return typeForTag(llandoTag).getKlass();
    }

    public void register(Class<?> aClass) throws IncompatibleClassException {
        RegisteredType aType = new RegisteredType(aClass);
        typeMap.put(aType.classTag, aType);
        classMap.put(aClass, aType);
    }

    public void register(Class<?> aClass, String classTagAlias, boolean primitive) throws IncompatibleClassException {
        RegisteredType aType = new RegisteredType(aClass, classTagAlias, primitive, true);
        typeMap.put(aType.classTag, aType);
        classMap.put(aClass, aType);
    }

    public RegisteredType typeForTag(String llandoTag) {
        return typeMap.get(llandoTag);
    }

    @SuppressWarnings("unchecked")
    public RegisteredType typeForObject(Object obj) {
        RegisteredType result = null;
        Class klass = obj.getClass();
        do {
            result = classMap.get(klass);
            klass = klass.getSuperclass();
        } while (result == null && klass != null);
        return result;
    }

    public static TypeRegistry getInstance() {
        if (singleton == null) {
            singleton = new TypeRegistry();
        }
        return singleton;
    }
}
