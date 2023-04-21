package com.googlecode.jtype;

import static com.googlecode.jtype.Utils.checkNotNull;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Factory for creating types.
 * 
 * @author Mark Hobson
 * @version $Id: Types.java 110 2011-11-23 17:19:43Z markhobson $
 * @see Type
 */
public final class Types {

    private static final WildcardType UNBOUNDED_WILDCARD_TYPE = wildcardType(null, null);

    private static final Pattern ARRAY_PATTERN = Pattern.compile("\\[\\s*\\]$");

    private static final Pattern UPPER_BOUND_PATTERN = Pattern.compile("^\\?\\s+extends\\s+");

    private static final Pattern LOWER_BOUND_PATTERN = Pattern.compile("^\\?\\s+super\\s+");

    private Types() {
        throw new AssertionError();
    }

    /**
	 * Creates a type variable for the specified declaration, name and bounds.
	 * 
	 * @param <D>
	 *            the type of generic declaration that declared the type variable
	 * @param declaration
	 *            the generic declaration that declared the type variable
	 * @param name
	 *            the name of the type variable
	 * @param bounds
	 *            the upper bounds of the type variable
	 * @return the type variable
	 */
    public static <D extends GenericDeclaration> TypeVariable<D> typeVariable(D declaration, String name, Type... bounds) {
        return new DefaultTypeVariable<D>(declaration, name, bounds);
    }

    /**
	 * Creates a generic array type for the specified component type.
	 * 
	 * @param componentType
	 *            the component type
	 * @return the generic array type
	 */
    public static GenericArrayType genericArrayType(Type componentType) {
        return new DefaultGenericArrayType(componentType);
    }

    /**
	 * Creates a parameterized type for the specified raw type and actual type arguments.
	 * 
	 * @param rawType
	 *            the raw type
	 * @param actualTypeArguments
	 *            the actual type arguments
	 * @return the parameterized type
	 * @throws MalformedParameterizedTypeException
	 *             if the raw type is not a parameterized type or the number of actual type arguments differs from those
	 *             defined on the raw type
	 */
    public static ParameterizedType parameterizedType(Class<?> rawType, Type... actualTypeArguments) {
        return new DefaultParameterizedType(null, rawType, actualTypeArguments);
    }

    /**
	 * Creates a parameterized type for the specified raw type with unbounded wildcard actual type arguments.
	 * 
	 * @param rawType
	 *            the raw type
	 * @return the parameterized type
	 * @throws MalformedParameterizedTypeException
	 *             if the raw type is not a parameterized type
	 */
    public static ParameterizedType unboundedParameterizedType(Class<?> rawType) {
        checkNotNull(rawType, "rawType");
        int typeParameterCount = rawType.getTypeParameters().length;
        Type[] actualTypeArguments = new Type[typeParameterCount];
        Arrays.fill(actualTypeArguments, unboundedWildcardType());
        return parameterizedType(rawType, actualTypeArguments);
    }

    /**
	 * Creates an unbounded wildcard type.
	 * 
	 * @return the wildcard type
	 */
    public static WildcardType unboundedWildcardType() {
        return UNBOUNDED_WILDCARD_TYPE;
    }

    /**
	 * Creates a wildcard type with the specified upper bound.
	 * 
	 * @param upperBound
	 *            the upper bound type
	 * @return the wildcard type
	 */
    public static WildcardType upperBoundedWildcardType(Type upperBound) {
        checkNotNull(upperBound, "upperBound");
        return wildcardType(new Type[] { upperBound }, null);
    }

    /**
	 * Creates a wildcard type with the specified lower bound.
	 * 
	 * @param lowerBound
	 *            the lower bound type
	 * @return the wildcard type
	 */
    public static WildcardType lowerBoundedWildcardType(Type lowerBound) {
        checkNotNull(lowerBound, "lowerBound");
        return wildcardType(null, new Type[] { lowerBound });
    }

    /**
	 * Returns a type that corresponds to the specified string.
	 * 
	 * @param typeName
	 *            the string to be parsed
	 * @return the type
	 */
    public static Type valueOf(String typeName) {
        return valueOf(typeName, (Set<String>) null);
    }

    /**
	 * Returns a type that corresponds to the specified string using the specified import context.
	 * 
	 * @param typeName
	 *            the string to be parsed
	 * @param imports
	 *            the fully qualified class names to use when an unqualified class name is encountered
	 * @return the type
	 * @throws IllegalArgumentException
	 *             if the import context contains duplicate entries for an unqualified class name
	 */
    public static Type valueOf(String typeName, Set<String> imports) {
        checkNotNull(typeName, "typeName");
        Map<String, String> importMap = createImportMap(imports);
        return valueOf(typeName, importMap);
    }

    private static WildcardType wildcardType(Type[] upperBounds, Type[] lowerBounds) {
        return new DefaultWildcardType(upperBounds, lowerBounds);
    }

    private static Map<String, String> createImportMap(Set<String> imports) {
        if (imports == null) {
            return Collections.emptyMap();
        }
        Map<String, String> importMap = new HashMap<String, String>();
        for (String className : imports) {
            String simpleClassName = ClassUtils.getSimpleClassName(className);
            if (importMap.containsKey(simpleClassName)) {
                throw new IllegalArgumentException("Duplicate imports: " + importMap.get(simpleClassName) + " and " + className);
            }
            importMap.put(simpleClassName, className);
        }
        return importMap;
    }

    private static Type valueOf(String typeName, Map<String, String> imports) {
        typeName = typeName.trim();
        Matcher arrayMatcher = ARRAY_PATTERN.matcher(typeName);
        if (arrayMatcher.find()) {
            String componentName = typeName.substring(0, arrayMatcher.start());
            Type componentType = valueOf(componentName, imports);
            return TypeUtils.getArrayType(componentType);
        }
        if (typeName.startsWith("?")) {
            return parseWildcardType(typeName, imports);
        }
        int argStart = typeName.indexOf('<');
        if (argStart == -1) {
            return parseClass(typeName, imports);
        }
        int argEnd = typeName.lastIndexOf('>');
        if (argEnd == -1) {
            throw new IllegalArgumentException("Mismatched type argument delimiters: " + typeName);
        }
        String rawTypeName = typeName.substring(0, argStart).trim();
        Class<?> rawType = parseClass(rawTypeName, imports);
        String[] actualTypeArgumentNames = typeName.substring(argStart + 1, argEnd).split(",");
        Type[] actualTypeArguments = new Type[actualTypeArgumentNames.length];
        for (int i = 0; i < actualTypeArgumentNames.length; i++) {
            actualTypeArguments[i] = valueOf(actualTypeArgumentNames[i], imports);
        }
        return parameterizedType(rawType, actualTypeArguments);
    }

    private static Class<?> parseClass(String className, Map<String, String> imports) {
        Class<?> klass = ClassUtils.valueOf(className);
        if (klass != null) {
            return klass;
        }
        if (!className.contains(".") && imports.containsKey(className)) {
            String qualifiedClassName = imports.get(className);
            klass = ClassUtils.valueOf(qualifiedClassName);
            if (klass != null) {
                return klass;
            }
        }
        throw new IllegalArgumentException("Class not found: " + className);
    }

    private static WildcardType parseWildcardType(String typeName, Map<String, String> imports) {
        Type[] upperBounds;
        Type[] lowerBounds;
        Matcher upperBoundMatcher;
        Matcher lowerBoundMatcher;
        if ("?".equals(typeName)) {
            upperBounds = null;
            lowerBounds = null;
        } else if ((upperBoundMatcher = UPPER_BOUND_PATTERN.matcher(typeName)).find()) {
            String upperBoundName = typeName.substring(upperBoundMatcher.end());
            Type upperBound = valueOf(upperBoundName, imports);
            upperBounds = new Type[] { upperBound };
            lowerBounds = null;
        } else if ((lowerBoundMatcher = LOWER_BOUND_PATTERN.matcher(typeName)).find()) {
            String lowerBoundName = typeName.substring(lowerBoundMatcher.end());
            Type lowerBound = valueOf(lowerBoundName, imports);
            upperBounds = null;
            lowerBounds = new Type[] { lowerBound };
        } else {
            throw new IllegalArgumentException("Invalid wildcard type: " + typeName);
        }
        return wildcardType(upperBounds, lowerBounds);
    }
}
