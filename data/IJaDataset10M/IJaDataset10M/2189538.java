package org.jmetis.reflection.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jmetis.kernel.metadata.IClassDescription;
import org.jmetis.kernel.metadata.IMetaDataIntrospector;
import org.jmetis.kernel.metadata.IMetaDataRegistry;
import org.jmetis.reflection.Primitives;

/**
 * {@code MetaDataIntrospector}
 * 
 * @author aerlach
 */
public class MetaDataIntrospector implements IMetaDataIntrospector {

    private static final String GET_PREFIX = "get";

    private static final String SET_PREFIX = "set";

    private static final String IS_PREFIX = "is";

    private static final Map<String, Method[]> EMPTY_PROPERTY_METHODS = Collections.emptyMap();

    private static final Type[] EMPTY_TYPE_ARRAY = {};

    private IMetaDataRegistry metaDataRegistry;

    /**
	 * Constructs a new {@code MetaDataIntrospector} instance.
	 * 
	 */
    public MetaDataIntrospector(IMetaDataRegistry metaDataRegistry) {
        super();
        this.metaDataRegistry = metaDataRegistry;
    }

    public IClassDescription classDescriptorOf(Class<?> classToDescribe) {
        return metaDataRegistry.classDescriptorOf(classToDescribe);
    }

    public Field[] declaredFieldsOf(final Class<?> declaringClass) throws SecurityException {
        if (System.getSecurityManager() != null) {
            return AccessController.doPrivileged(new PrivilegedAction<Field[]>() {

                public Field[] run() {
                    return declaringClass.getDeclaredFields();
                }
            });
        }
        return declaringClass.getDeclaredFields();
    }

    protected Field basicDeclaredFieldNamed(Class<?> declaringClass, String fieldName) throws NoSuchFieldException {
        NoSuchFieldException exception = null;
        Class<?> currentClass = declaringClass;
        while (currentClass != null) {
            try {
                Field field = declaringClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException ex) {
                if (exception == null) {
                    exception = ex;
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        if (exception != null) {
            throw exception;
        }
        return null;
    }

    public Field declaredFieldNamed(final Class<?> declaringClass, final String fieldName) throws SecurityException, NoSuchFieldException {
        if (System.getSecurityManager() != null) {
            try {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<Field>() {

                    public Field run() throws SecurityException, NoSuchFieldException {
                        return MetaDataIntrospector.this.basicDeclaredFieldNamed(declaringClass, fieldName);
                    }
                });
            } catch (PrivilegedActionException ex) {
                Throwable rootCause = ex.getCause();
                if (rootCause instanceof NoSuchFieldException) {
                    throw (NoSuchFieldException) rootCause;
                } else if (rootCause instanceof SecurityException) {
                    throw (SecurityException) rootCause;
                } else if (rootCause instanceof RuntimeException) {
                    throw (RuntimeException) rootCause;
                }
                ex.printStackTrace();
            }
        }
        return basicDeclaredFieldNamed(declaringClass, fieldName);
    }

    protected Method[] basicDeclaredMethodsOf(Class<?> declaringClass) {
        if (declaringClass.isInterface()) {
            return declaringClass.getMethods();
        }
        return declaringClass.getDeclaredMethods();
    }

    public Method[] declaredMethodsOf(final Class<?> declaringClass) throws SecurityException {
        if (System.getSecurityManager() != null) {
            return AccessController.doPrivileged(new PrivilegedAction<Method[]>() {

                public Method[] run() {
                    return MetaDataIntrospector.this.basicDeclaredMethodsOf(declaringClass);
                }
            });
        }
        return basicDeclaredMethodsOf(declaringClass);
    }

    public Method[] allDeclaredMethodsOf(Class<?> declaringClass) {
        List<Method> declaredMethods = new ArrayList<Method>();
        while (declaringClass != null) {
            for (Method method : declaredMethodsOf(declaringClass)) {
                declaredMethods.add(method);
            }
            declaringClass = declaringClass.getSuperclass();
        }
        return declaredMethods.toArray(new Method[declaredMethods.size()]);
    }

    /**
	 * Return <code>true</code> if the supplied '<code>candidateMethod</code>'
	 * can be consider a validate candidate for the {@link Method} that is
	 * {@link Method#isBridge() bridged} by the supplied {@link Method bridge
	 * Method}. This method performs inexpensive checks and can be used quickly
	 * filter for a set of possible matches.
	 */
    protected boolean isBridgedCandidateFor(Method candidateMethod, Method bridgeMethod) {
        return !candidateMethod.isBridge() && !candidateMethod.equals(bridgeMethod) && candidateMethod.getName().equals(bridgeMethod.getName()) && candidateMethod.getParameterTypes().length == bridgeMethod.getParameterTypes().length;
    }

    /**
	 * Search for the generic {@link Method} declaration whose erased signature
	 * matches that of the supplied bridge method.
	 * 
	 * @throws IllegalStateException
	 *             if the generic declaration cannot be found
	 */
    protected Method findGenericDeclaration(Method bridgeMethod) {
        Class<?> superclass = bridgeMethod.getDeclaringClass().getSuperclass();
        while (!Object.class.equals(superclass)) {
            Method method = Primitives.searchForMatch(superclass, bridgeMethod);
            if (method != null && !method.isBridge()) {
                return method;
            }
            superclass = superclass.getSuperclass();
        }
        Class<?>[] interfaces = Primitives.allInterfacesOf(bridgeMethod.getDeclaringClass());
        for (Class<?> anInterface : interfaces) {
            Method method = Primitives.searchForMatch(anInterface, bridgeMethod);
            if (method != null && !method.isBridge()) {
                return method;
            }
        }
        return null;
    }

    /**
	 * Determine whether or not the bridge {@link Method} is the bridge for the
	 * supplied candidate {@link Method}.
	 */
    protected boolean isBridgeMethodFor(Method bridgeMethod, Method candidateMethod, Map<TypeVariable<?>, Type> typeVariableMap) {
        if (isResolvedTypeMatch(candidateMethod, bridgeMethod, typeVariableMap)) {
            return true;
        }
        Method method = findGenericDeclaration(bridgeMethod);
        return method != null ? Primitives.isResolvedTypeMatch(method, candidateMethod, typeVariableMap) : false;
    }

    /**
	 * Search for the bridged method in the given candidates.
	 * 
	 * @param candidateMethods
	 *            the List of candidate Methods
	 * @param bridgeMethod
	 *            the bridge method
	 * @return the bridged method, or <code>null</code> if none found
	 */
    protected Method searchCandidates(List<Method> candidateMethods, Method bridgeMethod) {
        Map<TypeVariable<?>, Type> typeParameterMap = Primitives.createTypeVariableMap(bridgeMethod.getDeclaringClass());
        for (int i = 0; i < candidateMethods.size(); i++) {
            Method candidateMethod = candidateMethods.get(i);
            if (isBridgeMethodFor(bridgeMethod, candidateMethod, typeParameterMap)) {
                return candidateMethod;
            }
        }
        return null;
    }

    /**
	 * Return the original method for the given {@code bridgedMethod}.
	 * <p>
	 * It is safe to call this method passing in a non-bridge {@link Method}
	 * instance. In such a case, the supplied {@link Method} instance is
	 * returned directly to the caller. Callers are <strong>not</strong>
	 * required to check for bridging before calling this method.
	 * </p>
	 * 
	 * @throws IllegalStateException
	 *             if no bridged {@link Method} can be found
	 */
    public Method findBridgedMethod(Method bridgeMethod) {
        if (!bridgeMethod.isBridge()) {
            return bridgeMethod;
        }
        List<Method> candidateMethods = new ArrayList<Method>();
        Method[] methods = allDeclaredMethodsOf(bridgeMethod.getDeclaringClass());
        for (Method candidateMethod : methods) {
            if (isBridgedCandidateFor(candidateMethod, bridgeMethod)) {
                candidateMethods.add(candidateMethod);
            }
        }
        Method result;
        if (candidateMethods.size() == 1) {
            result = candidateMethods.get(0);
        } else {
            result = searchCandidates(candidateMethods, bridgeMethod);
        }
        return result;
    }

    protected String propertyNameFrom(Method method, int startIndex) {
        String methodName = method.getName();
        char[] characters = methodName.toCharArray();
        characters[startIndex] = Character.toLowerCase(characters[startIndex]);
        return new String(characters, startIndex, characters.length - startIndex);
    }

    private Method[] propertyMethodsFrom(String propertyName, Map<String, Method[]> collectedMethods) {
        Method[] methods = collectedMethods.get(propertyName);
        if (methods == null) {
            methods = new Method[2];
            collectedMethods.put(propertyName, methods);
        }
        return methods;
    }

    public Map<String, Method[]> propertyMethodsOf(Class<?> declaringClass) {
        if (declaringClass.isAnnotation() || declaringClass.isAnonymousClass() || declaringClass.isArray() || declaringClass.isEnum() || declaringClass.isInterface() || declaringClass.isPrimitive()) {
            return MetaDataIntrospector.EMPTY_PROPERTY_METHODS;
        }
        Method[] declaredMethods = declaredMethodsOf(declaringClass);
        Map<String, Method[]> allPropertyMethods = new HashMap<String, Method[]>(declaredMethods.length * 2);
        for (Method method : declaredMethods) {
            int modifiers = method.getModifiers();
            if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
                Class<?> resultType = method.getReturnType();
                String methodName = method.getName();
                if (methodName.length() >= 3) {
                    Method[] propertyMethods = null;
                    if (resultType == void.class) {
                        if (methodName.startsWith(MetaDataIntrospector.SET_PREFIX)) {
                            propertyMethods = propertyMethodsFrom(propertyNameFrom(method, 3), allPropertyMethods);
                            propertyMethods[1] = method;
                        }
                    } else if (methodName.startsWith(MetaDataIntrospector.GET_PREFIX)) {
                        propertyMethods = propertyMethodsFrom(propertyNameFrom(method, 3), allPropertyMethods);
                        propertyMethods[0] = method;
                    } else if ((resultType == boolean.class || resultType == Boolean.class) && methodName.startsWith(MetaDataIntrospector.IS_PREFIX)) {
                        propertyMethods = propertyMethodsFrom(propertyNameFrom(method, 2), allPropertyMethods);
                        propertyMethods[0] = method;
                    }
                }
            }
        }
        return allPropertyMethods;
    }

    public Annotation[] declaredAnnotationsOf(final AnnotatedElement annotatedElement) {
        if (System.getSecurityManager() != null) {
            return AccessController.doPrivileged(new PrivilegedAction<Annotation[]>() {

                public Annotation[] run() {
                    return annotatedElement.getDeclaredAnnotations();
                }
            });
        }
        return annotatedElement.getDeclaredAnnotations();
    }

    public <T extends Annotation> List<Method> annotatedMethodsFrom(Class<T> annotationClass, Class<?> type) {
        Class<?> currentType = type;
        List<Method> annotatedMethods = new ArrayList<Method>();
        while (currentType != null) {
            for (Method method : currentType.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotationClass)) {
                    annotatedMethods.add(method);
                }
            }
            currentType = currentType.getSuperclass();
        }
        return annotatedMethods;
    }

    public Type[] actualTypeArgumentsOf(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return parameterizedType.getActualTypeArguments();
        }
        return MetaDataIntrospector.EMPTY_TYPE_ARRAY;
    }

    /**
	 * Determine the raw type for the given generic parameter type.
	 */
    private Type getRawType(Type genericType, Map<TypeVariable<?>, Type> typeVariableMap) {
        if (genericType instanceof TypeVariable) {
            TypeVariable<?> tv = (TypeVariable<?>) genericType;
            Type result = typeVariableMap.get(tv);
            return result != null ? result : Object.class;
        } else if (genericType instanceof ParameterizedType) {
            return ((ParameterizedType) genericType).getRawType();
        } else {
            return genericType;
        }
    }

    protected Map<TypeVariable<?>, Type> addTypeVariableTo(TypeVariable<?> typeVariable, Type type, Map<TypeVariable<?>, Type> typeVariableMap) {
        if (typeVariableMap == null || typeVariableMap.isEmpty()) {
            typeVariableMap = new HashMap<TypeVariable<?>, Type>();
        }
        typeVariableMap.put(typeVariable, type);
        return typeVariableMap;
    }

    /**
	 * Read the {@link TypeVariable TypeVariables} from the supplied
	 * {@link ParameterizedType} and add mappings corresponding to the
	 * {@link TypeVariable#getName TypeVariable name} -> concrete type to the
	 * supplied {@link Map}.
	 * <p>
	 * Consider this case:
	 * 
	 * <pre class="code> public interface Foo&lt;S, T&gt; { .. }
	 * 
	 * public class FooImpl implements Foo&lt;String, Integer&gt; { .. } </pre>
	 * 
	 * For '<code>FooImpl</code>' the following mappings would be added to the
	 * {@link Map}: {S=java.lang.String, T=java.lang.Integer}.
	 */
    protected Map<TypeVariable<?>, Type> addTypeVariablesOfParameterizedTypeTo(ParameterizedType parameterizedType, Map<TypeVariable<?>, Type> typeVariableMap) {
        Type rawType = parameterizedType.getRawType();
        if (rawType instanceof Class) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            TypeVariable<?>[] typeParameters = ((Class<?>) rawType).getTypeParameters();
            for (int i = 0; i < actualTypeArguments.length; i++) {
                Type actualTypeArgument = actualTypeArguments[i];
                TypeVariable<?> typeVariable = typeParameters[i];
                if (actualTypeArgument instanceof Class) {
                    typeVariableMap = addTypeVariableTo(typeVariable, actualTypeArgument, typeVariableMap);
                } else if (actualTypeArgument instanceof GenericArrayType) {
                    typeVariableMap = addTypeVariableTo(typeVariable, actualTypeArgument, typeVariableMap);
                } else if (actualTypeArgument instanceof ParameterizedType) {
                    typeVariableMap = addTypeVariableTo(typeVariable, ((ParameterizedType) actualTypeArgument).getRawType(), typeVariableMap);
                } else if (actualTypeArgument instanceof TypeVariable) {
                    TypeVariable<?> typeVariableArgument = (TypeVariable<?>) actualTypeArgument;
                    Type resolvedType = typeVariableMap.get(typeVariableArgument);
                    if (resolvedType == null) {
                        resolvedType = extractClassForTypeVariable(typeVariableArgument);
                    }
                    if (resolvedType != null) {
                        typeVariableMap = addTypeVariableTo(typeVariable, resolvedType, typeVariableMap);
                    }
                }
            }
        }
        return typeVariableMap;
    }

    protected Map<TypeVariable<?>, Type> addTypeVariablesOfInterfaceTo(Type genericInterface, Map<TypeVariable<?>, Type> typeVariableMap) {
        if (genericInterface instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
            typeVariableMap = addTypeVariablesOfParameterizedTypeTo(parameterizedType, typeVariableMap);
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class) {
                typeVariableMap = addTypeVariablesOfInterfacesTo((Class<?>) rawType, typeVariableMap);
            }
        } else if (genericInterface instanceof Class) {
            typeVariableMap = addTypeVariablesOfInterfacesTo((Class<?>) genericInterface, typeVariableMap);
        }
        return typeVariableMap;
    }

    protected Map<TypeVariable<?>, Type> addTypeVariablesOfInterfacesTo(Class<?> classToDescribe, Map<TypeVariable<?>, Type> typeVariableMap) {
        for (Type genericInterface : classToDescribe.getGenericInterfaces()) {
            typeVariableMap = addTypeVariablesOfInterfaceTo(genericInterface, typeVariableMap);
        }
        return typeVariableMap;
    }

    protected Map<TypeVariable<?>, Type> addTypeVariablesOfSuperclassesTo(Class<?> classToDescribe, Map<TypeVariable<?>, Type> typeVariableMap) {
        Type genericType = classToDescribe.getGenericSuperclass();
        if (genericType instanceof Type) {
            Class<?> currentClass = classToDescribe.getSuperclass();
            while (!Object.class.equals(currentClass)) {
                if (genericType instanceof ParameterizedType) {
                    typeVariableMap = addTypeVariablesOfParameterizedTypeTo((ParameterizedType) genericType, typeVariableMap);
                }
                typeVariableMap = addTypeVariablesOfInterfacesTo(currentClass, typeVariableMap);
                genericType = currentClass.getGenericSuperclass();
                currentClass = currentClass.getSuperclass();
            }
        }
        return typeVariableMap;
    }

    protected Map<TypeVariable<?>, Type> addTypeVariablesOfEnclosingClassTo(Class<?> classToDescribe, Map<TypeVariable<?>, Type> typeVariableMap) {
        while (classToDescribe.isMemberClass()) {
            Type genericType = classToDescribe.getGenericSuperclass();
            if (genericType instanceof ParameterizedType) {
                typeVariableMap = addTypeVariablesOfParameterizedTypeTo((ParameterizedType) genericType, typeVariableMap);
            }
            classToDescribe = classToDescribe.getEnclosingClass();
        }
        return typeVariableMap;
    }

    public Map<TypeVariable<?>, Type> typeVariableMapOf(Class<?> classToDescribe) {
        Map<TypeVariable<?>, Type> typeVariableMap = Collections.emptyMap();
        typeVariableMap = addTypeVariablesOfInterfacesTo(classToDescribe, typeVariableMap);
        typeVariableMap = addTypeVariablesOfSuperclassesTo(classToDescribe, typeVariableMap);
        typeVariableMap = addTypeVariablesOfEnclosingClassTo(classToDescribe, typeVariableMap);
        return typeVariableMap;
    }

    /**
	 * Return <code>true</code> if the {@link Type} signature of both the
	 * supplied {@link Method#getGenericParameterTypes() generic Method} and
	 * concrete {@link Method} are equal after resolving all
	 * {@link TypeVariable TypeVariables} using the supplied
	 * {@link #createTypeVariableMap TypeVariable Map}, otherwise returns
	 * <code>false</code>.
	 */
    protected boolean isResolvedTypeMatch(Method genericMethod, Method candidateMethod, Map<TypeVariable<?>, Type> typeVariableMap) {
        Type[] genericParameters = genericMethod.getGenericParameterTypes();
        Class<?>[] candidateParameters = candidateMethod.getParameterTypes();
        if (genericParameters.length != candidateParameters.length) {
            return false;
        }
        for (int i = 0; i < genericParameters.length; i++) {
            Type genericParameter = genericParameters[i];
            Class<?> candidateParameter = candidateParameters[i];
            if (candidateParameter.isArray()) {
                Type rawType = getRawType(genericParameter, typeVariableMap);
                if (rawType instanceof GenericArrayType) {
                    if (!candidateParameter.getComponentType().equals(getRawType(((GenericArrayType) rawType).getGenericComponentType(), typeVariableMap))) {
                        return false;
                    }
                    break;
                }
            }
            if (!candidateParameter.equals(getRawType(genericParameter, typeVariableMap))) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Extracts the bound '<code>Class</code>' for a give {@link TypeVariable}.
	 */
    private Class<?> extractClassForTypeVariable(TypeVariable<?> typeVariable) {
        Type[] bounds = typeVariable.getBounds();
        Type result = null;
        if (bounds.length > 0) {
            Type bound = bounds[0];
            if (bound instanceof ParameterizedType) {
                result = ((ParameterizedType) bound).getRawType();
            } else if (bound instanceof Class) {
                result = bound;
            } else if (bound instanceof TypeVariable) {
                result = extractClassForTypeVariable((TypeVariable<?>) bound);
            }
        }
        return result instanceof Class ? (Class<?>) result : null;
    }

    /**
	 * @param aType
	 *            The container type.
	 * @return Returns the component type of a container type.
	 */
    protected Class<?> calculateComponentType(Type aType) {
        if (aType instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) aType).getActualTypeArguments();
            assert actualTypeArguments.length > 0 : "actualTypeArguments.length > 0";
            if (actualTypeArguments[0] instanceof Class) {
                return (Class<?>) actualTypeArguments[0];
            } else if (actualTypeArguments[0] instanceof WildcardType) {
                Type[] upperBounds = ((WildcardType) actualTypeArguments[0]).getUpperBounds();
                if (upperBounds != null && upperBounds.length > 0 && upperBounds[0] != Object.class && upperBounds[0] instanceof Class<?>) {
                    return (Class<?>) upperBounds[0];
                }
                assert true;
            }
        } else if (aType instanceof TypeVariable) {
        } else if (aType instanceof GenericArrayType) {
            Type genericComponentType = ((GenericArrayType) aType).getGenericComponentType();
            if (genericComponentType instanceof Class) {
                return (Class<?>) genericComponentType;
            }
        } else if (aType instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) aType).getUpperBounds();
            if (upperBounds != null && upperBounds.length > 0 && upperBounds[0] != Object.class) {
                return (Class<?>) upperBounds[0];
            }
            assert true;
        } else {
            assert false : "Unreachable code";
        }
        return null;
    }
}
