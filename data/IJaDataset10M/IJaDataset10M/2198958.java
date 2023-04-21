package com.sitechasia.osgi.api_bridge;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.sf.cglib.Enhancer;
import net.sf.cglib.MethodInterceptor;
import net.sf.cglib.MethodProxy;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import com.sitechasia.osgi.api_bridge.exceptions.ApiBridgeException;
import com.sitechasia.osgi.api_bridge.exceptions.UntranslatableClassException;
import com.sitechasia.osgi.api_bridge.utilities.ClassUtils;

public class ApiBridge {

    public Object bridge(Object apiObject) {
        return bridge(apiObject, false);
    }

    public Object bridge(Object apiObject, String packageName) {
        return bridge(apiObject, packageName, false);
    }

    protected Object bridge(Object apiObject, String packageName, boolean returnSameObjectIfUnbridgable) {
        addPackageNameToApiPackageNamesArray(packageName);
        return bridge(apiObject, returnSameObjectIfUnbridgable);
    }

    protected Object bridge(Object apiObject, boolean returnSameObjectIfUnbridgable) {
        if (apiObject == null) {
            return null;
        }
        if (isEmptyOfApiPackageNamesArray()) throw new UntranslatableClassException(apiObject.getClass());
        if (apiObject.getClass().isArray()) {
            if (isOfApiPackages(apiObject.getClass().getComponentType())) {
                int arrayLength = Array.getLength(apiObject);
                Class<?> matchingComponentType = findClassMatch(apiObject.getClass().getComponentType());
                Object matchingArray = Array.newInstance(matchingComponentType, arrayLength);
                for (int i = 0; i < arrayLength; i++) {
                    Array.set(matchingArray, i, bridge(Array.get(apiObject, i)));
                }
                return matchingArray;
            } else {
                if (returnSameObjectIfUnbridgable) {
                    return apiObject;
                } else {
                    throw new UntranslatableClassException(apiObject.getClass());
                }
            }
        }
        Class<?> superClassMatch = findSuperClassMatch(apiObject.getClass());
        if (superClassMatch != null) {
            return createProxyForSuperClass(apiObject, superClassMatch);
        } else {
            Set<Class<?>> interfacesMatch = findIntefacesMatch(apiObject.getClass());
            if (interfacesMatch.size() > 0) {
                return createProxyForInterfaces(apiObject, interfacesMatch.toArray(new Class<?>[0]));
            } else {
                if (returnSameObjectIfUnbridgable) {
                    return apiObject;
                } else {
                    throw new UntranslatableClassException(apiObject.getClass());
                }
            }
        }
    }

    protected Object createProxyForSuperClass(Object apiObject, Class<?> superClassMatch) {
        final Delegator delegator = new Delegator(apiObject, this);
        MethodInterceptor invocationHandler = new MethodInterceptor() {

            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return delegator.invoke(obj, method, args);
            }
        };
        return Enhancer.enhance(superClassMatch, findIntefacesMatch(apiObject.getClass()).toArray(new Class<?>[0]), invocationHandler, getTargetApiClassLoader());
    }

    protected Object createProxyForInterfaces(Object apiObject, Class<?>[] interfacesMatch) {
        return java.lang.reflect.Proxy.newProxyInstance(getTargetApiClassLoader(), interfacesMatch, new Delegator(apiObject, this));
    }

    protected Set<Class<?>> findIntefacesMatch(Class<?> clazz) {
        Set<Class<?>> result = getCache().getInterfaceMap().get(clazz);
        if (result == null) {
            result = new HashSet<Class<?>>();
            for (Class<?> interfac : ClassUtils.getAllInterfaces(clazz)) {
                if (isOfApiPackages(interfac)) {
                    result.add(findClassMatch(interfac));
                }
            }
            getCache().getInterfaceMap().put(clazz, result);
        }
        return result;
    }

    protected Class<?> findSuperClassMatch(Class<?> clazz) {
        if (getCache().getSuperClassMap().containsKey(clazz)) {
            return getCache().getSuperClassMap().get(clazz);
        } else {
            Class<?> superClassMatch = null;
            clazz = clazz.getSuperclass();
            while (clazz != null) {
                if (isOfApiPackages(clazz)) {
                    superClassMatch = findClassMatch(clazz);
                    break;
                }
                clazz = clazz.getSuperclass();
            }
            getCache().getSuperClassMap().put(clazz, superClassMatch);
            return superClassMatch;
        }
    }

    protected Class<?> findClassMatch(Class<?> clazz) {
        try {
            return getTargetApiClassLoader().loadClass(clazz.getName());
        } catch (ClassNotFoundException ex) {
            throw new ApiBridgeException(String.format("Was not able to find a matching class '%s' in the target class loader", clazz.getName()), ex);
        }
    }

    protected boolean isOfApiPackages(Class<?> clazz) {
        return getApiPackageNames().contains(ClassUtils.getPackageName(clazz));
    }

    protected ApiBridge(ClassLoader targetApiClassLoader, String... packageNames) {
        this.targetApiClassLoader = targetApiClassLoader;
        if (ArrayUtils.isEmpty(packageNames)) {
            this.apiPackageNames = new HashSet<String>(0);
        } else {
            this.apiPackageNames = new HashSet<String>(Arrays.asList(packageNames));
        }
        this.apiPackageNamesArray = apiPackageNames.toArray(new String[0]);
    }

    private final ClassLoader targetApiClassLoader;

    public ClassLoader getTargetApiClassLoader() {
        return targetApiClassLoader;
    }

    private final Set<String> apiPackageNames;

    public Set<String> getApiPackageNames() {
        return apiPackageNames;
    }

    private String[] apiPackageNamesArray;

    public String[] getApiPackageNamesArray() {
        return apiPackageNamesArray;
    }

    private final ApiBridgeCache cache = new ApiBridgeCache();

    public ApiBridgeCache getCache() {
        return cache;
    }

    public static ApiBridge getApiBridge(ClassLoader targetApiClassLoader, String... packageNames) {
        if (targetApiClassLoader == null) targetApiClassLoader = Thread.currentThread().getContextClassLoader();
        String apiBridgeKey = formApiBridgeKey(targetApiClassLoader, packageNames);
        ApiBridge apiBridge = getApiBridges().get(apiBridgeKey);
        if (apiBridge == null) {
            apiBridge = new ApiBridge(targetApiClassLoader, packageNames);
            if (StringUtils.split(apiBridgeKey, '-').length > 1) apiBridges.put(apiBridgeKey, apiBridge);
        }
        return apiBridge;
    }

    public static ApiBridge getApiBridge() {
        return getApiBridge(null);
    }

    private static Map<String, ApiBridge> apiBridges = new HashMap<String, ApiBridge>();

    private static Map<String, ApiBridge> getApiBridges() {
        return apiBridges;
    }

    private static String formApiBridgeKey(ClassLoader targetApiClassLoader, String... packageNames) {
        StringBuffer result = new StringBuffer();
        result.append(targetApiClassLoader.hashCode());
        result.append('-');
        if (!ArrayUtils.isEmpty(packageNames)) for (String packageName : packageNames) {
            result.append(packageName.hashCode());
            result.append(':');
        }
        return result.toString();
    }

    private boolean isEmptyOfApiPackageNamesArray() {
        return ArrayUtils.isEmpty(getApiPackageNamesArray());
    }

    private void addPackageNameToApiPackageNamesArray(String packageName) {
        packageName = StringUtils.trimToEmpty(packageName);
        if (StringUtils.isNotEmpty(packageName)) {
            apiPackageNamesArray = null;
            getApiPackageNames().add(packageName);
            apiPackageNamesArray = getApiPackageNames().toArray(new String[0]);
        }
    }
}
