package org.fest.reflect.reference;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.fest.reflect.exception.ReflectionError;

/**
 * Understands a references a generic type. Based on Neal Gafter's
 * <code><a href="http://gafter.blogspot.com/2006/12/super-type-tokens.html" target="_blank">TypeReference</a></code>.
 * @param <T> the generic type in this reference. 
 * 
 * @author crazybob@google.com (Bob Lee)
 * @author Alex Ruiz
 * 
 * @since 1.1
 */
public abstract class TypeRef<T> {

    private final Class<?> rawType;

    /**
   * Creates a new </code>{@link TypeRef}</code>.
   * @throws ReflectionError if the generic type of this reference is missing type parameter.
   */
    public TypeRef() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) throw new ReflectionError("Missing type parameter.");
        Type type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        rawType = type instanceof Class<?> ? (Class<?>) type : (Class<?>) ((ParameterizedType) type).getRawType();
    }

    /**
   * Returns the raw type of the generic type in this reference.
   * @return the raw type of the generic type in this reference.
   */
    public final Class<?> rawType() {
        return rawType;
    }
}
